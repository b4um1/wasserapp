package at.fhhgb.mc.wasserapp.waterlevel.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import at.fhhgb.mc.wasserapp.waterlevel.FragmentOneDay;
import at.fhhgb.mc.wasserapp.waterlevel.FragmentSevenDays;
import at.fhhgb.mc.wasserapp.waterlevel.ShowMeasuringPointActivity;
import at.fhhgb.mc.wasserapp.waterlevel.model.MeasuringPoint;

public class ChartsPagerAdapter extends FragmentPagerAdapter{
	int mMpId;
	public ChartsPagerAdapter(FragmentManager fm, int _mpId) {
		super(fm);
		mMpId = _mpId;
	}

	@Override
	public Fragment getItem(int index) {
		Bundle bundle;
		 switch (index) {
	        case 0:
	        	bundle = new Bundle();
	        	bundle.putInt("measuringpointId", mMpId);
	        	FragmentOneDay fragmentOneDay = new FragmentOneDay();
	        	fragmentOneDay.setArguments(bundle);
	            return fragmentOneDay;
	        case 1:
	        	bundle = new Bundle();
	        	bundle.putInt("measuringpointId", mMpId);
	        	FragmentSevenDays fragmentSevenDays = new FragmentSevenDays();
	        	fragmentSevenDays.setArguments(bundle);
	            return fragmentSevenDays;
	        }
	        return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
