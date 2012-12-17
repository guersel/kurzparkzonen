package org.saabo.android.support.google.kml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.saabo.android.support.google.kml.KMLOgdwienModel.MultiGeometry.InnerBoundaryIs;
import org.saabo.android.support.google.kml.KMLOgdwienModel.MultiGeometry.OuterBoundaryIs;
import org.saabo.android.support.google.kml.KMLOgdwienModel.MultiGeometry.Polygon;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Color;

import com.google.android.maps.GeoPoint;

public class KMLOgdwienHandler extends DefaultHandler {
	
	private KMLOgdwienModel kmlModel;
	private KMLOgdwienModel.Placemark placemark;
	private Polygon polygon;
	private HashMap<String, Boolean> tags;
	private String value;
	
	
	@Override
	public void startDocument() throws SAXException {
		kmlModel = new KMLOgdwienModel();
		tags = new HashMap<String, Boolean>();
		value = "";
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tags.put(localName, true);
		
		if (localName.equals("Placemark")) {
			placemark = new KMLOgdwienModel.Placemark();
		} else if (localName.equals("Polygon")) {
			polygon = new Polygon();
		}
		
		value = "";
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		value += new String(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {		
		if (!Boolean.TRUE.equals(tags.get("Placemark"))) {
			return;
		}
		
		if (localName.equals("Placemark")) {
			kmlModel.placemarks.add(placemark);
		} else if (localName.equals("Polygon")) {
			placemark.multiGeometry.polygon.add(polygon);
		} else if (localName.equals("name")) {
			placemark.name = value;
		} else if (localName.equals("description")) {
			placemark.description = value;
		} else if (localName.equals("longitude")) {
			placemark.lookAt.longitude = Double.valueOf(value);
		} else if (localName.equals("latitude")) {
			placemark.lookAt.latitude = Double.valueOf(value);
		} else if (localName.equals("heading")) {
			placemark.lookAt.heading = Double.valueOf(value);
		} else if (localName.equals("tilt")) {
			placemark.lookAt.tilt = Double.valueOf(value);
		} else if (localName.equals("range")) {
			placemark.lookAt.range = Double.valueOf(value);
		} else if (localName.equals("color")) {
			value = "#" + value.trim();
			if (Boolean.TRUE.equals(tags.get("IconStyle"))) {
				placemark.style.iconStyle.color = Color.parseColor(value);
			} else if (Boolean.TRUE.equals(tags.get("LabelStyle"))) {
				placemark.style.labelStyle.color = Color.parseColor(value);
			} else if (Boolean.TRUE.equals(tags.get("PolyStyle"))) {
				placemark.style.polyStyle.color = Color.parseColor(value);
			} else if (Boolean.TRUE.equals(tags.get("LineStyle"))) {
				placemark.style.lineStyle.color = Color.parseColor(value);
			}
		} else if (localName.equals("scale")) {
			placemark.style.iconStyle.scale = Float.valueOf(value);
		} else if (localName.equals("href")) {
			placemark.style.iconStyle.icon.href = value;
		} else if (localName.equals("outline")) {
			placemark.style.polyStyle.outline = Integer.valueOf(value);
		} else if (localName.equals("width")) {
			placemark.style.lineStyle.width = Integer.valueOf(value);
		} else if (localName.equals("coordinates")) {
			if (Boolean.TRUE.equals(tags.get("Point"))) {
				String[] values = value.trim().split(",");
				placemark.multiGeometry.point.coordinates = new GeoPoint((int) (Double.valueOf(values[0]) * 1E6), (int) (Double.valueOf(values[1]) * 1E6));
			} else if (Boolean.TRUE.equals(tags.get("LinearRing"))) {
				String[] coordinates = value.trim().split(" ");
				
				List<GeoPoint> points = new ArrayList<GeoPoint>();
				for (String current : coordinates) {
					String[] coordinate = current.trim().split(",");
					GeoPoint point = new GeoPoint((int) (Double.valueOf(coordinate[0]) * 1E6), (int) (Double.valueOf(coordinate[1]) * 1E6));
					points.add(point);
				}
				
				if (Boolean.TRUE.equals(tags.get("outerBoundaryIs"))) {
					OuterBoundaryIs outer = new OuterBoundaryIs();
					outer.linearRing.coordinates = points.toArray(new GeoPoint[]{});
					polygon.outerBoundaryIs = outer;
				} else if (Boolean.TRUE.equals(tags.get("innerBoundaryIs"))) {
					InnerBoundaryIs inner = new InnerBoundaryIs();
					inner.linearRing.coordinates = points.toArray(new GeoPoint[]{});
					polygon.innerBoundaryIs.add(inner);
				}
			}
		} 
		
		tags.put(localName, false);
	}

	public KMLOgdwienModel getKmlModel() {
		return kmlModel;
	}

}
