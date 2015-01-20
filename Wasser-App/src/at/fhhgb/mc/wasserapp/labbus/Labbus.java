package at.fhhgb.mc.wasserapp.labbus;

public class Labbus {
	private int mId;
	private String mDate;
	private String mCity;
	private int mZip;
	private String mText;
	
	
	public Labbus(int id, String date, String city, int zip, String text) {
		super();
		mId = id;
		mDate = date;
		mCity = city;
		mZip = zip;
		mText = text;
	}
		
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
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
	public String getmText() {
		return mText;
	}
	public void setmText(String mText) {
		this.mText = mText;
	}
	
	
}
