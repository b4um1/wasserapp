package at.fhhgb.mc.wasserapp.model;
/**
 * Rating model for healing springs
 * It encloses a rating and a comment to a fontain,wc, ...
 * @author mariobaumgartner
 *
 */
public class RatingHealingSpring {
	private long id;
	private String mComment;
	private int mGrade;
	private HealingSpring mHealingSpring;
	
	public HealingSpring getmHealingSpring() {
		return mHealingSpring;
	}
	public void setmHealingSpring(HealingSpring mHealingSpring) {
		this.mHealingSpring = mHealingSpring;
	}
	public String getmComment() {
		return mComment;
	}
	public void setmComment(String mComment) {
		this.mComment = mComment;
	}
	public int getmGrade() {
		return mGrade;
	}
	public void setmGrade(int mGrade) {
		this.mGrade = mGrade;
	}
	public long getId() {
		return id;
	}
}
