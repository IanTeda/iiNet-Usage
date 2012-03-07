package au.id.teda.iinetusage.phone.helper;

/**
 * Method for getting and setting application preferences
 * @author Ian
 *
 */
public class PreferenceHelper extends ConnectivityHelper {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = PreferenceHelper.class.getSimpleName();
	
	// Preference keys
	private static final String SHOW_REFRESH_DIALOG_KEY = "show_refresh_dialog";
	private static final String HIDE_STATUS_BAR_KEY = "hide_status_bar";
	private static final String PEAK_USAGE_ALERT_KEY = "peak_usage_alert";
	private static final String OFFPEAK_USAGE_ALERT_KEY = "offpeak_usage_alert";
	
	public boolean showRefreshDialog(){
		return mySettings.getBoolean(SHOW_REFRESH_DIALOG_KEY, true);
	}
	
	public boolean hidePhoneStatusBar(){
		return mySettings.getBoolean(HIDE_STATUS_BAR_KEY, true);
	}
	
	public int getPeakUsageAlertValue(){
		return mySettings.getInt(PEAK_USAGE_ALERT_KEY, 75);
	}
	
	public int getOffpeakUsageAlertValue(){
		return mySettings.getInt(OFFPEAK_USAGE_ALERT_KEY, 75);
	}
	
}
