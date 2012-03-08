package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class AlertView extends PreferenceHelper {
	
	//private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = AlertboxView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	private View myAlertView;
	private Button myAlert;
	
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

	
	private static final AccountHelper myAccount = new AccountHelper();
	
	public AlertView(Context context) {
		
		// Construct context
		myActivityContext = context;
		
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Initialise alert button
		myAlertView = myActivity.findViewById(R.id.alert_view);
		myAlert = (Button) myActivity.findViewById(R.id.alert);
		
		// Initialise alert strings
		noUser = myActivity.getString(R.string.alert_no_user);
		noPass = myActivity.getString(R.string.alert_no_pass);
		noUserPass = myActivity.getString(R.string.alert_no_user_pass);
		noData = myActivity.getString(R.string.alert_no_data);
		peakOffpeakQuota = myActivity.getString(R.string.alert_peak_offpeak_quota);
		peakQuota = myActivity.getString(R.string.alert_peak_quota);
		offpeakQuota = myActivity.getString(R.string.alert_offpeak_quota);
		peakOffpeakUsage = myActivity.getString(R.string.alert_peak_offpeak_usage);
		peakUsage = myActivity.getString(R.string.alert_peak_usage);
		offpeakUsage = myActivity.getString(R.string.alert_offpeak_usage);

	}

	/**
	 * Get the string value of the current alert
	 * @return string value of button text
	 */
	public String getAlert(){
		String myAlertString = (String) myAlert.getText();
		return myAlertString;
	}
	
	public void setAlert() {
	
		// Check if we are missing a username and password
		if (!isUsernamePasswordSet()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(noUserPass);
		}
		// Else are we missing a password
		else if (!isPasswordSet()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(noPass);
		}
		// Else are we missing a username
		else if (!isUsernameSet()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(noUser);
		}
		// Else are we missing data
		else if (!myAccount.isStatusSet()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(noData);
		}
		// Else is the peak & offpeak over the quota
		else if (myAccount.isCurrentPeakShaped() 
				&& myAccount.isCurrentOffpeakShaped()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(peakOffpeakQuota);
		}
		// Else peak over and offpeak usage
		else if (myAccount.isCurrentPeakShaped()
				&& myAccount.isOffpeakUsageOver()){
			// Show alert view
			showAlert();
			// Set alert text
			String alertString = peakQuota + " &amp; " + offpeakUsage;
			myAlert.setText(alertString);
		}
		// Else offpeak over and peak usage
		else if (myAccount.isCurrentOffpeakShaped()
				&& myAccount.isPeakUsageOver()){
			// Show alert view
			showAlert();
			// Set alert text
			String alertString = offpeakQuota + " &amp; " + peakUsage;
			myAlert.setText(alertString);
		}
		// Else is the peak over allotted quota
		else if (myAccount.isCurrentPeakShaped()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(peakQuota);
		}
		// Else is the offpeak over allotted quota
		else if (myAccount.isCurrentOffpeakShaped()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(offpeakQuota);
		}
		// Else is peak and offpeak over the alert value
		else if (myAccount.isPeakUsageOver()
				&& myAccount.isOffpeakUsageOver()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(peakOffpeakUsage);
		}
		// Else is the peak over the alert value
		else if (myAccount.isPeakUsageOver()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(peakUsage);
		}
		// Else is the offpeak over the alert value
		else if (myAccount.isOffpeakUsageOver()){
			// Show alert view
			showAlert();
			// Set alert text
			myAlert.setText(offpeakUsage);
		}
		// Else hide alert
		else {
			hideAlert();
		}

	}
	
	public void hideAlert(){
		myAlertView.setVisibility(View.GONE);
		
		// Check if we are missing a username and password
		if (!isUsernamePasswordSet()){
			
		}
		// Else are we missing a password
		else if (!isPasswordSet()){

		}
		// Else are we missing a username
		else if (!isUsernameSet()){

		}
		// Else are we missing data
		else if (!myAccount.isStatusSet()){

		}
		// Else is the peak & offpeak over the quota
		else if (myAccount.isCurrentPeakShaped() 
				&& myAccount.isCurrentOffpeakShaped()){

		}
		// Else peak over and offpeak usage
		else if (myAccount.isCurrentPeakShaped()
				&& myAccount.isOffpeakUsageOver()){

		}
		// Else offpeak over and peak usage
		else if (myAccount.isCurrentOffpeakShaped()
				&& myAccount.isPeakUsageOver()){

		}
		// Else is the peak over allotted quota
		else if (myAccount.isCurrentPeakShaped()){

		}
		// Else is the offpeak over allotted quota
		else if (myAccount.isCurrentOffpeakShaped()){

		}
		// Else is peak and offpeak over the alert value
		else if (myAccount.isPeakUsageOver()
				&& myAccount.isOffpeakUsageOver()){

		}
		// Else is the peak over the alert value
		else if (myAccount.isPeakUsageOver()){

		}
		// Else is the offpeak over the alert value
		else if (myAccount.isOffpeakUsageOver()){

		}
	}
	
	public void showAlert(){
		myAlertView.setVisibility(View.VISIBLE);
	}

}
