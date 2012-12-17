package org.saabo.parkingzone.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class WMSOverlay extends Overlay {
	
	private static final String TAG = WMSOverlay.class.getName();  
	private Bitmap bitmap;
	
	public WMSOverlay(final Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	@Override
	public void draw(final Canvas canvas, final MapView mapView, final boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		paint.setAlpha(0x888);		
		
		canvas.drawBitmap(bitmap, 0, 0, paint);
		
	}
}     