package au.id.teda.volumeusage.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.volumeusage.activity.MainActivity;
import au.id.teda.volumeusage.database.AccountInfoDBAdapter;
import au.id.teda.volumeusage.database.AccountStatusDBAdapter;

public class AccountHelper {
	
	/**
	 *  Static tag strings for loging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AccountHelper.class.getSimpleName();
	
	private static final String PEAK = "peak";
	private static final String OFFPEAK = "offpeak";
	
	/**
	 *  Object for storing context
	 */
	private Context context;
	
	// TODO: look at ContentProvider for keeping cursor alive

	// AccountHelper constructor
	public AccountHelper(Context context) {
		this.context = context; // Set context for methods
	}
	
	// Check if account info exists
	public boolean infoExists() {
		//Log.d(DEBUG_TAG, "AccountHelper > infoExists()");
        AccountInfoDBAdapter accountInfoDBAdapter = new AccountInfoDBAdapter(context);
        accountInfoDBAdapter.open();
        boolean infoExists = accountInfoDBAdapter.checkAccountInfoExists();
        accountInfoDBAdapter.close();
        if (infoExists){
        	return true;
        } else {
        	return false;
        }
	}
	
	// Check if status info exists
	public boolean statusExists() {
		//Log.d(DEBUG_TAG, "AccountHelper > infoExists()");
        AccountStatusDBAdapter accountStatusDBAdapter = new AccountStatusDBAdapter(context);
        accountStatusDBAdapter.open();
        boolean infoExists = accountStatusDBAdapter.checkAccountInfoExists();
        accountStatusDBAdapter.close();
        if (infoExists){
        	return true;
        } else {
        	return false;
        }
	}
	
	// Check if username exists
	public boolean usernameExists(){
		//Log.d(DEBUG_TAG, "AccountHelper > usernameExists()");
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String myUsername = settings.getString("iinet_username", "");
        if (myUsername.length() > 0 ){
        	return true;
        } else {
        	return false;
        }
	}
	
	// Check if password exists
	public boolean passwordExists(){
		//Log.d(DEBUG_TAG, "AccountHelper > passwordExists()");
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String myPassword = settings.getString("iinet_password", "");
        if (myPassword.length() > 0 ){
        	return true;
        } else {
        	return false;
        }
	}
	
	// Get quota and used data calc percentage and then build return string
	public String dataUsageString(String period){
		//Log.d(DEBUG_TAG, "DashboardUsageStats > dataUsageString() > Period: " + period);
		
		// Get quota and used data values
		double quotaLong = getAccountQuota(period);
		double usedLong = getAccountUsed(period);
		
		// Set Percentage format
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(0);
		
		// Set usage format
		NumberFormat numberFormat = new DecimalFormat("#,###.0");
		Double usedDouble = usedLong/1000;
		
		// Build string for return statement
		String usageString = numberFormat.format(usedDouble) + "Gb (" + percentFormat.format(usedLong/quotaLong) + ")";
		//Log.d(DEBUG_TAG, "DashboardUsageStats > dataUsageString() > usageString: " + usageString);
		return usageString;
	}
	
	// Get quota and used data calc percentage and then build return string
	public String getdataRemainingString(String period){
		//Log.d(DEBUG_TAG, "DashboardUsageStats > dataUsageString() > Period: " + period);
		
		// Get quota and used data values
		double quotaLong = getAccountQuota(period);
		double usedLong = getAccountUsed(period);
		
		// Calculate remainingg data
		double remaingLong = quotaLong - usedLong;
		
		// Set Percentage format
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(0);
		
		// Set usage format
		NumberFormat numberFormat = new DecimalFormat("#,###.0");
		Double remainingDouble = remaingLong/1000;
		
		// Build string for return statement
		String remainingString = numberFormat.format(remainingDouble) + "Gb (" + percentFormat.format(remaingLong/quotaLong) + ")";
		//Log.d(DEBUG_TAG, "DashboardUsageStats > dataUsageString() > usageString: " + usageString);
		return remainingString;
	}
	
