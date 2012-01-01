package au.id.teda.iinetusage.phone;

import android.app.Application;
import android.content.Context;
import android.util.Log;
 
public class AppGlobals extends Application {
	
	// TODO: Prehaps rename to globals
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AppGlobals.class.getSimpleName();
     
	private static Context context; // Instance context
	
	private static boolean changedPref = false; // Instance context
     
	public void onCreate(){
		Log.i(INFO_TAG, "MyApp > onCreate()");
              
		// Get application context for globals
		AppGlobals.context = getApplicationContext();
	}
     
	@Override
	public void onTerminate() {
		Log.i(INFO_TAG, "onTerminate()");
		super.onTerminate();
	}
      
	public static Context getAppContext() {
		return context;
	}
     
	public static boolean checkChangePref(){
		return changedPref;
	}
	
	/**
	 * Credential status
	 */
	private boolean isChecked;
	
	public boolean isChecked(){
	    return isChecked;
	}
	
	public void setIsChecked(boolean b){
		isChecked = b;
	}
	
}
