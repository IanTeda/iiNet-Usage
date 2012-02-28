package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class PieChart extends ChartBuilder {
	

	// Static tag strings for logging information and debug
	// private static final String DEBUG_TAG = "iiNet Usage";
	// private static final String INFO_TAG = PieChart.class.getSimpleName();
	
	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String REMAINING = "Remaining";
	private static final String TITLE = "Data Usage";

	// Context for class
	private Context myActivity;

	/**
	 * PieChart constructor
	 * @param context
	 */
	public PieChart(Context context) {
		super(context);
		myActivity = context;
	}
	
	/**
	 * Public method for returning pie chart view
	 * @return pie chart view
	 */
	public View getPieChartView() {
		return ChartFactory.getPieChartView(myActivity, 
				getPieChartDataSet(),
				getPieChartRenderer());
	}
	
	/**
	 * Method for getting pie chart data set
	 * @return pie chart category series
	 */
	protected CategorySeries getPieChartDataSet() {

		// Account object and initialise
		AccountHelper myStatus = new AccountHelper();

		// Get values from account
		long peak = myStatus.getCurrentPeakUsed() / 1000000000;
		long offpeak = myStatus.getCurrentOffpeakUsed() / 1000000000;
		long peakQuota = myStatus.getPeakQuota() / 1000;
		long offpeakQuota = myStatus.getPeakQuota() / 1000;
		long remaining = peakQuota + offpeakQuota - peak - offpeak;

		// Category series object and intialise
		CategorySeries series = new CategorySeries(TITLE);

		// Add data to category series
		series.add(PEAK, peak);
		series.add(OFFPEAK, offpeak);
		series.add(REMAINING, remaining);

		// Return category series
		return series;
	}
	
	/**
	 * Render (display) settings for pie chart
	 * @return pie chart renderer
	 */
	private DefaultRenderer getPieChartRenderer() {
		int[] colors = new int[] { getPeakColor(), getOffpeakColor(), getRemainingFillColor() };
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setAxesColor(getBackgroundColor());
	    renderer.setPanEnabled(false);
	    renderer.setFitLegend(true);
	    renderer.setShowLabels(false);
	    renderer.setLabelsTextSize(getLabelsTextSize());
	    renderer.setLegendTextSize(getLegendTextSize());
	    renderer.setAntialiasing(true);
	    
	    return renderer;
	}

}