	// Get account quota from database based on period flag
	public long getAccountQuota(String period){
        //Log.d(DEBUG_TAG, "MainActivity > getAccountQuota() > Period: " + period);
    	long usage = 0;
    	
    	AccountHelper accountInfo = new AccountHelper(context);
        if (accountInfo.infoExists()){
            AccountInfoDBAdapter accountInfoDBAdapter = new AccountInfoDBAdapter(context);
            accountInfoDBAdapter.open();
            Cursor cursor = accountInfoDBAdapter.fetchAccountInfo(1);
            if (period == "peak"){
            	usage = cursor.getLong(cursor.getColumnIndex(accountInfoDBAdapter.PEAK_QUOTA)); // Pull peak quota from cursor
            } else if (period == "offpeak"){
            	usage = cursor.getLong(cursor.getColumnIndex(accountInfoDBAdapter.OFFPEAK_QUOTA)); // Pull peak quota from cursor
            }
            //Log.d(DEBUG_TAG, "MainActivity > getAccountQuota() > usage: " + usage);
            cursor.close();
            accountInfoDBAdapter.close();
        } else {
        	usage = 0;
        }
		return usage;
	}
	
	// Get current used data from database based on period flag
	public long getAccountUsed(String period){
		//Log.d(DEBUG_TAG, "MainActivity > getAccountUsed() > Period: " + period);
		long usage = 0;
	        
		AccountHelper accountInfo = new AccountHelper(context);
		if (accountInfo.statusExists()){
			AccountStatusDBAdapter accountStatusDBHelper = new AccountStatusDBAdapter(context);
			accountStatusDBHelper.open();
			Cursor cursor = accountStatusDBHelper.fetchAllAccountStatus();
			cursor.moveToLast();
			if (period == "peak"){
				usage = cursor.getLong(cursor.getColumnIndex(accountStatusDBHelper.PEAK_USED))/1000000; // Pull peak quota from cursor
			} else if (period == "offpeak"){
				usage = cursor.getLong(cursor.getColumnIndex(accountStatusDBHelper.OFFPEAK_USED))/1000000; // Pull peak quota from cursor
			}
			//Log.d(DEBUG_TAG, "MainActivity > getAccountUsed() > usage: " + usage);       
			cursor.close();
			accountStatusDBHelper.close();
		} else {
			usage = 0;
		}
		return usage;
	}
	
	// Get data used as a percentage
	public double getPercentageUsed(String period){
		// Get quota and used data values
		double quotaLong = getAccountQuota(period);
		double usedLong = getAccountUsed(period);
		
		double percentageUsedDouble = usedLong/quotaLong;
		
		return percentageUsedDouble;
	}
	
	// Check if data usage is over quota
	// TODO: Delete here and add to AccoutStatus Helper
	public boolean usageOver(String period){
		boolean check = false;
		double dataUsed = getPercentageUsed(period);
		
		if (dataUsed > 1){
			check = true;
		} else {
			check = false;
		}
		return check;
	}
	
	// Check if data usage is over usage alert preference
	// TODO: Move to AccountStatusHelper
	public boolean usageAlert(String period){
		// Get peak and offpeak alert preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		boolean alert = false;
		
		// Check for peak usage alert
		if (period == "peak"){
			
			double peakAlert = settings.getInt("peak_usage_alert", 75);
			peakAlert = peakAlert/100;
			double peakUsed = getPercentageUsed("peak");
			
			if (peakUsed > peakAlert){
				alert = true;
			} else {
				alert = false;
			}
			
		// Check for offpeak usage alert
		} else if (period == "offpeak"){
			
			double offpeakAlert = settings.getInt("offpeak_usage_alert", 75);
			offpeakAlert = offpeakAlert/100;
			double offpeakUsed = getPercentageUsed("offpeak");
			
			if (offpeakUsed > offpeakAlert){
				alert = true;
			} else {
				alert = false;
			}
		}
		return alert;
	}

