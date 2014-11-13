/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;

// TODO: Auto-generated Javadoc
/**
 * The Class RiversActivity.
 */
public class RiversActivity extends Activity implements OnItemClickListener, OnClickListener {

	/** The m list. */
	private ArrayList<River> mList;
	
	/** The Constant CLICKED_ID. */
	private static final String CLICKED_ID = "clickedRiver";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);
		
		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(4);
		overridePendingTransition(0, 0);
		
		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		mList = new ArrayList<River>();
		
		if (haveNetworkConnection()) {
			fillArrayList();

			// fill the list
			MyArrayAdapterRivers adapter = new MyArrayAdapterRivers(getApplicationContext(), R.layout.list_layout, mList);
			
			Log.i("mList", mList.get(0).getmRiverName());

			ListView v = (ListView) findViewById(R.id.container_rivers);
			v.setOnItemClickListener(this);
			v.setAdapter(adapter);
		} else {
			finish();
		}
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
					getString(R.string.waterlevel_turn_on_internet), Toast.LENGTH_LONG).show();
		}

		return haveConnectedWifi || haveConnectedMobile;
	}

	/**
	 * fill the ArrayLists with data from the DB.
	 */
	public void fillArrayList() {
		String retrieveQuery = "";
		try {
			retrieveQuery = new RetrieveTask().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
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
		ArrayList<String> riverStringList = new ArrayList<String>();
		ArrayList<River> riverList = new ArrayList<River>();

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

		// fill the list, so that each river is in the list (only once)
		for (int i = 0; i < mpointList.size(); i++) {
			if (!(riverStringList.contains(mpointList.get(i).getmRiverName()))) {
				riverStringList.add(mpointList.get(i).getmRiverName());
				riverList.add(new River(mpointList.get(i).getmRiverName()));
			}
		}

		ArrayList<MeasuringPoint> mpList = new ArrayList<MeasuringPoint>();

		// add to the final ArrayLists
		for (int i = 0; i < riverStringList.size(); i++) {
			for (int j = 0; j < mpointList.size(); j++) {
				if (riverStringList.get(i).equals(
						mpointList.get(j).getmRiverName())) {
					mpList.add(mpointList.get(j));
				}
			}
			mList.add(new River(riverStringList.get(i), mpList));
			mpList = new ArrayList<MeasuringPoint>();
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(this, MeasuringPointActivity.class);
		i.putExtra(CLICKED_ID, id);
		i.putExtra("ArrayList", mList);
		startActivity(i);
	}

	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		/* (non-Javadoc)
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
			if (LoginActivity.superUser) {
				map = new Intent(this, ChooseMarkerActivity.class);
				map.putExtra("user", true);
			} else {
				map = new Intent(this, MapActivity.class);
				map.putExtra("user", false);
				map.putExtra("m_markertype", "all");
			}
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
