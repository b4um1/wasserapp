/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity.detail;


/**
 * Model class Comment
 * @author mariobaumgartner
 *
 */
public class Comment {
	
	private String m_comment;
	private int m_grade;
	
	public Comment(){
		
	}
	public Comment(String _comment,int _grade){
		m_comment = _comment;
		m_grade = _grade;
	}
	
	public String getM_comment() {
		return m_comment;
	}
	public void setM_comment(String m_comment) {
		this.m_comment = m_comment;
	}
	public int getM_grade() {
		return m_grade;
	}
	public void setM_grade(int m_grade) {
		this.m_grade = m_grade;
	}
	public String toString(){
		return m_comment + " " + m_grade;
	}
}
