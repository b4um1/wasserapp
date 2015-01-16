/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ParseException;

import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.AboutActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.RssActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

import com.google.android.gms.drive.internal.f;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.preference.PreferenceManager;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;

// TODO: Auto-generated Javadoc
/**
 * The Class WaterLevelsActivity.
 *
 * @author Thomas Kranzer
 */
public class WaterLevelsActivity extends Activity implements OnClickListener,
		OnItemClickListener, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7331219460096596757L;

	/** The m list favs. */
	private ArrayList<MeasuringPoint> mListFavs;

	private int mCounter = 0;

	private final String GETLATESTMEASUREMENT = "getLatestMeasurmentById";
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/rivers.php";

	private final String USER_AGENT = "Mozilla/5.0";
	
	ListView v;
	OnItemClickListener onItemClickListener;
	
	//static TextView tvFav;
	static LinearLayout ll1;
	static LinearLayout ll2;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_water_levels);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(3);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);
		
		onItemClickListener = this;

		if (haveNetworkConnection()) {
			mListFavs = new ArrayList<MeasuringPoint>();
			mListFavs = FavsRepository.loadFavs(getApplicationContext());
			
			
			//tvFav = (TextView) findViewById(R.id.tv_addFav);
			ll1 = (LinearLayout) findViewById(R.id.LinearLayout1);
			ll2 = (LinearLayout) findViewById(R.id.LinearLayout2);
			if (mListFavs.isEmpty()) {
				setInfoText();
			} else {
				setHeaderText();
			}
			updateFavs();
		} else {
			finish();
		}
	}
	
	public static void setInfoText() {
		ll1.setVisibility(View.GONE);
		ll2.setVisibility(View.VISIBLE);
	}
	
	public static void setHeaderText() {
		ll1.setVisibility(View.VISIBLE);
		ll2.setVisibility(View.GONE);
	}

	/**
	 * This method checks if the app is connected to the internet Then it
	 * returns true, else it returns false.
	 * 
	 * @return boolean
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

	/**
	 * Update favs.
	 */
	public void updateFavs() {
		for (int i = 0; i < mListFavs.size(); i++) {
			new RetrieveLatestMeasurement().execute();
		}
	}

	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveLatestMeasurement extends
			AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve",
					"entere do in background of retrieve all rivers task: ");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(FTPURLOFPHPFUNCTIONS);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("function",
						GETLATESTMEASUREMENT));
				urlParameters.add(new BasicNameValuePair("measuringpoint_id",
						"" + mListFavs.get(mCounter++).getmMeasuringPointId()));
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

			WaterlevelJSONParser waterlevelParser = new WaterlevelJSONParser();

			List<HashMap<String, String>> waterlevelList = new ArrayList<HashMap<String, String>>();
			try {
				waterlevelList = waterlevelParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return waterlevelList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			if ((result != null) && (result.size() != 0)) {
				
				HashMap<String, String> parsermap = result.get(0);
				String measuringpointId = parsermap.get("measuringpointId");
				String waterlevel = parsermap.get("waterlevel");
	
				for (int i = 0; i < mListFavs.size(); i++) {
					if (mListFavs.get(i).getmMeasuringPointId() == Integer.parseInt(measuringpointId)) {
						mListFavs.get(i).setmWaterlevel(waterlevel);
					}
				}
			}
			FavsRepository.storeFavs(getApplicationContext(), mListFavs);
			MyArrayAdapterWaterlevel adapter = new MyArrayAdapterWaterlevel(
					getApplicationContext(), R.layout.list_waterlevel,
					mListFavs);
			v = (ListView) findViewById(R.id.container_waterlevel);
			v.setOnItemClickListener(onItemClickListener);
			v.setAdapter(adapter);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	public void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		HomeActivity.setPositionToMark(this);

		mCounter = 0;
		mListFavs = FavsRepository.loadFavs(getApplicationContext());
		Log.i("onResume", "onResume");
		if (mListFavs.isEmpty()) {
			setInfoText();
		} else {
			setHeaderText();
		}
		
		updateFavs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	public boolean onOptionsItemSelected(MenuItem _item) {
		Intent i = new Intent(this, RiversActivity.class);
		startActivity(i);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@SuppressLint("ShowToast")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		//loadMListFavs();
		if (mListFavs != null) {
			Intent i = new Intent(this, ShowMeasuringPointActivity.class);
			MeasuringPoint mp = mListFavs.get((int) id);
			i.putExtra("measuringpoint", mp);
			startActivity(i);
		} else {
			mListFavs = new ArrayList<MeasuringPoint>();
			Toast.makeText(getApplicationContext(), "Error", 2000).show();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {
		Intent i = new Intent();

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

		case R.id.fl_fav_header:
			i = new Intent(this, RiversActivity.class);
			break;
		case R.id.b_back:
			onBackPressed();
			break;
		}
		if (i != null && i.getComponent() != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}
	}
}
