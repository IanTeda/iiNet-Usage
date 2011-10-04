package au.id.teda.volumeusage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.helper.AccountHelper;
import au.id.teda.volumeusage.prefs.Preferences;
import au.id.teda.volumeusage.service.RefreshUsageData;
import au.id.teda.volumeusage.service.ServiceHelper;
import au.id.teda.volumeusage.view.SetStatusBar;

/**
 *  SummaryActivity.java
 *  Purpose: This activity displays the summary information of the account
 *  and the account status
 * 
 *  @author Ian Teda
 *  @version Alpha
 *  
 */

public class SummaryActivity extends Activity implements OnClickListener {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = SummaryActivity.class.getSimpleName();
	
	/**
	 *  Activity onCreate method.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(INFO_TAG, "onCreate()");
		super.onCreate(savedInstanceState); //reload any saved activity instance state
		setContentView(R.layout.summary);
		
		// Setup action bar title and buttons
		setUpActionBar();
	}
	
    /**
     *  Activity onResume method
     */
	@Override
	public void onResume(){
		super.onResume();
		Log.i(INFO_TAG, "onResume()");
		
		fillSummaryView();
		
        // Set status bar hide or not
    	SetStatusBar setStatusBar = new SetStatusBar(this);
    	setStatusBar.showHide();
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	fillSummaryView();
        }
    };
	
	/**
	 *  Method used to set action bar title, reference buttons and set onClick listener
	 */
	public void setUpActionBar(){
		// Set action bar title
		TextView abTitle = (TextView) findViewById(R.id.action_bar_title_text);
		abTitle.setText(R.string.ab_summary_view_title);
		
		// Reference action bar buttons and set onClick
        ImageButton abRefreshButton = (ImageButton) findViewById(R.id.action_bar_refresh_button); // This is the refresh button on the action bar
        abRefreshButton.setOnClickListener(this);
        ImageButton abHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button); // Take me back to the dashboard
        abHomeButton.setOnClickListener(this);
	}
	
	/**
	 * Method used to fill data/text in summary view
	 */
	public void fillSummaryView(){
		Log.i(INFO_TAG, "fillSummaryView()");
		AccountHelper accountInfo = new AccountHelper(this);
		
		// Set plan / product info
		TextView planProductTV = (TextView) findViewById(R.id.sum_account_plan_tv);
		planProductTV.setText(Html.fromHtml(accountInfo.getPlanProduct()));
		
		// Set plan offpeak start and end
		TextView planOffpeakTimeTV = (TextView) findViewById(R.id.sum_account_period_tv);
		planOffpeakTimeTV.setText(Html.fromHtml(accountInfo.getPeakPeriod()));
		
		// Set quota for plan
		TextView planQuotaTV = (TextView) findViewById(R.id.sum_account_quota_tv);
		planQuotaTV.setText(Html.fromHtml(accountInfo.getQuotaString()));
		
		// Set IP address
		TextView ipTV = (TextView) findViewById(R.id.sum_account_ip_tv);
		ipTV.setText(Html.fromHtml(accountInfo.getIPAddress()));
		
		// Set update
		TextView upTimeTV = (TextView) findViewById(R.id.sum_account_since_tv);
		upTimeTV.setText(Html.fromHtml(accountInfo.getUpTime()));
		
		// Set anniversary
		TextView upRollOverTV = (TextView) findViewById(R.id.sum_current_rollover_tv);
		upRollOverTV.setText(Html.fromHtml(accountInfo.getRollOver()));
		
		// Set days to roll over
		TextView daysToGoTV = (TextView) findViewById(R.id.sum_togo_tv);
		daysToGoTV.setText(Html.fromHtml(accountInfo.getDaysToGoString()));
		
		// Set days since last roll over
		TextView daysSoFareTV = (TextView) findViewById(R.id.sum_sofare_tv);
		daysSoFareTV.setText(Html.fromHtml(accountInfo.getDaysSoFareString()));
		
		// Set data used peak
		TextView peakDataTV = (TextView) findViewById(R.id.sum_usage_peak_tv);
		peakDataTV.setText(Html.fromHtml(accountInfo.getDataUsedPeak()));
		
		// Set data used peak
		TextView offpeakDataTV = (TextView) findViewById(R.id.sum_usage_offpeak_tv);
		offpeakDataTV.setText(Html.fromHtml(accountInfo.getDataUsedOffpeak()));
		
		// Set data trend peak
		TextView peakTrendTV = (TextView) findViewById(R.id.sum_trend_peak_tv);
		peakTrendTV.setText(Html.fromHtml(accountInfo.getUsageTrendString("peak")));
		
		// Set data trend peak
		TextView offpeakTrendTV = (TextView) findViewById(R.id.sum_trend_offpeak_tv);
		offpeakTrendTV.setText(Html.fromHtml(accountInfo.getUsageTrendString("offpeak")));
		
		// Set suggested data use for peak
		TextView peakSuggestTV = (TextView) findViewById(R.id.sum_suggested_peak_tv);
		peakSuggestTV.setText(Html.fromHtml(accountInfo.getSuggestedUseString("peak")));
		
		// Set suggested data use for offpeak
		TextView offpeakSuggestTV = (TextView) findViewById(R.id.sum_suggested_offpeak_tv);
		offpeakSuggestTV.setText(Html.fromHtml(accountInfo.getSuggestedUseString("offpeak")));
		
	}
	
    /**
     * Method for handling onClicks in this activity
     * @param: button
     */
	@Override
	public void onClick(View button) {
		Log.i(INFO_TAG, "onClick() > Button: " + button.getId());
		switch (button.getId()) {
		case R.id.action_bar_home_button:
			Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
            startActivity(dashboardActivityIntent);
			break;
		case R.id.action_bar_refresh_button:
			new RefreshUsageData(this, handler).execute();
			break;
		default:
			Log.i(INFO_TAG, "onClick() > Default switch");
		}
	}
	
	/**
	 * onCreate method for options menu
	 * What happens when we press the menu button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(INFO_TAG, "onCreateOptionsMenu()");
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater(); // New menu object
		inflater.inflate(R.menu.menu, menu); // Lets list the menu.xml
		return true;
	}
	
	/**
	 * onClick method for menu buttons
	 * What happens when some clicks a menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(INFO_TAG, "onOptionsItemSelected() > Button: " + item.getTitle());
		switch (item.getItemId()) {
		case R.id.menu_settings_button:
			Intent menuIntent = new Intent(this, Preferences.class);
			startActivity(menuIntent);
			return true;
		case R.id.menu_refresh_button:
			ServiceHelper serviceHelper = new ServiceHelper(this);
			new RefreshUsageData(this, handler).execute();
			return true;
		case R.id.menu_about_button:
			Intent aboutIntent2 = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent2);
			return true;
		}
		//Log.d("iiNet Usage", "MainActivity > Menu onClick default case");
		return false;
	}

}
