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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.animation.ViewFlipperAnimation;
import au.id.teda.iinetusage.phone.chart.StackedBarChart;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class ChartActivity extends ActionbarHelperActivity {

	// Static tags for debugging
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = ChartActivity.class.getSimpleName();

	private GraphicalView myChart;
	private ViewFlipper myViewFlipper;
	private float oldTouchValue;
	private ViewFlipperAnimation myFlipperAnimation;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);

		// loadChart();
		
		viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.right_out);
        
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
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
			// use this whenever data has changed and you want to redraw
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
	
	class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                 viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                 viewFlipper.showNext();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                 viewFlipper.setInAnimation(slideRightIn);
                    viewFlipper.setOutAnimation(slideRightOut);
                 viewFlipper.showPrevious();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

}
