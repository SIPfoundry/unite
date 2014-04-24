package org.ezuce.unitemedia.service;

import java.awt.Dimension;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;

import org.ezuce.unitemedia.streaming.StreamType;
import org.jitsi.service.neomedia.format.MediaFormat;
import org.jivesoftware.smack.packet.IQ;

public interface IQColibriService {
	public static final int PAYLOAD_ID = 105;

	ColibriConferenceIQ createConferenceIQForInvitee(ColibriConferenceIQ.Channel channel, String moderator, String invitee, StreamType strType);
	
	void addScreenSizeAdvancedAttr(ColibriConferenceIQ.Channel channel, Dimension size);
	
	Dimension getRecvScreenSizeAdvancedAttr(ColibriConferenceIQ.Channel channel);
	
	ColibriConferenceIQ createConferenceIQForStopStream(String conferenceId, Set<String> channelIds, String videobridgeJID, String from, StreamType strType);
	
	ColibriConferenceIQ createConferenceIQForDenyStreaming(String conferenceId, String from, String to);
	
	ColibriConferenceIQ.Channel createChannel(List<MediaFormat> formats, int id);
	
	ColibriConferenceIQ parseConferenceIQ(IQ iq) throws Exception;
	
	void shutdownExecutor();
	
	ExecutorService getSingleThreadExecutor();
}
