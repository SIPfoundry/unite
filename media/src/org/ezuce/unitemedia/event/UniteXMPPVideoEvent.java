package org.ezuce.unitemedia.event;

import org.ezuce.unitemedia.streaming.MultiCastData;

public class UniteXMPPVideoEvent implements UniteEvent {
	private UniteXMPPVideoEventType m_eventType;
	private MultiCastData m_multiCastData;
	
	public UniteXMPPVideoEvent(UniteXMPPVideoEventType eventType, MultiCastData multiCastData) {
		m_multiCastData = multiCastData;
		m_eventType = eventType;
	}
	
	@Override
	public UniteXMPPVideoEventType getEventType() {
		return m_eventType;
	}

	public MultiCastData getMultiCastData() {
		return m_multiCastData;
	}	

}
