package au.id.teda.iinetusage.phone.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import au.id.teda.iinetusage.phone.AppGlobals;

/**
 * ConnectivityHelper for all things connection
 * 
 * @author Ian
 * 
 */
public class ConnectivityHelper extends UserPassHelper {

	//private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = ConnectivityHelper.class.getSimpleName();

	// Preference string key object
	private static final String WIFI_ONLY = "wifi_only";

	public boolean isWifiOnly() {
		return mySettings.getBoolean(WIFI_ONLY, false);
	}

	/**
	 * Check for Internet connection based on preferences and connectivity
	 * 
	 * @return
	 */
	public boolean isConnected() {

		// Set connectivity manager object
		ConnectivityManager myConMan = (ConnectivityManager) AppGlobals
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		// Get connection information
		NetworkInfo netInfo = myConMan.getActiveNetworkInfo();

		// Skip if no connection manager, or background data disabled
		if (netInfo == null || !myConMan.getBackgroundDataSetting()) {

			// Looks like we have airplane mode on or background roaming is not
			// allowed
			return false;

			// Else we ware good to check for connectivity
		} else {
			// Get 3G connectivity
			Boolean is3g = myConMan.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
			// Get WiFi connectivity
			Boolean isWifi = myConMan.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

			//Log.d(DEBUG_TAG, "is3g: " + is3g + " | isWifi: " + isWifi + " | Wifi Only: " + isWifiOnly());

			// Check to see if we have WiFi
			if (isWifi) {

				// Looks like we have WiFi connectivity
				return true;

				// Check to see if we have 3G and not WiFi only
			} else if (is3g && !isWifiOnly()) {

				// Looks like we have 3G and not set to WiFi only
				return true;

			} else {

				Log.v(INFO_TAG, "Looks like we don't have an internet connection");
				// Looks like we have no connectivity
				return false;
			}

		}

	}

}
