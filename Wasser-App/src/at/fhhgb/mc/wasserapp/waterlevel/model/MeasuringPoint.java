/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasuringPoint.
 */
public class MeasuringPoint implements Serializable{
	
	/** The m measuring point id. */
	private int mMeasuringPointId;
	
	/** The m measuring point name. */
	private String mMeasuringPointName;
	
	/** The m river name. */
	private String mRiverName;
	
	private String mWaterlevel;
	
	private String mTimestamp;
	
	public String getmTimestamp() {
		return mTimestamp;
	}

	public void setmTimestamp(String mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	/**
	 * Instantiates a new measuring point.
	 *
	 * @param measuringPointName the measuring point name
	 * @param waterlevel the waterlevel
	 * @param riverName the river name
	 */
	public MeasuringPoint(int measuringPointId, String measuringPointName, String riverName, String waterlevel, String timestamp) {
		mMeasuringPointId = measuringPointId;
		mMeasuringPointName = measuringPointName;
		mRiverName = riverName;
		mWaterlevel = waterlevel;
		mTimestamp = timestamp;
	}
	
	public String getmWaterlevel() {
		return mWaterlevel;
	}

	public void setmWaterlevel(String mWaterlevel) {
		this.mWaterlevel = mWaterlevel;
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
	 * Gets the m river name.
	 *
	 * @return the m river name
	 */
	public String getmRiverName() {
		return mRiverName;
	}

	public int getmMeasuringPointId() {
		return mMeasuringPointId;
	}

	public void setmMeasuringPointId(int mMeasuringPointId) {
		this.mMeasuringPointId = mMeasuringPointId;
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
