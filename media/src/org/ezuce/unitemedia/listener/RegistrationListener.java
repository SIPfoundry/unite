package org.ezuce.unitemedia.listener;

import org.ezuce.unitemedia.event.UniteRegistrationEvent;
import org.ezuce.unitemedia.event.UniteRegistrationEventType;
import org.ezuce.unitemedia.phone.User;

import net.java.sip.communicator.service.protocol.OperationSetBasicTelephony;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.RegistrationState;
import net.java.sip.communicator.service.protocol.event.CallListener;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeEvent;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeListener;

public class RegistrationListener implements RegistrationStateChangeListener {
	private User m_user;
	private ProtocolProviderService m_provider;
	private CallListener m_callListener;
	
	public RegistrationListener(User user, ProtocolProviderService provider) {
		m_user = user;
		m_provider = provider;
		m_callListener = new CallListenerImpl(m_user);
	}

	@Override
	public void registrationStateChanged(RegistrationStateChangeEvent evt) {
		OperationSetBasicTelephony<?> telephonyOpSet = m_provider.getOperationSet(OperationSetBasicTelephony.class);
		if (evt.getNewState() == RegistrationState.REGISTERED) {
			m_user.notify(
					new UniteRegistrationEvent(m_user.getUserName(), UniteRegistrationEventType.REGISTERED));
			telephonyOpSet.addCallListener(m_callListener);
		} else if (evt.getNewState() == RegistrationState.UNREGISTERED 
				|| evt.getNewState() == RegistrationState.CONNECTION_FAILED
				|| evt.getNewState() == RegistrationState.UNREGISTERING) {
			m_user.notify(
					new UniteRegistrationEvent(m_user.getUserName(), UniteRegistrationEventType.UNREGISTERED));
			telephonyOpSet.removeCallListener(m_callListener);
		}
	}	
}
