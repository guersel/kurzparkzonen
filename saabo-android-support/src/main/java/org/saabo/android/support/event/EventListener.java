package org.saabo.android.support.event;

/**
 * All listeners should implement this interface to enable listening on state changes.
 * @author guersel
 *
 */
public interface EventListener {
	
	/**
	 * Will be called by an event dispatcher if a state change occurs.
	 * @param event The event which occured
	 */
	public void onEvent(Event event);
}
