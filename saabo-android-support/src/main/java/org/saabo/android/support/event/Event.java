package org.saabo.android.support.event;

/**
 * Defines an general event.
 * @author guersel
 *
 */
public interface Event {
	
	/**
	 * The event type.
	 * @return The event type
	 */
	public String getType();
	
	/**
	 * The event source object.
	 * @return The event source object.
	 */
	public Object getSource();
	
	/**
	 * The event source object.
	 * @param source The event source object
	 */
	public void setSource(Object source);
}
