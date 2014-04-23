package org.ezuce.media.manager;

import org.ezuce.media.ui.listeners.ScreenSharingInvitees;

public class ScreenSharingInviteesImpl implements ScreenSharingInvitees {	
	private String [] m_invitees;
	
	public ScreenSharingInviteesImpl(String ... invitees) {
		m_invitees = invitees;
	}
	
	@Override
	public String[] getInvitees() {
		return m_invitees;
	}

}
