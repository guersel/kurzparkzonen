package org.saabo.android.support.google.kml;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saabo.FileUtil;
import org.saabo.android.support.google.kml.KMLOgdwienModel.Placemark;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

@RunWith(org.saabo.android.robolectric.SaaboRobolectricTestrunner.class)
public class KMLOgdwienHandlerTest {
	
	@Test
	public void handleKMLData() throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		
		byte[] content = FileUtil.readFileContent("fixtures/ogdwien/kurzparkzonen/kml/ogdwien-KURZPARKZONEOGD.kml");
		
		XMLReader xmlReader = parser.getXMLReader();
		KMLOgdwienHandler handler = new KMLOgdwienHandler();
		xmlReader.setContentHandler(handler);
		xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
		xmlReader.parse(new InputSource(new ByteArrayInputStream(content)));
		KMLOgdwienModel model = handler.getKmlModel();
		
		Assert.assertEquals(28, model.placemarks.size());
		
		Placemark placemark = model.placemarks.get(0);
		Assert.assertNotNull(placemark.multiGeometry.polygon.get(0).outerBoundaryIs.linearRing);
		Assert.assertEquals(0, placemark.multiGeometry.polygon.get(0).innerBoundaryIs.size());
		
		placemark = model.placemarks.get(1);	
		Assert.assertEquals(2, placemark.multiGeometry.polygon.size());
		Assert.assertNotNull(placemark.multiGeometry.polygon.get(0).outerBoundaryIs.linearRing);
		Assert.assertEquals(0, placemark.multiGeometry.polygon.get(0).innerBoundaryIs.size());
		Assert.assertNotNull(placemark.multiGeometry.polygon.get(1).outerBoundaryIs.linearRing);
		Assert.assertEquals(3, placemark.multiGeometry.polygon.get(1).innerBoundaryIs.size());
		
		placemark = model.placemarks.get(16);
		Assert.assertNotNull(placemark.multiGeometry.polygon.get(0).outerBoundaryIs.linearRing);
		Assert.assertEquals(5, placemark.multiGeometry.polygon.get(0).innerBoundaryIs.size());
	}
	
}
