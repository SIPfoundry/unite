package org.ezuce.media.manager;

import java.awt.Component;
import java.util.Collection;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.phone.notifications.EzuceNotification;
import org.ezuce.common.resource.Utils;
import org.ezuce.media.ui.VideoWindow;
import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteCallEventType;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteVideoEventType;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEventType;
import org.ezuce.unitemedia.phone.UniteCall;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.ezuce.unitemedia.streaming.MultiCastData;
import org.jivesoftware.spark.util.log.Log;

public class UnitemediaEventManager {
	private boolean receivingXMPPVideo;
	private boolean transmittingXMPPVideo;
	private boolean onCall;
	private boolean remoteVideoCallAdded;
	private boolean localVideoCallAdded;
	
	private static UnitemediaEventManager singleton = new UnitemediaEventManager();
	
    public static synchronized UnitemediaEventManager getInstance() {
        return singleton;
    }
    
    public void updateGivenCallEvent(UniteCallEvent event) {
    	String extension = PhoneManager.getInstance().getCaleeExtension(event.getCall());
    	
    	UIMediaManager mediaManager = UIMediaManager.getInstance();
    	PhoneManager phoneManager = PhoneManager.getInstance();
    	UnitemediaRegistry registry = UnitemediaRegistry.getInstance();
    	AudioNotificationManager audioManager = AudioNotificationManager.getInstance();
    	
		switch ((UniteCallEventType) event.getEventType()) {
		case CALL_OUTGOING:
			audioManager.playCallingSound();
			mediaManager.updateAllCallOutgoing(extension, event.getCall().getCallID(), event.isVideo());
			onCall = true;
			registry.callOutgoing(event);
			break;
		case CALL_INCOMING:
			audioManager.playRingingSound();
			mediaManager.updateAllCallIncoming(extension, event.getCall().getCallID(), event.isVideo());
			onCall = true;
			registry.callIncoming(event);
			break;
		case CALL_ENDED:
			//we need to stop sound here too for cases when the call never gets in progress
			audioManager.stopSound();
			mediaManager.refreshSIPVideo();
			//might be an incomming call that was never answered
	    	EzuceNotification.getInstance().hideCall();
			mediaManager.updateCallTabCallEnded(event.getCall().getCallID());
			mediaManager.updateChatCallEnded(event.getCall().getCallID());
			onCall = phoneManager.getActiveCalls().size() > 0;
			registry.callEnded(event);
			break;
		case INCOMING_CALL_IN_PROGRESS:
			audioManager.stopSound();
	    	EzuceNotification.getInstance().hideCall();
			mediaManager.updateIncomingCallTabCallInProgress(event.getCall().getCallID(), phoneManager.isCalledNumberOwnedConference(extension));
			mediaManager.updateIncomingChatCallInProgress(event.getCall().getCallID(), phoneManager.isCalledNumberOwnedConference(extension));
			if (event.isPreviouslyOnHold()) {
				mediaManager.rebuildJPanelCallTabMainControls(event.getCall().getCallID());
				mediaManager.rebuildJPanelChatMainControls(event.getCall().getCallID());
			}
			putAllOnHoldExceptId(event);
			onCall = true;
			registry.incomingCallInProgress(event);
			break;
		case OUTGOING_CALL_IN_PROGRESS:
			audioManager.stopSound();
			mediaManager.updateOutgoingCallTabCallInProgress(event.getCall().getCallID(), phoneManager.isCalledNumberOwnedConference(extension));
			mediaManager.updateOutgoingChatCallInProgress(event.getCall().getCallID(), phoneManager.isCalledNumberOwnedConference(extension));
			if (event.isPreviouslyOnHold()) {
				mediaManager.rebuildJPanelCallTabMainControls(event.getCall().getCallID());
				mediaManager.rebuildJPanelChatMainControls(event.getCall().getCallID());
			}
			putAllOnHoldExceptId(event);
			onCall = true;
			registry.outgoingCallInProgress(event);
			break;
		case LOCALLY_ON_HOLD:
			mediaManager.refreshSIPVideo();
			mediaManager.updateCallTabCallOnHold(event.getCall().getCallID());
			mediaManager.updateChatCallOnHold(event.getCall().getCallID());
			onCall = true;
			registry.locallyOnHold(event);
			break;
		case MUTUALY_ON_HOLD:
			mediaManager.refreshSIPVideo();
			mediaManager.updateCallTabCallOnHold(event.getCall().getCallID());
			mediaManager.updateChatCallOnHold(event.getCall().getCallID());
			onCall = true;
			registry.mutuallyOnHold(event);
			break;
		case REMOTELY_ON_HOLD:
			mediaManager.refreshSIPVideo();
			mediaManager.updateCallTabCallOnHold(event.getCall().getCallID());
			mediaManager.updateChatCallOnHold(event.getCall().getCallID());
			onCall = true;
			registry.remotelyOnHold(event);
			break;
	    }	
    }
    
    public void updateGivenSIPVideoEvent(UniteVideoEvent event) {
    	UIMediaManager mediaManager = UIMediaManager.getInstance();
    	UnitemediaRegistry registry = UnitemediaRegistry.getInstance();
    	
		switch ((UniteVideoEventType) event.getEventType()) {
		case LOCAL_VIDEO_STREAMING:
			if (PhoneManager.getInstance().isScreenSharingEnabled()) {
				mediaManager.showTransmitterCallTabEnabledScreenSharingPanel();
				mediaManager.showTransmitterChatEnabledScreenSharingPanel();
			}
			registry.localVideoStreaming(event);
			break;
		case REMOTE_SCREEN_ADDED:
			mediaManager.refreshSIPVideo();
			remoteVideoCallAdded = true;
			registry.remoteScreenAdded(event);
			break;
		case REMOTE_SCREEN_REMOVED:
			mediaManager.refreshSIPVideo();
			remoteVideoCallAdded = false;
			registry.remoteScreenRemoved(event);
			break;
		case LOCAL_SCREEN_ADDED:
			mediaManager.refreshSIPVideo();
			localVideoCallAdded = true;
			registry.localScreenAdded(event);
			break;
		case LOCAL_SCREEN_REMOVED:
			mediaManager.refreshSIPVideo();
			localVideoCallAdded = false;
			registry.localScreenRemoved(event);
			break;
		}
    }
    
