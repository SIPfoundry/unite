package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;

public class OnOffHoldCallListener implements ActionListener {
	private String m_callId;
	
	public OnOffHoldCallListener(String callId) {
		m_callId = callId;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		PhoneManager.getInstance().putOnOffHold(m_callId);
	}

}
