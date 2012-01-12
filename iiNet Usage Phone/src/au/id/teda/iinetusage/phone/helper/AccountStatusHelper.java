package au.id.teda.iinetusage.phone.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import au.id.teda.iinetusage.phone.AppGlobals;

public class AccountStatusHelper extends AccountInfoHelper {
	
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = AccountStatusHelper.class.getSimpleName();
	
	// Create instance of shared preferences based on app context
	private SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(AppGlobals.getAppContext());
	
	
	// Set static string values for preference keys
	private final static String LAST_UPDATE = "lastUpdate";
	private final static String CURRENT_DATA_PERIOD = "currentDataPeriod";
	private final static String CURRENT_ANNIVERSARY = "currentAnniversary";
	private final static String CURRENT_DAYS_SO_FAR = "currentDaysSoFare";
	private final static String CURRENT_DAYS_TO_GO = "currentDaysToGo";
	private final static String CURRENT_PEAK_SHAPED = "currentPeakShaped";
	private final static String CURRENT_OFF_PEAK_SHAPED = "currentOffPeakShaped";
	private final static String CURRENT_PEAK_USED = "currentPeakUsed";
	private final static String CURRENT_OFF_PEAK_USED = "currentOffPeakUsed";
	private final static String CURRENT_UPLOAD_USED = "currentUploadUsed";
	private final static String CURRENT_FREEZONE_USED = "currentFreezoneUsed";
	private final static String CURRENT_PEAK_SHAPED_SPEED = "currentPeakShapedSpeed";
	private final static String CURRENT_OFF_PEAK_SHAPED_SPEED = "currentOffPeakShapedSpeed";
	private final static String CURRENT_UP_TIME = "currentUpTime";
	private final static String CURRENT_IP = "CurrentIp";

	/**
	 * Method for setting current account status
	 * 
	 * @param systemDateTime
	 * @param period
	 * @param anniversary
	 * @param days_so_far
	 * @param days_to_go
	 * @param peak_shaped
	 * @param offpeak_shaped
	 * @param peak_used
	 * @param offpeak_used
	 * @param upload_used
	 * @param freezone_used
	 * @param peak_shaped_speed
	 * @param offpeak_shaped_speed
	 * @param uptime
	 * @param ip_address
	 */
	public void setAccoutStatus(long systemDateTime, String period,
			long anniversary, long days_so_far, long days_to_go,
			int peak_shaped, int offpeak_shaped, long peak_used,
			long offpeak_used, long upload_used, long freezone_used,
			long peak_shaped_speed, long offpeak_shaped_speed, long uptime,
			String ip_address) {
		
		// Set object for editor
		SharedPreferences.Editor myEditor = mySettings.edit();
		
		myEditor.putLong(LAST_UPDATE, systemDateTime);
		myEditor.putString(CURRENT_DATA_PERIOD, period);
		myEditor.putLong(CURRENT_ANNIVERSARY, anniversary);
		myEditor.putLong(CURRENT_DAYS_SO_FAR, days_so_far);
		myEditor.putLong(CURRENT_DAYS_TO_GO, days_to_go);
		myEditor.putInt(CURRENT_PEAK_SHAPED, peak_shaped);
		myEditor.putInt(CURRENT_OFF_PEAK_SHAPED, offpeak_shaped);
		myEditor.putLong(CURRENT_PEAK_USED, peak_used);
		myEditor.putLong(CURRENT_OFF_PEAK_USED, offpeak_used);
		myEditor.putLong(CURRENT_UPLOAD_USED, upload_used);
		myEditor.putLong(CURRENT_FREEZONE_USED, freezone_used);
		myEditor.putLong(CURRENT_PEAK_SHAPED_SPEED, peak_shaped_speed);
		myEditor.putLong(CURRENT_OFF_PEAK_SHAPED_SPEED, offpeak_shaped_speed);
		myEditor.putLong(CURRENT_UP_TIME, uptime);
		myEditor.putString(CURRENT_IP, ip_address);
		// Commit values to preferences
		myEditor.commit();
		
	}

	/**
	 * Method for getting last update
	 * @return
	 */
	public Long getLastUpdate(){
		return mySettings.getLong(LAST_UPDATE, 0);
	}
	
	/**
	 * Method for getting current data period
	 * @return
	 */
	public String getCurrentDataPeriod(){
		return mySettings.getString(CURRENT_DATA_PERIOD, "Current data period not set");
	}

	/**
	 * Method for getting current roll over (anniversary) date
	 * @return
	 */
	public Long getCurrentAnniversary(){
		return mySettings.getLong(CURRENT_ANNIVERSARY, 0);
	}
	
	/**
	 * Method for getting current days to go
	 * @return
	 */
	public Long getCurrentDaysToGo(){
		return mySettings.getLong(CURRENT_DAYS_TO_GO, 0);
	}

	/**
	 * Method for getting current days so far
	 * @return
	 */
	public Long getCurrentDaysSoFar(){
		return mySettings.getLong(CURRENT_DAYS_SO_FAR, 0);
	}

	/**
	 * Method for getting current peak shape speed
	 * @return
	 */
	public int isCurrentPeakShaped(){
		return mySettings.getInt(CURRENT_PEAK_SHAPED, 0);
	}
	
	/**
	 * Method for checking if current off peak is shaped
	 * @return
	 */
	public int isCurrentOffPeakShaped(){
		return mySettings.getInt(CURRENT_OFF_PEAK_SHAPED, 0);
	}

	/**
	 * Method for checking if current peak is shaped
	 * @return
	 */
	public Long getCurrentPeakUsed(){
		return mySettings.getLong(CURRENT_PEAK_USED, 0);
	}
	
	/**
	 * Method for getting current off peak data used to date
	 * @return
	 */
	public Long getCurrentOffPeakUsed(){
		return mySettings.getLong(CURRENT_OFF_PEAK_USED, 0);
	}

	/**
	 * Method for geting current uploaded data used to date
	 * @return
	 */
	public Long getCurrentUploadUsed(){
		return mySettings.getLong(CURRENT_UPLOAD_USED, 0);
	}

	/**
	 * Method for getting current freezone date used to date
	 * @return
	 */
	public Long getCurrentFreezoneUsed(){
		return mySettings.getLong(CURRENT_FREEZONE_USED, 0);
	}
	
	/**
	 * Method for getting current peak shaped speed
	 * @return
	 */
	public Long getCurrentPeakShapedSpeed(){
		return mySettings.getLong(CURRENT_PEAK_SHAPED_SPEED, 0);
	}
	
	/**
	 * Method for getting current off peak shaped speed
	 * @return
	 */
	public Long getCurrentOffPeakShapedSpeed(){
		return mySettings.getLong(CURRENT_OFF_PEAK_SHAPED_SPEED, 0);
	}

	/**
	 * Method for getting current up time of broadband connection
	 * @return
	 */
	public Long getCurrentUpTime(){
		return mySettings.getLong(CURRENT_UP_TIME, 0);
	}

	/**
	 * Method for returning current ip address of broadband connection
	 * @return
	 */
	public String getCurrentIp(){
		return mySettings.getString(CURRENT_IP, "0.0.0.0.0");
	}
}
