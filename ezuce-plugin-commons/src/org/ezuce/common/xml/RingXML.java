package org.ezuce.common.xml;

public class RingXML {
	private int expiration;
	private String type;
	private boolean enabled;
	private String number;

	public RingXML() {

	}
	public RingXML(int expiration, String type, boolean enabled, String number) {
		this.expiration = expiration;
		this.type = type;
		this.enabled = enabled;
		this.number = number;
	}
	public int getExpiration() {
		return expiration;
	}
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

}
