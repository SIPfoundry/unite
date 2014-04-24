package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;

public class InviteToConfListener implements ActionListener{
	private String m_callId;
	
	public InviteToConfListener(String callId) {
		m_callId = callId;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		PhoneManager.getInstance().inviteIntoConference(m_callId);
	}
}
