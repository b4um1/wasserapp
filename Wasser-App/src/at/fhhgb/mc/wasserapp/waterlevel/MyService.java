/*
 * 
 */
package at.fhhgb.mc.wasserapp.waterlevel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ParseException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.parser.WaterlevelJSONParser;
import at.fhhgb.mc.wasserapp.waterlevel.model.MeasuringPoint;
import at.fhhgb.mc.wasserapp.waterlevel.model.NotificationModel;

// TODO: Auto-generated Javadoc
/**
 * @author Thomas
 * The Class MyService.
 *
 */
public class MyService extends Service implements Serializable {

	/** The log tag. */
	private final String LOG_TAG = "Service";

	private final String GETLATESTMEASUREMENT = "getLatestMeasurmentById";
	private final String FTPURLOFPHPFUNCTIONS = "http://wasserapp.reecon.eu/rivers.php";

	private final String USER_AGENT = "Mozilla/5.0";

	private int mId;

	/** The m_notification_list. */
	private ArrayList<NotificationModel> m_notification_list;


	private ArrayList<MyNotification> mMyNotificationList;


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	public void onCreate() {
		super.onCreate();
		Log.i(LOG_TAG, "onCreate ... ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	public void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG, "onDestroy ... ");
	}

	public void startNotificationService() {
		for (int i = 0; i < m_notification_list.size(); i++) {
			mId = m_notification_list.get(i).getmMp().getmMeasuringPointId();
			try {
				new RetrieveLatestMeasurement().execute().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void checkForNotification() {
		if (mMyNotificationList != null) {
			for (int i = 0; i < mMyNotificationList.size(); i++) {
				for (int j = 0; j < m_notification_list.size(); j++) {
					if (mMyNotificationList.get(i).getmId() == m_notification_list
							.get(j).getmMp().getmMeasuringPointId()) {

						int currentWaterlevel = mMyNotificationList.get(i)
								.getmCurrentWaterlevel();
						int notificationWaterlevel = m_notification_list.get(j)
								.getmNotificationValue();

						if (m_notification_list.get(j).ismIsSmaller()) {
							if (notificationWaterlevel >= currentWaterlevel) {
								showNotification(j);
							}
						} else {
							if (notificationWaterlevel <= currentWaterlevel) {
								showNotification(j);
							}
						}
					}
				}
			}
		}
	}
	

	/**
	 * @param _index
	 */
	public void showNotification(int _index) {

		String rivername = m_notification_list.get(_index).getmMp()
				.getmRiverName();
		String measuringpointname = m_notification_list.get(_index).getmMp()
				.getmMeasuringPointName();
		int waterlevel = m_notification_list.get(_index)
				.getmNotificationValue();

		String msgText = getString(R.string.notification_msg_1) + " "
				+ rivername + " " + getString(R.string.notification_msg_2)
				+ " " + measuringpointname + " "
				+ getString(R.string.notification_msg_3) + " " + waterlevel
				+ " " + getString(R.string.notification_msg_4);

		Intent showIntent = new Intent(this, ShowMeasuringPointActivity.class);
		MeasuringPoint mp = m_notification_list.get(_index).getmMp();
		showIntent.putExtra("measuringpoint", mp);

		m_notification_list.remove(_index);
		FavsRepository.storeNotificationList(getApplicationContext(),
				m_notification_list);

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				WaterLevelsActivity.class), 0);
		android.app.Notification.Builder builder = new Notification.Builder(
				this);
		builder.setContentTitle("Wasser App Benachrichtigung")
				.setContentText(msgText)
				.setDefaults(Notification.DEFAULT_ALL)
				.setSmallIcon(R.drawable.app_icon)
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_HIGH)
				.addAction(R.drawable.app_icon, "Pegelstaende anzeigen ...", pi);

		Notification notification = new Notification.BigTextStyle(builder)
				.bigText(msgText).build();

		notificationManager.notify(0, notification);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (haveNetworkConnection()) {

			Log.i(LOG_TAG, "onStartCommand ... ");

			m_notification_list = FavsRepository
					.loadNotificationList(getApplicationContext());
			if (m_notification_list != null && m_notification_list.size() != 0) {

				mMyNotificationList = new ArrayList<MyNotification>();
				startNotificationService();

				FavsRepository.storeNotificationList(getApplicationContext(),
						m_notification_list);

			} else {
				stopSelf();
			}
		}
		return Service.START_NOT_STICKY;
	}

	/**
	 * The Class RetrieveTask.
	 *
	 * @author Thomas Kranzer
	 */
	private class RetrieveLatestMeasurement extends
			AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve",
					"entere do in background of retrieve all rivers task: ");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(FTPURLOFPHPFUNCTIONS);

			// add header
			post.setHeader("User-Agent", USER_AGENT);
			StringBuffer result = new StringBuffer();
			try {
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("function",
						GETLATESTMEASUREMENT));
				urlParameters.add(new BasicNameValuePair("measuringpoint_id",
						"" + mId));
				post.setEntity(new UrlEncodedFormEntity(urlParameters));
				HttpResponse response = client.execute(post);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));

					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					// System.out.println(result.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result.toString();
		}

		@Override
		protected void onPostExecute(String _result) {
			super.onPostExecute(_result);
			new ParserTask().execute(_result);
		}
	}

	/**
	 * Background Parser-Task
	 */
	private class ParserTask extends
			AsyncTask<String, Void, List<HashMap<String, String>>> {

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {

			WaterlevelJSONParser waterlevelParser = new WaterlevelJSONParser();

			List<HashMap<String, String>> waterlevelList = new ArrayList<HashMap<String, String>>();
			try {
				waterlevelList = waterlevelParser.parse(params[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return waterlevelList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			if ((result != null) && (result.size() != 0)) {

				HashMap<String, String> parsermap = result.get(0);
				String measuringpointId = parsermap.get("measuringpointId");
				String waterlevel = parsermap.get("waterlevel");

				MyNotification temp = new MyNotification();
				temp.setmId(Integer.parseInt(measuringpointId));
				temp.setmCurrentWaterlevel(Integer.parseInt(waterlevel));

				if (!mMyNotificationList.contains(temp)) {
					mMyNotificationList.add(temp);
				}
				checkForNotification();
			}
		}
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

/**
 * @author Thomas
 *
 */
class MyNotification {
	int mId;
	int mCurrentWaterlevel;

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public int getmCurrentWaterlevel() {
		return mCurrentWaterlevel;
	}

	public void setmCurrentWaterlevel(int mCurrentWaterlevel) {
		this.mCurrentWaterlevel = mCurrentWaterlevel;
	}

}
