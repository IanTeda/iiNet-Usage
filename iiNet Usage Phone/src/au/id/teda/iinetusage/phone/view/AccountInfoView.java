package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class AccountInfoView extends AccountHelper {

	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = AccountInfoView.class.getSimpleName();

	private final Context myActivityContext;
	private final Activity myActivity;

	// TextView objects for IP & Up-time block
	private final TextView myIpTitle;
	private final TextView myIpUpSlashTitle;
	private final TextView myUpTitle;
	private final TextView myIpUpdata;

	// TextView objects for quota block
	private final TextView myPeakTitle;
	private final TextView myPeakOffpeakTitle;
	private final TextView myOffPeakTitle;
	private final TextView myPeakOffpeakData;
	
	// TextView objects for rollover period block
	private final TextView myRolloverTitle;
	private final TextView myRolloverPeriodTitle;
	private final TextView myPeriodTitle;
	private final TextView myRolloverPeriodData;
	
	// TextView objects for rollover period block
	private final TextView myDaysSoFareTitle;
	private final TextView myDaysTitle;
	private final TextView myDaysToGoTitle;
	private final TextView myDaysData;
	
	// Color values for focus and alternate
	private final int focusColor;
	private final int alternateColor;

	public AccountInfoView(Context context) {
		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;

		// IP & Up Time TextView objects
		myIpTitle = (TextView) myActivity.findViewById(R.id.account_info_ip);
		myIpUpSlashTitle = (TextView) myActivity.findViewById(R.id.account_info_ip_up_slash);
		myUpTitle = (TextView) myActivity.findViewById(R.id.account_info_up);
		myIpUpdata = (TextView) myActivity.findViewById(R.id.account_info_ip_up_data);

		// Quota TextView objects
		myPeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_peak);
		myPeakOffpeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_slash);
		myOffPeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_offpeak);
		myPeakOffpeakData = (TextView) myActivity.findViewById(R.id.account_info_quota_data);
		
		// Rollover & Period TextView objects
		myRolloverTitle = (TextView) myActivity.findViewById(R.id.account_info_rollover);
		myRolloverPeriodTitle = (TextView) myActivity.findViewById(R.id.account_info_rollover_period_slash);
		myPeriodTitle = (TextView) myActivity.findViewById(R.id.account_info_period);
		myRolloverPeriodData = (TextView) myActivity.findViewById(R.id.account_info_rollover_period_data);
		
		// Days TextView objects
		myDaysSoFareTitle = (TextView) myActivity.findViewById(R.id.account_info_days_soFare);
		myDaysTitle = (TextView) myActivity.findViewById(R.id.account_info_days_soFare_toGo_slash);
		myDaysToGoTitle = (TextView) myActivity.findViewById(R.id.account_info_days_toGo);
		myDaysData = (TextView) myActivity.findViewById(R.id.account_info_days_data);
		
		// Set focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
	}

	public void switchIpUpBlock(){
		if (isIpTitleFocus()){
			myIpTitle.setTextColor(alternateColor);
			myIpTitle.setTypeface(null, 0);
			myUpTitle.setTextColor(focusColor);
			myUpTitle.setTypeface(null, 1);
		}
		else{
			myIpTitle.setTextColor(focusColor);
			myIpTitle.setTypeface(null, 1);
			myUpTitle.setTextColor(alternateColor);
			myUpTitle.setTypeface(null, 0);
		}
	}
	
	/**
	 * Check if the ip title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isIpTitleFocus(){
		// Get current color value of TextView
		int ipTitleColor = myIpTitle.getCurrentTextColor();
		
		// Check current colour against XML defined focus color
		if (ipTitleColor == focusColor){
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
	 * Determine what state the account info view is in and change it between restore and minimise
	 */
	public void resizeAccountInfo(){
		// Check if block is already hidden
		if (isIpUpBlockHidden()
				&& isQuotaBlockHidden()){
			
			// Look like it is so lets make it visible
			maximiseAccountInfo();
		}
		// Else it must be visible
		else {
			
			// So lets hide it
			restoreAccountInfo();
		}

	}
	
	/**
	 * Method for minimise (hiding) all blocks
	 */
	public void minimiseAccountInfo() {
		hideDaysBlock();
		hideIpUpBlock();
		hideQuotaBlock();
		hideRolloverPeriodBlock();
	}
	
	/**
	 * Method for maximising (showing) all blocks
	 */
	public void maximiseAccountInfo(){
		showDaysBlock();
		showIpUpBlock();
		showQuotaBlock();
		showRolloverPeriodBlock();
	}
	
	/**
	 * Method to restore (top only) blocks 
	 */
	public void restoreAccountInfo(){
		showRolloverPeriodBlock();
		showDaysBlock();
		hideQuotaBlock();
		hideIpUpBlock();
		
	}
	
	/**
	 * Method for hiding IP Up-time block
	 */
	public void hideIpUpBlock() {
		myIpTitle.setVisibility(View.GONE);
		myIpUpSlashTitle.setVisibility(View.GONE);
		myUpTitle.setVisibility(View.GONE);
		myIpUpdata.setVisibility(View.GONE);
	}

	/**
	 * Method for showing IP Up-time block
	 */
	public void showIpUpBlock() {
		myIpTitle.setVisibility(View.VISIBLE);
		myIpUpSlashTitle.setVisibility(View.VISIBLE);
		myUpTitle.setVisibility(View.VISIBLE);
		myIpUpdata.setVisibility(View.VISIBLE);
	}

	/**
	 * Method for checking if IP Up-time block is hidden
	 * 
	 * @return true if block hidden
	 */
	public boolean isIpUpBlockHidden() {
		// Check if views are set to gone
		if (myIpTitle.getVisibility() == View.GONE
				&& myIpUpSlashTitle.getVisibility() == View.GONE
				&& myUpTitle.getVisibility() == View.GONE
				&& myIpUpdata.getVisibility() == View.GONE) {
			// They are so lets return true
			return true;
		}
		// Else it must be visible
		else {
			// So lets return false
			return false;
		}
	}

	/**
	 * Method for hiding Quota block
	 */
	public void hideQuotaBlock() {
		myPeakTitle.setVisibility(View.GONE);
		myPeakOffpeakTitle.setVisibility(View.GONE);
		myOffPeakTitle.setVisibility(View.GONE);
		myPeakOffpeakData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Quota block
	 */
	public void showQuotaBlock() {
		myPeakTitle.setVisibility(View.VISIBLE);
		myPeakOffpeakTitle.setVisibility(View.VISIBLE);
		myOffPeakTitle.setVisibility(View.VISIBLE);
		myPeakOffpeakData.setVisibility(View.VISIBLE);
	}

	/**
	 * Method for checking if Quota block is hidden
	 * 
	 * @return true if block hidden
	 */
	public boolean isQuotaBlockHidden() {
		// Check if views are set to gone
		if (myPeakTitle.getVisibility() == View.GONE
				&& myPeakOffpeakTitle.getVisibility() == View.GONE
				&& myOffPeakTitle.getVisibility() == View.GONE
				&& myPeakOffpeakData.getVisibility() == View.GONE) {
			// They are so lets return true
			return true;
		}
		// Else it must be visible
		else {
			// So lets return false
			return false;
		}
	}
	
	/**
	 * Method for hiding Quota block
	 */
	public void hideRolloverPeriodBlock() {
		myRolloverTitle.setVisibility(View.GONE);
		myRolloverPeriodTitle.setVisibility(View.GONE);
		myPeriodTitle.setVisibility(View.GONE);
		myRolloverPeriodData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Quota block
	 */
	public void showRolloverPeriodBlock() {
		myRolloverTitle.setVisibility(View.VISIBLE);
		myRolloverPeriodTitle.setVisibility(View.VISIBLE);
		myPeriodTitle.setVisibility(View.VISIBLE);
		myRolloverPeriodData.setVisibility(View.VISIBLE);
	}

	/**
	 * Method for checking if Quota block is hidden
	 * 
	 * @return true if block hidden
	 */
	public boolean isRolloverPeriodBlockHidden() {
		// Check if views are set to gone
		if (myRolloverTitle.getVisibility() == View.GONE
				&& myRolloverPeriodTitle.getVisibility() == View.GONE
				&& myPeriodTitle.getVisibility() == View.GONE
				&& myRolloverPeriodData.getVisibility() == View.GONE) {
			// They are so lets return true
			return true;
		}
		// Else it must be visible
		else {
			// So lets return false
			return false;
		}
	}
	
	/**
	 * Method for hiding Days block
	 */
	public void hideDaysBlock() {
		myDaysSoFareTitle.setVisibility(View.GONE);
		myDaysTitle.setVisibility(View.GONE);
		myDaysToGoTitle.setVisibility(View.GONE);
		myDaysData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Days block
	 */
	public void showDaysBlock() {
		myDaysSoFareTitle.setVisibility(View.VISIBLE);
		myDaysTitle.setVisibility(View.VISIBLE);
		myDaysToGoTitle.setVisibility(View.VISIBLE);
		myDaysData.setVisibility(View.VISIBLE);
	}

	/**
	 * Method for checking if Days block is hidden
	 * 
	 * @return true if block hidden
	 */
	public boolean isDaysBlockHidden() {
		// Check if views are set to gone
		if (myDaysSoFareTitle.getVisibility() == View.GONE
				&& myDaysTitle.getVisibility() == View.GONE
				&& myDaysToGoTitle.getVisibility() == View.GONE
				&& myDaysData.getVisibility() == View.GONE) {
			// They are so lets return true
			return true;
		}
		// Else it must be visible
		else {
			// So lets return false
			return false;
		}
	}

}
