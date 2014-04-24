package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;

public class AcceptCallListener implements ActionListener {
	String m_callId;
	boolean m_video;
	
	public AcceptCallListener(String callId, boolean video) {
		m_callId = callId;
		m_video = video;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!m_video) {
			PhoneManager.getInstance().answerCall(m_callId);
		} else {
			PhoneManager.getInstance().answerVideoCall(m_callId);
		}
	}
}