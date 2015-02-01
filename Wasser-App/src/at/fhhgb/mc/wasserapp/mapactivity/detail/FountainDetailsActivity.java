/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity.detail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.client.ClientProtocolException;
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
import org.json.simple.parser.ParseException;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.Fountain;
import at.fhhgb.mc.wasserapp.mapactivity.Healingspring;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.mapactivity.MarkerJSONParser;
import at.fhhgb.mc.wasserapp.mapactivity.MyMarkerObject;
import at.fhhgb.mc.wasserapp.mapactivity.Toilet;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class FountainDetailsActivity.
 */
public class FountainDetailsActivity extends Activity implements
		OnClickListener {

	/** The m_marker_address. */
	private String m_marker_address;

	/** The tv_address. */
	private TextView tv_address;

	/** The tv_lat lng. */
	private TextView tv_latLng;

	/** The bt_send. */
	private Button bt_send;

	/** The adapter. */
	private ArrayAdapterDetail adapter;

	/** The list_of_comments. */
	private ArrayList<Comment> list_of_comments = new ArrayList<Comment>();

	private String m_marker_lat;
	private String m_marker_lng;

	/** The m_bool. */
	private boolean m_attribute;
	private String m_type;
	private String m_marker_id;

	private String m_newComment = "";
	private String m_newGrade = "0";

	private ImageButton btn_grade1;
	private ImageButton btn_grade2;
	private ImageButton btn_grade3;
	private ImageButton btn_remove_written_comment;

	private int m_total_grade = 0;

	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/marker.php";
	private final String USER_AGENT = "Mozilla/5.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(11);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		bt_send = (Button) findViewById(R.id.btn_send);
		bt_send.setOnClickListener(this);

		btn_grade1 = (ImageButton) findViewById(R.id.b_drop_1);
		btn_grade1.setOnClickListener(this);

		btn_grade2 = (ImageButton) findViewById(R.id.b_drop_2);
		btn_grade2.setOnClickListener(this);

		btn_grade3 = (ImageButton) findViewById(R.id.b_drop_3);
		btn_grade3.setOnClickListener(this);

		btn_remove_written_comment = (ImageButton) findViewById(R.id.b_details_removeComment);
		btn_remove_written_comment.setOnClickListener(this);

		Intent i = getIntent();
		m_marker_address = i.getStringExtra("address");
		m_marker_lat = i.getStringExtra("lat");
		m_marker_lng = i.getStringExtra("lng");
		m_attribute = i.getBooleanExtra("attribute", false);
		m_type = i.getStringExtra("type");
		m_marker_id = i.getStringExtra("marker_id");

		Log.i("FountainDetails", m_type);

		TextView tv_title = (TextView) findViewById(R.id.tv_details_title);
		TextView tv_drinkable_accessible = null;

		if (m_type.equals("fountain")) {
			tv_drinkable_accessible = (TextView) findViewById(R.id.tv_drinkable);
			tv_title.setText("Brunnendetails");

		} else {
			if (m_type.equals("toilet")) {
				tv_drinkable_accessible = (TextView) findViewById(R.id.tv_details_drinkable_image);
				tv_drinkable_accessible.setVisibility(View.GONE);
				tv_drinkable_accessible = (TextView) findViewById(R.id.tv_details_accessible_image);
				tv_drinkable_accessible.setVisibility(View.VISIBLE);
				tv_title.setText("Toilettendetails");
			} else {
				tv_title.setText("Heilquellendetails");
			}
		}
		tv_drinkable_accessible = (TextView) findViewById(R.id.tv_drinkable);
		if (m_attribute) {
			tv_drinkable_accessible.setText("Ja");
			Log.e("bool", "true");

		} else {
			tv_drinkable_accessible.setText("Nein");
			Log.e("bool", "false");
		}
		tv_address = (TextView) findViewById(R.id.tv_address);
		Log.e("latlng", "" + tv_latLng);
		tv_address.setText(m_marker_address);

		// parse MySQL query
		new RetrieveTaskComments().execute();

		adapter = new ArrayAdapterDetail(this, R.layout.list_comments,
				list_of_comments);
		ListView v = (ListView) findViewById(R.id.lv_comments);
		v.setAdapter(adapter);

	}

	private void calculateTotalGrade() {
		int size = list_of_comments.size();
		int sum_grade = 0;
		for (Comment c : list_of_comments) {
			sum_grade += c.getM_grade();
		}

		double exactgrade = ((double) sum_grade) / ((double) size);

		if (exactgrade < 0.5) {
			m_total_grade = 0;
		} else if (exactgrade >= 0.5 && exactgrade < 1.5) {
			m_total_grade = 1;
		} else if (exactgrade >= 1.5 && exactgrade < 2.5) {
			m_total_grade = 2;
		} else if (exactgrade >= 2.5 && exactgrade <= 3) {
			m_total_grade = 3;
		}

		Log.i("CALCULATION_OF_GRADE", "Exact: " + exactgrade
				+ " m_total_grade: " + m_total_grade);

		setTotalGradeinView();
	}

	private void setTotalGradeinView() {
		ImageView drop1 = (ImageView) findViewById(R.id.img_grade01);
		ImageView drop2 = (ImageView) findViewById(R.id.img_grade02);
		ImageView drop3 = (ImageView) findViewById(R.id.img_grade03);

		int grade = m_total_grade;

		Log.i("SETTINGTOTALGRADE", "" + grade);

		if (grade == 0) {
			drop1.setImageResource(R.drawable.ic_drop_0); // drop1 voll drop0
			drop2.setImageResource(R.drawable.ic_drop_0);
			drop3.setImageResource(R.drawable.ic_drop_0);
		} else if (grade == 1) {
			drop1.setImageResource(R.drawable.ic_drop_1);
			drop2.setImageResource(R.drawable.ic_drop_0);
			drop3.setImageResource(R.drawable.ic_drop_0);
		} else if (grade == 2) {
			drop1.setImageResource(R.drawable.ic_drop_1);
			drop2.setImageResource(R.drawable.ic_drop_1);
			drop3.setImageResource(R.drawable.ic_drop_0);
		} else if (grade == 3) {
			drop1.setImageResource(R.drawable.ic_drop_1);
			drop2.setImageResource(R.drawable.ic_drop_1);
			drop3.setImageResource(R.drawable.ic_drop_1);
		}
	}

	/**
	 * The Class retrieves all comments of the specific marker e.g fountain,
	 * toilet, healingspring
	 */
	private class RetrieveTaskComments extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.i("Retrieve comments of", m_type + ": " + m_marker_id);

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
				urlParameters.add(new BasicNameValuePair("function",
						"getAllCommentsOfAMarker"));
				urlParameters
						.add(new BasicNameValuePair("marker_table", m_type)); // fountain,
																				// toilet,
																				// healingspring
				urlParameters.add(new BasicNameValuePair("marker_id",
						m_marker_id));
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
			Log.i("Starting Parsing Task in onPostExecute", _result);
			new ParserTask().execute(_result);
		}
	}

	/**
	 * The ParserTask parses the json which was retrieved in RetrieveTask, and
	 * generates an array of comments - type<Comment>
	 * 
	 * @author mariobaumgartner
	 *
	 */
	private class ParserTask extends
			AsyncTask<String, Void, List<HashMap<String, String>>> {

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			CommentJSONParser commentParser = new CommentJSONParser();

			List<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
			try {
				commentList = commentParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return commentList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> _resultlist) {
			list_of_comments.removeAll(list_of_comments);
			m_total_grade = 0;
			for (int i = 0; i < _resultlist.size(); i++) {
				HashMap<String, String> parsermap = _resultlist.get(i);
				String grade = parsermap.get("grade");
				String comment = parsermap.get("comment");
				int int_grade = 0;
				if (grade!=null) {
					int_grade = Integer.parseInt(grade);
				}
				Comment c = new Comment(comment, int_grade);
				Log.i("CommentParserTask", "" + c);
				list_of_comments.add(c);
			}
			calculateTotalGrade();
			setTotalGradeinView();
		}
	}

	/**
	 * This Task saves one comment into remote db
	 */
	private class SaveTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			String url = FTPURLOFPHPFUNCTIONS;

			HttpParams httpParameters = new BasicHttpParams();
			HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
			HttpProtocolParams
					.setHttpElementCharset(httpParameters, HTTP.UTF_8);

			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);

			post.setHeader("User-Agent", USER_AGENT);

			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("function",
						"insertComment"));
				urlParameters
						.add(new BasicNameValuePair("marker_table", m_type));
				urlParameters.add(new BasicNameValuePair("marker_id",
						m_marker_id));
				urlParameters.add(new BasicNameValuePair("grade", m_newGrade));
				urlParameters.add(new BasicNameValuePair("comment",
						m_newComment));

				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						urlParameters, HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = client.execute(post);

				Log.i("SAVETASK", "" + response.getStatusLine().getStatusCode());

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {

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

		// Send Button
		case R.id.btn_send:
			EditText tf = (EditText) findViewById(R.id.tf_comment);
			m_newComment = tf.getText().toString();
			tf.setText("");
			new SaveTask().execute();

			Comment c = new Comment(m_newComment, Integer.parseInt(m_newGrade));
			list_of_comments.add(c);
			calculateTotalGrade();
			adapter.notifyDataSetChanged();
			break;

		case R.id.b_drop_1:
			m_newGrade = "1";
			btn_grade1.setImageResource(R.drawable.ic_drop_1);
			btn_grade2.setImageResource(R.drawable.ic_drop_0);
			btn_grade3.setImageResource(R.drawable.ic_drop_0);
			break;
		case R.id.b_drop_2:
			m_newGrade = "2";
			btn_grade1.setImageResource(R.drawable.ic_drop_1);
			btn_grade2.setImageResource(R.drawable.ic_drop_1);
			btn_grade3.setImageResource(R.drawable.ic_drop_0);
			break;
		case R.id.b_drop_3:
			m_newGrade = "3";
			btn_grade1.setImageResource(R.drawable.ic_drop_1);
			btn_grade2.setImageResource(R.drawable.ic_drop_1);
			btn_grade3.setImageResource(R.drawable.ic_drop_1);
			break;

		case R.id.b_details_removeComment:
			EditText comment = (EditText) findViewById(R.id.tf_comment);
			comment.setText("");

			break;
		case R.id.b_back:
			i = new Intent();
			onBackPressed();
			break;
		}
		if (i != null && i.getComponent() != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}
	}
}
