package at.fhhgb.mc.wasserapp.model;

/**
 * @author Thomas Kranzer
 * Model of EmergencyInfo with getter and setter
 */
public class EmergencyInfo {
	private long id;
	private String mEmail;
	private String mUrl;
	private String mTel;
	private String mContactPerson;
	private String mPhoto;
	private EmergencyManagement mEmergencymanagement;
	
	public EmergencyManagement getmEmergencymanagement() {
		return mEmergencymanagement;
	}
	public void setmEmergencymanagement(EmergencyManagement mEmergencymanagement) {
		this.mEmergencymanagement = mEmergencymanagement;
	}
	public String getmEmail() {
		return mEmail;
	}
	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public String getmTel() {
		return mTel;
	}
	public void setmTel(String mTel) {
		this.mTel = mTel;
	}
	public String getmContactPerson() {
		return mContactPerson;
	}
	public void setmContactPerson(String mContactPerson) {
		this.mContactPerson = mContactPerson;
	}
	public String getmPhoto() {
		return mPhoto;
	}
	public void setmPhoto(String mPhoto) {
		this.mPhoto = mPhoto;
	}
	public long getId() {
		return id;
	}
	
}
