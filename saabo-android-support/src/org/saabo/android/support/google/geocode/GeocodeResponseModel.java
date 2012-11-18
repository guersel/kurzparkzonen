package org.saabo.android.support.google.geocode;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * This class is the bean representation of the geocode json response from the google geocode service.
 * Using the gson library one can easily convert the json structure into the java model representation.
 * @author guersel
 *
 */
public class GeocodeResponseModel {
	
	public List<Results> results;
	public String status;
	
	public static class Results {
		
		@SerializedName("address_components")
		public List<AddressComponents> addressComponents;
		
		@SerializedName("formatted_address")
		public String formattedAddress;
		
		public Geometry geometry;
		public List<String> types;
		
		
		public static class AddressComponents {
			
			public static final String TYPE_ROUTE = "route";
			public static final String TYPE_STREET_NUMBER = "street_number";
			public static final String TYPE_LOCALITY = "locality";
			public static final String TYPE_POSTAL_CODE = "postal_code";
			
			@SerializedName("long_name") 
			public String longName;
			
			@SerializedName("short_name")
			public String shortName;
			
			public List<String> types;
						
		}
		
		public static class Geometry {
			
			public Location location;
			
			@SerializedName("location_type")
			public String locationType;
			
			public ViewPort viewport;			
			
			public static class Location {
				public Double lat;
				public Double lng;
			}
			
			public static class ViewPort {
				public Northeast northeast;
				public Southwest southwest;
				
				public static class Northeast {
					public Double lat;
					public Double lng;
				}
				
				public static class Southwest {
					public Double lat;
					public Double lng;
				}
			}
			
		}
	}
}
