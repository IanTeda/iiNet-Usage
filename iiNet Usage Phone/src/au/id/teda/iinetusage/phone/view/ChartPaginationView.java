package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import au.id.teda.iinetusage.phone.R;

/**
 * Class for ViewFlipper pagination
 * @author Ian
 *
 */
public class ChartPaginationView {

	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = ChartPaginationView.class.getSimpleName();

	// Context/Activity objects
	private final Context myActivityContext;
	private final Activity myActivity;

	// View objects for pagination
	private final View myLineChartDot;
	private final View myBarChartDot;
	private final View myPieChartDot;
	private final View myDoughnutChartDot;

	// Drawable objects for drawable backgrounds
	private final Drawable activeBackground;
	private final Drawable inactiveBackground;

	// Change size of pagination square
	private final int activeSize = 15;
	private final int inactiveSize =12;

	/**
	 * Class constructor
	 * 
	 * @param context
	 */
	public ChartPaginationView(Context context) {

		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;

		// Initialise pagination views
		myLineChartDot = (View) myActivity.findViewById(R.id.chart_footer_line);
		myBarChartDot = (View) myActivity.findViewById(R.id.chart_footer_bar);
		myPieChartDot = (View) myActivity.findViewById(R.id.chart_footer_pie);
		myDoughnutChartDot = (View) myActivity.findViewById(R.id.chart_footer_doughnut);

		// Initialise drawable backgrounds
		activeBackground = myActivity.getResources().getDrawable(R.drawable.pagination_active);
		inactiveBackground = myActivity.getResources().getDrawable(R.drawable.pagination_inactive);

	}

	/**
	 * Method for setting position of pagination
	 * @param position
	 */
	public void setActive(int position){
		if (position == 1){
			setBarChartActive();
		}
		else if (position == 2){
			setPieChartActive();
		}
		else if (position == 3){
			setDoughnutChartActive();
		}
		else {
			setLineChartActive();
		}

	}

	/**
	 * Set pagination to line chart
	 */
	private void setLineChartActive(){
		myLineChartDot.setBackgroundDrawable(activeBackground);
		myBarChartDot.setBackgroundDrawable(inactiveBackground);
		myPieChartDot.setBackgroundDrawable(inactiveBackground);
		myDoughnutChartDot.setBackgroundDrawable(inactiveBackground);

		setActiveSize(myLineChartDot);
		setInactiveSize(myBarChartDot);
		setInactiveSize(myPieChartDot);
		setInactiveSize(myDoughnutChartDot);
	}

	/**
	 * Set pagination to bar chart
	 */
	private void setBarChartActive(){
		myLineChartDot.setBackgroundDrawable(inactiveBackground);
		myBarChartDot.setBackgroundDrawable(activeBackground);
		myPieChartDot.setBackgroundDrawable(inactiveBackground);
		myDoughnutChartDot.setBackgroundDrawable(inactiveBackground);

		setInactiveSize(myLineChartDot);
		setActiveSize(myBarChartDot);
		setInactiveSize(myPieChartDot);
		setInactiveSize(myDoughnutChartDot);
	}

	/**
	 * Set pagination to pie chart
	 */
	private void setPieChartActive(){
		myLineChartDot.setBackgroundDrawable(inactiveBackground);
		myBarChartDot.setBackgroundDrawable(inactiveBackground);
		myPieChartDot.setBackgroundDrawable(activeBackground);
		myDoughnutChartDot.setBackgroundDrawable(inactiveBackground);

		setInactiveSize(myLineChartDot);
		setInactiveSize(myBarChartDot);
		setActiveSize(myPieChartDot);
		setInactiveSize(myDoughnutChartDot);
	}

	/**
	 * Set pagination to Doughnut chart
	 */
	private void setDoughnutChartActive(){
		myLineChartDot.setBackgroundDrawable(inactiveBackground);
		myBarChartDot.setBackgroundDrawable(inactiveBackground);
		myPieChartDot.setBackgroundDrawable(inactiveBackground);
		myDoughnutChartDot.setBackgroundDrawable(activeBackground);

		setInactiveSize(myLineChartDot);
		setInactiveSize(myBarChartDot);
		setInactiveSize(myPieChartDot);
		setActiveSize(myDoughnutChartDot);
	}

	/**
	 * Resize view to active size
	 * @param view to be resized
	 */
	private void setActiveSize(View view){
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height =  getPixelDip(activeSize);
		params.width = getPixelDip(activeSize);
		view.setLayoutParams(params);
	}

	/**
	 * Resize view to inactive size
	 * @param view to be resized
	 */
	private void setInactiveSize(View view){
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height =  getPixelDip(inactiveSize);
		params.width = getPixelDip(inactiveSize);
		view.setLayoutParams(params);
	}

	/**
	 * Calculate pixel value for dip
	 * @param dip value to be converted
	 * @return pixel value of dip for current screen density
	 */
	private int getPixelDip(int dip){
		final float scale = myActivity.getResources().getDisplayMetrics().density;
		int pixels = (int) (dip * scale + 0.5f);
		return pixels;
	}

}