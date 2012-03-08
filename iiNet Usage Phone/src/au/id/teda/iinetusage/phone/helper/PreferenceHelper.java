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
	
	// Alert strings
	private static final String NO_USER_KEY = "noUser";
	private static final String NO_PASS_KEY = "noPass";
	private static final String NO_USER_PASS_KEY = "noUserPass";
	private static final String NO_DATA_KEY = "noData";
	private static final String PEAK_USAGE_KEY = "peakUsage";
	private static final String OFFPEAK_USAGE_KEY = "offpeakUsage";
	private static final String PEAK_OFFPEAK_USAGE_KEY = "peakOffpeakUsage";
	private static final String PEAK_QUOTA_KEY = "peakQuota";
	private static final String OFFPEAK_QUOTA_KEY = "offpeakQuota";
	private static final String PEAK_OFFPEAK_QUOTA_KEY = "peakOffpeakQuota";
	private static final String PEAK_QUOTA_OFFPEAK_USAGE_KEY = "peakQuotaOffpeakUsage";
	private static final String OFFPEAK_QUOTA_PEAK_USAGE_KEY = "offpeakQuotapeakUsage";
	
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
	
	public boolean isPeakQuotaOffpeakUsageClosed(){
		return mySettings.getBoolean(PEAK_QUOTA_OFFPEAK_USAGE_KEY, true);
	}
	
	public boolean isOffpeakQuotaPeakUsageClosed(){
		return mySettings.getBoolean(OFFPEAK_QUOTA_PEAK_USAGE_KEY, true);
	}
	
	public boolean isNoUserClosed(){
		return mySettings.getBoolean(NO_USER_KEY, true);
	}
	
	public boolean isNoPassClosed(){
		return mySettings.getBoolean(NO_PASS_KEY, true);
	}
	
	public boolean isNoUserPassClosed(){
		return mySettings.getBoolean(NO_USER_PASS_KEY, true);
	}
	
	public boolean isNoDataClosed(){
		return mySettings.getBoolean(NO_DATA_KEY, true);
	}
	
	public boolean isPeakUsageClosed(){
		return mySettings.getBoolean(PEAK_USAGE_KEY, true);
	}
	
	public boolean isOffpeakUsageClosed(){
		return mySettings.getBoolean(OFFPEAK_USAGE_KEY, true);
	}
	
	public boolean isPeakOffpeakUsageClosed(){
		return mySettings.getBoolean(PEAK_OFFPEAK_USAGE_KEY, true);
	}
	
	public boolean isPeakQuotaeClosed(){
		return mySettings.getBoolean(PEAK_QUOTA_KEY, true);
	}
	
	public boolean isOffpeakQuotaeClosed(){
		return mySettings.getBoolean(OFFPEAK_QUOTA_KEY, true);
	}
	
	public boolean isPeakOffpeakQuotaeClosed(){
		return mySettings.getBoolean(PEAK_OFFPEAK_QUOTA_KEY, true);
	}
	
	
	
	
	
	
	public void setPeakQuotaOffpeakUsageClosed(){
		myEditor.putBoolean(PEAK_QUOTA_OFFPEAK_USAGE_KEY, true);
		myEditor.commit();
	}
	
	public void setOffpeakQuotaPeakUsageClosed(){
		myEditor.putBoolean(OFFPEAK_QUOTA_PEAK_USAGE_KEY, true);
		myEditor.commit();
	}
	
	public void setNoUserClosed(){
		myEditor.putBoolean(NO_USER_KEY, true);
		myEditor.commit();
	}
	
	public void setNoPassClosed(){
		myEditor.putBoolean(NO_PASS_KEY, true);
		myEditor.commit();
	}
	
	public void setNoUserPassClosed(){
		myEditor.putBoolean(NO_USER_PASS_KEY, true);
		myEditor.commit();
	}
	
	public void setNoDataClosed(){
		myEditor.putBoolean(NO_DATA_KEY, true);
		myEditor.commit();
	}
	
	public void setPeakUsageClosed(){
		myEditor.putBoolean(PEAK_USAGE_KEY, true);
		myEditor.commit();
	}
	
	public void setOffpeakUsageClosed(){
		myEditor.putBoolean(OFFPEAK_USAGE_KEY, true);
		myEditor.commit();
	}
	
	public void setPeakOffpeakUsageClosed(){
		myEditor.putBoolean(PEAK_OFFPEAK_USAGE_KEY, true);
		myEditor.commit();
	}
	
	public void setPeakQuotaeClosed(){
		myEditor.putBoolean(PEAK_QUOTA_KEY, true);
		myEditor.commit();
	}
	
	public void setOffpeakQuotaeClosed(){
		myEditor.putBoolean(OFFPEAK_QUOTA_KEY, true);
		myEditor.commit();
	}
	
	public void setPeakOffpeakQuotaeClosed(){
		myEditor.putBoolean(PEAK_OFFPEAK_QUOTA_KEY, true);
		myEditor.commit();
	}
}
