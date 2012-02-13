package au.id.teda.iinetusage.phone.chart;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class PieChart extends ChartBuilder {
	

	// Static tag strings for logging information and debug
	// private static final String DEBUG_TAG = "iiNet Usage";
	// private static final String INFO_TAG = PieChart.class.getSimpleName();

	private Context context;

	public PieChart(Context context) {
		super(context);
		this.context = context;
	}
	
	public View getPieChartView() {
		return ChartFactory.getPieChartView(context, 
				getCategoryDataSet(),
				getChartRenderer());
	}
	
	
	private DefaultRenderer getChartRenderer() {
		int[] colors = new int[] { getPeakColor(), getOffpeakColor(), getRemainingColor() };
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setAxesColor(getBackgroundColor());
	    renderer.setPanEnabled(false);
	    renderer.setFitLegend(true);
	    renderer.setLabelsTextSize(18);
	    renderer.setLegendTextSize(22);
	    renderer.setAxesColor(getLabelColor());
	    renderer.setAntialiasing(true);
	    renderer.setChartTitleTextSize(20);
	    
	    return renderer;
	}

}
