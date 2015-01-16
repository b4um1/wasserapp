package at.fhhgb.mc.wasserapp.waterlevel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FavsRepository {
	
	private static String mFavsKeyword = "favs";

	public static ArrayList<MeasuringPoint> loadFavs(Context _context) {
		ArrayList<MeasuringPoint> favsList = new ArrayList<MeasuringPoint>();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(_context);
		Gson gson = new Gson();
		String json = prefs.getString(mFavsKeyword, "");
		Type type = new TypeToken<ArrayList<MeasuringPoint>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			favsList = gson.fromJson(json, type);
		}
		return favsList;
	}
	
	public static void storeFavs(Context _context, ArrayList<MeasuringPoint> _favs) {
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(_context);
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(_favs);
		editor.putString(mFavsKeyword, json);
		editor.commit();
	}
}
