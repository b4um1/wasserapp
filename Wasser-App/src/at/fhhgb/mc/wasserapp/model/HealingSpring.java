package at.fhhgb.mc.wasserapp.model;

import java.util.Date;

/**
 * Model for healingspring
 * @author mariobaumgartner
 *
 */
public class HealingSpring {
	private long id;
	private Address mAddress;
	private Date mCreationDate;
	private Date mUpdateDate;
	public Address getmAddress() {
		return mAddress;
	}
	public void setmAddress(Address mAddress) {
		this.mAddress = mAddress;
	}
	public Date getmCreationDate() {
		return mCreationDate;
	}
	public void setmCreationDate(Date mCreationDate) {
		this.mCreationDate = mCreationDate;
	}
	public Date getmUpdateDate() {
		return mUpdateDate;
	}
	public void setmUpdateDate(Date mUpdateDate) {
		this.mUpdateDate = mUpdateDate;
	}
	public long getId() {
		return id;
	}
	
}
