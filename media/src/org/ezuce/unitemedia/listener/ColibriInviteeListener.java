package org.ezuce.unitemedia.listener;

import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;
import org.ezuce.unitemedia.streaming.XMPPUser;

public class ColibriInviteeListener extends ColibriListener {
	
	public ColibriInviteeListener(XMPPUser user) {
		super(user);
	}
	
	@Override
	public void processPacketFromVideobridge(ColibriConferenceIQ parsedIQ) {
	
	}

	@Override
	public void processPacketFromInvitee(ColibriConferenceIQ parsedIQ) {

	}	
}