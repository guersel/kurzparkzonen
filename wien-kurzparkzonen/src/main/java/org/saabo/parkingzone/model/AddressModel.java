package org.saabo.parkingzone.model;

import java.io.Serializable;

public class AddressModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String streetname = "";
	private String streetnumber = "";
	private String locality = "";
	private String postalCode = "";
	private Double lng = 0.0;
	private Double lat = 0.0;
	private String formattedAddress = "";
	
	
	public String getStreetname() {
		return streetname;
	}
	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}
	public String getStreetnumber() {
		return streetnumber;
	}
	public void setStreetnumber(String streetnumber) {
		this.streetnumber = streetnumber;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public String getFormattedAddress() {
		return formattedAddress;
	}
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
	
	@Override
	public String toString() {
		return "Latitude " + lat + " Longitude " + lng;
	}
}
