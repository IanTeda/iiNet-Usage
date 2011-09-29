package au.id.teda.volumeusage.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.helper.AccountStatusHelper;
import au.id.teda.volumeusage.notification.DialogHelper;
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
	
	private CharSequence tickerText;
	private CharSequence notificationTitle;
	private CharSequence notificationText;
	
	private Context context;
	private Timer dataCollectionTimer;
	private URL xmlPath;
	long updateInterval;
	
	// Implemented method, do nothing as we are not binding service
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	// On creation of the service
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(INFO_TAG, "onCreate()");
		Context context = DataCollectionService.this; //TODO: Do i need this context??
        dataCollectionTimer = new Timer("DataCollectorTimer");
        dataCollectionTimer.schedule(dataCollectionTask, 1000L, 60 * 1000L); //TODO: Change 1000L to updateInterval after development
	}
	
	// On start load intent and start ID
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	// onStart Command is processed after onCreate but with intents
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	super.onStartCommand(intent, flags, startId);
    	Log.i(INFO_TAG, "onStartCommand()");
        
    	//Grab intent values
    	try { //TODO: This may belong somewhere else for prefrence change updates???
    		xmlPath = new URL(intent.getExtras().getString("xmlPath"));  // TODO: Try / catch really? Why?
    	} catch (MalformedURLException e) {
    		Log.e(INFO_TAG, "onStartCommand() > Bad URL", e);
    	}

    	updateInterval = (long) intent.getExtras().getInt("updateInterval");
        
        // We want this service to continue running until it is explicitly stopped,
    	// so return sticky.
		return START_STICKY;
    }
	
    // onDestroy shutdown task timer
	@Override
	public void onDestroy() {
		Log.i(INFO_TAG, "onDestroy()");
		super.onDestroy();
		if (dataCollectionTimer != null) {
			dataCollectionTimer.cancel();
		Log.i(INFO_TAG, "onDestroy() > dataCollectionTimer stopped");
		}
	}

	private TimerTask dataCollectionTask = new TimerTask() {
		
		@Override
		public void run() {
			Log.i(INFO_TAG, "dataCollectionTask");
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext());
			boolean backgroundUpdates = settings.getBoolean("background_updates", false);
			if (backgroundUpdates){
				Log.d(DEBUG_TAG, "dataCollectionTask > xmlPath: " + xmlPath + " & updateInterval: " + updateInterval);
			} else {
				Log.d(DEBUG_TAG, "dataCollectionTask > cancel timer");
				dataCollectionTimer.cancel();
			}
			//xmlParse(xmlPath);
			checkStatus();
		}
	};
    
	public void xmlParse(URL xmlPath) {
		Log.i(INFO_TAG, "xmlParse");
		
		try {
			//URL url = new URL("http://www.anddev.org/images/tut/basic/parsingxml/example.xml"); // Create a URL we want to load some xml-data from.
			InputSource is = new InputSource(MyApp.getAppContext().getResources().openRawResource(R.raw.adsl2  )); // Our developement xml file
			SAXParserFactory spf = SAXParserFactory.newInstance(); // Create a SAXParserFactory so we can
			SAXParser sp = spf.newSAXParser(); // Create a SAXParser so we can
			XMLReader xr = sp.getXMLReader(); // Create a XMLReader
			DailyUsageSAXHandler myAccountInfoSAXHandler = new DailyUsageSAXHandler(); // Create a new ContentHandler and apply it to the XML-Reader
			xr.setContentHandler(myAccountInfoSAXHandler);
			xr.parse(new InputSource(is.getByteStream())); // Parse the xml-data from our development file
			//xr.parse(new InputSource(url.openStream())); // Parse the xml-data from our URL.
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

}
