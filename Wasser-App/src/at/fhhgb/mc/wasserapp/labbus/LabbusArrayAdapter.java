package at.fhhgb.mc.wasserapp.labbus;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.waterlevel.model.River;

public class LabbusArrayAdapter extends ArrayAdapter<Labbus>{
	/** The m_ list. */
	List<Labbus> m_List;
	
	/** The m_ context. */
	private Context m_Context;

	/**
	 * Instantiates a new my array adapter labbus.
	 *
	 * @param _context the _context
	 * @param _resource the _resource
	 * @param _objects the _objects
	 */
	public LabbusArrayAdapter(Context _context, int _resource,List<Labbus> _objects) {
		super(_context, _resource, _objects);
		m_List = _objects;
		m_Context = _context;
	}
	
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		if (_convertView == null) {
			Context c = getContext();
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_labbus, null);
		}
		Labbus element = m_List.get(_position);
		if (element != null) {
			TextView v = null;
			v = (TextView) _convertView.findViewById(R.id.tv_labbus_zip);
			v.setText("" + element.getmZip());
			
			v = (TextView) _convertView.findViewById(R.id.tv_labbus_city);
			v.setText(element.getmCity());
			
			v = (TextView) _convertView.findViewById(R.id.tv_labbus_date);
			v.setText(element.getmDate());
			
			v = (TextView) _convertView.findViewById(R.id.tv_labbus_text);
			v.setText(element.getmText());
		}
		return _convertView;
	}
}
