package org.ezuce.unitemedia.context;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ezuce.unitemedia.service.UniteLibJitsi;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.resources.ResourceManagementService;
import org.jitsi.util.StringUtils;
import org.osgi.framework.*;

public class UniteBundleContext implements BundleContext {
	Map<RefKey, ServiceReference<?>> m_references = new HashMap<RefKey, ServiceReference<?>>();
	private final Map<ServiceReference<?>, Object> m_osgiServices = new HashMap<ServiceReference<?>, Object>();
	
	@Override
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle installBundle(String location, InputStream input)
			throws BundleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle installBundle(String location) throws BundleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle[] getBundles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addServiceListener(ServiceListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeServiceListener(ServiceListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBundleListener(BundleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBundleListener(BundleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFrameworkListener(FrameworkListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFrameworkListener(FrameworkListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceRegistration<?> registerService(String[] clazzes,
			Object service, Dictionary<String, ?> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceRegistration<?> registerService(String clazz, Object service,
			Dictionary<String, ?> properties) {
		StringBuffer filter = null;
		if (properties != null) {
			filter = new StringBuffer();
			String key = null;
			Enumeration<String> keys = properties.keys();
			while(keys.hasMoreElements()) {
				key = keys.nextElement();
				filter.append("(" + key + "=" + properties.get(key) + ")");
			}
		}
		RefKey refKey = new RefKey(clazz, filter != null ? filter.toString() : null);
		ServiceReference<?> ref = new UniteServiceReferenceImpl<String>(clazz, filter != null ?filter.toString() : null);
		m_references.put(refKey, ref);
		if (service != null) {
			m_osgiServices.put(ref, service);
		}
		return new UniteServiceRegistrationImpl(ref);
	}

	@Override
	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			S service, Dictionary<String, ?> properties) {
		StringBuffer filter = null;
		if (properties != null) {
			filter = new StringBuffer();
			String key = null;
			Enumeration<String> keys = properties.keys();
			while(keys.hasMoreElements()) {
				key = keys.nextElement();
				filter.append(key + "=" + properties.get(key));
			}
		}
		RefKey refKey = new RefKey(clazz.getName(), filter != null ?filter.toString() : null);
		ServiceReference<?> ref = new UniteServiceReferenceImpl<String>(clazz.getName(), filter != null ?filter.toString() : null);
		m_references.put(refKey, ref);
		if (service != null) {
			m_osgiServices.put(ref, service);
		}
		return new UniteServiceRegistrationImpl(ref);
	}

	@Override
	public ServiceReference<?>[] getServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		Set<RefKey> refKeys = m_references.keySet();
		Set<ServiceReference> returnedRefs = new TreeSet<ServiceReference>(); 
		//If filter is null, we return all registered service references with the given class name
		if (filter == null) {
			for (RefKey refKey : refKeys) {
				if (StringUtils.isEquals(refKey.getName(), clazz)) {
					returnedRefs.add(m_references.get(refKey));
				}
			}
			return returnedRefs.toArray(new ServiceReference[returnedRefs.size()]);
		} else {
			for (RefKey refKey : refKeys) {
				if (StringUtils.isEquals(refKey.getName(), clazz) && StringUtils.isEquals(refKey.getFilter(), filter)) {
					returnedRefs.add(m_references.get(refKey));
				}
			}
			return returnedRefs.toArray(new ServiceReference[returnedRefs.size()]);	
		}
	}

	@Override
	public ServiceReference<?>[] getAllServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceReference<?> getServiceReference(String clazz) {
		return m_references.get(new RefKey(clazz, null));
	}

	@Override
	public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		// TODO Auto-generated method stub
		return (ServiceReference<S>)m_references.get(new RefKey(clazz.getName(), null));
	}

	@Override
	public <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		Collection<ServiceReference<S>> list = new ArrayList<ServiceReference<S>>();
		ServiceReference<S> serviceRef = (ServiceReference<S>) m_references.get(new RefKey(clazz.getName(), filter));
		list.add(serviceRef);
		return list;
	}

	@Override
	public <S> S getService(ServiceReference<S> reference) {
		// TODO Auto-generated method stub
		UniteServiceReferenceImpl<String> ref = (UniteServiceReferenceImpl<String>)reference;
		String className = ref.getReferencedClassName();
		S service = (S)m_osgiServices.get(ref);
		if (service != null) {
			return service;
		}
		if (className.equals(ConfigurationService.class.getName())) {
			return (S)UniteLibJitsi.getConfigurationService();
		} else if (className.equals(FileAccessService.class.getName())) {
			return (S)UniteLibJitsi.getFileAccessService();
		} else if (className.equals(MediaService.class.getName())) {
			return (S)UniteLibJitsi.getMediaService();
		} else if(className.equals(ResourceManagementService.class.getName())) {
			return (S)UniteLibJitsi.getUniteManagementService();
		}
		return null;
	}

	@Override
	public boolean ungetService(ServiceReference<?> reference) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getDataFile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filter createFilter(String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle(String location) {
		// TODO Auto-generated method stub
		return null;
	}

}