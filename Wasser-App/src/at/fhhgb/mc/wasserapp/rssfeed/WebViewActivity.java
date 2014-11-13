package at.fhhgb.mc.wasserapp.rssfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.waterlevel.WaterLevelsActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class WebViewActivity.
 */
public class WebViewActivity extends Activity implements OnClickListener,OnTouchListener {

	/** The url. */
	private String url = "http://www.ooewasser.at/de/wir-ueber-uns/info-center/ooe-wasser-nachrichten.html";
	
	/** The webview. */
	private WebView webview;

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(7);
		vf.setOnTouchListener(this);
		overridePendingTransition(0, 0);
		HomeActivity.setPositionToMark(this);

		Button actionBarButton = (Button)findViewById(R.id.b_news);
		actionBarButton.setPressed(true);

		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);

		final ProgressBar pbar = (ProgressBar) findViewById(R.id.pb1);
		webview = (WebView) findViewById(R.id.webview_oowasser);
		webview.setWebViewClient(new WebViewClient());
		webview.setScrollX(600);
		webview.setScrollY(500);
		webview.getSettings().setBuiltInZoomControls(true);

		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress < 100 && pbar.getVisibility() == ProgressBar.GONE) {
					pbar.setVisibility(ProgressBar.VISIBLE);
				}
				pbar.setProgress(progress);
				if (progress == 100) {
					pbar.setVisibility(ProgressBar.GONE);
				}
			}
		});

		webview.loadUrl(url);

	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		HomeActivity.setPositionToMark(this);
		Button actionBarButton = (Button)findViewById(R.id.b_news);
		actionBarButton.setPressed(true);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {
		Intent i;
		switch (_button.getId()) {
		//Actionbar
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
			//End Actionbar
		default :
			i = new Intent();
		}
		if(i != null && i.getComponent() != null){
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);	
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int yPos = webview.getScrollY();
			Log.i("Scroll", "yPos = " + yPos);
		}
		return false;
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
