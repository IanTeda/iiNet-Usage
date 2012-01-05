package au.id.teda.iinetusage.phone.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.AppGlobals;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.async.CheckCredentialsAsync;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;
import au.id.teda.iinetusage.phone.helper.UrlBuilderHelper;


/**
 * Class: UserPassActivity
 * 
 * Description: Activity for setting and checking username and passwod
 * 
 * @version Alpha
 * 
 * @author Ian Teda
 *
 */

// TODO: Toast no connectivity

public class UserPassActivity extends ActionbarActivity implements OnClickListener, TextWatcher {
	
	// Static tags for logging
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = UserPassActivity.class.getSimpleName();
	
	// Set shared preference helper object
	private PreferenceHelper myPreferenceHelper = new PreferenceHelper();
	
    // Set EditText objects
    private EditText myEmailET;
    private EditText myPassET;
	
    // Set email/username and password string object
	private String myEmail;
	private String myPass;
	
	// Set url string object
	private URL myUrl;
	
	// Set button object
	private Button userPassBTN;
	
	// Loading flag
	private boolean loadingFlag;
	
	/**
	 * onCreate method for activity 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// setting default screen to login.xml
        setContentView(R.layout.user_pass);
        
        // Set reference for edit text objects
        myEmailET = (EditText) findViewById(R.id.user_pass_email);
        myPassET = (EditText) findViewById(R.id.user_pass_pass);
        
        // Set onClick listner for edit text objects
        myEmailET.addTextChangedListener(this);
        myPassET.addTextChangedListener(this);
        
    	// Set reference for activity button
        userPassBTN = (Button) findViewById(R.id.user_pass_btn);
        
        if (myPreferenceHelper.isPassed()
        		&& myPreferenceHelper.isUsernamePasswordSet()) {
        	
        	// Flag loading edittexts
        	loadingFlag = true;
        	
        	// Load edit text objects with strings stored in preferences
        	myEmailET.setText(myPreferenceHelper.getUsername());
			myPassET.setText(myPreferenceHelper.getPassword());
			
			// Flag no longer loading edit text objects
			loadingFlag = false;
        }
       
	}

	/**
	 * onResume method for activity
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	loadView();
        }
    };
	
	/**
	 *  Method used to set action bar title, reference buttons and set onClick listener
	 */
	public void setUpActionBar(){
		// Hide refresh button
		hideActionbarRefersh();
		
		// Set action bar title
		setActionbarTitle(this.getString(R.string.actionbar_user_pass_title));
		
	}
	
    /**
     * Method for handling onClicks in this activity
     * @param: button
     */
	@Override
	public void onClick(View button) {
		Log.i(INFO_TAG, "onClick() > Button: " + button.getId());
	}
	
	/**
	 * Method for handling activity button onClick events
	 * @param view
	 */
	public void onUserPassBtnClick(View view){
		
		// Reference button so we can check button text for change once user/pass validated
		Button myButton = (Button) findViewById(R.id.user_pass_btn);
		Log.i(INFO_TAG, "checkCredentials() > Button: " + myButton.getText());
		
		// If button text set to good then then safe to save user/pass and go to dashboard
		if (myButton.getText() == getString(R.string.user_pass_btn_good)){
			
			// Take me to the dashboard
			goHome();
			//new CheckCredentialsAsync(this, handler, buildUrl()).execute();

		// Else validate input, if true then execute async task
		} else if (validateInput()){
			new CheckCredentialsAsync(handler, buildUrl()).execute();
		}
	}
	
	/**
	 * Method for handling onClick events for the show/hide password check box
	 *
	 * @param view
	 */
	public void onCheckBoxClick(View view){
		
		// Flag loading edittexts so re-check doesn't come up
    	loadingFlag = true;
    	
		//If check box is checked
		if (((CheckBox) view).isChecked()) {
			
			// Show password
			myPassET.setTransformationMethod(null);
			
		// If check box is unchecked	
		} else {
			
			// Hide password
			myPassET.setTransformationMethod(new PasswordTransformationMethod());
		}
		
		// Flag no longer loading edit text objects
		loadingFlag = false;
	}
	
