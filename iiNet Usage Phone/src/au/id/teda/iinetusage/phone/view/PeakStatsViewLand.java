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
		percentUnit = myActivity.getString(R.string.peak_offpeak_percent);
		gigabyteUnit = myActivity.getString(R.string.peak_offpeak_gigabyte);
		
		// Initialise focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
	
		// Initialise Daily Average TextView objects
		myPeakData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_data);
		myPeakQuota = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_quota);
		myPeakUnit = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_unit);
		
		// Set type face custom font to number
		myPeakData.setTypeface(myFontNumber);
		myPeakQuota.setTypeface(myFontNumber);
		myPeakUnit.setTypeface(myFontNumber);
	}
	
	public void loadView(){
		
	}

}
