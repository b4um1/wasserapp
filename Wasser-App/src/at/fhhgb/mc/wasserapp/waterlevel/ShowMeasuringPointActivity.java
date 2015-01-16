/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowMeasuringPointActivity.
 *
 * @author Thomas Kranzer
 */
public class ShowMeasuringPointActivity extends Activity implements
		Serializable, OnClickListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3925323245801208989L;
	
	/** The m chart. */
	private GraphicalView mChart;
	
	/** The m day. */
	private String[] mDay = new String[] { "7", "6", "5", "4", "3", "2", "1",
			"0" };
	
	/** The m mp. */
	private MeasuringPoint mMp;

	/** The m_tb_notification. */
	private ToggleButton m_tb_notification;
	
	/** The et_notification. */
	private EditText et_notification;

	/** The m_notification_list. */
	public ArrayList<NotificationModel> m_notification_list;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(6);
		overridePendingTransition(0, 0);
		
		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		Bundle bundle = getIntent().getExtras();

		mMp = (MeasuringPoint) bundle.getSerializable("measuringpoint");

		TextView title = (TextView) findViewById(R.id.tv_waterlevels_show_measuring_point_title);
		title.setText(mMp.getmMeasuringPointName() + ", " + mMp.getmRiverName());

		TextView tv = (TextView) findViewById(R.id.tv_waterlevels_show_measuring_point_river_measuring_point);
		tv.setText(mMp.getmMeasuringPointName() + ", " + mMp.getmRiverName());

		tv = (TextView) findViewById(R.id.tv_waterlevels_show_measuring_point_water_level_content);
		//tv.setText("" + mMp.getmWaterlevel() + " m");

		openChart();

		m_tb_notification = (ToggleButton) findViewById(R.id.toggleButton1);
		m_tb_notification.setEnabled(false);
		m_tb_notification.setOnClickListener(this);

		et_notification = (EditText) findViewById(R.id.et_waterlevels_show_measuring_point_notification_input);
		et_notification.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

				//float realValue = mMp.getmWaterlevel();

				float myValue = 0;
				if (et_notification.getText() != null && !et_notification.getText().toString().equals("")) {
					myValue = Float.parseFloat(et_notification.getText().toString());
				}

//				if (!et_notification.getText().toString().equals("")
//						&& myValue > realValue) {
//					m_tb_notification.setEnabled(true);
//				} else {
//					m_tb_notification.setEnabled(false);
//				}

			}
		});

		m_notification_list = new ArrayList<NotificationModel>();

		setNotificationState();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		HomeActivity.setPositionToMark(this);
	}

	/**
	 * draws the line chart.
	 */
	private void openChart() {
		// Define the number of elements you want in the chart.
		int z[] = { 0, 1, 2, 3, 4, 5, 6, 7 };

		// TODO: FILL THE ARRAY WITH VALUES FROM THE DB !!!!
		//float x[] = { 4, 2, 3, 3.6f, 4, 2, 3.5f, mMp.getmWaterlevel() };

		// Create XY Series for X Series.
		XYSeries xSeries = new XYSeries("X Series");

		// Adding data to the X Series.
//		for (int i = 0; i < z.length; i++) {
//			xSeries.add(z[i], x[i]);
//		}

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

		// mRenderer.setYLabelsPadding();

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

		LinearLayout chart_container = (LinearLayout) findViewById(R.id.Chart_layout);

		// Creating an intent to plot line chart using dataset and
		// multipleRenderer

		mChart = (GraphicalView) ChartFactory.getLineChartView(
				getBaseContext(), dataset, mRenderer);

		// Add the graphical view mChart object into the Linear layout .
		chart_container.addView(mChart);

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {
		Intent i = new Intent();

		switch (_button.getId()) {
		//Actionbar
		case R.id.b_home:
			i = new Intent(this, HomeActivity.class);
			break;
		case R.id.b_position:
			i = new Intent();
			Intent map;
			if(LoginActivity.superUser){
				map = new Intent(this, ChooseMarkerActivity.class);
				map.putExtra("user", true);
			} else{
				map = new Intent(this, MapActivity.class);
				map.putExtra("user", false);	
				map.putExtra("m_markertype", "all");
			}
			startActivity(map);
			break;
		case R.id.b_news:
			i = new Intent(this, WebViewActivity.class);
			break;
		case R.id.b_more:
			i = new Intent(this, MoreActivity.class);
			break;
			//End Actionbar
			
		case R.id.b_back:
			onBackPressed();
			break;
		case R.id.toggleButton1:
			loadNotificationList();
			if (m_tb_notification.isChecked()) {
				boolean alreadyInTheList = false;
				if (!(et_notification.getText().toString().equals(""))) {
					for (int j = 0; j < m_notification_list.size(); j++) {
						if ((mMp.getmRiverName().equals(m_notification_list
								.get(j).getmMp().getmRiverName()))
								&& (mMp.getmMeasuringPointName()
										.equals(m_notification_list.get(j)
												.getmMp()
												.getmMeasuringPointName()))) {
							alreadyInTheList = true;
						}
					}
					if (!alreadyInTheList) {
						m_notification_list.add(new NotificationModel(mMp,
								Float.parseFloat(et_notification.getText()
										.toString())));
					}
				}

				Log.i("toggle", "isChecked true");
				Intent serviceIntent = new Intent(this, MyService.class);
				startService(serviceIntent);
				Calendar cal = Calendar.getInstance();
				Intent intent = new Intent(this, MyService.class);
				PendingIntent pintent = PendingIntent.getService(this, 0,
						intent, 0);

				AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				// Start service every hour 3600*1000
				alarm.setRepeating(AlarmManager.RTC_WAKEUP,
						cal.getTimeInMillis(), 3600*1000, pintent);
			} else {
				Log.i("toggle", "isChecked false");

				for (int j = 0; j < m_notification_list.size(); j++) {
					if ((mMp.getmRiverName().equals(m_notification_list.get(j)
							.getmMp().getmRiverName()))
							&& (mMp.getmMeasuringPointName()
									.equals(m_notification_list.get(j).getmMp()
											.getmMeasuringPointName()))) {
						m_notification_list.remove(j);
					}
				}
				stopService(new Intent(this, MyService.class));
			}
			storeNotificationList();

			break;
		}
		if(i != null && i.getComponent() != null){
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			finish();
			startActivity(i);
		}
	}

	/**
	 * Sets the notification state.
	 */
	public void setNotificationState() {
		loadNotificationList();
		for (int j = 0; j < m_notification_list.size(); j++) {
			if ((mMp.getmRiverName().equals(m_notification_list.get(j).getmMp()
					.getmRiverName()))
					&& (mMp.getmMeasuringPointName().equals(m_notification_list
							.get(j).getmMp().getmMeasuringPointName()))) {
				et_notification.setText(""
						+ String.valueOf(m_notification_list.get(j)
								.getmNotificationValue()));
				m_tb_notification.setChecked(true);
			}
		}

	}

	/**
	 * Load notification list.
	 */
	public void loadNotificationList() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = prefs.getString("notification", "");
		Type type = new TypeToken<ArrayList<NotificationModel>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			m_notification_list = gson.fromJson(json, type);
		} else {
			m_notification_list = new ArrayList<NotificationModel>();
		}
	}

	/**
	 * Store notification list.
	 */
	public void storeNotificationList() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(m_notification_list);
		editor.putString("notification", json);
		editor.commit();
	}

}
