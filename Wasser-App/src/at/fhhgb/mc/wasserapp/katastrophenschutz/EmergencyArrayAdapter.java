package at.fhhgb.mc.wasserapp.katastrophenschutz;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.fhhgb.mc.wasserapp.R;

public class EmergencyArrayAdapter extends ArrayAdapter<EmergencyManagementModel>{
	/** The m_ list. */
	List<EmergencyManagementModel> m_List;
	
	/** The m_ context. */
	private Context m_Context;

	/**
	 * Instantiates a new my array adapter emergency management.
	 *
	 * @param _context the _context
	 * @param _resource the _resource
	 * @param _objects the _objects
	 */
	public EmergencyArrayAdapter(Context _context, int _resource,List<EmergencyManagementModel> _objects) {
		super(_context, _resource, _objects);
		m_List = _objects;
		m_Context = _context;
	}
	
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		if (_convertView == null) {
			Context c = getContext();
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_emergencymanagement, null);
		}
		EmergencyManagementModel element = m_List.get(_position);
		if (element != null) {
			TextView v = null;
			v = (TextView) _convertView.findViewById(R.id.tf_emergencylist_title);
			v.setText("" + element.getmTitle());
			
			v = (TextView) _convertView.findViewById(R.id.tf_emergencylist_description);
			v.setText(element.getmDescription());
			
			v = (TextView) _convertView.findViewById(R.id.tf_emergencylist_time);
			v.setText(element.getmTime());
			
		}
		return _convertView;
	}
}
