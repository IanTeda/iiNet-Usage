package au.id.teda.iinetusage.phone.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import au.id.teda.iinetusage.phone.AppGlobals;
import au.id.teda.iinetusage.phone.R;

public class AccountHelper extends AccountStatusHelper {

	// Static tag strings for logging information and debug
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = AccountHelper.class.getSimpleName();

	// Set data period format
	private final SimpleDateFormat myDataPeriodFormat = new SimpleDateFormat("MMM yyyy");
	private final SimpleDateFormat myRolloverDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
	private final SimpleDateFormat myPeriodDateFormat = new SimpleDateFormat("MMMM yyyy");

	private Context myApplicationContext = AppGlobals.getAppContext();
	
	/**
	 * Method for checking if data period passed is the latest
	 * 
	 * @param inputDataPeriod
	 * @return
	 */
	public boolean checkDataPeriodLatest(String inputDataPeriod) {
		// Log.d(DEBUG_TAG, "myCheckDate: " + inputDataPeriod +
		// "| myCurrentDate: " + getCurrentDataPeriod());

		try {

			Date myCheckDate = myDataPeriodFormat.parse(inputDataPeriod);
			Date myCurrentDate = myDataPeriodFormat.parse(getCurrentDataPeriod());

			// Use java Data class to check if passed date is after or equal to
			// current date stored in preferences
			if (myCheckDate.after(myCurrentDate)
					|| myCheckDate.equals(myCurrentDate)) {
				
				// Looks like it is so return true
				return true;
			} else {
				
				// Doesn't appear to be so return false
				return false;
			}

		} catch (ParseException e) {
			// Opps error in parsing date so pass false
			Log.v(INFO_TAG, "Error parsing date");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getProductPlanString(){
		if (isPlanSet() && isProductSet()){
			String productPlanString = getPlan() + " (" + getProduct() + ")";
			return productPlanString;
		} 
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_plan);
		}
	}
	
	/**
	 * Method for returning Current Anniversary string
	 * @return Anniversary string if set, if not return XML value
	 * @throws ParseException
	 */
	public String getCurrentAnniversaryString(){
		// Check to see if anniversary has been set
		if (isCurrentAnniversarySet()){
			// Looks like it has
			
			// Parse long number into date for formatting
			String myAnniversary = myRolloverDateFormat.format(getCurrentAnniversary());
			
			// Return formatted string
			return myAnniversary;
		} 
		// Else it must not be set so return default XML string value
		else {
			// So return string value in XML
			return myApplicationContext.getString(R.string.account_info_rollover_period_data);
		}
		
	}
	
	/**
	 * Method for returning current data period as a string
	 * @return a string value of the current data perios
	 * @throws ParseException
	 */
	public String getCurrentDataPeriodString() throws ParseException{
		if (isCurrentDataPeriodSet()){
			
			// Get current period date string and parse it into a date object
			Date myCurrentPeriodDate = myDataPeriodFormat.parse(getCurrentDataPeriod());
			
			// Format Current Period Date into period format date string
			String myCurrentPeriod = myPeriodDateFormat.format(myCurrentPeriodDate);
			
			// Return formatted string
			return myCurrentPeriod;
		} 
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_plan);
		}
		
	}
	
	/**
	 * Method for returning string value of days to go
	 * @return string value of days to go
	 */
	public String getCurrentDaysToGoString() {
		// Check to see if current days to go is set
		if (isCurrentDaysToGoSet()){
			// Looks like it is so return string value of days to go
			
			// Get long value parse and add days to end
			String myDays = String.valueOf(getCurrentDaysToGo()) + " Days";
			
			// Return formatted string
			return myDays;
		} 
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_days_data);
		}
		
	}
	
	/**
	 * Method for returning string value of days so far
	 * @return string value of days so far
	 */
	public String getCurrentDaysSoFarString() {
		// Check to see if current days to go is set
		if (isCurrentDaysSoFarSet()){
			// Looks like it is so return string value of days to go
			
			// Get long value parse and add days to end
			String myDays = String.valueOf(getCurrentDaysSoFar()) + " Days";
			
			// Return formatted string
			return myDays;
		} 
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_days_data);
		}
		
	}
	
	public String getUpTimeString(){
		if (isCurrentUpTimeSet()){
			Date myUpTimeDate = null;
			myUpTimeDate.setTime(getCurrentUpTime());
		}
		else {
			return myApplicationContext.getString(R.string.account_info_days_data);
		}
	}


}
