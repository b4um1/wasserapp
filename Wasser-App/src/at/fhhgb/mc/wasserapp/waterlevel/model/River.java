/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel.model;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class River.
 */
public class River implements Serializable {

	/** The m river id. */
	private int mRiverId;

	/** The m river name. */
	private String mRiverName;

	/**
	 * Instantiates a new river.
	 */
	public River() {

	}

	/**
	 * Instantiates a new river.
	 *
	 * @param riverName
	 *            the river name
	 */
	public River(int riverId, String riverName) {
		mRiverId = riverId;
		mRiverName = riverName;
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
	 * @param mRiverName
	 *            the new m river name
	 */
	public void setmRiverName(String mRiverName) {
		this.mRiverName = mRiverName;
	}

	public int getmRiverId() {
		return mRiverId;
	}

	public void setmRiverId(int mRiverId) {
		this.mRiverId = mRiverId;
	}

}
