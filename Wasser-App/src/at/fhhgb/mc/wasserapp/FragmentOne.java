package at.fhhgb.mc.wasserapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Toast;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.waterlevel.WaterLevelsActivity;

public class FragmentOne extends Fragment implements OnClickListener {
	
	Context mContext;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        mContext = rootView.getContext();
        
        Button b_fountain = (Button) rootView.findViewById(R.id.b_fountain);
        b_fountain.setOnClickListener(this);
        
        Button b_wc = (Button) rootView.findViewById(R.id.b_wc);
        b_wc.setOnClickListener(this);
        
        Button b_waterlevel = (Button) rootView.findViewById(R.id.b_waterlevel);
        b_waterlevel.setOnClickListener(this);
        
        Button b_precipitation = (Button) rootView.findViewById(R.id.b_healing);
        b_precipitation.setOnClickListener(this);
        
        return rootView;
    }

	@Override
	public void onClick(View _button) {
		_button.setPressed(true);
		switch (_button.getId()) {
		case R.id.b_fountain:
			Intent map_fontain;
			map_fontain = new Intent(mContext, MapActivity.class);
			map_fontain.putExtra("user", false);
			map_fontain.putExtra("m_markertype", "fountain");
			startActivity(map_fontain);
	
			break;
		case R.id.b_wc:
			Intent map_wc;
			map_wc = new Intent(mContext, MapActivity.class);
			map_wc.putExtra("user", false);
			map_wc.putExtra("m_markertype", "toilet");
			startActivity(map_wc);
			break;
		case R.id.b_waterlevel:
			Intent waterlevel;
			waterlevel = new Intent(mContext, WaterLevelsActivity.class);
			startActivity(waterlevel);
			break;
		case R.id.b_healing:
			Intent hs;
			hs = new Intent(mContext, MapActivity.class);
			hs.putExtra("user", false);
			hs.putExtra("m_markertype", "healingspring");
			startActivity(hs);
			break;
		}
	}
}
