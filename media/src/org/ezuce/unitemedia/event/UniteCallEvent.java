package org.ezuce.unitemedia.event;
import java.lang.Enum;

import net.java.sip.communicator.service.protocol.Call;
public class UniteCallEvent implements UniteEvent {
	private Call m_call;
	private UniteCallEventType m_eventType;
	private boolean m_previouslyOnHold;
	private boolean m_video;
	private boolean m_noResponse = true;
	
	public UniteCallEvent(Call call, UniteCallEventType eventType, boolean previouslyOnHold, boolean video, boolean noResponse) {
		m_call = call;
		m_eventType = eventType;
		m_previouslyOnHold = previouslyOnHold;
		m_video = video;
		m_noResponse = noResponse;
	}
	
	public UniteCallEvent(Call call, UniteCallEventType eventType, boolean previouslyOnHold, boolean video) {
		this(call, eventType, previouslyOnHold, video, true);
	}

	public Call getCall() {
		return m_call;
	}

	public Enum<?> getEventType() {
		return m_eventType;
	}

	public boolean isPreviouslyOnHold() {
		return m_previouslyOnHold;
	}

	public boolean isVideo() {
		return m_video;
	}

	public boolean isNoResponse() {
		return m_noResponse;
	}	
}
