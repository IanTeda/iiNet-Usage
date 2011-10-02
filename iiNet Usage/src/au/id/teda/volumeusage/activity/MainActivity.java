package au.id.teda.volumeusage.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.database.DataBaseHelper;
import au.id.teda.volumeusage.prefs.Preferences;
import au.id.teda.volumeusage.service.RefreshUsageData;
import au.id.teda.volumeusage.service.ServiceHelper;
import au.id.teda.volumeusage.view.DashBoardAlertView;
import au.id.teda.volumeusage.view.DashboardUsageStats;

/**
 *  MainActivity.java
 *  Purpose: This is the activity loaded during startup of the application. It  
 *  displays dashboard.xml
 * 
 *  @author Ian Teda
 *  @version Alpha 1.1
 *  
 */

public class MainActivity extends Activity implements OnClickListener {
	
	//TODO: Menu shortcut key in strings???
	//TODO: Add comments and description to activitys & methods
	//TODO: Edit menu/menu icons to make it look better
	//TODO: Is there a better way to hide the alert box so layout fills better
	//TODO: Add code to save application state
	//TODO: Add pressed and focus state button images to res folder and update drawable xml files
	//TODO: Add landscape layout to action bar to include text for buttons
	//TODO: Add refresh animation to action bar > look at action bar libraries that include honeycomb
	//TODO: Hyperlink to website on about dialog
	
	/**
	 *  Static tag strings for loging information and debug
	 */
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = MainActivity.class.getSimpleName();
	
	/**
	 *  Activity onCreate method.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(INFO_TAG, "onCreate()");
		super.onCreate(savedInstanceState); //reload any saved activity instance state
        setContentView(R.layout.dashboard);
        
        // Load database. Create if it doesn't exist
        checkDatabaseCreate();
        
        // Reference view buttons and set onClick listener
        loadButtons();
        
        // Start background service for auto updates
        startAutoUpdates();
        
	}

	/**
	 *  Handler for inter class communication
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	loadView();
        }
    };
	
    /**
     *  Activity onResume method
     */
    @Override
    public void onResume(){
    	Log.i(INFO_TAG, "onResume()");
    	super.onResume();
    	
    	// Load activity view
    	loadView();
    }
    
    /**
     * Fix orientation of dashboard to portrait
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    /**
     *  Method for referencing buttons and setting onClick listeners
     */
    public void loadButtons(){
        Log.i(INFO_TAG, "onCreate() > Setup buttons");
        Button dbSummaryButton = (Button) findViewById(R.id.db_summary_activity_button);
        dbSummaryButton.setOnClickListener(this);
        Button dbGraphButton = (Button) findViewById(R.id.db_usage_graph_activity_button);
        dbGraphButton.setOnClickListener(this);
        Button dbDataButton = (Button) findViewById(R.id.db_usage_data_activity_button);
        dbDataButton.setOnClickListener(this);
        Button dbAboutButton = (Button) findViewById(R.id.db_about_button);
        dbAboutButton.setOnClickListener(this);
        ImageButton dbActionBarSettingsButton = (ImageButton) findViewById(R.id.db_action_bar_settings_button);
        dbActionBarSettingsButton.setOnClickListener(this);
    }
    
    /**
     *  Method for loading alert box and usage stats in view
     */
    public void loadView(){
    	Log.i(INFO_TAG, "loadView()");
    	// Reference alert layout and button
    	LinearLayout alertBoxLayout = (LinearLayout)findViewById(R.id.DB_AlertBox);
    	Button alertBoxButton = (Button) findViewById(R.id.DB_AlertBTN);
    	
    	// Load and set alert button view
    	DashBoardAlertView dashBoardAlertView = new DashBoardAlertView(this);
    	dashBoardAlertView.setAlert(alertBoxLayout, alertBoxButton);
    	
    	// Reference usage text views
    	TextView peakTextView = (TextView) findViewById(R.id.db_peak_tv);
    	TextView offpeakTextView = (TextView) findViewById(R.id.db_offpeak_tv);
    	
    	// Load and set usage text views
    	DashboardUsageStats dashboardUsageStats = new DashboardUsageStats(this);
    	dashboardUsageStats.setUsageTextViews(peakTextView, offpeakTextView);
    	dashboardUsageStats.setUsageLayoutAlert(peakTextView, offpeakTextView);
    }
    
    /**
     *  Method for handling onclick events with the alert button/view
     * 	@param view > This is required as part of xml onclick variable. Doesn't do anything here
     */
    public void onClickAlertBTN(View view){
    	Button alertBoxButton = (Button) findViewById(R.id.DB_AlertBTN);
    	Log.i(INFO_TAG, "onClickAlertBTN() > Button: " + alertBoxButton.getText());
    	if (alertBoxButton.getText() == this.getString(R.string.db_alertbox_nodata)){
    		new RefreshUsageData(this, handler).execute();
    	} else if (alertBoxButton.getText() == this.getString(R.string.db_alertbox_userpass)
    			|| alertBoxButton.getText() == this.getString(R.string.db_alertbox_user)
    			|| alertBoxButton.getText() == this.getString(R.string.db_alertbox_pass)){
    		Intent menuIntent = new Intent(this, Preferences.class);
    		startActivity(menuIntent);
    	}
    }
    
    /**
     * Method for handling onClicks in this activity
     * @param: button
     */
	@Override
 	public void onClick(View button) {
		Log.i(INFO_TAG, "onClick() > Button: " + button.getId());
		/** If exit button clicked exit, else (default) new intent with button id **/
		switch (button.getId()) {
		case R.id.db_action_bar_settings_button:
			Intent settingsIntent = new Intent(MainActivity.this, Preferences.class);
			startActivity(settingsIntent);
			break;
		case R.id.db_about_button:
			Intent aboutIntent1 = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent1);
			break;
		case R.id.db_summary_activity_button:
			Intent summaryAcitivityIntent = new Intent(MainActivity.this, SummaryActivity.class);
			startActivity(summaryAcitivityIntent);
			break;
		case R.id.db_usage_graph_activity_button:
			Intent usageGraphAcitivityIntent = new Intent(MainActivity.this, UsageGraphActivity.class);
			startActivity(usageGraphAcitivityIntent);
			break;
		case R.id.db_usage_data_activity_button:
			Intent rawDataAcitivityIntent = new Intent(MainActivity.this, UsageDataActivity.class);
			startActivity(rawDataAcitivityIntent);
			break;
		default:
			//Log.d(DEBUG_TAG, "MainActivity > onClick() > Default");
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
			new RefreshUsageData(this, handler).execute();
			return true;
		case R.id.menu_about_button:
			Intent aboutIntent2 = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent2);
			return true;
		}
		//Log.d(DEBUG_TAG, "MainActivity > onOptionsItemSelected() > Default switch");
		return false;
	}
	
	/**
	 *  Checking method for database existance, if not create
	 *  
	 *  @param None
	 *  @return None
	 * 	
	 * 	Method loads database helper class and tries to estalish a connection.
	 * 	Database helper class will create database on connection if it doesen't exist
	 *  
	 */
	public void checkDatabaseCreate(){
        Log.i(INFO_TAG, "onCreate() > Check/Create database");
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        dbHelper.getWritableDatabase();
        dbHelper.close();
	}
	
	public void startAutoUpdates(){
		ServiceHelper serviceHelper = new ServiceHelper(this);
		serviceHelper.startDataCollectionService();
	}
	
}
