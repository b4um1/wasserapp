/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class River.
 */
public class River implements Serializable{
	
	/** The m river name. */
	private String mRiverName;
	
	/** The m measuring points. */
	private ArrayList<MeasuringPoint> mMeasuringPoints;
	
	/**
	 * Instantiates a new river.
	 */
	public River() {
		
	}
	
	/**
	 * Instantiates a new river.
	 *
	 * @param riverName the river name
	 */
	public River(String riverName) {
		mRiverName = riverName;
	}
	
	/**
	 * Instantiates a new river.
	 *
	 * @param riverName the river name
	 * @param measuringPoints the measuring points
	 */
	public River(String riverName, ArrayList<MeasuringPoint> measuringPoints) {
		mRiverName = riverName;
		mMeasuringPoints = measuringPoints;
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

	/**
	 * Gets the m measuring points.
	 *
	 * @return the m measuring points
	 */
	public ArrayList<MeasuringPoint> getmMeasuringPoints() {
		return mMeasuringPoints;
	}

	/**
	 * Sets the m measuring points.
	 *
	 * @param mMeasuringPoints the new m measuring points
	 */
	public void setmMeasuringPoints(ArrayList<MeasuringPoint> mMeasuringPoints) {
		this.mMeasuringPoints = mMeasuringPoints;
	}

}
