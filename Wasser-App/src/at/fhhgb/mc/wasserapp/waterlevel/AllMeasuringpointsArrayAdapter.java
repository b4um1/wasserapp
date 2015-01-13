package at.fhhgb.mc.wasserapp.waterlevel;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import at.fhhgb.mc.wasserapp.R;

public class AllMeasuringpointsArrayAdapter extends ArrayAdapter<String> {
	List<String> mList;
	private Context mContext;
	
	public AllMeasuringpointsArrayAdapter(Context _context, int _resource,
			List<String> _objects) {
		super(_context, _resource, _objects);
		mList = _objects;
		mContext = _context;
	}
	
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		if (_convertView == null) {
			Context c = getContext();
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_layout, null);
		}
		String element = mList.get(_position);
		if (element != null) {
			TextView v = null;
			v = (TextView) _convertView.findViewById(R.id.tv_river);
			v.setText(element);
		}
		return _convertView;
	}
}
