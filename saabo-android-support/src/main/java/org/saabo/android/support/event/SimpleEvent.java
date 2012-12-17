package org.saabo.android.support.event;

/**
 * Simple implementation of the <code>Event</code> interface.
 * @author guersel
 *
 */
public class SimpleEvent implements Event {

	private String type;
	private Object source;
	
	public SimpleEvent(final String type) {
		this.type = type;
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public Object getSource() {
		return source;
	}

	@Override
	public void setSource(final Object source) {
		this.source = source;
	}

}
