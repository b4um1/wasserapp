/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.fhhgb.mc.wasserapp.waterlevel.model.MeasuringPoint;
import at.fhhgb.mc.wasserapp.R;

// TODO: Auto-generated Javadoc
/**
 * The Class MyArrayAdapterMeasuringPoint.
 */
public class MyArrayAdapterMeasuringPoint extends ArrayAdapter<MeasuringPoint>{
	
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
	public MyArrayAdapterMeasuringPoint(Context _context, int _resource,
			List<MeasuringPoint> _objects) {
		super(_context, _resource, _objects);
		m_List = _objects;
		m_Context = _context;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		if (_convertView == null) {
			Context c = getContext();
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_layout, null);
		}
		MeasuringPoint element = m_List.get(_position);
		if (element != null) {
			TextView v = null;
			v = (TextView) _convertView.findViewById(R.id.tv_river);
			v.setText(element.getmMeasuringPointName());
		}
		return _convertView;
	}
}
