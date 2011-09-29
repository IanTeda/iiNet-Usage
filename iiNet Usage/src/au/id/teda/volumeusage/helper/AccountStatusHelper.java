package au.id.teda.volumeusage.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AccountStatusHelper extends AccountHelper {
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AccountStatusHelper.class.getSimpleName();
	
	private Context context;
	
	private final static String PEAK = "peak";
	private final static String OFFPEAK = "offpeak";

	public AccountStatusHelper(Context context) {
		super(context);
		this.context = context;
	}

	// Check if data usage is over usage alert preference
	public boolean usageAlert(String period){
		// Get peak and offpeak alert preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		boolean alert = false;
		
		// Check for peak usage alert
		if (period == PEAK){
			
			double peakAlert = settings.getInt("peak_usage_alert", 75);
			peakAlert = peakAlert/100;
			double peakUsed = getPercentageUsed(PEAK);
			
			if (peakUsed > peakAlert){
				alert = true;
			} else {
				alert = false;
			}
			
		// Check for offpeak usage alert
		} else if (period == OFFPEAK){
			
			double offpeakAlert = settings.getInt("offpeak_usage_alert", 75);
			offpeakAlert = offpeakAlert/100;
			double offpeakUsed = getPercentageUsed(OFFPEAK);
			
			if (offpeakUsed > offpeakAlert){
				alert = true;
			} else {
				alert = false;
			}
		}
		return alert;
	}

	// Check if data usage is over quota
	public boolean usageOver(String period){
		boolean check = false;
		double dataUsed = getPercentageUsed(period);
		
		if (dataUsed > 1){
			check = true;
		} else {
			check = false;
		}
		return check;
	}
	
}
