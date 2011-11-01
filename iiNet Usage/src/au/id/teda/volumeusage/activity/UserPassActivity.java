package au.id.teda.volumeusage.activity;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.sax.CheckUserPassSAXHandler;
import au.id.teda.volumeusage.view.SetStatusBar;


// http://www.androidhive.info/2011/10/android-login-and-registration-screen-design/

//TODO: Add show password option in preferences / this activity

public class UserPassActivity extends Activity implements OnClickListener {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = UserPassActivity.class.getSimpleName();
	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext());
	
	private String myEmail;
	private String myPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// setting default screen to login.xml
        setContentView(R.layout.user_pass);
       
	}

	@Override
	protected void onResume() {
		super.onResume();
		
        // Set status bar hide or not
    	SetStatusBar setStatusBar = new SetStatusBar(this);
    	setStatusBar.showHide();
    	
		// Setup action bar title and buttons
		setUpActionBar();
		
		// Load activity view
		loadView();
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	Log.i(INFO_TAG, "handleMessage( " + msg + " )");
           //TODO: how do i use this??
        	//fillData();
        }
    };
	
	/**
	 *  Method used to set action bar title, reference buttons and set onClick listener
	 */
	public void setUpActionBar(){
		// Set action bar title
		TextView abTitle = (TextView) findViewById(R.id.action_bar_title_text);
		abTitle.setText(R.string.ab_user_pass_title);
		
		// Reference action bar buttons and set onClick

        // Take me back to the dashboard
        ImageButton abHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button);
        abHomeButton.setOnClickListener(this);
	}
	
    /**
     * Method for handling onClicks in this activity
     * @param: button
     */
	@Override
	public void onClick(View button) {
		Log.i(INFO_TAG, "onClick() > Button: " + button.getId());
		switch (button.getId()) {
		case R.id.action_bar_home_button:
			goHome();
			break;
		default:
			Log.i(INFO_TAG, "onClick() > Default switch");
		}
	}
	
	public void onUserPassBtnClick(View view){
		
		// Reference button so we can check button text for change once user/pass validated
		Button myButton = (Button) findViewById(R.id.user_pass_btn);
		Log.i(INFO_TAG, "checkCredentials() > Button: " + myButton.getText());
		
		if (myButton.getText() == getString(R.string.user_pass_btn_good)){
			//goHome();
			checkCredentials();
		} else {
			if (validateInput()){
				checkCredentials();
			}
		}
	}
	
	public boolean validateInput(){
		
		boolean check = true;
		
        // Set EditText id's
        EditText myEmailET = (EditText) findViewById(R.id.user_pass_email);
        EditText myPassET = (EditText) findViewById(R.id.user_pass_pass);
		
		String myEmail = myEmailET.getText().toString();
		String myPass = myPassET.getText().toString();
		
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
		}
		
		// Validate username is an email address
		if (!checkEmail(myEmail)){
			popup(getString(R.string.user_pass_malformemail));
			check = false;
		}
		
		//Log.d(DEBUG_TAG, "validateInput() > Username: " + myEmail + " validation is: " + checkEmail(myEmail));
		//Log.d(DEBUG_TAG, "validateInput() > Password: " + myPass);
		//Log.d(DEBUG_TAG, "validateInput() > Check: " + check);
		
		return check;
	}
	
	public void checkCredentials(){
		// Check if there is an error
		try {
			
	        // Set EditText id's
	        EditText myEmailET = (EditText) findViewById(R.id.user_pass_email);
	        EditText myPassET = (EditText) findViewById(R.id.user_pass_pass);
			
			String myEmail = myEmailET.getText().toString();
			String myPass = myPassET.getText().toString();
			
			String pathString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
					"username=" + myEmail + 
					"&action=login" +
					"&password=" + myPass;
			URL url = new URL(pathString);
			Log.d(DEBUG_TAG, "checkCredentials() > URL: " + url);
			
        	//ServiceHelper serviceHelper = new ServiceHelper(this);
        	//URL url = new URL(serviceHelper.buildXMLPath());
        	//Log.d(DEBUG_TAG, "URL: " + url);
        	//URL url = new URL("http://www.anddev.org/images/tut/basic/parsingxml/example.xml"); // Create a URL we want to load some xml-data from.
        	
			// Load xml from our developement xml file
			InputSource is = new InputSource(MyApp.getAppContext().getResources().openRawResource(R.raw.auth_fail));
			
			// Create a SAXParserFactory so we can
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	
        	// Create a SAXParser so we can
        	SAXParser sp = spf.newSAXParser();
        	
        	// Create a XMLReader
        	XMLReader xr = sp.getXMLReader();
        	
        	// Create a new ContentHandler and apply it to the XML-Reader
        	CheckUserPassSAXHandler myUserPassSAXHandler = new CheckUserPassSAXHandler();
        	xr.setContentHandler(myUserPassSAXHandler);
        	
        	// Parse the xml-data from our development file
        	xr.parse(new InputSource(is.getByteStream())); 
        	//xr.parse(new InputSource(url.openStream())); // Parse the xml-data from our URL.
        	//Log.d(DEBUG_TAG, "checkCredentials() > Checking username / password > try");
        } catch (Exception e) {
        	// Display any Error to catLog
        	Log.d("iiNet Usage", "XML Querry error: " + e.getMessage());
        }
		
		//Log.d(DEBUG_TAG, "checkCredentials() > Checking username / passworded: " + preferences.getBoolean("isPassedChk", false));
		loadView();
	}
		
	public void goHome(){
		Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
        startActivity(dashboardActivityIntent);
	}
	
	public void loadView(){
		
		// Load button and set depending on status of check
		Button userPassBTN = (Button) findViewById(R.id.user_pass_btn);
		// If check is ok then load good to go
		if (preferences.getBoolean("isPassedChk", false)){
			//Log.d(DEBUG_TAG, "loadView() > Load ok button");
			userPassBTN.setText(getString(R.string.user_pass_btn_good));	
		// Else assume check failed and load creditial check
		} else {
			//Log.d(DEBUG_TAG, "loadView() > Load check button");
			userPassBTN.setText(getString(R.string.user_pass_btn_nogood));
		}
	}
	
	public void popup(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	public boolean checkEmail(String email){
	    Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();

	}
	
}
