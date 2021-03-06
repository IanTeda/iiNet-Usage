package au.id.teda.iinetusage.phone.sax;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class DailyUsageSAXHandler extends DefaultHandler {
	
	//private final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = DailyUsageSAXHandler.class.getSimpleName();
	
	// Objects for storing data types in xml
	private String usageFlag;
	public String typeUsageFlag;
	
	// Object used for temporary storage
	private AccountInfoParseObject tempAcountInfo = new AccountInfoParseObject();
	private AccountStatusParseObject tempAcountStatus = new AccountStatusParseObject();
	private DailyUsageParseObject tempDailyUsage = new DailyUsageParseObject();

	// Objects for database queries
	private DailyDataDBAdapter myDailyDataDB;
	
	// Preference instance
	AccountHelper myAccount = new AccountHelper();
	
	// Set tag variables
	public static final String II_FEED = "ii_feed"; // Man xml parent tag
	public static final String ACCOUNT_INFO = "account_info"; // Account parent tag
	public static final String PLAN = "plan"; // What plan am I on
	public static final String PRODUCT = "product"; // What is the plan product called
	public static final String VOLUME_USAGE = "volume_usage";
	public static final String OFFPEAK_START = "offpeak_start"; // What time does my offpeak start
	public static final String OFFPEAK_END = "offpeak_end"; // What time does my offpeak end
	public static final String QUOTA_RESET = "quota_reset"; // Quota reset parent tag
    public static final String ANNIVERSARY = "anniversary"; // What date does my usage roll over
    public static final String DAYS_SO_FARE = "days_so_far"; // How long am I into my current period
    public static final String DAYS_REMAINING = "days_remaining"; // How many days to go on my current period
    public static final String EXPECTED_TRAFFIC_TYPES = "expected_traffic_types";
    public static final String TYPE = "type";
    public static final String CLASSIFICATION = "classification";
    public static final String USED = "used";
    public static final String NAME = "name";
    public static final String QUOTA_ALLOCATION = "quota_allocation";
    public static final String IS_SHAPED = "is_shaped";
    public static final String DAY_HOUR = "day_hour";
    public static final String PERIOD = "period";
    public static final String USAGE = "usage";
    public static final String CONNECTIONS = "connections";
    public static final String IP = "ip";
	
	//Set inTag variables
    private boolean inFeed = false;
    private boolean inAccountInfo = false;
	private boolean inPlan = false;
	private boolean inProduct = false;
	private boolean inVolumeUsage = false;
	private boolean inOffpeakStart = false;
	private boolean inOffpeakEnd = false;
	private boolean inQuotaReset = false;
	private boolean inAnniversary = false;
	private boolean inDaysSoFare = false;
	private boolean inDaysRemaining = false;
	private boolean inExpectedTrafficTypes = false;
	private boolean inType = false;
	private boolean inName = false;
	private boolean inQuotaAllocation = false;
	private boolean inIsShaped = false;
	private boolean inDayHour = false;
	private boolean inUsage = false;
	private boolean inConnections = false;
	private boolean inIP = false;
	
	// This function is called when the parser reaches an element
	@Override
	public void startElement(String uri, String name, String qName, Attributes atts) {
		//Log.d(DEBUG_TAG, "DailyUsageSAXHandler > startElement: " + name.trim());
		if (name.trim().equalsIgnoreCase(II_FEED)){
			inFeed = true;
		} else if (name.trim().equalsIgnoreCase(ACCOUNT_INFO)){
			inAccountInfo = true;
		} else if (name.trim().equalsIgnoreCase(PLAN)){
			inPlan = true;
		} else if (name.trim().equalsIgnoreCase(PRODUCT)){
			inProduct = true;
		} else if (name.trim().equalsIgnoreCase(VOLUME_USAGE)){
			inVolumeUsage = true;
		} else if (name.trim().equalsIgnoreCase(OFFPEAK_START)){
			inOffpeakStart = true;
		} else if (name.trim().equalsIgnoreCase(OFFPEAK_END)){
			inOffpeakEnd = true;
		} else if (name.trim().equalsIgnoreCase(QUOTA_RESET)){
			inQuotaReset = true;
		} else if (name.trim().equalsIgnoreCase(ANNIVERSARY)){
			inAnniversary = true;
		} else if (name.trim().equalsIgnoreCase(DAYS_SO_FARE)){
			inDaysSoFare = true;
		} else if (name.trim().equalsIgnoreCase(DAYS_REMAINING)){
			inDaysRemaining = true;
		} else if (name.trim().equalsIgnoreCase(EXPECTED_TRAFFIC_TYPES)){
			inExpectedTrafficTypes = true;
		} else if (name.trim().equalsIgnoreCase(TYPE)){
			inType = true;
			String typeClassificationValue = atts.getValue(CLASSIFICATION);
			String typeUsedValue = atts.getValue(USED);
			if (typeClassificationValue.trim().equalsIgnoreCase("peak")){
				tempAcountStatus.peakUsed = typeUsedValue;
				
			} else if (typeClassificationValue.trim().equalsIgnoreCase("offpeak")){
				tempAcountStatus.offpeakUsed = typeUsedValue;
				
			} else if (typeClassificationValue.trim().equalsIgnoreCase("uploads")){
				tempAcountStatus.uploadsUsed = typeUsedValue;
				
			} else if (typeClassificationValue.trim().equalsIgnoreCase("freezone")){
				tempAcountStatus.freezoneUsed = typeUsedValue;
				
			}
			
		} else if (name.trim().equalsIgnoreCase(NAME)){
			inName = true;
		} else if (name.trim().equalsIgnoreCase(QUOTA_ALLOCATION)){
			inQuotaAllocation = true;
		} else if (name.trim().equalsIgnoreCase(IS_SHAPED)){
			inIsShaped = true;
			if (usageFlag.trim().equalsIgnoreCase("peak")){
				tempAcountStatus.peakShapingSpeed = atts.getValue("speed");
				
			} else if (usageFlag.trim().equalsIgnoreCase("offpeak")){
				tempAcountStatus.offpeakShapingSpeed = atts.getValue("speed");
				
			}
			
		} else if (name.trim().equalsIgnoreCase(DAY_HOUR)){
			inDayHour = true;
			String dayHourValue = atts.getValue(PERIOD);
			tempDailyUsage.date =  dayHourValue;
			
		} else if (name.trim().equalsIgnoreCase(USAGE)){
			inUsage = true;
			typeUsageFlag = atts.getValue(TYPE);
			
		} else if (name.trim().equalsIgnoreCase(CONNECTIONS)){
			inConnections = true;
		} else if (name.trim().equalsIgnoreCase(IP)){
			inIP = true;
			tempAcountStatus.uptime = atts.getValue("on_since");
		}
	 }
	
	// This function is called when the parser reaches the end of an element
	@Override
	public void endElement(String uri, String name, String qName) {
		//Log.d(DEBUG_TAG, "DailyUsageSAXHandler > endElement: " + name.trim());
		if (name.trim().equalsIgnoreCase(II_FEED)){
			inFeed = false;
		} else if (name.trim().equalsIgnoreCase(ACCOUNT_INFO)){
			inAccountInfo = true; // TODO: This could be an error will need to check
		} else if (name.trim().equalsIgnoreCase(PLAN)){
			inPlan = false;
		} else if (name.trim().equalsIgnoreCase(PRODUCT)){
			inProduct = false;
		} else if (name.trim().equalsIgnoreCase(VOLUME_USAGE)){
			inVolumeUsage = false;
		} else if (name.trim().equalsIgnoreCase(OFFPEAK_START)){
			inOffpeakStart = false;
		} else if (name.trim().equalsIgnoreCase(OFFPEAK_END)){
			inOffpeakEnd = false;
		} else if (name.trim().equalsIgnoreCase(QUOTA_RESET)){
			inQuotaReset = false;
		} else if (name.trim().equalsIgnoreCase(ANNIVERSARY)){
			inAnniversary = false;
		} else if (name.trim().equalsIgnoreCase(DAYS_SO_FARE)){
			inDaysSoFare = false;
		} else if (name.trim().equalsIgnoreCase(DAYS_REMAINING)){
			inDaysRemaining = false;
		} else if (name.trim().equalsIgnoreCase(EXPECTED_TRAFFIC_TYPES)){
			inExpectedTrafficTypes = false;
		} else if (name.trim().equalsIgnoreCase(TYPE)){
			inType = false;
		} else if (name.trim().equalsIgnoreCase(NAME)){
			inName = false;
		} else if (name.trim().equalsIgnoreCase(QUOTA_ALLOCATION)){
			inQuotaAllocation = false;
		} else if (name.trim().equalsIgnoreCase(IS_SHAPED)){
			inIsShaped = false;
		} else if (name.trim().equalsIgnoreCase(DAY_HOUR)){
			inDayHour = false;
		} else if (name.trim().equalsIgnoreCase(USAGE)){
			inUsage = false;
		} else if (name.trim().equalsIgnoreCase(CONNECTIONS)){
			inConnections = false;
		} else if (name.trim().equalsIgnoreCase(IP)){
			inIP = false;
		}
		
		// Check to see if we have values for 'Days to Go', 'Days so Fare' & first date in  xml
		// Then check that we haven't set the data period yet
		if (tempAcountStatus.daysToGo != null 
				&& tempAcountStatus.daysSoFar != null 
				&& tempDailyUsage.date != null
				&& tempDailyUsage.period == null){
		
				try {
					
					// Set date format for string input and output
					SimpleDateFormat myInputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat myOutputDateFormat = new SimpleDateFormat("MMM yyyy");
					
					// Convert date string to date
					Date myDate = myInputDateFormat.parse(tempDailyUsage.date);
					
					// Add date to calendar and then add 27 days to get data period
					Calendar myCalendar = Calendar.getInstance();
					myCalendar.setTime(myDate);
					myCalendar.add(Calendar.DATE, 27);
					
					// Convert and set date to string value
					tempDailyUsage.period = myOutputDateFormat.format(myCalendar.getTime());
					
				// Catch any parse errors during string to date parse
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		// Check to see if we have all the data required for a dailyDB entry
		if (tempDailyUsage.date != null
				&& tempDailyUsage.period != null
				&& tempDailyUsage.peak != null 
				&& tempDailyUsage.offpeak != null
				&& tempDailyUsage.uploads !=null
				&& tempDailyUsage.freezone !=null ){
						
			// If we have all the data add to database
			long date = StringToLongDate(tempDailyUsage.date, "yyyy-MM-dd").getTime();
			String period = tempDailyUsage.period;
			long peak = Long.parseLong(tempDailyUsage.peak);
			long offpeak = Long.parseLong(tempDailyUsage.offpeak);
			long upload = Long.parseLong(tempDailyUsage.uploads);
			long freezone = Long.parseLong(tempDailyUsage.freezone );
			
			myDailyDataDB = new DailyDataDBAdapter();
			myDailyDataDB.open();
			myDailyDataDB.createDailyUsage(date, period, peak, offpeak, upload, freezone);
			myDailyDataDB.close();
			
			/**
			Log.d(DEBUG_TAG, "Insert DailyUsageDB > " + " date: "+ date + " | period: "+ period 
					+ " | peak: " + peak + " | offpeak: " + offpeak + " | upload: " + upload + " | freezone: " + freezone);
			**/
			
			// Clear objects for next pass over xml
			tempDailyUsage.date = null;
			tempDailyUsage.peak = null;
			tempDailyUsage.offpeak = null;
			tempDailyUsage.uploads = null;
			tempDailyUsage.freezone = null;
		}
		
		// Check to see if we have all the data required for a AccountInfoDB entry
		if (tempAcountInfo.plan != null 
				&& tempAcountInfo.product != null 
				&& tempAcountInfo.offpeakStart != null
				&& tempAcountInfo.offpeakEnd !=null
				&& tempAcountInfo.peakQuota !=null
				&& tempAcountInfo.offpeakQuota !=null
				&& tempDailyUsage.period != null){
			
			// If we have all the data add to shared preferences
			String plan = tempAcountInfo.plan;
			String product = tempAcountInfo.product;
			String offpeakStart = tempAcountInfo.offpeakStart;
			String offpeakEnd = tempAcountInfo.offpeakEnd;
			long peakQuota = Long.parseLong(tempAcountInfo.peakQuota);
			long offpeakQuota = Long.parseLong(tempAcountInfo.offpeakQuota);
			
			if (myAccount.checkDataPeriodLatest(tempDailyUsage.period)){
				myAccount.setAccountInfo(plan, product, offpeakStart, offpeakEnd, peakQuota, offpeakQuota);
			}
			
			// Clear objects for next pass over xml
			tempAcountInfo.plan = null;
			tempAcountInfo.product = null;
			tempAcountInfo.offpeakStart = null;
			tempAcountInfo.offpeakEnd = null;
			tempAcountInfo.peakQuota = null;
			tempAcountInfo.offpeakQuota = null;
		}
		
		// Check to see if we have all the data required for a AccountInfoDB entry
		if (tempAcountStatus.anniversary != null 
				&& tempAcountStatus.daysSoFar != null 
				&& tempAcountStatus.daysToGo != null
				&& tempAcountStatus.peakShaped !=null
				&& tempAcountStatus.offpeakShaped !=null
				&& tempAcountStatus.peakUsed !=null
				&& tempAcountStatus.offpeakUsed !=null
				&& tempAcountStatus.uploadsUsed !=null
				&& tempAcountStatus.freezoneUsed !=null
				&& tempAcountStatus.peakShapingSpeed !=null
				&& tempAcountStatus.offpeakShapingSpeed !=null
				&& tempAcountStatus.uptime != null
				&& tempAcountStatus.ipAddress != null
				&& tempDailyUsage.period != null){
			
			// If we have all the data add to shared preferences
			long systemDateTime = System.nanoTime(); // Set current time
			String period = tempDailyUsage.period;
			String ip_address = tempAcountStatus.ipAddress; 
			long uptime = StringToLongDate(tempAcountStatus.uptime, "yyyy-MM-dd HH:mm:ss").getTime();
			long days_so_far = Long.parseLong(tempAcountStatus.daysSoFar); 
			long days_to_go = Long.parseLong(tempAcountStatus.daysToGo); 
			//long anniversary = Long.parseLong(tempAcountStatus.anniversary);
			long anniversary = System.currentTimeMillis() + (days_to_go * 24 * 60 * 60 * 1000); //TODO: Can this be changed to current sytem time + days to go?
			long peak_used = Long.parseLong(tempAcountStatus.peakUsed);
			long offpeak_used = Long.parseLong(tempAcountStatus.offpeakUsed);
			long upload_used = Long.parseLong(tempAcountStatus.uploadsUsed);  
			long freezone_used = Long.parseLong(tempAcountStatus.freezoneUsed);
			long peak_shaped_speed = Long.parseLong(tempAcountStatus.peakShapingSpeed);
			long offpeak_shaped_speed = Long.parseLong(tempAcountStatus.offpeakShapingSpeed);
			int peak_shaped = returnBooleanInt(tempAcountStatus.peakShaped);
			int offpeak_shaped = returnBooleanInt(tempAcountStatus.offpeakShaped);
			
			myAccount.setAccoutStatus(systemDateTime, period, anniversary,
					days_so_far, days_to_go, peak_shaped, offpeak_shaped,
					peak_used, offpeak_used, upload_used, freezone_used,
					peak_shaped_speed, offpeak_shaped_speed,
					uptime, ip_address );
			
			/**Log.d(DEBUG_TAG, "DailyUsageSAXHandler > endElement > insert accountStatusDB entry (" + refresh_date + ", "
					+ ip_address + ", "
					+ uptime + ", "
					+ anniversary + ", "
					+ days_so_far + ", "
					+ days_to_go + ", "
					+ peak_used + ", "
					+ offpeak_used + ", "
					+ upload_used + ", "
					+ freezone_used + ", "
					//+ peak_shaped + ", "
					//+ offpeak_shaped + ", "
					+ peak_shaping_speed + ", "
					+ offpeak_shaping_speed + ");");**/
			
			// Clear objects for next pass over xml
			tempAcountStatus.anniversary = null;
			tempAcountStatus.daysSoFar = null;
			tempAcountStatus.daysToGo = null;
			tempAcountStatus.peakShaped = null;
			tempAcountStatus.offpeakShaped = null;
			tempAcountStatus.peakUsed = null;
			tempAcountStatus.offpeakUsed = null;
			tempAcountStatus.uploadsUsed = null;
			tempAcountStatus.freezoneUsed = null;
			tempAcountStatus.peakShapingSpeed = null;
			tempAcountStatus.offpeakShapingSpeed = null;
			tempAcountStatus.uptime = null;
			tempAcountStatus.ipAddress = null;
		}
	}

	// This is called when we are between the start and end
	@Override
	public void characters(char ch[], int start, int length) {
        String chars = (new String(ch).substring(start, start + length));
        if (inFeed && inAccountInfo && inPlan){
        	tempAcountInfo.plan = chars;
        	
        } else if (inFeed && inAccountInfo && inProduct){
			tempAcountInfo.product = chars;
			
		} else if (inFeed && inVolumeUsage && inOffpeakStart){
			tempAcountInfo.offpeakStart = chars;
			
		} else if (inFeed && inVolumeUsage && inOffpeakEnd){
			tempAcountInfo.offpeakEnd = chars;
			
		} else if (inFeed && inVolumeUsage && inQuotaReset && inAnniversary){
			tempAcountStatus.anniversary = chars;
			
		} else if (inFeed && inVolumeUsage && inQuotaReset && inDaysSoFare){
			tempAcountStatus.daysSoFar = chars;
			
		} else if (inFeed && inVolumeUsage && inQuotaReset && inDaysRemaining){
			tempAcountStatus.daysToGo = chars;
			
		} else if (inFeed && inVolumeUsage && inExpectedTrafficTypes && inType && inName){
			usageFlag = chars;
			
		} else if (inFeed && inVolumeUsage && inExpectedTrafficTypes && inType && inQuotaAllocation){
			if (usageFlag.trim().equalsIgnoreCase("peak")){
				tempAcountInfo.peakQuota = chars;

			} else if (usageFlag.trim().equalsIgnoreCase("offpeak")){
				tempAcountInfo.offpeakQuota = chars;
			}
			
		} else if (inFeed && inVolumeUsage && inExpectedTrafficTypes && inType && inIsShaped){
			if (usageFlag.trim().equalsIgnoreCase("peak")){
				tempAcountStatus.peakShaped = chars;
			
			} else if (usageFlag.trim().equalsIgnoreCase("offpeak")){
				tempAcountStatus.offpeakShaped = chars;
			}
			
			
		} else if (inFeed && inVolumeUsage && inDayHour && inUsage){
			if (typeUsageFlag.trim().equalsIgnoreCase("peak")){
				tempDailyUsage.peak = chars;
				
			} else if (typeUsageFlag.trim().equalsIgnoreCase("offpeak")){
				tempDailyUsage.offpeak = chars;
				
			} else if (typeUsageFlag.trim().equalsIgnoreCase("uploads")){
				tempDailyUsage.uploads = chars;
				
			} else if (typeUsageFlag.trim().equalsIgnoreCase("freezone")){
				tempDailyUsage.freezone = chars;
			}
			
		} else if (inConnections && inIP){
			tempAcountStatus.ipAddress = chars;
			
		}
	}
	
	private Date StringToLongDate(String dateStringToLong , String dateFormat) {
		SimpleDateFormat inputFormat = new SimpleDateFormat(dateFormat);
        Date convertedDate = null;
		try {
			convertedDate = inputFormat.parse(dateStringToLong);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}
	
	private int returnBooleanInt (String stringBooolean){
		if (stringBooolean == "true"){
			return 1;
		} else {
			return 0;
		}
	}
	
	

}
