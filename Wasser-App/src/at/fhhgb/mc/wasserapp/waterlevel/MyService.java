/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.waterlevel.model.MeasuringPoint;

// TODO: Auto-generated Javadoc
/**
 * The Class MyService.
 */
public class MyService extends Service implements Serializable {

	/** The log tag. */
	private final String LOG_TAG = "Service";
	
	/** The m_notification_list. */
	private ArrayList<NotificationModel> m_notification_list;
	
	/** The m list favs. */
	private ArrayList<MeasuringPoint> mListFavs;

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	public void onCreate() {
		super.onCreate();
		Log.i(LOG_TAG, "onCreate ... ");
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	public void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG, "onDestroy ... ");
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (haveNetworkConnection()) {
			loadNotificationList();
			if (m_notification_list != null && m_notification_list.size() != 0) {
				refreshFavs();
				Log.i(LOG_TAG,
						"onStartCommand ... "
								+ m_notification_list.get(0)
										.getmNotificationValue());

				ArrayList<Integer> remind = new ArrayList<Integer>();
				for (int i = 0; i < m_notification_list.size(); i++) {
					for (int j = 0; j < mListFavs.size(); j++) {
						if ((m_notification_list.get(i).getmMp()
								.getmRiverName()
								.equals(mListFavs.get(j).getmRiverName()) && (m_notification_list
								.get(i).getmMp().getmMeasuringPointName()
								.equals(mListFavs.get(j)
										.getmMeasuringPointName())))) {
//							if (m_notification_list.get(i)
//									.getmNotificationValue() <= mListFavs
//									.get(j).getmWaterlevel()) {
//
//								remind.add(i);
//
//								String rivername = m_notification_list.get(i)
//										.getmMp().getmRiverName();
//								String measuringpointname = m_notification_list
//										.get(i).getmMp()
//										.getmMeasuringPointName();
//								float waterlevel = m_notification_list.get(i)
//										.getmNotificationValue();
//
//								String msgText = getString(R.string.notification_msg_1) + rivername
//										+ getString(R.string.notification_msg_2) + measuringpointname
//										+ getString(R.string.notification_msg_3)
//										+ waterlevel + getString(R.string.notification_msg_4);
//								
////								Intent showIntent = new Intent(this, ShowMeasuringPointActivity.class);
////								MeasuringPoint mp = m_notification_list.get(i).getmMp();
////								showIntent.putExtra("measuringpoint", mp);
//
//								NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//								PendingIntent pi = PendingIntent.getActivity(
//										this, 0, new Intent(this, WaterLevelsActivity.class), 0);
//								android.app.Notification.Builder builder = new Notification.Builder(
//										this);
//								builder.setContentTitle(
//										"Wasser App Benachrichtigung")
//										.setContentText("Big text Notification")
//										.setDefaults(Notification.DEFAULT_ALL)
//										.setSmallIcon(R.drawable.app_icon)
//										.setAutoCancel(true)
//										.setPriority(Notification.PRIORITY_HIGH)
//										.addAction(R.drawable.app_icon,
//												"Pegelstaende anzeigen ...", pi);
//									
//								Notification notification = new Notification.BigTextStyle(
//										builder).bigText(msgText).build();
//
//								notificationManager.notify(0, notification);
//
//
//							}
						}
					}
				}
				for (int i = 0; i < remind.size(); i++) {
					m_notification_list.remove(i);
				}
				storeNotificationList();
			} else {
				stopSelf();
			}
		}
		return Service.START_NOT_STICKY;
	}

	/**
	 * Refresh favs.
	 */
	public void refreshFavs() {
		loadMListFavs();
		String retrieveQuery = "";
		try {
			retrieveQuery = new RetrieveTask().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Set<String> fieldNames = new HashSet<String>();
		StajParser stajParser = null;
		stajParser = new StajParser(retrieveQuery);
		int counter = 0;
		// evaluate, how many entries are in the database
		while (stajParser.hasNext()) {
			JsonStreamElement next = stajParser.next();
			if (next.jsonStreamElementType() == JsonStreamElementType.START_FIELD) {
				fieldNames.add(next.text());
				counter++;
			}
		}
		ArrayList<MeasuringPoint> mpointList = new ArrayList<MeasuringPoint>();

		for (int i = 0; i < (counter - 1) / 3; i++) {
			try {
				String measuringpointname = new JdomParser().parse(
						retrieveQuery).getStringValue("measuringpoint", i,
						"measuringpointname");
				float waterlevel = Float.parseFloat(new JdomParser().parse(
						retrieveQuery).getStringValue("measuringpoint", i,
						"waterlevel"));
				String river = new JdomParser().parse(retrieveQuery)
						.getStringValue("measuringpoint", i, "river");

//				mpointList.add(new MeasuringPoint(measuringpointname,
//						waterlevel, river));

			} catch (InvalidSyntaxException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < mpointList.size(); i++) {
			for (int j = 0; j < mListFavs.size(); j++) {
				if ((mpointList.get(i).getmRiverName()
						.equals(mListFavs.get(j).getmRiverName()) && (mpointList
						.get(i).getmMeasuringPointName().equals(mListFavs
						.get(j).getmMeasuringPointName())))) {
//					mListFavs.get(j).setmWaterlevel(
//							mpointList.get(i).getmWaterlevel());
				}
			}
		}
		storeMListFavs();
	}

	/**
	 * The Class RetrieveTask.
	 */
	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			Log.e("retrieve", "entere do in background of retrieve task: ");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=retrieveMeasuringpoints";
			URL url = null;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
	}

	/**
	 * Load notification list.
	 */
	public void loadNotificationList() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = prefs.getString("notification", "");
		Type type = new TypeToken<ArrayList<NotificationModel>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			m_notification_list = gson.fromJson(json, type);
		} else {
			m_notification_list = new ArrayList<NotificationModel>();
		}
	}

	/**
	 * Store notification list.
	 */
	public void storeNotificationList() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(m_notification_list);
		editor.putString("notification", json);
		editor.commit();
	}

	/**
	 * Load m list favs.
	 */
	public void loadMListFavs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = prefs.getString("list", "");
		Type type = new TypeToken<ArrayList<MeasuringPoint>>() {
		}.getType();
		if (gson.fromJson(json, type) != null) {
			mListFavs = gson.fromJson(json, type);
		} else {
			mListFavs = new ArrayList<MeasuringPoint>();
		}
	}

	/**
	 * Store m list favs.
	 */
	public void storeMListFavs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(mListFavs);
		editor.putString("list", json);
		editor.commit();
	}

	/**
	 * This method checks if the app is connected to the internet Then it
	 * returns true, else it returns false.
	 * 
	 * @return boolean
	 */
	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}

		return haveConnectedWifi || haveConnectedMobile;
	}
}
