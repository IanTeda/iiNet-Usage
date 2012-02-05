package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;


public class StackedBarChart extends ChartBuilder {

	/**
	 *  Static tag strings for loging information and debug
	 */
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = StackedBarChart.class.getSimpleName();
	
	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String NOT_USED = "Not Used";
	
    private Context context;
    private double maxDataUsage = 0;
    private int dayCounter = -3;
	
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
	 *  @return bar chart view from chartfactory
	 */
	public View getBarChartView (){
		return ChartFactory.getBarChartView(context, getMyData(), getMyRenderer(), Type.STACKED);
	}
	
	/**
	 * getMyData() for generating dataset with stacked bar chart graphs
	 * @return XYMultipleSeriesDataset for displaying graph
	 */
	private XYMultipleSeriesDataset getMyData() {
		// Open Database
		DailyDataDBAdapter dailyDataDB = new DailyDataDBAdapter();
		dailyDataDB.open();
		
		// Get current data period
		AccountHelper accountHelper = new AccountHelper();
		String dataPeriod = accountHelper.getCurrentDataPeriod();
		
		// Retrive cursor for given data period & manage
		Cursor dailyDBCursor = dailyDataDB.fetchPeriodUsage(dataPeriod);
		
		// Define usage categories for graph
		CategorySeries peakSeries = new CategorySeries(PEAK);
		CategorySeries offpeakSeries = new CategorySeries(OFFPEAK);
		
		// Move to first cursor entry and start adding to array
		dailyDBCursor.moveToFirst();
		while (dailyDBCursor.isAfterLast() == false) {
        	long peakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.PEAK))/1000000; // Pull peak usage from cursor
        	long offpeakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.OFFPEAK))/1000000; // Pull offpeak usage from cursor
    		//Log.d(DEBUG_TAG, "UsageGraphActivity > getMyData() > Peak: " + peakUsageLong + " & Offpeak: " + offpeakUsageLong);
        	
    		// Make stack (achartengine does not do it by default).
    		if (peakUsageLong > offpeakUsageLong){
    			peakUsageLong = peakUsageLong + offpeakUsageLong;
    		} else {
    			offpeakUsageLong = offpeakUsageLong + peakUsageLong;
    		}
    		//Log.d(DEBUG_TAG, "UsageGraphActivity > getMyData() > Peak: " + peakUsageLong + " & Offpeak: " + offpeakUsageLong);
    		
    		peakSeries.add(peakUsageLong);
    		offpeakSeries.add(offpeakUsageLong);
    		//Log.d(DEBUG_TAG, "UsageGraphActivity > getMyData() > peakSeries: " + peakSeries.getValue(dayCounter) + " & offpeakSeries: " + offpeakSeries.getValue(dayCounter));
    		// Set max data usage for rendering graph
    		if (maxDataUsage < peakUsageLong + offpeakUsageLong){
    			maxDataUsage = peakUsageLong + offpeakUsageLong;
    		}
			++dayCounter;
    		dailyDBCursor.moveToNext();
        }
        dailyDBCursor.close();
		dailyDataDB.close();
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(peakSeries.toXYSeries());
		dataset.addSeries(offpeakSeries.toXYSeries());
		return dataset;
	}
	
	private XYMultipleSeriesRenderer getMyRenderer() {
		// Must have same number of renderer entries as data sets
		maxDataUsage = maxDataUsage + 100; // Get the max number for data usage and us as max y-axis value + 500
		//Log.d(DEBUG_TAG, "UsageGraphActivity > getMyRenderer() > Set max Y-Axis as: " + maxDataUsage);
		//Log.d(DEBUG_TAG, "UsageGraphActivity > getMyRenderer() > Set max x-Axis as: " + dayCounter);
	    int[] colors = new int[] { Color.parseColor("#95FF5900"), Color.parseColor("#65FF5900") };
	    XYMultipleSeriesRenderer myRenderer = buildBarRenderer(colors);
	    setChartSettings(myRenderer, NOT_USED, NOT_USED, NOT_USED,0.5,
	    		dayCounter, 0, maxDataUsage, Color.parseColor("#b9b9b9"), Color.parseColor("#b9b9b9")); 
	    myRenderer.getSeriesRendererAt(0).setDisplayChartValues(false);
	    myRenderer.getSeriesRendererAt(1).setDisplayChartValues(false);
	    
	    myRenderer.setApplyBackgroundColor(true);
	    myRenderer.setBackgroundColor(Color.TRANSPARENT);
	    myRenderer.setMarginsColor(Color.parseColor("#181717"));
	    
	    //myRenderer.setXLabels(1);
	    //myRenderer.setYLabels(2500);
	    myRenderer.setXLabelsAlign(Align.LEFT);
	    myRenderer.setYLabelsAlign(Align.LEFT);
	    myRenderer.setPanEnabled(false, false);
	    // renderer.setZoomEnabled(false);
	    myRenderer.setZoomRate(1.1f);
	    myRenderer.setBarSpacing(0.5f);
	    //myRenderer.setShowGrid(true);
	    //myRenderer.setAntialiasing(true);
	    //myRenderer.setFitLegend(true);
	    return myRenderer;
	}
	
}
