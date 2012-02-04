package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class OffpeakStatsView extends AccountHelper {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = OffpeakStatsView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	// Type face object for custom font
	private final Typeface myFontNumber;
	
	// TextView objects for offpeak title
	private final TextView myOffpeakPeriod;
	
	// TextView objects for Daily Average block
	private final TextView myOffpeakDailyUsedTitle;
	private final TextView myOffpeakDailySlashTitle;
	private final TextView myOffpeakDailySuggestedTitle;
	private final TextView myOffpeakDailyData;
	
	// TextView objects for Data block
	private final TextView myOffpeakDataUsedTitle;
	private final TextView myOffpeakDataSlashTitle;
	private final TextView myOffpeakDataRemainingTitle;
	private final TextView myOffpeakDataData;
	
	// TextView objects for Percent Used block
	private final TextView myOffpeakPercentUsedTitle;
	private final TextView myOffpeakPercentSlashTitle;
	private final TextView myOffpeakPercentRemainingTitle;
	private final TextView myOffpeakNumberData;
	private final TextView myOffpeakNumberUnit;
	
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
	public OffpeakStatsView (Context context){
		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Set custom font
		myFontNumber = Typeface.createFromAsset(((ContextWrapper) myActivity).getAssets(), "OSP-DIN.ttf"); 
		
		// Set offpeak title objects
		myOffpeakPeriod = (TextView) myActivity.findViewById(R.id.offpeak_time);
		
		// Set Daily Average TextView objects
		myOffpeakDailyUsedTitle = (TextView) myActivity.findViewById(R.id.offpeak_daily_used);
		myOffpeakDailySlashTitle = (TextView) myActivity.findViewById(R.id.offpeak_daily_slash);
		myOffpeakDailySuggestedTitle = (TextView) myActivity.findViewById(R.id.offpeak_daily_suggested);
		myOffpeakDailyData = (TextView) myActivity.findViewById(R.id.offpeak_daily_data);

		// Set Data TextView objects
		myOffpeakDataUsedTitle = (TextView) myActivity.findViewById(R.id.offpeak_data_used);
		myOffpeakDataSlashTitle = (TextView) myActivity.findViewById(R.id.offpeak_data_slash);
		myOffpeakDataRemainingTitle = (TextView) myActivity.findViewById(R.id.offpeak_data_remaining);
		myOffpeakDataData = (TextView) myActivity.findViewById(R.id.offpeak_data_data);
		
		// Set Percent TextView objects
		myOffpeakPercentUsedTitle = (TextView) myActivity.findViewById(R.id.offpeak_percent_used);
		myOffpeakPercentSlashTitle = (TextView) myActivity.findViewById(R.id.offpeak_percent_slash);
		myOffpeakPercentRemainingTitle = (TextView) myActivity.findViewById(R.id.offpeak_percent_remaining);
		myOffpeakNumberData = (TextView) myActivity.findViewById(R.id.offpeak_number);
		myOffpeakNumberUnit = (TextView) myActivity.findViewById(R.id.offpeak_number_unit);
		
		// Set custom font to number
		myOffpeakNumberData.setTypeface(myFontNumber);
		myOffpeakNumberUnit.setTypeface(myFontNumber);
		
		// Set focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
		
		// Set percent and Gigabyte units
		percentUnit = myActivity.getString(R.string.peak_offpeak_percent);
		gigabyteUnit = myActivity.getString(R.string.peak_offpeak_gigabyte);
	}
	
	public void loadView(){
		
		// Set number block to percent used
		myOffpeakNumberData.setText(getOffpeakDataUsedPercent());
		
		// Set offpeak data to used
		myOffpeakDataData.setText(getOffpeakDataUsedMb());
		
		// Set text to average daily usage
		myOffpeakDailyData.setText(getOffpeakDailyDataUsedMb());
		
		// Set TextView offpeak period
		myOffpeakPeriod.setText(getOffpeakPeriod());
	}
	
	
	/**
	 * Switch unit of number block
	 */
	public void switchUnitNumberBlock(){
		
		// Check to see if percent unit is shown
		if (isPercentUnitShown()){
			// It is so switch to Giga byte unit
			myOffpeakNumberUnit.setText(gigabyteUnit);
			
			// Check if it is used or remaining
			if (isPercentUsedFocus()){
				// Set Gb value to Used TextView
				myOffpeakNumberData.setText(getOffpeakDataUsedGb());
			} 
			else {
				// Set Gb value to TextView
				myOffpeakNumberData.setText(getOffpeakDataRemainingGb());
			}
			
		}
		// Else it must be Giga byte unit
		else{
			// So change to percent unit
			myOffpeakNumberUnit.setText(percentUnit);
			
			// Check if it is used or remaining
			if (isPercentUsedFocus()){
				// Set Gb value to Used TextView
				myOffpeakNumberData.setText(getOffpeakDataUsedPercent());
			} 
			else {
				// Set Gb value to TextView
				myOffpeakNumberData.setText(getOffpeakDataRemainingPercent());
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
		String currentUnit = (String) myOffpeakNumberUnit.getText();
		
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
			myOffpeakPercentUsedTitle.setTextColor(alternateColor);
			myOffpeakPercentRemainingTitle.setTextColor(focusColor);
			
			// Check to see if percent unit is shown
			if (isPercentUnitShown()){
				// Set perecent value to remaining TextView
				myOffpeakNumberData.setText(getOffpeakDataRemainingPercent());
			}
			else {
				// Set Gb value for remaining
				myOffpeakNumberData.setText(getOffpeakDataRemainingGb());
			}
		}
		// Else it must be in Gb so switch focus to percent
		else{
			// Switch focus to used
			myOffpeakPercentUsedTitle.setTextColor(focusColor);
			myOffpeakPercentRemainingTitle.setTextColor(alternateColor);
			
			// Check to see if percent unit is shown
			if (isPercentUnitShown()){
				// Set perecent value to remaining TextView
				myOffpeakNumberData.setText(getOffpeakDataUsedPercent());
			}
			else {
				// Set Gb value for remaining
				myOffpeakNumberData.setText(getOffpeakDataUsedGb());
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
		int currentColor = myOffpeakPercentUsedTitle.getCurrentTextColor();
		
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
			myOffpeakDataUsedTitle.setTextColor(alternateColor);
			myOffpeakDataRemainingTitle.setTextColor(focusColor);
			
			// Set offpeak data to remaining
			myOffpeakDataData.setText(getOffpeakDataRemainingMb());
		}
		// Else remaining has focus
		else{
			// Switch focus to used
			myOffpeakDataUsedTitle.setTextColor(focusColor);
			myOffpeakDataRemainingTitle.setTextColor(alternateColor);
			
			// Set offpeak data to used
			myOffpeakDataData.setText(getOffpeakDataUsedMb());
		}
	}
	
	/**
	 * Check if the Data title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDataUsedFocus(){
		// Get current color value of TextView
		int currentColor = myOffpeakDataUsedTitle.getCurrentTextColor();
		
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
		// Check if daily used has focus
		if (isDailyUsedFocus()){
			// Switch focus to suggested usage
			myOffpeakDailyUsedTitle.setTextColor(alternateColor);
			myOffpeakDailySuggestedTitle.setTextColor(focusColor);
			
			// Set text to suggested usage
			myOffpeakDailyData.setText(getOffpeakDailyDataSuggestedMb());
		}
		else{
			// Switch focus to av daily use
			myOffpeakDailyUsedTitle.setTextColor(focusColor);
			myOffpeakDailySuggestedTitle.setTextColor(alternateColor);
			
			// Set text to average daily usage
			myOffpeakDailyData.setText(getOffpeakDailyDataUsedMb());
			
		}
	}
	
	/**
	 * Check if the Daily Data title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDailyUsedFocus(){
		// Get current color value of TextView
		int currentColor = myOffpeakDailyUsedTitle.getCurrentTextColor();
		
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
