package at.fhhgb.mc.wasserapp.waterlevel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.fhhgb.mc.wasserapp.R;

public class FragmentSevenDays extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_seven_days,
				container, false);

		return rootView;
	}
}
