package au.id.teda.volumeusage.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.async.RefreshUsageAsync;
import au.id.teda.volumeusage.database.DailyDataDBAdapter;
import au.id.teda.volumeusage.helper.AccountHelper;
import au.id.teda.volumeusage.prefs.Preferences;
import au.id.teda.volumeusage.service.ServiceHelper;
import au.id.teda.volumeusage.view.DailyDataCursorAdapter;
import au.id.teda.volumeusage.view.SetStatusBar;

/**
 *  UsageDataActivity.java
 *  Purpose: This activity displays a list of daily usage statistics
 * 
 *  @author Ian Teda
 *  @version Alpha 1.2
 *  
 */
public class UsageDataActivity extends ListActivity implements OnClickListener {
	
	// TODO: Add onclick for day breakdown
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = UsageDataActivity.class.getSimpleName();
	
	private RefreshUsageAsync refreshUsageAsync;
	private DailyDataDBAdapter dailyDataDB;
	private Object setListAdapter;
	private DailyDataCursorAdapter dailyDataCursorAdapter;
	private Cursor dailyDBCursor;
	
	/**
	 *  Activity onCreate method.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //reload any saved activity instance state
		Log.i(INFO_TAG, "onCreate()");
		setContentView(R.layout.usage_data);
		
		// Setup action bar title and buttons
		setUpActionBar();
	}
	
    /**
     *  Activity onResume method
     */
	@Override
	protected void onResume(){
		super.onResume();
		// Create database object and open adapter
		Log.i(INFO_TAG, "onResume()");
		fillData();
		//TODO: Add close database call for activity
		
        // Set status bar hide or not
    	SetStatusBar setStatusBar = new SetStatusBar(this);
    	setStatusBar.showHide();
	}

	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	Log.i(INFO_TAG, "handleMessage( " + msg + " )");
           //TODO: how do i use this??
        	fillData();
        }
    };
	
	/**
	 *  Method used to set action bar title, reference buttons and set onClick listener
	 */
	public void setUpActionBar(){
		Log.i(INFO_TAG, "setUpActionBar()");
		// Set action bar title
		TextView abTitle = (TextView) findViewById(R.id.action_bar_title_text);
		abTitle.setText(R.string.ab_usage_data_view_title);
		
		// Reference action bar buttons and set onClick
        //ImageButton abRefreshButton = (ImageButton) findViewById(R.id.action_bar_refresh_button); // This is the refresh button on the action bar
       // abRefreshButton.setOnClickListener(this);
       // ImageButton abHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button); // Take me back to the dashboard
        //abHomeButton.setOnClickListener(this);
	}
	
	/**
	 * 
	 */
	private void fillData() {
		// Create cursor object, fetch all data from DB into cursor
		Log.i(INFO_TAG, "fillData()");
		
		AccountHelper accountHelper = new AccountHelper(this);
		String dataPeriod = accountHelper.getCurrentPeriod();
		
		dailyDataDB = new DailyDataDBAdapter(this);
		dailyDataDB.open();
		dailyDBCursor = dailyDataDB.fetchPeriodUsage(dataPeriod);
		startManagingCursor(dailyDBCursor);
		setListAdapter(new DailyDataCursorAdapter(this, dailyDBCursor));
		dailyDataDB.close();
	}

    /**
     * Method for handling onClicks in this activity
     * @param: button
     */
	@Override
	public void onClick(View button) {
		Log.i(INFO_TAG, "onClick() > Button: " + button.getId());
	}

	/**
	 * onClick method for action bar
	 * @param button
	 */
	public void onActionBarClick(View button){
		Log.i(INFO_TAG, "onClick() > Button: " + button.getId());
		switch (button.getId()) {
		case R.id.action_bar_home_button:
			Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
            startActivity(dashboardActivityIntent);
			break;
		case R.id.action_bar_refresh_button:
			new RefreshUsageAsync(this, handler).execute();
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
			new RefreshUsageAsync(this, handler).execute();
			return true;
		case R.id.menu_about_button:
			Intent aboutIntent2 = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent2);
			return true;
		}
		return false;
	}
	
}
