package org.saabo.parkingzone.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.saabo.android.support.async.AsyncTaskResult;
import org.saabo.android.support.google.kml.KMLOgdwienHandler;
import org.saabo.android.support.google.kml.KMLOgdwienModel;
import org.saabo.android.support.http.Http;
import org.saabo.parkingzone.activity.StreetMapActivity;
import org.saabo.parkingzone.model.AddressModel;
import org.saabo.parkingzone.util.URLUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
        	
        	GeoPoint geoPoint = new GeoPoint((int) (address.getLat() * 1E6), (int) (address.getLng() * 1E6));
        	
        	view.showProgressDialog();
        	view.updateMapPosition(geoPoint, 18);
        	int width = view.getMapView().getWidth();
        	int height = view.getMapView().getHeight();
			GeoPoint pointLeftTop = view.getMapView().getProjection().fromPixels(0, 0);
			GeoPoint pointRightBottom = view.getMapView().getProjection().fromPixels(width - 1,  height - 1);
			
			KMLTask task = new KMLTask();
			task.execute(new Object[] {pointLeftTop, pointRightBottom});
    	}
	}
	
	private class KMLTask extends AsyncTask<Object, Void, AsyncTaskResult<KMLOgdwienModel>> { 
		@Override
		protected AsyncTaskResult<KMLOgdwienModel> doInBackground(Object... params) {
			
			GeoPoint pointLeftTop = (GeoPoint) params[0];
			GeoPoint pointRightBottom = (GeoPoint) params[1];
			
			String url = URLUtil.getKMLUrl(pointLeftTop, pointRightBottom);
			
			Log.d(TAG, url);
			
			try {
				Http http = new Http(url);
				byte[] buffer = http.doGet();
				
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				
				XMLReader xmlReader = parser.getXMLReader();
				KMLOgdwienHandler handler = new KMLOgdwienHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
				xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
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
			if (result.getResult() != null) {
				view.onUpdateSuccess();
			} else {
				
				
			}
		}
	}
	
}
