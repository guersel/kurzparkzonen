package org.saabo.parkingzone.activity;

import org.saabo.parkingzone.R;
import org.saabo.parkingzone.controller.StreetMapController;
import org.saabo.parkingzone.map.WMSOverlay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * This activity shows the initial map view (Google maps) to allow for searching addresses or to show
 * the current location of the user.
 * @author guersel
 *
 */
public class StreetMapActivity extends MapActivity {

	private final static String TAG = StreetMapActivity.class.getName();
	public static final String INTENT_PARAM_ADDRESS = "address";
	private MapView mapView;
	private MapController mapController;
	private MyLocationOverlay myLocationOverlay;
	private boolean locationProviderAvailable;
	private StreetMapController controller;
	private ProgressDialog progressDialog;
	private WMSOverlay wmsOverlay;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.map);
        
        controller = new StreetMapController(this);
        
        initMapView(); 
        
        initMyLocationOverlay();        
    }

    // Initial Google map view
	private void initMapView() {
    	mapView = (MapView) findViewById(R.id.mapview);
    	mapController = mapView.getController();
    	
        mapView.setBuiltInZoomControls(true);
	}
    
	// Allow to update the current location on the map view
    private void initMyLocationOverlay() {
    	myLocationOverlay = new MyLocationOverlay(this, mapView) {
    		@Override
    		public synchronized void onLocationChanged(Location location) {
    			super.onLocationChanged(location);
    			
    			GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
    			mapController.animateTo(geoPoint);
    		}
    	};
    	mapView.getOverlays().add(myLocationOverlay);
    }
    
    // This method will display the search dialog
    @Override
    public boolean onSearchRequested() {
    	return super.onSearchRequested();
    }

	@Override
    protected void onStart() {
    	super.onStart();
    	Log.d(TAG, "onStart()");
    	
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d(TAG, "onResume()");
    	
    	//this.locationProviderAvailable = myLocationOverlay.enableMyLocation();
    	Log.d(TAG, this.locationProviderAvailable ? "Location provider found" : "Location provider not found");
    	myLocationOverlay.enableCompass(); // shows the compass on the map view
    }
    
    /**
     * Will be called by the AddressSearchableController to navigate to a new address.
     */
    @Override
    public void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	Log.d(TAG, "onNewIntent()");
    	
    	// Start progress bar
    	controller.handleAddressUpdateIntent(intent);
    }
    
    public void updateMapPosition(GeoPoint geoPoint, int zoomLevel) {
		mapController.animateTo(geoPoint);
    	mapController.setZoom(zoomLevel);
	}
    
    public void updateWMSOverlay(final Bitmap bitmap) {
    	if (mapView.getOverlays().contains(wmsOverlay)) {
    		mapView.getOverlays().remove(wmsOverlay);
    	}    	
    	wmsOverlay = new WMSOverlay(bitmap);
    	mapView.getOverlays().add(wmsOverlay);
    	mapView.postInvalidate();
    	
    }
    
    public void showProgressDialog() {
		dismissProgressDialog();
		progressDialog = ProgressDialog.show(this, "Search", "search for parkzone areas");
	}
    
    public void onUpdateSuccess() {
    	dismissProgressDialog();
    }
    
    private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
    
    // Persistent data should saved here
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.d(TAG, "onPause()");
    	
    	myLocationOverlay.disableMyLocation();
    	myLocationOverlay.disableCompass();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d(TAG, "onStop()");
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.d(TAG, "onRestart()");
    }
    
    @Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
    // Will be called on configuration changes (eg. scree orientation) or the system kills the activity
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d(TAG, "onDestroy()");
    }
    
    // Will be called before onStop() and possibly before onPause()
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// The superclass call will save view states
    	super.onSaveInstanceState(outState);
    }
    
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	// The superclass call will restore view states
    	super.onRestoreInstanceState(savedInstanceState);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.street_map, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_search:
    		Log.d(TAG, "Search options menu item clicked");
    		return onSearchRequested();
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    public boolean isLocationProviderAvailable() {
		return locationProviderAvailable;
	}

	public MapView getMapView() {
		return mapView;
	}
}
