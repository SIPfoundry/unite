package org.ezuce.unitemedia.listener;

import java.util.HashMap;
import java.util.Map;

import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteCallEventType;
import org.ezuce.unitemedia.phone.UniteCall;
import org.ezuce.unitemedia.phone.User;

import net.java.sip.communicator.service.protocol.event.CallEvent;
import net.java.sip.communicator.service.protocol.event.CallListener;

public class CallListenerImpl implements CallListener {
	private User m_user;
	private Map<String, CallConferenceListenerImpl> m_callConfListenerMap =  new HashMap<String, CallConferenceListenerImpl>() ;
	
	public CallListenerImpl(User user) {
		m_user = user;
	}
	@Override
	public void incomingCallReceived(CallEvent event) {
		UniteCall uniteCall = new UniteCall(event.getSourceCall(), event.isVideoCall(), 
				UniteCall.UniteCallType.INCOMING);
		m_user.addUniteCall(uniteCall);	
		m_user.notify(
				new UniteCallEvent(event.getSourceCall(), UniteCallEventType.CALL_INCOMING, false, event.isVideoCall()));
		m_callConfListenerMap.put(event.getSourceCall().getCallID(), new CallConferenceListenerImpl(m_user, uniteCall));
	}

	@Override
	public void outgoingCallCreated(CallEvent event) {
		UniteCall uniteCall = new UniteCall(event.getSourceCall(), event.isVideoCall(), 
				UniteCall.UniteCallType.OUTGOING);
		m_user.addUniteCall(uniteCall);
		m_user.notify(
				new UniteCallEvent(event.getSourceCall(), UniteCallEventType.CALL_OUTGOING, false, event.isVideoCall()));
		m_callConfListenerMap.put(event.getSourceCall().getCallID(), new CallConferenceListenerImpl(m_user, uniteCall));
	}

	@Override
	public void callEnded(CallEvent event) {
		CallConferenceListenerImpl callListener = m_callConfListenerMap.get(event.getSourceCall().getCallID()); 
		if (callListener != null) {
			callListener.dispose();
			m_callConfListenerMap.remove(event.getSourceCall().getCallID());
		}
		UniteCall uniteCall = m_user.removeUniteCall(event.getSourceCall().getCallID());
		m_user.notify(
				new UniteCallEvent(event.getSourceCall(), UniteCallEventType.CALL_ENDED, false, event.isVideoCall(), uniteCall.isNoResponse()));
	}
}
