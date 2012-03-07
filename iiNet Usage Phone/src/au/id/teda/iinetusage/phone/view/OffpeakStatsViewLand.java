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
	private final TextView myOffpeakData;
	private final TextView myOffpeakQuota;
	private final TextView myOffpeakUnit;
	
	// TextView objects for peak number
	private final TextView myOffpeakDailyData;
	
	// TextView object for peak status
	private final TextView myStatus;
	
	// TextView object for peak period
	private final TextView myOffpeakPeriod;
	
	// TextView object for peak title
	private final TextView myOffpeakTitle;
	
	// String object for peak title
	private final String offpeakToGoTitle;
	private final String offpeakSoFarTitle;
	
	
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
	
		// Initialise Peak Data TextView objects
		myOffpeakData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_data);
		myOffpeakQuota = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_quota);
		myOffpeakUnit = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_unit);
		
		// Initialise Daily Average TextView objects
		myOffpeakDailyData = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_suggested_remaining_data);
		
		// Initialise status TextView objects
		myStatus = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_status_data);
		
		// Initialise peak period TextView objects
		myOffpeakPeriod = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_period_data);
		
		// Initialise peak title TextView objects
		myOffpeakTitle = (TextView) myActivity.findViewById(R.id.dashboard_landscape_offpeak_title);
		offpeakToGoTitle = myActivity.getString(R.string.offpeak_land_toGo);
		offpeakSoFarTitle = myActivity.getString(R.string.offpeak_land_soFar);;
		
		// Set type face custom font to number
		myOffpeakData.setTypeface(myFontNumber);
		myOffpeakQuota.setTypeface(myFontNumber);
		myOffpeakUnit.setTypeface(myFontNumber);
	}
	
	/**
	 * Load PeakStatsViewLand view
	 */
	public void loadView(){
		
		// Set peak GB used
		setOffpeakGb();
		
		// Set current peak status
		setStatus();
		
		// Set peak period
		setOffpeakPeriod();
		
		// Set peak view
		setOffpeakDailyToGo();
		
	}
	
	/**
	 * Switch between Days So Far and Days To Go
	 */
	public void switchOffpeakData(){
		// Check if to go is set
		if (isToGoSet()){
			// Switch to so far
			myOffpeakTitle.setText(offpeakSoFarTitle);
			setOffpeakDailySoFar();
			
		}
		// Else so far must be set
		else {
			// Switch to to go
			myOffpeakTitle.setText(offpeakToGoTitle);
			setOffpeakDailyToGo();
			
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
		// Get current peak title from text view
		String currentTitle = (String) myOffpeakTitle.getText();
			
		// Check if current unit string matches Giga byte unit string stored in xml
		if (currentTitle == offpeakToGoTitle){
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
		// Must be Gigabyte unit
		else {
			// So return false
			return false;
		}
	}
	
	/**
	 * Set peak data to Giga Bytes
	 */
	private void setOffpeakGb(){
		
		myOffpeakQuota.setVisibility(View.VISIBLE);
		myOffpeakUnit.setText(gigabyteUnit);
		
		// Set peak GB quota
		myOffpeakQuota.setText(getOffpeakQuotaStringGBLand());
		
		// Check if peak to go is set
		if (isToGoSet()){
			// Set peak data as to go
			myOffpeakData.setText(getOffpeakGbToGo());
		} 
		// Else So Far must be set
		else {
			// Set peak data as used
			myOffpeakData.setText(getOffpeakGbSoFar());
		}
		
	}
	
	/**
	 * Set peak data to percent
	 */
	private void setOffpeakPercent(){
		myOffpeakQuota.setVisibility(View.GONE);
		myOffpeakUnit.setText(percentUnit);
		
		// Check if peak to go is set
		if (isToGoSet()){
			// Set peak data as to go
			myOffpeakData.setText(getOffpeakPercentToGo());
		} 
		// Else So Far must be set
		else {
			// Set peak data as used
			myOffpeakData.setText(getOffpeakPercentSoFar());
		}
	}
	
	/**
	 * Set peak daily data as Days To Go
	 */
	private void setOffpeakDailyToGo(){
		myOffpeakDailyData.setText(getOffpeakDailyMbToGo());
	}
	
	/**
	 * Set peak daily data as Days So Far
	 */
	private void setOffpeakDailySoFar(){
		myOffpeakDailyData.setText(getOffpeakDailyMbSoFar());
	}
	
	/**
	 * Set current peak status
	 */
	private void setStatus(){
		// Check if peak is shaped
		if (isCurrentOffpeakShaped()){
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
	private void setOffpeakPeriod(){
		myOffpeakPeriod.setText(getOffpeakPeriodLand());
	}

}
