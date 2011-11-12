package au.id.teda.volumeusage.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import au.id.teda.volumeusage.helper.AccountHelper;
import au.id.teda.volumeusage.R;

public class DashboardUsageStats {

	/**
	 *  Static tag strings for loging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = DashboardUsageStats.class.getSimpleName();
	
	private static final String PEAK = "peak";
	private static final String OFFPEAK = "offpeak";
	
	private static final String SO_FARE = "so_fare";
	private static final String TO_GO = "to_go";
	
	private Context context;
	
	public DashboardUsageStats(Context context) {
		Log.i(INFO_TAG, "Constructor");
		this.context = context;
	}

	public void setUsageTextViews(TextView peakTextView,
			TextView offpeakTextView) {
		Log.i(INFO_TAG, "setUsageTextViews()");
		
		SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(context);
        String myDataSetting = mySettings.getString("dashboard_summary_pref", SO_FARE);
        //String myDataSetting = "so_fare";
		//Log.d(DEBUG_TAG, "Summary Setting: " + myDataSetting + " String: " + SO_FARE);
		
		AccountHelper accountHelper = new AccountHelper(context);
		if (accountHelper.infoExists() 
				&& accountHelper.statusExists()
				&& myDataSetting.equals(SO_FARE)){
	        //Log.d(DEBUG_TAG, "Summary Setting; " + myDataSetting);
			peakTextView.setText(accountHelper.dataUsageString(PEAK));
	    	offpeakTextView.setText(accountHelper.dataUsageString(OFFPEAK));
		} else if (accountHelper.infoExists() 
				&& accountHelper.statusExists() 
				&& myDataSetting.equals(TO_GO)){
			peakTextView.setText(accountHelper.getdataRemainingString(PEAK));
	    	offpeakTextView.setText(accountHelper.getdataRemainingString(OFFPEAK));
		}
	}
	
	public void setUsageLayoutAlert(TextView peakTextView,
			TextView offpeakTextView) {
		Log.i(INFO_TAG, "setUsageLayoutAlert()");
		AccountHelper accountHelper = new AccountHelper(context);
		
		if (accountHelper.usageAlert(PEAK)){
			if (accountHelper.usageOver(PEAK)){
				peakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_over));
			} else {
				peakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_alert));
			}
		} else {
			peakTextView.setTextColor(context.getResources().getColor(R.color.h2_text_color));
		}
		
		if (accountHelper.usageAlert(OFFPEAK)){
			if (accountHelper.usageOver(OFFPEAK)){
				offpeakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_over));
			} else {
				offpeakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_alert));
			}
		} else {
			offpeakTextView.setTextColor(context.getResources().getColor(R.color.h2_text_color));
		}
		
	}
	
}
