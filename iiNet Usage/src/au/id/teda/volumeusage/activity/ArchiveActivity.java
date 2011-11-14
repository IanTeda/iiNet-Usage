package au.id.teda.volumeusage.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.async.RefreshUsageAsync;
import au.id.teda.volumeusage.prefs.Preferences;
import au.id.teda.volumeusage.service.ServiceHelper;
import au.id.teda.volumeusage.view.SetStatusBar;

/**
 * 
 * @author Ian
 *
 */
// http://pareshnmayani.wordpress.com/tag/android-listview-example/
// http://www.vogella.de/articles/AndroidListView/article.html

// http://saigeethamn.blogspot.com/2010/04/custom-listview-android-developer.html
// http://ykyuen.wordpress.com/2010/01/03/android-simple-listview-using-simpleadapter/
// http://ykyuen.wordpress.com/2010/03/15/android-%E2%80%93-applying-alternate-row-color-in-listview-with-simpleadapter/

public class ArchiveActivity extends ListActivity {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = ArchiveActivity.class.getSimpleName();
	static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(INFO_TAG, "onCreate()");
		setContentView(R.layout.archive);

        // create the grid item mapping
        String[] from = new String[] {"rowid", "col_1", "col_2", "col_3"};
        int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4 };

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < 10; i++){
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("rowid", "" + i);
        	map.put("col_1", "col_1_item_" + i);
        	map.put("col_2", "col_2_item_" + i);
        	map.put("col_3", "col_3_item_" + i);
        	fillMaps.add(map);
        }

        // fill in the grid_item layout
        SimpleAdapter myAdapter = new SimpleAdapter(this,
        		fillMaps,
        		R.drawable.archive_listitem_row,
        		from,
        		to);

        populateList();

        setListAdapter(myAdapter);
        
		// Setup action bar title and buttons
		//setupActionBar();
		
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
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		// Get the item that was clicked
		Object listItem = this.getListAdapter().getItem(position);
		String keyword = listItem.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
				.show();
	}
	
	private void onAnalysisClick(View view){
		Toast.makeText(this, "Analysis Archive", Toast.LENGTH_LONG)
		.show();
	}
	
	private void populateList() {
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("pen","MONT Blanc");
		temp.put("price", "200.00$");
		temp.put("color", "Silver, Grey, Black");
		list.add(temp);
		HashMap<String,String> temp1 = new HashMap<String,String>();
		temp1.put("pen","Gucci");
		temp1.put("price", "300.00$");
		temp1.put("color", "Gold, Red");
		list.add(temp1);
		HashMap<String,String> temp2 = new HashMap<String,String>();
		temp2.put("pen","Parker");
		temp2.put("price", "400.00$");
		temp2.put("color", "Gold, Blue");
		list.add(temp2);
		HashMap<String,String> temp3 = new HashMap<String,String>();
		temp3.put("pen","Sailor");
		temp3.put("price", "500.00$");
		temp3.put("color", "Silver");
		list.add(temp3);
		HashMap<String,String> temp4 = new HashMap<String,String>();
		temp4.put("pen","Porsche Design");
		temp4.put("price", "600.00$");
		temp4.put("color", "Silver, Grey, Red");
		list.add(temp4);
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
			new RefreshUsageAsync(this, handler).execute();
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
