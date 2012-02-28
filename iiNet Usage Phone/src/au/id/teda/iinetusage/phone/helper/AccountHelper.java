package au.id.teda.iinetusage.phone.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import au.id.teda.iinetusage.phone.AppGlobals;
import au.id.teda.iinetusage.phone.R;

public class AccountHelper extends AccountStatusHelper {

	// Static tag strings for logging information and debug
	private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = AccountHelper.class.getSimpleName();

	// Set data period format
	private final SimpleDateFormat myDataPeriodFormat = new SimpleDateFormat("MMM yyyy");
	private final SimpleDateFormat myRolloverDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
	private final SimpleDateFormat myPeriodDateFormat = new SimpleDateFormat("MMMM yyyy");
	
	NumberFormat myNumberFormat = new DecimalFormat("#,#00");

	private Context myApplicationContext = AppGlobals.getAppContext();
	
	/**
	 * Method for checking if data period passed is the latest
	 * 
	 * @param inputDataPeriod
	 * @return true if period date is greater or equal to current
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
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Method for getting and formatting plan and product string
	 * @return String value of broadband product and plan
	 */
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
	
	/**
	 * Method for returning Up Time String
	 * @return string value of up time
	 */
	public String getCurrentUpTimeString(){
		// Check to see if current up time is set
		if (isCurrentUpTimeSet()){
			
			// Retrive and load date of current up time
			Date myUpTimeDate = new Date(getCurrentUpTime());
			
			// Format date value into string
			String myUpTimeString = myRolloverDateFormat.format(myUpTimeDate);

			// Return formated date string
			return myUpTimeString; // TODO: Change to xx Days 2 Hrs
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_days_data);
		}
	}
	
	/**
	 * Method for returning current IP address string
	 * @return string value of ip address
	 */
	public String getCurrentIpAddressString(){
		// Check to see if current IP address is set
		if (isCurrentIpSet()){
			
			// Return stored IP address in shared preferences
			return getCurrentIp();
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_ip_data);
		}
	}
	
	/**
	 * Method for getting peak quota String
	 * @return String value of peak quota formatted
	 */
	public String getPeakQuotaStringGB(){
		// Check if peak quota is set
		if (isPeakQuotaSet()){
			
			// Get Long value of peak quota and convert to Gb
			Long peakQuotaLong = getPeakQuota()/1000;
			
			// Format Long value into String
			String quotaString = String.valueOf(peakQuotaLong) + " (Gb)";
			
			// Return formated string value
			return quotaString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_quota_data);
		}
	}
	
	/**
	 * Get the peak daily quota in Mb
	 * @return int value of peak daily quota in Mb
	 */
	public double getPeakDailyQuotaMb(){
		// Check if peak quota is set
		if (isPeakQuotaSet()){
			
			// Get Long value of peak quota and convert to Mb
			Long quotaLong = getPeakQuota();
			
			// Get number of days in period
			Long durationDays = getCurrentDaysToGo() + getCurrentDaysSoFar();
			
			double daily = quotaLong/durationDays;
			
			// Return daily av. suggested use
			return daily;
		}
		// Else it must not be set so return default XML string value
		else {
			return 10;
		}
	}
	
	/**
	 * Get the offpeak daily quota in Mb
	 * @return int value of offpeak daily quota in Mb
	 */
	public double getOffpeakDailyQuotaMb(){
		// Check if peak quota is set
		if (isPeakQuotaSet()){
			
			// Get Long value of peak quota and convert to Mb
			Long quotaLong = getOffpeakQuota();
			
			// Get number of days in period
			Long durationDays = getCurrentDaysToGo() + getCurrentDaysSoFar();
			
			double daily = quotaLong/durationDays;
			
			// Return daily av. suggested use
			return daily;
		}
		// Else it must not be set so return default XML string value
		else {
			return 10;
		}
	}
	
	/**
	 * Method for getting peak quota String
	 * @return String value of peak quota formatted
	 */
	public String getPeakQuotaStringGBLand(){
		// Check if peak quota is set
		if (isPeakQuotaSet()){
			
			// Get Long value of peak quota and convert to Gb
			Long peakQuotaLong = getPeakQuota()/1000;
			
			// Format Long value into String
			String quotaString = "/ " + String.valueOf(peakQuotaLong);
			
			// Return formated string value
			return quotaString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_quota_data);
		}
	}
	
	/**
	 * Method for getting off peak quota String
	 * @return String value of off peak quota formatted
	 */
	public String getOffpeakQuotaString(){
		// Check if off peak quota is set
		if (isOffpeakQuotaSet()){
			
			// Get Long value of off peak quota and convert to Gb
			Long offPeakQuotaLong = getOffpeakQuota()/1000;
			
			// Format Long value into String
			String quotaString = String.valueOf(offPeakQuotaLong) + " (Gb)";
			
			// Return formated string value
			return quotaString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_quota_data);
		}
	}
	
	/**
	 * Method for getting peak quota String
	 * @return String value of peak quota formatted
	 */
	public String getOffpeakQuotaStringGBLand(){
		// Check if peak quota is set
		if (isOffpeakQuotaSet()){
			
			// Get Long value of peak quota and convert to Gb
			Long offpeakQuotaLong = getOffpeakQuota()/1000;
			
			// Format Long value into String
			String quotaString = "/ " + String.valueOf(offpeakQuotaLong);
			
			// Return formated string value
			return quotaString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.account_info_quota_data);
		}
	}
	
	/**
	 * Method for getting peak data used as a percent value
	 * @return string value of percentage of peak data used
	 */
	public String getPeakDataUsedPercent(){
		// Check to see if peak data and quota is set
		if (isCurrentPeakUsedSet() && isPeakQuotaSet()){
			
			// Get peak data used
			double dataUsed = getCurrentPeakUsed()/1000000;
			
			// Get peak quota
			double quota = getPeakQuota();
			
			// Determine percentage value
			double percent = dataUsed/quota*100;
			
			// Format percentage value
			NumberFormat percentNumberFormat = new DecimalFormat("#,#00");
			String percentString = percentNumberFormat.format(percent);
			
			return percentString;
			
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting offpeak data used as a percent value
	 * @return string value of percentage of offpeak data used
	 */
	public String getOffpeakDataUsedPercent(){
		// Check to see if peak data and quota is set
		if (isCurrentOffpeakUsedSet() && isOffpeakQuotaSet()){
			
			// Get peak data used
			double dataUsed = getCurrentOffpeakUsed()/1000000;
			
			// Get peak quota
			double quota = getOffpeakQuota();
			
			// Determine percentage value
			double percent = dataUsed/quota*100;
			
			// Format percentage value
			String percentString = myNumberFormat.format(percent);
			
			return percentString;
			
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting peak data remaining as a percent value
	 * @return string value of percentage of peak data remaining
	 */
	public String getPeakDataRemainingPercent(){
		// Check to see if peak data and quota is set
		if (isCurrentPeakUsedSet() && isPeakQuotaSet()){
			
			// Get peak quota
			double quota = getPeakQuota();

			// Get peak data used
			double dataUsed = quota - (getCurrentPeakUsed()/1000000);
			
			// Determine percentage value
			double percent = dataUsed/quota*100;
			
			// Format percentage value
			String percentString = myNumberFormat.format(percent);
			
			return percentString;
			
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting off peak data remaining as a percent value
	 * @return string value of percentage of off peak data remaining
	 */
	public String getOffpeakDataRemainingPercent(){
		// Check to see if peak data and quota is set
		if (isCurrentOffpeakUsedSet() && isOffpeakQuotaSet()){
			
			// Get peak quota
			double quota = getOffpeakQuota();

			// Get peak data used
			double dataUsed = quota - (getCurrentOffpeakUsed()/1000000);
			
			// Determine percentage value
			double percent = dataUsed/quota*100;
			
			// Format percentage value
			String percentString = myNumberFormat.format(percent);
			
			return percentString;
			
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting peak data used in Gigabytes
	 * @return string value of peak data used to date in Gb
	 */
	public String getPeakDataRemainingGb(){
		if (isCurrentPeakUsedSet()){
			
			// Get peak data used in Gb
			double dataUsed = getCurrentPeakUsed()/1000000/1000;
			
			// Get peak quota
			double quota = getPeakQuota()/1000;
			
			// Calculate remaining data
			double dataRemining = quota - dataUsed;
			
			// Format Giga Byte value
			String gigaByteString = myNumberFormat.format(dataRemining);
			
			return gigaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting off peak data used in Gigabytes
	 * @return string value of off peak data used to date in Gb
	 */
	public String getOffpeakDataRemainingGb(){
		if (isCurrentOffpeakUsedSet()){
			
			// Get peak data used in Gb
			double dataUsed = getCurrentOffpeakUsed()/1000000/1000;
			
			// Get peak quota
			double quota = getOffpeakQuota()/1000;
			
			// Calculate remaining data
			double dataRemining = quota - dataUsed;
			
			// Format Giga Byte value
			String gigaByteString = myNumberFormat.format(dataRemining);
			
			return gigaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting peak data used in Megabytes
	 * @return string value of peak data used to date in Mb
	 */
	public String getPeakDataRemainingMb(){
		if (isCurrentPeakUsedSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentPeakUsed()/1000000;
			
			// Get peak quota
			double quota = getPeakQuota();
			
			// Calculate remaining data
			double dataRemining = quota - dataUsed;
			
			// Format Giga Byte value
			String gigaByteString = myNumberFormat.format(dataRemining) + " (Mb)";
			
			return gigaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting off peak data used in Megabytes
	 * @return string value of off peak data used to date in Mb
	 */
	public String getOffpeakDataRemainingMb(){
		if (isCurrentOffpeakUsedSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentOffpeakUsed()/1000000;
			
			// Get peak quota
			double quota = getOffpeakQuota();
			
			// Calculate remaining data
			double dataRemining = quota - dataUsed;
			
			// Format Giga Byte value
			String gigaByteString = myNumberFormat.format(dataRemining) + " (Mb)";
			
			return gigaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting peak data remaining in Gigabytes
	 * @return string value of peak data used to date in Gb
	 */
	public String getPeakDataUsedGb(){
		if (isCurrentPeakUsedSet()){
			
			// Get peak data used in Gb
			double dataUsed = getCurrentPeakUsed()/1000000/1000;
			
			// Format Mega Byte value
			String megaByteString = myNumberFormat.format(dataUsed);
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting off peak data remaining in Gigabytes
	 * @return string value of off peak data used to date in Gb
	 */
	public String getOffpeakDataUsedGb(){
		if (isCurrentOffpeakUsedSet()){
			
			// Get peak data used in Gb
			double dataUsed = getCurrentOffpeakUsed()/1000000/1000;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(dataUsed);
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_number);
		}
	}
	
	/**
	 * Method for getting peak data remaining in Megabytes
	 * @return string value of peak data used to date in Mb
	 */
	public String getPeakDataUsedMb(){
		if (isCurrentPeakUsedSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentPeakUsed()/1000000;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(dataUsed) + " (Mb)";
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting off peak data remaining in Megabytes
	 * @return string value of off peak data used to date in Mb
	 */
	public String getOffpeakDataUsedMb(){
		if (isCurrentOffpeakUsedSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentOffpeakUsed()/1000000;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(dataUsed) + " (Mb)";
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting peak daily data remaining in Megabytes
	 * @return string value of peak data used to date in Mb
	 */
	public String getPeakDailyDataUsedMb(){
		if (isCurrentPeakUsedSet() && isCurrentDaysSoFarSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentPeakUsed()/1000000;
			
			// Get days so far
			double daysSoFar = getCurrentDaysSoFar();
			
			// Calculate average daily data
			double dailyDataUsed = dataUsed/daysSoFar;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(dailyDataUsed) + " (Mb)";
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting off peak daily data remaining in Megabytes
	 * @return string value of off peak data used to date in Mb
	 */
	public String getOffpeakDailyDataUsedMb(){
		if (isCurrentOffpeakUsedSet() && isCurrentDaysSoFarSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentOffpeakUsed()/1000000;
			
			// Get days so far
			double daysSoFar = getCurrentDaysSoFar();
			
			// Calculate average daily data
			double dailyDataUsed = dataUsed/daysSoFar;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(dailyDataUsed) + " (Mb)";
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting peak data used in Megabytes
	 * @return string value of peak data used to date in Mb
	 */
	public String getPeakDailyDataSuggestedMb(){
		if (isCurrentPeakUsedSet() && isCurrentDaysToGoSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentPeakUsed()/1000000;
			
			// Get peak quota
			double quota = getPeakQuota();
			
			// Calculate remaining data
			double dataRemining = quota - dataUsed;
			
			// Get days to go
			double daysToGo = getCurrentDaysToGo();
			
			// Calculate suggested daily usage
			double suggestedDailyMb = dataRemining/daysToGo;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(suggestedDailyMb) + " (Mb)";
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Method for getting off peak data used in Megabytes
	 * @return string value of off peak data used to date in Mb
	 */
	public String getOffpeakDailyDataSuggestedMb(){
		if (isCurrentOffpeakUsedSet() && isCurrentDaysToGoSet()){
			
			// Get peak data used in Mb
			double dataUsed = getCurrentOffpeakUsed()/1000000;
			
			// Get peak quota
			double quota = getOffpeakQuota();
			
			// Calculate remaining data
			double dataRemining = quota - dataUsed;
			
			// Get days to go
			double daysToGo = getCurrentDaysToGo();
			
			// Calculate suggested daily usage
			double suggestedDailyMb = dataRemining/daysToGo;
			
			// Format percentage value
			String megaByteString = myNumberFormat.format(suggestedDailyMb) + " (Mb)";
			
			return megaByteString;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_data_data);
		}
	}
	
	/**
	 * Get the peak data period times
	 * @return string value of start finish times for peak data period
	 */
	public String getPeakPeriod(){
		// Check to see if peak start and end is set
		if (isOffpeakStartSet() && isOffpeakEndSet()){
			
			String peakStart = getOffpeakEnd();
			String peakEnd = getOffpeakStart();
			
			String period = "(" + peakStart + " to " + peakEnd + ")";
			
			return period;
			
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_period);
		}
	}
	
	/**
	 * Get the plans peak period string for landscape view
	 * @return string value of peak the plans peak period
	 */
	public String getPeakPeriodLand(){
		// Check to see if peak start and end is set
		if (isOffpeakStartSet() && isOffpeakEndSet()){
			String peakStart = getOffpeakEnd().substring(0, (getOffpeakEnd().length() - 3));
			String peakEnd = getOffpeakStart().substring(0, (getOffpeakEnd().length() - 3));
			
			String period = removeZeros(peakStart) + getAmPm(peakStart) + " to " + removeZeros(peakEnd) + getAmPm(peakEnd);
			
			return period;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_period);
		}
		
	}
	
	/**
	 * Get 'am' or 'pm' based on hour of the day
	 * @param hourString
	 * @return am or pm based on hour of day
	 */
	public String getAmPm(String hourString){
		int hour = Integer.parseInt(hourString);
		
		if (hour >= 12 ){
			return "pm";
		}
		else{
			return "am";
		}
		
	}
	
	/**
	 * Remove zero characters from string value passed to method
	 * @param s = string value to remove zeros from
	 * @return s with zero's removed
	 */
	public static String removeZeros(String s) {
	
	   String r = "";
	   char zero = '0';
	
	   for (int i = 0; i < s.length(); i ++) {
	      if (s.charAt(i) != zero) r += s.charAt(i);
	   }
	
	   return r;
	}
	
	/**
	 * Get the offpeak data period times
	 * @return string value of start finish times for off peak data period
	 */
	public String getOffpeakPeriod(){
		if (isOffpeakStartSet() && isOffpeakEndSet()){
			
			String offpeakStart = getOffpeakStart();
			String offpeakEnd = getOffpeakEnd();
			
			String period = "(" + offpeakStart + " to " + offpeakEnd + ")";
			
			return period;
			
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_period);
		}
	}
	
	/**
	 * Get the plans offpeak period string for landscape view
	 * @return string value of offpeak the plans peak period
	 */
	public String getOffpeakPeriodLand(){
		// Check to see if peak start and end is set
		if (isOffpeakStartSet() && isOffpeakEndSet()){
			String peakStart = getOffpeakEnd().substring(0, (getOffpeakEnd().length() - 3));
			String peakEnd = getOffpeakStart().substring(0, (getOffpeakEnd().length() - 3));
			
			String period = removeZeros(peakEnd) + getAmPm(peakEnd) + " to " + removeZeros(peakStart) + getAmPm(peakStart);
			
			return period;
		}
		// Else it must not be set so return default XML string value
		else {
			return myApplicationContext.getString(R.string.peak_offpeak_stats_period);
		}
		
	}


}
