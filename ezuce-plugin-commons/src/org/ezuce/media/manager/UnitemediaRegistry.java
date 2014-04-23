package org.ezuce.media.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.fmj.media.Log;

import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;

public class UnitemediaRegistry {
	private static UnitemediaRegistry unitemediaRegistry = new UnitemediaRegistry();
	
	private List<UnitemediaEventListener> listListener = new ArrayList<UnitemediaEventListener>();
	
	public static synchronized UnitemediaRegistry getInstance() {
		return unitemediaRegistry;
	}
	public void addUnitemediaEventListener(UnitemediaEventListener listener) {
		listListener.add(listener);
	}
	
	public void removeUnitemediaEventListener(UnitemediaEventListener listener) {
		listListener.remove(listener);
	}
	
	public void callOutgoing(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.callOutgoing(event);
		}
	}
	
	public void callIncoming(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.callIncoming(event);
		}
	}
	
	public void callEnded(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.callEnded(event);
		}
	}
	
	public void incomingCallInProgress(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.incomingCallInProgress(event);
		}
	}
	
	public void outgoingCallInProgress(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.outgoingCallInProgress(event);
		}	
	}
	
	public void locallyOnHold(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.locallyOnHold(event);
		}
	}
	
	public void mutuallyOnHold(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.mutuallyOnHold(event);
		}
	}
	
	public void remotelyOnHold(UniteCallEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.remotelyOnHold(event);
		}
	}
	
	public void localVideoStreaming(UniteVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.localVideoStreaming(event);
		}
	}
	
	public void remoteScreenAdded(UniteVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.remoteScreenAdded(event);
		}
	}
	
	public void remoteScreenRemoved(UniteVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.remoteScreenRemoved(event);
		}
	}
	
	public void localScreenAdded(UniteVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.localScreenAdded(event);
		}
	}
	
	public void localScreenRemoved(UniteVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.localScreenRemoved(event);
		}
	}
	
	public void transmitterStreamInitialized(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.transmitterStreamInitialized(event);
		}
	}
	
	public void transmitterStreamDenied(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.transmitterStreamDenied(event);
		}
	}
	
	public void transmitterStreamStarted(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.transmitterStreamStarted(event);
		}
	}
	
	public void transmitterStreamEnded(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.transmitterStreamEnded(event);
		}
	}
	
	public void receiverStreamInitialized(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.receiverStreamInitialized(event);
		}
	}
	
	public void receiverStreamStarted(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.receiverStreamStarted(event);
		}
	}
	
	public void receiverStreamEnded(UniteXMPPVideoEvent event) {
		for (UnitemediaEventListener listener : listListener) {
			listener.receiverStreamEnded(event);
		}
	}
	
	public void customize() {
		for (UnitemediaEventListener listener : listListener) {
			listener.customize();
		}
	}
}
