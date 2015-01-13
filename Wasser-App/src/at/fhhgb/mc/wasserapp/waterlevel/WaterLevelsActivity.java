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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.preference.PreferenceManager;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;
import at.fhhgb.mc.wasserapp.waterlevel.MyArrayAdapter;

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

	/* (non-Javadoc)
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

		if (haveNetworkConnection()) {
			mListFavs = new ArrayList<MeasuringPoint>();

//			loadMListFavs();
//			updateFavs();
//			storeMListFavs();

			MyArrayAdapter adapter = new MyArrayAdapter(
					getApplicationContext(), R.layout.list_waterlevel,
					mListFavs);

			ListView v = (ListView) findViewById(R.id.container_waterlevels);
			v.setOnItemClickListener(this);
			v.setAdapter(adapter);
		} else {
			finish();
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

	/**
	 * Update favs.
	 */
	public void updateFavs() {

		String retrieveQuery = "";
		try {
			retrieveQuery = new RetrieveTask().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		Set<String> fieldNames = new HashSet<String>();
		StajParser stajParser = null;
		stajParser = new StajParser(retrieveQuery);
		int counter = 0;
		// evaluate, how many entries are in the database
		while (stajParser.hasNext()) {
			JsonStreamElement next = stajParser.next();
			if (next.jsonStreamElementType() == JsonStreamElementType.START_FIELD) {
				fieldNames.add(next.text());
				counter++;
			}
		}

		ArrayList<MeasuringPoint> mpointList = new ArrayList<MeasuringPoint>();

		for (int i = 0; i < (counter - 1) / 3; i++) {
			try {
				String measuringpointname = new JdomParser().parse(
						retrieveQuery).getStringValue("measuringpoint", i,
						"measuringpointname");
				float waterlevel = Float.parseFloat(new JdomParser().parse(
						retrieveQuery).getStringValue("measuringpoint", i,
						"waterlevel"));
				String river = new JdomParser().parse(retrieveQuery)
						.getStringValue("measuringpoint", i, "river");

				mpointList.add(new MeasuringPoint(measuringpointname,
						waterlevel, river));

			} catch (InvalidSyntaxException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < mListFavs.size(); i++) {
			for (int j = 0; j < mpointList.size(); j++) {
				if ((mListFavs.get(i).getmRiverName().equals(mpointList.get(j)
						.getmRiverName()))
						&& (mListFavs.get(i).getmMeasuringPointName()
								.equals(mpointList.get(j)
										.getmMeasuringPointName()))) {
					mListFavs.set(i, mpointList.get(j));
					Log.i("in", mpointList.get(j).getmMeasuringPointName());
				}
			}
		}
	}

	/**
	 * The Class RetrieveTask.
	 */
	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			Log.e("retrieve", "entere do in background of retrieve task: ");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=retrieveMeasuringpoints";
			URL url = null;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	public void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		HomeActivity.setPositionToMark(this);

		// LOAD

		loadMListFavs();
		if (mListFavs != null) {

			MyArrayAdapter adapter = new MyArrayAdapter(
					getApplicationContext(), R.layout.list_waterlevel,
					mListFavs);
			ListView v = (ListView) findViewById(R.id.container_waterlevels);
			v.setAdapter(adapter);
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	public boolean onOptionsItemSelected(MenuItem _item) {
		Intent i = new Intent(this, RiversActivity.class);
		startActivity(i);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@SuppressLint("ShowToast")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		loadMListFavs();
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

	/**
	 * Load m list favs.
	 */
	public void loadMListFavs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = prefs.getString("list", "");
		Type type = new TypeToken<ArrayList<MeasuringPoint>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			mListFavs = gson.fromJson(json, type);
		} else {
			mListFavs = new ArrayList<MeasuringPoint>();
		}
	}

	/**
	 * Store m list favs.
	 */
	public void storeMListFavs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(mListFavs);
		editor.putString("list", json);
		editor.commit();
	}

	/* (non-Javadoc)
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

		case R.id.b_addFavorite:
			i = new Intent(this, AllMeasuringpoints.class);
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
