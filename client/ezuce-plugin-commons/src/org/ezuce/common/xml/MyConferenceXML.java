package org.ezuce.common.xml;

public class MyConferenceXML {
	private boolean enabled;
	private String name;
	private String description;
	private String extension;
	private String accessCode;
	
	public MyConferenceXML(boolean enabled, String name, String description,
			String extension, String accessCode) {
		this.enabled = enabled;
		this.name = name;
		this.description = description;
		this.extension = extension;
		this.accessCode = accessCode;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getExtension() {
		return extension;
	}
	public String getAccessCode() {
		return accessCode;
	}	
}
