package au.id.teda.volumeusage.activity;

import org.achartengine.GraphicalView;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.chart.StackedBarChart;
import au.id.teda.volumeusage.prefs.Preferences;
import au.id.teda.volumeusage.service.RefreshUsageData;
import au.id.teda.volumeusage.service.ServiceHelper;

/**
 *  UsageGraphActivity.java
 *  Purpose: This activity displays the usage graphs
 * 
 *  @author Ian Teda
 *  @version Alpha
 *  
 */

public class UsageGraphActivity extends Activity implements OnClickListener {
	
	//TODO: Add other charts
	
	/**
	 *  Static tag strings for loging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = UsageGraphActivity.class.getSimpleName();

	private GraphicalView mChartView;
	
	/**
	 *  Activity onCreate method.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(INFO_TAG, "onCreate()");
		super.onCreate(savedInstanceState); //reload any saved activity instance state
		setContentView(R.layout.usage_graph);
		
		setUpActionBar();
        
        loadChart();

	}
	
	/**
	 *  Activity onResume method.
	 */
	@Override
	protected void onResume() {
		super.onResume();      
		if (mChartView != null) {
			mChartView.repaint();
		}
	}

	/**
	 *  Method used to set action bar title, reference buttons and set onClick listener
	 */
	public void setUpActionBar(){
		Log.i(INFO_TAG, "setupActionBar()");
		
		// Set action bar title
		TextView abTitle = (TextView) findViewById(R.id.action_bar_title_text);
		abTitle.setText(R.string.ab_usage_graph_view_title);
		
		// Set action bar onClick
        ImageButton abRefreshButton = (ImageButton) findViewById(R.id.action_bar_refresh_button); // This is the refresh button on the action bar
        abRefreshButton.setOnClickListener(this);
        ImageButton abHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button); // Take me back to the dashboard
        abHomeButton.setOnClickListener(this);
	}
	
	public void loadChart(){
		Log.i(INFO_TAG, "loadChart()");
		if (mChartView==null) {
			Log.d(DEBUG_TAG, "UsageGraphActivity > loadChart() > Chart view doesn't exist > Generate");
			LinearLayout chartLayout = (LinearLayout) findViewById(R.id.chart);
			//mChartView = ChartFactory.getBarChartView(this, getMyData(), getMyRenderer(), Type.STACKED);
			StackedBarChart stackedBarChart = new StackedBarChart(this);
			mChartView = (GraphicalView) stackedBarChart.getBarChartView();
			chartLayout.addView(mChartView);
		} else {
			Log.d(DEBUG_TAG, "UsageGraphActivity > loadChart() > Chart view exist > Repaint");
			mChartView.repaint(); // use this whenever data has changed and you want to redraw
		}
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
           //TODO: how do i use this??
        }
    };

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
			ServiceHelper serviceHelper = new ServiceHelper(this);
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
