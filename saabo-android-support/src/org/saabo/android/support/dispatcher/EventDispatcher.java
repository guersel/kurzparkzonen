package org.saabo.android.support.dispatcher;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.saabo.android.support.event.Event;
import org.saabo.android.support.event.EventListener;

import android.util.Log;

public class EventDispatcher implements Dispatcher {

	private static final String TAG = EventDispatcher.class.getName();
	private HashMap<String, CopyOnWriteArrayList<EventListener>> listenerMap;
	private Dispatcher target;
	
	public EventDispatcher() {
		this(null);
	}
	
	public EventDispatcher(Dispatcher target) {
		listenerMap = new HashMap<String, CopyOnWriteArrayList<EventListener>>();
		this.target = (target != null) ? target : this;
	}
	
	@Override
	public void addEventListener(String type, EventListener listener) {
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

	@Override
	public void removeEventListener(String type, EventListener listener) {
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
	
	@Override
	public void dispatchEvent(Event event) {
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

	@Override
	public boolean hasEventListener(String type, EventListener listener) {
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
