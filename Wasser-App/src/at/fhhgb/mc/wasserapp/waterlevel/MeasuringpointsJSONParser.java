package at.fhhgb.mc.wasserapp.waterlevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class MeasuringpointsJSONParser {
	
	private final String MEASURINGPOINT_NAME = "measuringpoint_name";
	private final String LOGTAG = "MeasuringpointsJSONParser";

	/**
	 * Receives an String which is an json encoded array It returns an array of
	 * measuringpointnames
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
	
		return getMeasuringpoints(array);
	}
	
	/**
	 * Returns a List of Hashmaps which represents all the measuringpoints
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getMeasuringpoints(JSONArray _jArray) {
		int count = _jArray.size();
		List<HashMap<String, String>> measuringpointList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> measuringpoint = null;

		for (int i = 0; i < count; i++) {
			measuringpoint = getMeasuringpoint((JSONObject) _jArray.get(i));
			measuringpointList.add(measuringpoint);
		}
		return measuringpointList;
	}
	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jMeasuringpoint
	 * @return
	 */
	private HashMap<String, String> getMeasuringpoint(JSONObject _jMeasuringpoint) {

		HashMap<String, String> map = new HashMap<String, String>();
		String measuringpointName = "-NA-";

		if (_jMeasuringpoint.containsKey(MEASURINGPOINT_NAME)) {
			measuringpointName = "" + _jMeasuringpoint.get(MEASURINGPOINT_NAME);
		}
		map.put("measuringpointName", measuringpointName);

		return map;
	}
}
