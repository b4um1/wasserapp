/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.preference.PreferenceManager;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;


// TODO: Auto-generated Javadoc
/**
 * The Class MeasuringPointActivity.
 *
 * @author Thomas Kranzer
 */
public class MeasuringPointActivity extends Activity implements Serializable,
OnItemClickListener, OnClickListener {

	/** The Constant serialVersionUID. */
	//private static final long serialVersionUID = -2880774401199368291L;
	
	/** The m list. */
	private ArrayList<MeasuringPoint> mList;
	
	/** The m clicked id. */
	private int mRiverId = -1;
	private String mRiverName = "";
	
	private final String GETMEASURINGPOINTS = "getAllMeasuringpoints";
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/rivers.php";
	private final String USER_AGENT = "Mozilla/5.0";
	
	/** The Constant RIVER_ID. */
	private static final String RIVER_ID = "riverId";
	private static final String RIVER_NAME = "riverName";


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(5);
		overridePendingTransition(0, 0);
		
		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);
		
		mList = new ArrayList<MeasuringPoint>();

		mRiverId = getIntent().getIntExtra(RIVER_ID, -1);
		mRiverName = getIntent().getStringExtra(RIVER_NAME);
		
		Log.i("RIVER NAME:", mRiverName);
		
		if (haveNetworkConnection()) {
			fillArrayList();
		}
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
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		ArrayList<MeasuringPoint> favsList;

		// LOAD INTERNAL DATA
		favsList = FavsRepository.loadFavs(getApplicationContext());
		
		boolean isAlreadyInTheList = false;
		for (int i = 0; i < favsList.size(); i++) {
			if (favsList.get(i).getmMeasuringPointName().equals(mList.get(position).getmMeasuringPointName())) {
				isAlreadyInTheList = true;
			}
		}

		if (!isAlreadyInTheList) {
			// UPDATE INTERNAL DATA
			favsList.add(mList.get(position));
			// STORE INTERNAL DATA
			FavsRepository.storeFavs(getApplicationContext(), favsList);
		}
		
		Intent i = new Intent(getApplicationContext(),
				WaterLevelsActivity.class);
		startActivity(i);
		finish();
		
	}

	
	public void displayMeasuringpoints() {
		MyArrayAdapterMeasuringPoint adapter = new MyArrayAdapterMeasuringPoint(
				this, R.layout.list_layout, mList);

		ListView v = (ListView) findViewById(R.id.container_measuringpoint);
		v.setOnItemClickListener(this);
		v.setAdapter(adapter);
	}
	
	public void fillArrayList() {
		new RetrieveMeasuringpoints().execute();
	}

	
	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveMeasuringpoints extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve", "entere do in background of retrieve measuringpoints task: ");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(FTPURLOFPHPFUNCTIONS);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters
						.add(new BasicNameValuePair("function", GETMEASURINGPOINTS));
				urlParameters.add(new BasicNameValuePair("river_id", "" + mRiverId));
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
			Log.i("result", _result);
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

			MeasuringpointJSONParser measuringpointParser = new MeasuringpointJSONParser();

			List<HashMap<String, String>> measuringpointList = new ArrayList<HashMap<String, String>>();
			try {
				measuringpointList = measuringpointParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return measuringpointList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			String name;
			int id;
			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> parsermap = result.get(i);
				id = Integer.parseInt(parsermap.get("measuringpointId").toString());
				name = parsermap.get("measuringpointName");
				mList.add(new MeasuringPoint(id, name, mRiverName, "-"));
			}
			displayMeasuringpoints();
		}
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
		}
		if(i != null && i.getComponent() != null){
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			finish();
			startActivity(i);
		}
	}
}
