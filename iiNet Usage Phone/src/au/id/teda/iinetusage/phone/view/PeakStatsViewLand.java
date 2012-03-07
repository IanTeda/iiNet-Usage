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
	private final String peakToGoTitle;
	private final String peakSoFarTitle;
	
	
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
		peakToGoTitle = myActivity.getString(R.string.peak_land_toGo);
		peakSoFarTitle = myActivity.getString(R.string.peak_land_soFar);;
		
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
		setPeakDailyToGo();
		
	}
	
	/**
	 * Switch between Days So Far and Days To Go
	 */
	public void switchPeakData(){
		// Check if to go is set
		if (isToGoSet()){
			// Switch to so far
			myPeakTitle.setText(peakSoFarTitle);
			setPeakDailySoFar();
			
		}
		// Else so far must be set
		else {
			// Switch to to go
			myPeakTitle.setText(peakToGoTitle);
			setPeakDailyToGo();
			
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
		if (currentTitle == peakToGoTitle){
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
		// Must be Gigabyte unit
		else {
			// So return false
			return false;
		}
	}
	
	/**
	 * Set peak data to Giga Bytes
	 */
	private void setPeakGb(){
		
		myPeakQuota.setVisibility(View.VISIBLE);
		myPeakUnit.setText(gigabyteUnit);
		
		// Set peak GB quota
		myPeakQuota.setText(getPeakQuotaStringGBLand());
		
		// Check if peak to go is set
		if (isToGoSet()){
			// Set peak data as to go
			myPeakData.setText(getPeakGbToGo());
		} 
		// Else So Far must be set
		else {
			// Set peak data as used
			myPeakData.setText(getPeakGbSoFar());
		}
		
	}
	
	/**
	 * Set peak data to percent
	 */
	private void setPeakPercent(){
		myPeakQuota.setVisibility(View.GONE);
		myPeakUnit.setText(percentUnit);
		
		// Check if peak to go is set
		if (isToGoSet()){
			// Set peak data as to go
			myPeakData.setText(getPeakPercentToGo());
		} 
		// Else So Far must be set
		else {
			// Set peak data as used
			myPeakData.setText(getPeakPercentSoFar());
		}
	}
	
	/**
	 * Set peak daily data as Days To Go
	 */
	private void setPeakDailyToGo(){
		myPeakDailyData.setText(getPeakDailyMbToGo());
	}
	
	/**
	 * Set peak daily data as Days So Far
	 */
	private void setPeakDailySoFar(){
		myPeakDailyData.setText(getPeakDailyMbSoFar());
	}
	
	/**
	 * Set current peak status
	 */
	private void setStatus(){
		// Check if peak is shaped
		if (isCurrentPeakShaped()){
			// It is so set shaped
			myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_shaped));
			myStatus.setTextColor(alertColor);
		}
		// Else we are unshaped
		else {
			// So set status unshaped
			myStatus.setText(myActivity.getString(R.string.peak_offpeak_status_unshaped));
			myStatus.setTextColor(normalColor);
		}
	}
	
	/**
	 * Set peak period times
	 */
	private void setPeakPeriod(){
		myPeakPeriod.setText(getPeakPeriodLand());
	}

}