	// Get account plan & product
	public String getPlanProduct(){
		String planProduct = null;
        if (infoExists()){
        	AccountInfoDBAdapter accountInfoDBAdapter = new AccountInfoDBAdapter(context);
        	accountInfoDBAdapter.open();
            Cursor cursor = accountInfoDBAdapter.fetchAccountInfo(1);
            String plan = cursor.getString(cursor.getColumnIndex(accountInfoDBAdapter.PLAN)); // Pull peak quota from cursor
            String product = cursor.getString(cursor.getColumnIndex(accountInfoDBAdapter.PRODUCT)); // Pull peak quota from cursor
            cursor.close();
            accountInfoDBAdapter.close();
            planProduct = "<b>Plan:</b> " + plan + " (" + product + ")";
        } else {
        	planProduct = "<b>Plan:</b> No Plan Set";
        }
		return planProduct;
	}
	
	// Get plan off peak start and end times
	public String getPeakPeriod(){
		String offpeakTime = null;
        if (infoExists()){
        	AccountInfoDBAdapter accountInfoDBAdapter = new AccountInfoDBAdapter(context);
        	accountInfoDBAdapter.open();
            Cursor cursor = accountInfoDBAdapter.fetchAccountInfo(1);
            String offpeakStart = cursor.getString(cursor.getColumnIndex(accountInfoDBAdapter.OFFPEAK_START)); 
            String offpeakEnd = cursor.getString(cursor.getColumnIndex(accountInfoDBAdapter.OFFPEAK_END));
            cursor.close();
            accountInfoDBAdapter.close();
            offpeakTime = "<b>Offpeak Period:</b> " + offpeakStart + " " + offpeakEnd;
        } else {
        	offpeakTime = "<b>Offpeak Period:</b> No Time Set";
        }
		return offpeakTime;
	}
	
	public String getQuotaString(){
		
		String quotaString = null;
		if (infoExists()){
			String peakQuota = String.valueOf(getAccountQuota(PEAK) / 1000);
			String offpeakQuota = String.valueOf(getAccountQuota(OFFPEAK) / 1000);
			quotaString = "<b>Quota:</b> "
					+ peakQuota + "Gb / " 
					+ offpeakQuota + "Gb";
            
		} else {
			quotaString = "<b>Peak/Offpeak Quota:</b> --Gb / --Gb";
		}
		return quotaString;
	}
	
	// Get ip address of latest status update
	public String getIPAddress(){
		String ipAddress = null;
        if (statusExists()){
        	AccountStatusDBAdapter accountStatusDBAdapter = new AccountStatusDBAdapter(context);
        	accountStatusDBAdapter.open();
            Cursor cursor = accountStatusDBAdapter.fetchAllAccountStatus();
            cursor.moveToLast();
            String ip = cursor.getString(cursor.getColumnIndex(accountStatusDBAdapter.IP_ADDRESS));
            cursor.close();
            accountStatusDBAdapter.close();
            ipAddress = "<b>IP Address:</b> " + ip;
        } else {
        	ipAddress = "<b>IP Address:</b> No IP Set";
        }
		return ipAddress;
	}
	
	// Get up time of broadband connection
	public String getUpTime(){
		String upTime = null;
        if (statusExists()){
        	AccountStatusDBAdapter accountStatusDBAdapter = new AccountStatusDBAdapter(context);
        	accountStatusDBAdapter.open();
            Cursor cursor = accountStatusDBAdapter.fetchAllAccountStatus();
            cursor.moveToLast();
            long upTimeMilli = cursor.getLong(cursor.getColumnIndex(accountStatusDBAdapter.UPTIME));
            cursor.close();
            accountStatusDBAdapter.close();
            long currentTimeMilli = System.currentTimeMillis();
            long diffDays = (currentTimeMilli - upTimeMilli)  / (24 * 60 * 60 * 1000);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
            Date resultdate = new Date(upTimeMilli);
            
            upTime = "<b>Up Time:</b> " + dateFormat.format(resultdate) + " (" + diffDays + " Days)";
            
        } else {
        	upTime = "<b>On Since:</b> dd MMM yyyy --:-- (--Days)";
        }
		return upTime;
	}
	
