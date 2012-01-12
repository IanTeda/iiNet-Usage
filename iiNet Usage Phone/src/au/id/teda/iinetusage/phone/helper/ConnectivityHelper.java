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
	
	private Context myActivityContext;
	
	/**
	 * Class constructor
	 */
	public ConnectivityHelper(Context activityContext) {
		
		// Set activity context
		myActivityContext = activityContext;
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
		ConnectivityManager myConMan = (ConnectivityManager) myActivityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Get connection information
		NetworkInfo netInfo = myConMan.getActiveNetworkInfo();
		
		// Skip if no connection manager, or background data disabled
		if (netInfo == null || !myConMan.getBackgroundDataSetting()) {
			
			// Looks like we have airplane mode on or background roaming is not allowed
			return false;
			
		// Else we ware good to check for connectivity
		} else {
			// Get 3G connectivity
		    Boolean is3g = myConMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		    // Get WiFi connectivity
		    Boolean isWifi = myConMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
			
		    Log.d(DEBUG_TAG, "is3g: " + is3g + " | isWifi: " + isWifi + " | Wifi Only: " + isWifiOnly());
		    
		    // Check to see if we have WiFi
		    if (isWifi){
		    	
		    	// Looks like we have WiFi connectivity
		    	return true;
		    
		    // Check to see if we have 3G and not WiFi only
		    } else if (is3g && !isWifiOnly()){
		    	
		    	// Looks like we have 3G and not set to WiFi only
		    	return true;
		    	
		    } else {
		    	
		    	// Looks like we have no connectivity
		    	return false;
		    }
		    
		}
		
	}

}
