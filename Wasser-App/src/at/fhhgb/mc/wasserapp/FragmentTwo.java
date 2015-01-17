package at.fhhgb.mc.wasserapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import at.fhhgb.mc.wasserapp.labbus.LabbusActivity;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;

public class FragmentTwo extends Fragment implements OnClickListener{
	
	Context mContext;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        mContext = rootView.getContext();
         
        Button b_labbus = (Button) rootView.findViewById(R.id.b_labbus);
        b_labbus.setOnClickListener(this);
        
        return rootView;
    }


	@Override
	public void onClick(View _button) {
		_button.setPressed(true);
		switch (_button.getId()) {
		case R.id.b_labbus:
			Intent i;
			i = new Intent(mContext, LabbusActivity.class);
			startActivity(i);
			break;
		}
	}
}
