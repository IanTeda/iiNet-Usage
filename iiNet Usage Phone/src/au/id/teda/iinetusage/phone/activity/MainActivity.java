package au.id.teda.iinetusage.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.async.RefreshUsageAsync;
import au.id.teda.iinetusage.phone.helper.UserPassHelper;
import au.id.teda.iinetusage.phone.view.AccountInfoView;
import au.id.teda.iinetusage.phone.view.AlertboxView;

public class MainActivity extends ActionbarHelperActivity {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = MainActivity.class.getSimpleName();
	
	private Button myAlertboxButton;
	private AlertboxView myAlertboxView;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        
		myAlertboxButton = (Button) findViewById(R.id.alertbox_button);
		myAlertboxView = new AlertboxView(this);
        myAlertboxView.setAlert(myAlertboxButton);
        
    }
	
	/**
	 * Method for managing onClick events of the Alertbox based on current alert text
	 * @param view
	 */
	public void onClickAlertboxButton (View view){
		// Get current string value of the alert box
		String alertboxButtonText =  (String) myAlertboxButton.getText();
		
		// Set action based on value of alertbox string
    	if ( alertboxButtonText == this.getString(R.string.alertbox_no_data)){
    		Toast.makeText(this, "Refersh data", Toast.LENGTH_SHORT).show();
    		
    	} else if (alertboxButtonText == this.getString(R.string.alertbox_no_userpass)
    			|| alertboxButtonText == this.getString(R.string.alertbox_no_user)
    			|| alertboxButtonText == this.getString(R.string.alertbox_no_pass)){
    		Intent myUserPassIntent = new Intent(MainActivity.this, UserPassActivity.class);
			startActivity(myUserPassIntent);
    	}
		
	}
	
	public void onClickActionbarRefresh (View view){
		new RefreshUsageAsync(this, handler).execute();
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	//fillSummaryView();
        }
    };
    
    public void onPeakStatsClick (View button){
    	Toast.makeText(this, "onPeakStatsClick " + button, Toast.LENGTH_SHORT).show();
    	
    }
    
    public void onAccountInfoClick (View button){
    	
    	AccountInfoView myAccountInfoView = new AccountInfoView(this);
    	
    	switch (button.getId()) {
    	case R.id.account_info_expand:
    		myAccountInfoView.resizeAccountInfo();
    		break;
    	case R.id.account_info_rollover_period_button:
    		Toast.makeText(this, "Rollover", Toast.LENGTH_SHORT).show();
    		break;
    	case R.id.account_info_days_button:
    		Toast.makeText(this, "Days", Toast.LENGTH_SHORT).show();
    		break;
    	case R.id.account_info_ip_up_button:
    		myAccountInfoView.switchFocusIpUpBlock();
    		break;
    	case R.id.account_info_quota_button:
    		Toast.makeText(this, "Quota", Toast.LENGTH_SHORT).show();
    		break;
    	default:
    		Toast.makeText(this, "onAccountInfoClick Error ", Toast.LENGTH_SHORT).show();
    		break;
    	}
			
    }
	
	/**
	 * onCreate method for options menu
	 * What happens when we press the menu button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		// Menu inflator object
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/**
	 * onClick method for menu buttons
	 * What happens when some clicks a menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings_button:
			Intent menuIntent = new Intent(this, PreferencesActivity.class);
			startActivity(menuIntent);
			return true;
		case R.id.menu_refresh_button:
			
			return true;
		case R.id.menu_about_button:

			return true;
		}
		//Log.d(DEBUG_TAG, "MainActivity > onOptionsItemSelected() > Default switch");
		return false;
	}
}