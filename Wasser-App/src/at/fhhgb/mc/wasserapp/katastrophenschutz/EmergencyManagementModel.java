package at.fhhgb.mc.wasserapp.katastrophenschutz;

public class EmergencyManagementModel {
	
	private String mDescription;
	private String mTitle;
	private String mTime;
	private String mTel;
	private String mUrl;
	private String mCreatorID;
	
	public EmergencyManagementModel(String _desc, String _title, String _time,String _tel, String _url, String _CreatorID){
		mDescription = _desc;
		mTitle = _title;
		mTime = _time;
		mTel = _tel;
		mUrl = _url;
		mCreatorID = _CreatorID;
	}
	
	public String getmDescription() {
		return mDescription;
	}
	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}

	public String getmTel() {
		return mTel;
	}

	public void setmTel(String mTel) {
		this.mTel = mTel;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getmCreatorID() {
		return mCreatorID;
	}

	public void setmCreatorID(String mCreatorID) {
		this.mCreatorID = mCreatorID;
	}
	
	

}
