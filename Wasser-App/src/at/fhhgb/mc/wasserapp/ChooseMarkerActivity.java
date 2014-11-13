/*
 * 
 */
package at.fhhgb.mc.wasserapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.RssActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;
import at.fhhgb.mc.wasserapp.waterlevel.WaterLevelsActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class ChooseMarkerActivity.
 */
public class ChooseMarkerActivity extends Activity implements OnClickListener {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(1);
		overridePendingTransition(0, 0);
		
		Button actionBarButton = (Button)findViewById(R.id.b_position);
		actionBarButton.setPressed(true);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		HomeActivity.setPositionToMark(this);
		
		Button actionBarButton = (Button)findViewById(R.id.b_position);
		actionBarButton.setPressed(true);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {

		Intent i;
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

		case R.id.b_mark_fountain:
			i = new Intent();
			Intent map_fontain;
			map_fontain = new Intent();
			map_fontain = new Intent(this, MapActivity.class);
			map_fontain.putExtra("user", true);
			map_fontain.putExtra("m_markertype", "fountain");
			startActivity(map_fontain);

			break;
		case R.id.b_mark_wc:
			i = new Intent();
			Intent map_wc;
			map_wc = new Intent();
			map_wc = new Intent(this, MapActivity.class);
			map_wc.putExtra("user", true);
			map_wc.putExtra("m_markertype", "wc");
			startActivity(map_wc);
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
		Intent i = new Intent(this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
	}
}
