package at.fhhgb.mc.wasserapp.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class WaterlevelJSONParser {
	
	private final String MEASURINGPOINT_ID = "measuringpoint_id";
	private final String WATERLEVEL = "waterlevel";
	private final String LOGTAG = "WaterlevelJSONParser";
	
	/**
	 * Receives an String which is an json encoded array It returns an array of
	 * waterlevels
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

		return getWaterlevels(array);
	}
	
	/**
	 * Returns a List of Hashmaps which represents all the waterlevels
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getWaterlevels(JSONArray _jArray) {
		int count = _jArray.size();
		List<HashMap<String, String>> waterlevelList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> waterlevel = null;

		for (int i = 0; i < count; i++) {
			waterlevel = getWaterlevel((JSONObject) _jArray.get(i));
			waterlevelList.add(waterlevel);
		}
		return waterlevelList;
	}

	
	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jWaterlevel
	 * @return
	 */
	private HashMap<String, String> getWaterlevel(JSONObject _jWaterlevel) {

		HashMap<String, String> map = new HashMap<String, String>();

		String measuringpointId = "-1";
		String waterlevel = "-1";

		if (_jWaterlevel.containsKey(MEASURINGPOINT_ID)) {
			measuringpointId = "" + _jWaterlevel.get(MEASURINGPOINT_ID);
		}
		if (_jWaterlevel.containsKey(WATERLEVEL)) {
			waterlevel = "" + _jWaterlevel.get(WATERLEVEL);
		}

		map.put("measuringpointId", measuringpointId);
		map.put("waterlevel", waterlevel);

		return map;
	}
}
