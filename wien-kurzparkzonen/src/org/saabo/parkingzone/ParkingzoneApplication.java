package org.saabo.parkingzone;

import org.saabo.android.support.setting.SharedPreferencesUtil;

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
		// URL of WMS (Web Map Service) service
		SharedPreferencesUtil.putString(context, Constants.SharedPreferences.SHARED_PREFERENCES_URL, 
				Constants.SharedPreferences.URL_WMS, 
				"http://data.wien.gv.at/daten/wms?request=GetMap&version=1.1.1&width=%d&height=%d&layers=KURZPARKZONEOGD&styles=&format=image/gif&bbox=%f,%f,%f,%f&srs=EPSG:4326");
	}
	
	public static Context getAppContext() {
		return context;
	}
	
}
