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
		//private static final String INFO_TAG = OffpeakStatsViewPort.class.getSimpleName();
		
		private final Context myActivityContext;
		private final Activity myActivity;
		
		// Type face object for custom font
		private final Typeface myFontNumber;
		
		// TextView objects for offpeak title
		private final TextView myOffpeakPeriod;
		
		// TextView objects for Daily Average block
		private final TextView myOffpeakDailySoFar;
		private final TextView myOffpeakDailyToGo;
		private final TextView myOffpeakDailyData;
		
		// TextView objects for Data block
		private final TextView myOffpeakDataSoFar;
		private final TextView myOffpeakDataToGo;
		private final TextView myOffpeakDataData;
		
		// TextView objects for Percent Used block
		private final TextView myOffpeakNumberSoFar;
		private final TextView myOffpeakNumberToGo;
		private final TextView myOffpeakNumberData;
		private final TextView myOffpeakNumberUnit;
		
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
		public OffpeakStatsViewPort (Context context){
			
			// Initialise activity context
			myActivityContext = context;
			
			// Initialise activity from context
			myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;
			
			// Initialise custom number font
			myFontNumber = Typeface.createFromAsset(((ContextWrapper) myActivity).getAssets(), "OSP-DIN.ttf"); 
			
			// Initialise offpeak title objects
			myOffpeakPeriod = (TextView) myActivity.findViewById(R.id.offpeak_time);
			
			// Initialise Daily Average TextView objects
			myOffpeakDailySoFar = (TextView) myActivity.findViewById(R.id.offpeak_daily_used);
			myOffpeakDailyToGo = (TextView) myActivity.findViewById(R.id.offpeak_daily_suggested);
			myOffpeakDailyData = (TextView) myActivity.findViewById(R.id.offpeak_daily_data);

			// Initialise Data TextView objects
			myOffpeakDataSoFar = (TextView) myActivity.findViewById(R.id.offpeak_data_used);
			myOffpeakDataToGo = (TextView) myActivity.findViewById(R.id.offpeak_data_remaining);
			myOffpeakDataData = (TextView) myActivity.findViewById(R.id.offpeak_data_data);
			
			// Initialise Percent TextView objects
			myOffpeakNumberSoFar = (TextView) myActivity.findViewById(R.id.offpeak_percent_used);
			myOffpeakNumberToGo = (TextView) myActivity.findViewById(R.id.offpeak_percent_remaining);
			myOffpeakNumberData = (TextView) myActivity.findViewById(R.id.offpeak_number);
			myOffpeakNumberUnit = (TextView) myActivity.findViewById(R.id.offpeak_number_unit);
			
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
			myOffpeakNumberData.setTypeface(myFontNumber);
			myOffpeakNumberUnit.setTypeface(myFontNumber);
		}
		
		public void loadView(){
			
			// Set offpeak number
			setNumberUnitGb();
			setNumberGbSoFar();
			setNumberColor();
			
			// Set offpeak data to used
			setFocusDataSoFar();
			setDataSoFar();
			
			// Set text to average daily usage
			setFocusDailySoFar();
			setDailySoFar();
			
			// Set TextView offpeak period
			setOffpeakPeriod();
		}

		/**
		 * Switch focus of the offpeak view
		 */
		public void switchOffpeakView(){
			
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
		 * Set period (time schedule) for offpeak usage
		 */
		private void setOffpeakPeriod() {
			myOffpeakPeriod.setText(getOffpeakPeriod());
		}

		/**
		 * Set focus of offpeak view to remaining
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
		 * Set focus of offpeak view to used
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
		 * Set the color of number based on status of offpeak usage
		 */
		private void setNumberColor() {
			// Set text color based on alert
			if (isCurrentOffpeakShaped()){
				myOffpeakNumberData.setTextColor(usageOverColor);
				myOffpeakNumberUnit.setTextColor(usageOverColor);
			}
			else if (isOffpeakUsageOver()){
				myOffpeakNumberData.setTextColor(usageAlertColor);
				myOffpeakNumberUnit.setTextColor(usageAlertColor);
			}
			else {
				myOffpeakNumberData.setTextColor(textColor);
				myOffpeakNumberUnit.setTextColor(textColor);
		
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
			int currentColor = myOffpeakNumberSoFar.getCurrentTextColor();
			
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
			int currentColor = myOffpeakDataSoFar.getCurrentTextColor();
			
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
			int currentColor = myOffpeakDailyToGo.getCurrentTextColor();
			
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
		 * Set offpeak unit to Giga Byte
		 */
		private void setNumberUnitGb() {
			myOffpeakNumberUnit.setText(gigabyteUnit);
		}

		/**
		 * Set offpeak unit to percent
		 */
		private void setNumberUnitPercent() {
			myOffpeakNumberUnit.setText(percentUnit);
		}

		/**
		 * Set offpeak number to Giga Byte to go
		 */
		private void setNumberGbToGo() {
			// Set Gb value to TextView
			myOffpeakNumberData.setText(getOffpeakGbToGo());
		}

		/**
		 * Set offpeak number to Giga Byte so far
		 */
		private void setNumberGbSoFar() {
			myOffpeakNumberData.setText(getOffpeakGbSoFar());
		}

		/**
		 * Set offpeak number to percent to go
		 */
		private void setNumberPercentToGo() {
			myOffpeakNumberData.setText(getOffpeakPercentToGo());
		}

		/**
		 * Set offpeak number to percent so far
		 */
		private void setNumberPercentSoFar() {
			myOffpeakNumberData.setText(getOffpeakPercentSoFar());
		}

		/**
		 * Set data block focus to used
		 */
		private void setDataSoFar() {
			myOffpeakDataData.setText(getOffpeakMbSoFar());
		}

		/**
		 * Set data block focus to remaining
		 */
		private void setDataToGo() {
			myOffpeakDataData.setText(getOffpeakMbToGo());
		}

		/**
		 * Set daily block as so far
		 */
		private void setDailySoFar() {
			myOffpeakDailyData.setText(getOffpeakDailyMbSoFar());
		}

		/**
		 * Set daily block as to go
		 */
		private void setDailyToGo() {
			myOffpeakDailyData.setText(getOffpeakDailyMbToGo());
		}

		/**
		 * Set focus of number as to go
		 */
		private void setFocusNumberToGo() {
			myOffpeakNumberSoFar.setTextColor(alternateColor);
			myOffpeakNumberToGo.setTextColor(focusColor);
		}

		/**
		 * Set focus of number as so far
		 */
		private void setFocusNumberSoFar() {
			myOffpeakNumberSoFar.setTextColor(focusColor);
			myOffpeakNumberToGo.setTextColor(alternateColor);
		}

		/**
		 * Set focus of data as to go
		 */
		private void setFocusDataToGo() {
			myOffpeakDataSoFar.setTextColor(alternateColor);
			myOffpeakDataToGo.setTextColor(focusColor);
		}

		/**
		 * Set data focus as so far
		 */
		private void setFocusDataSoFar() {
			myOffpeakDataSoFar.setTextColor(focusColor);
			myOffpeakDataToGo.setTextColor(alternateColor);
		}

		/**
		 * Set focus of daily data as to go
		 */
		private void setFocusDailyToGo() {
			myOffpeakDailySoFar.setTextColor(alternateColor);
			myOffpeakDailyToGo.setTextColor(focusColor);
		}

		/**
		 * Set focus of daily data as so far
		 */
		private void setFocusDailySoFar() {
			myOffpeakDailySoFar.setTextColor(focusColor);
			myOffpeakDailyToGo.setTextColor(alternateColor);
		}

	}