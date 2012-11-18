package org.saabo.parkingzone.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.saabo.android.support.async.AsyncTaskResult;
import org.saabo.android.support.http.Http;
import org.saabo.parkingzone.activity.StreetMapActivity;
import org.saabo.parkingzone.model.AddressModel;
import org.saabo.parkingzone.util.URLUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
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
	public void handleAddressUpdateIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
    	if (bundle != null) {
    		AddressModel address = (AddressModel) bundle.getSerializable(StreetMapActivity.INTENT_PARAM_ADDRESS);
        	Log.d(TAG, address.getFormattedAddress() + "; " + address.toString());
        	
        	GeoPoint geoPoint = new GeoPoint((int) (address.getLat() * 1E6), (int) (address.getLng() * 1E6));
        	
        	view.showProgressDialog();
        	view.updateMapPosition(geoPoint, 14);
        	
        	int width = view.getMapView().getWidth();
        	int height = view.getMapView().getHeight();
        	
        	Log.d(TAG, "View width=" + width + " height=" + height);
        	
        	GeoPoint pointLeftTop = view.getMapView().getProjection().fromPixels(0, 0);
			GeoPoint pointRightBottom = view.getMapView().getProjection().fromPixels(width - 1,  height - 1);
        	
			WMSTask task = new WMSTask();
        	task.execute(new Object[] {Integer.valueOf(width), Integer.valueOf(height), pointLeftTop, pointRightBottom});
    	}
	}
	
	private class WMSTask extends AsyncTask<Object, Void, AsyncTaskResult<Bitmap>> {

		@Override
		protected AsyncTaskResult<Bitmap> doInBackground(Object... params) {
			Integer width = (Integer) params[0];
			Integer height = (Integer) params[1];
			GeoPoint pointLeftTop = (GeoPoint) params[2];
			GeoPoint pointRightBottom = (GeoPoint) params[3];
			
			String url = URLUtil.getWMSUrl(width, height, pointLeftTop, pointRightBottom);
			
			Log.d(TAG, url);
			
			try {
				Http http = new Http(url);
				byte[] buffer = http.doGet();
				Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(buffer));
				return new AsyncTaskResult<Bitmap>(bitmap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return new AsyncTaskResult<Bitmap>(e);
			} catch (IOException e) {
				e.printStackTrace();
				return new AsyncTaskResult<Bitmap>(e);
			}
		}
		
		@Override
		protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
			super.onPostExecute(result);
			
			if (result.getException() != null) {
				
			} else {
				view.updateWMSOverlay(result.getResult());
				view.onUpdateSuccess();
			}
		}
	}
}
