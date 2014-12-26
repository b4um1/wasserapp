/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity;

import at.fhhgb.mc.wasserapp.R;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Wc.
 */
public class Wc extends MyMarkerObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wc.
	 */
	public Wc (){
		super();
		setM_icon(true);
		setM_type("wc");
	}
	
	/* (non-Javadoc)
	 * @see at.fhhgb.mc.wasserapp.mapactivity.MarkerObject#setM_icon(boolean)
	 */
	public void setM_icon(boolean isAccessibleIsDrinkable) {
		if(isAccessibleIsDrinkable){
			this.setM_icon( BitmapDescriptorFactory.fromResource(R.drawable.ic_position_accessible));
		}else{
			this.setM_icon( BitmapDescriptorFactory.fromResource(R.drawable.ic_position_notaccessible));
		}
	}	
	
	/* (non-Javadoc)
	 * @see at.fhhgb.mc.wasserapp.mapactivity.MarkerObject#setM_checkboxStringBool(java.lang.String)
	 */
	@Override
	public void setM_checkboxStringBool(String _checkboxProof) {
		if (_checkboxProof.equals("true")) {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_accessible));
		} else {
			this.setM_icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_position_notaccessible));
		}
	}
}
