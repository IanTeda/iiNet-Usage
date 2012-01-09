package au.id.teda.iinetusage.phone.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.AppGlobals;
import au.id.teda.iinetusage.phone.R;

/**
 * ConnectivityHelper for all things connection
 * @author Ian
 *
 */
public class ConnectivityHelper extends UserPassHelper {
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = ConnectivityHelper.class.getSimpleName();
	
	// Preference string key object
	private static final String WIFI_ONLY = "wifi_only";
	
	private Context myContext;
	
	/**
	 * Class constructor
	 */
	public ConnectivityHelper() {
		
		// Set context for class
		myContext = AppGlobals.getAppContext();
		
	}
	
	public boolean isWifiOnly(){
		return mySettings.getBoolean(WIFI_ONLY, false);
	}
	
	/**
	 * Check for Internet connection based on preferences and connectivity
	 * @return
	 */
	public boolean isConnected(){
		
		// Set connectivity manager object
		ConnectivityManager myConMan = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
			
		    //Log.d(DEBUG_TAG, "is3g: " + is3g);
		    //Log.d(DEBUG_TAG, "isWifi: " + isWifi);
		    //Log.d(DEBUG_TAG, "Wifi Only: " + mySettings.getBoolean(WIFI_ONLY, false));
		    
		    if (is3g && !isWifiOnly()) {
		    	//Log.d(DEBUG_TAG, "3G and non wifi connection allowed");
		    	Log.i(INFO_TAG, "isConnected(): true");
		    	return true;
		    	
		    } else if(isWifi && isWifiOnly()) {
		    	//Log.d(DEBUG_TAG, "3G and non wifi connectin allowed");
		    	Log.i(INFO_TAG, "isConnected(): true");
		    	return true;
		    	
		    } else {
		    	Log.i(INFO_TAG, "isConnected(): false");
		    	Toast.makeText(myContext, myContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
		    	return false;
		    }
		}
		
	}

}