    public void updateGivenXMPPVideoEvent(UniteXMPPVideoEvent event) {
    	UIMediaManager mediaManager = UIMediaManager.getInstance();
    	StreamingManager strManager = StreamingManager.getInstance();
		MultiCastData mcData = event.getMultiCastData();
		UnitemediaRegistry registry = UnitemediaRegistry.getInstance();
		
		switch((UniteXMPPVideoEventType) event.getEventType()) {
		case TRANSMITTER_STREAM_INITIALIZED:
			Log.warning("SCREEN SHARING INITIALIZED");
			mediaManager.showTransmitterCallTabConnectingScreenSharingPanel();
			mediaManager.showTransmitterChatConnectingScreenSharingPanel();
			strManager.connect((MediaChannel)event.getMultiCastData().getData());
			transmittingXMPPVideo = true;
			registry.transmitterStreamInitialized(event);
			break;
		case TRANSMITTER_STREAM_DENIED:
			String address = (String)mcData.getData();
			Log.warning("Screen sharing session denied by:" + address);
			transmittingXMPPVideo = false;
			registry.transmitterStreamDenied(event);
			break;
		case TRANSMITTER_STREAM_STARTED:
			Log.warning("YOUR STREAM STARTED TO BE SENDING TO THE VIDEOBRIDGE");
			mediaManager.showTransmitterCallTabEnabledScreenSharingPanel();
			mediaManager.showTransmitterChatEnabledScreenSharingPanel();
			StreamingManager.getInstance().sendAcceptMessageToInvitees();
			transmittingXMPPVideo = true;
			registry.transmitterStreamStarted(event);
			break;
		case TRANSMITTER_STREAM_ENDED:
			Log.warning("YOUR STREAM STOPPED TO BE SENDING TO THE VIDEOBRIDGE");
			mediaManager.hideTransmitterCallTabScreenSharingPanel();
			mediaManager.hideTransmitterChatScreenSharingPanel();
			transmittingXMPPVideo = false;
			registry.transmitterStreamEnded(event);
		    break;	
		case RECEIVER_STREAM_INITIALIZED:
			Log.error("MIRCEA INITALIZEEEEE");
			mediaManager.showReceiverCallTabAcceptScreenSharingPanel();
			mediaManager.showReceiverChatAcceptScreenSharingPanel();
			receivingXMPPVideo = true;
			registry.receiverStreamInitialized(event);
			break;			
		case RECEIVER_STREAM_STARTED:			
			Log.warning("Stream STARTED MIRCEA ^%%%%%%%%%%% "+mcData.getData());
			mediaManager.showReceiverCallTabEnabledScreenSharingPanel();
			mediaManager.showReceiverChatEnabledScreenSharingPanel();
			mediaManager.addXMPPVideo((Component)mcData.getData());
    		VideoWindow vWindow = mediaManager.getVideoWindowManager().getVideoWindowByXMPPJid(org.ezuce.common.resource.Utils.getImId(strManager.getFromJID()));
    		vWindow.setOpponentName(org.ezuce.common.resource.Utils.getAlias(strManager.getFromJID()));
    		vWindow.restartDurationTimer();
			receivingXMPPVideo = true;
			registry.receiverStreamStarted(event);
			break;
		case RECEIVER_STREAM_ENDED:
			mediaManager.hideReceiverCallTabScreenSharingPanel();
			mediaManager.hideReceiverChatScreenSharingPanel();
			mediaManager.removeXMPPVideo((Component)mcData.getData());
			StreamingManager.getInstance().disconnect();
			receivingXMPPVideo = false;
			registry.receiverStreamEnded(event);
			break;			
		}
    }
    
	private void putAllOnHoldExceptId(final UniteCallEvent event) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PhoneManager.getInstance().putAllOnHoldExceptId(
						event.getCall().getCallID());	
			}
		});
	}
	
	public String onCall(String jid) {
		String extension = Utils.getExtension(jid);
		Collection<UniteCall> calls = PhoneManager.getInstance().getActiveCalls();

		for (UniteCall call : calls) {
			if (StringUtils.equals(PhoneManager.getInstance()
					.getCaleeExtension(call.getCall()), extension)) {
				return call.getCall().getCallID();
			}
		}
		return null;
	}

	public boolean isReceivingXMPPVideo() {
		return receivingXMPPVideo;
	}

	public boolean isTransmittingXMPPVideo() {
		return transmittingXMPPVideo;
	}

	public boolean isOnCall() {
		return onCall;
	}

	public boolean isRemoteVideoCallAdded() {
		return remoteVideoCallAdded;
	}

	public boolean isLocalVideoCallAdded() {
		return localVideoCallAdded;
	}

	public void setReceivingXMPPVideo(boolean receivingXMPPVideo) {
		this.receivingXMPPVideo = receivingXMPPVideo;
	}

	public void setTransmittingXMPPVideo(boolean transmittingXMPPVideo) {
		this.transmittingXMPPVideo = transmittingXMPPVideo;
	}		
}
