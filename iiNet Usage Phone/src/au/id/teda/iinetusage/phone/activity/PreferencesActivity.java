package au.id.teda.iinetusage.phone.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;


public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	//private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = PreferencesActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		setPhoneStatusBar();
		
		// Register for changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

	}
	
    @Override
    protected void onDestroy() {
    	
    	getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    	super.onDestroy();
    }
    
	/**
	 * Show hide phone status based on preference setting
	 */
	public void setPhoneStatusBar() {
		PreferenceHelper mySetttings = new PreferenceHelper();

		if (mySetttings.hidePhoneStatusBar()) {
			((Activity) this).getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			((Activity) this).getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}
	
}
