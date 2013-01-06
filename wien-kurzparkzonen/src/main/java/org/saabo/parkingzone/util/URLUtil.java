package org.saabo.parkingzone.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.saabo.android.support.setting.SharedPreferencesUtil;
import org.saabo.parkingzone.Constants;
import org.saabo.parkingzone.ParkingzoneApplication;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.maps.GeoPoint;

/**
 * URL utility class.
 * @author guersel
 *
 */
public final class URLUtil {
	
	private URLUtil() {}
	
	private static final String TAG = URLUtil.class.getName();
	
	/**
	 * Generate a Google geocode webservice url for searching addresses.
	 * @param address The name of the street in vienna
	 * @return The Google geocode webservice url
	 */
	public static String getAddressUrl(final String address) {
		if (address == null || "".equals(address.trim())) {
			throw new IllegalArgumentException("Address must not be null or empty.");
		}
		
		String geocodeUrl = SharedPreferencesUtil.getString(ParkingzoneApplication.getAppContext(), 
				Constants.SharedPreferences.SHARED_PREFERENCES_URL, 
				Constants.SharedPreferences.URL_GOOGLE_GEOCODE_ADDRESS);
		try {
			return geocodeUrl + URLEncoder.encode(address, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.d(TAG, e.getMessage());
		}
		return "";
	}
	
	public static String getWMSUrl(final int width, final int height, final GeoPoint leftTop, final GeoPoint rightBottom) {
		
		String wmsUrlUnformatted = SharedPreferencesUtil.getString(ParkingzoneApplication.getAppContext(), Constants.SharedPreferences.SHARED_PREFERENCES_URL, Constants.SharedPreferences.URL_WMS);
		
		final float leftTopLongitude = (float) (leftTop.getLongitudeE6()/1E6);
		final float leftTopLatitude = (float) (leftTop.getLatitudeE6()/1E6);
		final float rightBottomLongitude = (float) (rightBottom.getLongitudeE6()/1E6);
		final float rightBottomLatitude = (float) (rightBottom.getLatitudeE6()/1E6);
		
		String wmsUrl = String.format(Locale.US, wmsUrlUnformatted, width, height, leftTopLongitude, rightBottomLatitude, rightBottomLongitude, leftTopLatitude);
		return wmsUrl;
	}
	
	public static String getKMLUrl(final VisibleRegion visibleRegion) {
		String kmlUrlUnformatted = SharedPreferencesUtil.getString(ParkingzoneApplication.getAppContext(), Constants.SharedPreferences.SHARED_PREFERENCES_URL, Constants.SharedPreferences.URL_KML);
		
		LatLngBounds latLngBounds = visibleRegion.latLngBounds;
		LatLng northeast = latLngBounds.northeast;
		LatLng southwest = latLngBounds.southwest;
		
		String kmlUrl = String.format(Locale.US, kmlUrlUnformatted, southwest.latitude, southwest.longitude, northeast.latitude, northeast.longitude);
		return kmlUrl;
	}
	
	public static String getKMLUrlWien() {
		String kmlUrlWien = SharedPreferencesUtil.getString(ParkingzoneApplication.getAppContext(), Constants.SharedPreferences.SHARED_PREFERENCES_URL, Constants.SharedPreferences.URL_KML_WIEN);
		return kmlUrlWien;
	}
}
