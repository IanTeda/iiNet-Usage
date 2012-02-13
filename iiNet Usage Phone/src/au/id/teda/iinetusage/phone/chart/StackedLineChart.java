package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public class StackedLineChart extends ChartBuilder {
	
	//Static tag strings for loging information and debug
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = StackedLineChart.class.getSimpleName();
	
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
		
		// Set max y-axis as max usage + 100
		Double maxDataUsage = getMaxDataUsage() + 100;
		
		// Set colors to be used in chart
		int[] colors = new int[] { Color.parseColor("#95FF5900"), Color.parseColor("#65FF5900") };
		
		// Set point styles
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
	    
	    // Set renderer
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		
		// Chart settings
		setChartSettings(renderer, NOT_USED, NOT_USED, NOT_USED, 0, getChartDays(), 0, 100000, Color.parseColor("#b9b9b9"), Color.parseColor("#b9b9b9"));
		
		//renderer.setXLabels(10);
		//renderer.setYLabels(10);
		renderer.setShowGrid(false);
		//renderer.setXLabelsAlign(Align.RIGHT);
		//renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(false);
		renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		
		return renderer;
	}

}
