package org.saabo.android.support.message;

import android.content.Context;
import android.widget.Toast;

/**
 * This class contains helper methods to create android messages easier.
 * @author guersel
 *
 */
public final class MessageHelper {
	
	/** Disable constructor. **/
	private MessageHelper() {}
	
	/**
	 * Create a toast message with short visible time.
	 * @param context The android application context
	 * @param resId The resource id of the text
	 */
	public static void showShortMessage(final Context context, final int resId) {
		Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Create a toast message with long visible time.
	 * @param context The android application context
	 * @param resId The resource id of the text
	 */
	public static void showLongMessage(final Context context, final int resId) {
		Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_LONG).show();
	}
}
