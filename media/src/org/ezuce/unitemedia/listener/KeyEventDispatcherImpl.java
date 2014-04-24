package org.ezuce.unitemedia.listener;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.CallPeer;
import net.java.sip.communicator.service.protocol.OperationSetDTMF;

import org.ezuce.unitemedia.phone.UniteCall;
import org.ezuce.unitemedia.phone.User;
import org.jitsi.service.protocol.DTMFTone;

public class KeyEventDispatcherImpl implements KeyEventDispatcher {
	private User m_user;
	
	public KeyEventDispatcherImpl(User user) {
		m_user = user;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
        switch (e.getID()) {
        case KeyEvent.KEY_PRESSED:
        	for (UniteCall uniteCall : m_user.getUniteCalls()) {
        		if (uniteCall.isConnected()) {
        			startSendingDtmf(e.getKeyChar(), uniteCall.getCall());
        		}
        	}
            break;
        case KeyEvent.KEY_RELEASED:
        	for (UniteCall uniteCall : m_user.getUniteCalls()) {
        		if (uniteCall.isConnected()) {
        			stopSendingDtmfTone(uniteCall.getCall());
        		}
        	}
            break;
        }
		return false;
	}
	
	private synchronized void startSendingDtmf(char dtmf, Call call) {
        Iterator<? extends CallPeer> callPeers = call.getCallPeers();
        try {
            while (callPeers.hasNext()) {
                CallPeer peer = callPeers.next();
                OperationSetDTMF dtmfOpSet
                    = peer.getProtocolProvider().getOperationSet(
                            OperationSetDTMF.class);

                if (dtmfOpSet != null) {
                	DTMFTone tone= DTMFTone.getDTMFTone(String.valueOf(dtmf));
                	if (tone != null) {
                		dtmfOpSet.startSendingDTMF(peer, tone);
                	} else {
                		System.out.println("DTMF tone not available");
                	}
                }
            }
        }
        catch (Throwable t) {
        	t.printStackTrace();
            System.out.println("Failed to send a DTMF tone.");
        }
	}
	
    private synchronized void stopSendingDtmfTone(Call call) {
        Iterator<? extends CallPeer> callPeers = call.getCallPeers();
        try {
            while (callPeers.hasNext()) {
                CallPeer peer = callPeers.next();
                OperationSetDTMF dtmfOpSet
                    = peer.getProtocolProvider().getOperationSet(
                            OperationSetDTMF.class);

                if (dtmfOpSet != null)
                    dtmfOpSet.stopSendingDTMF(peer);
            }
        }
        catch (Throwable t) {
            System.out.println("Failed to send a DTMF tone.");
        }
    }	

}
