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

	/**
	 * Instantiates a new fountain.
	 */
	public Fountain() {
		super();
		setM_icon(true);
		setM_type("fountain");
	}

	
	/* (non-Javadoc)
	 * @see at.fhhgb.mc.wasserapp.mapactivity.MarkerObject#setM_icon(boolean)
	 */
	public void setM_icon(boolean isAccessibleIsDrinkable) {
		if (isAccessibleIsDrinkable) {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_drinkable));
		} else {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_notdrinkable));
		}
	}

	// parameter( _checkboxProof) is string because the value in the mysql table
	// is also a string value
	
	/* (non-Javadoc)
	 * @see at.fhhgb.mc.wasserapp.mapactivity.MarkerObject#setM_checkboxStringBool(java.lang.String)
	 */
	public void setM_checkboxStringBool(String _checkboxProof) {
		if (_checkboxProof.equals("true")) {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_drinkable));
		} else {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_notdrinkable));
		}
	}

}
