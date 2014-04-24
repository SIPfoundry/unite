package org.ezuce.unitemedia.listener;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteCallEventType;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteVideoEventType;
import org.ezuce.unitemedia.phone.UniteCall;
import org.ezuce.unitemedia.phone.User;
import org.jitsi.util.event.VideoEvent;
import org.jitsi.util.event.VideoListener;

import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.CallConference;
import net.java.sip.communicator.service.protocol.CallPeer;
import net.java.sip.communicator.service.protocol.CallPeerState;
import net.java.sip.communicator.service.protocol.ConferenceMember;
import net.java.sip.communicator.service.protocol.OperationSetVideoTelephony;
import net.java.sip.communicator.service.protocol.event.CallChangeEvent;
import net.java.sip.communicator.service.protocol.event.CallChangeListener;
import net.java.sip.communicator.service.protocol.event.CallPeerChangeEvent;
import net.java.sip.communicator.service.protocol.event.CallPeerConferenceAdapter;
import net.java.sip.communicator.service.protocol.event.CallPeerConferenceEvent;
import net.java.sip.communicator.service.protocol.event.CallPeerEvent;
import net.java.sip.communicator.service.protocol.event.CallPeerListener;

public class CallConferenceListenerImpl extends CallPeerConferenceAdapter
		implements CallChangeListener, PropertyChangeListener, VideoListener, CallPeerListener {
	private User m_user;
	private UniteCall m_call;
	private DSMouseAndKeyboardListener dsListener;
	public CallConferenceListenerImpl(User user, UniteCall call) {
		m_user = user;
		m_call = call;
		CallConference callConference = m_call.getCall().getConference();
		callConference.addCallChangeListener(this);
		callConference.addCallPeerConferenceListener(this);
		callConference.addPropertyChangeListener(this);
		for (Call c : callConference.getCalls()) {
			addListeners(c);
		}
	}

	private void addListeners(Call call) {
		OperationSetVideoTelephony videoTelephony = call.getProtocolProvider()
				.getOperationSet(OperationSetVideoTelephony.class);

		if (videoTelephony != null) {
			videoTelephony.addPropertyChangeListener(call, this);
		}

		Iterator<? extends CallPeer> callPeerIter = call.getCallPeers();

		while (callPeerIter.hasNext()) {
			addListeners(callPeerIter.next());
		}
	}
	
	private void addVideoListener(CallPeer callPeer) {
		OperationSetVideoTelephony videoTelephony = callPeer
				.getProtocolProvider().getOperationSet(
						OperationSetVideoTelephony.class);

		if (videoTelephony != null) {
			videoTelephony.addVideoListener(callPeer, this);
		}
	}

	private void addListeners(CallPeer callPeer) {
		addVideoListener(callPeer);
		callPeer.addCallPeerListener(this);
		for (ConferenceMember conferenceMember : callPeer
				.getConferenceMembers()) {
			addListeners(conferenceMember);
		}
	}

	private void addListeners(ConferenceMember conferenceMember) {
		conferenceMember.addPropertyChangeListener(this);
	}

	@Override
	public void callPeerAdded(CallPeerEvent ev) {
		onCallPeerEvent(ev);
	}

	@Override
	public void callPeerRemoved(CallPeerEvent ev) {
		onCallPeerEvent(ev);
	}

	public void dispose() {
		CallConference callConference = m_call.getCall()
				.getConference();
		callConference.removeCallChangeListener(this);
		callConference.removeCallPeerConferenceListener(this);
		callConference.removePropertyChangeListener(this);
		for (Call call : callConference.getCalls()) {
			removeListeners(call);
		}
	}

	@Override
	protected void onCallPeerConferenceEvent(CallPeerConferenceEvent ev) {
		switch (ev.getEventID()) {
		case CallPeerConferenceEvent.CONFERENCE_MEMBER_ADDED:
			addListeners(ev.getConferenceMember());
			break;
		case CallPeerConferenceEvent.CONFERENCE_MEMBER_REMOVED:
			removeListeners(ev.getConferenceMember());
			break;
		}
	}


	private void onCallPeerEvent(CallPeerEvent ev) {
		switch (ev.getEventID()) {
		case CallPeerEvent.CALL_PEER_ADDED:
			addListeners(ev.getSourceCallPeer());
			break;
		case CallPeerEvent.CALL_PEER_REMOVED:
			removeListeners(ev.getSourceCallPeer());
			break;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		String propertyName = ev.getPropertyName();
		if (CallConference.CALLS.equals(propertyName)) {
			if (ev.getSource() instanceof CallConference) {
				Object oldValue = ev.getOldValue();
				if (oldValue instanceof Call) {
					removeListeners((Call) oldValue);
				}
				Object newValue = ev.getNewValue();
				if (newValue instanceof Call) {
					addListeners((Call) newValue);
				}
			}
		} else if (OperationSetVideoTelephony.LOCAL_VIDEO_STREAMING.equals(propertyName) && m_user.getConnectedCall() != null) {
			m_user.notify(new UniteVideoEvent(m_user.getConnectedCall().getCall(), 
					UniteVideoEventType.LOCAL_VIDEO_STREAMING));
		}
	}
    
	private void removeListeners(Call call) {
		OperationSetVideoTelephony videoTelephony = call.getProtocolProvider()
				.getOperationSet(OperationSetVideoTelephony.class);

		if (videoTelephony != null) {
			videoTelephony.addPropertyChangeListener(call, this);
		}
		Iterator<? extends CallPeer> callPeerIter = call.getCallPeers();
		while (callPeerIter.hasNext()) {
			removeListeners(callPeerIter.next());
		}
	}
	
	private void removeVideoListener(CallPeer callPeer) {
		OperationSetVideoTelephony videoTelephony = callPeer
				.getProtocolProvider().getOperationSet(
						OperationSetVideoTelephony.class);

		if (videoTelephony != null) {
			videoTelephony.removeVideoListener(callPeer, this);
		}
	}

	private void removeListeners(CallPeer callPeer) {
		removeVideoListener(callPeer);
        callPeer.removeCallPeerListener(this);
		for (ConferenceMember conferenceMember : callPeer
				.getConferenceMembers()) {
			removeListeners(conferenceMember);
		}
	}


	private void removeListeners(ConferenceMember conferenceMember) {
		conferenceMember.removePropertyChangeListener(this);
	}

	@Override
	public void videoAdded(VideoEvent ev) {
		Component visualComponent = ev.getVisualComponent();
		if (visualComponent != null) {
			switch (ev.getOrigin()) {
				case VideoEvent.REMOTE:
					m_user.notify(
							new UniteVideoEvent(m_user.getConnectedCall().getCall(),
									UniteVideoEventType.REMOTE_SCREEN_ADDED));
					if (dsListener != null) {
						dsListener.setVideoComponent(visualComponent);
					}
					break;
				case VideoEvent.LOCAL:
					m_user.notify(
							new UniteVideoEvent(m_user.getConnectedCall().getCall(),
									UniteVideoEventType.LOCAL_SCREEN_ADDED));
					break;
			}
		}
		
		System.out.println("added " + visualComponent.isDisplayable() + " " + visualComponent.isBackgroundSet() + " " + 
				VideoEvent.typeToString(ev.getType()) + " ** " + VideoEvent.originToString(ev.getOrigin()) + 
				" *** " + ev.getVisualComponent().getHeight() + " *** " + visualComponent.getWidth());
	}

    @Override
	public void videoRemoved(VideoEvent ev) {
    	Component visualComponent = ev.getVisualComponent();
		if (visualComponent != null) {
			switch (ev.getOrigin()) {
				case VideoEvent.REMOTE:
					m_user.notify(
							new UniteVideoEvent(m_user.getConnectedCall().getCall(),
									UniteVideoEventType.REMOTE_SCREEN_REMOVED));
					break;
				case VideoEvent.LOCAL:
					m_user.notify(
							new UniteVideoEvent(m_user.getConnectedCall().getCall(),
									UniteVideoEventType.LOCAL_SCREEN_REMOVED));
					break;
			}
		}

		System.out.println("removed " + 
				VideoEvent.typeToString(ev.getType()) + " ** " + VideoEvent.originToString(ev.getOrigin()));
	}

    @Override
	public void videoUpdate(VideoEvent ev) {
	}

	@Override
	public void callStateChanged(CallChangeEvent evt) {

	}

	@Override
	public void peerAddressChanged(CallPeerChangeEvent arg0) {
		
	}

	@Override
	public void peerDisplayNameChanged(CallPeerChangeEvent arg0) {
		
	}

	@Override
	public void peerImageChanged(CallPeerChangeEvent arg0) {
		
	}

	@Override
	public void peerStateChanged(CallPeerChangeEvent evt) {
		Call call = evt.getSourceCallPeer().getCall();
		UniteCall uniteCall = null;
		if (call == null) {
			//TODO debug logging 
			return;
		} else {
			uniteCall = m_user.getUniteCall(call.getCallID());
			if (uniteCall == null) {
				//TODO debug logging
				return;
			}
		}
		CallPeerState oldState = (CallPeerState)evt.getOldValue();
		boolean previouslyOnHold = oldState.getStateString().equals(CallPeerState._ON_HOLD_LOCALLY) ||
				oldState.getStateString().equals(CallPeerState._ON_HOLD_MUTUALLY) ||
				oldState.getStateString().equals(CallPeerState._ON_HOLD_REMOTELY);
		if (evt.getSourceCallPeer().getState().getStateString().equals(CallPeerState._CONNECTED)) {
			uniteCall.setConnected(true);
			uniteCall.setOnHold(false);
			if (dsListener == null) {
				dsListener = new DSMouseAndKeyboardListener(evt.getSourceCallPeer());
			}
			switch (m_call.getUniteCallType()) {
			    case INCOMING:
				    m_user.notify(
				    		new UniteCallEvent(evt.getSourceCallPeer().getCall(), UniteCallEventType.INCOMING_CALL_IN_PROGRESS, previouslyOnHold, uniteCall.isVideo(), false));
				    break;
			    case OUTGOING:
				    m_user.notify(
				    		new UniteCallEvent(evt.getSourceCallPeer().getCall(), UniteCallEventType.OUTGOING_CALL_IN_PROGRESS, previouslyOnHold, uniteCall.isVideo(), false));
				    break;
			}
		} else if (evt.getSourceCallPeer().getState().getStateString().equals(CallPeerState._ON_HOLD_LOCALLY)) {
			uniteCall.setConnected(false);
			uniteCall.setOnHold(true);
			m_user.notify(
					new UniteCallEvent(evt.getSourceCallPeer().getCall(), UniteCallEventType.LOCALLY_ON_HOLD, previouslyOnHold, uniteCall.isVideo()));
		} else if (evt.getSourceCallPeer().getState().getStateString().equals(CallPeerState._ON_HOLD_REMOTELY)) {
			uniteCall.setConnected(false);
			uniteCall.setOnHold(true);
			m_user.notify(
					new UniteCallEvent(evt.getSourceCallPeer().getCall(), UniteCallEventType.REMOTELY_ON_HOLD, previouslyOnHold, uniteCall.isVideo()));
		} else if (evt.getSourceCallPeer().getState().getStateString().equals(CallPeerState._ON_HOLD_MUTUALLY)) {
			uniteCall.setConnected(false);
			uniteCall.setOnHold(true);
			m_user.notify(
					new UniteCallEvent(evt.getSourceCallPeer().getCall(), UniteCallEventType.MUTUALY_ON_HOLD, previouslyOnHold, uniteCall.isVideo()));
		} else {
			uniteCall.setConnected(false);
			uniteCall.setOnHold(false);
		}
	}

	@Override
	public void peerTransportAddressChanged(CallPeerChangeEvent arg0) {
		
	}
}
