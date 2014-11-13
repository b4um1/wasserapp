/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity.detail;

// TODO: Auto-generated Javadoc
/**
 * The Class Comment.
 */
public class Comment {
	
	/** The comment. */
	private String comment;
	
	/** The rating. */
	private String rating;
	
	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}
	
	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return comment + " " + rating;
	}
}
