package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;


public class StackedBarChart extends ChartBuilder {

	//Static tag strings for logging information and debug
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = StackedBarChart.class.getSimpleName();
	
    private Context context;
    
	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String REMAINING = "Remaining";
	private static final String TITLE = "Data Usage";
    
    private static final String NOT_USED = "Not Used";
    
    private double maxDataUsage = 0;;
	
	/**
	 *  StackedBarChart constructor. Pass activity context
	 */
	public StackedBarChart(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 *  getBarChartView ()
	 *  
	 *  @return bar chart view from ChartFactory
	 */
	public View getBarChartView (){
		return ChartFactory.getBarChartView(context, 
				getStackedBarChartDataSet(), 
				getStackedBarChartRenderer(), 
				Type.STACKED);
	}
	
	/**
	 * Method for getting stacked data set
	 * 
	 * @return XYMultipleSeriesDataset from database
	 */
	protected XYMultipleSeriesDataset getStackedBarChartDataSet() {

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

		// Move to first cursor entry
		dailyDBCursor.moveToFirst();

		// And start adding to array
		while (dailyDBCursor.isAfterLast() == false) {

			// Get peak data usage from current cursor position
			long peakUsageLong = dailyDBCursor.getLong(dailyDBCursor
					.getColumnIndex(DailyDataDBAdapter.PEAK)) / 1000000000;

			// Get offpeak data usage from current cursor position
			long offpeakUsageLong = dailyDBCursor.getLong(dailyDBCursor
					.getColumnIndex(DailyDataDBAdapter.OFFPEAK)) / 1000000000;

			// Make data stacked (achartengine does not do it by default).
			if (peakUsageLong > offpeakUsageLong) {
				peakUsageLong = peakUsageLong + offpeakUsageLong;
			} else {
				offpeakUsageLong = offpeakUsageLong + peakUsageLong;
			}

			// Add current cursor values to data series
			peakSeries.add(peakUsageLong);
			offpeakSeries.add(offpeakUsageLong);

			// Set max data usage for rendering graph
			if (maxDataUsage < peakUsageLong + offpeakUsageLong) {
				maxDataUsage = peakUsageLong + offpeakUsageLong;
			}

			//
			dailyDBCursor.moveToNext();
		}

		dailyDBCursor.close();
		dailyDataDB.close();

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(peakSeries.toXYSeries());
		dataset.addSeries(offpeakSeries.toXYSeries());
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
	
	private XYMultipleSeriesRenderer getStackedBarChartRenderer() {
		// Set data series color
	    int[] colors = new int[] { getPeakColor(), getOffpeakColor() };
	    
	    // Load and initialise render objects
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    
	    renderer.setXAxisMin(0);
		renderer.setXAxisMax(getChartDays());
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(getMaxDataUsage());
		renderer.setLabelsColor(getLabelColor());
	    
	    // Chart render settings
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setMarginsColor(getBackgroundColor());
	    renderer.setPanEnabled(false, false);
	    renderer.setFitLegend(true);
	    renderer.setLabelsTextSize(18);
	    renderer.setLegendTextSize(22);
	    renderer.setAxesColor(getLabelColor());
	    renderer.setAntialiasing(true);
	    renderer.setBarSpacing(0.5f);
	    
	    return renderer;
	}
	
}
