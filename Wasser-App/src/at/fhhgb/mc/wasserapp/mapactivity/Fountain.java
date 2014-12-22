package at.fhhgb.mc.wasserapp.mapactivity;

import android.media.Image;
import at.fhhgb.mc.wasserapp.R;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Fountain.
 */
public class Fountain extends MarkerObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public Fountain() {
		super();
		setM_type("fountain");
	}

	
	/**
	 * Here you can set the icon of the marker. You can choose between drinkable/not drinkable
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
