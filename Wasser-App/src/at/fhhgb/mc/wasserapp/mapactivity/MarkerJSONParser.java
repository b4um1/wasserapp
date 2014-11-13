/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class MarkerJSONParser.
 */
public class MarkerJSONParser {
	
	/**
	 *  Receives a JSONObject and returns a list.
	 *
	 * @param jObject the j object
	 * @return the list
	 */
	public List<HashMap<String,String>> parse(JSONObject jObject){		
		
		JSONArray jMarkers = null;
		try {			
			/** Retrieves all the elements in the 'markers' array */
			jMarkers = jObject.getJSONArray("markers");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getMarkers with the array of json object
		 * where each json object represent a marker
		 */
		return getMarkers(jMarkers);
	}
	
	
	/**
	 * Gets the markers.
	 *
	 * @param jMarkers the j markers
	 * @return the markers
	 */
	private List<HashMap<String, String>> getMarkers(JSONArray jMarkers){
		int markersCount = jMarkers.length();
		List<HashMap<String, String>> markersList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> marker = null;	

		/** Taking each marker, parses and adds to list object */
		for(int i=0; i<markersCount;i++){
			try {
				/** Call getMarker with marker JSON object to parse the marker */
				marker = getMarker((JSONObject)jMarkers.get(i));
				markersList.add(marker);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}
		
		return markersList;
	}
	
	/**
	 *  Parsing the Marker JSON object.
	 *
	 * @param jMarker the j marker
	 * @return the marker
	 */
	private HashMap<String, String> getMarker(JSONObject jMarker){

		HashMap<String, String> marker = new HashMap<String, String>();
		String id = "-NA-";
		String lat = "-NA-";
		String lng ="-NA-";
		String address="-NA-";
		String type="-NA-";
		String bool="-NA-";
		String comment="-NA-";
		String imglink="-NA-";

		
		try {
			
			
			// Extracting latitude, if available
			if(!jMarker.isNull("id")){
				id = jMarker.getString("id");
			}
			
			// Extracting latitude, if available
			if(!jMarker.isNull("lat")){
				lat = jMarker.getString("lat");
			}
			
			// Extracting longitude, if available
			if(!jMarker.isNull("lng")){
				lng = jMarker.getString("lng");
			}	
			
			if(!jMarker.isNull("address")){
				address = jMarker.getString("address");
			}
			
			if(!jMarker.isNull("type")){
				type = jMarker.getString("type");
			}
			
			if(!jMarker.isNull("bool")){
				bool = jMarker.getString("bool");
			}
			if(!jMarker.isNull("comment")){
				comment = jMarker.getString("comment");
			}
			
			if(!jMarker.isNull("imglink")){
				imglink = jMarker.getString("imglink");
			}
			
			marker.put("id", id);
			marker.put("lat", lat);
			marker.put("lng", lng);		
			marker.put("address", address);			
			marker.put("type", type);			
			marker.put("bool", bool);	
			marker.put("comment", comment);			
			marker.put("imglink", imglink);			

			
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return marker;
	}
}
