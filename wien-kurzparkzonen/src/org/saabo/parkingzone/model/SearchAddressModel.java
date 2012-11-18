package org.saabo.parkingzone.model;

import java.util.ArrayList;
import java.util.List;

import org.saabo.android.support.dispatcher.EventDispatcher;
import org.saabo.android.support.event.SimpleEvent;

public final class SearchAddressModel extends EventDispatcher {
	
	private List<AddressModel> addresses;

	public static class AddressEvent extends SimpleEvent {

		public static final String ADDRESSES_CHANGED = "addressesChanged";
		
		public AddressEvent(String type) {
			super(type);
		}	
	}
	
	public SearchAddressModel() {
		addresses = new ArrayList<AddressModel>();
	}
	
	public void setAddresses(List<AddressModel> addresses) {
		this.addresses = addresses;
		dispatchEvent(new AddressEvent(AddressEvent.ADDRESSES_CHANGED));
	}
	
	
	// Getter
	public List<AddressModel> getAddresses() {
		return addresses;
	}
	
}
