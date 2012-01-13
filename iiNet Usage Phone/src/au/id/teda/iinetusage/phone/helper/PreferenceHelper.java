package au.id.teda.iinetusage.phone.helper;

/**
 * Method for getting and setting application preferences
 * @author Ian
 *
 */
public class PreferenceHelper extends ConnectivityHelper {
	
	// Preference keys
	private static final String SHOW_REFRESH_DIALOG = "show_refresh_dialog";
	
	
	public boolean showRefreshDialog(){
		return mySettings.getBoolean(SHOW_REFRESH_DIALOG, false);
	}
	

}
