package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class AlertboxView extends PreferenceHelper {
	
	//private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = AlertboxView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	private Button myAlertboxButton;
	
	public AlertboxView(Context context) {
		
		// Construct context
		myActivityContext = context;
		
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Initialise alert button
		myAlertboxButton = (Button) myActivity.findViewById(R.id.alertbox_button);
	}

	/**
	 * Get the string value of the current alreat box
	 * @return string value of button text
	 */
	public String getAlert(){
		String myAlert = (String) myAlertboxButton.getText();
		return myAlert;
	}
	
	public void setAlert() {
        
		// TODO: Make an alert string builder
		
		// Missing password and username
		if (!isUsernamePasswordSet()){
			showAlertbox();
			myAlertboxButton.setText(myActivity.getString(R.string.alertbox_no_userpass));
        	
        // Missing password
		} else if (!isPasswordSet()){
			showAlertbox();
			myAlertboxButton.setText(myActivity.getString(R.string.alertbox_no_pass));
        	
		// Missing username
		} else if (!isUsernameSet()){
			showAlertbox();
			myAlertboxButton.setText(myActivity.getString(R.string.alertbox_no_user));

		}
	}
	
	public void hideAlertbox(){
		myAlertboxButton.setVisibility(View.GONE);
	}
	
	public void showAlertbox(){
		myAlertboxButton.setVisibility(View.VISIBLE);
	}

}
