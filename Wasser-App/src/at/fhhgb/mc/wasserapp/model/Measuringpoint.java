package at.fhhgb.mc.wasserapp.model;

import java.util.ArrayList;

/**
 * @author Thomas Kranzer
 * Model of Measuringpoint with getter and setter
 */
public class Measuringpoint {
	private long id;
	private String mName;
	private River mRiver;
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public River getmRiver() {
		return mRiver;
	}
	public void setmRiver(River mRiver) {
		this.mRiver = mRiver;
	}
	public long getId() {
		return id;
	}
}
