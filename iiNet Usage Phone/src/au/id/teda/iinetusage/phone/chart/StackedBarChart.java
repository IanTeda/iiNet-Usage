package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;


public class StackedBarChart extends ChartBuilder {

	//Static tag strings for logging information and debug
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = StackedBarChart.class.getSimpleName();
	
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
		return ChartFactory.getBarChartView(context, 
				getStackedDataSet(), 
				getChartRenderer(), 
				Type.STACKED);
	}
	
	private XYMultipleSeriesRenderer getChartRenderer() {
		// Set data series color
	    int[] colors = new int[] { getPeakColor(), getOffpeakColor() };
	    
	    // Load and initialise render objects
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    
	    // Default chart settings
	    setChartSettings(renderer, 
	    		NOT_USED, NOT_USED, NOT_USED,
	    		0.5, getChartDays(), 
	    		0, getMaxDataUsage(), 
	    		getAxesColor(), getLabelColor());
	    
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
