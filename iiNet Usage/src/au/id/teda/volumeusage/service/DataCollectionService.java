package au.id.teda.volumeusage.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.helper.AccountStatusHelper;
import au.id.teda.volumeusage.notification.NotificationHelper;
import au.id.teda.volumeusage.sax.DailyUsageSAXHandler;

// http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/LocalService.html
// http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.html
// http://mindtherobot.com/blog/37/android-architecture-tutorial-developing-an-app-with-a-background-service-using-ipc/

public class DataCollectionService extends Service {
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = DataCollectionService.class.getSimpleName();
	
	private final static String PEAK = "peak";
	private final static String OFFPEAK = "offpeak";
	
	private boolean notifyAlertPeak = true;
	private static final int ALERT_PEAK_ID = 1;
	private boolean notifyAlertOffpeak = true;
	private static final int ALERT_OFFPEAK_ID = 2;
	private boolean notifyOverPeak = true;
	private static final int OVER_PEAK_ID = 3;
	private boolean notifyOverOffpeak = true;
	private static final int OVER_OFFPEAK_ID = 4;
	
	private static final String SHOW = "show";
	private static final String DISMISS = "dismiss";
	
	private final static String XML_PATH="xmlPath";
	private final static String UPDATE_INTERVAL = "updateInterval";
	private final static String BACKGROUND_UPDATES = "background_updates";
	
	private CharSequence tickerText;
	private CharSequence notificationTitle;
	private CharSequence notificationText;
	
	private Context context;
	private URL url;
	private long updateInterval;
	
	private Timer dataCollectionTimer = new Timer();
	
	// onStart Command is processed after onCreate but with intents
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	super.onStartCommand(intent, flags, startId);
    	Log.i(INFO_TAG, "onStartCommand()");
        
    	//Grab intent values
    	try { //TODO: This may belong somewhere else for prefrence change updates???
    		url = new URL(intent.getExtras().getString(XML_PATH));  // TODO: Try / catch really? Why?
    	} catch (MalformedURLException e) {
    		Log.e(INFO_TAG, "onStartCommand() > Bad URL", e);
    	}

    	updateInterval = (long) intent.getExtras().getInt(UPDATE_INTERVAL);
    	
    	Log.d(DEBUG_TAG, "onStartCommand() > Update Interval: " + updateInterval + " URL: " + url);
    	
        startDataCollectionService();
        
