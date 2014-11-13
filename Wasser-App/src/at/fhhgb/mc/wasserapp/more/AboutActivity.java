
package at.fhhgb.mc.wasserapp.more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R.id;
import at.fhhgb.mc.wasserapp.R.layout;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.rssfeed.RssActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class AboutActivity.
 */
public class AboutActivity extends Activity implements OnClickListener {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_about);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(9);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		TextView publisher = (TextView) findViewById(R.id.tv_about_publisher);
		publisher
				.setText("Amt der OOe Landesregierung Direktion Umwelt und Wasserwirtschaft "
						+ "Abteilung Grund- und Trinkwasserwirtschaft Dienststelle OOe "
						+ "WASSER\nKaertnerstrasse 10 - 12, 4021 Linz\nTel.: (+43 732) "
						+ "7720-14030\nFax: (+43 732) 7720-214008\nE-Mail: "
						+ "ooewasser@ooe.gv.at\nwww.oewasser.at");

		TextView programmers = (TextView) findViewById(R.id.tv_about_programmers);
		programmers
				.setText("Dan Neatu, Eduard Berbecaru,\nThomas Kranzer, Mario Baumgartner");
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

		case R.id.b_login:
			i = new Intent(this, LoginActivity.class);
			break;
		case R.id.b_about:
			i = new Intent(this, AboutActivity.class);
			break;
		case R.id.b_back:
			i = new Intent();
			onBackPressed();
			break;
		default:
			i = new Intent();
		}
		if (i != null && i.getComponent() != null) {
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			finish();
			startActivity(i);
		}
	}
}
