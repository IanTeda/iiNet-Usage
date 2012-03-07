package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class PeakStatsViewPort extends AccountHelper {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = PeakStatsViewPort.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	// Type face object for custom font
	private final Typeface myFontNumber;
	
	// TextView objects for peak title
	private final TextView myPeakPeriod;
	
	// TextView objects for Daily Average block
	private final TextView myPeakDailySoFar;
	private final TextView myPeakDailyToGo;
	private final TextView myPeakDailyData;
	
	// TextView objects for Data block
	private final TextView myPeakDataSoFar;
	private final TextView myPeakDataToGo;
	private final TextView myPeakDataData;
	
	// TextView objects for Percent Used block
	private final TextView myPeakNumberSoFar;
	private final TextView myPeakNumberToGo;
	private final TextView myPeakNumberData;
	private final TextView myPeakNumberUnit;
	
	// String objects for unit in unit block
	private final String percentUnit;
	private final String gigabyteUnit;
	
	// Color values for focus and alternate
	private final int focusColor;
	private final int alternateColor;
	private final int usageAlertColor;
	private final int usageOverColor;
	private final int textColor;
	
	/**
	 * Class constructor
	 * @param context
	 */
	public PeakStatsViewPort (Context context){
		
		// Initialise activity context
		myActivityContext = context;
		
		// Initialise activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
		
		// Initialise custom number font
		myFontNumber = Typeface.createFromAsset(((ContextWrapper) myActivity).getAssets(), "OSP-DIN.ttf"); 
		
		// Initialise peak title objects
		myPeakPeriod = (TextView) myActivity.findViewById(R.id.peak_time);
		
		// Initialise Daily Average TextView objects
		myPeakDailySoFar = (TextView) myActivity.findViewById(R.id.peak_daily_used);
		myPeakDailyToGo = (TextView) myActivity.findViewById(R.id.peak_daily_suggested);
		myPeakDailyData = (TextView) myActivity.findViewById(R.id.peak_daily_data);

		// Initialise Data TextView objects
		myPeakDataSoFar = (TextView) myActivity.findViewById(R.id.peak_data_used);
		myPeakDataToGo = (TextView) myActivity.findViewById(R.id.peak_data_remaining);
		myPeakDataData = (TextView) myActivity.findViewById(R.id.peak_data_data);
		
		// Initialise Percent TextView objects
		myPeakNumberSoFar = (TextView) myActivity.findViewById(R.id.peak_percent_used);
		myPeakNumberToGo = (TextView) myActivity.findViewById(R.id.peak_percent_remaining);
		myPeakNumberData = (TextView) myActivity.findViewById(R.id.peak_number);
		myPeakNumberUnit = (TextView) myActivity.findViewById(R.id.peak_number_unit);
		
		// Initialise focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
		usageAlertColor = myActivity.getResources().getColor(R.color.usage_alert_color);
		usageOverColor = myActivity.getResources().getColor(R.color.usage_over_color);
		textColor = myActivity.getResources().getColor(R.color.application_text_color);

		
		// Initialise percent and Gigabyte units
		percentUnit = myActivity.getString(R.string.peak_offpeak_unit_percent);
		gigabyteUnit = myActivity.getString(R.string.peak_offpeak_unit_gigabyte);
		
		// Set type face custom font to number
		myPeakNumberData.setTypeface(myFontNumber);
		myPeakNumberUnit.setTypeface(myFontNumber);
	}
	
	public void loadView(){
		
		// Set peak number
		setNumberUnitGb();
		setNumberGbSoFar();
		setNumberColor();
		
		// Set peak data to used
		setFocusDataSoFar();
		setDataSoFar();
		
		// Set text to average daily usage
		setFocusDailySoFar();
		setDailySoFar();
		
		// Set TextView peak period
		setPeakPeriod();
	}

	/**
	 * Switch focus of the peak view
	 */
	public void switchPeakView(){
		
		// Check if data focus is on used
		if(isDataFocusSoFar()){
			// Switch focus to remaining
			setToGo();
		}
		// Else data focus is on remaining
		else {
			// Switch focus to used
			setSoFar();
		}
	}
	
	/**
	 * Switch unit of number
	 */
	public void switchNumberUnit(){
		
		// Check to see if percent unit is shown
		if (isPercentUnitShown()){
			// It is so switch to Giga byte unit
			setNumberUnitGb();
			setNumberGb();
		}
		// Else it must be Giga byte unit
		else{
			// So change to percent unit
			setNumberUnitPercent();
			setNumberPercent();
		}
	}

	/**
	 * Switch the focus of Percent block
	 */
	public void switchFocusNumber(){
		// Check if so far has focus
		if (isNumberFocusSoFar()){
			// Switch focus to remaining
			setFocusNumberToGo();
			setNumberToGo();
		}
		// Else it must be in Gb so switch focus to percent
		else{
			// Switch focus to used
			setFocusNumberSoFar();
			setNumberSoFar();
		}
	}

	/**
	 * Switch the focus of Data block
	 */
	public void switchFocusData(){
		// Check if so far ha focus
		if (isDataFocusSoFar()){
			// Set data focus as to go
			setFocusDataToGo();
			setDataToGo();
		}
		// Else remaining has focus
		else{
			// Set data focus as so far
			setFocusDataSoFar();
			setDataSoFar();
		}
	}

	/**
	 * Switch the focus of Daily Data
	 */
	public void switchFocusDaily(){
		// Check daily is set as so far
		if (isDailyFocusSoFar()){
			// Set daily focus as to go
			setFocusDailyToGo();
			setDailyToGo();
		}
		// Else remaining has focus
		else{
			// Set daily focus as so far
			setFocusDailySoFar();
			setDailySoFar();
		}
	}

	/**
	 * Set period (time schedule) for peak usage
	 */
	private void setPeakPeriod() {
		myPeakPeriod.setText(getPeakPeriod());
	}

	/**
	 * Set focus of peak view to remaining
	 */
	private void setToGo(){
		// Set number as to go
		setFocusNumberToGo();
		setNumberToGo();
		
		// Set data as to go
		setFocusDataToGo();
		setDataToGo();
		
		// Set daily as to go
		setFocusDailyToGo();
		setDailyToGo();
		
		// Set color of number
		setNumberColor();
	}

	/**
	 * Set focus of peak view to used
	 */
	private void setSoFar(){
		// Set number as so far
		setFocusNumberSoFar();
		setNumberSoFar();
		
		// Set data as so far
		setFocusDataSoFar();
		setDataSoFar();
		
		// Set daily as so far
		setFocusDailySoFar();
		setDailySoFar();
		
		// Set color of number
		setNumberColor();
	}

	/**
	 * Set unit number block to percent
	 */
	private void setNumberPercent() {
		
		// Set number unit as percent
		setNumberUnitPercent();
		
		// Check if number is set as so far
		if (isNumberFocusSoFar()){
			// It is so set as percent so far
			setNumberPercentSoFar();
		} 
		// Else number must be set as to go
		else {
			// So set number as percent to go
			setNumberPercentToGo();
		}
		
		// Set color of number
		setNumberColor();
	}

	/**
	 * Set number block to Giga Byte
	 */
	private void setNumberGb() {
	
		// Set number unit as Giga Bytes
		setNumberUnitGb();
		
		// Check if number is set as so far
		if (isNumberFocusSoFar()){
			// It is so set number Giga Bytes as so far
			setNumberGbSoFar();
		} 
		// Else number must be set as to go
		else {
			// So set number GB as to go
			setNumberGbToGo();
		}
		
		// Set color of number
		setNumberColor();
	}

	/**
	 * Set focus of percent block to remaining
	 */
	private void setNumberToGo() {
		
		// Change color of title block as to go
		setFocusNumberToGo();
		
		// Check to see if percent unit is shown
		if (isPercentUnitShown()){
			// It is so set number as percent to go
			setNumberPercentToGo();
		}
		// Else Giga Bytes must be showing
		else {
			// So set number as Gigabytes to go
			setNumberGbToGo();
		}
		
		// Set color of number
		setNumberColor();
		
	}

	/**
	 * Set focus of number block as so far
	 */
	private void setNumberSoFar() {
		
		// Change color of title blocks as so far
		setFocusNumberSoFar();
		
		// Check to see if percent unit is shown
		if (isPercentUnitShown()){
			// It is so set number as percent so far
			setNumberPercentSoFar();
		}
		// Else Giga byte must be showing
		else {
			// So set number as Giga Bytes so far
			setNumberGbSoFar();
		}
		
		// Set color of number
		setNumberColor();
	}

	/**
	 * Set the color of number based on status of peak usage
	 */
	private void setNumberColor() {
		// Set text color based on alert
		if (isPeakUsageOver()){
			myPeakNumberData.setTextColor(usageAlertColor);
			myPeakNumberUnit.setTextColor(usageAlertColor);
		}
		else {
			myPeakNumberData.setTextColor(textColor);
			myPeakNumberUnit.setTextColor(textColor);
	
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
		// Must be Giga byte unit
		else {
			// So return false
			return false;
		}
		
		
	}

	/**
	 * Check if the Percent Used title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isNumberFocusSoFar(){
		// Get current color value of TextView
		int currentColor = myPeakNumberSoFar.getCurrentTextColor();
		
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
	 * Check if the Data title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDataFocusSoFar(){
		// Get current color value of TextView
		int currentColor = myPeakDataSoFar.getCurrentTextColor();
		
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
	 * Check if the Daily Data title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDailyFocusSoFar(){
		// Get current color value of TextView
		int currentColor = myPeakDailyToGo.getCurrentTextColor();
		
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
	 * Set peak unit to Giga Byte
	 */
	private void setNumberUnitGb() {
		myPeakNumberUnit.setText(gigabyteUnit);
	}

	/**
	 * Set peak unit to percent
	 */
	private void setNumberUnitPercent() {
		myPeakNumberUnit.setText(percentUnit);
	}

	/**
	 * Set peak number to Giga Byte to go
	 */
	private void setNumberGbToGo() {
		// Set Gb value to TextView
		myPeakNumberData.setText(getPeakGbToGo());
	}

	/**
	 * Set peak number to Giga Byte so far
	 */
	private void setNumberGbSoFar() {
		myPeakNumberData.setText(getPeakGbSoFar());
	}

	/**
	 * Set peak number to percent to go
	 */
	private void setNumberPercentToGo() {
		myPeakNumberData.setText(getPeakPercentToGo());
	}

	/**
	 * Set peak number to percent so far
	 */
	private void setNumberPercentSoFar() {
		myPeakNumberData.setText(getPeakPercentSoFar());
	}

	/**
	 * Set data block focus to used
	 */
	private void setDataSoFar() {
		myPeakDataData.setText(getPeakMbSoFar());
	}

	/**
	 * Set data block focus to remaining
	 */
	private void setDataToGo() {
		myPeakDataData.setText(getPeakMbToGo());
	}

	/**
	 * Set daily block as so far
	 */
	private void setDailySoFar() {
		myPeakDailyData.setText(getPeakDailyMbSoFar());
	}

	/**
	 * Set daily block as to go
	 */
	private void setDailyToGo() {
		myPeakDailyData.setText(getPeakDailyMbToGo());
	}

	/**
	 * Set focus of number as to go
	 */
	private void setFocusNumberToGo() {
		myPeakNumberSoFar.setTextColor(alternateColor);
		myPeakNumberToGo.setTextColor(focusColor);
	}

	/**
	 * Set focus of number as so far
	 */
	private void setFocusNumberSoFar() {
		myPeakNumberSoFar.setTextColor(focusColor);
		myPeakNumberToGo.setTextColor(alternateColor);
	}

	/**
	 * Set focus of data as to go
	 */
	private void setFocusDataToGo() {
		myPeakDataSoFar.setTextColor(alternateColor);
		myPeakDataToGo.setTextColor(focusColor);
	}

	/**
	 * Set data focus as so far
	 */
	private void setFocusDataSoFar() {
		myPeakDataSoFar.setTextColor(focusColor);
		myPeakDataToGo.setTextColor(alternateColor);
	}

	/**
	 * Set focus of daily data as to go
	 */
	private void setFocusDailyToGo() {
		myPeakDailySoFar.setTextColor(alternateColor);
		myPeakDailyToGo.setTextColor(focusColor);
	}

	/**
	 * Set focus of daily data as so far
	 */
	private void setFocusDailySoFar() {
		myPeakDailySoFar.setTextColor(focusColor);
		myPeakDailyToGo.setTextColor(alternateColor);
	}

}
