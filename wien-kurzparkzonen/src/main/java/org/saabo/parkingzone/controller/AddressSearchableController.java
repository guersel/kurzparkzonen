package org.saabo.parkingzone.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.saabo.android.support.async.AsyncTaskResult;
import org.saabo.android.support.google.geocode.GeocodeResponseModel;
import org.saabo.android.support.http.Http;
import org.saabo.parkingzone.R;
import org.saabo.parkingzone.activity.AddressSearchableActivity;
import org.saabo.parkingzone.activity.StreetMapActivity;
import org.saabo.parkingzone.model.AddressModel;
import org.saabo.parkingzone.model.AddressModelUtil;
import org.saabo.parkingzone.model.SearchAddressModel;
import org.saabo.parkingzone.util.URLUtil;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Controller of the address search view (activity).
 */
public class AddressSearchableController {
	
	private static final String TAG = AddressSearchableController.class.getName();
	private SearchAddressModel addressModel;
	private AddressSearchableActivity view;
	
	public AddressSearchableController(AddressSearchableActivity view, SearchAddressModel addressModel) {
		this.addressModel = addressModel;
		this.view = view;
	}

	/**
	 * Will be called by the <code>AddressSearchableActivity</code>
	 * @param intent The <code>Intent</code> object which waked up the activity
	 */
	public void handleIntent(final Intent intent) {
		Log.d(TAG, "handleIntent()");
		
		String action = intent.getAction();
		if (action == null) {
			Log.d(TAG, "No intent action available, finish activity");
			view.onError(R.string.error_no_intent_action);
			return;
		}
		
		Log.d(TAG, "Intent action is " + action);
		
		if (Intent.ACTION_SEARCH.equals(action)) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			
			Log.d(TAG, "Search query is " + query);
			
			if (query == null || "".equals(query.trim())) {
				Log.d(TAG, "No query available, finish activity");
				view.onError(R.string.error_no_intent_action);
				return;
			}
			
			searchForAddress(query);
		}
	}
	
	/**
	 * Will be called when the user touches an item on the list view.
	 * @param position The item position which was clicked by the user
	 */
	public void onListItemClick(final int position) {
		AddressModel selectedAddress = addressModel.getAddresses().get(position);
		
		Intent intent = new Intent(view, StreetMapActivity.class);
		intent.putExtra(StreetMapActivity.INTENT_PARAM_ADDRESS, selectedAddress);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		view.startActivity(intent);
	}
	
	/**
	 * Prepare asynchronous search task. 
	 * @param address The address which should be searched for
	 */
	private void searchForAddress(final String address) {
		AddressSearchTask searchTask = new AddressSearchTask();
		
		view.showProgressDialog();
		searchTask.execute(address);
	}
	
	/**
	 * The asynchronous task for searching addresses.<br />
	 * This task calls the google geocode service to get address information.
	 * @author guersel
	 *
	 */
	private class AddressSearchTask extends AsyncTask<String, Void, AsyncTaskResult<GeocodeResponseModel>> {

		/**
		 * This method will be called after the method <code>execute</code> was called.<br />
		 * Don't forget that this mehtod runs in an own thread, so user interface calls should be prevented.
		 */
		@Override
		protected AsyncTaskResult<GeocodeResponseModel> doInBackground(String... address) {
			GeocodeResponseModel  geocodeModel = null;
			
			try {
				String geocodeUrl = URLUtil.getAddressUrl(address[0]);
				Log.d(TAG, geocodeUrl);
				
				Http httpClient = new Http(geocodeUrl);
				byte[] response = httpClient.doGet();
				
				Gson gson = new Gson();
				geocodeModel = gson.fromJson(new String(response, "UTF-8"), GeocodeResponseModel.class);
				
			} catch (MalformedURLException e) {
			} catch (IOException e) {
				return new AsyncTaskResult<GeocodeResponseModel>(e);
			}
			
			return new AsyncTaskResult<GeocodeResponseModel>(geocodeModel);
		}

		/**
		 * This method will be called after the asynchronous task is finished.<br />
		 * Calls on the ui-thread can be done here.
		 */
		@Override
		protected void onPostExecute(AsyncTaskResult<GeocodeResponseModel> result) {
			super.onPostExecute(result);
			
			if (result.getException() != null) {
				Log.d(TAG, "Exception during address search: " + result.getException().getMessage());
				view.onError(R.string.error_io_exception);
			} else if (isCancelled()) {
				Log.d(TAG, "AsyncTask was canceled");
				view.onError(R.string.error_search_unknow_reason);
			} else {
				addressModel.setAddresses(AddressModelUtil.getAddressModels(result.getResult()));
				view.onSearchSuccess();
			}
		}
	}
}
