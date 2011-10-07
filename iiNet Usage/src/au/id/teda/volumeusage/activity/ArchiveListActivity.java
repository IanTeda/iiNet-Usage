package au.id.teda.volumeusage.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.prefs.Preferences;
import au.id.teda.volumeusage.service.RefreshUsageData;
import au.id.teda.volumeusage.service.ServiceHelper;
import au.id.teda.volumeusage.view.SetStatusBar;

/**
 * 
 * @author Ian
 *
 */
// http://pareshnmayani.wordpress.com/tag/android-listview-example/
// http://www.vogella.de/articles/AndroidListView/article.html

public class ArchiveListActivity extends ListActivity {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = ArchiveListActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(INFO_TAG, "onCreate()");
		setContentView(R.layout.archive);
		
		// Setup action bar title and buttons
		//setupActionBar();
		
		ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
		
		}
	
    /**
     * Creates and returns a list adapter for the current list activity
     * @return
     */
    protected ListAdapter createAdapter() {
    	// Create some mock data
    	String[] testValues = new String[] {
    			"Test1",
    			"Test2",
    			"Test3"
    	};
 
    	// Create a simple array adapter (of type string) with the test values
    	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
 
    	return adapter;
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
				.show();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(INFO_TAG, "onRestart()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(INFO_TAG, "onResume()");
		
        // Set status bar hide or not
    	SetStatusBar setStatusBar = new SetStatusBar(this);
    	setStatusBar.showHide();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(INFO_TAG, "onDestroy()");
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	Log.i(INFO_TAG, "handleMessage( " + msg + " )");
           //TODO: how do i use this??
        }
    };

    /**
     * Method for handling onClicks in this activity
     * @param: button
     */
    /**
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
	}**/
	
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
		return false;
	}
	
	/**
	 *  Method used to set action bar title, reference buttons and set onClick listener
	 */
	public void setupActionBar(){
		Log.i(INFO_TAG, "setUpActionBar()");
		// Set action bar title
		TextView abTitle = (TextView) findViewById(R.id.action_bar_title_text);
		abTitle.setText(R.string.ab_history_title);
		
		// Reference action bar buttons and set onClick
		// This is the refresh button on the action bar
        ImageButton abRefreshButton = (ImageButton) findViewById(R.id.action_bar_refresh_button);
        //abRefreshButton.setOnClickListener(this);
        
        // Take me back to the dashboard
        ImageButton abHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button);
        //abHomeButton.setOnClickListener(this);
	}
	

}
