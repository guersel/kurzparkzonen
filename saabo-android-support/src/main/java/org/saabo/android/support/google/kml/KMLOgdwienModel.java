package org.saabo.android.support.google.kml;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.google.android.maps.GeoPoint;

/**
 * This model holds the KML response data from the ogdwien kml service.
 * @author guersel
 *
 */
public class KMLOgdwienModel {
		
	public List<Placemark> placemarks = new ArrayList<Placemark>();
	
	public static class Placemark {
		public String name;
		public String description;
		public LookAt lookAt = new LookAt();
		public Style style = new Style();
		public MultiGeometry multiGeometry = new MultiGeometry();
	}
	
	public static class LookAt {
		public double longitude;
		public double latitude;
		public double heading;
		public double tilt;
		public double range;
	}
	
	public static class Style {
		public IconStyle iconStyle = new IconStyle();
		public LabelStyle labelStyle = new LabelStyle();
		public PolyStyle polyStyle = new PolyStyle();
		public LineStyle lineStyle = new LineStyle();
		
		public static class IconStyle {
			public int color;
			public float scale;
			public Icon icon = new Icon();
			
			public static class Icon {
				public String href;
			}
		}
		
		public static class LabelStyle {
			public int color;
		}
		
		public static class PolyStyle {
			public int color;
			public int outline;
		}
		
		public static class LineStyle {
			public int color;
			public int width;
		}
	}
	
	public static class MultiGeometry {
		public Point point = new Point();
		public List<Polygon> polygon = new ArrayList<Polygon>();
		
		public static class Point {
			public GeoPoint coordinates;
		}
		
		public static class Polygon {
			public OuterBoundaryIs outerBoundaryIs = new OuterBoundaryIs();
			public List<InnerBoundaryIs> innerBoundaryIs = new ArrayList<InnerBoundaryIs>();
		}
		
		public static class OuterBoundaryIs {
			public LinearRing linearRing = new LinearRing();
		}
		
		public static class InnerBoundaryIs {
			public LinearRing linearRing = new LinearRing();
		}
		
		public static class LinearRing {
			public GeoPoint[] coordinates;
		}
	}
}
