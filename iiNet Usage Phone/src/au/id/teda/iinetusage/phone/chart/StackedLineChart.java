package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class StackedLineChart extends ChartBuilder {
	
	//Static tag strings for logging information and debug
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = StackedLineChart.class.getSimpleName();
	
    private Context myActivity;
    
	private static final String PEAK = "Peak";
	private static final String PEAK_QUOTA = "Peak Quota";
	private static final String OFFPEAK = "Offpeak";
	private static final String OFFPEAK_QUOTA = "Offpeak Quota";
    
    private double maxDataUsage = 0;
    
	public StackedLineChart(Context context) {
		super(context);
		myActivity = context;
	}

	public View getStackedLineChartView() {
		return ChartFactory.getLineChartView(myActivity, 
				getStackedLineChartDataSet(),
				getStackedLineChartRenderer());
	}
	
	/**
	 * Method for getting stacked data set
	 * 
	 * @return XYMultipleSeriesDataset from database
	 */
	protected XYMultipleSeriesDataset getStackedLineChartDataSet() {
		
		// Set daily average objects and initialise
		double peakAv = 0;
		double offpeakAv = 0;
		double offPeakAvStacked = 0;

		// Open Database
		DailyDataDBAdapter dailyDataDB = new DailyDataDBAdapter();
		dailyDataDB.open();

		// Get current data period
		AccountHelper accountHelper = new AccountHelper();
		String dataPeriod = accountHelper.getCurrentDataPeriod();

		// Retrieve cursor for given data period
		Cursor dailyDBCursor = dailyDataDB.fetchPeriodUsage(dataPeriod);

		// Set String value categories for graph
		CategorySeries peakSeries = new CategorySeries(PEAK);
		CategorySeries offpeakSeries = new CategorySeries(OFFPEAK);
		CategorySeries peakQuotaSeries = new CategorySeries(PEAK_QUOTA);
		CategorySeries offpeakQuotaSeries = new CategorySeries(OFFPEAK_QUOTA);
		
		// Get average daily useage
		double peakDailyAv = accountHelper.getPeakDailyQuotaMb();
		double offpeakDailyAv = accountHelper.getOffpeakDailyQuotaMb();
		
		// Move to first cursor entry
		dailyDBCursor.moveToFirst();

		// And start adding to array
		while (dailyDBCursor.isAfterLast() == false) {

			// Get peak data usage from current cursor position
			long peakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.PEAK)) / 1000000000;
			accumPeak = accumPeak + peakUsageLong;

			// Get offpeak data usage from current cursor position
			long offpeakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.OFFPEAK)) / 1000000000;
			accumOffpeak = accumOffpeak + offpeakUsageLong;

			// Set average daily line
			peakAv = peakAv + peakDailyAv;
			offpeakAv = offpeakAv + offpeakDailyAv;
			offPeakAvStacked = peakAv + offpeakAv;

			
			// Make data stacked (achartengine does not do it by default).
			accumPeakStacked = accumPeak + accumOffpeak;

			// Add current cursor values to data series
			peakSeries.add(accumPeakStacked);
			offpeakSeries.add(accumOffpeak);
			peakQuotaSeries.add(peakAv/1000);
			offpeakQuotaSeries.add(offPeakAvStacked/1000);
			
			//Log.d(DEBUG_TAG, "peakDailyAv: " + peakDailyAv + " | offpeakDailyAv: " + offpeakDailyAv + " | peakAv: " + peakAv/1000 + " | offPeakAvStacked: " + offPeakAvStacked/1000);

			// Set max data usage for rendering graph
			if (maxDataUsage < accumPeak + accumOffpeak) {
				maxDataUsage = accumPeak + accumOffpeak;
			}

			//
			dailyDBCursor.moveToNext();
		}
		dailyDBCursor.close();
		dailyDataDB.close();

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(peakSeries.toXYSeries());
		dataset.addSeries(offpeakSeries.toXYSeries());
		dataset.addSeries(peakQuotaSeries.toXYSeries());
		dataset.addSeries(offpeakQuotaSeries.toXYSeries());
		return dataset;
	}

	/**
	 * Method for retrieving max data used [set in getStackedDataSet()]
	 * 
	 * @return double of max data used
	 */
	public double getMaxDataUsage() {
		return maxDataUsage;
	}

	private XYMultipleSeriesRenderer getStackedLineChartRenderer() {
		// Set render object and initialise
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

	    // Set series render object
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    
	    // Peak series render settings
	    r.setColor(getPeakColor());
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(getPeakFillColor());
	    r.setFillPoints(false);
	    r.setLineWidth(3);
	    renderer.addSeriesRenderer(r);
	    
	    // Offpeak series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getOffpeakColor());
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(getOffpeakFillColor());
	    r.setFillPoints(false);
	    r.setLineWidth(3);
	    renderer.addSeriesRenderer(r);
	    
	    // Peak daily series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getPeakTrendColor());
	    r.setFillBelowLine(false);
	    r.setFillPoints(false);
	    r.setLineWidth(2);
	    renderer.addSeriesRenderer(r);
	    
	    // Offpeak daily series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getOffpeakTrendColor());
	    r.setFillBelowLine(false);
	    r.setFillPoints(false);
	    r.setLineWidth(2);
	    renderer.addSeriesRenderer(r);
	    
	    // Graph render settings
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setMarginsColor(getBackgroundColor());
	    renderer.setPanEnabled(false, false);
	    renderer.setShowLegend(true);
	    renderer.setFitLegend(true);
	    renderer.setLabelsTextSize(getLabelsTextSize());
	    renderer.setLegendTextSize(getLegendTextSize());
	    renderer.setAxesColor(getLabelColor());
	    renderer.setAntialiasing(true);
	    renderer.setXAxisMin(0);
		renderer.setXAxisMax(getChartDays());
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(100);
		renderer.setAxesColor(getAxesColor());
		renderer.setLabelsColor(getLabelColor());
	    
	    // Set point size to 0 to hide
	    renderer.setPointSize(0f);

	    return renderer;
	}

}
