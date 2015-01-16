package at.fhhgb.mc.wasserapp.waterlevel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChartsPagerAdapter extends FragmentPagerAdapter{

	public ChartsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		 switch (index) {
	        case 0:
	            // Top Rated fragment activity
	            return new FragmentOneDay();
	        case 1:
	            // Games fragment activity
	            return new FragmentSevenDays();
	        }
	 
	        return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
