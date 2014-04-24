package org.ezuce.unitemedia.service;

import java.lang.reflect.Constructor;

import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.resources.ResourceManagementService;
import org.jitsi.util.Logger;

public abstract class UniteLibJitsi extends LibJitsi implements IUniteLibJitsi{
    /**
     * The <tt>Logger</tt> used by the <tt>LibJitsi</tt> class for logging
     * output.
     */
    private static final Logger logger = Logger.getLogger(UniteLibJitsi.class);

    /**
     * The <tt>LibJitsi</tt> instance which is provides the implementation of
     * the <tt>getXXXService</tt> methods.
     */
    private static IUniteLibJitsi impl;
    
    /**
     * Starts/initializes the use of the <tt>libjitsi</tt> library.
     */
    public static void start() {
    	LibJitsi.start();
        start(null);
    }

	private static void start(Object context) {
		String implBaseClassName = UniteLibJitsi.class.getName().replace(
				".service.", ".impl.");
		IUniteLibJitsi impl = null;
		Class<?> implClass = null;
		String implClassName = implBaseClassName + "Impl";
		try {
			implClass = Class.forName(implClassName);
		} catch (ClassNotFoundException cnfe) {
			logger.error("cannot initalize library "+cnfe.getMessage());
		} catch (ExceptionInInitializerError eiie) {
			logger.error("cannot initalize library "+eiie.getMessage());
		} catch (LinkageError le) {
			logger.error("cannot initalize library "+le.getMessage());
		}
		if ((implClass != null) && IUniteLibJitsi.class.isAssignableFrom(implClass)) {
			try {
				if (context == null) {
					impl = (IUniteLibJitsi) implClass.newInstance();
				} else {
					/*
					 * Try to find a Constructor which will accept the specified
					 * context.
					 */
					Constructor<?> constructor = null;

					for (Constructor<?> aConstructor : implClass
							.getConstructors()) {
						Class<?>[] parameterTypes = aConstructor
								.getParameterTypes();

						if ((parameterTypes.length == 1)
								&& parameterTypes[0].isInstance(context)) {
							constructor = aConstructor;
							break;
						}
					}

					impl = (UniteLibJitsi) constructor.newInstance(context);
				}
			} catch (Throwable t) {
				if (t instanceof ThreadDeath) {
					throw (ThreadDeath) t;
				}
			}
		}

		if (impl == null) {
			throw new IllegalStateException("impl");
		}
		else {
			UniteLibJitsi.impl = impl;
			logger.info("UniteLibJitsi is initialized");
		}
	}
	
    /**
     * Invokes {@link #getService(Class)} on {@link #impl}.
     *
     * @param serviceClass the class of the service to be retrieved
     * @return a service of the specified type if such a service is associated
     * with the library
     * @throws IllegalStateException if the library is not currently initialized
     */
    private static <T> T invokeGetServiceOnUniteImpl(Class<T> serviceClass) {
        IUniteLibJitsi impl = UniteLibJitsi.impl;

        if (impl == null)
            throw new IllegalStateException("impl");
        else
            return impl.getUniteService(serviceClass);
    }
    
    public static ResourceManagementService getUniteManagementService() {
        return invokeGetServiceOnUniteImpl(UniteResourceManagementService.class);
    }
}
