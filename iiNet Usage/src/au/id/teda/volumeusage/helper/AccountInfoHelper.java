package au.id.teda.volumeusage.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.volumeusage.MyApp;

public class AccountInfoHelper {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = AccountInfoHelper.class.getSimpleName();
	
	// Create instance of shared preferences based on app context
	private SharedPreferences mySettings = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext());
	
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
		
		/**
		Log.d(DEBUG_TAG, "setAccountInfo( " + plan + " " + product + " " + offPeakStart + " "
				+ offPeakEnd + " " + peakQuota + " " + offpeakQuota);
		**/
		
		SharedPreferences.Editor myEditor = mySettings.edit();
		myEditor.putString(PLAN, plan);
		myEditor.putString(PRODUCT, product);
		myEditor.putString(OFF_PEAK_START, offPeakStart);
		myEditor.putString(OFF_PEAK_END, offPeakEnd);
		myEditor.putLong(PEAK_QUOTA, peakQuota);
		myEditor.putLong(OFF_PEAK_QUOTA, offpeakQuota);
		myEditor.commit();
		
		Log.i(INFO_TAG, "Account info set");
	}
	
	public String getPlan(){
		return mySettings.getString(PLAN, "Plan not set");
	}
	
	public String getProduct(){
		return mySettings.getString(PRODUCT, "Product not set");
	}
	
	public String getOffPeakStart(){
		return mySettings.getString(OFF_PEAK_START, "Off peak start not set");
	}
	
	public String getOffPeakEnd(){
		return mySettings.getString(OFF_PEAK_END, "Off peak end not set");
	}
	
	public Long getPeakQuota(){
		return mySettings.getLong(PEAK_QUOTA, 0);
	}
	
	public Long getOffPeakQuota(){
		return mySettings.getLong(OFF_PEAK_QUOTA, 0);
	}

}
