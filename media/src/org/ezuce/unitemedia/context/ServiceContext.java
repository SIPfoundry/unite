package org.ezuce.unitemedia.context;

import java.io.File;

import org.ezuce.unitemedia.impl.IQColibriServiceImpl;
import org.ezuce.unitemedia.impl.UniteHIDServiceImpl;
import org.ezuce.unitemedia.service.IQColibriService;
import org.ezuce.unitemedia.service.UniteLibJitsi;
import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.resources.ResourceManagementService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import net.java.sip.communicator.impl.certificate.CertificateVerificationActivator;
import net.java.sip.communicator.impl.credentialsstorage.CredentialsStorageActivator;
import net.java.sip.communicator.impl.netaddr.NetaddrActivator;
import net.java.sip.communicator.impl.netaddr.NetworkAddressManagerServiceImpl;
import net.java.sip.communicator.impl.packetlogging.PacketLoggingActivator;
import net.java.sip.communicator.impl.protocol.sip.SipActivator;
import net.java.sip.communicator.impl.version.VersionActivator;
import net.java.sip.communicator.service.hid.HIDService;
import net.java.sip.communicator.service.netaddr.NetworkAddressManagerService;
import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.AccountManager;
import net.java.sip.communicator.service.protocol.ProtocolNames;
import net.java.sip.communicator.service.protocol.ProtocolProviderActivator;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.media.ProtocolMediaActivator;
import net.java.sip.communicator.util.UtilActivator;

public final class ServiceContext {
    /**
     * The name of the property that stores our home dir location.
     */
    public static final String PNAME_SC_HOME_DIR_LOCATION =
            "net.java.sip.communicator.SC_HOME_DIR_LOCATION";

    /**
     * The name of the property that stores our home dir name.
     */
    public static final String PNAME_SC_HOME_DIR_NAME =
            "net.java.sip.communicator.SC_HOME_DIR_NAME";	

    /**
     * The currently active name.
     */
    private static String overridableDirName = "Unite";
    
	private BundleContext ctx;
    
    private static ServiceContext m_context;
    
    //private constructor, do not instantiate this class
    private ServiceContext() {
    	
    }
    
    public static synchronized void start() throws Exception {
    	if (m_context == null) {
    		m_context = new ServiceContext();
    	}
    	m_context.init();
    }
    	
	private void init() throws Exception {
        String osName = System.getProperty("os.name");

        setSystemProperties(osName);

 
        setScHomeDir(osName);

        // this needs to be set before any DNS lookup is run
        File f = new File(System.getProperty(PNAME_SC_HOME_DIR_LOCATION),
            System.getProperty(PNAME_SC_HOME_DIR_NAME)
            + File.separator + ".usednsjava");
        if(f.exists()) {
            System.setProperty(
                "sun.net.spi.nameservice.provider.1", "dns,dnsjava");
        }
        
		createBundleContext();
		
		new ProtocolProviderActivator().start(ctx);
		new UtilActivator().start(ctx);
		new PacketLoggingActivator().start(ctx);
		new NetaddrActivator().start(ctx);
		new CredentialsStorageActivator().start(ctx);
		new SipActivator().start(ctx);
		new ProtocolMediaActivator().start(ctx);
		new CertificateVerificationActivator().start(ctx);
		new VersionActivator().start(ctx);	
	}
	
	private void createBundleContext() {
		UniteLibJitsi.start();
		ctx = new UniteBundleContext();
		ctx.registerService(ConfigurationService.class, null, null);
		ctx.registerService(FileAccessService.class, null, null);
		ctx.registerService(HIDService.class, new UniteHIDServiceImpl(), null);
		ctx.registerService(AccountManager.class, new AccountManager(ctx), null);
		ctx.registerService(ResourceManagementService.class, null, null);
		ctx.registerService(NetworkAddressManagerService.class.getName(), new NetworkAddressManagerServiceImpl(), null);
		ctx.registerService(MediaService.class, null, null);
		try {
			ctx.registerService(IQColibriService.class, new IQColibriServiceImpl() , null);
		} catch (Exception e) {
			System.out.println("Cannot initialize colibri service");
		}
	}
	
    public static ProtocolProviderFactory getProtocolProviderFactory() {
    	return m_context.getFactory();
    }
	
	@SuppressWarnings("unchecked")
	public static Object getService(@SuppressWarnings("rawtypes") ServiceReference sr) {
		return m_context.getCtx().getService(sr);
	}
	
	public static MediaService getMediaService() {
		return UniteLibJitsi.getMediaService();
	}
	
	public static ConfigurationService getConfigurationService() {
		return UniteLibJitsi.getConfigurationService();
	}
	
