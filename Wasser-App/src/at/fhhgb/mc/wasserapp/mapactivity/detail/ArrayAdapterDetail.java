/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity.detail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.waterlevel.MeasuringPoint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// TODO: Auto-generated Javadoc
/**
 * The Class ArrayAdapterDetail.
 */
public class ArrayAdapterDetail extends ArrayAdapter<Comment> {
	
	/** The m list. */
	List<Comment> mList;
	
	/** The m context. */
	private Context mContext;

	/**
	 * Instantiates a new array adapter.
	 *
	 * @param _context the _context
	 * @param _resource the _resource
	 * @param _objects the _objects
	 */
	public ArrayAdapterDetail(Context _context, int _resource,
			List<Comment> _objects) {
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
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_convertView = inflater.inflate(R.layout.list_comments, null);
		}
		Comment comm = mList.get(_position);
		Log.i("element", comm.getComment());
		
		if (comm != null) {
			TextView v = (TextView) _convertView
					.findViewById(R.id.textView_list_comment);
			v.setText(comm.getComment());

//			v = (TextView) _convertView
//					.findViewById(R.id.tf_list_rating);
//			v.setText("" + comm.getRating());

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
