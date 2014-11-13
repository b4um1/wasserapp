/*
 * 
 */
package at.fhhgb.mc.wasserapp.more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.R.layout;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.rssfeed.RssActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class MoreActivity.
 */
public class MoreActivity extends Activity implements OnClickListener {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(8);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		
		Button actionBarButton = (Button)findViewById(R.id.b_more);
		actionBarButton.setPressed(true);
		
		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
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
		Button actionBarButton = (Button)findViewById(R.id.b_more);
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

		case R.id.b_login:
			if (haveNetworkConnection()) {
				i = new Intent(this, LoginActivity.class);
			} else {
				i = new Intent();
			}
			break;
		case R.id.b_about:
			i = new Intent(this, AboutActivity.class);
			break;
		default:
			i = new Intent();
		}
		if (i != null && i.getComponent() != null) {
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}
	}

	/**
	 * Have network connection.
	 *
	 * @return true, if successful
	 */
	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		if (!(haveConnectedWifi || haveConnectedMobile)) {
			Toast.makeText(this,
					getString(R.string.waterlevel_turn_on_internet),
					Toast.LENGTH_LONG).show();
		}

		return haveConnectedWifi || haveConnectedMobile;
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
