package org.saabo.parkingzone.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class GoogleMapInfoWindowAdapter implements InfoWindowAdapter {

	private static final String TAG = GoogleMapInfoWindowAdapter.class.getName();
	private Context context;
	
	public GoogleMapInfoWindowAdapter(final Context context) {
		this.context = context;
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		Log.i(TAG, "getInfoContents()");
		Log.i(TAG, marker.getSnippet());
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(org.saabo.parkingzone.R.layout.infomarker, null);
		
		TextView textView = (TextView) view.findViewById(org.saabo.parkingzone.R.id.title);
		textView.setText(marker.getTitle());
		
		WebView webView = (WebView) view.findViewById(org.saabo.parkingzone.R.id.webview);
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadData("<html><head></head><body>TEST</body></html>", "text/html; charset=UTF-8", null);
		
		return view;
//		WebView webView = new WebView(context);
//		webView.loadData("<html><body><h1>Hello, WebView</h1></body></html>", "text/html", "UTF-8");
//		webView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		return webView;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		Log.i(TAG, "getInfoWindow()");
		return null;
	}

}
