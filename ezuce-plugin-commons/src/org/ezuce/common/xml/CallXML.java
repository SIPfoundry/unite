package org.ezuce.common.xml;

public class CallXML {
	private String callerAor;
	private String calleeAor;
	private String calleeContact;
	private String startTime;
	private String duration;
	private String termination;
	private String calleeRoute;
	public CallXML(String callerAor, String calleeAor, String calleeContact,
			String startTime, String duration, String termination,
			String calleeRoute) {
		super();
		this.callerAor = callerAor;
		this.calleeAor = calleeAor;
		this.calleeContact = calleeContact;
		this.startTime = startTime;
		this.duration = duration;
		this.termination = termination;
		this.calleeRoute = calleeRoute;
	}
	public String getCallerAor() {
		return callerAor;
	}
	public String getCalleeAor() {
		return calleeAor;
	}
	public String getCalleeContact() {
		return calleeContact;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getDuration() {
		return duration;
	}
	public String getTermination() {
		return termination;
	}
	public String getCalleeRoute() {
		return calleeRoute;
	}	
}
