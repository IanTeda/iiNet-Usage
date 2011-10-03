package au.id.teda.volumeusage.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

public class SetStatusBar {

	/**
	 *  Static tag strings for loging information and debug
	 */
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = SetStatusBar.class.getSimpleName();
	
	private static final String HIDE_STATUS_BAR = "hide_status_bar";
	private Context context;
	
	// AccountHelper constructor
	public SetStatusBar(Context context) {
		this.context = context; // Set context for methods
	}
	
	public void showHide(){
        
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        boolean hideStatusBar = settings.getBoolean(HIDE_STATUS_BAR, false);
        
        if (hideStatusBar){
        	((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
        	((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
	}
}