	public static AudioNotifierService getAudioNotifierService() {
		return UniteLibJitsi.getAudioNotifierService();
	}
	
	public static AccountManager getAccountManager() {
		return (AccountManager)getService(m_context.getCtx().getServiceReference(AccountManager.class));
	}
	
	public static ProtocolProviderService getProtocolProviderService(AccountID accId) {
		ServiceReference sr = getProtocolProviderFactory().getProviderForAccount(accId);		
		return (ProtocolProviderService) ServiceContext.getService(sr);
	}	
	
	public static IQColibriService getIQColibriService() {
		return (IQColibriService)getService(m_context.getCtx().getServiceReference(IQColibriService.class));
	}
	
    /**
     * Sets some system properties specific to the OS that needs to be set at
     * the very beginning of a program (typically for UI related properties,
     * before AWT is launched).
     *
     * @param osName OS name
     */
    private void setSystemProperties(String osName) {
        // setup here all system properties that need to be initialized at
        // the very beginning of an application
        if(osName.startsWith("Windows")) {
            // disable Direct 3D pipeline (used for fullscreen) before
            // displaying anything (frame, ...)
            System.setProperty("sun.java2d.d3d", "false");
        }
        else if(osName.startsWith("Mac")) {
            // On Mac OS X when switch in fullscreen, all the monitors goes
            // fullscreen (turns black) and only one monitors has images
            // displayed. So disable this behavior because somebody may want
            // to use one monitor to do other stuff while having other ones with
            // fullscreen stuff.
            System.setProperty("apple.awt.fullscreencapturealldisplays",
                "false");
        }
    }
    
    private void setScHomeDir(String osName) {
        /*
         * Though we'll be setting the SC_HOME_DIR_* property values depending
         * on the OS running the application, we have to make sure we are
         * compatible with earlier releases i.e. use
         * ${user.home}/.sip-communicator if it exists (and the new path isn't
         * already in use).
         */
        String location = System.getProperty(PNAME_SC_HOME_DIR_LOCATION);
        String name = System.getProperty(PNAME_SC_HOME_DIR_NAME);

        boolean isHomeDirnameForced = name != null;

        if ((location == null) || (name == null)) {
            String defaultLocation = System.getProperty("user.home") + File.separator + ".eZuce";
            String defaultName = "Unite";

            // Whether we should check legacy names
            // 1) when such name is not forced we check
            // 2) if such is forced and is the overridableDirName check it
            //      (the later is the case with name transition SIP Communicator
            //      -> Jitsi, check them only for Jitsi)
            boolean chekLegacyDirNames = (name == null) ||
                name.equals(overridableDirName);

            if (osName.startsWith("Mac")) {
                if (location == null)
                    location =
                            System.getProperty("user.home") + File.separator
                            + "Library" + File.separator
                            + "Application Support";
                if (name == null)
                    name = "Unite";
            }
            else if (osName.startsWith("Windows")) {
                /*
                 * Primarily important on Vista because Windows Explorer opens
                 * in %USERPROFILE% so .sip-communicator is always visible. But
                 * it may be a good idea to follow the OS recommendations and
                 * use APPDATA on pre-Vista systems as well.
                 */
                if (location == null)
                    location = System.getenv("APPDATA");
                if (name == null)
                    name = "Unite";
            }

            /* If there're no OS specifics, use the defaults. */
            if (location == null) {
                location = defaultLocation;
            }

            if (name == null) {
                name = defaultName;
            }

            /*
             * As it was noted earlier, make sure we're compatible with previous
             * releases. If the home dir name is forced (set as system property)
             * doesn't look for the default dir.
             */
            if (!isHomeDirnameForced
                && (new File(location, name).isDirectory() == false)
                && new File(defaultLocation, defaultName).isDirectory()) {
                location = defaultLocation;
                name = defaultName;
            }
            System.setProperty(PNAME_SC_HOME_DIR_LOCATION, location);
            System.setProperty(PNAME_SC_HOME_DIR_NAME, name);
            new File(location, name + File.separator + "log").mkdirs();
        }
    }

	private ProtocolProviderFactory getFactory() {
		try {
			ServiceReference[] refs = m_context.getCtx().getServiceReferences(
					ProtocolProviderFactory.class.getName(), "(" + ProtocolProviderFactory.PROTOCOL + "=" +ProtocolNames.SIP + ")");
			ProtocolProviderFactory sipFactory = (ProtocolProviderFactory)getService(refs[0]);
			return sipFactory;
		} catch (InvalidSyntaxException e) {
			System.out.println("Cannot retrieve protocol factory");
			return null;
		}
	}

	private BundleContext getCtx() {
		return ctx;
	}
}
