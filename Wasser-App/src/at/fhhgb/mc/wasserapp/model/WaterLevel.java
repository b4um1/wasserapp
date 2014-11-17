package at.fhhgb.mc.wasserapp.model;

import java.util.Date;

/**
 * @author Thomas Kranzer
 * Model of WaterLevel with getter and setter
 */
public class WaterLevel {
	private long id;
	private float mWaterLevel;
	private Date mTimeStamp;
	private Measuringpoint mMeasuringpoint;
	
	public float getmWaterLevel() {
		return mWaterLevel;
	}
	public void setmWaterLevel(float mWaterLevel) {
		this.mWaterLevel = mWaterLevel;
	}
	public Date getmTimeStamp() {
		return mTimeStamp;
	}
	public void setmTimeStamp(Date mTimeStamp) {
		this.mTimeStamp = mTimeStamp;
	}
	public Measuringpoint getmMeasuringpoint() {
		return mMeasuringpoint;
	}
	public void setmMeasuringpoint(Measuringpoint mMeasuringpoint) {
		this.mMeasuringpoint = mMeasuringpoint;
	}
	public long getId() {
		return id;
	}
	
	
}
