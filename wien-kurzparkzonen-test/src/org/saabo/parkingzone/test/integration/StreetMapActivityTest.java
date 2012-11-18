package org.saabo.parkingzone.test.integration;

import org.saabo.parkingzone.R;
import org.saabo.parkingzone.activity.AddressSearchableActivity;
import org.saabo.parkingzone.activity.StreetMapActivity;

import com.jayway.android.robotium.solo.Solo;

import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

public class StreetMapActivityTest extends ActivityInstrumentationTestCase2<StreetMapActivity> {

	private StreetMapActivity streetMapActivity;
	private Instrumentation instr;
	private Solo solo;
	
	/**
	 * Create the activity instance
	 * @param activityClass
	 */
	public StreetMapActivityTest() {
		super(StreetMapActivity.class);
	}
	
	/**
	 * Will be called before every test
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Should be set to false when any key events are send to the activity
		setActivityInitialTouchMode(false);
		streetMapActivity = getActivity();
		instr = getInstrumentation();
		solo = new Solo(instr, streetMapActivity);
	}

	/**
	 * Will be called after every test
	 */
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void testPreConditions() {
		// If activity is available
		assertNotNull(streetMapActivity);
		
		// If MapView is available
		assertNotNull(streetMapActivity.findViewById(R.id.mapview));
		// If search button is available
		assertNotNull(streetMapActivity.findViewById(R.id.menu_search));
	}
	
	public void testEnterAddress() {
		clickOnSearchAndEnterAddress("Untere Donaustraße 33");
	}
	
	public void testAddressSearchActivityCall() {
		clickOnSearchAndEnterAddress("Untere Donaustraße 33");		
		solo.sendKey(Solo.ENTER);
		
		solo.assertCurrentActivity("Wrong activity found", AddressSearchableActivity.class);
	}
	
	public void testEnterNotAvailableAddressAndSearch() {
		clickOnSearchAndEnterAddress("Not available address");
		solo.sendKey(Solo.ENTER);
		
		AddressSearchableActivity searchActivity = (AddressSearchableActivity) solo.getCurrentActivity();
		assertEquals(0, searchActivity.getAddresses().size());
	}
	
	private void clickOnSearchAndEnterAddress(String address) {
		View view = solo.getView(R.id.menu_search);
		solo.clickOnView(view);
		EditText editText = solo.getEditText(0);
		solo.enterText(editText, address);
	}
	
}


