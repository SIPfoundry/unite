package org.ezuce.unitemedia.event;

import net.java.sip.communicator.service.protocol.Call;

public class UniteVideoEvent implements UniteEvent {
	private Call m_call;
	private UniteVideoEventType m_eventType;
	
	public UniteVideoEvent(Call call, UniteVideoEventType eventType) {
		m_call = call;
		m_eventType = eventType;
	}

	public UniteVideoEventType getEventType() {
		return m_eventType;
	}

	public Call getCall() {
		return m_call;
	}	
}
