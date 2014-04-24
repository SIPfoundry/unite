package org.ezuce.unitemedia.listener;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;
import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ.Channel;

import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEventType;
import org.ezuce.unitemedia.streaming.ChannelMulticastData;
import org.ezuce.unitemedia.streaming.DirectionType;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.ezuce.unitemedia.streaming.MultiCastData;
import org.ezuce.unitemedia.streaming.ParticipantMultiCastData;
import org.ezuce.unitemedia.streaming.StreamConnection;
import org.ezuce.unitemedia.streaming.StreamType;
import org.ezuce.unitemedia.streaming.XMPPUser;
import org.jivesoftware.smack.XMPPConnection;


/**
 * Listener for all colibri packets
 *
 */
public class ColibriListener extends AbstractColibriListener {
	private List<ColibriConferenceIQ> m_packets = new ArrayList<ColibriConferenceIQ>();
	
	public ColibriListener(XMPPUser user) {
		super(user);
	}

	@Override
	public void processPacketFromInvitee(ColibriConferenceIQ parsedIQ) {
		//notify moderator user that the receiver rejected the stream
		ParticipantMultiCastData pmData = new ParticipantMultiCastData();
		pmData.setImAddress(parsedIQ.getFrom());
		if (parsedIQ.getContents().size() == 0) {
			m_user.notify(new UniteXMPPVideoEvent(UniteXMPPVideoEventType.TRANSMITTER_STREAM_DENIED, pmData));
		}
	}

