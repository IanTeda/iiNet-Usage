package au.id.teda.iinetusage.phone.helper;

/**
 * Method for getting and setting application preferences
 * @author Ian
 *
 */
public class PreferenceHelper extends ConnectivityHelper {
	
	// Preference keys
	private static final String SHOW_REFRESH_DIALOG = "show_refresh_dialog";
	private static final String HIDE_STATUS_BAR = "hide_status_bar";
	
	
	public boolean showRefreshDialog(){
		return mySettings.getBoolean(SHOW_REFRESH_DIALOG, true);
	}
	
	public boolean hidePhoneStatusBar(){
		return mySettings.getBoolean(HIDE_STATUS_BAR, true);
	}
	
}