	// Get account rollover data
	public String getRollOver(){
		String rollOverString = null;
        if (statusExists()){
        	AccountStatusDBAdapter accountStatusDBAdapter = new AccountStatusDBAdapter(context);
        	accountStatusDBAdapter.open();
            Cursor cursor = accountStatusDBAdapter.fetchAllAccountStatus();
            cursor.moveToLast();
            long rollOverMilli = cursor.getLong(cursor.getColumnIndex(accountStatusDBAdapter.ANNIVERSARY));
            cursor.close();
            accountStatusDBAdapter.close();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
            Date resultdate = new Date(rollOverMilli);
            
            rollOverString = "<b>Rollover:</b> " + dateFormat.format(resultdate);
            
        } else {
        	rollOverString = "<b>Rollover:</b> dd MMM yyyy";
        }
		return rollOverString;
	}
	
	// Get a long value of the number of days so fare
	public long getDaysSoFare(){
		if (statusExists()){
        	AccountStatusDBAdapter accountStatusDBAdapter = new AccountStatusDBAdapter(context);
        	accountStatusDBAdapter.open();
            Cursor cursor = accountStatusDBAdapter.fetchAllAccountStatus();
            cursor.moveToLast();
            long daysToGoLong = cursor.getLong(cursor.getColumnIndex(accountStatusDBAdapter.DAYS_SO_FARE));
            cursor.close();
            accountStatusDBAdapter.close();
            return daysToGoLong;
		} else {
			return (Long) null;	
		}
	}
	
	// Get account rollover data
	public String getDaysToGoString(){
		String daysToGoString = null;
        if (statusExists()){
            long daysToGoLong = getDaysToGo(); 
            
            daysToGoString = "<b>Days To Go:</b> " + Long.toString(daysToGoLong) + " Days";
            
        } else {
        	daysToGoString = "<b>Days To Go:</b> -- Days";
        }
		return daysToGoString;
	}
	
	// Get a long value of the number of days so fare
	public long getDaysToGo(){
		if (statusExists()){
        	AccountStatusDBAdapter accountStatusDBAdapter = new AccountStatusDBAdapter(context);
        	accountStatusDBAdapter.open();
            Cursor cursor = accountStatusDBAdapter.fetchAllAccountStatus();
            cursor.moveToLast();
            long daysToGoLong = cursor.getLong(cursor.getColumnIndex(accountStatusDBAdapter.DAYS_TO_GO));
            cursor.close();
            accountStatusDBAdapter.close();
            return daysToGoLong;
		} else {
			return (Long) null;	
		}
	}
	
	// Get account rollover data
	public String getDaysSoFareString(){
		String daysSoFareString = null;
        if (statusExists()){
            daysSoFareString = "<b>Days So Fare:</b> " + Long.toString(getDaysSoFare()) + " Days";
        } else {
        	daysSoFareString = "<b>Days So Fare:</b> -- Days";
        }
		return daysSoFareString;
	}
	
	// Get peak data used string
	public String getDataUsedPeak(){
		//Log.d(DEBUG_TAG, "getDataUsed()");
		String dataUsedString = null;
		if (statusExists()){
			dataUsedString = "<b>Peak Usage:</b> " + dataUsageString("peak");
		} else {
			dataUsedString = "<b>Peak Usage:</b> --Gb (---%)";
		}
		
		return dataUsedString;
	}
	
	// Get offpeak data used string
	public String getDataUsedOffpeak(){
		//Log.d(DEBUG_TAG, "getDataUsed()");
		String dataUsedString = null;
		if (statusExists()){
			dataUsedString = "<b>Offpeak Usage:</b> " + dataUsageString("offpeak");
		} else {
			dataUsedString = "<b>Offpeak Usage:</b> --Gb (---%)";
		}
		
		return dataUsedString;
	}


