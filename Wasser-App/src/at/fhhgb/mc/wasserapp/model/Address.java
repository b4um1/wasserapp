package at.fhhgb.mc.wasserapp.model;


/**
 * 
 * @author mariobaumgartner
 * Address model 
 */
public class Address {
	private long id;
	private double mLongitude;
	private double mLatitude;
	private String mStreet;
	private String mCity;
	private int mZipcode;
	
	public double getmLongitude() {
		return mLongitude;
	}
	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}
	public double getmLatitude() {
		return mLatitude;
	}
	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}
	public String getmStreet() {
		return mStreet;
	}
	public void setmStreet(String mStreet) {
		this.mStreet = mStreet;
	}
	public String getmCity() {
		return mCity;
	}
	public void setmCity(String mCity) {
		this.mCity = mCity;
	}
	public int getmZipcode() {
		return mZipcode;
	}
	public void setmZipcode(int mZipcode) {
		this.mZipcode = mZipcode;
	}
	public long getId() {
		return id;
	}
	
	
}
