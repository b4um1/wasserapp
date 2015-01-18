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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
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
	private boolean m_attribute;
	private String m_type;
	private String m_marker_id;
	
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/marker.php";
	private final String USER_AGENT = "Mozilla/5.0";
	
	
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
		m_attribute = i.getBooleanExtra("attribute", false);
		m_type = i.getStringExtra("type");
		m_marker_id = i.getStringExtra("marker_id");
		
		Log.i("FountainDetails",m_type);
		
		TextView tv_title = (TextView) findViewById(R.id.tv_details_title);
		ImageView img_view = (ImageView) findViewById(R.id.img_detailsview);
		TextView tv_drinkable_accessible = null;
		
		
		if (m_type.equals("fountain")){
			tv_drinkable_accessible = (TextView) findViewById(R.id.tv_drinkable);
			tv_title.setText("Brunnendetails");
			img_view.setImageResource(R.drawable.pic_fountain);
			
			
		}else{
			if (m_type.equals("toilet")){
				tv_drinkable_accessible = (TextView) findViewById(R.id.tv_details_drinkable_image);
				tv_drinkable_accessible.setVisibility(View.GONE);
				tv_drinkable_accessible = (TextView) findViewById(R.id.tv_details_accessible_image);
				tv_drinkable_accessible.setVisibility(View.VISIBLE);
				tv_title.setText("Toilettendetails");
				img_view.setImageResource(R.drawable.pic_wc);
			}else{
				tv_title.setText("Heilquellendetails");
			}
		}
		tv_drinkable_accessible = (TextView) findViewById(R.id.tv_drinkable);
		if(m_attribute){
			tv_drinkable_accessible.setText("Ja");
			Log.e("bool", "true");
			
		}else{
			tv_drinkable_accessible.setText("Nein");
			Log.e("bool", "false");
		}
		tv_address = (TextView) findViewById(R.id.tv_address);
		Log.e("latlng", "" + tv_latLng);
		tv_address.setText(m_marker_address);

		//parse MySQL query
		new RetrieveTaskComments().execute();
		
		adapter = new ArrayAdapterDetail(this, R.layout.list_comments, list_of_comments);
		ListView v = (ListView) findViewById(R.id.lv_comments);
		v.setAdapter(adapter);
		
	}

	/**
	 * Parses the JSON Stream in single Comment-Objects ann puts in into a arraylist - list_of_comments<Comment>.
	 */
	private void parseJson(String _result) {
		list_of_comments.removeAll(list_of_comments);
		String comments = _result;
		
		/*JSONArray jComments = null;
		JSONObject json = null;
		Comment comm;
		try {
			json = new JSONObject(comments);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			// Retrieves all the elements in the 'markers' array 
			jComments = json.getJSONArray("comments");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		int commCount = jComments.length();
		// Taking each marker, parses and adds to list object 
		for(int i1=0; i1<commCount;i1++){
			try {
				// Call getMarker with marker JSON object to parse the marker 
				comm = getComment((JSONObject) jComments.get(i1));
				list_of_comments.add(comm);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}
		*/
		
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

		@Override
		protected String doInBackground(Void... params) {
			Log.i("Retrieve comments of", m_type + ": "+m_marker_id);

			String url = FTPURLOFPHPFUNCTIONS;

			HttpParams httpParameters = new BasicHttpParams();
			HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
			HttpProtocolParams
					.setHttpElementCharset(httpParameters, HTTP.UTF_8);
			
			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("function", "getAllCommentsOfAMarker"));
				urlParameters.add(new BasicNameValuePair("marker_table", m_type)); //fountain, toilet, healingspring
				urlParameters.add(new BasicNameValuePair("marker_id", m_marker_id));
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
					System.out.println(result.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result.toString();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			parseJson(result);
		}
	}

	/**
	 * This Task saves one comment into remote db
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
		/*EditText tf = (EditText) findViewById(R.id.tf_comment);
		m_comment = tf.getText().toString();
//		Toast.makeText(getApplicationContext(), "Kommentar wurde gespeichert",
//				Toast.LENGTH_SHORT).show();
		tf.setText("");
		m_rating = "5";
		new SaveTask().execute();
		parseJson();
		adapter.notifyDataSetChanged();
		*/
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
