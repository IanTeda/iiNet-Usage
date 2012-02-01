package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
	private final TextView myPeakNumberUnit;
	
	// String objects for unit in unit block
	private final String percentUnit;
	private final String gigabyteUnit;
	
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
		myPeakDataRemainingTitle = (TextView) myActivity.findViewById(R.id.peak_data_remaining);
		myPeakDataData = (TextView) myActivity.findViewById(R.id.peak_data_data);
		
		// Set Percent TextView objects
		myPeakPercentUsedTitle = (TextView) myActivity.findViewById(R.id.peak_percent_used);
		myPeakPercentSlashTitle = (TextView) myActivity.findViewById(R.id.peak_percent_slash);
		myPeakPercentRemainingTitle = (TextView) myActivity.findViewById(R.id.peak_percent_remaining);
		myPeakNumberData = (TextView) myActivity.findViewById(R.id.peak_number);
		myPeakNumberUnit = (TextView) myActivity.findViewById(R.id.peak_number_unit);
		
		// Set focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
		
		// Set percent and Gigabyte units
		percentUnit = myActivity.getString(R.string.peak_offpeak_percent);
		gigabyteUnit = myActivity.getString(R.string.peak_offpeak_gigabyte);
	}
	
	public void loadView(){
		
		// Set peak data to used
		myPeakDataData.setText(getPeakDataUsedMb());
	}
	
	
	/**
	 * Switch unit of number block
	 */
	public void switchUnitNumberBlock(){
		
		// Check to see if percent unit is shown
		if (isPercentUnitShown()){
			// It is so switch to Giga byte unit
			myPeakNumberUnit.setText(gigabyteUnit);
			
			// Check if it is used or remaining
			if (isPercentUsedFocus()){
				// Set Gb value to Used TextView
				myPeakNumberData.setText(getPeakDataUsedGb());
			} 
			else {
				// Set Gb value to TextView
				myPeakNumberData.setText(getPeakDataRemainingGb());
			}
			
		}
		// Else it must be Giga byte unit
		else{
			// So change to percent unit
			myPeakNumberUnit.setText(percentUnit);
			
			// Check if it is used or remaining
			if (isPercentUsedFocus()){
				// Set Gb value to Used TextView
				myPeakNumberData.setText(getPeakDataUsedPercent());
			} 
			else {
				// Set Gb value to TextView
				myPeakNumberData.setText(getPeakDataRemainingPercent());
			}
		}
	}
	
	/**
	 * Check if the Percent Used title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isPercentUnitShown(){
		
		// Get current unit from text view
		String currentUnit = (String) myPeakNumberUnit.getText();
		
		// Check if current unit string matches percent unit string stored in xml
		if (currentUnit == percentUnit){
			// Looks like it is so return true
			return true;
		}
		// Must be Gigabyte unit
		else {
			// So return false
			return false;
		}
		
		
	}
	
	
	/**
	 * Switch the focus of Percent block
	 */
	public void switchFocusPercentBlock(){
		// Check to see if used has focus
		if (isPercentUsedFocus()){
			// Switch focus to remaining
			myPeakPercentUsedTitle.setTextColor(alternateColor);
			myPeakPercentRemainingTitle.setTextColor(focusColor);
			
			// Check to see if percent unit is shown
			if (isPercentUnitShown()){
				// Set perecent value to remaining TextView
				myPeakNumberData.setText(getPeakDataRemainingPercent());
			}
			else {
				// Set Gb value for remaining
				myPeakNumberData.setText(getPeakDataRemainingGb());
			}
		}
		// Else it must be in Gb so switch focus to percent
		else{
			// Switch focus to used
			myPeakPercentUsedTitle.setTextColor(focusColor);
			myPeakPercentRemainingTitle.setTextColor(alternateColor);
			
			// Check to see if percent unit is shown
			if (isPercentUnitShown()){
				// Set perecent value to remaining TextView
				myPeakNumberData.setText(getPeakDataUsedPercent());
			}
			else {
				// Set Gb value for remaining
				myPeakNumberData.setText(getPeakDataUsedGb());
			}
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
		// Check if used has focus
		if (isDataUsedFocus()){
			// Switch focus to data remaining
			myPeakDataUsedTitle.setTextColor(alternateColor);
			myPeakDataRemainingTitle.setTextColor(focusColor);
			
			// Set peak data to remaining
			myPeakDataData.setText(getPeakDataRemainingMb());
		}
		// Else remaining has focus
		else{
			// Switch focus to used
			myPeakDataUsedTitle.setTextColor(focusColor);
			myPeakDataRemainingTitle.setTextColor(alternateColor);
			
			// Set peak data to used
			myPeakDataData.setText(getPeakDataUsedMb());
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
