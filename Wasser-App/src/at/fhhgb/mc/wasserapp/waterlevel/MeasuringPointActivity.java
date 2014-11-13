/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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
	private static final long serialVersionUID = -2880774401199368291L;
	
	/** The m clicked id. */
	private long mClickedId = -1;
	
	/** The m list. */
	private ArrayList<River> mList;


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(5);
		overridePendingTransition(0, 0);
		
		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);

		mClickedId = getIntent().getLongExtra("clickedRiver", -1);
		mList = (ArrayList<River>) getIntent()
				.getSerializableExtra("ArrayList");

		MyArrayAdapterMeasuringPoint adapter = new MyArrayAdapterMeasuringPoint(
				this, R.layout.list_layout, mList.get(
						(int) mClickedId).getmMeasuringPoints());

		ListView v = (ListView) findViewById(R.id.container_measuringpoint);
		v.setOnItemClickListener(this);
		v.setAdapter(adapter);
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
		ArrayList<MeasuringPoint> list = new ArrayList<MeasuringPoint>();

		// LOAD INTERNAL DATA
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = prefs.getString("list", "");
		Type type = new TypeToken<ArrayList<MeasuringPoint>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			list = gson.fromJson(json, type);
		}

		boolean isAlreadyInTheList = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i)
					.getmMeasuringPointName()
					.equals(mList.get((int) mClickedId).getmMeasuringPoints()
							.get((int) id).getmMeasuringPointName())) {
				isAlreadyInTheList = true;
			}
		}

		if (!isAlreadyInTheList) {
			// UPDATE INTERNAL DATA
			list.add(mList.get((int) mClickedId).getmMeasuringPoints()
					.get((int) id));

			// STORE INTERNAL DATA
			prefs = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			editor = prefs.edit();
			gson = new Gson();
			json = gson.toJson(list);
			editor.putString("list", json);
			editor.commit();

		}

		Intent i = new Intent(getApplicationContext(),
				WaterLevelsActivity.class);
		startActivity(i);


		finish();
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
