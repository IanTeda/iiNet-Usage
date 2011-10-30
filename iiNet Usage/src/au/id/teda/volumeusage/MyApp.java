package au.id.teda.volumeusage;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import au.id.teda.volumeusage.activity.MainActivity;
import au.id.teda.volumeusage.service.DataCollectionService;
import au.id.teda.volumeusage.service.ServiceHelper;

public class MyApp extends Application {
	
	// TODO: Prehaps rename to globals
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = MyApp.class.getSimpleName();
     
	private static Context context; // Instance context
	
	private static boolean changedPref = false; // Instance context
     
	public void onCreate(){
		Log.i(INFO_TAG, "MyApp > onCreate()");
              
		// Get application context for globals
		MyApp.context=getApplicationContext();
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
