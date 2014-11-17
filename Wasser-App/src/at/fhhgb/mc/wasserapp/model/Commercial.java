package at.fhhgb.mc.wasserapp.model;

/**
 * @author Thomas Kranzer
 * Model of Commercial with getter and setter
 */
public class Commercial {
	private long id;
	private String mPhoto;
	private String mWebLink;
	public String getmPhoto() {
		return mPhoto;
	}
	public void setmPhoto(String mPhoto) {
		this.mPhoto = mPhoto;
	}
	public String getmWebLink() {
		return mWebLink;
	}
	public void setmWebLink(String mWebLink) {
		this.mWebLink = mWebLink;
	}
	public long getId() {
		return id;
	}
	
	
}
