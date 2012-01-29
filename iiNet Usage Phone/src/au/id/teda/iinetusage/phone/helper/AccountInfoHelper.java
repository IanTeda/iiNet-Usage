package au.id.teda.iinetusage.phone.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.iinetusage.phone.AppGlobals;

public class AccountInfoHelper {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = AccountInfoHelper.class.getSimpleName();
	
	// Create instance of shared preferences based on app context
	protected SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(AppGlobals.getAppContext());
	protected SharedPreferences.Editor myEditor = mySettings.edit();
	
	// Set static string values for preference keys
	private final static String PLAN = "plan";
	private final static String PRODUCT = "product";
	private final static String OFF_PEAK_START = "offPeakStart";
	private final static String OFF_PEAK_END = "offPeakEnd";
	private final static String PEAK_QUOTA = "peakDataQuota";
	private final static String OFF_PEAK_QUOTA = "offPeakDataQuota";
	
	/**
	 * Method for setting account info values to shared preferences
	 * @param plan
	 * @param product
	 * @param offPeakStart
	 * @param offPeakEnd
	 * @param peakQuota
	 * @param offpeakQuota
	 */
	public void setAccountInfo(String plan, String product,
			String offPeakStart, String offPeakEnd, long peakQuota,
			long offpeakQuota){
		
		myEditor.putString(PLAN, plan);
		myEditor.putString(PRODUCT, product);
		myEditor.putString(OFF_PEAK_START, offPeakStart);
		myEditor.putString(OFF_PEAK_END, offPeakEnd);
		myEditor.putLong(PEAK_QUOTA, peakQuota);
		myEditor.putLong(OFF_PEAK_QUOTA, offpeakQuota);
		myEditor.commit();
		
	}
	
	/**
	 * Method for checking if all account information exists
	 * @return true if all data present
	 */
	public boolean infoExists() {
		// Check to see if we have all the account information stored
		if (getPlan().length() > 0
				&& getProduct().length() >0
				&& getOffPeakStart().length() > 0
				&& getOffPeakEnd().length() > 0
				&& getPeakQuota() > 0
				&& getOffPeakQuota() > 0){
			
			// Looks like it does so lets return true
			return true;
		} else {
			
			// Doesn't seem to be all there so return false
			return false;
		}
	}
	
	/**
	 * Method for getting account plan
	 * @return
	 */
	public String getPlan(){
		return mySettings.getString(PLAN, "");
	}
	
	/**
	 * Check if plan has been set
	 * @return true if plan string length is greater then 0
	 */
	public boolean isPlanSet(){
		// Check plan string length is greater then 0
		if (getPlan().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
			
	}
	
	/**
	 * Method for getting account product
	 * @return
	 */
	public String getProduct(){
		return mySettings.getString(PRODUCT, "");
	}
	
	
	/**
	 * Check if product has been set
	 * @return true if product string length is greater then 0
	 */
	public boolean isProductSet(){
		// Check product string length is greater then 0
		if (getProduct().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
			
	}
	
	/**
	 * Method for getting off peak start time
	 * @return
	 */
	public String getOffPeakStart(){
		return mySettings.getString(OFF_PEAK_START, "");
	}
	
	/**
	 * Method for getting off peak end time
	 * @return
	 */
	public String getOffPeakEnd(){
		return mySettings.getString(OFF_PEAK_END, "");
	}
	
	/**
	 * Method for getting peak quota value
	 * @return
	 */
	public Long getPeakQuota(){
		return mySettings.getLong(PEAK_QUOTA, 0);
	}
	
	/**
	 * Method for getting off peak quota vale
	 * @return
	 */
	public Long getOffPeakQuota(){
		return mySettings.getLong(OFF_PEAK_QUOTA, 0);
	}

}
