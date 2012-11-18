package org.saabo.parkingzone.test.integration;

import java.util.List;

import org.saabo.parkingzone.activity.AddressSearchableActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.SearchManager;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class AddressSearchableActivityTest extends ActivityInstrumentationTestCase2<AddressSearchableActivity> {

	private Activity searchActivity;
	private Instrumentation instr;
	
	public AddressSearchableActivityTest() {
		super(AddressSearchableActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		instr = getInstrumentation();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCallActivityWithEmptyIntentQuery() {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, "");
		
		setActivityIntent(intent);
		getActivity();
		
		assertEquals(0, getActivity().getAddresses().size());
	}

	// Call the onCreate() method of the AddressSearchableActivity and look if Intent is handled correctly 
	public void testCallActivityWithValidIntentQuery() {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, "Leopoldsgasse");
		
		setActivityIntent(intent);
		getActivity();
		
		// Wait for asynchronous job
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {	
		}
		
		assertTrue(getActivity().getAddresses().size() > 0);
	}
	
	// Call the onNewIntent() method of the AddressSearchableActivity and look if Intent is handled correctly
	public void testCallOnNewIntentWithValidQuery() {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, "Leopoldsgasse");
				
		instr.callActivityOnNewIntent(getActivity(), intent);
		
		// Wait for asynchronous job
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		
		assertTrue(getActivity().getAddresses().size() > 0);
	}
	
	// Call the AddressSearchableActivity with a query containing at least one whitespace character
	// e.g. query = "Untere Donaustrasse"
	public void testCallActivityWithSpaceInQuery() {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, "Untere Donaustraße 33");
		
		instr.callActivityOnNewIntent(getActivity(), intent);
		
		// Wait for asynchronous job
		try {
			Thread.sleep(5000);
		} catch(InterruptedException e) {
		}
		assertTrue(getActivity().getAddresses().size() > 0);
		
	}

}
