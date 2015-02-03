package at.fhhgb.mc.wasserapp.labbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class LabbusJSONParser {
	private final String LABBUS_ID = "id";
	private final String LABBUS_DATE = "arrive_date";
	private final String LABBUS_CITY = "city";
	private final String LABBUS_ZIP = "zip";
	private final String LABBUS_TEXT = "zusatzinfo";
	
	private final String LOGTAG = "LabbusJSONParser";
	
	/**
	 * Receives an String which is an json encoded array It returns an array of
	 * labbus
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

		return getLabbuses(array);
	}
	
	/**
	 * Returns a List of Hashmaps which represents all the labbusses	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getLabbuses(JSONArray _jArray) {
		int count = _jArray.size();
		List<HashMap<String, String>> labbusList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> river = null;

		for (int i = 0; i < count; i++) {
			river = getLabbus((JSONObject) _jArray.get(i));
			labbusList.add(river);
		}
		return labbusList;
	}
	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jLabbus
	 * @return
	 */
	private HashMap<String, String> getLabbus(JSONObject _jLabbus) {

		HashMap<String, String> map = new HashMap<String, String>();

		String id = "-NA-";
		String date = "-NA-";
		String city = "-NA-";
		String zip = "-NA-";
		String text = "-NA-";

		if (_jLabbus.containsKey(LABBUS_ID)) {
			id = "" + _jLabbus.get(LABBUS_ID);
		}
		if (_jLabbus.containsKey(LABBUS_DATE)) {
			date = "" + _jLabbus.get(LABBUS_DATE);
		}
		if (_jLabbus.containsKey(LABBUS_CITY)) {
			city = "" + _jLabbus.get(LABBUS_CITY);
		}
		if (_jLabbus.containsKey(LABBUS_ZIP)) {
			zip = "" + _jLabbus.get(LABBUS_ZIP);
		}
		if (_jLabbus.containsKey(LABBUS_TEXT)) {
			text = "" + _jLabbus.get(LABBUS_TEXT);
		}
		
		map.put("id", id);
		map.put("date", date);
		map.put("city", city);
		map.put("zip", zip);
		map.put("text", text);
		
		return map;
	}
}
