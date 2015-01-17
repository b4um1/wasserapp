package at.fhhgb.mc.wasserapp.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class RiverJSONParser {

	private final String RIVER_ID = "id";
	private final String RIVER_NAME = "river_name";
	private final String LOGTAG = "RiversJSONParser";

	/**
	 * Receives an String which is an json encoded array It returns an array of
	 * rivers
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

		return getRivers(array);
	}

	/**
	 * Returns a List of Hashmaps which represents all the rivers
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getRivers(JSONArray _jArray) {
		int count = _jArray.size();
		List<HashMap<String, String>> riverList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> river = null;

		for (int i = 0; i < count; i++) {
			river = getRiver((JSONObject) _jArray.get(i));
			riverList.add(river);
		}
		return riverList;
	}

	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jRiver
	 * @return
	 */
	private HashMap<String, String> getRiver(JSONObject _jRiver) {

		HashMap<String, String> map = new HashMap<String, String>();

		String riverId = "-NA-";
		String riverName = "-NA-";

		if (_jRiver.containsKey(RIVER_ID)) {
			riverId = "" + _jRiver.get(RIVER_ID);
		}
		if (_jRiver.containsKey(RIVER_NAME)) {
			riverName = "" + _jRiver.get(RIVER_NAME);
		}
		map.put("riverId", riverId);
		map.put("riverName", riverName);

		return map;
	}
}
