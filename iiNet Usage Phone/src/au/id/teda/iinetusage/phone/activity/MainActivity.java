package au.id.teda.iinetusage.phone.activity;

import android.os.Bundle;
import android.util.Log;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.UserPassHelper;

public class MainActivity extends ActionbarActivity {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = MainActivity.class.getSimpleName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        
        userPassCheck();
    }

	private void userPassCheck() {
		UserPassHelper UserPassHelper = new UserPassHelper();
		if (!UserPassHelper.isUsernamePasswordSet()){
			Log.d(DEBUG_TAG, "User password not set, open settings");
		}
		
	}
}