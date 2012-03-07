package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class OffpeakStatsViewLand extends AccountHelper {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = OffpeakStatsViewLand.class.getSimpleName();
	
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
	
	// TextView objects for offpeak number
	private final TextView myOffpeakData;
	private final TextView myOffpeakQuota;
	private final TextView myOffpeakUnit;
	
	// TextView objects for offpeak number
	private final TextView myOffpeakDailyData;
	
	// TextView object for offpeak status
	private final TextView myStatus;
	
	// TextView object for offpeak period
	private final TextView myOffpeakPeriod;
	
	// TextView object for offpeak title
	private final TextView myOffpeakTitle;
	
	// String object for offpeak title
	private final String offpeakTitleToGo;
	private final String offpeakTitleSoFar;
	
	// Color values for usage alerts
	private final int usageAlertColor;
	private final int usageOverColor;
	private final int textColor;
	
	
	/**
	 * Class constructor
	 * @param context
	 */
	public OffpeakStatsViewLand (Context context){
		
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
	
		// Initialise Offpeak Data TextView objects
		myOffpeakData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_data);
		myOffpeakQuota = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_quota);
		myOffpeakUnit = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_unit);
		
		// Initialise Daily Average TextView objects
		myOffpeakDailyData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_suggested_remaining_data);
		
		// Initialise status TextView objects
		myStatus = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_status_data);
		
		// Initialise offpeak period TextView objects
		myOffpeakPeriod = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_period_data);
		
		// Initialise offpeak title TextView objects
		myOffpeakTitle = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_title);
		offpeakTitleToGo = myActivity.getString(R.string.offpeak_land_toGo);
		offpeakTitleSoFar = myActivity.getString(R.string.offpeak_land_soFar);
		
		// Initialise colours
		usageAlertColor = myActivity.getResources().getColor(R.color.usage_alert_color);
		usageOverColor = myActivity.getResources().getColor(R.color.usage_over_color);
		textColor = myActivity.getResources().getColor(R.color.application_text_color);
		
		// Set type face custom font to number
		myOffpeakData.setTypeface(myFontNumber);
		myOffpeakQuota.setTypeface(myFontNumber);
		myOffpeakUnit.setTypeface(myFontNumber);
	}
	
	/**
	 * Load OffpeakStatsViewLand view
	 */
	public void loadView(){
		
		// Set offpeak GB used
		setOffpeakGb();
		
		// Set current offpeak status
		setStatus();
		
		// Set offpeak period
		setOffpeakPeriod();
		
		// Set offpeak view
		setDailyToGo();
		
	}
	
	/**
	 * Switch between Days So Far and Days To Go
	 */
	public void switchOffpeakData(){
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
		if (isOffpeakGbSet()){
			// Set Giga byte data
			setOffpeakGb();
		} 
		// Else percent must be set
		else {
			// Set percent data
			setOffpeakPercent();
		}
	}

	/**
	 * Switch between percent and Giga Byte
	 */
	public void switchOffpeakUnit(){
		// Check if Giga Byte unit is shown
		if (isOffpeakGbSet()){
			// Looks like it is so switch to percent
			setOffpeakPercent();
		}
		// Else percent is shown
		else {
			// So switch to Gigabyte
			setOffpeakGb();
		}
	}
	
	/**
	 * Check to see if days to go is set
	 * @return true if set
	 */
	private boolean isToGoSet(){
		// Get current offpeak title from text view
		String currentTitle = (String) myOffpeakTitle.getText();
			
		// Check if current unit string matches Giga byte unit string stored in xml
		if (currentTitle == offpeakTitleToGo){
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
	private boolean isOffpeakGbSet(){
			
		// Get current unit from text view
		String currentUnit = (String) myOffpeakUnit.getText();
			
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
	 * Set offpeak data to Giga Bytes
	 */
	private void setOffpeakGb(){
		
		// We need to show quota for Gb
		showQuota();
		
		// Set offpeak GB quota
		setQuota();
		
		// Check if offpeak to go is set
		if (isToGoSet()){
			// Set offpeak data as to go
			setGbToGo();
		} 
		// Else So Far must be set
		else {
			// Set offpeak data as used
			setGbSoFar();
		}
		
		// Set acolor based on status
		setNumberColor();
		
	}

	/**
	 * Set offpeak data to percent
	 */
	private void setOffpeakPercent(){
		
		// Don't need the quota for percent so hide it
		hideQuota();
		
		// Check if offpeak to go is set
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
	 * Set current offpeak status
	 */
	private void setStatus(){
		// Check if offpeak is shaped
		if (isCurrentOffpeakShaped()){
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
	 * Set offpeak status as unshaped
	 */
	private void setStatusUnshaped() {
		myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_unshaped));
		myStatus.setTextColor(normalColor);
	}

	/**
	 * Set offpeak status as shaped
	 */
	private void setStatusShaped() {
		myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_shaped));
		myStatus.setTextColor(alertColor);
	}
	
	/**
	 * Set offpeak period times
	 */
	private void setOffpeakPeriod(){
		myOffpeakPeriod.setText(getOffpeakPeriodLand());
	}

	/**
	 * Set title as to go
	 */
	private void setTitleToGo() {
		myOffpeakTitle.setText(offpeakTitleToGo);
	}

	/**
	 * Set title as so far
	 */
	private void setTitleSoFar() {
		myOffpeakTitle.setText(offpeakTitleSoFar);
	}

	/**
	 * Set Giga Bytes to go
	 */
	private void setGbToGo() {
		myOffpeakData.setText(getOffpeakGbToGo());
	}

	/**
	 * Set Giga Bytes so far
	 */
	private void setGbSoFar() {
		myOffpeakData.setText(getOffpeakGbSoFar());
	}

	/**
	 * 
	 */
	private void setQuota() {
		myOffpeakQuota.setText(getOffpeakQuotaStringGBLand());
	}

	/**
	 * Set percent to go
	 */
	private void setPercentToGo() {
		myOffpeakData.setText(getOffpeakPercentToGo());
	}

	/**
	 * Set percent so far
	 */
	private void setPercentSoFar() {
		myOffpeakData.setText(getOffpeakPercentSoFar());
	}

	/**
	 * Set offpeak daily data as Days To Go
	 */
	private void setDailyToGo(){
		myOffpeakDailyData.setText(getOffpeakDailyMbToGo());
	}

	/**
	 * Set offpeak daily data as Days So Far
	 */
	private void setDailySoFar(){
		myOffpeakDailyData.setText(getOffpeakDailyMbSoFar());
	}

	/**
	 * Show quota
	 */
	private void showQuota() {
		myOffpeakQuota.setVisibility(View.VISIBLE);
		myOffpeakUnit.setText(gigabyteUnit);
	}

	/**
	 * Hide quota
	 */
	private void hideQuota() {
		myOffpeakQuota.setVisibility(View.GONE);
		myOffpeakUnit.setText(percentUnit);
	}
	
	/**
	 * Set the color of number based on status of offpeak usage
	 */
	private void setNumberColor() {
		// Set text color based on alert
		if (isCurrentOffpeakShaped()){
			myOffpeakData.setTextColor(usageOverColor);
			myOffpeakQuota.setTextColor(usageOverColor);
			myOffpeakUnit.setTextColor(usageOverColor);
		}
		else if (isOffpeakUsageOver()){
			myOffpeakData.setTextColor(usageAlertColor);
			myOffpeakQuota.setTextColor(usageAlertColor);
			myOffpeakUnit.setTextColor(usageAlertColor);
		}
		else {
			myOffpeakData.setTextColor(textColor);
			myOffpeakQuota.setTextColor(textColor);
			myOffpeakUnit.setTextColor(textColor);
		}
	}
}