         // We want this service to continue running until it is explicitly stopped,
    	// so return sticky.
		return START_STICKY;
    }
    
    // onDestroy shutdown task timer
	@Override
	public void onDestroy() {
		Log.i(INFO_TAG, "onDestroy()");
		super.onDestroy();
		endDataCollectionService();
	}
    
    public void startDataCollectionService(){
    	Log.d(DEBUG_TAG, "Start timer");
	    
    	dataCollectionTimer.scheduleAtFixedRate( new DataCollectionTask(), // Task to run
    			updateInterval, // Intial delay before starting task
    			updateInterval); // Delay between running task
    }
    
	public void endDataCollectionService(){
        if (dataCollectionTimer != null)
        	dataCollectionTimer.cancel();
	}
	
    class DataCollectionTask extends TimerTask {

    	@Override
    	public void run() {
    		Log.d(DEBUG_TAG, "startDataCollectionService() > Update Interval: " + updateInterval + " URL: " + url);
    		
    	}
    	
    }

    
	/**private TimerTask dataCollectionTask = new TimerTask() {
		
		@Override
		public void run() {
			Log.i(INFO_TAG, "dataCollectionTask");
			
	    	Log.d(DEBUG_TAG, "Timer values: " + updateInterval);
	    	
			
			// Get preference for background updates
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext());
			boolean backgroundUpdates = settings.getBoolean(BACKGROUND_UPDATES, false);
			
			// Double check background services enabled
			if (backgroundUpdates){
				
				// Lets parse our XML file then
				xmlParse(url);
				
				// Check for phone status bar notification cases
				checkStatus();
				
			} else {
				Log.d(DEBUG_TAG, "dataCollectionTask > cancel timer");
				dataCollectionTimer.cancel();
			}
			
		}
	};**/



	public void xmlParse(URL url) {
		Log.i(INFO_TAG, "xmlParse");
		
		try {
			
			//InputSource is = new InputSource(MyApp.getAppContext().getResources().openRawResource(R.raw.adsl2  )); // Our developement xml file
			
			// Create a SAXParserFactory so we can
			SAXParserFactory spf = SAXParserFactory.newInstance();
			
			// Create a SAXParser so we can
			SAXParser sp = spf.newSAXParser();
			
			 // Create a XMLReader
			XMLReader xr = sp.getXMLReader();
			
			// Create a new ContentHandler and apply it to the XML-Reader
			DailyUsageSAXHandler myAccountInfoSAXHandler = new DailyUsageSAXHandler(); 
			xr.setContentHandler(myAccountInfoSAXHandler);
			//xr.parse(new InputSource(is.getByteStream())); // Parse the xml-data from our development file
			
			 // Parse the xml-data from our URL.
			xr.parse(new InputSource(url.openStream()));
			
		} catch (Exception e) {
			// Display any Error to catLog
			Log.d(DEBUG_TAG, "XML Querry error: " + e.getMessage());
		}
	}
	
	
	// Check usage status after update and notify user if approaching quota settings or over
	public void checkStatus(){
		
		//TODO: Add usage value and percentage to alert
		
		AccountStatusHelper accoutStatusHelper = new AccountStatusHelper(this);
		NotificationHelper notify = new NotificationHelper(this);
		
			if (accoutStatusHelper.usageAlert(PEAK) && notifyAlertPeak){
				Log.d(DEBUG_TAG, "Peak alert");
				
				// Set notification
				tickerText = getString(R.string.alert_peak_ticker);
				notificationTitle = getString(R.string.alert_peak_title);
				notificationText = getString(R.string.alert_peak_text);
				
				// Notify user
				notify.usageNotification(tickerText, notificationTitle, notificationText, ALERT_PEAK_ID);
				
				// Notify only once
				notifyAlertPeak = false;
			}
			
			if (accoutStatusHelper.usageAlert(OFFPEAK) && notifyAlertOffpeak){
				Log.d(DEBUG_TAG, "Offpeak alert");
				
				// Set notification
				tickerText = getString(R.string.alert_offpeak_ticker);
				notificationTitle = getString(R.string.alert_offpeak_title);
				notificationText = getString(R.string.alert_offpeak_text);
				
				// Notify user
				notify.usageNotification(tickerText, notificationTitle, notificationText, ALERT_OFFPEAK_ID);
				
				// Notify only once
				notifyAlertOffpeak = false;
			}
			
			if (accoutStatusHelper.usageOver(PEAK) && notifyOverPeak){
				Log.d(DEBUG_TAG, "Peak over");
				
				// Set notification
				tickerText = getString(R.string.over_peak_ticker);
				notificationTitle = getString(R.string.over_peak_title);
				notificationText = getString(R.string.over_peak_text);
				
				// Notify user
				notify.usageNotification(tickerText, notificationTitle, notificationText, OVER_PEAK_ID);
				
				// Notify only once
				notifyOverPeak = false;
			}
			
			if (accoutStatusHelper.usageOver(OFFPEAK) && notifyOverOffpeak){
				Log.d(DEBUG_TAG, "Offpeak over");
				
				// Set notification
				tickerText = getString(R.string.over_offpeak_ticker);
				notificationTitle = getString(R.string.over_offpeak_title);
				notificationText = getString(R.string.over_offpeak_text);
				
				// Notify user
				notify.usageNotification(tickerText, notificationTitle, notificationText, OVER_OFFPEAK_ID);
				
				// Notify only once
				notifyOverOffpeak = false;
			}
		
			
			
	}

	// Implemented method, do nothing as we are not binding service
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}