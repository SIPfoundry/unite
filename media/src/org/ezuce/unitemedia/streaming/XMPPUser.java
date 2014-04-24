package org.ezuce.unitemedia.streaming;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.ezuce.unitemedia.context.AbstractUser;
import org.ezuce.unitemedia.event.UniteEvent;
import org.ezuce.unitemedia.listener.ColibriInviteeListener;
import org.ezuce.unitemedia.listener.ColibriListener;
import org.ezuce.unitemedia.listener.ColibriModeratorListener;
import org.ezuce.unitemedia.listener.VideobridgeVideoListener;
import org.jitsi.service.neomedia.VideoMediaStream;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

public class XMPPUser extends AbstractUser {
	private XMPPConnection m_connection;
	private StreamConnection m_streamingConnection;
	private DirectionType m_directionType;
	private List<String> m_invitees = new ArrayList<String> ();
	private String conferenceId = null;
	private String m_videobridgeJID;
	private int m_transmitterRTPPort;
	private int m_transmitterRTCPPort;
	private int m_receiverRTPPort;
	private int m_receiverRTCPPort;
	//the JID from where packets are receiving - this information can is used by client
	//on receiving_initialized stream and start_receiving events 
	private String m_fromJID;
	private String m_videobridgeAddress;
	private MediaChannel m_mediaChannel;
	
	
	/**
	 * Creates a user designated to send/receive video stream based on XMPP signaling (Colibri)
	 * @param conn - The XMPP connection of the user
	 * @param videobridgeJID the JID for the installed videobridge
	 * @param directionType - a user can be only a sender (designated only to initiate/send video streams)
	 * or it can be only a receiver (to receive video streams), or it can both receive or send streams
	 */
	public XMPPUser(XMPPConnection conn, String videobridgeAddress, String videobridgeJID, 
			DirectionType directionType, int tranRTPPort, int tranRTCPPort, int recvRTPPort, int recvRTCPPort) {
		m_connection = conn;
		m_directionType = directionType;
		m_videobridgeJID = videobridgeJID;
		m_transmitterRTPPort = tranRTPPort;
		m_transmitterRTCPPort = tranRTCPPort;
		m_receiverRTPPort = recvRTPPort;
		m_receiverRTCPPort = recvRTCPPort;
		m_videobridgeAddress = videobridgeAddress;
		switch(m_directionType) {
		case sendonly:
			m_connection.addPacketListener(new ColibriModeratorListener(this), new PacketTypeFilter(IQ.class));
			break;
		case receiveonly:
			m_connection.addPacketListener(new ColibriInviteeListener(this), new PacketTypeFilter(IQ.class));
			break;
		case sendandreceive:
			m_connection.addPacketListener(new ColibriListener(this), new PacketTypeFilter(IQ.class));
			break;
		}
	}

	public XMPPConnection getXMPPConnection() {
		return m_connection;
	}
	
    public StreamConnection getStreamingConnection() {
    	return m_streamingConnection;
    }

	public void addInvitee(String invitee) {
		m_invitees.add(invitee);
	}

	public List<String> getInvitees() {
		return m_invitees;
	}
	
	public void clearInvitees() {
		m_invitees.clear();
	}

	public void setStreamingConnection(StreamConnection streamingConnection) {
		m_streamingConnection = streamingConnection;
	}

	public String getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}

	public String getVideobridgeJID() {
		return m_videobridgeJID;
	}

	public int getTransmitterRTPPort() {
		return m_transmitterRTPPort;
	}

	public int getTransmitterRTCPPort() {
		return m_transmitterRTCPPort;
	}

	public int getReceiverRTPPort() {
		return m_receiverRTPPort;
	}

	public int getReceiverRTCPPort() {
		return m_receiverRTCPPort;
	}

	public String getFromJID() {
		return m_fromJID;
	}

	public void setFromJID(String fromJID) {
		m_fromJID = fromJID;
	}

	public String getVideobridgeAddress() {
		return m_videobridgeAddress;
	}

	public MediaChannel getMediaChannel() {
		return m_mediaChannel;
	}

	public void setMediaChannel(MediaChannel mediaChannel) {
		m_mediaChannel = mediaChannel;
	}	
}
