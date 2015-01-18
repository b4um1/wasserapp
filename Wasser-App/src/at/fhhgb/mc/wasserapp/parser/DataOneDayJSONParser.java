package at.fhhgb.mc.wasserapp.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class DataOneDayJSONParser {

	private final String TIMESTAMP = "time_stamp";
	private final String WATERLEVEL = "waterlevel";

	private final String LOGTAG = "DataOneDayJSONParser";

	/**
	 * Receives an String which is an json encoded array It returns an array of
	 * measuringpoints
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

		return getDataOneDays(array);
	}

	/**
	 * Returns a List of Hashmaps which represents all the measuringpoints
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getDataOneDays(JSONArray _jArray) {
		int count = _jArray.size();
		List<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> data = null;

		for (int i = 0; i < count; i++) {
			data = getDataOneDay((JSONObject) _jArray.get(i));
			datalist.add(data);
		}
		return datalist;
	}

	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jData
	 * @return
	 */
	private HashMap<String, String> getDataOneDay(JSONObject _jData) {

		HashMap<String, String> map = new HashMap<String, String>();

		String timestamp = "-NA-";
		String waterlevel = "-NA-";

		if (_jData.containsKey(TIMESTAMP)) {
			timestamp = "" + _jData.get(TIMESTAMP);
		}
		if (_jData.containsKey(WATERLEVEL)) {
			waterlevel = "" + _jData.get(WATERLEVEL);
		}
		map.put("timestamp", timestamp);
		map.put("waterlevel", waterlevel);

		return map;
	}
}
