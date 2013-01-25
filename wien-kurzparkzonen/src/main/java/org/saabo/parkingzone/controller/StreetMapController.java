package org.saabo.parkingzone.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.saabo.android.support.async.AsyncTaskResult;
import org.saabo.android.support.google.kml.KMLOgdwienHandler;
import org.saabo.android.support.google.kml.KMLOgdwienModel;
import org.saabo.android.support.google.kml.KMLOgdwienModel.MultiGeometry.InnerBoundaryIs;
import org.saabo.android.support.google.kml.KMLOgdwienModel.MultiGeometry.Polygon;
import org.saabo.android.support.google.kml.KMLOgdwienModel.Placemark;
import org.saabo.android.support.http.Http;
import org.saabo.android.support.message.MessageHelper;
import org.saabo.parkingzone.Constants;
import org.saabo.parkingzone.ParkingzoneApplication;
import org.saabo.parkingzone.R;
import org.saabo.parkingzone.activity.StreetMapActivity;
import org.saabo.parkingzone.db.ParkingzoneOpenHelper;
import org.saabo.parkingzone.db.ParkingzoneService;
import org.saabo.parkingzone.model.AddressModel;
import org.saabo.parkingzone.util.URLUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.maps.GeoPoint;

/**
 * Controller of StreetMapActivity.
 * 
 * @author guersel
 * 
 */
public class StreetMapController {

	private static final String TAG = StreetMapController.class.getName();
	private StreetMapActivity view;
	private ParkingzoneService service;

	public StreetMapController(StreetMapActivity view) {
		this.view = view;
		service = new ParkingzoneService();
	}

