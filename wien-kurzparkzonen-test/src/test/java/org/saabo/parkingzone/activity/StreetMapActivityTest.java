package org.saabo.parkingzone.activity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saabo.parkingzone.R;
import org.saabo.robolectric.SaaboRobolectricTestrunner;

import com.xtremelabs.robolectric.shadows.ShadowActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

@RunWith(SaaboRobolectricTestrunner.class)
public class StreetMapActivityTest {
	
	private StreetMapActivity activity;
	
	@Before
	public void before() {
		activity = new StreetMapActivity();
	}
	
	@Test
	public void checkIfMapViewIsAvailable() {
		activity.onCreate(null);
		
		View mapView = activity.findViewById(R.id.mapview);
		
		Assert.assertNotNull(mapView);
	}
}
