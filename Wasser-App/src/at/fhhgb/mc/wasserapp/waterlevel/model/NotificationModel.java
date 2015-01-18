/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel.model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationModel.
 */
public class NotificationModel implements Serializable{
	
	/** The m mp. */
	private MeasuringPoint mMp;
	
	/** The m notification value. */
	private int mNotificationValue;
	
	private boolean mIsSmaller;


	/**
	 * Instantiates a new notification model.
	 *
	 * @param mp the mp
	 * @param notificationValue the notification value
	 */
	public NotificationModel(MeasuringPoint mp, int notificationValue, boolean isSmaller) {
		mMp = mp;
		mNotificationValue = notificationValue;
		mIsSmaller = isSmaller;
	}

	
	public boolean ismIsSmaller() {
		return mIsSmaller;
	}


	public void setmIsSmaller(boolean mIsSmaller) {
		this.mIsSmaller = mIsSmaller;
	}


	/**
	 * Gets the m notification value.
	 *
	 * @return the m notification value
	 */
	public int getmNotificationValue() {
		return mNotificationValue;
	}

	/**
	 * Sets the m notification value.
	 *
	 * @param mNotificationValue the new m notification value
	 */
	public void setmNotificationValue(int mNotificationValue) {
		this.mNotificationValue = mNotificationValue;
	}


	/**
	 * Gets the m mp.
	 *
	 * @return the m mp
	 */
	public MeasuringPoint getmMp() {
		return mMp;
	}


	/**
	 * Sets the m mp.
	 *
	 * @param mMp the new m mp
	 */
	public void setmMp(MeasuringPoint mMp) {
		this.mMp = mMp;
	}

}
