package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class PeakStatsView extends AccountHelper {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = PeakStatsView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	// TextView objects for Daily Average block
	private final TextView myPeakDailyUsedTitle;
	private final TextView myPeakDailySlashTitle;
	private final TextView myPeakDailySuggestedTitle;
	private final TextView myPeakDailyData;
	
	// TextView objects for Data block
	private final TextView myPeakDataUsedTitle;
	private final TextView myPeakDataSlashTitle;
	private final TextView myPeakDataRemainingTitle;
	private final TextView myPeakDataData;
	
	// TextView objects for Percent Used block
	private final TextView myPeakPercentUsedTitle;
	private final TextView myPeakPercentSlashTitle;
	private final TextView myPeakPercentRemainingTitle;
	private final TextView myPeakNumberData;
	
	
	
	// Color values for focus and alternate
	private final int focusColor;
	private final int alternateColor;
	
	/**
	 * Class constructor
	 * @param context
	 */
	public PeakStatsView (Context context){
		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Set Daily Average TextView objects
		myPeakDailyUsedTitle = (TextView) myActivity.findViewById(R.id.peak_daily_used);
		myPeakDailySlashTitle = (TextView) myActivity.findViewById(R.id.peak_daily_slash);
		myPeakDailySuggestedTitle = (TextView) myActivity.findViewById(R.id.peak_daily_suggested);
		myPeakDailyData = (TextView) myActivity.findViewById(R.id.peak_daily_data);

		// Set Data TextView objects
		myPeakDataUsedTitle = (TextView) myActivity.findViewById(R.id.peak_data_used);
		myPeakDataSlashTitle = (TextView) myActivity.findViewById(R.id.peak_data_slash);
		myPeakDataRemainingTitle = (TextView) myActivity.findViewById(R.id.peak_data_remaing);
		myPeakDataData = (TextView) myActivity.findViewById(R.id.peak_data_data);
		
		// Set Percent TextView objects
		myPeakPercentUsedTitle = (TextView) myActivity.findViewById(R.id.peak_percent_used);
		myPeakPercentSlashTitle = (TextView) myActivity.findViewById(R.id.peak_percent_slash);
		myPeakPercentRemainingTitle = (TextView) myActivity.findViewById(R.id.peak_percent_remaing);
		myPeakNumberData = (TextView) myActivity.findViewById(R.id.peak_number);
		
		
		// Set focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
	}
	
	/**
	 * Switch the focus of Percent block
	 */
	public void switchFocusPercentBlock(){
		if (isPercentUsedFocus()){
			myPeakPercentUsedTitle.setTextColor(alternateColor);
			myPeakPercentRemainingTitle.setTextColor(focusColor);
		}
		else{
			myPeakPercentUsedTitle.setTextColor(focusColor);
			myPeakPercentRemainingTitle.setTextColor(alternateColor);
		}
	}
	
	/**
	 * Check if the Percent Used title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isPercentUsedFocus(){
		// Get current color value of TextView
		int currentColor = myPeakPercentUsedTitle.getCurrentTextColor();
		
		// Check current colour against XML defined focus color
		if (currentColor == focusColor){
			// Looks like current color matches stored value so return true
			return true;
		}
		// Else it must be the alternate color
		else {
			// So return false (shaded)
			return false;
		}
	}
	
	
	/**
	 * Switch the focus of Data block
	 */
	public void switchFocusDataBlock(){
		if (isDataUsedFocus()){
			myPeakDataUsedTitle.setTextColor(alternateColor);
			myPeakDataRemainingTitle.setTextColor(focusColor);
		}
		else{
			myPeakDataUsedTitle.setTextColor(focusColor);
			myPeakDataRemainingTitle.setTextColor(alternateColor);
		}
	}
	
	/**
	 * Check if the Data title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDataUsedFocus(){
		// Get current color value of TextView
		int currentColor = myPeakDataUsedTitle.getCurrentTextColor();
		
		// Check current colour against XML defined focus color
		if (currentColor == focusColor){
			// Looks like current color matches stored value so return true
			return true;
		}
		// Else it must be the alternate color
		else {
			// So return false (shaded)
			return false;
		}
	}
	
	/**
	 * Switch the focus of Daily Data block
	 */
	public void switchFocusDailyBlock(){
		if (isDailyUsedFocus()){
			myPeakDailyUsedTitle.setTextColor(alternateColor);
			myPeakDailySuggestedTitle.setTextColor(focusColor);
		}
		else{
			myPeakDailyUsedTitle.setTextColor(focusColor);
			myPeakDailySuggestedTitle.setTextColor(alternateColor);
		}
	}
	
	/**
	 * Check if the Daily Data title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDailyUsedFocus(){
		// Get current color value of TextView
		int currentColor = myPeakDailyUsedTitle.getCurrentTextColor();
		
		// Check current colour against XML defined focus color
		if (currentColor == focusColor){
			// Looks like current color matches stored value so return true
			return true;
		}
		// Else it must be the alternate color
		else {
			// So return false (shaded)
			return false;
		}
	}

}
