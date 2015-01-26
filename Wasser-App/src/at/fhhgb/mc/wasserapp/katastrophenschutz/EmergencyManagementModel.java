package at.fhhgb.mc.wasserapp.katastrophenschutz;

public class EmergencyManagementModel {
	
	private String mDescription;
	private String mTitle;
	private String mTime;
	
	public EmergencyManagementModel(String _desc, String _title, String _time){
		mDescription = _desc;
		mTitle = _title;
		mTime = _time;
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
	
	

}
