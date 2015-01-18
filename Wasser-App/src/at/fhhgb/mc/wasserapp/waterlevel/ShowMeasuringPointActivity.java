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
import at.fhhgb.mc.wasserapp.waterlevel.adapter.ChartsPagerAdapter;
import at.fhhgb.mc.wasserapp.waterlevel.model.MeasuringPoint;
import at.fhhgb.mc.wasserapp.waterlevel.model.NotificationModel;
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
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowMeasuringPointActivity.
 *
 * @author Thomas Kranzer
 */
public class ShowMeasuringPointActivity extends FragmentActivity implements ActionBar.TabListener,
		Serializable, OnClickListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3925323245801208989L;
	
	/** The m mp. */
	private MeasuringPoint mMp;

	/** The m_tb_notification. */
	private ToggleButton m_tb_notification;
	
	/** The et_notification. */
	private EditText et_notification;

	/** The m_notification_list. */
	public ArrayList<NotificationModel> m_notification_list;

	
	private ViewPager viewPager;
	private ChartsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "One Day", "Seven Days" };
	
	LinearLayout llOne;
	LinearLayout llTwo;
	
	CheckBox cb;

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
		tv.setText(mMp.getmWaterlevel() + " cm");
		
		
		String[] dateTime = mMp.getmTimestamp().split(" ");
		String[] date = dateTime[0].split("-");
		String[] time = dateTime[1].split(":");
		tv = (TextView) findViewById(R.id.tv_time);
		tv.setText(date[2] + "." + date[1] + "." + date[0] + " / " + time[0] + ":" + time[1] + " Uhr");

		
		llOne = (LinearLayout) findViewById(R.id.waterlevel_page_one);
		llTwo = (LinearLayout) findViewById(R.id.waterlevel_page_two);
		
		// Initilization
        viewPager = (ViewPager) findViewById(R.id.pager_wl);
        actionBar = getActionBar();
        mAdapter = new ChartsPagerAdapter(getSupportFragmentManager(), mMp.getmMeasuringPointId());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.hide();
        
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                if (position == 0) {
            		llOne.setVisibility(View.VISIBLE);
            		llTwo.setVisibility(View.GONE);
                } else {
            		llOne.setVisibility(View.GONE);
            		llTwo.setVisibility(View.VISIBLE);
                }
            }
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        
        cb = (CheckBox) findViewById(R.id.checkBoxNotification);
        cb.setOnClickListener(this);
        
		et_notification = (EditText) findViewById(R.id.et_waterlevels_show_measuring_point_notification_input);
		
		et_notification.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cb.setChecked(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void afterTextChanged(Editable s) {}
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
		case R.id.checkBoxNotification:
            if(cb.isChecked()){
                
				int realValue = Integer.parseInt(mMp.getmWaterlevel());

				int myValue = 0;
				if (et_notification.getText() != null && !et_notification.getText().toString().equals("")) {
					myValue = Integer.parseInt(et_notification.getText().toString());
				}
                
				if (!et_notification.getText().toString().equals("")
						&& myValue != realValue && myValue < 99999) {
					Toast.makeText(getApplicationContext(), "Benachrichtigung aktiviert!", Toast.LENGTH_LONG).show();
					
					boolean alreadyInTheList = false;
					
					for (int j = 0; j < m_notification_list.size(); j++) {
						if (mMp.getmMeasuringPointId() == m_notification_list.get(j).getmMp().getmMeasuringPointId()) {
							alreadyInTheList = true;
						}
					}
					if (!alreadyInTheList) {
						boolean isSmaller = false;
						if (myValue < realValue) {
							isSmaller = true;
						}
						m_notification_list.add(new NotificationModel(mMp, Integer.parseInt(et_notification.getText().toString()), isSmaller));
					} else {
						for (int j = 0; j < m_notification_list.size(); j++) {
							if (mMp.getmMeasuringPointId() == m_notification_list
									.get(j).getmMp().getmMeasuringPointId()) {
								m_notification_list.remove(j);
							}
						}
						boolean isSmaller = false;
						if (myValue < realValue) {
							isSmaller = true;
						}
						m_notification_list.add(new NotificationModel(mMp, Integer.parseInt(et_notification.getText().toString()), isSmaller));
					}
					
					Intent serviceIntent = new Intent(this, MyService.class);
					startService(serviceIntent);
					Calendar cal = Calendar.getInstance();
					Intent intent = new Intent(this, MyService.class);
					PendingIntent pintent = PendingIntent.getService(this, 0,
							intent, 0);
//
//					AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//					// Start service every hour 3600*1000
//					alarm.setRepeating(AlarmManager.RTC_WAKEUP,
//							cal.getTimeInMillis(), 3600*1000, pintent);
					
				} else {
					cb.setChecked(false);
					for (int j = 0; j < m_notification_list.size(); j++) {
						if (mMp.getmMeasuringPointId() == m_notification_list
								.get(j).getmMp().getmMeasuringPointId()) {
							m_notification_list.remove(j);
						}
					}
//					stopService(new Intent(this, MyService.class));
				}
				FavsRepository.storeNotificationList(getApplicationContext(), m_notification_list);
            } else {
            	et_notification.setText("");
            }
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
		m_notification_list = FavsRepository.loadNotificationList(getApplicationContext());
		for (int j = 0; j < m_notification_list.size(); j++) {
			if (mMp.getmMeasuringPointId() == m_notification_list.get(j).getmMp().getmMeasuringPointId()) {
				et_notification.setText("" + String.valueOf(m_notification_list.get(j).getmNotificationValue()));
				cb.setChecked(true);
			}
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
