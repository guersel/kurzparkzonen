package org.saabo.parkingzone.activity;

import java.util.ArrayList;
import java.util.List;

import org.saabo.android.support.event.Event;
import org.saabo.android.support.event.EventListener;
import org.saabo.android.support.message.MessageHelper;
import org.saabo.parkingzone.R;
import org.saabo.parkingzone.controller.AddressSearchableController;
import org.saabo.parkingzone.model.AddressModel;
import org.saabo.parkingzone.model.SearchAddressModel;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * This activity provides address search capabilities for the whole application.
 * @author guersel
 */
public class AddressSearchableActivity extends ListActivity implements AddressSearchableView {
	
	private static final String TAG = AddressSearchableActivity.class.getName();
	private List<AddressModel> addresses = new ArrayList<AddressModel>();
	private SearchAddressModel addressModel;
	private AddressSearchableController controller;
	private ArrayAdapter<String> adapter;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.list);
		
		// Initialize the list adapter for showing addresses
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
		setListAdapter(adapter);
		
		// Connect Model, View, Controller
		addressModel = new SearchAddressModel();
		addressModel.addEventListener(SearchAddressModel.AddressEvent.ADDRESSES_CHANGED, addressChangedListener);
		controller = new AddressSearchableController(this, addressModel);
		
		controller.handleIntent(getIntent());
	}
	
	// This listener will be called by the model if any update is available
	private EventListener addressChangedListener = new EventListener() {
		
		@Override
		public void onEvent(Event event) {
			SearchAddressModel model = (SearchAddressModel) event.getSource();
			addresses = model.getAddresses();
			
			// Update the adapter of the activity
			adapter.clear();
			for(AddressModel address : addresses) {
				adapter.add(address.getFormattedAddress());
			}
		}
	};
	
	protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
		controller.onListItemClick(position);

	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);  // Update current intent instance
		
		controller.handleIntent(intent);	
	}
	
	public void onSearchSuccess() {
		dismissProgressDialog();
	}
	
	public void onError(int resId) {
		MessageHelper.showShortMessage(this, resId);
		finish();
	}
	
	public void showProgressDialog() {
		dismissProgressDialog();
		progressDialog = new ProgressDialog(this).show(this, "Search", "search for address");
	}
	
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy()");
	
		dismissProgressDialog();
	
		super.onDestroy();
		
	}
	
	private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	// Getter and setter
	public List<AddressModel> getAddresses() {
		return addresses;
	}
	
}
