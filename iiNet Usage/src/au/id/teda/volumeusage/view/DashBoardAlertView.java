package au.id.teda.volumeusage.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.helper.AccountHelper;
import au.id.teda.volumeusage.service.ServiceHelper;

public class DashBoardAlertView {

	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = DashBoardAlertView.class.getSimpleName();
	
	private Context context;

	public DashBoardAlertView(Context context) {
		Log.i(INFO_TAG, "Constructor");
		this.context = context;
	}

	public void setAlert(LinearLayout alertBoxLayout, Button alertBoxButton) {
        
		AccountHelper accountHelper = new AccountHelper(context);
		//Log.d(DEBUG_TAG, "DashBoardAlertView > setAlert() Password: " + accountInfo.passwordExists());
		//Log.d(DEBUG_TAG, "DashBoardAlertView > setAlert() Username: " + accountInfo.usernameExists());
        
		// TODO: Make an alert string builder
		
		// Missing password and username
		if (accountHelper.passwordExists() == false && accountHelper.usernameExists() == false){
        	alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_userpass));
        // Missing password
		} else if (accountHelper.passwordExists() == false){
        	alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_pass));
		// Missing username
		} else if (accountHelper.usernameExists() == false){
        	alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_user));
		// Missing first refresh
		} else if (accountHelper.infoExists() == false){
        	alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_nodata));
		
		} else if (accountHelper.getPercentageUsed("peak") > 1){
			alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_overpeak));
		} else if (accountHelper.getPercentageUsed("offpeak") > 1){
			alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_overoffpeak));
		} else if (accountHelper.getPercentageUsed("peak") > 1 && accountHelper.getPercentageUsed("offpeak") > 1){
			alertBoxLayout.setVisibility(View.VISIBLE);
        	alertBoxButton.setText(context.getString(R.string.db_alertbox_overpeak_offpeak));
        } else {
        	alertBoxLayout.setVisibility(View.INVISIBLE);
        	alertBoxButton.setText("No Alerts");
        }

	}

}
