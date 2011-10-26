package au.id.teda.volumeusage.activity;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.sax.CheckUserPassSAXHandler;
import au.id.teda.volumeusage.view.SetStatusBar;


// http://www.androidhive.info/2011/10/android-login-and-registration-screen-design/

public class UserPassActivity extends Activity implements OnClickListener {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = UserPassActivity.class.getSimpleName();

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
	}
	
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
		
		if (myButton.getText() == this.getString(R.string.user_pass_btn_ok)){
			goHome();
		} else {
			checkCredentials();
		}
	}
	
	public boolean checkCredentials(){
		boolean credentialChk = false;
		// Check if there is an error
		try {
        	//ServiceHelper serviceHelper = new ServiceHelper(this);
        	//URL url = new URL(serviceHelper.buildXMLPath());
        	//Log.d(DEBUG_TAG, "URL: " + url);
        	//URL url = new URL("http://www.anddev.org/images/tut/basic/parsingxml/example.xml"); // Create a URL we want to load some xml-data from.
        	
			// Load xml from our developement xml file
			InputSource is = new InputSource(MyApp.getAppContext().getResources().openRawResource(R.raw.iinet_may));
			
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
		
		boolean isChecked = ((MyApp) this.getApplication()).isChecked();
		
		Log.d(DEBUG_TAG, "checkCredentials() > Checking username / password: " + isChecked);
		return credentialChk;
	}
		
	public void goHome(){
		Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
        startActivity(dashboardActivityIntent);
	}

}
