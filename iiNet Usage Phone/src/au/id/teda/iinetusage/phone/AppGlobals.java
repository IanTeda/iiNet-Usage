package au.id.teda.iinetusage.phone;

import android.app.Application;
import android.content.Context;
 
public class AppGlobals extends Application {
	
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = AppGlobals.class.getSimpleName();
     
	// Set context object
	private static Context myContext;
     
	public void onCreate(){
	    super.onCreate();

		// Get application context for globals
		myContext = getApplicationContext();
	}
           
	public static Context getAppContext() {
		return myContext;
	}
	
}
