/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity;

import at.fhhgb.mc.wasserapp.R;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Toilet.
 */
public class Toilet extends MyMarkerObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wc.
	 */
	public Toilet (){
		super();
		setM_icon(true);
		setM_type("toilet");
	}
	/**
	 * Sets the icon of the toilet marker
	 */
	public void setM_icon(boolean isbarrierfree){
		if(isbarrierfree){
			this.setM_icon( BitmapDescriptorFactory.fromResource(R.drawable.ic_position_accessible));
		}else{
			this.setM_icon( BitmapDescriptorFactory.fromResource(R.drawable.ic_position_notaccessible));
		}
	}	
}
