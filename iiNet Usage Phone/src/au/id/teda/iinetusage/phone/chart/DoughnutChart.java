package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

/**
 * Doughnut chart class for display chart
 * @author Ian Teda
 *
 */
public class DoughnutChart extends ChartBuilder {

	// Static tag strings for logging information and debug
	// private static final String DEBUG_TAG = "iiNet Usage";
	// private static final String INFO_TAG = DoughnutChart.class.getSimpleName();
	
	// Static string values
	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String USED = "Used";
	private static final String REMAINING = "Remaining";
	private static final String TITLE = "Data Usage";
	private static final String DAYS_SO_FARE = "Days So Fare";
	private static final String DAYS_TO_GO = "Days to Go";

	// Activity context
	private Context context;

	/**
	 * DoughnutChart constructor
	 * @param context
	 */
	public DoughnutChart(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * Get Doughnut Chart
	 * @return doughnut chart view
	 */
	public View getDoughnutChartView() {
		return ChartFactory.getDoughnutChartView(context, 
				getDoughnutChartDataSeries(),
				getDoughnutChartRenderer());
	}

	/**
	 * Get doughnut chart data series
	 * @return chart data series
	 */
	private MultipleCategorySeries getDoughnutChartDataSeries() {
		
		// Account object and initialise
		AccountHelper myStatus = new AccountHelper();

		// Peak data
		long peak = myStatus.getCurrentPeakUsed() / 1000000000;
		long peakQuota = myStatus.getPeakQuota() / 1000;
		long peakRemaining = peakQuota - peak;
		double[] peakDouble = { peak, peakRemaining }; 
		String[] peakCats = { USED, REMAINING };
		
		// Offpeak data
		long offpeak = myStatus.getCurrentOffpeakUsed() / 1000000000;
		long offpeakQuota = myStatus.getPeakQuota() / 1000;
		long offpeakRemaining = offpeakQuota - offpeak;
		double[] offpeakDouble = { offpeak, offpeakRemaining };
		String[] offpeakCats = { USED, REMAINING };
		
		// Days
		long daysSoFare = myStatus.getCurrentDaysSoFar();
		long daysToGo = myStatus.getCurrentDaysToGo();
		double[] daysDouble = { daysSoFare, daysToGo };
		String[] dayCats = { DAYS_SO_FARE, DAYS_TO_GO };

		// Data series and initialise
		MultipleCategorySeries series = new MultipleCategorySeries(TITLE);
		series.add(REMAINING, dayCats, daysDouble);
		series.add(PEAK, peakCats, peakDouble );
		series.add(OFFPEAK, offpeakCats, offpeakDouble);
		
		// Return data series
		return series;
	}
	
	/**
	 * Doughnut chart display settings
	 * @return chart renderer
	 */
	private DefaultRenderer getDoughnutChartRenderer() {

	    DefaultRenderer renderer = new DefaultRenderer();
	    
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(getPeakColor());
		renderer.addSeriesRenderer(r);
	    
		r = new SimpleSeriesRenderer();
		r.setColor(getPeakFillColor());
		renderer.addSeriesRenderer(r);
		
	    //renderer.setMargins(new int[] { 20, 30, 15, 0 });
	    //renderer.setApplyBackgroundColor(true);
	    //renderer.setBackgroundColor(Color.TRANSPARENT);
	    //renderer.setAxesColor(Color.TRANSPARENT);
	    //renderer.setLabelsColor(Color.TRANSPARENT);
	    //renderer.setShowAxes(true);
	    renderer.setPanEnabled(false);
	    renderer.setFitLegend(true);
	    //renderer.setLabelsTextSize(18);
	    //renderer.setLegendTextSize(22);
	    //renderer.setShowLabels(true);
	    //renderer.setAntialiasing(true);
	    
	    return renderer;
	}

}
