package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;

public class HangupCallListener implements ActionListener {
	String m_callId;
	
	public HangupCallListener(String callId) {
		m_callId = callId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PhoneManager.getInstance().hangupCall(m_callId);
	}

}
