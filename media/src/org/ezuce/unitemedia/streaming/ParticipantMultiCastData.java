package org.ezuce.unitemedia.streaming;

public class ParticipantMultiCastData implements MultiCastData {
	String m_imAddress;
	
	@Override
	public Object getData() {		
		return m_imAddress;
	}

	public void setImAddress(String imAddress) {
		m_imAddress = imAddress;
	}
}
