/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity.detail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.R.id;
import at.fhhgb.mc.wasserapp.R.layout;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.mapactivity.MarkerObject;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class FountainDetailsActivity.
 */
public class FountainDetailsActivity extends Activity implements OnClickListener {

	/** The m_marker_address. */
	private String m_marker_address;
	
	/** The m_marker_lat lng. */
	private String m_marker_latLng;
	
	/** The tv_address. */
	private TextView tv_address;
	
	/** The tv_lat lng. */
	private TextView tv_latLng;
	
	/** The m_marker_lat. */
	private String m_marker_lat;
	
	/** The m_marker_lng. */
	private String m_marker_lng;
	
	/** The m_comment. */
	private String m_comment;
	
	/** The m_rating. */
	private String m_rating;
	
	/** The tv_lat. */
	private TextView tv_lat;
	
	/** The tv_lng. */
	private TextView tv_lng;
	
	/** The bt_send. */
	private Button bt_send;
	
	/** The adapter. */
	private ArrayAdapterDetail adapter;
	
	/** The list_of_comments. */
	private ArrayList<Comment> list_of_comments = new ArrayList<Comment>();
	
	/** The m_bool. */
	private boolean m_bool;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(11);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		bt_send = (Button) findViewById(R.id.btn_send);
		bt_send.setOnClickListener(this);

		
		Intent i = getIntent();
		m_marker_address = i.getStringExtra("address");
		m_marker_lat = i.getStringExtra("lat");
		m_marker_lng = i.getStringExtra("lng");
		m_bool = i.getBooleanExtra("bool", false);
		if(m_bool){
			TextView tv_drinkable = (TextView) findViewById(R.id.tv_drinkable);
			tv_drinkable.setText("trinkbar");
			Log.e("bool", "true");
			
		}else{
			TextView tv_drinkable = (TextView) findViewById(R.id.tv_drinkable);
			tv_drinkable.setText("nicht trinkbar");
			Log.e("bool", "false");
		}
		tv_address = (TextView) findViewById(R.id.tv_address);
		Log.e("latlng", "" + tv_latLng);
		tv_address.setText(m_marker_address);

		//parse MySQL query
		parseJson();
		
		adapter = new ArrayAdapterDetail(this, R.layout.list_comments, list_of_comments);
		ListView v = (ListView) findViewById(R.id.lv_comments);
		v.setAdapter(adapter);
		
	}

	/**
	 * Parses the JSON Stream in single Comment-Objects ann puts in into a arraylist - list_of_comments<Comment>.
	 */
	private void parseJson() {
		list_of_comments.removeAll(list_of_comments);
		String comments = "";
		try {
			comments = new RetrieveTaskComments().execute().get();
			Log.i("formdetails", comments);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray jComments = null;
		JSONObject json = null;
		Comment comm;
		try {
			json = new JSONObject(comments);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			/** Retrieves all the elements in the 'markers' array */
			jComments = json.getJSONArray("comments");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		int commCount = jComments.length();
		/** Taking each marker, parses and adds to list object */
		for(int i1=0; i1<commCount;i1++){
			try {
				/** Call getMarker with marker JSON object to parse the marker */
				comm = getComment((JSONObject) jComments.get(i1));
				list_of_comments.add(comm);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * Gets the Comment-Object from the single JSON-Object.
	 *
	 * @param JSONObject jComment
	 * @return Comment c
	 */
	private Comment getComment(JSONObject jComment) {

		Comment comm = new Comment();

		try {
			// Extracting comment, if available
			if (!jComment.isNull("comment")) {
				comm.setComment(jComment.getString("comment"));
			}

			// Extracting comment, if available
			if (!jComment.isNull("rating")) {
				comm.setRating(jComment.getString("rating"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return comm;
	}

	/**
	 * The Class RetrieveTaskComments.
	 */
	private class RetrieveTaskComments extends AsyncTask<Void, Void, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			Log.e("retrieve", "do in background of retrieve task: ");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=retrieveComments";
			URL url = null;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());

				outputStreamWriter.write("lat=" + m_marker_lat + "&lng="
						+ m_marker_lng);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				Log.d("url", url.toString());

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("stringbuffer", sb.toString());

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

	// Background thread to save the location in remote MySQL server
	/**
	 * The Class SaveTask.
	 */
	private class SaveTask extends AsyncTask<Void, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(Void... params) {

			Log.d("send", "send object to database");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=saveComment";
			URL url = null;
			try {
				url = new URL(strUrl);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());
				outputStreamWriter.write("lat=" + m_marker_lat + "&lng="
						+ m_marker_lng + "&comment=" + m_comment + "&rating="
						+ m_rating);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				StringBuffer sb = new StringBuffer();

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("save sb:", sb.toString());

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		EditText tf = (EditText) findViewById(R.id.tf_comment);
		m_comment = tf.getText().toString();
//		Toast.makeText(getApplicationContext(), "Kommentar wurde gespeichert",
//				Toast.LENGTH_SHORT).show();
		tf.setText("");
		m_rating = "5";
		new SaveTask().execute();
		parseJson();
		adapter.notifyDataSetChanged();
		
		Intent i = new Intent();
		switch (v.getId()) {

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
			i = new Intent();
			onBackPressed();
			break;
		}
		if(i != null && i.getComponent() != null){
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);	
		}
	}
}
