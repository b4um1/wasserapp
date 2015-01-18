package at.fhhgb.mc.wasserapp.waterlevel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import at.fhhgb.mc.wasserapp.waterlevel.model.MeasuringPoint;
import at.fhhgb.mc.wasserapp.waterlevel.model.NotificationModel;

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
	
	/**
	 * Store notification list.
	 */
	public static void storeNotificationList(Context _context, ArrayList<NotificationModel> _notificationList) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(_context);
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(_notificationList);
		editor.putString("notification", json);
		editor.commit();
	}
	
	/**
	 * Load notification list.
	 */
	public static ArrayList<NotificationModel> loadNotificationList(Context _context) {
		ArrayList<NotificationModel> notificationList = new ArrayList<NotificationModel>();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(_context);
		Gson gson = new Gson();
		String json = prefs.getString("notification", "");
		Type type = new TypeToken<ArrayList<NotificationModel>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			notificationList = gson.fromJson(json, type);
		}
		return notificationList;
	}
}
