package at.fhhgb.mc.wasserapp.model;

import java.util.Date;

/**
 * @author Thomas Kranzer
 * Model of Laborbus with getter and setter
 */
public class Laborbus {
	private long id;
	private Date mDateTime;
	private String mCity;
	private int mZip;
	
	public long getId() {
		return id;
	}
	public Date getmDateTime() {
		return mDateTime;
	}
	public void setmDateTime(Date mDateTime) {
		this.mDateTime = mDateTime;
	}
	public String getmCity() {
		return mCity;
	}
	public void setmCity(String mCity) {
		this.mCity = mCity;
	}
	public int getmZip() {
		return mZip;
	}
	public void setmZip(int mZip) {
		this.mZip = mZip;
	}
	
}