	@Override
	public void processPacketFromModerator(ColibriConferenceIQ parsedIQ) {
		try {
			//TODO check if we need different content to receive
			ColibriConferenceIQ.Content content = parsedIQ.getContent("video");
			List<ColibriConferenceIQ.Channel> channels = content.getChannels();

			if (channels.size() > 0) {
				notifyUserToReceiveStream(channels);

			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * Typically the videobridge notifies moderator two times
	 * -A first time - as a response to colibri packet sent by moderator to videobridge to initialize a screen sharing conference
	 *  where the videobridge specifies channels for moderator and each invitee (no ssrc value)
	 *  the packet will contain channels for moderator and invitees
	 * -B second time when it received the stream from moderator.
	 *  the packet will contain only one single channel, the one associated to moderator and ssrc value 
	 *  in this moment the moderator can notify invitees with channel info where to receive the moderator stream
	 */
	@Override
	public void processPacketFromVideobridge(ColibriConferenceIQ parsedIQ) {
		try {
			String id = parsedIQ.getID();
			ChannelMulticastData mcData = new ChannelMulticastData();
			ColibriConferenceIQ.Content content = parsedIQ.getContent("video");
			List<ColibriConferenceIQ.Channel> channels = content.getChannels();
			MediaChannel videoChannel;
			boolean moderatorChannelAssociated = false;
			ColibriConferenceIQ conference;
			int i = 0;
			UniteXMPPVideoEventType notifyEvent = null;
			//This is an end conference event as a response from videobridge that moderator ended
			if (channels == null || channels.size() == 0) {
				System.out.println("Transmitted STREAM ENDED");
				m_user.notify(new UniteXMPPVideoEvent(UniteXMPPVideoEventType.TRANSMITTER_STREAM_ENDED, null));
				return;
			}
			for (ColibriConferenceIQ.Channel channel : channels) {
				System.out.println("MIRCEA channel aaa " + channel + " SSRC " + channel.getSSRCs()+ " CCC " + channels.size());
				//memorize all channel ids - we need them when we want to stop the conference
		    	m_user.getStreamingConnection().addChannelId(channel.getID());
				int [] ssrcs = channel.getSSRCs();
			    if (!moderatorChannelAssociated) {
					videoChannel = new MediaChannel(channel.getHost(), channel.getRTPPort(), channel.getRTCPPort(), channel.getExpire());
					videoChannel.setSSRCs(ssrcs);
					mcData.setMediaChannel(videoChannel);
					if (ssrcs == null || ssrcs.length == 0) {
						notifyEvent = UniteXMPPVideoEventType.TRANSMITTER_STREAM_INITIALIZED;
					} else {
						notifyEvent = UniteXMPPVideoEventType.TRANSMITTER_STREAM_STARTED;
						//user initiated conference is now live. keep the ID. Eventually we want to stop it at some point
						m_user.setConferenceId(id);
					}
					moderatorChannelAssociated = true;
				} else {
					//we need to send the screen size to the receiver, so the receiver to properly scale resolution
					ServiceContext.getIQColibriService().addScreenSizeAdvancedAttr(channel, m_user.getStreamingConnection().getScreenSize());
					conference = ServiceContext.getIQColibriService().createConferenceIQForInvitee(channel, 
							m_user.getXMPPConnection().getUser(), m_user.getInvitees().get(i++), m_user.getStreamingConnection().getStreamType());
					System.out.println("****Collect packet to send to invitee");
					//collect packets to send to invitees. Packets will be sent to invitees when videobridge is ready to relay the stream.
					//Videobridge becomes ready to relay the stream when sends back to moderator the channel that moderator has chosen + ssrc value to mark that
					//the moderator started to send the stream and vidobridge acknowledged that
					m_packets.add(conference);
				}			    
			}
			if (notifyEvent != null) {
				if (notifyEvent == UniteXMPPVideoEventType.TRANSMITTER_STREAM_STARTED) {
					System.out.println("MODERATOR STARTED STREAMING -notify invitees with host/port info");
					//notify all invitees that moderator just started streaming and relaying can proceed
					//send channel information to invitee to inform where to listen for incoming stream
					notifyInvitees();
				}
				//Notiy moderator last because we do not want to overlap with other packets videobridge might send
				//For example if TRANSMITTER_STREAM_INITIALIZED is to be notified, moderator will start sending stream and
				//will receive back from videobridge a new packet with the ssrc value
				//We need to make sure that notification is executed after processPacket exits
				System.out.println("Notifiy moderator with event " + notifyEvent);
				ServiceContext.getIQColibriService().getSingleThreadExecutor().execute(new NotifyTask(notifyEvent, mcData));				
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Invitee needs to send empty datagram packets to videobridge in a synchronized way
	 * Videobridge needs to relay data in a secvential way to all invitees
	 * @param channels
	 * @throws Exception
	 */
	private synchronized void notifyUserToReceiveStream(List<Channel> channels) throws Exception {
		Channel channel = channels.get(0);
		ChannelMulticastData mcData = new ChannelMulticastData();
		Dimension recvSize = ServiceContext.getIQColibriService().getRecvScreenSizeAdvancedAttr(channel);
		System.out.println("Notify user about SIZE: " + recvSize);
		MediaChannel mediaChannel = null;
		if(recvSize == null) {
			mediaChannel = new MediaChannel(channel.getHost(), channel.getRTPPort(), channel.getRTCPPort(), channel.getExpire());
		} else {
			mediaChannel = new MediaChannel(channel.getHost(), channel.getRTPPort(), channel.getRTCPPort(), channel.getExpire(), (int)recvSize.getWidth(), (int)recvSize.getHeight());
		}
		mcData.setMediaChannel(mediaChannel);
		//make sure to close any opened connector. At this point invitee is about to notify videobridge that is ready to receive data
	    //and will immediately open a streamConnection to receive moderator's stream
		if (m_user.getStreamingConnection() != null) {
			m_user.getStreamingConnection().close();
		}
		m_user.setMediaChannel(mediaChannel);
		m_user.notify(new UniteXMPPVideoEvent(UniteXMPPVideoEventType.RECEIVER_STREAM_INITIALIZED, mcData));
	}
	
	private void notifyInvitees() {
		System.out.println("Notifying invitees MMMMIRCEA AA ");
		XMPPConnection connXMPP = m_user.getXMPPConnection();
		for (ColibriConferenceIQ packet : m_packets) {
			System.out.println("Mircea send Packet : " + packet.toXML());
			connXMPP.sendPacket(packet);
		}
		//invitees are notified with channel info where to receive the data. 
		//the immediate step that the invitee does is to send an empty datagram back to videobridge, so videobridge knows to relay the moderator stream
		//@see invitee packet interceptor: VideobridgeInviteeListener.java
		m_packets.clear();
	}
	
	private class NotifyTask implements Runnable {
		UniteXMPPVideoEventType m_eventType;
		MultiCastData m_data;
		public NotifyTask(UniteXMPPVideoEventType eventType, MultiCastData data) {
			m_eventType = eventType;
			m_data = data;
		}
		@Override
		public void run() {
			m_user.notify(new UniteXMPPVideoEvent(m_eventType, m_data));
			
		}
		
	}	

}
