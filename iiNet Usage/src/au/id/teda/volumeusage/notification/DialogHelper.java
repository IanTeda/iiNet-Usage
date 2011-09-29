package au.id.teda.volumeusage.notification;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.prefs.Preferences;

public class DialogHelper {
	
	// TODO: Create a custom dialog for alert
	// TODO: Create an external class for dialogs
	
	/**
	 *  Static tag strings for loging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = DialogHelper.class.getSimpleName();
	
	private static final String SHOW = "show";
	private static final String DISMISS = "dismiss";
	
	private Context context;
	private boolean showRefreshDialog;
	private ProgressDialog progressDialog;
	
	public DialogHelper(Context context) {
		Log.i(INFO_TAG, "Constructor");
		this.context = context;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		showRefreshDialog = settings.getBoolean("hide_refresh_dialog", false);
	}
	
    public void dialogUsernamePassword(){
    	Log.i(INFO_TAG, "dialogUsernamePassword()");
    	
    	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
    	alertBuilder.setMessage("Username or password not set\nGoto settings"); // Set message for dialog
    	alertBuilder.setCancelable(true); // Can the user use the back button on alert. Yes they can
            
    	alertBuilder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			Intent menuIntent = new Intent(context, Preferences.class);
    			context.startActivity(menuIntent);
    		}
    	});
            
    	alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.cancel(); // Cancel out of the dialog
    		}
    	});
            
    	AlertDialog alert = alertBuilder.create();
    	alert.show();
    }
    
    public void dialogRefreshingData(String doWhat){
    	Log.i(INFO_TAG, "toastDataUpdate()");
    	
		if (doWhat == SHOW && showRefreshDialog == false){
    		progressDialog = ProgressDialog.show(context,
    				MyApp.getAppContext().getString(R.string.refresh_progress_dialog_title),
    				MyApp.getAppContext().getString(R.string.refresh_progress_dialog_description),
    				true);
    	} else if (doWhat == DISMISS && showRefreshDialog == false) {
    		progressDialog.dismiss();
    	}
    	
    	
    }

}
