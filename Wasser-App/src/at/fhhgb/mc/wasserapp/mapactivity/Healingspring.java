package at.fhhgb.mc.wasserapp.mapactivity;

import at.fhhgb.mc.wasserapp.R;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class Healingspring extends MyMarkerObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public Healingspring() {
		super();
		setM_type("healingspring");
	}

	/**
	 * Sets the icon of the healingspring
	 */
	public void setM_icon(boolean _isDrinkable) {
		this.setM_icon(BitmapDescriptorFactory
				.fromResource(R.drawable.ic_position_healingspring));
	}
}