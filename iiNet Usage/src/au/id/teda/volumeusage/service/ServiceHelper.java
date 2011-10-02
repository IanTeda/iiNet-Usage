package au.id.teda.volumeusage.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ServiceHelper {
	
	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = ServiceHelper.class.getSimpleName();
	
	private Intent serviceIntent;
	private Context context;
	
	private final static String BACKGROUND_UPDATES = "background_updates";
	private final static String UPDATE_INTERVAL = "updateInterval";
	private final static String PASSWORD = "iinet_password";
	private final static String USERNAME = "iinet_username";
	
	private final static String XML_PATH="xmlPath";
	
	// ServiceHelper constructor
	public ServiceHelper(Context context) {
		Log.i(INFO_TAG, "Constructor");
		this.context = context;
	}

	// Start the data collection service in backgroud
	public void startDataCollectionService(){
		Log.i(INFO_TAG, "startDataCollectionService()");
		
		// Get preferences for updating
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		boolean backgroundUpdates = settings.getBoolean(BACKGROUND_UPDATES, false);
		int updateIntervalMillisec = settings.getInt(UPDATE_INTERVAL, 86400000);
		
		//Log.d(DEBUG_TAG, "buildXMLPath() > buildXMLPath: " + buildXMLPath("current"));
		if (backgroundUpdates){
			serviceIntent = new Intent(context, DataCollectionService.class);
			serviceIntent.putExtra(XML_PATH, buildXMLPath());
			serviceIntent.putExtra(UPDATE_INTERVAL, updateIntervalMillisec);
			
			//Log.d(DEBUG_TAG, "startDataCollectionService() > Build XML Path: " + buildXMLPath() 
					//+ "update interval: " + updateIntervalMillisec);
			context.startService(serviceIntent);
		}
	}

	// Stop the data collection service running in backgroud
	public void stopDataCollectionService(){
		Log.i(INFO_TAG, "stopDataCollectionService()");
		context.stopService(serviceIntent);
	}
	
	// Restart (Stop/Start) data collection service
	public void restartDataCollectionService(){
		Log.i(INFO_TAG, "restartDataCollectionService()");
		stopDataCollectionService();
		startDataCollectionService();
	}
	
	// Build URL to fetch XML
	public String buildXMLPath(){
		//Log.i(INFO_TAG, "buildXMLPath()");
		String pathString = null;
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String myUsername = settings.getString(USERNAME, "defaultUsername");
		String myPassword = settings.getString(PASSWORD, "defaultPasswrd");
		
		pathString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
					"username=" + myUsername + 
					"&action=login" +
					"&password=" + myPassword;
		
		//Log.d(DEBUG_TAG, "buildXMLPath() > pathString: " + pathString);
		return pathString;
	}
	
	
}