	/**
	 * Method for validating input before submitting query
	 * @return
	 */
	public boolean validateInput(){
		
		boolean check = true;
		
		// Get string values form edit text views
		myEmail = myEmailET.getText().toString();
		myPass = myPassET.getText().toString();
		
		// Check for no input edit text
		if (myEmail.length()<1 && myPass.length()<1) {
			popup(getString(R.string.user_pass_nouserpass));
			check = false;
		} else if (myEmail.length()<1){
			popup(getString(R.string.user_pass_nouser));
			check = false;
		} else if (myPass.length()<1){
			popup(getString(R.string.user_pass_nopass));
			check = false;
			
		// Validate username is an email address
		} else if (!checkEmail(myEmail)){
			popup(getString(R.string.user_pass_malformemail));
			check = false;
		}
		
		//Log.d(DEBUG_TAG, "validateInput() > Username: " + myEmail + " validation is: " + checkEmail(myEmail));
		//Log.d(DEBUG_TAG, "validateInput() > Password: " + myPass);
		//Log.d(DEBUG_TAG, "validateInput() > Check: " + check);
		
		return check;
	}
	
	/**
	 * Method for going to the application dashboard
	 */
	private void goHome(){
		// Load username/password into preferences
		//Log.d(DEBUG_TAG, "goHome() > isPassChk: " + settings.getBoolean("isPassedChk", false));

		 if (!myPreferenceHelper.isPassed()) {
				myPreferenceHelper.setUserPass(myEmail, myPass);
		 }
		
		// Start dashboard activity
		Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
        startActivity(dashboardActivityIntent);
	}
	
	/**
	 * Method used to load/set activity view
	 */
	public void loadView(){
		
		// If check is ok then load good to go
		if (myPreferenceHelper.isPassed()){
			//Log.d(DEBUG_TAG, "loadView() > Load ok button");
			userPassBTN.setText(getString(R.string.user_pass_btn_good));
			
		// Else assume check failed and load creditial check
		} else {
			//Log.d(DEBUG_TAG, "loadView() > Load check button");
			userPassBTN.setText(getString(R.string.user_pass_btn_nogood));
		}
	}
	
	/**
	 * Method for displaying toast messages
	 * @param msg
	 */
	public void popup(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Method used to check if email looks like an email address
	 * @param email
	 * @return
	 */
	public boolean checkEmail(String email){
	    Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();

	}
	
	/**
	 * Method used to set username and password to preferences
	 */
	public void setUserPass(){
		

		
		
		//Log.d(DEBUG_TAG, "setUserPass() > Username set to: " + settings.getString(USERNAME, "Username Error"));
		//Log.d(DEBUG_TAG, "setUserPass() > Password set to: " + settings.getString(PASSWORD, "Password Error"));
	}
	
	/**
	 * Method used to build URL from username and password
	 * @return
	 */
	private URL buildUrl(){
		
		try {
			myEmail = myEmailET.getText().toString();
			myPass = myPassET.getText().toString();
			
			String pathString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
					"username=" + myEmail + 
					"&action=login" +
					"&password=" + myPass;
			myUrl = new URL(pathString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.d(DEBUG_TAG, "buildUrl() > URL: " + myUrl);
		
		return myUrl;
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// Nothing to do here, but is an implemented method so needs to go in
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// Nothing to do here, but is an implemented method so needs to go in
		
	}

	/**
	 * Method for detecting changes to the edittext object and changing the button text
	 */
	@Override
	public void onTextChanged(CharSequence charSeq, int start, int before, int count) {
		//Log.v(DEBUG_TAG, "onTextChanged()> CharSeq: " + charSeq + " Start: " + start + " Before: " + before + " Count: " + count);
		
		// If edit text feilds change 
		if (count > 0 || before > 0) {
			// and password has already been checked set button to recheck
			if (myPreferenceHelper.isPassed() && !loadingFlag){
				
				//Log.d(DEBUG_TAG, "onTextChanged() > Change detected ");
						
				// Reset isPassed to false and blank username & pass
				myPreferenceHelper.setIsPassed(false);
				myPreferenceHelper.setUserPass("", "");
						
				// Change button text to recheck
				userPassBTN.setText(getString(R.string.user_pass_btn_rechk));

			}
		}
	}
	
}
