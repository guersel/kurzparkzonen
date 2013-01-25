package org.saabo.parkingzone.activity;

import org.saabo.android.support.message.MessageHelper;
import org.saabo.parkingzone.R;
import org.saabo.parkingzone.controller.StreetMapController;
import org.saabo.parkingzone.map.WMSOverlay;
import org.saabo.parkingzone.util.GeoConstants;
import org.saabo.parkingzone.view.GoogleMapInfoWindowAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
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
	private StreetMapController controller;
	private ProgressDialog progressDialog;
	private MapFragment mapFragment;
	private GoogleMap googleMap;
	private PopupWindow popup;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.mapv2);
        
        controller = new StreetMapController(this);
        
        if (!initGoogleMap()) {
        	finish();
        	return;
        }      
        
        setNextChangeListener();
        
        // Go intially to vienna on the map
        LatLng location = new LatLng(48.217353, 16.373062);
		updateMapPosition(location, 12);
    }

    /**
     * Initialize google maps
     * @return true if initialization was successful otherwise false
     */
    private boolean initGoogleMap() {
    	// Get google maps from the fragment
    	mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map); 
        googleMap = mapFragment.getMap();
        
        if (googleMap == null) {
        	MessageHelper.showLongMessage(this, R.string.error_no_google_maps);
        	return false;
        }
        // Show the road map on google map view
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        googleMap.setMyLocationEnabled(true);    
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(GeoConstants.LOCATION_WIEN));
        //googleMap.setInfoWindowAdapter(new GoogleMapInfoWindowAdapter(this));        
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				Log.d(TAG, "onMarkerClick()");
				LayoutInflater inflater = (LayoutInflater) StreetMapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(org.saabo.parkingzone.R.layout.infomarker, null);
				
				TextView textView = (TextView) view.findViewById(org.saabo.parkingzone.R.id.title);
				textView.setText(marker.getTitle());
				
				WebView webView = (WebView) view.findViewById(org.saabo.parkingzone.R.id.webview);
				webView.setWebChromeClient(new WebChromeClient());
				webView.loadData(marker.getSnippet(), "text/html; charset=UTF-8", null);
				
				popup = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				popup.showAtLocation(view, Gravity.CENTER, 0, 0);
				
				Button button = (Button) view.findViewById(R.id.cancel);
				button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						popup.dismiss();
					}
				});			
				
				Log.d(TAG, "onMarkerClick() finished()");
				return true;
			}
		});
        
        return true;
    }
    
    /**
     *  This method will display the search dialog.
     */
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
    	controller.onResume();
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
    
    /** **/
    private void setNextChangeListener() {
    	googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				Log.d(TAG, "Google maps camera position changed");
				controller.fetchParkzoneDataWien();
				googleMap.setOnCameraChangeListener(null);
			}
		});
    }
    
    /**
     * Changes the camera position on the map.
     * @param location The <code>LatLng</code> object holds latitude and longitude information
     * @param zoomLevel The zoom level of the map view
     */
    public void updateMapPosition(final LatLng location, final int zoomLevel) {
    	CameraPosition cp = CameraPosition.fromLatLngZoom(location, zoomLevel);
    	googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));    	
	}
    
    public void drawPolygon(final PolygonOptions polygonOptions) {
		googleMap.addPolygon(polygonOptions);
	}
    
    public void drawMarker(final MarkerOptions markerOptions) {
		googleMap.addMarker(markerOptions);
	}
    
    /**
     * Will be called by the controller.
     */
    public void showProgressDialog() {
		dismissProgressDialog();
		progressDialog = ProgressDialog.show(this, getString(R.string.search), getString(R.string.search_parkingzones));
	}
    
    /**
     * Will be called by the controller on success.
     */
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
    	controller.onPause();
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
    	Log.d(TAG, "onRestoreInstanceState()");
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    		if (popup != null && popup.isShowing()) {
    			popup.dismiss();
    			return true;
    		}
    	}
    	return super.onKeyDown(keyCode, event);
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

	public GoogleMap getGoogleMap() {
		return googleMap;
	}


}
