package org.ezuce.unitemedia.phone;

import net.java.sip.communicator.service.protocol.Call;

public class UniteCall {
	private boolean m_video;
	private Call m_call = null;
	private boolean m_connected = false;
	private boolean m_onHold = false;
	private boolean m_noResponse = true;
	public enum UniteCallType {
		INCOMING,
		OUTGOING;
	}
	private UniteCallType uniteCallType;
	
	public UniteCall(Call call, boolean video, UniteCallType type) {
		m_call = call;
		m_video = video;
		uniteCallType = type;
	}

	public boolean isVideo() {
		return m_video;
	}

	public Call getCall() {
		return m_call;
	}

	public UniteCallType getUniteCallType() {
		return uniteCallType;
	}

	public boolean isConnected() {
		return m_connected;
	}

	public void setConnected(boolean connected) {
		m_connected = connected;
		m_noResponse = false;
	}

	public boolean isOnHold() {
		return m_onHold;
	}

	public void setOnHold(boolean onHold) {
		m_onHold = onHold;
	}

	public boolean isNoResponse() {
		return m_noResponse;
	}	
}
