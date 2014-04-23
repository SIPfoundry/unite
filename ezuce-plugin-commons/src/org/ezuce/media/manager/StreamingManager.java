package org.ezuce.media.manager;

import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.Utils;
import org.ezuce.media.observers.XMPPStreamingObserver;
import org.ezuce.media.ui.VideoWindowManager;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEventType;
import org.ezuce.unitemedia.streaming.DirectionType;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.ezuce.unitemedia.streaming.StreamType;
import org.ezuce.unitemedia.streaming.Streamer;
import org.ezuce.unitemedia.streaming.VideoMultiCastData;
import org.ezuce.unitemedia.streaming.XMPPUser;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class StreamingManager {
	private static StreamingManager singleton;
	private Streamer streamer;
	private XMPPUser user;
	private int TRANSMITTER_RTP_PORT = 2000;
	private int TANSMITTER_RTCP_PORT = 2001;
	private int RECEIVER_RTP_PORT = 4000;
	private int RECEIVER_RTCP_PORT = 4001;
	
	private StreamingManager() {
		streamer = new Streamer();
		user = new XMPPUser(SparkManager.getConnection(), Config.getInstance().getVideobridgeAddress(),
				Config.getInstance().getVideobridgeJid(), DirectionType.sendandreceive, 
				TRANSMITTER_RTP_PORT, TANSMITTER_RTCP_PORT, RECEIVER_RTP_PORT, RECEIVER_RTCP_PORT);
		user.addObserver(new XMPPStreamingObserver());
	}
	
    public static synchronized StreamingManager getInstance() {
    	if (!Config.getInstance().isVideobridgeAvailable()) {
    		throw new UnsupportedOperationException();
    	}    	
        if (null == singleton) {
            singleton = new StreamingManager();
            
            return singleton;
        }
        return singleton;
    }
    
    public void connect(MediaChannel channel) {
		try {
			Log.warning("Send stream to: " + Config.getInstance().getVideobridgeAddress() + " ports: " + channel.getRtpPort() + " ; " + channel.getRtcpPort());
			user.getStreamingConnection().connect(channel.getRtpPort(), 
					channel.getRtcpPort(), Config.getInstance().getVideobridgeAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    }
    
    public void sendAcceptMessageToInvitees() {
    	for (String invitee : user.getInvitees()) {
    		Message message = new Message();
            message.setType(Message.Type.chat);
            message.setTo(invitee);
            message.setFrom(user.getFromJID());
            message.setBody(Res.getString("screensharing.invitation"));
    		user.getXMPPConnection().sendPacket(message);
    	}
    }
    
    public void acceptReceiving(MediaChannel channel) {
    	streamer.acceptReceiving(user, channel, StreamType.desktopVideo, Config.getInstance().getVideobridgeAddress());
    }
    
    public void rejectReceiving() {
    	streamer.rejectReceiving(user);
    }
    
    public void disconnect() {
    	if (user.getStreamingConnection() != null) {
    		user.getStreamingConnection().close();
    		user.setStreamingConnection(null);
    	}
    }
    
    public void enableOrDisableScreenSharing(String ... invitees) {
    	try {
    		if(isScreenSharingEnabled()) {
    			streamer.stopStreaming(user);
    		} else {
    			streamer.initializeStreaming(user, StreamType.desktopVideo, invitees);
    		}
    	} catch (Exception ex) {
    		Log.error("Exception enabling/disabling screen share", ex);
    	}
    }
    
    public boolean isScreenSharingEnabled() {
    	Log.warning("IS SCREEN SHARING ENABLED ? MIRCEA HMMM " + streamer.isStreaming(user));
    	return streamer.isStreaming(user);
    }
    
    public void initializeScreenSharing(String... invitees) throws Exception {
		streamer.initializeStreaming(user, StreamType.desktopVideo, invitees);
    }
    
    public void stopScreenSharing() throws Exception {
    	streamer.stopStreaming(user);
    }
    
    public boolean isReceivingFrom(String jid) {
    	return getFromJID() != null && getFromJID().contains(jid);
    }
    
    public boolean isTransmittingTo(String jid) {
    	for (String invitee : user.getInvitees()) {
    		if (Utils.getImId(invitee).equals(Utils.getImId(jid))) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean isReceiving() {
    	return streamer.isReceiving(user);
    }
    
    public boolean isInitializing() {
    	return streamer.isInitializing(user);
    }
    
    public boolean isInviting() {
    	return !user.getInvitees().isEmpty();
    }
  
    public boolean isTransmitting() {
    	return streamer.isTransmitting(user);
    }
    
    public String getFromJID() {
    	return user.getFromJID();
    }
    
    public MediaChannel getReceiverMediaChannel() {
    	return user.getMediaChannel();
    }
}
