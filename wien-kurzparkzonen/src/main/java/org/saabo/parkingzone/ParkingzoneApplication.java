package org.saabo.parkingzone;

import org.saabo.android.support.setting.SharedPreferencesUtil;
import org.saabo.parkingzone.db.ParkingzoneService;

import android.app.Application;
import android.content.Context;

/**
 * This class will be exist during the whole lifecycle of the application.<br />
 * Hold general information here.
 * @author guersel
 *
 */
public class ParkingzoneApplication extends Application {
	
	private static Context context = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		
		// Google geocode webservice url for searching addresses in vienna
		SharedPreferencesUtil.putString(context, Constants.SharedPreferences.SHARED_PREFERENCES_URL, 
				Constants.SharedPreferences.URL_GOOGLE_GEOCODE_ADDRESS, 
				"http://maps.google.com/maps/api/geocode/json?language=de&components=country:Austria|administrative_area:Wien&sensor=false&address=");
		SharedPreferencesUtil.putString(context, Constants.SharedPreferences.SHARED_PREFERENCES_URL, Constants.SharedPreferences.URL_KML, 
				"http://data.wien.gv.at/daten/geoserver/ows?version=1.3.0&service=WMS&request=GetMap&crs=EPSG:4326&bbox=%f,%f,%f,%f&width=1&height=1&layers=ogdwien:KURZPARKZONEOGD&styles=&format=application/vnd.google-earth.kml+xml");
		
		SharedPreferencesUtil.putString(context, Constants.SharedPreferences.SHARED_PREFERENCES_URL, Constants.SharedPreferences.URL_KML_WIEN,
				"http://data.wien.gv.at/daten/geoserver/ows?version=1.3.0&service=WMS&request=GetMap&crs=EPSG:4326&bbox=48.10,16.16,48.34,16.59&width=1&height=1&layers=ogdwien:KURZPARKZONEOGD&styles=&format=application/vnd.google-earth.kml+xml");
		
		
	}
	
	public static Context getAppContext() {
		return context;
	}
	
}
