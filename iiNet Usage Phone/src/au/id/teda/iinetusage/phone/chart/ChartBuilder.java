package au.id.teda.iinetusage.phone.chart;

import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class ChartBuilder {
	
    private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	
    private Context context;
    
	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	
	private double maxDataUsage = 0;
	long accumPeak;
	long accumPeakStacked;
	long accumOffpeak = 0;

	// Constructor - takes the context to allow the database to be opened/created
    public ChartBuilder(Context context) {
    	this.context = context;
    }
	
	  /**
	   * Builds a bar multiple series renderer to use the provided colors.
	   * 
	   * @param colors the series renderers colors
	   * @return the bar multiple series renderer
	   */
	  protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(25);
	    int length = colors.length;
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      r.setColor(colors[i]);
	      renderer.addSeriesRenderer(r);
	    }
	    return renderer;
	  }
	  
	  /**
	   * Builds an XY multiple series renderer.
	   * 
	   * @param colors the series rendering colors
	   * @param styles the series point styles
	   * @return the XY multiple series renderers
	   */
	  protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    setRenderer(renderer, colors, styles);
	    return renderer;
	  }
	  
	  protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		    renderer.setAxisTitleTextSize(16);
		    renderer.setChartTitleTextSize(20);
		    renderer.setLabelsTextSize(15);
		    renderer.setLegendTextSize(15);
		    renderer.setPointSize(5f);
		    renderer.setMargins(new int[] { 20, 30, 15, 20 });
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);
		      r.setPointStyle(styles[i]);
		      renderer.addSeriesRenderer(r);
		    }
		  }
	
	
	
	  /**
	   * Sets a few of the series renderer settings.
	   * 
	   * @param renderer the renderer to set the properties to
	   * @param title the chart title
	   * @param xTitle the title for the X axis
	   * @param yTitle the title for the Y axis
	   * @param xMin the minimum value on the X axis
	   * @param xMax the maximum value on the X axis
	   * @param yMin the minimum value on the Y axis
	   * @param yMax the maximum value on the Y axis
	   * @param axesColor the axes color
	   * @param labelsColor the labels color
	   */
	  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
	      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
	      int labelsColor) {
	    //renderer.setChartTitle(title);
	    //renderer.setXTitle(xTitle);
	    //renderer.setYTitle(yTitle);
	    renderer.setXAxisMin(xMin);
	    renderer.setXAxisMax(xMax);
	    renderer.setYAxisMin(yMin);
	    renderer.setYAxisMax(yMax);
	    renderer.setAxesColor(axesColor);
	    renderer.setLabelsColor(labelsColor);
	  }
	  
	  /**
	   * Builds a bar multiple series dataset using the provided values.
	   * 
	   * @param titles the series titles
	   * @param values the values
	   * @return the XY multiple bar dataset
	   */
	  protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    int length = titles.length;
	    for (int i = 0; i < length; i++) {
	      CategorySeries series = new CategorySeries(titles[i]);
	      double[] v = values.get(i);
	      int seriesLength = v.length;
	      for (int k = 0; k < seriesLength; k++) {
	        series.add(v[k]);
	      }
	      dataset.addSeries(series.toXYSeries());
	    }
	    return dataset;
	  }
	  
		/**
		 * Method for returning the number of calendar days for this period
		 * @return
		 */
		protected int getChartDays(){
			AccountHelper myStatus = new AccountHelper();
			
			Long daysSoFare = myStatus.getCurrentDaysSoFar();
			Long daysToGo = myStatus.getCurrentDaysToGo();
			
			int chartDays = (int) (daysSoFare + daysToGo);
			
			return chartDays;
		}
		
		/**
		 * Method for getting stacked data set
		 * @return XYMultipleSeriesDataset from database
		 */
		protected XYMultipleSeriesDataset getStackedDataSet() {
			
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
	        	long peakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.PEAK))/1000000;
	        	
	        	// Get offpeak data usage from current cursor position
	        	long offpeakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.OFFPEAK))/1000000;
	        	
	    		// Make data stacked (achartengine does not do it by default).
	    		if (peakUsageLong > offpeakUsageLong){
	    			peakUsageLong = peakUsageLong + offpeakUsageLong;
	    		} else {
	    			offpeakUsageLong = offpeakUsageLong + peakUsageLong;
	    		}
	    		
	    		// Add current cursor values to data series
	    		peakSeries.add(peakUsageLong);
	    		offpeakSeries.add(offpeakUsageLong);
	    		
	    		// Set max data usage for rendering graph
	    		if (maxDataUsage < peakUsageLong + offpeakUsageLong){
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
		 * Method for getting stacked data set
		 * @return XYMultipleSeriesDataset from database
		 */
		protected XYMultipleSeriesDataset getStackedAccumDataSet() {
			
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
	        	long peakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.PEAK))/1000000;
	        	accumPeak = accumPeak + peakUsageLong;
	        	
	        	// Get offpeak data usage from current cursor position
	        	long offpeakUsageLong = dailyDBCursor.getLong(dailyDBCursor.getColumnIndex(DailyDataDBAdapter.OFFPEAK))/1000000;
	        	accumOffpeak = accumOffpeak + offpeakUsageLong;
	        	Log.d(DEBUG_TAG, "accumPeak: " + accumPeak + " | accumOffpeak: " + accumOffpeak);
	        	
	    		// Make data stacked (achartengine does not do it by default).
	        	accumPeakStacked = accumPeak + accumOffpeak;
	    		/**if (accumPeak > accumOffpeak){
	    			accumPeak = accumPeak + accumOffpeak;
	    		} else {
	    			accumOffpeak = accumOffpeak + accumPeak;
	    		}**/
	    		
	    		// Add current cursor values to data series
	    		peakSeries.add(accumPeakStacked);
	    		offpeakSeries.add(accumOffpeak);
	    		
	    		// Set max data usage for rendering graph
	    		if (maxDataUsage < accumPeak + accumOffpeak){
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
			return dataset;
		}
		
		/**
		 * Method for retrieving max data used [set in getStackedDataSet()]
		 * @return double of max data used
		 */
		public double getMaxDataUsage(){
			return maxDataUsage;
		}

}
