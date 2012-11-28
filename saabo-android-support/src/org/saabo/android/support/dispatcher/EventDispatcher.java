package org.saabo.android.support.dispatcher;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.saabo.android.support.event.Event;
import org.saabo.android.support.event.EventListener;

import android.util.Log;

/**
 * Model classes should extend this class to be observable by other objects.
 * @author guersel
 *
 */
public class EventDispatcher implements Dispatcher {

	private static final String TAG = EventDispatcher.class.getName();
	private HashMap<String, CopyOnWriteArrayList<EventListener>> listenerMap;
	private Dispatcher target;
	
	public EventDispatcher() {
		this(null);
	}
	
	public EventDispatcher(final Dispatcher target) {
		listenerMap = new HashMap<String, CopyOnWriteArrayList<EventListener>>();
		this.target = (target != null) ? target : this;
	}
	
	/**
	 * Register an object as an event <code>listener</code>.
	 * @param type The event type on which the listener listens
	 * @param listener The event listener object
	 */
	@Override
	public final void addEventListener(final String type, final EventListener listener) {
		if (type == null || listener == null) {
			Log.e(TAG, "Arguments type and listener must not null.");
			return;
		}
		
		synchronized(listenerMap) {
			CopyOnWriteArrayList<EventListener> listeners = listenerMap.get(type);
			
			if (listeners == null) {
				listeners = new CopyOnWriteArrayList<EventListener>();
				listenerMap.put(type, listeners);
			}
			
			listeners.add(listener);
		}
	}

	/**
	 * Remove the <code>listener</code> for the certain <code>type</code> of event.
	 * @param type The event type on which the listener listens
	 * @param listener The event listener object
	 */
	@Override
	public final void removeEventListener(final String type, final EventListener listener) {
		if (type == null || listener == null) {
			Log.e(TAG, "Arguments type and listener must not null.");
			return;
		}
		
		synchronized(listenerMap) {
			CopyOnWriteArrayList<EventListener> listeners = listenerMap.get(type);
			
			if (listeners == null) {
				return;
			}
			
			listeners.remove(listener);	
			if (listeners.size() == 0) {
				listenerMap.remove(type);
			}
		}
		
	}
	
	/**
	 * Dispatch an <code>event</code> to all listeners which listens on the occured event.
	 * @param event The occured event
	 */
	@Override
	public final void dispatchEvent(final Event event) {
		if (event == null) {
			throw new IllegalArgumentException("Argument event must not null.");
		}
		
		event.setSource(target);
		String type = event.getType();
		
		CopyOnWriteArrayList<EventListener> listeners;
		synchronized(listenerMap) {
			listeners = listenerMap.get(type);
		}
		
		if (listeners == null) {
			Log.d(TAG, "No listeners found.");
			return;
		}
		
		for(EventListener listener : listeners) {
			listener.onEvent(event);
		}
	}

	/**
	 * Check if an <code>listener</code> is registered for the certain <code>type</code> of event.
	 * @param type The event type on which the listener listens
	 * @param listener The event listener object
	 */
	@Override
	public final boolean hasEventListener(final String type, final EventListener listener) {
		if (type == null || listener == null) {
			Log.e(TAG, "Arguments type and listener must not null.");
			return false;
		}
		
		synchronized(listenerMap) {
			CopyOnWriteArrayList<EventListener> listeners = listenerMap.get(type);
			
			if (listeners == null) {
				return false;
			}
			
			return listeners.contains(listener);
		}
	}

	

}
