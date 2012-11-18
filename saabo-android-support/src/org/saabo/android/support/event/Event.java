package org.saabo.android.support.event;

public interface Event {
	
	public String getType();
	public Object getSource();
	public void setSource(Object source);
}
