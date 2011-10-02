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
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.helper.AccountStatusHelper;
import au.id.teda.volumeusage.notification.NotificationHelper;
import au.id.teda.volumeusage.sax.DailyUsageSAXHandler;

// http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/LocalService.html
// http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.html
// http://mindtherobot.com/blog/37/android-architecture-tutorial-developing-an-app-with-a-background-service-using-ipc/

/**
 * This class is used as a background service for check account data usage and showing
 * status bar notifications if data goes over user set alert or quota
 * 
 * @author Ian Teda
 * @version: Alpha 1.4
 *
 */


public class DataCollectionService extends Service {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = DataCollectionService.class.getSimpleName();
	
	// String values for retrieving current usage
	private final static String PEAK = "peak";
	private final static String OFFPEAK = "offpeak";
	
	// Booleans so we only display status bar alerts once
	private boolean notifyAlertPeak = true;
	private boolean notifyAlertOffpeak = true;
	private boolean notifyOverPeak = true;
	private boolean notifyOverOffpeak = true;
	
	// Unique static message id's in case we want to manipulate message
	private static final int ALERT_PEAK_ID = 1;
	private static final int ALERT_OFFPEAK_ID = 2;
	private static final int OVER_PEAK_ID = 3;
	private static final int OVER_OFFPEAK_ID = 4;
	
	// Static string variables for getting values out of service intent call
	private final static String XML_PATH = "xmlPath";
	private final static String UPDATE_INTERVAL = "updateInterval";
	
	// Objects for storing service intent values
	private URL url;
	private long updateInterval;
	
	// Objects for status bar notifications
	private CharSequence tickerText;
	private CharSequence notificationTitle;
	private CharSequence notificationText;
	
	// Timer object
	private Timer dataCollectionTimer = new Timer();
	
	/**
	 *  This override method gets values stored in service intent used to start service
	 *  onStartCommand is processed after onCreate but with intents
	 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	super.onStartCommand(intent, flags, startId);
    	Log.i(INFO_TAG, "onStartCommand()");
        
    	try { 
    		//Grab intent values
    		url = new URL(intent.getExtras().getString(XML_PATH)); 
    		updateInterval = (long) intent.getExtras().getInt(UPDATE_INTERVAL);
        	//Log.d(DEBUG_TAG, "onStartCommand() > Update Interval: " + updateInterval + " URL: " + url);
    		
    	} catch (MalformedURLException e) {
    		Log.e(INFO_TAG, "onStartCommand() > Bad URL", e);
    		// TODO: What other errors could there be??
    		
    	}

    	// Call method for starting service
        startDataCollectionService();
        
        // We want this service to continue running until it is explicitly stopped, so return sticky.
		return START_STICKY;
    }
    
    // onDestroy shutdown task timer
    /**
     * This override method calls the endDataCollectionService.
     */
	@Override
	public void onDestroy() {
		Log.i(INFO_TAG, "onDestroy()");
		super.onDestroy();
		endDataCollectionService();
	}
    
	/**
	 * Method for starting background service timer and a fixed scheduled rate
	 */
    public void startDataCollectionService(){
    	//Log.d(DEBUG_TAG, "Start timer");
	    
    	dataCollectionTimer.scheduleAtFixedRate( new DataCollectionTask(), // Task to run
    			updateInterval, // Intial delay before starting task
    			updateInterval); // Delay between running task
    }
    
    /**
     * Method for ending the background service timer
     */
	public void endDataCollectionService(){
        if (dataCollectionTimer != null)
        	dataCollectionTimer.cancel();
	}
	
	/**
	 * Timer task run on each scheduled run
	 * @author Ian
	 *
	 */
    class DataCollectionTask extends TimerTask {

    	@Override
    	public void run() {
    		Log.i(INFO_TAG, "DataCollectionTask > run()");
    		//Log.d(DEBUG_TAG, "startDataCollectionService() > Update Interval: " + updateInterval + " URL: " + url);
    		
			// Lets parse our XML file then
			xmlParse(url);
			
			// Check for phone status bar notification cases
			checkStatus();
    		
    	}
    	
    }

    /**
     * Method calling parsing objects
     * @param url
     */
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
			Log.d(INFO_TAG, "XML Querry error: " + e.getMessage());
		}
	}
	
	/**
	 * Method for checking if we need to display and displaying status bar alerts
	 */
	public void checkStatus(){
		
		AccountStatusHelper accoutStatusHelper = new AccountStatusHelper(this);
		NotificationHelper notify = new NotificationHelper(this);
		
			if (accoutStatusHelper.usageAlert(PEAK) && notifyAlertPeak){
				//Log.d(DEBUG_TAG, "Peak alert");
				
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
				//Log.d(DEBUG_TAG, "Offpeak alert");
				
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
				//Log.d(DEBUG_TAG, "Peak over");
				
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
				//Log.d(DEBUG_TAG, "Offpeak over");
				
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

	/**
	 * Implemented method, but do nothing as we are not binding this service
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}