	/**
	 * Handle intent calls.
	 * 
	 * @param intent
	 */
	public void handleAddressUpdateIntent(final Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			AddressModel address = (AddressModel) bundle
					.getSerializable(StreetMapActivity.INTENT_PARAM_ADDRESS);
			Log.d(TAG,
					address.getFormattedAddress() + "; " + address.toString());

			LatLng location = new LatLng(address.getLat(), address.getLng());

			GoogleMap map = view.getGoogleMap();
			VisibleRegion visibleRegion = map.getProjection()
					.getVisibleRegion();
			Log.d(TAG, visibleRegion.toString());

			view.updateMapPosition(location, 17);
		}
	}

	public void onResume() {
		service.open();
	}

	public void onPause() {
		service.close();
	}

	/**
	 * Get the kml data for wien, either from the database or via webservice.
	 */
	public void fetchParkzoneDataWien() {
		view.showProgressDialog();

		byte[] kmlData = service.findWienParkzoneData();

		if (kmlData != null) {
			KMLOgdwienModel kmlWien;
			try {
				kmlWien = parseKml(kmlData);	
				drawPolygons(kmlWien);

				view.onUpdateSuccess();
				
				Date lastUpdate = service.findLastUpdate();
				Date now = new Date();
				// Silently update parkzone data if last update was 7 days before
				if ((now.getTime() - lastUpdate.getTime()) > 604800000) {
					Log.d(TAG, "Last update was before 7 days, get parkzone data again.");
					SilentKMLTask task = new SilentKMLTask();
					task.execute(new Object[]{});
				}			
				
				return;
			} catch (Exception e) {
				Log.e(TAG, "Error during parsing Kml data.");
				// The file could not be readed, try it via webservice on next
				// step
			}

		}

		KMLTask task = new KMLTask();
		task.execute(new Object[] {});
	}

	/**
	 * Draw polygons from kml model <code>kmlWien</code> on Google Map.
	 * 
	 * @param kmlWien
	 *            Contains the kml data
	 */
	private void drawPolygons(final KMLOgdwienModel kmlWien) {
		if (kmlWien != null) {

			for (Placemark placemark : kmlWien.placemarks) {
				// Create marker options to create marker
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.position(new LatLng(placemark.lookAt.latitude,
						placemark.lookAt.longitude));
				markerOptions.title(placemark.name);
				markerOptions.snippet(placemark.description);

				view.drawMarker(markerOptions);

				// Create polygon options to draw polygon
				for (Polygon polygon : placemark.multiGeometry.polygon) {
					PolygonOptions polygonOptions = new PolygonOptions();
					LatLng[] outerCoordinates = polygon.outerBoundaryIs.linearRing.coordinates;
					polygonOptions.add(outerCoordinates);

					for (InnerBoundaryIs innerBoundary : polygon.innerBoundaryIs) {
						List<LatLng> innerCoordinates = Arrays
								.asList(innerBoundary.linearRing.coordinates);
						// Change coordinate direction to counterwise
						Collections.reverse(innerCoordinates);
						polygonOptions.addHole(innerCoordinates);
					}

					polygonOptions.fillColor(placemark.style.polyStyle.color);
					polygonOptions.strokeWidth(1.0f);

					view.drawPolygon(polygonOptions);
				}

			}

		} else {
			MessageHelper.showLongMessage(view, R.string.error_io_exception);
		}
	}

	/**
	 * Parse the kml data to a <code>KMLOgdwienModel</code> object.
	 * 
	 * @param kmlData
	 *            The kml data
	 * @return The mode representation of the kml data
	 * @throws Exception
	 *             if an error occurs
	 */
	private KMLOgdwienModel parseKml(final byte[] kmlData) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		XMLReader xmlReader = parser.getXMLReader();
		KMLOgdwienHandler handler = new KMLOgdwienHandler();
		xmlReader.setContentHandler(handler);
		xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes",
				false);
		xmlReader.parse(new InputSource(new ByteArrayInputStream(kmlData)));
		KMLOgdwienModel model = handler.getKmlModel();
		return model;
	}

	/**
	 * This task obtains the Wien KML data from a webservice and saves it on the
	 * internal storage. Additionally the polygons will be drawn on Google Maps.
	 * 
	 * @author guersel
	 * 
	 */
	private class KMLTask extends
			AsyncTask<Object, Void, AsyncTaskResult<KMLOgdwienModel>> {

		@Override
		protected AsyncTaskResult<KMLOgdwienModel> doInBackground(
				Object... params) {
			String url = URLUtil.getKMLUrlWien();

			Log.d(TAG, url);

			try {
				Http http = new Http(url);
				byte[] buffer = http.doGet();
				Log.d(TAG, "KML content received");

				KMLOgdwienModel model = parseKml(buffer);

				Log.d(TAG, "" + model.placemarks.size());

				int id = service.findId();
				service.saveWienParkzoneData(id, "application/kml+google",
						buffer);

				return new AsyncTaskResult<KMLOgdwienModel>(model);
			} catch (Exception e) {
				e.printStackTrace();
				return new AsyncTaskResult<KMLOgdwienModel>(e);
			}
		}

		@Override
		protected void onPostExecute(AsyncTaskResult<KMLOgdwienModel> result) {
			super.onPostExecute(result);
			view.onUpdateSuccess();

			drawPolygons(result.getResult());
		}

	}

	private class SilentKMLTask extends
			AsyncTask<Object, Void, AsyncTaskResult<KMLOgdwienModel>> {

		@Override
		protected AsyncTaskResult<KMLOgdwienModel> doInBackground(
				Object... params) {
			String url = URLUtil.getKMLUrlWien();
			
			Log.d(TAG, "Get data silently");
			Log.d(TAG, url);

			try {
				Http http = new Http(url);
				byte[] buffer = http.doGet();
				Log.d(TAG, "KML content received");

				int id = service.findId();
				service.saveWienParkzoneData(id, "application/kml+google",
						buffer);
				
				Log.d(TAG, "Data is silently updated");
				
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return new AsyncTaskResult<KMLOgdwienModel>(e);
			}
		}

		@Override
		protected void onPostExecute(AsyncTaskResult<KMLOgdwienModel> result) {
			super.onPostExecute(result);
		}

	}

}
