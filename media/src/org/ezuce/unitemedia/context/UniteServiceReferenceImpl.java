package org.ezuce.unitemedia.context;

import org.jitsi.util.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class UniteServiceReferenceImpl<S> implements ServiceReference {
	String m_referencedClassName;
	String m_filter;
	
	public UniteServiceReferenceImpl(String referencedClassName, String filter) {
		m_referencedClassName = referencedClassName;
		m_filter = filter;
	}		
	
	public String getReferencedClassName() {
		return m_referencedClassName;
	}

	public String getFilter() {
		return m_filter;
	}
	
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + m_referencedClassName.hashCode();
        hash = hash * 31 + (m_filter == null ? 0 : m_filter.hashCode());
        return hash;
    }
	
	@Override
	public boolean equals(Object ref) {
		return StringUtils.isEquals(m_filter, ((UniteServiceReferenceImpl<S>)ref).getFilter()) && 
				StringUtils.isEquals(m_referencedClassName, ((UniteServiceReferenceImpl<S>)ref).getReferencedClassName());
	}

	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getPropertyKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public Bundle getBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	public Bundle[] getUsingBundles() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAssignableTo(Bundle bundle, String className) {
		// TODO Auto-generated method stub
		return true;
	}

	public int compareTo(Object reference) {
		// TODO Auto-generated method stub
		return 0;
	}

}
