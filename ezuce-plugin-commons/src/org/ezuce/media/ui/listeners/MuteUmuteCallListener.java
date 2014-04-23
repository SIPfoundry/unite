package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;

public class MuteUmuteCallListener implements ActionListener {
	String m_callId;
	
	public MuteUmuteCallListener(String callId) {
		m_callId = callId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PhoneManager.getInstance().muteUnmuteCall(m_callId);
	}
}
