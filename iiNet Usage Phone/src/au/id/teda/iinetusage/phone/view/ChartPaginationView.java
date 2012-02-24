package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import au.id.teda.iinetusage.phone.R;

public class ChartPaginationView {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = ChartPaginationView.class.getSimpleName();

	private final Context myActivityContext;
	private final Activity myActivity;
	
	private final View myLineChartDot;
	private final View myBarChartDot;
	private final View myPieChartDot;
	private final View myDoughnutChartDot;
	
	private final Drawable activeBackground;
	private final Drawable inactiveBackground;
	
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
	
		// Set IP & Up Time TextView objects
		myLineChartDot = (View) myActivity.findViewById(R.id.chart_footer_line);
		myBarChartDot = (View) myActivity.findViewById(R.id.chart_footer_bar);
		myPieChartDot = (View) myActivity.findViewById(R.id.chart_footer_pie);
		myDoughnutChartDot = (View) myActivity.findViewById(R.id.chart_footer_doughnut);
		
		activeBackground = myActivity.getResources().getDrawable(R.drawable.pagination_active);
		inactiveBackground = myActivity.getResources().getDrawable(R.drawable.pagination_inactive);
		
	}
	
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
	
	public void setLineChartActive(){
		myLineChartDot.setBackgroundDrawable(activeBackground);
		myBarChartDot.setBackgroundDrawable(inactiveBackground);
		myPieChartDot.setBackgroundDrawable(inactiveBackground);
		myDoughnutChartDot.setBackgroundDrawable(inactiveBackground);
	}
	
	public void setBarChartActive(){
		myLineChartDot.setBackgroundDrawable(inactiveBackground);
		myBarChartDot.setBackgroundDrawable(activeBackground);
		myPieChartDot.setBackgroundDrawable(inactiveBackground);
		myDoughnutChartDot.setBackgroundDrawable(inactiveBackground);
	}
	
	public void setPieChartActive(){
		myLineChartDot.setBackgroundDrawable(inactiveBackground);
		myBarChartDot.setBackgroundDrawable(inactiveBackground);
		myPieChartDot.setBackgroundDrawable(activeBackground);
		myDoughnutChartDot.setBackgroundDrawable(inactiveBackground);
	}
	
	public void setDoughnutChartActive(){
		myLineChartDot.setBackgroundDrawable(inactiveBackground);
		myBarChartDot.setBackgroundDrawable(inactiveBackground);
		myPieChartDot.setBackgroundDrawable(inactiveBackground);
		myDoughnutChartDot.setBackgroundDrawable(activeBackground);
	}
	
	
	
	
	/**private void setSizeSmall(View view){
		
		final float scale = myActivity.getResources().getDisplayMetrics().density;
		int dip = (int) (60 * scale + 0.5f);
		
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.layout_height = 14;
		params.width = 12;
	    view.setLayoutParams(myActivityContextparams);
	}**/


}
