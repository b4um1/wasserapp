/*
 * 
 */
package at.fhhgb.mc.wasserapp.mapactivity;

import java.io.Serializable;
import java.util.ArrayList;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import at.fhhgb.mc.wasserapp.R;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

// TODO: Auto-generated Javadoc
/**
 * The Class MarkerObject.
 */
public class MyMarkerObject  implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m_lat lng. */
	private LatLng m_latLng;
	
	private String m_address;
	
	/** The m_comment. */
	private String m_comment;
	
	/** The m_type. */
	private String m_type;
	
	/** The m_image link. */
	private String m_imageLink;
	
	/** The m_checkbox proof. */
	private boolean m_checkboxProof;
	
	/** The m_id. */
	private String m_id;
	
	/** The m_icon. */
	private BitmapDescriptor m_icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_position);
	
	/**
	 * Instantiates a new marker object.
	 *
	 * @param _latLng the _lat lng
	 * @param _address the _address
	 * @param _type the _type
	 * @param _bool the _bool
	 * @param _comment the _comment
	 * @param _imgLink the _img link
	 */
	
	public MyMarkerObject( LatLng _latLng, String _address, String _type, boolean _bool, String _comment, String _imgLink)
	{
		m_type = _type;
		m_latLng = _latLng;
		m_address = _address;
		m_type = _type;
		m_imageLink = _imgLink;
		m_comment = _comment;
		m_checkboxProof = true;
	}

	public String getM_address() {
		return m_address;
	}

	public void setM_address(String m_address) {
		this.m_address = m_address;
	}

	/**
	 * Instantiates a new marker object.
	 */
	public MyMarkerObject() {
		m_checkboxProof = true;
	}
	
	/**
	 * Instantiates a new marker object.
	 *
	 * @param _latLng the _lat lng
	 */
	public MyMarkerObject(LatLng _latLng) {
		m_latLng=_latLng;
	}

	
	/**
	 * Gets the m_lat lng.
	 *
	 * @return the m_lat lng
	 */
	public LatLng getM_latLng() {
		return m_latLng;
	}

	/**
	 * Sets the m_lat lng.
	 *
	 * @param m_latLng the new m_lat lng
	 */
	public void setM_latLng(LatLng m_latLng) {
		this.m_latLng = m_latLng;
	}

	/**
	 * Gets the m_comment.
	 *
	 * @return the m_comment
	 */
	public String getM_comment() {
		return m_comment;
	}

	/**
	 * Sets the m_comment.
	 *
	 * @param m_comment the new m_comment
	 */
	public void setM_comment(String m_comment) {
		this.m_comment = m_comment;
	}

	/**
	 * Gets the m_type.
	 *
	 * @return the m_type
	 */
	public String getM_type() {
		return m_type;
	}

	/**
	 * Sets the m_type.
	 *
	 * @param _type the new m_type
	 */
	public void setM_type(String _type) {
		this.m_type = _type;
	}

	/**
	 * Gets the m_icon.
	 *
	 * @return the m_icon
	 */
	public BitmapDescriptor getM_icon() {
		return m_icon;
	}

	/**
	 * Sets the m_icon.
	 *
	 * @param isAccessibleIsDrinkable the new m_icon
	 */
	public void setM_icon(boolean isAccessibleIsDrinkable) {
		if(isAccessibleIsDrinkable){
			m_icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_position);
		}else{
			m_icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_position);
		}
	}
	
	/**
	 * Sets the m_icon.
	 *
	 * @param _icon the new m_icon
	 */
	public void setM_icon(BitmapDescriptor _icon){
		m_icon = _icon;
	}

	/**
	 * Gets the m_image link.
	 *
	 * @return the m_image link
	 */
	public String getM_imageLink() {
		return m_imageLink;
	}

	/**
	 * Sets the m_image link.
	 *
	 * @param m_imageLink the new m_image link
	 */
	public void setM_imageLink(String m_imageLink) {
		this.m_imageLink = m_imageLink;
	}	
	
	/**
	 * Gets the m_checkbox string bool.
	 *
	 * @return the m_checkbox string bool
	 */
	public String getM_checkboxStringBool() {
		if(m_checkboxProof){
			return "true";
		}else{
			return "false";
		}
	}
	//parameter( _checkbox) is string because the value in the mysql table is also a string value
	/**
	 * Sets the m_checkbox string bool.
	 *
	 * @param _checkbox the new m_checkbox string bool
	 */
	public void setM_checkboxStringBool(String _checkbox) {
		if(_checkbox.equals("true")){
			m_checkboxProof = true;
		}else{
			m_checkboxProof = false;
		}
	}
	
	/**
	 * Sets the m_checkbox.
	 *
	 * @param _check the new m_checkbox
	 */
	public void setM_checkbox(boolean _check){
		m_checkboxProof = _check;
	}
	
	/**
	 * Gets the m_checkbox.
	 *
	 * @return the m_checkbox
	 */
	public boolean getM_checkbox(){
		return m_checkboxProof;
	}
	
	
	/**
	 * Gets the m_id.
	 *
	 * @return the m_id
	 */
	public String getM_id(){
		return m_id;
	}

	/**
	 * Sets the m_id.
	 *
	 * @param _id the new m_id
	 */
	public void setM_id(String _id) {

		m_id = _id;
	}
	
}
