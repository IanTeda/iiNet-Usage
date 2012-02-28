package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.iinetusage.phone.R;
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
	
	// Activity context
	private Context myActivity;

	/**
	 * DoughnutChart constructor
	 * @param context
	 */
	public DoughnutChart(Context context) {
		super(context);
		myActivity = context;
	}
	
	/**
	 * Get Doughnut Chart
	 * @return doughnut chart view
	 */
	public View getDoughnutChartView() {
		return ChartFactory.getDoughnutChartView(myActivity, 
				getDoughnutChartDataSeries(),
				getDoughnutChartRenderer());
	}

	/**
	 * Get doughnut chart data series
	 * @return chart data series
	 */
	private MultipleCategorySeries getDoughnutChartDataSeries() {
		
		// Static string values and initialise from XML values
		final String DAYS = myActivity.getResources().getString(R.string.chart_doughnut_days);
		final String DAYS_SO_FAR = myActivity.getResources().getString(R.string.chart_doughnut_days_soFar);
		final String DAYS_TO_GO = myActivity.getResources().getString(R.string.chart_doughnut_days_toGo);
		
		final String PEAK = myActivity.getResources().getString(R.string.chart_doughnut_peak);
		final String PEAK_SO_FAR = myActivity.getResources().getString(R.string.chart_doughnut_peak_soFar);
		final String PEAK_TO_GO = myActivity.getResources().getString(R.string.chart_doughnut_peak_toGo);
		
		final String OFFPEAK = myActivity.getResources().getString(R.string.chart_doughnut_offpeak);
		final String OFFPEAK_SO_FAR = myActivity.getResources().getString(R.string.chart_doughnut_offpeak_soFar);
		final String OFFPEAK_TO_GO = myActivity.getResources().getString(R.string.chart_doughnut_offpeak_toGo);
		
		// Account object and initialise
		AccountHelper myStatus = new AccountHelper();

		// Peak data
		long peak = myStatus.getCurrentPeakUsed() / 1000000000;
		long peakQuota = myStatus.getPeakQuota() / 1000;
		long peakRemaining = peakQuota - peak;
		double[] peakDouble = { peak, peakRemaining }; 
		String[] peakCats = { PEAK_SO_FAR, PEAK_TO_GO };
		
		// Offpeak data
		long offpeak = myStatus.getCurrentOffpeakUsed() / 1000000000;
		long offpeakQuota = myStatus.getPeakQuota() / 1000;
		long offpeakRemaining = offpeakQuota - offpeak;
		double[] offpeakDouble = { offpeak, offpeakRemaining };
		String[] offpeakCats = { OFFPEAK_SO_FAR, OFFPEAK_TO_GO };
		
		// Days
		long daysSoFare = myStatus.getCurrentDaysSoFar();
		long daysToGo = myStatus.getCurrentDaysToGo();
		double[] daysDouble = { daysSoFare, daysToGo };
		String[] dayCats = { DAYS_SO_FAR, DAYS_TO_GO };

		// Data series and initialise
		MultipleCategorySeries series = new MultipleCategorySeries(DAYS);
		series.add(DAYS_TO_GO, dayCats, daysDouble);
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
		
	    renderer.setLabelsTextSize(getLabelsTextSize());
	    renderer.setLegendTextSize(getLegendTextSize());
		
	    renderer.setPanEnabled(false);
	    renderer.setShowLegend(false);
	    renderer.setFitLegend(true);
	    
	    return renderer;
	}

}
