package org.ezuce.unitemedia.listener;

import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;

import org.ezuce.unitemedia.streaming.XMPPUser;

public class ColibriModeratorListener extends ColibriListener {

	public ColibriModeratorListener(XMPPUser user) {
		super(user);
	}

	@Override
	public void processPacketFromModerator(ColibriConferenceIQ parsedIQ) {
	}	
}
