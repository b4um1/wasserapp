package at.fhhgb.mc.wasserapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.RssActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;
import at.fhhgb.mc.wasserapp.waterlevel.MyService;
import at.fhhgb.mc.wasserapp.waterlevel.WaterLevelsActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeActivity.
 */
public class HomeActivity extends FragmentActivity implements OnClickListener, ActionBar.TabListener {

	/** The m_markertype. */
	public static String m_markertype;
	
	/** The m_superuser. */
	public static boolean m_superuser;
	
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "PageOne", "PageTwo" };

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
			return;
		}
		
		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(0);
		overridePendingTransition(0, 0);

		Button actionBarButton = (Button)findViewById(R.id.b_home);
		actionBarButton.setPressed(true);
		
		setAllButtonListener((ViewGroup) findViewById(R.id.rootActionbar), this);
		setPositionToMark(this);
		
		// Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
    	actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
  
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
         
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         	 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
  
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
	}

	/**
	 * Sets the position to mark.
	 *
	 * @param a the new position to mark
	 */
	static public void setPositionToMark(Activity a) {
		if (LoginActivity.superUser) {
			Button b = (Button) a.findViewById(R.id.b_position);
			b.setText(a.getString(R.string.actionbar_mark));
		}
	}

	/**
	 * Sets the all button listener.
	 *
	 * @param viewGroup the view group
	 * @param _listener the _listener
	 */
	static public void setAllButtonListener(ViewGroup viewGroup,
			OnClickListener _listener) {
		View v;
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				setAllButtonListener((ViewGroup) v, _listener);
			} else if (v instanceof Button) {
				((Button) v).setOnClickListener(_listener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		setPositionToMark(this);
		Button actionBarButton = (Button)findViewById(R.id.b_home);
		actionBarButton.setPressed(true);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {
		Intent i;
		_button.setPressed(true);
		switch (_button.getId()) {
		// Actionbar
		case R.id.b_home:
			i = new Intent(this, HomeActivity.class);
			break;
		case R.id.b_position:
			i = new Intent();
			Intent map;
			if (LoginActivity.superUser) {
				map = new Intent(this, ChooseMarkerActivity.class);
				map.putExtra("user", true);
			} else {
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
		// End Actionbar
		default:
			i = new Intent();
		}
		if (i != null && i.getComponent() != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finishAffinity();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
