package au.id.teda.iinetusage.phone.view;

import java.text.ParseException;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class AccountInfoView extends AccountHelper {

	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = AccountInfoView.class.getSimpleName();

	private final Context myActivityContext;
	private final Activity myActivity;

	// Button for plan and product title
	private final Button myProductPlanButton;

	// TextView objects for IP & Up-time block
	private final TextView myIpTitle;
	private final TextView myIpUpSlashTitle;
	private final TextView myUpTitle;
	private final TextView myIpUpdata;

	// TextView objects for quota block
	private final TextView myPeakTitle;
	private final TextView myPeakOffpeakTitle;
	private final TextView myOffPeakTitle;
	private final TextView myQuotaData;

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

	/**
	 * Class constructor
	 * 
	 * @param context
	 */
	public AccountInfoView(Context context) {
		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;

		// Set reference for Product Plan button
		myProductPlanButton = (Button) myActivity.findViewById(R.id.account_info_expand);

		// Set IP & Up Time TextView objects
		myIpTitle = (TextView) myActivity.findViewById(R.id.account_info_ip);
		myIpUpSlashTitle = (TextView) myActivity.findViewById(R.id.account_info_ip_up_slash);
		myUpTitle = (TextView) myActivity.findViewById(R.id.account_info_up);
		myIpUpdata = (TextView) myActivity.findViewById(R.id.account_info_ip_up_data);

		// Set Quota TextView objects
		myPeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_peak);
		myPeakOffpeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_slash);
		myOffPeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_offpeak);
		myQuotaData = (TextView) myActivity.findViewById(R.id.account_info_quota_data);

		// Set Rollover & Period TextView objects
		myRolloverTitle = (TextView) myActivity.findViewById(R.id.account_info_rollover);
		myRolloverPeriodTitle = (TextView) myActivity.findViewById(R.id.account_info_rollover_period_slash);
		myPeriodTitle = (TextView) myActivity.findViewById(R.id.account_info_period);
		myRolloverPeriodData = (TextView) myActivity.findViewById(R.id.account_info_rollover_period_data);

		// Set Days TextView objects
		myDaysSoFareTitle = (TextView) myActivity.findViewById(R.id.account_info_days_soFare);
		myDaysTitle = (TextView) myActivity.findViewById(R.id.account_info_days_soFare_toGo_slash);
		myDaysToGoTitle = (TextView) myActivity.findViewById(R.id.account_info_days_toGo);
		myDaysData = (TextView) myActivity.findViewById(R.id.account_info_days_data);

		// Set focus and alternate colours
		focusColor = myActivity.getResources().getColor(R.color.application_h2_text_color);
		alternateColor = myActivity.getResources().getColor(R.color.application_h2_text_alt_color);
	}

	/**
	 * Load account info view
	 */
	public void loadView() {
		// Set Account plan and product
		myProductPlanButton.setText(getProductPlanString());

		// Set Rollover TextView
		myRolloverPeriodData.setText(getCurrentAnniversaryString());

		// Set Days TextView
		myDaysData.setText(getCurrentDaysToGoString());
		
		// Set Ip TextView
		myIpUpdata.setText(getCurrentIpAddressString());
		
		// Set peak quota TextView
		myQuotaData.setText(getPeakQuotaString());
		

	}

	/**
	 * Switch the focus of Days Period block
	 */
	public void switchFocusDaysBlock() {
		// Check to see if Days So Far has focus
		if (isDaysSoFareFocus()) {

			// Lets change text color (focus)
			myDaysSoFareTitle.setTextColor(alternateColor);
			myDaysToGoTitle.setTextColor(focusColor);

			// Switch text view to days to go
			myDaysData.setText(getCurrentDaysToGoString());
		}
		// Else Days To Go has focus
		else {
			// Lets change text color (focus)
			myDaysSoFareTitle.setTextColor(focusColor);
			myDaysToGoTitle.setTextColor(alternateColor);

			// Switch TextView to days so far
			myDaysData.setText(getCurrentDaysSoFarString());
		}
	}

	/**
	 * Check if the Days title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isDaysSoFareFocus() {
		// Get current color value of TextView
		int currentColor = myDaysSoFareTitle.getCurrentTextColor();

		// Check current colour against XML defined focus color
		if (currentColor == focusColor) {
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
	 * Switch the focus of Rollover Period block
	 * 
	 * @throws ParseException
	 */
	public void switchFocusRolloverPeriodBlock() throws ParseException {
		// Check to see if Rollover has focus
		if (isRolloverTitleFocus()) {
			// Switch text color of titles
			myRolloverTitle.setTextColor(alternateColor);
			myPeriodTitle.setTextColor(focusColor);

			// Set TextView to Current Anniversary
			myRolloverPeriodData.setText(getCurrentDataPeriodString());

		}
		// Else Period must have focus
		else {
			// Switch text color of titles
			myRolloverTitle.setTextColor(focusColor);
			myPeriodTitle.setTextColor(alternateColor);

			// Set TextView to Current Anniversary
			myRolloverPeriodData.setText(getCurrentAnniversaryString());

		}
	}

	/**
	 * Check if the Rollover title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isRolloverTitleFocus() {
		// Get current color value of TextView
		int currentColor = myRolloverTitle.getCurrentTextColor();

		// Check current colour against XML defined focus color
		if (currentColor == focusColor) {
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
	 * Switch the focus of Quota block
	 */
	public void switchFocusQuotaBlock() {
		// Check to see if peak quota has focus
		if (isPeakTitleFocus()) {
			// Looks like it does so switch to off peak
			myPeakTitle.setTextColor(alternateColor);
			myOffPeakTitle.setTextColor(focusColor);
			
			// Change TextView char sequence
			myQuotaData.setText(getOffpeakQuotaString());
		} 
		// Else Offpeak has focus
		else {
			// So change focus to peak
			myPeakTitle.setTextColor(focusColor);
			myOffPeakTitle.setTextColor(alternateColor);
			
			// Change TextView char sequence
			myQuotaData.setText(getPeakQuotaString());
		}
	}

	/**
	 * Check if the Peak title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isPeakTitleFocus() {
		// Get current color value of TextView
		int currentColor = myPeakTitle.getCurrentTextColor();

		// Check current colour against XML defined focus color
		if (currentColor == focusColor) {
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
	 * Switch the focus of Ip Up-Time block
	 */
	public void switchFocusIpUpBlock() {
		// Check to see if ip has focus
		if (isIpTitleFocus()) {

			// Change color focus of titles
			myIpTitle.setTextColor(alternateColor);
			myUpTitle.setTextColor(focusColor);

			// Set TextView to Up Time
			myIpUpdata.setText(getCurrentUpTimeString());
		} else {
			myIpTitle.setTextColor(focusColor);
			myUpTitle.setTextColor(alternateColor);

			// Set TextView to Up Time
			myIpUpdata.setText(getCurrentIpAddressString());
		}
	}

	/**
	 * Check if the ip title is in focus
	 * 
	 * @return true if color matches XML value
	 */
	public boolean isIpTitleFocus() {
		// Get current color value of TextView
		int currentColor = myIpTitle.getCurrentTextColor();

		// Check current colour against XML defined focus color
		if (currentColor == focusColor) {
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
	 * Determine what state the account info view is in and change it between
	 * restore and minimise
	 */
	public void resizeAccountInfo() {
		// Check if block is already hidden
		if (isIpUpBlockHidden() && isQuotaBlockHidden()) {

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
	public void maximiseAccountInfo() {
		showDaysBlock();
		showIpUpBlock();
		showQuotaBlock();
		showRolloverPeriodBlock();
	}

	/**
	 * Method to restore (top only) blocks
	 */
	public void restoreAccountInfo() {
		showRolloverPeriodBlock();
		showDaysBlock();
		hideQuotaBlock();
		hideIpUpBlock();

	}

	/**
	 * Method for hiding IP Up-time block
	 */
	public void hideIpUpBlock() {
		// Set TextView visibility gone
		myIpTitle.setVisibility(View.GONE);
		myIpUpSlashTitle.setVisibility(View.GONE);
		myUpTitle.setVisibility(View.GONE);
		myIpUpdata.setVisibility(View.GONE);
	}

	/**
	 * Method for showing IP Up-time block
	 */
	public void showIpUpBlock() {
		// Set TextView visibility visible
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
		// Set TextView visibility gone
		myPeakTitle.setVisibility(View.GONE);
		myPeakOffpeakTitle.setVisibility(View.GONE);
		myOffPeakTitle.setVisibility(View.GONE);
		myQuotaData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Quota block
	 */
	public void showQuotaBlock() {
		// Set TextView visibility visible
		myPeakTitle.setVisibility(View.VISIBLE);
		myPeakOffpeakTitle.setVisibility(View.VISIBLE);
		myOffPeakTitle.setVisibility(View.VISIBLE);
		myQuotaData.setVisibility(View.VISIBLE);
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
				&& myQuotaData.getVisibility() == View.GONE) {
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
		// Set TextView visibility gone
		myRolloverTitle.setVisibility(View.GONE);
		myRolloverPeriodTitle.setVisibility(View.GONE);
		myPeriodTitle.setVisibility(View.GONE);
		myRolloverPeriodData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Quota block
	 */
	public void showRolloverPeriodBlock() {
		// Set TextView visibility visible
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
		// Set TextView visibility gone
		myDaysSoFareTitle.setVisibility(View.GONE);
		myDaysTitle.setVisibility(View.GONE);
		myDaysToGoTitle.setVisibility(View.GONE);
		myDaysData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Days block
	 */
	public void showDaysBlock() {
		// Set TextView visibility visible
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
