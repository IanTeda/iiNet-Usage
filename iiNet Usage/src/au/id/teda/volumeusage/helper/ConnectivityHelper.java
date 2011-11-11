package au.id.teda.volumeusage.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * ConnectivityHelper for all things connection
 * @author Ian
 *
 */
public class ConnectivityHelper {
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = ConnectivityHelper.class.getSimpleName();
	
	private Context context;
	
	// Preference string key object
	private static final String WIFI_ONLY = "wifi_only";

	private SharedPreferences mySettings;

	
	/**
	 * Class constructor
	 * @param context
	 */
	public ConnectivityHelper(Context context) {
		Log.d(INFO_TAG, "ConnectivityHelper()");
		
		// Set context for class
		this.context = context;
		
		// Set preference object
		mySettings = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	/**
	 * Check for internet connection based on preferences and connectivity
	 * @return
	 */
	public boolean isConnected(){
		
		// Set connectivity manager object
		ConnectivityManager myConMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Get connection information
		NetworkInfo info = myConMan.getActiveNetworkInfo();
		
		// Skip if no connection, or background data disabled
		if (info == null || !myConMan.getBackgroundDataSetting()) { // TODO: Check for roaming settings
			return false;
		} else {
			 // Confirm 3G connectivity
		    Boolean is3g = myConMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		     // Confirm wifi connectivity
		    Boolean isWifi = myConMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
			
		    Log.d(DEBUG_TAG, "is3g: " + is3g);
		    Log.d(DEBUG_TAG, "isWifi: " + isWifi);
		    Log.d(DEBUG_TAG, "Wifi Only: " + mySettings.getBoolean(WIFI_ONLY, false));
		    
		    if (is3g && !mySettings.getBoolean(WIFI_ONLY, false)) {
		    	Log.d(DEBUG_TAG, "3G and non wifi connectin allowed");
		    	return true;
		    	
		    } else if(isWifi && mySettings.getBoolean(WIFI_ONLY, false)) {
		    	Log.d(DEBUG_TAG, "3G and non wifi connectin allowed");
		    	return true;
		    	
		    } else {
		    	
		    	return false;
		    }
		}
		
	}

}
