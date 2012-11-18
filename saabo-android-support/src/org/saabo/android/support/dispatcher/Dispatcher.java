package org.saabo.android.support.dispatcher;

import org.saabo.android.support.event.Event;
import org.saabo.android.support.event.EventListener;

public interface Dispatcher {
	
	public void addEventListener(String type, EventListener listener);
	public void removeEventListener(String type, EventListener listener);
	public void dispatchEvent(Event event);
	public boolean hasEventListener(String type, EventListener listener);
	
}
