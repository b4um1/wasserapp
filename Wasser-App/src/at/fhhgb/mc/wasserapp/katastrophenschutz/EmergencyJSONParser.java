package at.fhhgb.mc.wasserapp.katastrophenschutz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class EmergencyJSONParser {
	private final String TITLE = "title";
	private final String COMMENT = "comment";
	private final String ID = "id";
	private final String TEL = "tel";
	private final String CONTACT_PERSON = "contact_person";
	private final String URL = "url";

	private final String LOGTAG = "EmergencyJSONParserClass";

	/**
	 * Receives an String which is an json encoded array It returnes an array of
	 * emergencies
	 * 
	 * @param _jsonResult
	 * @return
	 * @throws ParseException
	 */
	public List<HashMap<String, String>> parse(String _jsonResult)
			throws ParseException {
		Log.i(LOGTAG, "Parsing-process started");

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(_jsonResult);
		JSONArray array = (JSONArray) obj;

		return getEmergencies(array);
	}

	/**
	 * Returns a List of Hashmaps which represents all the emergencies
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getEmergencies(JSONArray _jArray) {
		int emergencyCounter = _jArray.size();
		List<HashMap<String, String>> emergencyList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> emergency = null;

		for (int i = 0; i < emergencyCounter; i++) {
			emergency = getEmergency((JSONObject) _jArray.get(i));
			emergencyList.add(emergency);
		}
		return emergencyList;
	}

	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jEmergency
	 * @return
	 */
	private HashMap<String, String> getEmergency(JSONObject _jEmergency) {

		HashMap<String, String> marker = new HashMap<String, String>();
		String id = "-NA-", comment = "-NA-", title = "", contact_person = "", url= "", tel = "";

		// Extracting creator_id if available
		if (_jEmergency.containsKey(ID)) {
			id = "" + _jEmergency.get(ID);
		}

		// Extracting comment --> description of emergency
		if (_jEmergency.containsKey(COMMENT)) {
			comment = "" + _jEmergency.get(COMMENT);
		}

		// Extracting title
		if (_jEmergency.containsKey(TITLE)) {
			title = "" + _jEmergency.get(TITLE);
		}

		// Extracting firstname, if available
		if (_jEmergency.containsKey(URL)) {
			url = "" + _jEmergency.get(URL);
		}
		// Extracting creation datetime, if available
		if (_jEmergency.containsKey(TEL)) {
			tel = "" + _jEmergency.get(TEL);
		}
		

		marker.put("id", id);
		marker.put("comment", comment);
		marker.put("title", title);
		marker.put("tel", tel);
		marker.put("url", url);
		//missing in php file
		//marker.put(CREATION_TIME, creation_time);

		return marker;
	}
}
