package at.fhhgb.mc.wasserapp.waterlevel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;

public class MyArrayAdapterWaterlevel extends ArrayAdapter<MeasuringPoint>{

	/** The m_ list. */
	List<MeasuringPoint> m_List;
	
	/** The m_ context. */
	private Context m_Context;

	/**
	 * Instantiates a new my array adapter measuring point.
	 *
	 * @param _context the _context
	 * @param _resource the _resource
	 * @param _objects the _objects
	 */
	public MyArrayAdapterWaterlevel(Context _context, int _resource,
			List<MeasuringPoint> _objects) {
		super(_context, _resource, _objects);
		m_List = _objects;
		m_Context = _context;
	}
	
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		if (_convertView == null) {
			Context c = getContext();
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_waterlevel, null);
		}
		MeasuringPoint element = m_List.get(_position);
		final int position = _position;
		final View convertView = _convertView;
		if (element != null) {
			
			ImageView ivDeleteFav = (ImageView) _convertView.findViewById(R.id.iv_waterlevels_delete_fav);
			ivDeleteFav.setOnClickListener(new OnClickListener() {
				
				public void onClick(View _v) {
					ArrayList<MeasuringPoint> list = FavsRepository.loadFavs(m_Context);
					list.remove(position);
					FavsRepository.storeFavs(m_Context, list);
					m_List.clear();
					for (int i = 0; i < list.size(); i++) {
						m_List.add(list.get(i));
					}
					
					if (m_List.isEmpty()) {
						WaterLevelsActivity.setInfoText();
					}
					notifyDataSetChanged();
				}
			});
			
			TextView v = null;
			v = (TextView) _convertView.findViewById(R.id.tv_waterLevels_location_river_content);
			v.setText(element.getmMeasuringPointName() + ", " + element.getmRiverName());
			
			v = (TextView) _convertView.findViewById(R.id.tv_waterLevels_waterLevel_content);
			if (element.getmWaterlevel().equals("-")) {
				v.setText(element.getmWaterlevel() + "");
			} else {
				v.setText(element.getmWaterlevel() + " cm");
			}
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
