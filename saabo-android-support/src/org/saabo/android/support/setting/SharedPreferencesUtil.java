package org.saabo.android.support.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Utility class for shared preferences in private mode. Only the owner application can access the preferences.
 * @author guersel
 *
 */
public final class SharedPreferencesUtil {

	/** Disable constructor. **/
	private SharedPreferencesUtil() {}
	
	/**
	 * Save a <code>String</code> in the shared preference <code>preferenceName</code>.
	 * @param context The context of the application
	 * @param preferenceName The name of the shared preference owned by the caller application
	 * @param key The key of the preference to save
	 * @param value The value of the preference to save
	 * @return true if the value was saved otherwise false
	 */
	public static boolean putString(final Context context, final String preferenceName, final String key, final String value) {
		Editor editor = getSharedPreferencesEditor(context, preferenceName);
		editor.putString(key, value);
		return editor.commit();
	}
	
	/**
	 * Get a <code>String</code> from the shared preference <code>preferenceName</code>.
	 * @param context The context of the application
	 * @param preferenceName The name of the shared preference owned by the caller application
	 * @param key The key of the preference to save
	 * @param defaultValue A default value which should be returned if no value is found for the key
	 * @return The value of the specified key
	 */
	public static String getString(final Context context, final String preferenceName, final String key, final String... defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context, preferenceName);
		
		if (defaultValue != null && defaultValue.length > 0) {
			return sharedPreferences.getString(key, defaultValue[0]);
		}
		
		return sharedPreferences.getString(key, "");		
	}
	
	/** Helper method to get the <code>Editor</code> object. **/
	private static Editor getSharedPreferencesEditor(final Context context, final String preferenceName) {
		SharedPreferences sharedPreferences = getSharedPreferences(context, preferenceName);
		return sharedPreferences.edit();
	}
	
	/** Helper method to get the <code>SharedPreferences</code> object. **/
	private static SharedPreferences getSharedPreferences(final Context context, final String preferenceName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
		return sharedPreferences;
	}
}
