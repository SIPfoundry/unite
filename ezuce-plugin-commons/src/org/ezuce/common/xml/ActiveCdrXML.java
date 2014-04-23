package org.ezuce.common.xml;

public class ActiveCdrXML {
	private String from;
	private String fromAor;
	private String to;
	private String toAor;
	private String direction;
	private String recipient;
	private boolean internal;
	private String type;
	private long startTime;
	private long duration;
		
	public ActiveCdrXML(String from, String fromAor, String to, String toAor,
			String direction, String recipient, boolean internal, String type,
			long startTime, long duration) {
		super();
		this.from = from;
		this.fromAor = fromAor;
		this.to = to;
		this.toAor = toAor;
		this.direction = direction;
		this.recipient = recipient;
		this.internal = internal;
		this.type = type;
		this.startTime = startTime;
		this.duration = duration;
	}
	public String getFrom() {
		return from;
	}
	public String getFromAor() {
		return fromAor;
	}
	public String getTo() {
		return to;
	}
	public String getToAor() {
		return toAor;
	}
	public String getDirection() {
		return direction;
	}
	public String getRecipient() {
		return recipient;
	}
	public boolean isInternal() {
		return internal;
	}
	public String getType() {
		return type;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getDuration() {
		return duration;
	}
	
	   
}
