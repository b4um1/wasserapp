/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.fhhgb.mc.wasserapp.waterlevel.MeasuringPoint;
import at.fhhgb.mc.wasserapp.R;

// TODO: Auto-generated Javadoc
/**
 * The Class MyArrayAdapter.
 */
public class MyArrayAdapter extends ArrayAdapter<MeasuringPoint> {
	
	/** The m list. */
	List<MeasuringPoint> mList;
	
	/** The m context. */
	private Context mContext;
	
	/** The mp_list. */
	private ArrayList<MeasuringPoint> mp_list;
	
	/** The notification_list. */
	private ArrayList<NotificationModel> notification_list;

	/**
	 * Instantiates a new my array adapter.
	 *
	 * @param _context the _context
	 * @param _resource the _resource
	 * @param _objects the _objects
	 */
	public MyArrayAdapter(Context _context, int _resource,
			List<MeasuringPoint> _objects) {
		super(_context, _resource, _objects);
		mList = _objects;
		mContext = _context;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		if (_convertView == null) {
			Context c = getContext();
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_waterlevel, null);
		}
		MeasuringPoint element = mList.get(_position);
		Log.i("element", element.getmMeasuringPointName());
		final int position = _position;
		if (element != null) {
			ImageView ivDeleteFav = (ImageView) _convertView
					.findViewById(R.id.iv_waterlevels_delete_fav);
			ivDeleteFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View _v) {

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(mContext);
					Editor editor = prefs.edit();
					Gson gson = new Gson();
					String json = prefs.getString("list", "");
					Type type = new TypeToken<ArrayList<MeasuringPoint>>() {
					}.getType();
					ArrayList<MeasuringPoint> mp_list = gson.fromJson(json, type);

					
					Log.i("size before", "" + mp_list.size());
					mp_list.remove(position);
					Log.i("size after", "" + mp_list.size());

					
					prefs = PreferenceManager
							.getDefaultSharedPreferences(mContext);
					editor = prefs.edit();
					gson = new Gson();
					json = gson.toJson(mp_list);
					editor.putString("list", json);
					editor.commit();

					mList.clear();
					for (int i = 0; i < mp_list.size(); i++) {
						mList.add(mp_list.get(i));
					}

					Log.i(" ---- ", "data set changed!");

					notifyDataSetChanged();

				}
			});

			TextView v = null;
			v = (TextView) _convertView
					.findViewById(R.id.tv_waterLevels_location_river_content);
			v.setText(element.getmMeasuringPointName() + ", "
					+ element.getmRiverName());

			v = (TextView) _convertView
					.findViewById(R.id.tv_waterLevels_waterLevel_content);
			v.setText("" + element.getmWaterlevel());

		}
		return _convertView;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


}
