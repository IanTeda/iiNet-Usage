package au.id.teda.iinetusage.phone.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;
import au.id.teda.iinetusage.phone.helper.UserPassHelper;

public class AlertboxView extends PreferenceHelper {
	
	private Context context;

	public AlertboxView(Context context) {
		Log.i(INFO_TAG, "Constructor");
		this.context = context;
	}

	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AlertboxView.class.getSimpleName();

	public void setAlert(Button alertBoxButton) {
        
		// TODO: Make an alert string builder
		
		// Missing password and username
		if (!isUsernamePasswordSet()){
			alertBoxButton.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.alertbox_userpass));
        	
        // Missing password
		} else if (!isPasswordSet()){
			alertBoxButton.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.alertbox_pass));
        	
		// Missing username
		} else if (!isUsernameSet()){
			alertBoxButton.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.alertbox_user));

		}
	}

}
