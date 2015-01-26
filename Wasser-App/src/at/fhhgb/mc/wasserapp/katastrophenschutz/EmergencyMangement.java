package at.fhhgb.mc.wasserapp.katastrophenschutz;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.labbus.LabbusArrayAdapter;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

public class EmergencyMangement extends Activity implements OnClickListener{
	
	TextView tf_test;
	ArrayList<EmergencyManagementModel> mList;

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
		
		//generateEmergencies();
		
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
	
	private void generateEmergencies(){
		mList = new ArrayList<EmergencyManagementModel>();
		
		EmergencyManagementModel m = new EmergencyManagementModel("hallo test1","test 1", "21.02.0123");
		mList.add(m);
		mList.add(m);
		mList.add(m);
		displayEmergencies();
	}
	
	private void displayEmergencies() {
		EmergencyArrayAdapter adapter = new EmergencyArrayAdapter(this,
				R.layout.list_labbus, mList);
		ListView v = (ListView) findViewById(R.id.lv_emergencymanagement);
		v.setAdapter(adapter);
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
}
