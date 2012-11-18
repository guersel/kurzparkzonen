package org.saabo.android.support.event;

public class SimpleEvent implements Event {

	private String type;
	private Object source;
	
	public SimpleEvent(String type) {
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
	public void setSource(Object source) {
		this.source = source;
	}

}
