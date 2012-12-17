package org.saabo.robolectric;

import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.RobolectricTestRunner;

public class SaaboRobolectricTestrunner extends RobolectricTestRunner {

	public SaaboRobolectricTestrunner(Class<?> testClass)
			throws InitializationError {
		super(testClass, new File("../wien-kurzparkzonen"));
	}

}
