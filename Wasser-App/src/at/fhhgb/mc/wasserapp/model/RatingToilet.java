package at.fhhgb.mc.wasserapp.model;
/**
 * Rating model for toilet
 * It encloses a rating and a comment to a fontain,wc, ...
 * @author mariobaumgartner
 *
 */
public class RatingToilet {
	private long id;
	private String mComment;
	private int mGrade;
	private Toilet mToilet;
	
	public Toilet getmToilet() {
		return mToilet;
	}
	public void setmToilet(Toilet mToilet) {
		this.mToilet = mToilet;
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
