package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class StackedLineChart extends ChartBuilder {
	
	//Static tag strings for logging information and debug
	//private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	//private static final String INFO_TAG = StackedLineChart.class.getSimpleName();
	
    private Context context;
    
    private static final String NOT_USED = "Not Used";

	public StackedLineChart(Context context) {
		super(context);
		this.context = context;
	}

	public View getStackedLineChartView() {
		return ChartFactory.getLineChartView(context, 
				getStackedAccumDataSet(),
				getChartRenderer());
	}

	private XYMultipleSeriesRenderer getChartRenderer() {
		// Set render object and initialise
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    
	    // Set chart defaults
	    setChartSettings(renderer,
	    		NOT_USED, getXAxes(), NOT_USED,
	    		0, getChartDays(),
	    		0, 100,
	    		getAxesColor(), getLabelColor());
	    
	    // Set point size to 0 to hide
	    renderer.setPointSize(0f);
	    
	    // Set series render object
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    
	    // Peak series render settings
	    r.setColor(getPeakColor());
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(getPeakFillColor());
	    r.setFillPoints(false);
	    r.setLineWidth(3);
	    renderer.addSeriesRenderer(r);
	    
	    // Offpeak series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getOffpeakColor());
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(getOffpeakFillColor());
	    r.setFillPoints(false);
	    r.setLineWidth(3);
	    renderer.addSeriesRenderer(r);
	    
	    // Graph render settings
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setMarginsColor(getBackgroundColor());
	    renderer.setPanEnabled(false, false);
	    renderer.setFitLegend(true);
	    renderer.setLabelsTextSize(18);
	    renderer.setLegendTextSize(22);
	    renderer.setAxesColor(getLabelColor());
	    renderer.setAntialiasing(true);

	    return renderer;
	}

}
