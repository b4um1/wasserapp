package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.parser.DataOneDayJSONParser;

public class FragmentSevenDays extends Fragment {
	Context mContext;
	View mRootView;
	/** The m chart. */
	private GraphicalView mChart;

	private final String GETDAYMEASUREMENT = "getWeekDataForMeasuringpoint";
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/rivers.php";

	private final String USER_AGENT = "Mozilla/5.0";

	ArrayList<TimeWaterlevel> eachDay;

	private int mMeasuringpointId;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_seven_days,
				container, false);
		mContext = mRootView.getContext();
		
		Bundle bundle = this.getArguments();

		mMeasuringpointId = -1;
		if (bundle != null) {
			mMeasuringpointId = bundle.getInt("measuringpointId", -1);
		}

		if (mMeasuringpointId != -1) {
			new RetrieveWeekMeasurements().execute();
		}

		// openChart();

		return mRootView;
	}
	
	private void openChart() {
		// Define the number of elements you want in the chart.
//		int z[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
//				17, 18, 19, 20, 21, 22, 23 };

		// TODO: FILL THE ARRAY WITH VALUES FROM THE DB !!!!

		// int x[] = { 171, 172, 171, 170, 171, 172, 173, 200};

		int z[] = new int[eachDay.size()];
		int x[] = new int[eachDay.size()];
		int month[] = new int[eachDay.size()];
		String[] dayMonth;
		for (int i = 0; i < eachDay.size(); i++) {
			x[i] = Integer.parseInt(eachDay.get(i).getWaterlevel());
			String temp = eachDay.get(i).getTimestamp();
			Log.i("TEMP", temp);
			dayMonth = temp.split("\\.");
			Log.i("size", dayMonth.length + "");
			z[i] = Integer.parseInt(dayMonth[0]);
			month[i] = Integer.parseInt(dayMonth[1]);
		}

		// Create XY Series for X Series.
		XYSeries xSeries = new XYSeries("X Series");

		// Adding data to the X Series.
//		for (int i = 0; i < z.length; i++) {
//			xSeries.add(z[i], x[i]);
//		}
		
		for (int i = 0; i < x.length; i++) {
			xSeries.add(i, x[i]);
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

		mRenderer.setXTitle(getString(R.string.waterlevel_date));
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setZoomEnabled(false, false);
		mRenderer.setXLabels(0);
		mRenderer.setPanEnabled(false);
		mRenderer.setAxesColor(Color.WHITE);

		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setYLabelsColor(0, Color.WHITE);
		mRenderer.setYLabelsVerticalPadding(-10);
		mRenderer.setYLabelsPadding(3);
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
		
		mRenderer.setMargins(new int[] {15, 55, 10, 55});

		for (int i = 0; i < z.length; i++) {
			if (z[i] < 10) {
				mRenderer.addXTextLabel(i, "0" + z[i] + "." + month[i]);
			} else {
				mRenderer.addXTextLabel(i, "" + z[i] + "." + month[i]);
			}
		}

		// Adding the XSeriesRenderer to the MultipleRenderer.
		mRenderer.addSeriesRenderer(Xrenderer);

		LinearLayout chart_container = (LinearLayout) mRootView
				.findViewById(R.id.Chart_layout);

		// Creating an intent to plot line chart using dataset and
		// multipleRenderer

		mChart = (GraphicalView) ChartFactory.getLineChartView(mContext,
				dataset, mRenderer);

		// Add the graphical view mChart object into the Linear layout .
		chart_container.addView(mChart);
	}
	
	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveWeekMeasurements extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve",
					"entere do in background of retrieve week measurement task: ");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(FTPURLOFPHPFUNCTIONS);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("function",
						GETDAYMEASUREMENT));
				urlParameters.add(new BasicNameValuePair("measuringpoint_id",
						"" + mMeasuringpointId));
				post.setEntity(new UrlEncodedFormEntity(urlParameters));
				HttpResponse response = client.execute(post);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));

					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					// System.out.println(result.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.i("WEEK DATA", result.toString());
			return result.toString();
		}

		@Override
		protected void onPostExecute(String _result) {
			super.onPostExecute(_result);
			new ParserTask().execute(_result);
		}
	}

	/**
	 * Background Parser-Task
	 */
	private class ParserTask extends
			AsyncTask<String, Void, List<HashMap<String, String>>> {

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {

			DataOneDayJSONParser dataParser = new DataOneDayJSONParser();

			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			try {
				dataList = dataParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return dataList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			String timestamp;
			String waterlevel;

			eachDay = new ArrayList<TimeWaterlevel>();

			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> parsermap = result.get(i);
				timestamp = parsermap.get("timestamp");
				waterlevel = parsermap.get("waterlevel");

				String[] dateTime = timestamp.split(" ");
				String[] date = dateTime[0].split("-");
				String day = date[2] + "." + date[1];
				boolean alreadyInTheList = false;
				for (int j = 0; j < eachDay.size(); j++) {
					if (eachDay.get(j).getTimestamp().equals(day)) {
						alreadyInTheList = true;
					}
				}
				if (!alreadyInTheList) {
					eachDay.add(new TimeWaterlevel(day, waterlevel));
				}
			}
			openChart();
		}
	}

	class TimeWaterlevel {
		String timestamp;
		String waterlevel;

		public TimeWaterlevel(String timestamp, String waterlevel) {
			super();
			this.timestamp = timestamp;
			this.waterlevel = waterlevel;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getWaterlevel() {
			return waterlevel;
		}

		public void setWaterlevel(String waterlevel) {
			this.waterlevel = waterlevel;
		}

	}
}