	// Get usage trend for days so far
	public String getUsageTrendString(String period){
		String usageTrendString = null;
		String periodString = null;
		
		if (period == "peak"){
			periodString = "Peak";
		} else if (period == "offpeak"){
			periodString = "Offeak";
		} else {
			periodString = "No period set";
		}
		
		if (statusExists()){
			double usedLong = getAccountUsed(period);
			long daysSoFareLong = getDaysSoFare();
	        
			//Log.d(DEBUG_TAG, "getUsageTrendString() > " + usedLong);
			//Log.d(DEBUG_TAG, "getUsageTrendString() > " + daysSoFareLong);
			
			NumberFormat numberFormat = new DecimalFormat("#,###");
	            
			usageTrendString = "<b>" + periodString + " Used:</b> " + numberFormat.format(usedLong/daysSoFareLong) + " Mb/Day";
		} else {
			usageTrendString = "<b>" + periodString + " Used:</b> --Mb (---%)";
		}
			
		return usageTrendString;
	}

	// Get suggested usage for days to go
	public String getSuggestedUseString(String period){
		String suggestedUseString = null;
		String periodString = null;
		
		if (period == "peak"){
			periodString = "Peak";
		} else if (period == "offpeak"){
			periodString = "Offeak";
		} else {
			periodString = "No period set";
		}
		
		if (statusExists()){
			long usedLong = getAccountUsed(period);
			long quotaLong = getAccountQuota(period);
			long daysToGoLong = getDaysToGo();
	            
			NumberFormat numberFormat = new DecimalFormat("#,###");
	            
			suggestedUseString = "<b>Suggested " + periodString + ":</b> " + numberFormat.format((quotaLong - usedLong)/daysToGoLong) + " Mb/Day";
		} else {
			suggestedUseString = "<b>Suggested " + periodString + ":</b> --Mb (---%)";
		}
			
		return suggestedUseString;
	}

	// Check if username and password exist. Return true if they do
	public boolean checkUsernamePassword() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context); //TODO: Change this to accoutHelper
        String myUsername = settings.getString("iinet_username", "");
        String myPassword = settings.getString("iinet_password", "");
        if (myUsername.length() == 0 || myPassword.length() == 0){
            Log.i(INFO_TAG, "checkUsernamePassword(): false");
        	return false;
        } else {
            Log.i(INFO_TAG, "checkUsernamePassword(): true");
        	return true;
        }
	}
	
	// Return Current Data Period
	public String getCurrentPeriod(){
		
		// Convert string value of days to go into millseconds
		Long datePeriodMillsecToGo = getDaysToGo()*24*60*60*1000;
		
		// Add current time in millseconds to days to go
		Long datePeriodMillsec = System.currentTimeMillis() + datePeriodMillsecToGo;
		
		// Convert millesconds timestamp into MMM yyyy
		SimpleDateFormat date_format = new SimpleDateFormat("MMM yyyy");
		
		// Create string
		String datePeriodString = date_format.format(datePeriodMillsec);
		
		// TODO: Should be able to just have "date_format.format(datePeriodMillsec)" as return no need for string first 
		
		return datePeriodString;
		
	}
	
	// Return Current Data Period
	public String getPeriodString(){
		
		Long datePeriodMillsecToGo = getDaysToGo()*24*60*60*1000; // Convert string value of days to go into millseconds
		Long datePeriodMillsec = System.currentTimeMillis() + datePeriodMillsecToGo; // Add current time in millseconds to days to go
		SimpleDateFormat date_format = new SimpleDateFormat("MMMMM yyyy"); // Convert millesconds timestamp into MMM yyyy
		String datePeriodString = date_format.format(datePeriodMillsec);
		
		return datePeriodString;
		
	}

	public String[] getArchivedMonths() {
		
		// Set current data period
		String CURRENT_PERIOD = getPeriodString();
		Log.d(DEBUG_TAG, "getArchivedMonths()> CURRENT_PERIOD: " + CURRENT_PERIOD);
		
		// Create an array of Strings, that will be put to our ListActivity
		String[] values = new String[] { "2011 October", "2011 September", "2011 August", "Add" };
		return values;
		
	}
}
