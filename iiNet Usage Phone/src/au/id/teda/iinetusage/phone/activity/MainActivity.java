package au.id.teda.iinetusage.phone.activity;

import java.text.ParseException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.async.RefreshUsageAsync;
import au.id.teda.iinetusage.phone.view.AccountInfoView;
import au.id.teda.iinetusage.phone.view.AlertboxView;
import au.id.teda.iinetusage.phone.view.OffpeakStatsView;
import au.id.teda.iinetusage.phone.view.PeakStatsViewLand;
import au.id.teda.iinetusage.phone.view.PeakStatsViewPort;

public class MainActivity extends ActionbarHelperActivity {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = MainActivity.class.getSimpleName();
	
	private Button myAlertboxButton;
	private AlertboxView myAlertboxView;
	
	// Object for AccountInfoView
	private AccountInfoView myAccountInfoView;
	
	// Object for PeakStatsView
	private PeakStatsViewPort myPeakStatsViewPort;
	private PeakStatsViewLand myPeakStatsViewLand;
	
	// Object for OffpeakStatsView
	private OffpeakStatsView myOffpeakStatsView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        
		// If view is in portrait set reference to objects
		if (isPortrait()) {
			myAlertboxButton = (Button) findViewById(R.id.alertbox_button);
			myAlertboxView = new AlertboxView(this);

			// Initialise views
			myAccountInfoView = new AccountInfoView(this);
			myPeakStatsViewPort = new PeakStatsViewPort(this);
			myOffpeakStatsView = new OffpeakStatsView(this);
		}
		// Else we must be in landscape
		else {
			// Initialise views
			myPeakStatsViewLand = new PeakStatsViewLand(this);
			
		}
        
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadViews();
		
		// We only have an action bar in portrait
		if (isPortrait()){
			setActionbarTitle(getString(R.string.actionbar_title_dashboard));
		}
		

	}
	
	public void loadViews(){
		// If view is in portrait load portrait views
		if (isPortrait()){
			// Load portrait views
	        myAlertboxView.setAlert(myAlertboxButton);
	        myAccountInfoView.loadView();
	        myPeakStatsViewPort.loadView();
	        myOffpeakStatsView.loadView();
		}
		// Else we are in landscape
		else {
			myPeakStatsViewLand.loadView();
		}

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
	
	/**
	 * Method for calling refresh async task
	 * @param view
	 */
	public void onClickActionbarRefresh (View view){
		new RefreshUsageAsync(this, handler).execute();
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	
        	loadViews();
        }
    };
    
    /**
     * onClick event for peak stats include
     * @param button
     */
    public void onPeakStatsClick (View button){
    	
    	// Switch cases for button clicks
    	switch (button.getId()) {
    	case R.id.peak_number_button:
    		myPeakStatsViewPort.switchUnitNumberBlock();
    		break;
    	case R.id.peak_percent_used_remaining_button:
    		myPeakStatsViewPort.switchFocusPercentBlock();
    		break;
    	case R.id.peak_average_data_used_remaining_button:
    		myPeakStatsViewPort.switchFocusDailyBlock();
    		break;
    	case R.id.peak_data_used_remaining_button:
    		myPeakStatsViewPort.switchFocusDataBlock();
    		break;
    	case R.id.peak_data_title_button:
    		myPeakStatsViewPort.switchPeakView();
    		break;
    	case R.id.offpeak_number_button:
    		myOffpeakStatsView.switchUnitNumberBlock();
    		break;
    	case R.id.offpeak_percent_used_remaining_button:
    		myOffpeakStatsView.switchFocusPercentBlock();
    		break;
    	case R.id.offpeak_average_data_used_remaining_button:
    		myOffpeakStatsView.switchFocusDailyBlock();
    		break;
    	case R.id.offpeak_data_used_remaining_button:
    		myOffpeakStatsView.switchFocusDataBlock();
    		break;
    	case R.id.offpeak_data_title_button:
    		myOffpeakStatsView.switchOffpeakView();
    		break;
    	default:
    		Toast.makeText(this, "onPeakStatsClick", Toast.LENGTH_SHORT).show();
    		break;
    	}
    	
    }
    
	public void onPeakLandStatsClick (View button){
		// Object for PeakStatsView class
    	PeakStatsViewLand myPeakStatsViewLand = new PeakStatsViewLand(this);
    	
    	// Switch cases for button clicks
    	switch (button.getId()) {
    	case R.id.dashboard_landscape_peak_data_button:
    		myPeakStatsViewLand.switchPeakUnit();
    		break;
    	case R.id.dashboard_landscape_peak_unit_button:
    		myPeakStatsViewLand.switchPeakUnit();
    		break;
    	case R.id.dashboard_landscape_peak_title_button:
    		myPeakStatsViewLand.switchPeakData();
    		break;
    	default:
    		Toast.makeText(this, "onPeakLandStatsClick Error", Toast.LENGTH_SHORT).show();
    		break;
    	}
	}
    
    /**
     * Onclick event for account info include
     * @param button
     * @throws ParseException 
     */
    public void onAccountInfoClick (View button) throws ParseException{
    	
    	// Switch cases for button clicks
    	switch (button.getId()) {
    	case R.id.account_info_expand:
    		myAccountInfoView.resizeAccountInfo();
    		break;
    	case R.id.account_info_rollover_period_button:
    		myAccountInfoView.switchFocusRolloverPeriodBlock();
    		break;
    	case R.id.account_info_days_button:
    		myAccountInfoView.switchFocusDaysBlock();
    		break;
    	case R.id.account_info_ip_up_button:
    		myAccountInfoView.switchFocusIpUpBlock();
    		break;
    	case R.id.account_info_quota_button:
    		myAccountInfoView.switchFocusQuotaBlock();
    		break;
    	default:
    		Toast.makeText(this, "onAccountInfoClick Error", Toast.LENGTH_SHORT).show();
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
		
		// Menu inflater object
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
			new RefreshUsageAsync(this, handler).execute();
			return true;
		case R.id.menu_about_button:

			return true;
		}
		//Log.d(DEBUG_TAG, "MainActivity > onOptionsItemSelected() > Default switch");
		return false;
	}
}