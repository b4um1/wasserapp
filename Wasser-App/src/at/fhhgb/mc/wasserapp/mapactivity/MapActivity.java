/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.gi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ViewFlipper;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;
import at.fhhgb.mc.wasserapp.mapactivity.detail.FountainDetailsActivity;
import at.fhhgb.mc.wasserapp.more.LoginActivity;
import at.fhhgb.mc.wasserapp.more.MoreActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class MapActivity.
 *
 * @author danneatu
 */
/**
 * @author danneatu
 * 
 */
public class MapActivity extends Activity implements OnMapClickListener,
OnMapLongClickListener, OnMarkerClickListener, InfoWindowAdapter,
OnInfoWindowClickListener, OnQueryTextListener, OnClickListener,
OnCheckedChangeListener, OnMarkerDragListener, OnShowListener,
LocationListener {

	/** The m_btn_save. */
	private Button m_btn_save;
	
	/** The m_btn_delete. */
	private Button m_btn_delete;
	
	/** The m_btn_go_to_marker. */
	private Button m_btn_go_to_marker;
	
	/** The m_markertype. */
	private String m_markertype;

	/** The m_dialog. */
	private Dialog m_dialog;
	
	/** The m_dialog_save_marker_alert. */
	private Dialog m_dialog_save_marker_alert;

	/** The m_searchview. */
	private SearchView m_searchview;
	
	/** The m_dialog_checkbox. */
	private CheckBox m_dialog_checkbox;
	
	/** The m_tv_title. */
	private TextView m_tv_title;

	/** The map fragment. */
	private com.google.android.gms.maps.MapFragment mapFragment;
	
	/** The m_googlemap. */
	private GoogleMap m_googlemap;
	
	/** The m_context. */
	private Context m_context = this;
	
	/** The m_current location. */
	private Location m_currentLocation;

	/** The m_marker_object. */
	private MarkerObject m_marker_object;
	
	/** The m_clicked_marker. */
	private Marker m_clicked_marker;
	
	/** The m_saved_marker_objects. */
	private ArrayList<MarkerObject> m_saved_marker_objects;

	/** The m_markeroption. */
	private MarkerOptions m_markeroption;

	/** The m_temp_lat long. */
	private LatLng m_temp_latLong;

	/** The m_checkbox. */
	private boolean m_checkbox;
	
	/** The m_super user. */
	private boolean m_superUser;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (haveNetworkConnection() && isGooglePlayServicesAvailable()) {
			setContentView(R.layout.actionbar);

			ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
			vf.setDisplayedChild(2);
			overridePendingTransition(0, 0);

			HomeActivity.setAllButtonListener(
					(ViewGroup) findViewById(R.id.rootActionbar), this);
			HomeActivity.setPositionToMark(this);

			Button actionBarButton = (Button)findViewById(R.id.b_position);

			if (m_superUser
					&& actionBarButton.getText().equals(
							getString(R.string.actionbar_mark))) {
				actionBarButton.setPressed(true);
			} else if (actionBarButton.getText().equals(
					getString(R.string.actionbar_positioin))) {
				actionBarButton.setPressed(true);
			}

			m_saved_marker_objects = new ArrayList<MarkerObject>();
			m_marker_object = new MarkerObject();

			actualizeMapActivity();
			mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
					.findFragmentById(R.id.map);
			mapFragment.setRetainInstance(true);
			m_googlemap = mapFragment.getMap();
			m_googlemap.setOnMapClickListener(this);
			m_googlemap.setOnMapLongClickListener(this);
			m_googlemap.setOnMarkerClickListener(this);
			m_googlemap.setOnMarkerDragListener(this);
			m_googlemap.setInfoWindowAdapter(this);
			m_googlemap.setOnInfoWindowClickListener(this);

			if (m_googlemap != null) {
				setUpMap();
			}

			m_searchview = (SearchView) findViewById(R.id.sv_maps_search);
			m_searchview.setOnQueryTextListener(this);
			m_searchview.setQueryHint("Adresse suchen....");

			m_dialog = new Dialog(MapActivity.this);
			m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			m_dialog.setContentView(R.layout.custom_marker_dialog);
			m_dialog.setOnShowListener(this);
			m_dialog.setCancelable(false);
			m_dialog.setCanceledOnTouchOutside(false);
			m_dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));

			

			m_btn_save = (Button) m_dialog.findViewById(R.id.btn_saveMarker);
			m_btn_save.setOnClickListener(this);
			m_btn_delete = (Button) m_dialog.findViewById(R.id.btn_delete);
			m_btn_delete.setOnClickListener(this);

			m_tv_title = ((TextView) m_dialog.findViewById(R.id.tv_address));

			m_dialog_checkbox = (CheckBox) m_dialog
					.findViewById(R.id.cb_drink_or_bar);
			if (m_markertype.equals("wc")) {
				m_dialog_checkbox.setText(getString(R.string.accessible));
			} else if (m_markertype.equals("fountain")) {
				m_dialog_checkbox.setText(getString(R.string.drinkable));
			}
			m_checkbox = true;
			m_dialog_checkbox.setOnCheckedChangeListener(this);

			Button b = (Button) m_dialog.findViewById(R.id.btn_takepicture);
			b.setVisibility(View.GONE);
			if (m_markertype.equals("fountain")) {
				b.setVisibility(View.VISIBLE);
			}

		} else {
			Toast t = Toast.makeText(this,
					"turn on your internet connection!!!!", Toast.LENGTH_LONG);
			t.show();
		}
	}

	/**
	 * Actualize map activity.
	 */
	private void actualizeMapActivity() {
		Intent i = getIntent();
		Log.e("intent in mapactivity", i.getStringExtra("m_markertype"));
		m_superUser = i.getBooleanExtra("user", false);
		m_markertype = i.getStringExtra("m_markertype");

		TextView tv = (TextView) findViewById(R.id.tv_maps_title);
		if (m_superUser) {
			if (m_markertype.equals("fountain")) {
				tv.setText(getString(R.string.map_mark_fountain));
			} else {
				tv.setText(getString(R.string.map_mark_wc));
			}
		} else {
			if (m_markertype.equals("fountain")) {
				tv.setText(getString(R.string.map_fountain));
			} else if (m_markertype.equals("wc")) {
				tv.setText(getString(R.string.map_wc));
			} else {
				tv.setText(getString(R.string.map_all));
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (haveNetworkConnection() && isGooglePlayServicesAvailable()) {

			overridePendingTransition(0, 0);
			HomeActivity.setPositionToMark(this);
		Button actionBarButton = (Button)findViewById(R.id.b_position);
		if(m_superUser && actionBarButton.getText().equals(getString(R.string.actionbar_mark))){
			actionBarButton.setPressed(true);
		}else if(actionBarButton.getText().equals(getString(R.string.actionbar_positioin))){
			actionBarButton.setPressed(true);
		}

			

			actualizeMapActivity();
			setUpMap();
			m_googlemap.clear();
			getDataFromServer();
		} else {
			Toast t = Toast.makeText(this,
					"turn on your internet connection!!!!", Toast.LENGTH_LONG);
			t.show();
		}
		// setUpMap();
		// m_googlemap.clear();
		// getDataFromServer();
		//
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
		if (haveConnectedWifi || haveConnectedMobile) {
			Toast t = Toast.makeText(this, "You are online!!!!",
					Toast.LENGTH_SHORT);
			t.show();
		} else {
			Toast t = Toast.makeText(this,
					"turn on your internet connection!!!!", Toast.LENGTH_SHORT);
			t.show();
		}

		return haveConnectedWifi || haveConnectedMobile;
	}

	/**
	 * This method is for the setup of the map. The camera moves to the current
	 * position of the user.
	 */
	private void setUpMap() {
		m_googlemap.setMyLocationEnabled(true);
		while (m_currentLocation == null) {
			m_currentLocation = getLocation();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			if (m_currentLocation == null) {
				Log.d("CurLatLng", "is null");
			} else {
				Log.d("CurLatLng", "is NOT null");
			}
		}
		LatLng latLng = new LatLng(m_currentLocation.getLatitude(),
				m_currentLocation.getLongitude());
		// move the zoom point to the location in the map
		m_googlemap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		m_googlemap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	/**
	 * This Method uses Location Manager to get the current location of the
	 * user.
	 * 
	 * @return the current location of the user
	 */
	private Location getLocation() {

		Location location = null;
		LocationManager locationManager;

		try {
			locationManager = (LocationManager) getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {

				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 1000, 25, this);
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
					// if GPS Enabled get lat/long using GPS Services
					if (isGPSEnabled) {
						if (location == null) {
							locationManager.requestLocationUpdates(
									LocationManager.GPS_PROVIDER, 1000, 25,
									this);
							Log.d("GPS", "GPS Enabled");
							if (locationManager != null) {
								location = locationManager
										.getLastKnownLocation(LocationManager.GPS_PROVIDER);

							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Get the address of the LatLng coordinates.
	 *
	 * @param _latLng the _lat lng
	 * @return Address list with the address of the LatLng point
	 */
	public List<Address> getAddress(LatLng _latLng) {
		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(m_context);

			if (_latLng.latitude != 0 || _latLng.longitude != 0) {

				addresses = geocoder.getFromLocation(_latLng.latitude,
						_latLng.longitude, 1);

				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				String country = addresses.get(0).getAddressLine(2);
				Log.d("TAG", "address= " + address + ", city =" + city
						+ ", country= " + country);
				return addresses;
			} else {
				Toast.makeText(m_context, "latitude and longitude are null",
						Toast.LENGTH_LONG).show();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * An AsyncTask class for accessing the GeoCoding Web Service.
	 */
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Address> addresses) {
			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_LONG).show();
			}
			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {
				Address address = addresses.get(i);
				// Creating an instance of GeoPoint, to display in Google Map
				LatLng latLng = new LatLng(address.getLatitude(),
						address.getLongitude());
				if (i == 0) {
					m_googlemap.animateCamera(CameraUpdateFactory
							.newLatLng(latLng));
					if (m_superUser) {
						m_checkbox = true;
						createNewMarkerObject(latLng);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMapClickListener#onMapClick(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMapClick(LatLng _latLng) {
		// get the address of the position where the user taps on the map.
		// getAddress(_latLng);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.maps.GoogleMap.OnMapLongClickListener#onMapLongClick
	 * (com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onMapLongClick(LatLng _latLng) {
		if (m_superUser) {
			createNewMarkerObject(_latLng);
			m_checkbox = true;
			m_temp_latLong = _latLng;
		}

	}

	/**
	 * Creates the new marker object.
	 *
	 * @param _latLng the _lat lng
	 */
	public void createNewMarkerObject(LatLng _latLng) {
		m_btn_save.setText("speichern");
		List<Address> addresslist = getAddress(_latLng);
		String address = addresslist.get(0).getAddressLine(0) + ", "
				+ addresslist.get(0).getAddressLine(1);
		if (m_markertype.equals("fountain")) {
			m_marker_object = new Fountain();
		}
		if (m_markertype.equals("wc")) {
			m_marker_object = new Wc();
		}
		m_marker_object.setM_latLng(_latLng);
		m_marker_object.setAdress(address);
		m_marker_object.setM_icon(m_checkbox);
		m_marker_object.setM_checkbox(m_checkbox);
		m_markeroption = new MarkerOptions();
		m_markeroption.title(m_marker_object.getM_address());
		m_markeroption.position(_latLng);
		m_markeroption.icon(m_marker_object.getM_icon());
		m_markeroption.draggable(false);

		m_clicked_marker = m_googlemap.addMarker(m_markeroption);
		m_dialog_checkbox.setChecked(m_checkbox);

		m_marker_object.setM_id(""+m_clicked_marker.getPosition());
		m_tv_title.setText(m_marker_object.getM_address());
		m_dialog.show();
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.InfoWindowAdapter#getInfoWindow(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public View getInfoWindow(Marker _marker) {
		m_clicked_marker = _marker;
		View myContentView = getLayoutInflater().inflate(
				R.layout.custom_marker_infowindow, null);
		TextView tvTitle = ((TextView) myContentView
				.findViewById(R.id.tv_title));
		tvTitle.setText(_marker.getTitle());

		return myContentView;
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMarkerClickListener#onMarkerClick(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public boolean onMarkerClick(Marker _marker) {
		m_clicked_marker = _marker;
		m_marker_object = getMarkerObject(""+_marker.getPosition());
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.InfoWindowAdapter#getInfoContents(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public View getInfoContents(Marker _marker) {

		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener#onInfoWindowClick(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public void onInfoWindowClick(Marker _marker) {
		TextView tvTitle = ((TextView) m_dialog.findViewById(R.id.tv_address));
		tvTitle.setText(_marker.getTitle());
		_marker.hideInfoWindow();

		if (!m_superUser) {
			Intent goToDetailsActivity = new Intent(this,
					FountainDetailsActivity.class);
			goToDetailsActivity
			.putExtra("address", m_clicked_marker.getTitle());
			goToDetailsActivity.putExtra("lat",
					"" + m_clicked_marker.getPosition().latitude);
			goToDetailsActivity.putExtra("lng",
					"" + m_clicked_marker.getPosition().longitude);
			goToDetailsActivity.putExtra("bool", true);
			m_marker_object = getMarkerObject(""+_marker.getPosition());
			this.startActivity(goToDetailsActivity);

		} else {
			m_marker_object = getMarkerObject(""+_marker.getPosition());
			m_dialog.show();
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _button) {
		Intent i;
		switch (_button.getId()) {
		// Actionbar
		case R.id.b_home:
			i = new Intent(this, HomeActivity.class);
			break;
		case R.id.b_position:
			i = new Intent();
			Intent map;
			if (LoginActivity.superUser) {
				map = new Intent(this, ChooseMarkerActivity.class);
				map.putExtra("user", true);
			} else {
				map = new Intent(this, MapActivity.class);
				map.putExtra("user", false);
				map.putExtra("m_markertype", "all");
			}
			startActivity(map);
			break;
		case R.id.b_news:
			i = new Intent(this, WebViewActivity.class);
			break;
		case R.id.b_more:
			i = new Intent(this, MoreActivity.class);
			break;
		case R.id.b_back:
			i = new Intent();
			onBackPressed();
			break;
			// End Actionbar
		default:
			i = new Intent();
		}
		if (i != null && i.getComponent() != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}

		if (_button.equals(m_btn_save)) {
			if (!m_saved_marker_objects.contains(m_marker_object)) {
				m_saved_marker_objects.add(m_marker_object);
				sendMarkerToServer(m_marker_object);
				m_dialog_checkbox.setChecked(m_marker_object.getM_checkbox());
				Toast.makeText(this, "Marker gespeichert", Toast.LENGTH_LONG)
				.show();
			} else {
				Toast.makeText(this, "Marker geupdatet", Toast.LENGTH_LONG)
				.show();
				m_clicked_marker.hideInfoWindow();
				m_marker_object.setAdress(m_clicked_marker.getTitle());
				Log.d("new marker address: ", m_marker_object.getM_address());
				m_dialog_checkbox.setChecked(m_marker_object.getM_checkbox());
				updateDataOnServer(m_marker_object);
			}
			m_dialog.dismiss();

		} else if (_button.equals(m_btn_delete)) {
			if (m_saved_marker_objects.contains(m_marker_object)) {
				m_saved_marker_objects.remove(m_marker_object);
				deleteMarkerFromServer(m_marker_object);
				Toast.makeText(this, "Marker geloescht", Toast.LENGTH_LONG)
				.show();
			}
			m_clicked_marker.remove();
			m_dialog.dismiss();
		} else if (_button.equals(m_btn_go_to_marker)) {
			m_dialog_save_marker_alert.dismiss();
			m_googlemap.moveCamera(CameraUpdateFactory
					.newLatLng(m_temp_latLong));
			m_googlemap.animateCamera(CameraUpdateFactory.zoomTo(15));
		}

	}

	/* (non-Javadoc)
	 * @see android.widget.SearchView.OnQueryTextListener#onQueryTextChange(java.lang.String)
	 */
	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.SearchView.OnQueryTextListener#onQueryTextSubmit(java.lang.String)
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		String location = query;
		if (location != null && !location.equals("")) {
			new GeocoderTask().execute(location);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.equals(m_dialog_checkbox)) {
			if (isChecked) {
				m_marker_object.setM_icon(true);
				m_marker_object.setM_checkbox(true);

			} else {
				m_marker_object.setM_checkbox(false);
				m_marker_object.setM_icon(false);
			}
			// if (m_clicked_marker != null) {
			// Log.e("onCheckedChange: ", m_marker_object.getM_id());
			m_clicked_marker.setIcon(m_marker_object.getM_icon());
			// }
		}
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMarkerDragListener#onMarkerDrag(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public void onMarkerDrag(Marker arg0) {
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMarkerDragListener#onMarkerDragEnd(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public void onMarkerDragEnd(Marker _marker) {
		m_temp_latLong = _marker.getPosition();

		deleteMarkerFromServer(m_marker_object);
		m_saved_marker_objects.remove(m_marker_object);
		createNewMarkerObject(_marker.getPosition());
		_marker.remove();
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMarkerDragListener#onMarkerDragStart(com.google.android.gms.maps.model.Marker)
	 */
	@Override
	public void onMarkerDragStart(Marker _marker) {
		m_clicked_marker = _marker;

		m_marker_object = getMarkerObject(""+_marker.getPosition());
		m_checkbox = m_marker_object.getM_checkbox();

		_marker.hideInfoWindow();
	}

	/**
	 * Gets the marker object.
	 *
	 * @param _id the _id
	 * @return the marker object
	 */
	private MarkerObject getMarkerObject(String _id) {
		if (_id != null) {

			for (int i = 0; i < m_saved_marker_objects.size(); i++) {
				if (_id.equals(m_saved_marker_objects.get(i).getM_id())) {
					return m_saved_marker_objects.get(i);
				} else {
					Log.e("getMmarker:", "marker not found");
				}
			}
		}
		return null;
	}

	/**
	 * Checks if is google play services available.
	 *
	 * @return true, if is google play services available
	 */
	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (status == ConnectionResult.SUCCESS) {
			return (true);
		} else {
			Toast.makeText(this, "Google Play ist not available",
					Toast.LENGTH_LONG).show();
			Log.d("google play", "google playservices not availible");

		}
		return false;
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnShowListener#onShow(android.content.DialogInterface)
	 */
	@Override
	public void onShow(DialogInterface _dialog) {
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Send marker to server.
	 *
	 * @param _markerObject the _marker object
	 */
	private void sendMarkerToServer(MarkerObject _markerObject) {
		new SaveTask().execute(_markerObject);
	}

	/**
	 * Delete marker from server.
	 *
	 * @param _m the _m
	 */
	private void deleteMarkerFromServer(MarkerObject _m) {

		new DeleteTask().execute(_m.getM_latLng());
	}

	/**
	 * Gets the data from server.
	 *
	 * @return the data from server
	 */
	private void getDataFromServer() {
		new RetrieveTask().execute();
	}

	/**
	 * Update data on server.
	 *
	 * @param _marker_object the _marker_object
	 */
	private void updateDataOnServer(MarkerObject _marker_object) {

		new UpdateTask().execute(_marker_object);
	}

	// Background thread to save the location in remote MySQL server
	/**
	 * The Class SaveTask.
	 */
	private class SaveTask extends AsyncTask<MarkerObject, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(MarkerObject... params) {

			Log.d("send", "send object to database");

			System.out.println(params[0]);

			LatLng latLng = params[0].getM_latLng();

			String id = params[0].getM_id();
			String lat = Double.toString(latLng.latitude);
			String lng = Double.toString(latLng.longitude);

			String address = params[0].getM_address();
			String type = params[0].getM_type();
			String bool = params[0].getM_checkboxStringBool();
			String comment = params[0].getM_comment();
			String imglink = params[0].getM_imageLink();

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=save";
			URL url = null;
			try {
				url = new URL(strUrl);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());
				outputStreamWriter.write("id=" + id + "&lat=" + lat + "&lng="
						+ lng + "&address=" + address + "&type=" + type
						+ "&bool=" + bool + "&comment=" + comment + "&imglink="
						+ imglink);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				StringBuffer sb = new StringBuffer();

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("save sb:", sb.toString());

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	// Background task to retrieve locations from remote mysql server
	/**
	 * The Class RetrieveTask.
	 */
	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			Log.d("retrieve", "entere do in background of retrieve task: ");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=retrieve";
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

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}

	}

	// Background thread to parse the JSON data retrieved from MySQL server
	/**
	 * The Class ParserTask.
	 */
	private class ParserTask extends
	AsyncTask<String, Void, List<HashMap<String, String>>> {
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			MarkerJSONParser markerParser = new MarkerJSONParser();
			JSONObject json = null;
			try {
				json = new JSONObject(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			List<HashMap<String, String>> markersList = markerParser
					.parse(json);
			return markersList;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			for (int i = 0; i < result.size(); i++) {

				HashMap<String, String> parsermap = result.get(i);

				double lat = Double.parseDouble(parsermap.get("lat"));
				double lng = Double.parseDouble(parsermap.get("lng"));
				LatLng latLng = new LatLng(lat, lng);
				String id = parsermap.get("id");
				String address = parsermap.get("address");
				String bool = parsermap.get("bool");
				String type = parsermap.get("type");
				String comment = parsermap.get("comment");
				String imglink = parsermap.get("imglink");

				MarkerObject m = new MarkerObject();

				if (type.equals("fountain")) {
					m = new Fountain();
				} else if (type.equals("wc")) {
					m = new Wc();
				}
				m.setM_id(id);
				m.setM_latLng(latLng);
				m.setAdress(address);
				m.setM_checkboxStringBool(bool);
				m.setM_icon(Boolean.parseBoolean(bool));
				m.setM_type(type);
				m.setM_comment(comment);
				m.setM_imageLink(imglink);
				Log.e("getmarkerfromserver: ", m.getM_id());
				if (type.equals(m_markertype)) {
					addMarkerFromMySqlServerToMap(m);
				} else if (m_markertype.equals("all")) {
					addMarkerFromMySqlServerToMap(m);
				}

			}
		}
	}

	/**
	 * Adds the marker from my sql server to map.
	 *
	 * @param _m the _m
	 */
	public void addMarkerFromMySqlServerToMap(MarkerObject _m) {
		MarkerOptions m = new MarkerOptions();
		m.title(_m.getM_address());
		m.position(_m.getM_latLng());
		m.icon(_m.getM_icon());
		m.draggable(false);
		m_saved_marker_objects.add(_m);
		m_googlemap.addMarker(m);
	}

	// Background task to delete locations from remote mysql server
	/**
	 * The Class DeleteTask.
	 */
	private class DeleteTask extends AsyncTask<LatLng, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(LatLng... params) {

			Log.d("send", "delete object from database");

			System.out.println(params[0]);

			String lat = Double.toString(params[0].latitude);
			String lng = Double.toString(params[0].longitude);
			Log.d("lat:", lat);
			Log.d("lng:", lng);
			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=delete";
			URL url = null;
			try {
				url = new URL(strUrl);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());

				outputStreamWriter.write("lat=" + lat + "&lng=" + lng);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				StringBuffer sb = new StringBuffer();
				Log.d("url", url.toString());

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("stringbuffer", sb.toString());

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	// Background task to update locations attributes on the remote mysql server
	/**
	 * The Class UpdateTask.
	 */
	private class UpdateTask extends AsyncTask<MarkerObject, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(MarkerObject... params) {

			Log.d("update", "update object in database");
			Log.d("lat", "" + Double.toString(params[0].getM_latLng().latitude));
			Log.d("lng",
					"" + Double.toString(params[0].getM_latLng().longitude));
			Log.d("checkbox", "" + params[0].getM_checkboxStringBool());

			System.out.println(params[0]);
			String lat = Double.toString(params[0].getM_latLng().latitude);
			String lng = Double.toString(params[0].getM_latLng().longitude);
			String checkboxString = params[0].getM_checkboxStringBool();
			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=update";
			URL url = null;
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());

				outputStreamWriter.write("lat=" + lat + "&lng=" + lng
						+ "&bool=" + checkboxString);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				StringBuffer sb = new StringBuffer();
				Log.d("url", url.toString());

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("stringbuffer", sb.toString());

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (!LoginActivity.superUser) {
			Intent i = new Intent(this, HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		} else {
			super.onBackPressed();
		}
	}
}
