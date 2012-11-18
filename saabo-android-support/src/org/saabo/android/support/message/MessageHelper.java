package org.saabo.android.support.message;

import android.content.Context;
import android.widget.Toast;

public class MessageHelper {
	
	public static void showShortMessage(Context context, int resId) {
		Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongMessage(Context context, int resId) {
		Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_LONG).show();
	}
}
