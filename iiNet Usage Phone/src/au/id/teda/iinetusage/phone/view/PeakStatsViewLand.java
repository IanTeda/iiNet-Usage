package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class PeakStatsViewLand extends AccountHelper {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = PeakStatsViewLand.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	// Type face object for custom font
	private final Typeface myFontNumber;
	
	// String objects for unit in unit block
	private final String percentUnit;
	private final String gigabyteUnit;
	
	// Color values for focus and alternate
	private final int focusColor;
	private final int alternateColor;
	
	// TextView objects for peak number
	private final TextView myPeakData;
	private final TextView myPeakQuota;
	private final TextView myPeakUnit;
	
	// TextView objects for peak number
	private final TextView myPeakDaily;
	private final TextView myPeakDailyData;
	
	// TextView object for peak status
	private final TextView myStatus;
	
	// TextView object for peak period
	private final TextView myPeakPeriod;
	
	/**
	 * Class constructor
	 * @param context
	 */
	public PeakStatsViewLand (Context context){
		
		// Initialise activity context
		myActivityContext = context;
		
		// Initialise activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Initialise custom number font
		myFontNumber = Typeface.createFromAsset(((ContextWrapper) myActivity).getAssets(), "OSP-DIN.ttf"); 
	
		// Initialise percent and Gigabyte units
		percentUnit = myActivity.getString(R.string.peak_offpeak_unit_percent);
		gigabyteUnit = myActivity.getString(R.string.peak_offpeak_unit_gigabyte);
		
		// Initialise focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
	
		// Initialise Peak Data TextView objects
		myPeakData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_data);
		myPeakQuota = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_quota);
		myPeakUnit = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_unit);
		
		// Initialise Daily Average TextView objects
		myPeakDaily = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_suggested_remaining);
		myPeakDailyData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_suggested_remaining_data);
		
		// Initialise status TextView objects
		myStatus = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_status_data);
		
		// Initialise peak period TextView objects
		myPeakPeriod = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_period_data);
		
		// Set type face custom font to number
		myPeakData.setTypeface(myFontNumber);
		myPeakQuota.setTypeface(myFontNumber);
		myPeakUnit.setTypeface(myFontNumber);
	}
	
	public void loadView(){
		
		// Set peak GB used
		myPeakData.setText(getPeakDataUsedGb());
		
		// Set peak GB quota
		myPeakQuota.setText(getPeakQuotaStringGBLand());
		
		// Set peak view
		setPeakRemaining();
		
	}
	
	private void setPeakRemaining(){
		setPeakDailySuggested();
		setStatus();
		setPeakPeriod();
	}
	
	private void setPeakDailySuggested(){
		myPeakDaily.setText(myActivity.getString(R.string.peak_offpeak_land_suggested));
		myPeakDailyData.setText(getPeakDailyDataSuggestedMb());
	}
	
	private void setPeakDailyUsed(){
		myPeakDaily.setText(myActivity.getString(R.string.peak_offpeak_land_used));
		myPeakDailyData.setText(getPeakDailyDataUsedMb());
	}
	
	private void setStatus(){
		// Check if peak is shaped
		if (isCurrentPeakShaped()){
			// It is so set shaped
			myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_shaped));
		}
		// Else we are unshaped
		else {
			// So set status unshaped
			myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_unshaped));
		}
	}
	
	private void setPeakPeriod(){
		myPeakPeriod.setText(getPeakPeriodLand());
	}

}
