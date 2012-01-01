package au.id.teda.iinetusage.phone.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import au.id.teda.iinetusage.phone.AppGlobals;

public class UserPassHelper {
	
	// Set shared preferences object
	private SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(AppGlobals.getAppContext());
	private SharedPreferences.Editor myEditor = mySettings.edit();

	/**
	 * Methods for getting, setting and checking username and password
	 */
	// Static strings for preference keys
	private final static String PASSWORD = "iinet_password";
	private final static String USERNAME = "iinet_username";
	private static final String ISPASSED = "isPassedChk";
	
	// Set username and password to App preferences
	public void setUserPass(String user, String pass){
		if (user != null && pass != null){
			myEditor.putString(USERNAME, user);
			myEditor.putString(PASSWORD, pass);
			myEditor.commit();
		}
	}
	
	// Get username from App preferences
	public String getUsername() {
		return mySettings.getString(USERNAME, "");
	}
	
	// Get password from App preferences
	public String getPassword() {
		return mySettings.getString(PASSWORD, "");
	}
	
	// Check if username and password are set in the App preferences
	public boolean isUsernamePasswordSet() {
		if ( getUsername().length() > 0 
				&& getPassword().length() > 0 ){
			return true;
		} else {
			return false;
		}
	}
	
	// Check if username is set
	public boolean isUsernameSet() {
		if (getUsername().length() > 0){
			return true;
		} else {
			return false;
		}
	}
	
	// Check if password is set
	public boolean isPasswordSet() {
		if (getPassword().length() > 0){
			return true;
		} else {
			return false;
		}
	}
}
