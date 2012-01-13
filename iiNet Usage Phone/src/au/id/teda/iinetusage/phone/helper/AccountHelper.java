package au.id.teda.iinetusage.phone.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class AccountHelper extends AccountStatusHelper {

	// Static tag strings for logging information and debug
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for
															// LogCat
	private static final String INFO_TAG = AccountHelper.class.getSimpleName();

	// Set data period format
	private final SimpleDateFormat myDataPeriodFormat = new SimpleDateFormat(
			"MMM yyyy");

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

}
