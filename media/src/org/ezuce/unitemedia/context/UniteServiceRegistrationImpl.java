package org.ezuce.unitemedia.context;

import java.util.Dictionary;

import org.osgi.framework.*;

public class UniteServiceRegistrationImpl<S> implements ServiceRegistration<S>{
	ServiceReference<S> m_reference;
	public UniteServiceRegistrationImpl(ServiceReference<S> reference) {
		m_reference = reference;
	}
	@Override
	public ServiceReference<S> getReference() {		
		return m_reference;
	}

	@Override
	public void setProperties(Dictionary<String, ?> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregister() {
		// TODO Auto-generated method stub
		
	}

}
