package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class AlertView extends PreferenceHelper {
	
	//private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = AlertboxView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	private Button myAlertButton;
	
	// Alert strings
	private static String noUser;
	private static String noPass;
	private static String noUserPass;
	private static String noData;
	private static String peakUsage;
	private static String offpeakUsage;
	private static String peakOffpeakUsage;
	private static String peakQuota;
	private static String offpeakQuota;
	private static String peakOffpeakQuota;
	
	
	public AlertView(Context context) {
		
		// Construct context
		myActivityContext = context;
		
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Initialise alert button
		myAlertButton = (Button) myActivity.findViewById(R.id.alert);
		
		// Initialise alert strings
		noUser = myActivity.getString(R.string.alert_no_user);
		noPass = myActivity.getString(R.string.alert_no_pass);
		noUserPass = myActivity.getString(R.string.alert_no_user_pass);
		noData = myActivity.getString(R.string.alert_no_data);
		peakUsage = myActivity.getString(R.string.alert_peak_usage);
		offpeakUsage = myActivity.getString(R.string.alert_offpeak_usage);
		peakOffpeakUsage = myActivity.getString(R.string.alert_peak_offpeak_usage);
		peakQuota = myActivity.getString(R.string.alert_peak_quota);
		offpeakQuota = myActivity.getString(R.string.alert_offpeak_quota);
		peakOffpeakQuota = myActivity.getString(R.string.alert_peak_offpeak_quota);
	}

	/**
	 * Get the string value of the current alreat box
	 * @return string value of button text
	 */
	public String getAlert(){
		String myAlert = (String) myAlertButton.getText();
		return myAlert;
	}
	
	public void setAlert() {
        
		// TODO: Make an alert string builder
		
		// Missing password and username
		if (!isUsernamePasswordSet()){
			showAlertbox();
			myAlertButton.setText(myActivity.getString(R.string.alert_no_user_pass));
        	
        // Missing password
		} else if (!isPasswordSet()){
			showAlertbox();
			myAlertButton.setText(myActivity.getString(R.string.alert_no_pass));
        	
		// Missing username
		} else if (!isUsernameSet()){
			showAlertbox();
			myAlertButton.setText(myActivity.getString(R.string.alert_no_user));

		}
	}
	
	public void hideAlertbox(){
		myAlertButton.setVisibility(View.GONE);
	}
	
	public void showAlertbox(){
		myAlertButton.setVisibility(View.VISIBLE);
	}

}
