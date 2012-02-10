package au.id.teda.iinetusage.phone.activity;

import java.text.ParseException;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.chart.StackedBarChart;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class ChartActivity extends ActionbarHelperActivity {

	// Static tags for debugging
	private static final String DEBUG_TAG = "iiNet Usage"; 
	private static final String INFO_TAG = ChartActivity.class.getSimpleName();

	private GraphicalView myChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);
		
		loadChart();
	}

	/**
	 * Activity onResume method.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		// Set chart title based on current period
		try {
			setChartTitle();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (myChart != null) {
			myChart.repaint();
		}
	}

	public void loadChart() {
		if (myChart == null) {
			LinearLayout chartLayout = (LinearLayout) findViewById(R.id.chart);
			StackedBarChart stackedBarChart = new StackedBarChart(this);
			myChart = (GraphicalView) stackedBarChart.getBarChartView();
			chartLayout.addView(myChart);
		} else {
			//use this whenever data has changed and you want to redraw
		}
	}

	public void setChartTitle() throws ParseException {
		AccountHelper myAccount = new AccountHelper();
		TextView upRollOverTV = (TextView) findViewById(R.id.chart_title);
		upRollOverTV.setText(myAccount.getCurrentDataPeriodString());
	}

	/**
	 * Handler for passing messages from other classes
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO: how do i use this??
		}
	};

}
