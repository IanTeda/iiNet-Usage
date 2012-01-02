package au.id.teda.iinetusage.phone.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import au.id.teda.iinetusage.phone.AppGlobals;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class AlertboxView extends PreferenceHelper {
	
	private Context myContext = AppGlobals.getAppContext();

	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AlertboxView.class.getSimpleName();

	public void setAlert(Button alertboxButton) {
        
		// TODO: Make an alert string builder
		
		// Missing password and username
		if (!isUsernamePasswordSet()){
			showAlertbox(alertboxButton);
			alertboxButton.setText(myContext.getString(R.string.alertbox_userpass));
        	
        // Missing password
		} else if (!isPasswordSet()){
			showAlertbox(alertboxButton);
			alertboxButton.setText(myContext.getString(R.string.alertbox_pass));
        	
		// Missing username
		} else if (!isUsernameSet()){
			showAlertbox(alertboxButton);
			alertboxButton.setText(myContext.getString(R.string.alertbox_user));

		}
	}
	
	public void hideAlertbox(Button alertboxButton){
		alertboxButton.setVisibility(View.GONE);
	}
	
	public void showAlertbox(Button alertboxButton){
		alertboxButton.setVisibility(View.VISIBLE);
	}

}
