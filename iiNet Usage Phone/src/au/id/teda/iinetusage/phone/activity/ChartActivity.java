package au.id.teda.iinetusage.phone.activity;

import java.text.ParseException;
import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.chart.StackedBarChart;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class ChartActivity extends ActionbarHelperActivity {

	// Static tags for debugging
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = ChartActivity.class.getSimpleName();

	// Chart objects
	private GraphicalView myStackedChart;
	private StackedBarChart myStackedBarChart;
	private LinearLayout myStackedLayout;

	// Gesture static int values to detect fling
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	// Gesture objects
	private GestureDetector myGestureDetector;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper myViewFlipper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);

		loadGestures();
		
		loadStackedChart();
	}

	/**
	 * Load gesture, animation and set touch listener for ViewFlipper
	 */
	private void loadGestures() {
		// Set reference for ViewFlipper layout
		myViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		
		// Set reference to gesture detector
		myGestureDetector = new GestureDetector(new MyGestureDetector());
		
		// Set animation references
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

		// Set the touch listener for the main view to be our custom gesture listener
		myViewFlipper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myGestureDetector.onTouchEvent(event);
				return false;
				}
			});
	}

	/**
	 * Check if it is an onTouchEvent or not
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (myGestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	/**
	 * Activity onResume method.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Set chart title based on current period
		setChartTitle();

		if (myStackedChart != null) {
			myStackedChart.repaint();
		}
	}

	/**
	 * Method for loading stacked chart into view
	 */
	public void loadStackedChart() {
		// Set reference for stacked layout
		myStackedLayout = (LinearLayout) findViewById(R.id.stacked_bar_chart);
		
		// Set reference for stacked bar chart object
		myStackedBarChart = new StackedBarChart(this);
		
		// Check if the chart doesn't already exist
		if (myStackedChart == null) {
			
			// Get chart view from library
			myStackedChart = (GraphicalView) myStackedBarChart.getBarChartView();
			
			// Add chart view to layout view
			myStackedLayout.addView(myStackedChart);
			
		} else {
			// use this whenever data has changed and you want to redraw
		}
		
		// Setup the touch listener for chart
		myStackedChart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myGestureDetector.onTouchEvent(event);
				return false;
				}
			});
	}

	/**
	 * Set chart title
	 * @throws ParseException
	 */
	public void setChartTitle(){
		AccountHelper myAccount = new AccountHelper();
		TextView upRollOverTV = (TextView) findViewById(R.id.chart_title);
		try {
			upRollOverTV.setText(myAccount.getCurrentDataPeriodString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handler for passing messages from other classes
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO: how do i use this??
		}
	};

	/**
	 * Gesture class for detecting and handling fling events
	 */
	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent motionEvent1,
				MotionEvent motionEvent2, 
				float velocityX,
				float velocityY) {
			try {
				// Check to see if swipe is to short
				if (Math.abs(motionEvent1.getY() - motionEvent2.getY()) > SWIPE_MAX_OFF_PATH) {
					return false;
				}
				// Check if it is a right to left swipe
				if (motionEvent1.getX() - motionEvent2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					myViewFlipper.setInAnimation(slideLeftIn);
					myViewFlipper.setOutAnimation(slideLeftOut);
					myViewFlipper.showNext();
				}
				// Else check if it is a left to right swipe
				else if (motionEvent2.getX() - motionEvent1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					myViewFlipper.setInAnimation(slideRightIn);
					myViewFlipper.setOutAnimation(slideRightOut);
					myViewFlipper.showPrevious();
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

		// It is necessary to return true from onDown for the onFling event to register
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

	}
}
