package org.ezuce.unitemedia.impl;

import org.ezuce.unitemedia.service.IUniteLibJitsi;
import org.jitsi.impl.libjitsi.LibJitsiImpl;

public class UniteLibJitsiImpl extends LibJitsiImpl implements IUniteLibJitsi {

	@Override
	public <T> T getUniteService(Class<T> serviceClass) {
		return getService(serviceClass);
	}
}
