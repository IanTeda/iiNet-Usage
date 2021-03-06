package au.id.teda.volumeusage.prefs;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.R.xml;
import au.id.teda.volumeusage.activity.MainActivity;
import au.id.teda.volumeusage.service.DataCollectionService;
import au.id.teda.volumeusage.service.ServiceHelper;
import au.id.teda.volumeusage.view.SetStatusBar;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = Preferences.class.getSimpleName();
	
	private static final String PEAK_KEY = "peak_usage_alert";
	private static final String OFFPEAK_KEY = "offpeak_usage_alert";
	private static final String UPDATE_INTERVAL = "updateInterval";
	private static final String BACKGROUD_UPDATES = "background_updates";
	private static final String HIDE_STATUS_BAR = "hide_status_bar";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(INFO_TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		
		// Register for changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	
        // Set status bar hide or not
    	SetStatusBar setStatusBar = new SetStatusBar(this);
    	setStatusBar.showHide();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.i(INFO_TAG, "onSharedPreferenceChanged() > Key: " + key);
		if (key.equals(UPDATE_INTERVAL) || key.equals(BACKGROUD_UPDATES)) {
			ServiceHelper serviceHelper = new ServiceHelper(this);
			serviceHelper.restartDataCollectionService();
			
		// Set status bar visibility on preference change
		} else if (key.equals(HIDE_STATUS_BAR)){

	        // Set status bar hide or not
	    	SetStatusBar setStatusBar = new SetStatusBar(this);
	    	setStatusBar.showHide();
		}
		
	}
	
    @Override
    protected void onDestroy() {
    	Log.i(INFO_TAG, "onDestroy()");
    	// Unregister from changes
    	getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    	super.onDestroy();
    }
	
}
