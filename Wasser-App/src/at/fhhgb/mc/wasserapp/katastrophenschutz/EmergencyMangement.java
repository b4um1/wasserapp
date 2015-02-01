package at.fhhgb.mc.wasserapp.katastrophenschutz;

import java.io.BufferedReader;
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

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

public class EmergencyMangement extends Activity implements OnClickListener{
	
	
	private ArrayList<EmergencyManagementModel> mList;
	private Animation mFadein;
	private Animation mFadeout;
	private TextView mDescriptionview;
	private final String getMethod = "getAllEmergencyInfos";
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/emergency.php";
	private final String USER_AGENT = "Mozilla/5.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(13);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener(
				(ViewGroup) findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);
		
		mFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
		mFadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
		
		mFadeout.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mDescriptionview.setVisibility(View.GONE);
			}
		});
		mList = new ArrayList<EmergencyManagementModel>();
		new RetrieveTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emergency_mangement, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void displayEmergencies() {
		EmergencyArrayAdapter adapter = new EmergencyArrayAdapter(this,
				R.layout.list_labbus, mList);
		ListView v = (ListView) findViewById(R.id.lv_emergencymanagement);
		v.setAdapter(adapter);
		v.setSelection(v.getAdapter().getCount()-1); //set focus on last element
	}
	public void removeAtomPayOnClickHandler(View v) {
		EmergencyHolder holder = (EmergencyHolder)v.getTag();
		mDescriptionview = holder.tv_description;
		
		if (holder.b_more){
			mDescriptionview.setVisibility(View.VISIBLE);
			mDescriptionview.startAnimation(mFadein);
			holder.b_more = false;
			holder.btn_more.setText("Weniger");
		}else{
			mDescriptionview.startAnimation(mFadeout);
			holder.b_more = true;
			holder.btn_more.setText("Mehr");
		}
		v.setTag(holder);
	}
	
	@Override
	public void onClick(View _v) {
		Intent i = new Intent();

		switch (_v.getId()) {
		// Actionbar
		case R.id.b_home:
			i = new Intent(this, HomeActivity.class);
			break;
		case R.id.b_position:
			i = new Intent();
			if (LoginActivity.superUser) {
				i = new Intent(this, ChooseMarkerActivity.class);
				i.putExtra("user", true);
			} else {
				i = new Intent(this, MapActivity.class);
				i.putExtra("user", false);
				i.putExtra("m_markertype", "all");
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
		}
		
		if (i != null && i.getComponent() != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			finish();
			startActivity(i);
		}
	}
	/**
	 * Retrieve task
	 * Retrieves all the emergencies
	 * @author mariobaumgartner
	 *
	 */
	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.d("Retrieve Emergencies", "Start to retrieve");
			String url = FTPURLOFPHPFUNCTIONS;

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters
						.add(new BasicNameValuePair("function", getMethod));
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result.toString();
		}

		@Override
		protected void onPostExecute(String _result) {
			super.onPostExecute(_result);
			Log.i("Result after retrieve", _result);
			new ParserTask().execute(_result);
		}
	}

	/**
	 * Starting to parse the result of Retrieve-Task
	 * @author mariobaumgartner
	 *
	 */
	private class ParserTask extends
			AsyncTask<String, Void, List<HashMap<String, String>>> {

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			EmergencyJSONParser emergencyParser = new EmergencyJSONParser();

			List<HashMap<String, String>> emergencyList = new ArrayList<HashMap<String, String>>();
			try {
				emergencyList = emergencyParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return emergencyList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			for (int i = 0; i < result.size(); i++) {

				HashMap<String, String> parsermap = result.get(i);

				String id = parsermap.get("id");
				String title = parsermap.get("title");
				String comment = parsermap.get("comment");
				String tel = parsermap.get("tel");
				String url = parsermap.get("url");
				String creationtime = parsermap.get("creationtime");

				EmergencyManagementModel emergency = new EmergencyManagementModel(comment, title, creationtime, tel, url, id);
				mList.add(emergency);
			}
			displayEmergencies();
		}
	}

}
