package at.fhhgb.mc.wasserapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import at.fhhgb.mc.wasserapp.waterlevel.WaterLevelsActivity;
import at.fhhgb.mc.wasserapp.ActivitySwipeDetector;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeActivity.
 * Mario Baumgartner
 * NEXT STEP
 */
public class HomeActivity extends Activity implements OnClickListener {

	/** The m_markertype. */
	public static String m_markertype;
	
	/** The m_superuser. */
	public static boolean m_superuser;

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

		ViewFlipper viewFlipperHome = (ViewFlipper)findViewById(R.id.viewflipperHome);		
		
		ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(viewFlipperHome, this);
		findViewById(R.id.rootActionbar).setOnTouchListener(activitySwipeDetector);
		


		
		Button actionBarButton = (Button)findViewById(R.id.b_home);
		actionBarButton.setPressed(true);

		setAllButtonListener((ViewGroup) findViewById(R.id.rootActionbar), this);
		setPositionToMark(this);
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

		case R.id.b_fountain:
			i = new Intent();
			Intent map_fontain;
			map_fontain = new Intent();
			map_fontain = new Intent(this, MapActivity.class);
			map_fontain.putExtra("user", false);
			map_fontain.putExtra("m_markertype", "fountain");
			startActivity(map_fontain);

			break;
		case R.id.b_wc:
			i = new Intent();
			Intent map_wc;
			map_wc = new Intent();
			map_wc = new Intent(this, MapActivity.class);
			map_wc.putExtra("user", false);
			map_wc.putExtra("m_markertype", "wc");
			startActivity(map_wc);
			break;
		case R.id.b_waterlevel:
			i = new Intent(this, WaterLevelsActivity.class);
			break;
		case R.id.b_precipitation:
			i = new Intent();
			// i = new Intent(this, PrecipitationActivity.class);
			break;
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

}
