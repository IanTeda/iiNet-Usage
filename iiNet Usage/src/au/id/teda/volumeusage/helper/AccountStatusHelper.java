package au.id.teda.volumeusage.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.volumeusage.MyApp;

public class AccountStatusHelper extends AccountInfoHelper {
	
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AccountStatusHelper.class.getSimpleName();
	
	// Create instance of shared preferences based on app context
	private SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext());
	
	
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
	
	private final static String PEAK = "peak";
	private final static String OFFPEAK = "offpeak";


	public void setAccoutStatus(long systemDateTime, String period,
			long anniversary, long days_so_far, long days_to_go,
			int peak_shaped, int offpeak_shaped, long peak_used,
			long offpeak_used, long upload_used, long freezone_used,
			long peak_shaped_speed, long offpeak_shaped_speed, long uptime,
			String ip_address) {
		
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
		
		myEditor.commit();
		
		Log.i(INFO_TAG, "Current status set");
	}

	public Long getLastUpdate(){
		return mySettings.getLong(LAST_UPDATE, 0);
	}
	
	public String getCurrentDataPeriod(){
		return mySettings.getString(CURRENT_DATA_PERIOD, "Current data period not set");
	}

	public Long getAnniversary(){
		return mySettings.getLong(CURRENT_ANNIVERSARY, 0);
	}
	
	public Long getCurrentDaysToGo(){
		return mySettings.getLong(CURRENT_DAYS_TO_GO, 0);
	}

	public Long getCurrentDaysSoFar(){
		return mySettings.getLong(CURRENT_DAYS_SO_FAR, 0);
	}

	public int getCurrentPeakShaped(){
		return mySettings.getInt(CURRENT_PEAK_SHAPED, 0);
	}
	
	public int getCurrentOffPeakShaped(){
		return mySettings.getInt(CURRENT_OFF_PEAK_SHAPED, 0);
	}

	public Long getCurrentPeakUsed(){
		return mySettings.getLong(CURRENT_PEAK_USED, 0);
	}
	
	public Long getCurrentOffPeakUsed(){
		return mySettings.getLong(CURRENT_OFF_PEAK_USED, 0);
	}

	public Long getCurrentUploadUsed(){
		return mySettings.getLong(CURRENT_UPLOAD_USED, 0);
	}

	public Long getCurrentFreezoneUsed(){
		return mySettings.getLong(CURRENT_FREEZONE_USED, 0);
	}
	
	public Long getCurrentPeakShapedSpeed(){
		return mySettings.getLong(CURRENT_PEAK_SHAPED_SPEED, 0);
	}
	
	public Long getCurrentOffPeakShapedSpeed(){
		return mySettings.getLong(CURRENT_OFF_PEAK_SHAPED_SPEED, 0);
	}

	public Long getCurrentUpTime(){
		return mySettings.getLong(CURRENT_UP_TIME, 0);
	}

	public String getCurrentIp(){
		return mySettings.getString(CURRENT_IP, "0.0.0.0.0");
	}
}
