package at.fhhgb.mc.wasserapp.model;

/**
 * @author Thomas Kranzer
 * Model of River with getter and setter
 */
public class River {
	private long id;
	private String mName;
	
	public long getId() {
		return id;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	
}
