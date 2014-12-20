package at.fhhgb.mc.wasserapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {

        switch (index) {
        case 0:
            // Top Rated fragment activity
        	Log.i("ONE", "ONE");
            return new FragmentOne();
        case 1:
            // Games fragment activity
        	Log.i("TWO", "TWO");
            return new FragmentTwo();
        }
 
        return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
