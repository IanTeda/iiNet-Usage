package au.id.teda.iinetusage.phone.activity;

import java.text.ParseException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.async.RefreshUsageAsync;
import au.id.teda.iinetusage.phone.cursor.DailyDataCursorAdapter;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class DailyDataActivity extends ListActivity {

	//Static strings for debugging 
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = DailyDataActivity.class.getSimpleName();
	
	// Set list objects
	private DailyDataDBAdapter myDailyDataDB;
	private Cursor myDailyDBCursor;
	private AccountHelper myAccount;
	
	// Set TextView Objects
	private TextView myTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_data);
        
        // Set Database adapter
        myDailyDataDB = new DailyDataDBAdapter();
        
        // Set AccountHelper
        myAccount = new AccountHelper();
        
        // Set reference to TextViews
        myTitle = (TextView) findViewById(R.id.daily_data_title);
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
        	
        	try {
				loadData();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    };
	
	
	/**
	 * Set the actionbar title with actionbarTitle string
	 * @param actionbarTitle
	 */
	public void setActionbarTitle(String actionbarTitle) {
		// Set TextView Reference
		TextView myActionbarTitle = (TextView) findViewById(R.id.actionbar_title);
		
		// Set TextView text
		myActionbarTitle.setText(actionbarTitle);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			loadData();
			//String actionbarTitle = getString(R.string.actionbar_title_daily_data);
			//setActionbarTitle(actionbarTitle);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private void loadData() throws ParseException{
		
		// Get string value of current data period
		String dataPeriod = myAccount.getCurrentDataPeriod();
		
		// Open database (i.e. set tables if needed)
		myDailyDataDB.open();
		
		// Set cursor object
		myDailyDBCursor = myDailyDataDB.fetchPeriodUsage(dataPeriod);
		
		// Start managing cursor TODO: What does this mean?
		startManagingCursor(myDailyDBCursor);
		
		// Load data into cursor array
		setListAdapter(new DailyDataCursorAdapter(this, myDailyDBCursor));
		
		// Close database
		myDailyDataDB.close();
		
		// Set title text
		myTitle.setText(myAccount.getCurrentDataPeriodString());
		
		setPhoneStatusBar();
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
	
	/**
	 * Action bar menu onClick method
	 * @param button
	 */
	public void onActionbarMenuClick(View button) {
		switch (button.getId()) {
		case R.id.actionbar_menu_dash:
			Intent dashIntent = new Intent(this, MainActivity.class);
			startActivity(dashIntent);
			break;
		case R.id.actionbar_menu_graph:
			Intent graphsIntent = new Intent(this, ChartActivity.class);
			startActivity(graphsIntent);
			break;
		case R.id.actionbar_menu_data:
			Intent dataIntent = new Intent(this, DailyDataActivity.class);
			startActivity(dataIntent);
			break;
		case R.id.actoionbar_menu_archive:
			Intent archiveIntent = new Intent(this, ArchiveActivity.class);
			startActivity(archiveIntent);
			break;
		default:
			Toast.makeText(this, "Button not recognised", Toast.LENGTH_SHORT)
					.show();
		}
	}
	

}
