package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class OffpeakStatsViewPort extends AccountHelper {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = OffpeakStatsView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	// Type face object for custom font
	private final Typeface myFontNumber;
	
	// TextView objects for offpeak title
	private final TextView myOffpeakPeriod;
	
	// TextView objects for Daily Average block
	private final TextView myOffpeakDailyUsedTitle;
	private final TextView myOffpeakDailySuggestedTitle;
	private final TextView myOffpeakDailyData;
	
	// TextView objects for Data block
	private final TextView myOffpeakDataUsedTitle;
	private final TextView myOffpeakDataRemainingTitle;
	private final TextView myOffpeakDataData;
	
	// TextView objects for Percent Used block
	private final TextView myOffpeakPercentUsedTitle;
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
	public OffpeakStatsViewPort (Context context){
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
		myOffpeakDailySuggestedTitle = (TextView) myActivity.findViewById(R.id.offpeak_daily_suggested);
		myOffpeakDailyData = (TextView) myActivity.findViewById(R.id.offpeak_daily_data);

		// Set Data TextView objects
		myOffpeakDataUsedTitle = (TextView) myActivity.findViewById(R.id.offpeak_data_used);
		myOffpeakDataRemainingTitle = (TextView) myActivity.findViewById(R.id.offpeak_data_remaining);
		myOffpeakDataData = (TextView) myActivity.findViewById(R.id.offpeak_data_data);
		
		// Set Percent TextView objects
		myOffpeakPercentUsedTitle = (TextView) myActivity.findViewById(R.id.offpeak_percent_used);
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
		percentUnit = myActivity.getString(R.string.peak_offpeak_unit_percent);
		gigabyteUnit = myActivity.getString(R.string.peak_offpeak_unit_gigabyte);
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
	 * Switch focus of the peak view
	 */
	public void switchOffpeakView(){
		
		// Check if data focus is on used
		if(isDataUsedFocus()){
			// Switch focus to remaining
			setOffpeakRemaining();
		}
		// Else data focus is on remaining
		else {
			// Switch focus to used
			setOffpeakUsed();
		}
	}
	
	/**
	 * Set focus of peak view to remaining
	 */
	private void setOffpeakRemaining(){
		setPercentRemaining();
		setDataRemaining();
		setDailySuggested();
	}
	
	/**
	 * Set focus of peak view to used
	 */
	private void setOffpeakUsed(){
		setPercentUsed();
		setDataUsed();
		setDailyUsed();
	}
	
	
	/**
	 * Switch unit of number block
	 */
	public void switchUnitNumberBlock(){
		
		// Check to see if percent unit is shown
		if (isPercentUnitShown()){
			// It is so switch to Giga byte unit
			setGigaByteUnit();
			
		}
		// Else it must be Giga byte unit
		else{
			// So change to percent unit
			setPercentUnit();
		}
	}

	/**
	 * Set focus of unit block to percent
	 */
	private void setPercentUnit() {
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

	/**
	 * Set focus of unit block to Giga Byte
	 */
	private void setGigaByteUnit() {
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
			setPercentRemaining();
		}
		// Else it must be in Gb so switch focus to percent
		else{
			// Switch focus to used
			setPercentUsed();
		}
	}

	/**
	 * Set percent block to used
	 */
	private void setPercentUsed() {
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

	/**
	 * Set percent block to remaining
	 */
	private void setPercentRemaining() {
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
			setDataRemaining();
		}
		// Else remaining has focus
		else{
			// Switch focus to used
			setDataUsed();
		}
	}

	/**
	 * Set data block to used
	 */
	private void setDataUsed() {
		myOffpeakDataUsedTitle.setTextColor(focusColor);
		myOffpeakDataRemainingTitle.setTextColor(alternateColor);
		
		// Set offpeak data to used
		myOffpeakDataData.setText(getOffpeakDataUsedMb());
	}

	/**
	 * Set data block to remaining
	 */
	private void setDataRemaining() {
		myOffpeakDataUsedTitle.setTextColor(alternateColor);
		myOffpeakDataRemainingTitle.setTextColor(focusColor);
		
		// Set offpeak data to remaining
		myOffpeakDataData.setText(getOffpeakDataRemainingMb());
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
			setDailySuggested();
		}
		else{
			// Switch focus to av daily use
			setDailyUsed();
			
		}
	}

	/**
	 * Set daily block to used
	 */
	private void setDailyUsed() {
		myOffpeakDailyUsedTitle.setTextColor(focusColor);
		myOffpeakDailySuggestedTitle.setTextColor(alternateColor);
		
		// Set text to average daily usage
		myOffpeakDailyData.setText(getOffpeakDailyDataUsedMb());
	}

	/**
	 * Set daily block to suggested
	 */
	private void setDailySuggested() {
		myOffpeakDailyUsedTitle.setTextColor(alternateColor);
		myOffpeakDailySuggestedTitle.setTextColor(focusColor);
		
		// Set text to suggested usage
		myOffpeakDailyData.setText(getOffpeakDailyDataSuggestedMb());
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
