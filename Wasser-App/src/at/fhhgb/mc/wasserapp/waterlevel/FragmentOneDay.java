package at.fhhgb.mc.wasserapp.waterlevel;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import at.fhhgb.mc.wasserapp.R;

public class FragmentOneDay extends Fragment{
	Context mContext;
	View mRootView;
	/** The m chart. */
	private GraphicalView mChart;
	
	/** The m day. */
	private String[] mDay = new String[] { "7", "6", "5", "4", "3", "2", "1", "0" };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_one_day,
				container, false);
		mContext = mRootView.getContext();
		
		openChart();
		
		return mRootView;
	}
	
	private void openChart() {
		// Define the number of elements you want in the chart.
		int z[] = { 0, 1, 2, 3, 4, 5, 6, 7 };

		// TODO: FILL THE ARRAY WITH VALUES FROM THE DB !!!!
		float x[] = { 4, 2, 3, 3.6f, 4, 2, 3.5f, 5 };

		// Create XY Series for X Series.
		XYSeries xSeries = new XYSeries("X Series");

		// Adding data to the X Series.
		for (int i = 0; i < z.length; i++) {
			xSeries.add(z[i], x[i]);
		}

		// Create a Dataset to hold the XSeries.

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// Add X series to the Dataset.
		dataset.addSeries(xSeries);

		// Create XYSeriesRenderer to customize XSeries

		XYSeriesRenderer Xrenderer = new XYSeriesRenderer();
		Xrenderer.setColor(Color.WHITE);
		Xrenderer.setPointStyle(PointStyle.DIAMOND);
		Xrenderer.setDisplayChartValues(false);
		Xrenderer.setLineWidth(8);
		Xrenderer.setFillPoints(true);

		// Create XYMultipleSeriesRenderer to customize the whole chart

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		mRenderer.setXTitle(getString(R.string.waterlevel_daysago));
		mRenderer.setYTitle(getString(R.string.waterlevel_meter));
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setZoomEnabled(false, false);
		mRenderer.setXLabels(0);
		mRenderer.setPanEnabled(false);
		mRenderer.setAxesColor(Color.WHITE);

		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setYLabelsAlign(Align.LEFT);
		mRenderer.setYLabelsColor(0, Color.WHITE);
		mRenderer.setXLabelsColor(Color.WHITE);

		//mRenderer.setYLabelsPadding();

		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
		mRenderer.setBackgroundColor(Color.TRANSPARENT);

		mRenderer.setLabelsTextSize(30);
		mRenderer.setAxisTitleTextSize(30);
		mRenderer.setChartTitleTextSize(30);

		mRenderer.setShowGrid(true);
		mRenderer.setClickEnabled(false);
		mRenderer.setShowLegend(false);

		for (int i = 0; i < z.length; i++) {
			mRenderer.addXTextLabel(i, mDay[i]);
		}

		// Adding the XSeriesRenderer to the MultipleRenderer.
		mRenderer.addSeriesRenderer(Xrenderer);

		LinearLayout chart_container = (LinearLayout) mRootView.findViewById(R.id.Chart_layout);

		// Creating an intent to plot line chart using dataset and
		// multipleRenderer

		mChart = (GraphicalView) ChartFactory.getLineChartView(
				mContext, dataset, mRenderer);

		// Add the graphical view mChart object into the Linear layout .
		chart_container.addView(mChart);

	}
	
}
