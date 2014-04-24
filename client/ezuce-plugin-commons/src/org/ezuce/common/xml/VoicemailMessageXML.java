package org.ezuce.common.xml;

public class VoicemailMessageXML {
	private String id;
	private boolean heard;
	private boolean urgent;
	private String folder;
	private String duration;
	private long received;
	private String author;
	private String authorExtension;
	private String username;
    private String format;
		
	public VoicemailMessageXML(String id, boolean heard, boolean urgent,
			String folder, String duration, long received, String author, String authorExtension, String username, String format) {
		this.id = id;
		this.heard = heard;
		this.urgent = urgent;
		this.folder = folder;
		this.duration = duration;
		this.received = received;
		this.author = author;
		this.authorExtension = authorExtension;
		this.username = username;
        this.format = format;
	}
	public String getId() {
		return id;
	}
	public boolean isHeard() {
		return heard;
	}
	public boolean isUrgent() {
		return urgent;
	}
	public String getFolder() {
		return folder;
	}
	public String getDuration() {
		return duration;
	}
	public long getReceived() {
		return received;
	}
	public String getAuthor() {
		return author;
	}	
	public String getAuthorExtension() {
		return authorExtension;
	}
	public String getUsername() {
		return username;
	}
    public String getFormat(){
        return this.format;
    }
}
