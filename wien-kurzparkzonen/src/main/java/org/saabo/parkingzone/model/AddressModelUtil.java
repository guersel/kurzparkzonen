package org.saabo.parkingzone.model;

import java.util.ArrayList;
import java.util.List;

import org.saabo.android.support.google.geocode.GeocodeResponseModel;
import org.saabo.android.support.google.geocode.GeocodeResponseModel.Results;
import org.saabo.android.support.google.geocode.GeocodeResponseModel.Results.AddressComponents;

import android.util.Log;

/**
 * Utiliy class for the address model.
 * @author guersel
 *
 */
public final class AddressModelUtil {
	
	private static final String TAG = AddressModel.class.getName();
	
	/**
	 * Generate a list of address models from the google geocode response.
	 * @param geocodeResponse
	 * @return
	 */
	public static List<AddressModel> getAddressModels(final GeocodeResponseModel geocodeResponse) {
		Log.d(TAG, "Get address models from GeocodeResponseModel");
		
		if (geocodeResponse != null && geocodeResponse.results.size() > 0) {
			Log.d(TAG, "Found addresses size " + geocodeResponse.results.size());
			List<AddressModel> tmpAddresses = new ArrayList<AddressModel>();
			
			for(Results result : geocodeResponse.results) {
				AddressModel address = new AddressModel();
				
				for(AddressComponents addressComponents : result.addressComponents) {
					String type = addressComponents.types.get(0);
					
					if (AddressComponents.TYPE_ROUTE.equals(type)) {
						address.setStreetname(addressComponents.longName);
					} else if (AddressComponents.TYPE_STREET_NUMBER.equals(type)) {
						address.setStreetnumber(addressComponents.longName);
					}  else if (AddressComponents.TYPE_LOCALITY.equals(type)) {
						address.setLocality(addressComponents.longName);
					}  else if (AddressComponents.TYPE_POSTAL_CODE.equals(type)) {
						address.setPostalCode(addressComponents.longName);
					}
				}
				address.setFormattedAddress(result.formattedAddress);
				address.setLat(result.geometry.location.lat);
				address.setLng(result.geometry.location.lng);
				
				tmpAddresses.add(address);
			}
			
			return tmpAddresses;
		}
		
		return new ArrayList<AddressModel>();
	}
}
