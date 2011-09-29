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
import au.id.teda.volumeusage.activity.MainActivity;
import au.id.teda.volumeusage.service.DataCollectionService;
import au.id.teda.volumeusage.service.ServiceHelper;

public class MyApp extends Application {
	
	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = MyApp.class.getSimpleName();
     
	private static Context context; // Instance context
     
	public void onCreate(){
		Log.i(INFO_TAG, "MyApp > onCreate()");
              
		// Get application context for globals
		MyApp.context=getApplicationContext();
		
		ServiceHelper serviceHelper = new ServiceHelper(this);
		serviceHelper.startDataCollectionService();
	}
     
	@Override
	public void onTerminate() {
		Log.i(INFO_TAG, "onTerminate()");
		super.onTerminate();
	}
      
	public static Context getAppContext() {
		return context;
	}
     
}