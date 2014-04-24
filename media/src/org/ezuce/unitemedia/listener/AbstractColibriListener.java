package org.ezuce.unitemedia.listener;

import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;

import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.streaming.XMPPUser;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.IQ.Type;

public abstract class AbstractColibriListener implements PacketListener {
	protected XMPPUser m_user;
	
	public AbstractColibriListener(XMPPUser user) {
		m_user = user;
	}
	
	@Override
	public void processPacket(Packet packet) {
		IQ iq = (IQ)packet;
		if (iq == null || iq.getType() == Type.ERROR) {
			return;
		}
		ColibriConferenceIQ parsedIQ = null;
		try {
			parsedIQ = ServiceContext.getIQColibriService().parseConferenceIQ(iq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (parsedIQ == null || parsedIQ.getType() == Type.ERROR) {
			return;
		}
		System.out.println("From: " + iq.getFrom() + " videobridge JID: " + m_user.getVideobridgeJID());
		if (iq.getFrom().contains(m_user.getVideobridgeJID())) {
			System.out.println("You are moderator - you received colibri packet from videobridge " +parsedIQ.toXML());
			processPacketFromVideobridge(parsedIQ);
		} else {
			if (m_user.getInvitees().size() == 0) {
				System.out.println("You are invitee - you received colibri packet from moderator " + parsedIQ.toXML());
				m_user.setFromJID(iq.getFrom());
			    processPacketFromModerator(parsedIQ);
			} else {
				processPacketFromInvitee(parsedIQ);
			}
		}
	
	}
	
	public abstract void processPacketFromInvitee(ColibriConferenceIQ parsedIQ);
	
	public abstract void processPacketFromModerator(ColibriConferenceIQ parsedIQ);
	
	public abstract void processPacketFromVideobridge(ColibriConferenceIQ parsedIQ);
}
