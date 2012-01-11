package au.id.teda.iinetusage.phone.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.async.CheckCredentialsAsync;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

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

public class UserPassActivity extends ActionbarActivity implements
		OnClickListener, TextWatcher {

	// Static tags for logging
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = UserPassActivity.class
			.getSimpleName();

	// Set shared preference helper object
	private PreferenceHelper mySettings = new PreferenceHelper();

	// Set status strings options
	private static final String SET = "set";
	private static final String CHECK = "check";
	private static final String ERROR = "error";
	private static final String CORRECT = "correct";
	private static final String RECHECK = "recheck";

	// Set EditText objects
	private EditText myEmailET;
	private EditText myPassET;

	// Set email/username and password string object
	private String myEmail;
	private String myPass;

	// Set url string object
	private URL myUrl;

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

	}

	@Override
	protected void onPause() {

		// Set username password if credential check has passed
		setUsernamePassword();

		// Clear error text
		mySettings.setErrorTxt("");

		// Clear pass check
		mySettings.setIsPassed(false);

		super.onPause();
	}

	/**
	 * onResume method for activity
	 */
	@Override
	protected void onResume() {
		super.onResume();

		loadView();
	}

	@Override
	protected void onDestroy() {

		// Check if we should set the username and password
		setUsernamePassword();

		// Reset error text to null
		mySettings.setErrorTxt("");

		super.onDestroy();
	}

	/**
	 * Handler for passing messages from other classes
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			loadView();
		}
	};

	/**
	 * Method used to set action bar title, reference buttons and set onClick
	 * listener
	 */
	public void setUpActionBar() {
		// Hide refresh button
		hideActionbarRefersh();

		// Set action bar title
		setActionbarTitle(this.getString(R.string.actionbar_user_pass_title));

	}

	/**
	 * Method for handling onClicks in this activity
	 * 
	 * @param: button
	 */
	@Override
	public void onClick(View button) {
	}

	/**
	 * Method for handling activity button onClick events
	 * 
	 * @param view
	 */
	public void onUserPassBtnClick(View view) {

		// Reference button so we can check button text for change once
		// user/pass validated
		Button myButton = (Button) findViewById(R.id.user_pass_btn);

		// If button text set to good then then safe to save user/pass and go to
		// dashboard
		if (myButton.getText() == getString(R.string.user_pass_button_correct)) {

			// Start dashboard activity
			Intent dashboardActivityIntent = new Intent(this,
					MainActivity.class);
			startActivity(dashboardActivityIntent);

			// Else validate input,
		} else if (validateInput()) {
			// Clear error text
			mySettings.setErrorTxt("");

			// If true then execute async task
			new CheckCredentialsAsync(this, handler, buildUrl()).execute();
		}
	}

	/**
	 * Method for handling onClick events for the show/hide password check box
	 * 
	 * @param view
	 */
	public void onCheckBoxClick(View view) {

		// Flag loading edittexts so re-check doesn't come up
		loadingFlag = true;

		// If check box is checked
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
	 * 
	 * @return
	 */
	public boolean validateInput() {

		boolean check = true;

		// Get string values form edit text views
		myEmail = myEmailET.getText().toString();
		myPass = myPassET.getText().toString();

		// Check for no input edit text
		if (myEmail.length() < 1 && myPass.length() < 1) {
			popup(getString(R.string.user_pass_nouserpass));
			check = false;
		} else if (myEmail.length() < 1) {
			popup(getString(R.string.user_pass_nouser));
			check = false;
		} else if (myPass.length() < 1) {
			popup(getString(R.string.user_pass_nopass));
			check = false;

			// Validate username is an email address
		} else if (!checkEmail(myEmail)) {
			popup(getString(R.string.user_pass_malformemail));
			check = false;
		}

		// Log.d(DEBUG_TAG, "validateInput() > Username: " + myEmail +
		// " validation is: " + checkEmail(myEmail));
		// Log.d(DEBUG_TAG, "validateInput() > Password: " + myPass);
		// Log.d(DEBUG_TAG, "validateInput() > Check: " + check);

		return check;
	}

	/**
	 * Method for displaying toast messages
	 * 
	 * @param msg
	 */
	public void popup(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * Method used to check if email looks like an email address
	 * 
	 * @param email
	 * @return
	 */
	public boolean checkEmail(String email) {
		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}

	/**
	 * Method used to build URL from username and password
	 * 
	 * @return
	 */
	private URL buildUrl() {

		try {
			myEmail = myEmailET.getText().toString();
			myPass = myPassET.getText().toString();

			String pathString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?"
					+ "username="
					+ myEmail
					+ "&action=login"
					+ "&password="
					+ myPass;
			myUrl = new URL(pathString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.d(DEBUG_TAG, "buildUrl() > URL: " + myUrl);

		return myUrl;

	}

	/**
	 * Method called on text change detected but before onTextChanged
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// Nothing to do here, but is an implemented method so needs to go in

	}

	/**
	 * Method for detecting changes to the edittext object and changing the
	 * button text
	 */
	@Override
	public void onTextChanged(CharSequence charSeq, int start, int before,
			int count) {

		// If edit text feilds change
		if (count > 0 || before > 0) {

			// and username password has already been set then set button to
			// recheck
			if (mySettings.isUsernamePasswordSet() || mySettings.isPassed()) {
				if (!loadingFlag) {

					// Reset isPassed to false and blank username & pass
					mySettings.setIsPassed(false);
					mySettings.setUserPass("", "");

					// Change button text to recheck
					setCheckButton(RECHECK);
					setStatusText(RECHECK);
				}

			}
		}
	}

	/**
	 * Method called after text change detected
	 */
	@Override
	public void afterTextChanged(Editable arg0) {
		// Nothing to do here, but is an implemented method so needs to go in
	}

	/**
	 * Method used to check if valid username password has been checked and add
	 * to shared prefferences if it has.
	 */
	private void setUsernamePassword() {

		// Check if creditial check has passed
		if (mySettings.isPassed()) {
			// if so then set username and password to preferences

			// TODO: make method for getting and setting edittext value
			// Get string values form edit text views
			myEmail = myEmailET.getText().toString();
			myPass = myPassET.getText().toString();

			mySettings.setUserPass(myEmailET.getText().toString(), myPassET
					.getText().toString());
		}
	}

	/**
	 * Method used to load/set activity view
	 */
	public void loadView() {

		// Set up the actionbar
		setUpActionBar();

		// Check if username and password have already been set
		if (mySettings.isUsernamePasswordSet()) {
			// If it is then load view set

			// Set check button password set
			setCheckButton(SET);
			// Set status text as password set
			setStatusText(SET);

			// Flag loading edittexts
			loadingFlag = true;
			// Load saved username password into edit texts
			setEmailEditText();
			setPasswordEditText();
			// Flag no longer loading edit text objects
			loadingFlag = false;

			// Check if we are trying to set a new password
		} else if (mySettings.isPassed()) {
			// If it is then load view correct

			// Set check button show all good
			setCheckButton(CORRECT);
			// Set status text to all good
			setStatusText(CORRECT);

			// Check if password is not set and we have an error text
		} else if (mySettings.isErrorTxt()) {
			// If there is set status as error

			// set check button to error
			setCheckButton(ERROR);
			// Set status text to error
			setStatusText(ERROR);

			// Else we must be checking
		} else {
			Log.d(DEBUG_TAG, "loadView() > Userpass check");

		}

	}

	public void setStatusText(String status) {

		// Set objects for text views
		TextView myCheckTextView = (TextView) findViewById(R.id.user_pass_status_enter);
		TextView myCorrectTextView = (TextView) findViewById(R.id.user_pass_status_correct);
		TextView myErrorTextView = (TextView) findViewById(R.id.user_pass_status_error);

		// Everything looks correct
		if (status == CORRECT || status == SET) {

			// Show status as correct
			myCheckTextView.setVisibility(View.GONE);
			myErrorTextView.setVisibility(View.GONE);
			myCorrectTextView.setVisibility(View.VISIBLE);

			if (status == SET) {
				myCorrectTextView.setText(R.string.user_pass_status_set);
			} else {
				myCorrectTextView.setText(R.string.user_pass_status_correct);
			}

			// There appears to be an error
		} else if (status == ERROR) {

			// Show status as error
			myCheckTextView.setVisibility(View.GONE);
			myCorrectTextView.setVisibility(View.GONE);
			myErrorTextView.setVisibility(View.VISIBLE);

			// Show status as recheck
		} else if (status == RECHECK) {

			// Show status as recheck
			myErrorTextView.setVisibility(View.GONE);
			myCorrectTextView.setVisibility(View.GONE);
			myCheckTextView.setVisibility(View.VISIBLE);

			myCheckTextView
					.setText(getString(R.string.user_pass_status_recheck));

			// If all else fails
		} else {

			// Show status as to be checked
			myErrorTextView.setVisibility(View.GONE);
			myCorrectTextView.setVisibility(View.GONE);
			myCheckTextView.setVisibility(View.VISIBLE);

			myCheckTextView.setText(getString(R.string.user_pass_status_enter));
		}
	}

	public void setCheckButton(String status) {

		// Set button object
		Button userPassBTN = (Button) findViewById(R.id.user_pass_btn);

		if (status == CORRECT || status == SET) {

			// Set button text to correct
			userPassBTN.setText(getString(R.string.user_pass_button_correct));

		} else if (status == ERROR) {

			// Set button text to error
			userPassBTN.setText(getString(R.string.user_pass_button_check));

		} else if (status == RECHECK) {

			// Set button text to error
			userPassBTN.setText(getString(R.string.user_pass_button_recheck));

		} else {

			// Set button text to error
			userPassBTN.setText(getString(R.string.user_pass_button_check));

		}
	}

	private String getEmailEditText() {
		return myEmailET.getText().toString();
	}

	private String getPasswordEditText() {
		return myPassET.getText().toString();
	}

	private void setEmailEditText() {
		if (mySettings.isUsernameSet()) {
			myEmailET.setText(mySettings.getUsername());
		}
	}

	private void setPasswordEditText() {
		if (mySettings.isPasswordSet()) {
			myPassET.setText(mySettings.getPassword());
		}
	}

}
