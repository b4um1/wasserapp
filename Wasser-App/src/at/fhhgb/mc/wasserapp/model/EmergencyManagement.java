package at.fhhgb.mc.wasserapp.model;

import java.util.Date;

/**
 * @author Thomas Kranzer
 * Model of EmergencyManagement with getter and setter
 */
public class EmergencyManagement {
	private long id;
	private Date mTimeStamp;
	private String mComment;
	private String mReference;
	private EmergencyInfo mEmergencyInfo;
	
	public Date getmTimeStamp() {
		return mTimeStamp;
	}
	public void setmTimeStamp(Date mTimeStamp) {
		this.mTimeStamp = mTimeStamp;
	}
	public String getmComment() {
		return mComment;
	}
	public void setmComment(String mComment) {
		this.mComment = mComment;
	}
	public String getmReference() {
		return mReference;
	}
	public void setmReference(String mReference) {
		this.mReference = mReference;
	}
	public EmergencyInfo getmEmergencyInfo() {
		return mEmergencyInfo;
	}
	public void setmEmergencyInfo(EmergencyInfo mEmergencyInfo) {
		this.mEmergencyInfo = mEmergencyInfo;
	}
	public long getId() {
		return id;
	}
	
}
