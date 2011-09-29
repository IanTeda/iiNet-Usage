package au.id.teda.volumeusage.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import au.id.teda.volumeusage.helper.AccountHelper;
import au.id.teda.volumeusage.R;

public class DashboardUsageStats {

	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = DashboardUsageStats.class.getSimpleName();
	
	private Context context;
	
	public DashboardUsageStats(Context context) {
		Log.i(INFO_TAG, "Constructor");
		this.context = context;
	}

	public void setUsageTextViews(TextView peakTextView,
			TextView offpeakTextView) {
		Log.i(INFO_TAG, "setUsageTextViews()");
		AccountHelper accountHelper = new AccountHelper(context);
		if (accountHelper.infoExists() && accountHelper.statusExists()){
			peakTextView.setText(accountHelper.dataUsageString("peak"));
	    	offpeakTextView.setText(accountHelper.dataUsageString("offpeak"));	
		}
	}
	
	public void setUsageLayoutAlert(TextView peakTextView,
			TextView offpeakTextView) {
		Log.i(INFO_TAG, "setUsageLayoutAlert()");
		AccountHelper accountHelper = new AccountHelper(context);
		
		if (accountHelper.usageAlert("peak")){
			if (accountHelper.usageOver("peak")){
				peakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_over));
			} else {
				peakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_alert));
			}
		} else {
			peakTextView.setTextColor(context.getResources().getColor(R.color.h2_text_color));
		}
		
		if (accountHelper.usageAlert("offpeak")){
			if (accountHelper.usageOver("offpeak")){
				offpeakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_over));
			} else {
				offpeakTextView.setTextColor(context.getResources().getColor(R.color.db_data_limit_alert));
			}
		} else {
			offpeakTextView.setTextColor(context.getResources().getColor(R.color.h2_text_color));
		}
		
	}
	
}
