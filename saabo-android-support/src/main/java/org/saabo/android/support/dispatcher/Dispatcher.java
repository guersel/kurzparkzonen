package org.saabo.android.support.dispatcher;

import org.saabo.android.support.event.Event;
import org.saabo.android.support.event.EventListener;

/**
 * General dispatcher methods.
 * Dispatchers are objects which inform other object on any state change.
 * @author guersel
 *
 */
public interface Dispatcher {
	
	/**
	 * Register an object as an event <code>listener</code>.
	 * @param type The event type on which the listener listens
	 * @param listener The event listener object
	 */
	public void addEventListener(String type, EventListener listener);
	
	/**
	 * Remove the <code>listener</code> for the certain <code>type</code> of event.
	 * @param type The event type on which the listener listens
	 * @param listener The event listener object
	 */
	public void removeEventListener(String type, EventListener listener);
	
	/**
	 * Dispatch an <code>event</code> to all listeners which listens on the occured event.
	 * @param event The occured event
	 */
	public void dispatchEvent(Event event);
	
	/**
	 * Check if an <code>listener</code> is registered for the certain <code>type</code> of event.
	 * @param type The event type on which the listener listens
	 * @param listener The event listener object
	 */
	public boolean hasEventListener(String type, EventListener listener);
	
}
