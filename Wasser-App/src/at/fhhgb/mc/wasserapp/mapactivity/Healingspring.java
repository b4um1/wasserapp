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
	 * WE NEED AN ICON
	 */
	public void setM_icon(boolean _isDrinkable) {
		if (_isDrinkable) {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_drinkable));
		} else {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_notdrinkable));
		}
	}
}