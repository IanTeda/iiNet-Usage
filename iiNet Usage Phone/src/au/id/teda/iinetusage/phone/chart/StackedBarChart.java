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

	//Static tag strings for loging information and debug
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = StackedBarChart.class.getSimpleName();
	
	private static final String NOT_USED = "Not Used";
	
    private Context context;
	
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
		return ChartFactory.getBarChartView(context, getStackedDataSet(), getChartRenderer(), Type.STACKED);
	}
	
	private XYMultipleSeriesRenderer getChartRenderer() {
		// Must have same number of renderer entries as data sets
		
		// Set max y-axis as max usage + 100
		Double maxDataUsage = getMaxDataUsage() + 100;
		
	    int[] colors = new int[] { Color.parseColor("#95FF5900"), Color.parseColor("#65FF5900") };
	    XYMultipleSeriesRenderer myRenderer = buildBarRenderer(colors);
	    setChartSettings(myRenderer, NOT_USED, NOT_USED, NOT_USED,0.5,
	    		getChartDays(), 0, maxDataUsage, Color.parseColor("#b9b9b9"), Color.parseColor("#b9b9b9")); 
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
