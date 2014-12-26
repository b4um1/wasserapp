/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity;

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
 * The Class MarkerJSONParser.
 */
public class MarkerJSONParser {

	private String mType;
	
	private final String DRINKABLE = "drinkable";
	private final String BARRIERFREE = "barrier_free";
	private final String UPDATE_TIME = "update_time";
	private final String CREATION_TIME = "creation_time";
	private final String LATITUDE = "latitude";
	private final String LONGITUDE = "longitude";
	private final String STREET = "street";
	private final String CITY = "city";
	private final String ZIP = "zip";
	private final String FIRSTNAME = "firstname";
	private final String SURNAME = "surename";
	private final String GRADE = "grade";
	private final String COMMENT = "comment";

	private final String LOGTAG = "MarkerJSONParserClass";

	public MarkerJSONParser(String _type) {
		mType = _type;
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
		Log.i(LOGTAG, "Parsing-process started");

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(_jsonResult);
		JSONArray array = (JSONArray) obj;

		return getMarkers(array);
	}

	/**
	 * Returns a List of Hashmaps which represents all the markers
	 * 
	 * @param _jArray
	 * @return List<Hashmap<String,String>>
	 */
	private List<HashMap<String, String>> getMarkers(JSONArray _jArray) {
		int markersCount = _jArray.size();
		List<HashMap<String, String>> markersList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> marker = null;

		for (int i = 0; i < markersCount; i++) {
			marker = getMarker((JSONObject) _jArray.get(i));
			markersList.add(marker);
		}
		return markersList;
	}

	/**
	 * Parses the json-object into a Hashmap<Key,value>
	 * 
	 * @param _jMarker
	 * @return
	 */
	private HashMap<String, String> getMarker(JSONObject _jMarker) {

		String searchid = mType + "_id";

		HashMap<String, String> marker = new HashMap<String, String>();
		String id = "-NA-";
		String lat = "-NA-";
		String lng = "-NA-";
		String address = "-NA-";
		String zip = "", city = "", street = "";
		String attribute = "-NA-";
		String comment = "-NA-";
		String surename = "", firstname = "", grade = "", update_time = "", creation_time = "";

		// Extracting fountain_id,toilet_id,healingspring_id, if available
		if (_jMarker.containsKey(searchid)) {
			id = "" + _jMarker.get(searchid);
		}

		// Extracting latitude, if available
		if (_jMarker.containsKey(LATITUDE)) {
			lat = "" + _jMarker.get(LATITUDE);
		}

		// Extracting longitude, if available
		if (_jMarker.containsKey(LONGITUDE)) {
			lng = "" + _jMarker.get(LONGITUDE);
		}

		// Extracting city, if available
		if (_jMarker.containsKey(CITY)) {
			city = (String) _jMarker.get(CITY);
		}

		// Extracting street, if available
		if (_jMarker.containsKey(STREET)) {
			street = (String) _jMarker.get(STREET);
		}
		// Extracting zip, if available
		if (_jMarker.containsKey(ZIP)) {
			zip = "" + _jMarker.get(ZIP);
		}

		address = street + " " + city + " " + zip;

		// Extracting surname, if available
		if (_jMarker.containsKey(SURNAME)) {
			surename = "" + _jMarker.get(SURNAME);
		}

		// Extracting firstname, if available
		if (_jMarker.containsKey(FIRSTNAME)) {
			firstname = "" + _jMarker.get(FIRSTNAME);
		}
		// Extracting creation datetime, if available
		if (_jMarker.containsKey(CREATION_TIME)) {
			creation_time = "" + _jMarker.get(CREATION_TIME);
		}
		// Extracting update datetime, if available
		if (_jMarker.containsKey(UPDATE_TIME)) {
			update_time = "" + _jMarker.get(UPDATE_TIME);
		}

		if (mType.equals("fountain")) {
			// Extracting drinkable, if available
			if (_jMarker.containsKey(DRINKABLE)) {
				attribute = "" + _jMarker.get(DRINKABLE);
			}
		}else{
			if (mType.equals("wc")){
				// Extracting drinkable, if available
				if (_jMarker.containsKey(BARRIERFREE)) {
					attribute = "" + _jMarker.get(BARRIERFREE);
				}
			}
		}

		// Extracting comment, if available
		if (_jMarker.containsKey(COMMENT)) {
			comment = "" + _jMarker.get(COMMENT);
		}

		// Extracting grade, if available
		if (!_jMarker.containsKey(GRADE)) {
			grade = "" + _jMarker.get(GRADE);
		}

		marker.put("id", id);
		marker.put("lat", lat);
		marker.put("lng", lng);
		marker.put("address", address);
		marker.put("type", mType);
		marker.put("attribute", attribute);
		marker.put("comment", comment);
		marker.put(STREET, street);
		marker.put(CITY, city);
		marker.put(ZIP, zip);
		marker.put(FIRSTNAME, firstname);
		marker.put(GRADE, grade);
		marker.put(SURNAME, surename);
		marker.put(UPDATE_TIME, update_time);
		marker.put(CREATION_TIME, creation_time);

		return marker;
	}
}
