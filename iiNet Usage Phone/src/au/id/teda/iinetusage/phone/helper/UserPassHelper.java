package au.id.teda.iinetusage.phone.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import au.id.teda.iinetusage.phone.AppGlobals;

public class UserPassHelper {
	
	// Set shared preferences object
	protected SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(AppGlobals.getAppContext());
	protected SharedPreferences.Editor myEditor = mySettings.edit();

	/**
	 * Methods for getting, setting and checking username and password
	 */
	// Static strings for preference keys
	private final static String PASSWORD = "iinet_password";
	private final static String USERNAME = "iinet_username";
	private final static String ISPASSED = "isPassedChk";
	private final static String ERRORTXT = "errortxt"; 
	
	// Set username and password to App preferences
	public void setUserPass(String user, String pass){
		if (user != null && pass != null){
			myEditor.putString(USERNAME, user);
			myEditor.putString(PASSWORD, pass);
			myEditor.commit();
		}
	}
	
	public void setUsername(String username){
		myEditor.putString(USERNAME, username);
		myEditor.commit();
	}
	
	public void setPassword(String password){
		myEditor.putString(PASSWORD, password);
		myEditor.commit();
	}
	
	// Set username password has been checked and works
	public void setIsPassed(boolean isPassed){
		myEditor.putBoolean(ISPASSED, isPassed);
		myEditor.commit();
	}
	
	// Set error text
	public void setErrorTxt(String errorTxt){
		myEditor.putString(ERRORTXT, errorTxt);
		myEditor.commit();
	}
	
	// Get username from App preferences
	public String getUsername() {
		return mySettings.getString(USERNAME, "");
	}
	
	// Get password from App preferences
	public String getPassword() {
		return mySettings.getString(PASSWORD, "");
	}
	
	// Get error text
	public String getErrorTxt(){
		return mySettings.getString(ERRORTXT, "");
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
	
	public boolean isPassed(){
		return mySettings.getBoolean(ISPASSED, false);
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
	
	public boolean isErrorTxt(){
		if (getErrorTxt().length() >0){
			return true;
		} else {
			return false;
		}
	}
}
