package org.saabo.parkingzone.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
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
import org.saabo.parkingzone.R;
import org.saabo.parkingzone.activity.StreetMapActivity;
import org.saabo.parkingzone.model.AddressModel;
import org.saabo.parkingzone.util.URLUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.maps.GeoPoint;

/**
 * Controller of StreetMapActivity.
 * @author guersel
 *
 */
public class StreetMapController {
	
	private static final String TAG = StreetMapController.class.getName();
	private StreetMapActivity view;
	
	public StreetMapController(StreetMapActivity view) {
		this.view = view;
	}
	
	/**
	 * Handle intent calls.
	 * @param intent
	 */
	public void handleAddressUpdateIntent(final Intent intent) {
		Bundle bundle = intent.getExtras();
    	if (bundle != null) {
    		AddressModel address = (AddressModel) bundle.getSerializable(StreetMapActivity.INTENT_PARAM_ADDRESS);
        	Log.d(TAG, address.getFormattedAddress() + "; " + address.toString());
        	
        	LatLng location = new LatLng(address.getLat(), address.getLng());
        
        	GoogleMap map = view.getGoogleMap();
        	VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        	Log.d(TAG, visibleRegion.toString());        	
        	
        	
        	view.updateMapPosition(location, 17);
    	}
	}
	
	public void fetchParkzoneData(final VisibleRegion visibleRegion) {
		view.showProgressDialog();
		KMLTask task = new KMLTask();
		task.execute(new Object[] {visibleRegion});
	}
	
	public void fetchParkzoneDataWien() {
		view.showProgressDialog();
				
		KMLTask task = new KMLTask();
		task.execute(new Object[] {});
	}
	
	private class KMLTask extends AsyncTask<Object, Void, AsyncTaskResult<KMLOgdwienModel>> { 
		@Override
		protected AsyncTaskResult<KMLOgdwienModel> doInBackground(Object... params) {
			
			//VisibleRegion visibleRegion = (VisibleRegion) params[0];
			
			//String url = URLUtil.getKMLUrl(visibleRegion);
			String url = URLUtil.getKMLUrlWien();
			
			Log.d(TAG, url);
			
			try {
				Http http = new Http(url);
				byte[] buffer = http.doGet();
				Log.d(TAG, "KML content received");
				
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				
				XMLReader xmlReader = parser.getXMLReader();
				KMLOgdwienHandler handler = new KMLOgdwienHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
				xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
				xmlReader.parse(new InputSource(new ByteArrayInputStream(buffer)));
				KMLOgdwienModel model = handler.getKmlModel();
				
				Log.d(TAG, ""+model.placemarks.size());
				
				return new AsyncTaskResult<KMLOgdwienModel>(model);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return new AsyncTaskResult<KMLOgdwienModel>(e);
			} catch (IOException e) {
				e.printStackTrace();
				return new AsyncTaskResult<KMLOgdwienModel>(e);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return new AsyncTaskResult<KMLOgdwienModel>(e);
			} catch (SAXException e) {
				e.printStackTrace();
				return new AsyncTaskResult<KMLOgdwienModel>(e);
			}
		}
		
		@Override
		protected void onPostExecute(AsyncTaskResult<KMLOgdwienModel> result) {
			super.onPostExecute(result);
			view.onUpdateSuccess();
			
			if (result.getResult() != null) {
				KMLOgdwienModel kmlWien = result.getResult();				
				
				for (Placemark placemark : kmlWien.placemarks) {
					
					for (Polygon polygon : placemark.multiGeometry.polygon) {
						PolygonOptions polygonOptions = new PolygonOptions();
						LatLng[] outerCoordinates = polygon.outerBoundaryIs.linearRing.coordinates;
						polygonOptions.add(outerCoordinates);
																		
						for (InnerBoundaryIs innerBoundary : polygon.innerBoundaryIs) {
							List<LatLng> innerCoordinates = Arrays.asList(innerBoundary.linearRing.coordinates);
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
	}
	
}
