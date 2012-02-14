package au.id.teda.iinetusage.phone.chart;

import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class ChartBuilder {

	// Static tag strings for logging information and debug
	// private static final String DEBUG_TAG = "iiNet Usage";
	// private static final String INFO_TAG =
	// ChartBuilder.class.getSimpleName();

	private Context myActivityContext;

	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String REMAINING = "Remaining";
	private static final String TITLE = "Data Usage";

	private double maxDataUsage = 0;
	long accumPeak;
	long accumPeakStacked;
	long accumOffpeak = 0;

	// Color values for focus and alternate
	private final int peakColor;
	private final int peakFillColor;
	private final int offpeakColor;
	private final int offpeakFillColor;
	private final int remainingColor;
	private final int remainingFillColor;
	private final int axesColor;
	private final int labelColor;
	private final int backgroundColor;

	private final String xAxes;

	// Chart builder constructor
	public ChartBuilder(Context context) {
		myActivityContext = context;

		// Chart colours
		peakColor = myActivityContext.getResources().getColor(R.color.chart_peak_color);
		peakFillColor = myActivityContext.getResources().getColor(R.color.chart_peak_fill_color);
		offpeakColor = myActivityContext.getResources().getColor(R.color.chart_offpeak_color);
		offpeakFillColor = myActivityContext.getResources().getColor(R.color.chart_offpeak_fill_color);
		remainingColor = myActivityContext.getResources().getColor(R.color.chart_remaining_color);
		remainingFillColor = myActivityContext.getResources().getColor(R.color.chart_remaining_fill_color);
		axesColor = myActivityContext.getResources().getColor(R.color.chart_axes_color);
		labelColor = myActivityContext.getResources().getColor(R.color.chart_label_color);
		backgroundColor = myActivityContext.getResources().getColor(R.color.application_background_color);

		// Chart strings
		xAxes = myActivityContext.getResources().getString(R.string.chart_x_title);

	}

	/**
	 * Get peak color from XML
	 * 
	 * @return int color value
	 */
	public int getPeakColor() {
		return peakColor;
	}

	/**
	 * Get offpeak color from XML
	 * 
	 * @return int color value
	 */
	public int getOffpeakColor() {
		return offpeakColor;
	}

	/**
	 * Get chart axes color from XML
	 * 
	 * @return int color value
	 */
	public int getAxesColor() {
		return axesColor;
	}

	/**
	 * Get chart labal color from XML
	 * 
	 * @return int color value
	 */
	public int getLabelColor() {
		return labelColor;
	}

	/**
	 * Get chart background color from XML
	 * 
	 * @return int color value
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Get peak fill color from XML
	 * 
	 * @return int color value
	 */
	public int getPeakFillColor() {
		return peakFillColor;
	}

	/**
	 * Get offpeak fill color from XML
	 * 
	 * @return int color value
	 */
	public int getOffpeakFillColor() {
		return offpeakFillColor;
	}

	/**
	 * Get string X-Axis from XML
	 * 
	 * @return String value
	 */
	public String getXAxes() {
		return xAxes;
	}

	/**
	 * Get remaining color from XML
	 * 
	 * @return int color value
	 */
	public int getRemainingColor() {
		return remainingColor;
	}
	
	/**
	 * Get remaining fill color from XML
	 * 
	 * @return int color value
	 */
	public int getRemainingFillColor() {
		return remainingFillColor;
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
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
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
	 * Builds a bar multiple series dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param values the values
	 * @return the XY multiple bar dataset
	 */
	protected XYMultipleSeriesDataset buildBarDataset(String[] titles,
			List<double[]> values) {
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
	 * Builds a category renderer to use the provided colors.
	 * 
	 * @param colors the colors
	 * @return the category renderer
	 */
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}


	/**
	 * Method for returning the number of calendar days for this period
	 * 
	 * @return
	 */
	protected int getChartDays() {
		AccountHelper myStatus = new AccountHelper();

		Long daysSoFare = myStatus.getCurrentDaysSoFar();
		Long daysToGo = myStatus.getCurrentDaysToGo();

		int chartDays = (int) (daysSoFare + daysToGo);

		return chartDays;
	}

	

}
