package at.fhhgb.mc.wasserapp.mapactivity.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class CommentJSONParser. Should parse the comments in json from a
 * specific markertype
 */
public class CommentJSONParser {

private final String GRADE = "grade";
	private final String COMMENT = "comment";
	private final String LOGTAG = "CommentJSONParserClass";

	public CommentJSONParser() {
	}

	/**
	 * Receives an String which is an json encoded array It returnes an array of
	 * markers
	 * 
	 * @param _jsonResult
	 * @return
	 * @throws ParseException
	 */
	public List<HashMap<String, String>> parse(String _jsonResult)
			throws ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(_jsonResult);
		JSONArray array = (JSONArray) obj;

		return getComments(array);
	}

	/**
	 * Returns a List of Hashmaps which represents all the comments
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getComments(JSONArray _jArray) {
		int commentCounter = _jArray.size();
		List<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> comment = null;

		for (int i = 0; i < commentCounter; i++) {
			comment = getComment((JSONObject) _jArray.get(i));
			commentList.add(comment);
		}
		return commentList;
	}

	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jComment
	 * @return
	 */
	private HashMap<String, String> getComment(JSONObject _jComment) {

		HashMap<String, String> commentHashmap = new HashMap<String, String>();
		String grade = "", comment = "";

		// Extracting comment, if available
		if (_jComment.containsKey(COMMENT)) {
			comment = "" + _jComment.get(COMMENT);
		}
		// Extracting grade, if available
		if (_jComment.containsKey(GRADE)) {
			grade = "" + _jComment.get(GRADE);
		}
		
		commentHashmap.put(COMMENT, comment);
		commentHashmap.put(GRADE, grade);

		return commentHashmap;
	}
}
