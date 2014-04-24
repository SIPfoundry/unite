package org.ezuce.media.manager;

import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;

public interface UnitemediaEventListener {
	void callOutgoing(UniteCallEvent event);
	
	void callIncoming(UniteCallEvent event);
	
	void callEnded(UniteCallEvent event);
	
	void incomingCallInProgress(UniteCallEvent event);
	
	void outgoingCallInProgress(UniteCallEvent event);
	
	void locallyOnHold(UniteCallEvent event);
	
	void mutuallyOnHold(UniteCallEvent event);
	
	void remotelyOnHold(UniteCallEvent event);
	
	void localVideoStreaming(UniteVideoEvent event);
	
	void remoteScreenAdded(UniteVideoEvent event);
	
	void remoteScreenRemoved(UniteVideoEvent event);
	
	void localScreenAdded(UniteVideoEvent event);
	
	void localScreenRemoved(UniteVideoEvent event);
	
	void transmitterStreamInitialized(UniteXMPPVideoEvent event);
	
	void transmitterStreamDenied(UniteXMPPVideoEvent event);
	
	void transmitterStreamStarted(UniteXMPPVideoEvent event);
	
	void transmitterStreamEnded(UniteXMPPVideoEvent event);
	
	void receiverStreamInitialized(UniteXMPPVideoEvent event);
	
	void receiverStreamStarted(UniteXMPPVideoEvent event);
	
	void receiverStreamEnded(UniteXMPPVideoEvent event);
	
	void customize();
}
