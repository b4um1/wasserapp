package at.fhhgb.mc.wasserapp.labbus;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.R.layout;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.parser.RiverJSONParser;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

public class LabbusActivity extends Activity implements OnClickListener {

	private final String GETLABBUSDATA = "getAllLaborbusDataInFuture";
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/laborbus.php";
	private final String PDFURLLABORBUS = "http://wasserapp.reecon.eu/Laborbus.pdf";
	private final String USER_AGENT = "Mozilla/5.0";

	ArrayList<Labbus> mLabbusList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(12);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		Button bMoreInfos = (Button) findViewById(R.id.b_labbus_moreinformation);
		bMoreInfos.setOnClickListener(this);

		mLabbusList = new ArrayList<Labbus>();

		new RetrieveLabbusData().execute();
	}

	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveLabbusData extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarLabbus);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve",
					"entere do in background of retrieve labbus task: ");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(FTPURLOFPHPFUNCTIONS);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("function",
						GETLABBUSDATA));
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

			LabbusJSONParser riversParser = new LabbusJSONParser();

			List<HashMap<String, String>> labbusList = new ArrayList<HashMap<String, String>>();
			try {
				labbusList = riversParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return labbusList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			String id;
			String[] dateString;
			String city;
			String zip;
			String text;

			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> parsermap = result.get(i);
				id = parsermap.get("id");
				dateString = parsermap.get("date").split(" ");
				city = parsermap.get("city");
				zip = parsermap.get("zip");
				text = parsermap.get("text");

				String[] tempDate = dateString[0].split("-");
				String date = tempDate[2] + "." + tempDate[1] + "."
						+ tempDate[0];
				mLabbusList.add(new Labbus(Integer.parseInt(id), date, city,
						Integer.parseInt(zip), text));
			}
			displayLabbusData();

			ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarLabbus);
			progressBar.setVisibility(View.GONE);
		}
	}

	private void displayLabbusData() {
		LabbusArrayAdapter adapter = new LabbusArrayAdapter(this,
				R.layout.list_labbus, mLabbusList);
		ListView v = (ListView) findViewById(R.id.lv_labbus);
		v.setAdapter(adapter);
	}

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
			break;
		case R.id.b_news:
			i = new Intent(this, WebViewActivity.class);
			break;
		case R.id.b_more:
			i = new Intent(this, MoreActivity.class);
			break;
		// End Actionbar
		case R.id.b_back:
			onBackPressed();
			break;
		case R.id.b_labbus_moreinformation:
			Intent a = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://docs.google.com/gview?embedded=true&url="
							+ PDFURLLABORBUS));
			startActivity(a);
			break;
		}
		if (i != null && i.getComponent() != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			finish();
			startActivity(i);
		}
	}
}
