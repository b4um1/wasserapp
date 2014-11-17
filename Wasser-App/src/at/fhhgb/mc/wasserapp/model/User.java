package at.fhhgb.mc.wasserapp.model;

import java.util.Date;

/**
 * Model for users
 * @author mariobaumgartner
 *
 */
public class User {
	private long id;
	private Date mCreationtime;
	private String mSurename;
	private String mLastname;
	private String mEmail;
	private Userrole mUserrole;
	
	public Date getmCreationtime() {
		return mCreationtime;
	}
	public void setmCreationtime(Date mCreationtime) {
		this.mCreationtime = mCreationtime;
	}
	public String getmSurename() {
		return mSurename;
	}
	public void setmSurename(String mSurename) {
		this.mSurename = mSurename;
	}
	public String getmLastname() {
		return mLastname;
	}
	public void setmLastname(String mLastname) {
		this.mLastname = mLastname;
	}
	public String getmEmail() {
		return mEmail;
	}
	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public Userrole getmUserrole() {
		return mUserrole;
	}
	public void setmUserrole(Userrole mUserrole) {
		this.mUserrole = mUserrole;
	}
	public long getId() {
		return id;
	}
	
	
}
