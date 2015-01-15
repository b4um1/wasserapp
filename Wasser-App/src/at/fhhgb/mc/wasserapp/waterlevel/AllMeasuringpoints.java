package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.R.id;
import at.fhhgb.mc.wasserapp.R.layout;
import at.fhhgb.mc.wasserapp.R.menu;

public class AllMeasuringpoints extends Activity {

	private final String USER_AGENT = "Mozilla/5.0";
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_measuringpoints);

		
		getAllMeasuringpoints(); // + fill the list
		
		
	}
	
	public void displayMeasuringpoints(ArrayList<String> _names) {
		
		AllMeasuringpointsArrayAdapter adapter = new AllMeasuringpointsArrayAdapter(this, R.layout.list_layout, _names);
		
		ListView v = (ListView) findViewById(R.id.container_rivers);
		v.setAdapter(adapter);
	}
	
	
	public void getAllMeasuringpoints() {
		new RetrieveTask().execute();
	}
	
	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve", "entere do in background of retrieve task: ");

			String url = "http://wasserapp.reecon.eu/rivers.php";

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters
						.add(new BasicNameValuePair("function", "getAllMeasuringpoints"));
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
					//System.out.println(result.toString());
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

			MeasuringpointsJSONParser measuringpointsParser = new MeasuringpointsJSONParser();

			List<HashMap<String, String>> measuringpointList = new ArrayList<HashMap<String, String>>();
			try {
				measuringpointList = measuringpointsParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return measuringpointList;
		}
		
		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			ArrayList<String> names = new ArrayList<String>();
			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> parsermap = result.get(i);
				String ret = parsermap.get("measuringpointName");
				names.add(ret);
			}
			displayMeasuringpoints(names);
		}
	}
}
