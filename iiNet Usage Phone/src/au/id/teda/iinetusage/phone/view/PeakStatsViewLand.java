package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class PeakStatsViewLand extends AccountHelper {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = PeakStatsViewLand.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	// Type face object for custom font
	private final Typeface myFontNumber;
	
	// String objects for unit in unit block
	private final String percentUnit;
	private final String gigabyteUnit;
	
	// Color values for status shaped/unshaped
	private final int alertColor;
	private final int normalColor;
	
	// TextView objects for peak number
	private final TextView myPeakData;
	private final TextView myPeakQuota;
	private final TextView myPeakUnit;
	
	// TextView objects for peak number
	private final TextView myPeakDailyData;
	
	// TextView object for peak status
	private final TextView myStatus;
	
	// TextView object for peak period
	private final TextView myPeakPeriod;
	
	// TextView object for peak title
	private final TextView myPeakTitle;
	
	// String object for peak title
	private final String peakTitleToGo;
	private final String peakTitleSoFar;
	
	// Color values for usage alerts
	private final int usageAlertColor;
	private final int usageOverColor;
	private final int textColor;
	
	
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
		alertColor = myActivity.getResources().getColor(R.color.red);
		normalColor = myActivity.getResources().getColor(R.color.white);
	
		// Initialise Peak Data TextView objects
		myPeakData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_data);
		myPeakQuota = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_quota);
		myPeakUnit = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_unit);
		
		// Initialise Daily Average TextView objects
		myPeakDailyData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_suggested_remaining_data);
		
		// Initialise status TextView objects
		myStatus = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_status_data);
		
		// Initialise peak period TextView objects
		myPeakPeriod = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_period_data);
		
		// Initialise peak title TextView objects
		myPeakTitle = (TextView) myActivity.findViewById(R.id.dashboard_landscape_peak_title);
		peakTitleToGo = myActivity.getString(R.string.peak_land_toGo);
		peakTitleSoFar = myActivity.getString(R.string.peak_land_soFar);
		
		// Initialise colours
		usageAlertColor = myActivity.getResources().getColor(R.color.usage_alert_color);
		usageOverColor = myActivity.getResources().getColor(R.color.usage_over_color);
		textColor = myActivity.getResources().getColor(R.color.application_text_color);
		
		// Set type face custom font to number
		myPeakData.setTypeface(myFontNumber);
		myPeakQuota.setTypeface(myFontNumber);
		myPeakUnit.setTypeface(myFontNumber);
	}
	
	/**
	 * Load PeakStatsViewLand view
	 */
	public void loadView(){
		
		// Set peak GB used
		setPeakGb();
		
		// Set current peak status
		setStatus();
		
		// Set peak period
		setPeakPeriod();
		
		// Set peak view
		setDailyToGo();
		
	}
	
	/**
	 * Switch between Days So Far and Days To Go
	 */
	public void switchPeakData(){
		// Check if to go is set
		if (isToGoSet()){
			// Switch to so far
			setTitleSoFar();
			setDailySoFar();
			
		}
		// Else so far must be set
		else {
			// Switch to to go
			setTitleToGo();
			setDailyToGo();
			
		}
		
		// Check if Giga Bytes is set
		if (isPeakGbSet()){
			// Set Giga byte data
			setPeakGb();
		} 
		// Else percent must be set
		else {
			// Set percent data
			setPeakPercent();
		}
	}

	/**
	 * Switch between percent and Giga Byte
	 */
	public void switchPeakUnit(){
		// Check if Giga Byte unit is shown
		if (isPeakGbSet()){
			// Looks like it is so switch to percent
			setPeakPercent();
		}
		// Else percent is shown
		else {
			// So switch to Gigabyte
			setPeakGb();
		}
	}
	
	/**
	 * Check to see if days to go is set
	 * @return true if set
	 */
	private boolean isToGoSet(){
		// Get current peak title from text view
		String currentTitle = (String) myPeakTitle.getText();
			
		// Check if current unit string matches Giga byte unit string stored in xml
		if (currentTitle == peakTitleToGo){
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
	 * Check to see if Giga Bytes is set
	 * @return true if it is
	 */
	private boolean isPeakGbSet(){
			
		// Get current unit from text view
		String currentUnit = (String) myPeakUnit.getText();
			
		// Check if current unit string matches Giga byte unit string stored in xml
		if (currentUnit == gigabyteUnit){
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
	 * Set peak data to Giga Bytes
	 */
	private void setPeakGb(){
		
		// We need to show quota for Gb
		showQuota();
		
		// Set peak GB quota
		setQuota();
		
		// Check if peak to go is set
		if (isToGoSet()){
			// Set peak data as to go
			setGbToGo();
		} 
		// Else So Far must be set
		else {
			// Set peak data as used
			setGbSoFar();
		}
		
		// Set acolor based on status
		setNumberColor();
		
	}

	/**
	 * Set peak data to percent
	 */
	private void setPeakPercent(){
		
		// Don't need the quota for percent so hide it
		hideQuota();
		
		// Check if peak to go is set
		if (isToGoSet()){
			setPercentToGo();
		} 
		// Else So Far must be set
		else {
			setPercentSoFar();
		}
		
		// Set acolor based on status
		setNumberColor();
	}

	/**
	 * Set current peak status
	 */
	private void setStatus(){
		// Check if peak is shaped
		if (isCurrentPeakShaped()){
			// It is so set shaped
			setStatusShaped();
		}
		// Else we are unshaped
		else {
			// So set status unshaped
			setStatusUnshaped();
		}
	}

	/**
	 * Set peak status as unshaped
	 */
	private void setStatusUnshaped() {
		myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_unshaped));
		myStatus.setTextColor(normalColor);
	}

	/**
	 * Set peak status as shaped
	 */
	private void setStatusShaped() {
		myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_shaped));
		myStatus.setTextColor(alertColor);
	}
	
	/**
	 * Set peak period times
	 */
	private void setPeakPeriod(){
		myPeakPeriod.setText(getPeakPeriodLand());
	}

	/**
	 * Set title as to go
	 */
	private void setTitleToGo() {
		myPeakTitle.setText(peakTitleToGo);
	}

	/**
	 * Set title as so far
	 */
	private void setTitleSoFar() {
		myPeakTitle.setText(peakTitleSoFar);
	}

	/**
	 * Set Giga Bytes to go
	 */
	private void setGbToGo() {
		myPeakData.setText(getPeakGbToGo());
	}

	/**
	 * Set Giga Bytes so far
	 */
	private void setGbSoFar() {
		myPeakData.setText(getPeakGbSoFar());
	}

	/**
	 * 
	 */
	private void setQuota() {
		myPeakQuota.setText(getPeakQuotaStringGBLand());
	}

	/**
	 * Set percent to go
	 */
	private void setPercentToGo() {
		myPeakData.setText(getPeakPercentToGo());
	}

	/**
	 * Set percent so far
	 */
	private void setPercentSoFar() {
		myPeakData.setText(getPeakPercentSoFar());
	}

	/**
	 * Set peak daily data as Days To Go
	 */
	private void setDailyToGo(){
		myPeakDailyData.setText(getPeakDailyMbToGo());
	}

	/**
	 * Set peak daily data as Days So Far
	 */
	private void setDailySoFar(){
		myPeakDailyData.setText(getPeakDailyMbSoFar());
	}

	/**
	 * Show quota
	 */
	private void showQuota() {
		myPeakQuota.setVisibility(View.VISIBLE);
		myPeakUnit.setText(gigabyteUnit);
	}

	/**
	 * Hide quota
	 */
	private void hideQuota() {
		myPeakQuota.setVisibility(View.GONE);
		myPeakUnit.setText(percentUnit);
	}
	
	/**
	 * Set the color of number based on status of peak usage
	 */
	private void setNumberColor() {
		// Set text color based on alert
		if (isPeakUsageOver()){
			myPeakData.setTextColor(usageAlertColor);
			myPeakQuota.setTextColor(usageAlertColor);
			myPeakUnit.setTextColor(usageAlertColor);
		}
		else {
			myPeakData.setTextColor(textColor);
			myPeakQuota.setTextColor(textColor);
			myPeakUnit.setTextColor(textColor);
		}
	}

}
