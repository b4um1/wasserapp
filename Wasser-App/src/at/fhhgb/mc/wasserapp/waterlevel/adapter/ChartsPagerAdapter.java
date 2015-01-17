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
		 switch (index) {
	        case 0:
	        	Bundle bundle = new Bundle();
	        	bundle.putInt("measuringpointId", mMpId);
	        	FragmentOneDay fragment = new FragmentOneDay();
	        	fragment.setArguments(bundle);
	            return fragment;
	        case 1:
	            return new FragmentSevenDays();
	        }
	        return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
