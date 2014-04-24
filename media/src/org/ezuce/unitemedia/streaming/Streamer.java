package org.ezuce.unitemedia.streaming;

import java.awt.Dimension;
import java.util.ArrayList;

import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;

import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.listener.VideobridgeVideoListener;
import org.ezuce.unitemedia.service.IQColibriService;
import org.jitsi.impl.neomedia.MediaUtils;
import org.jitsi.impl.neomedia.device.ScreenDeviceImpl;
import org.jitsi.impl.neomedia.format.VideoMediaFormatImpl;
import org.jitsi.service.neomedia.format.MediaFormat;
import org.jivesoftware.smack.util.StringUtils;

public class Streamer {
	public void initializeStreaming(XMPPUser user, StreamType streamType, String... invitees) throws Exception {
		if (user.getStreamingConnection() != null && user.getStreamingConnection().isStreaming()) {
			System.out.println("Cannot start streaming");
			return;
		}
		ColibriConferenceIQ conference = new ColibriConferenceIQ();
		ColibriConferenceIQ.Content content = new ColibriConferenceIQ.Content();
		content.setName(StreamUtils.getContentType(streamType));
		
		MediaFormat format = null;
		if (streamType == StreamType.audio) { 
			format = MediaUtils.getMediaFormat("PCMU", 8000);
		} else {
			format = MediaUtils.getMediaFormat("H264", VideoMediaFormatImpl.DEFAULT_CLOCK_RATE);
		}
		ArrayList<MediaFormat> formatList = new ArrayList<MediaFormat>();
		formatList.add(format);
		
		//add channel for moderator - where the moderator should send the stream
		content.addChannel(ServiceContext.getIQColibriService().createChannel(formatList, IQColibriService.PAYLOAD_ID));
		//add channels for invitees
		System.out.println("INVITEES SIZE " + invitees.length);
		user.clearInvitees();
		for (String invitee : invitees) {
			//we already added channel for moderator, make sure not to add it again. Also moderator cannot be invitee
			if (invitee != null && !StringUtils.parseBareAddress(user.getXMPPConnection().getUser()).equals(StringUtils.parseBareAddress(invitee))) {
				System.out.println("MIRCEA   ACTUAL******** "+ invitee);
				user.addInvitee(invitee);
				content.addChannel(ServiceContext.getIQColibriService().createChannel(formatList, IQColibriService.PAYLOAD_ID));
			}
		}
		conference.addContent(content);
		conference.setTo(user.getVideobridgeJID());
		conference.setFrom(user.getXMPPConnection().getUser());
		//close any existing connection before creating a new one
		StreamConnection streamConn = user.getStreamingConnection();
		if (streamConn != null) {
			System.out.println("Existing stream connection ---- just close it");
			streamConn.close();
		}
		StreamConnection streamConnection = new StreamConnection(DirectionType.sendonly, user.getTransmitterRTPPort(), user.getTransmitterRTCPPort(), streamType);
		streamConnection.setScreenSize(ScreenDeviceImpl.getDefaultScreenDevice().getSize());
		user.setStreamingConnection(streamConnection); 
		user.getXMPPConnection().sendPacket(conference);
	}
	
	public void stopStreaming(XMPPUser user) throws Exception {
		String from = user.getXMPPConnection().getUser();
		StreamConnection streamConn = user.getStreamingConnection();
		if (streamConn != null && user.getConferenceId() != null) {
			//The conference is live, send STOP packet to videobridge
			ColibriConferenceIQ conf = ServiceContext.getIQColibriService().createConferenceIQForStopStream(user.getConferenceId(),
				user.getStreamingConnection().getChannelIds(), user.getVideobridgeJID(), from, streamConn.getStreamType());
			user.getXMPPConnection().sendPacket(conf);			
		} else {
			System.out.println("Do not send any packet to videobridge, " +
					"this conference never become live, this is nothing for videobridge to do");
		}
		if (streamConn != null) {
			streamConn.close();
		}
		user.setStreamingConnection(null);
		//make sure to clear conference id - there is no conference attached to user
		user.setConferenceId(null);
		user.clearInvitees();
		user.setMediaChannel(null);
	}
	
	public boolean isStreaming(XMPPUser user) {
		StreamConnection streamConn = user.getStreamingConnection();
		return streamConn != null && streamConn.isStreaming();
	}
	
	public boolean isReceiving(XMPPUser user) {
		StreamConnection streamConn = user.getStreamingConnection();
		return streamConn != null && streamConn.isStreaming() && StreamUtils.isReceiver(streamConn.getDirectionType());
	}
	
	public boolean isTransmitting(XMPPUser user) {
		StreamConnection streamConn = user.getStreamingConnection();
		return streamConn != null && streamConn.isStreaming() && StreamUtils.isTransmitter(streamConn.getDirectionType());
	}
	
	/**
	 * returns true if receiver is in accept/reject mode. false if nothing happened or started to receive
	 * @return
	 */
	public boolean isInitializing(XMPPUser user) {
		return !isReceiving(user) && user.getMediaChannel() != null;
	}
	
	public void rejectReceiving(XMPPUser user) {
		String from = user.getXMPPConnection().getUser();
		user.getXMPPConnection().sendPacket(
				ServiceContext.getIQColibriService().createConferenceIQForDenyStreaming(user.getConferenceId(), from, user.getFromJID()));
		user.setMediaChannel(null);
	}
	
	public void acceptReceiving(XMPPUser user, MediaChannel mediaChannel, StreamType streamType, String vbPublicAddress) {
		try {	
			//The StreamType will depend on the content to receive. We are ready to receive the stream
			//The client will just need to connect to channel port/host
			//TODO change here if other stream type needed
			StreamConnection streamConn = new StreamConnection(DirectionType.receiveonly, 
					user.getReceiverRTPPort(), user.getReceiverRTCPPort(), streamType);
			VideobridgeVideoListener videoListener = new VideobridgeVideoListener(user);
			streamConn.setVideoListener(videoListener);
			streamConn.notifyAndConnect(mediaChannel.getRtpPort(), 
					mediaChannel.getRtcpPort(), vbPublicAddress);
			user.setStreamingConnection(streamConn);
		} catch (Exception e) {
			System.out.println("Exception during connect....");
			e.printStackTrace();
		}
	}
	
}
