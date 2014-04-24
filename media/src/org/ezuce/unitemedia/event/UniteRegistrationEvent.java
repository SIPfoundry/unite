package org.ezuce.unitemedia.event;

public class UniteRegistrationEvent implements UniteEvent {
	private String m_userId;
	private UniteRegistrationEventType m_regEventType;
	
	public UniteRegistrationEvent(String userId, UniteRegistrationEventType eventType) {
		m_userId = userId;
		m_regEventType = eventType;
	}

	public String getUserId() {
		return m_userId;
	}

	public UniteRegistrationEventType getEventType() {
		return m_regEventType;
	}	
}
