package org.saabo.parkingzone.model;

import java.util.ArrayList;
import java.util.List;

import org.saabo.android.support.dispatcher.EventDispatcher;
import org.saabo.android.support.event.SimpleEvent;

/**
 * Model class for address search. This class will inform all listeners if the address list changes.
 * @author guersel
 *
 */
public final class SearchAddressModel extends EventDispatcher {
	
	private List<AddressModel> addresses;

	/**
	 * Marks any address event.
	 * @author guersel
	 */
	public static class AddressEvent extends SimpleEvent {
		
		/** Address list changed. **/
		public static final String ADDRESSES_CHANGED = "addressesChanged";
		
		public AddressEvent(final String type) {
			super(type);
		}	
	}
	
	public SearchAddressModel() {
		addresses = new ArrayList<AddressModel>();
	}
	
	public void setAddresses(final List<AddressModel> addresses) {
		this.addresses = addresses;
		dispatchEvent(new AddressEvent(AddressEvent.ADDRESSES_CHANGED));
	}	
	
	// Getter
	public List<AddressModel> getAddresses() {
		return addresses;
	}
	
}
