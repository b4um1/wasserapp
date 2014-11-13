/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasuringPoint.
 */
public class MeasuringPoint implements Serializable{
	
	/** The m measuring point name. */
	private String mMeasuringPointName;
	
	/** The m waterlevel. */
	private float mWaterlevel;
	
	/** The m river name. */
	private String mRiverName;
	
	/**
	 * Instantiates a new measuring point.
	 *
	 * @param measuringPointName the measuring point name
	 * @param waterlevel the waterlevel
	 * @param riverName the river name
	 */
	public MeasuringPoint(String measuringPointName, float waterlevel, String riverName) {
		mMeasuringPointName = measuringPointName;
		mWaterlevel = waterlevel;
		mRiverName = riverName;
	}
	
	/**
	 * Gets the m measuring point name.
	 *
	 * @return the m measuring point name
	 */
	public String getmMeasuringPointName() {
		return mMeasuringPointName;
	}
	
	/**
	 * Sets the m measuring point name.
	 *
	 * @param mMeasuringPointName the new m measuring point name
	 */
	public void setmMeasuringPointName(String mMeasuringPointName) {
		this.mMeasuringPointName = mMeasuringPointName;
	}
	
	/**
	 * Gets the m waterlevel.
	 *
	 * @return the m waterlevel
	 */
	public float getmWaterlevel() {
		return mWaterlevel;
	}
	
	/**
	 * Sets the m waterlevel.
	 *
	 * @param mWaterlevel the new m waterlevel
	 */
	public void setmWaterlevel(float mWaterlevel) {
		this.mWaterlevel = mWaterlevel;
	}

	/**
	 * Gets the m river name.
	 *
	 * @return the m river name
	 */
	public String getmRiverName() {
		return mRiverName;
	}

	/**
	 * Sets the m river name.
	 *
	 * @param mRiverName the new m river name
	 */
	public void setmRiverName(String mRiverName) {
		this.mRiverName = mRiverName;
	}
	
}
