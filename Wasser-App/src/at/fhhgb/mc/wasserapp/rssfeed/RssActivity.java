/*
 * 
 */
package at.fhhgb.mc.wasserapp.rssfeed;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class RssActivity.
 */
public class RssActivity extends Activity implements OnClickListener {
	
	/** The m_vf. */
	private ViewFlipper m_vf;

	/** The m_b_home. */
	private Button m_b_home;
	
	/** The m_b_position. */
	private Button m_b_position;
	
	/** The m_b_more. */
	private Button m_b_more;
	
	/** The Constant RSS_DOWNLOAD_REQUEST_CODE. */
	private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;
	
	/** The url. */
	private String url = "http://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";
	
	/** The log tag. */
	private final String LOG_TAG = "RssActivity";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		m_vf = (ViewFlipper) findViewById(R.id.viewflipper);
		m_vf.setDisplayedChild(7);
		overridePendingTransition(0, 0);

		m_b_home = (Button) findViewById(R.id.b_home);
		m_b_home.setOnClickListener(this);
		m_b_position = (Button) findViewById(R.id.b_position);
		m_b_position.setOnClickListener(this);
		m_b_more = (Button) findViewById(R.id.b_more);
		m_b_more.setOnClickListener(this);
		
		TextView t = (TextView)findViewById(R.id.tf_list_comment);
		t.setText(url);
		// Rssfeed

		
		Log.i(LOG_TAG, "Starting Pending Intent");
		PendingIntent pendingResult = createPendingResult(
				RSS_DOWNLOAD_REQUEST_CODE, new Intent(), 0);
		Intent intent = new Intent(getApplicationContext(),
				RssService.class);
		intent.putExtra(RssService.URL_EXTRA, url);
		intent.putExtra(RssService.PENDING_RESULT_EXTRA,
				pendingResult);
		startService(intent);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {
		Intent markObject = new Intent(this, MapActivity.class);

		if (_button.getId() == m_b_position.getId()) { // Marker
			Intent i = new Intent(this, ChooseMarkerActivity.class);
			startActivity(i);
		}
		if (_button.getId() == m_b_home.getId()) { // Home
			Intent i = new Intent(this, HomeActivity.class);
			startActivity(i);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOG_TAG, "Service antwort:"+requestCode+":"+resultCode);
	    if (requestCode == RSS_DOWNLOAD_REQUEST_CODE) {
	        switch (resultCode) {
	            case RssService.INVALID_URL_CODE:
	                //handleInvalidURL();
	                break;
	            case RssService.ERROR_CODE:
	                //handleError(data);
	                break;
	            case RssService.RESULT_CODE:
	                handleRSS(data);
	                break;
	        }
	        handleRSS(data);
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Handle rss.
	 *
	 * @param data the data
	 */
	private void handleRSS(Intent data) {
        FeedRss rss = data.getParcelableExtra(RssService.RSS_RESULT_EXTRA);
        String result = "";
        for (int i=0; i<rss.size(); i++) {
        	FeedRss.Item item = rss.get(i);
            result += item.title;
        }
        EditText text = (EditText)findViewById(R.id.editText_rssfeed);
        text.setText(result);
    }

    /**
     * Handle error.
     *
     * @param data the data
     */
    private void handleError(Intent data) {
        // whatever you want
    }

    /**
     * Handle invalid url.
     */
    private void handleInvalidURL() {
        // whatever you want
    }


}
