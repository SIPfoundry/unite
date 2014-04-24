package org.ezuce.unitemedia.phone;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.CallPeer;
import net.java.sip.communicator.service.protocol.OperationFailedException;
import net.java.sip.communicator.service.protocol.OperationSetAdvancedTelephony;
import net.java.sip.communicator.service.protocol.OperationSetBasicTelephony;
import net.java.sip.communicator.service.protocol.OperationSetDesktopSharingServer;
import net.java.sip.communicator.service.protocol.OperationSetVideoTelephony;
import net.java.sip.communicator.service.protocol.ProtocolProviderActivator;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeListener;
import net.java.sip.communicator.service.protocol.media.MediaAwareCall;
import net.java.sip.communicator.service.protocol.media.MediaAwareCallPeer;
import net.java.sip.communicator.service.protocol.media.ProtocolMediaActivator;

import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.listener.KeyEventDispatcherImpl;
import org.ezuce.unitemedia.listener.RegistrationListener;
import org.ezuce.unitemedia.security.DefaultSecurityAuthority;
import org.jitsi.impl.neomedia.codec.video.h264.JNIEncoder;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.neomedia.Recorder;
import org.jitsi.service.neomedia.control.KeyFrameControl;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.util.SoundFileUtils;
import org.jitsi.util.StringUtils;
import org.osgi.framework.ServiceReference;

public class Phone {
    private KeyEventDispatcherImpl m_keyDispatcher;
    private RegistrationStateChangeListener m_regStateChangeListener;
    private Recorder m_recorder;
	
	public boolean loadAccount(User user) {
		AccountID accId = getAccount(user);
		if (accId != null) {
			System.out.println("Account properties: " + accId.getAccountProperties());
			user.setAccountId(accId);
			//set encoding defaults if not already set
			if (!accId.getAccountPropertyBoolean(EncodingDefaults.OVERRIDE_ENCODINGS, false)) {
				System.out.println("Writing codec default settings...");
				List<String> list = new ArrayList<String>();
				list.add(EncodingDefaults.KEY_SILK_12000);
				list.add(EncodingDefaults.KEY_SILK_8000);
				list.add(EncodingDefaults.KEY_VP8_90000);
				list.add(EncodingDefaults.KEY_H263_1998_90000);
				overrideEncodings(user, list);
				if (StringUtils.isEquals(getH264KeyFrameRequester(), KeyFrameControl.KeyFrameRequester.RTCP)) {
					setH264KeyFrameRequester(KeyFrameControl.KeyFrameRequester.SIGNALING);
					saveConfiguration();
				}
			}
			return true;
		}
		return false;
	}
	
    /**
     * Returns the package name of the <tt>factory</tt>.
     * @param factory the factory which package will be returned.
     * @return the package name of the <tt>factory</tt>.
     */
    private String getFactoryImplPackageName(ProtocolProviderFactory factory) {
        String className = factory.getClass().getName();
        return className.substring(0, className.lastIndexOf('.'));
    }
	
	private AccountID getAccount(User user) {
        ConfigurationService configService = ServiceContext.getConfigurationService();
        AccountID accId = null;
        ProtocolProviderFactory protocolProviderFactory = ServiceContext.getProtocolProviderFactory();
        String factoryPackage = getFactoryImplPackageName(protocolProviderFactory);
        List<String> accounts = configService.getPropertyNamesByPrefix(factoryPackage, true);
        //account associated to user not created yet - create new and store it
        boolean storeNew = true;
        String configAccountPropKey = null;
        if (!(accounts == null || accounts.isEmpty())) {
        	for (String account : accounts) {
        		String userId = configService.getString(account + "." + ProtocolProviderFactory.USER_ID);
        		System.out.println("Stored user id: " + userId);
        		if (StringUtils.isEquals(userId, user.getUserName())) {
        			storeNew = false;
                	accId = getNewAccount(user);
        			configAccountPropKey = account;
        		}
        	}
        }
        if (storeNew) {
        	accId = getNewAccount(user);
        	System.out.println("New account to store: " + accId.getAccountUniqueID());
        	try {
				ServiceContext.getAccountManager().storeAccount(protocolProviderFactory, accId);
			} catch (OperationFailedException e) {
				System.out.println("Cannot store new Account");
			}
        	return accId;
        }
        
        //load all properties for the stored account        
        List<String> storedAccountProperties =
                configService.getPropertyNamesByPrefix(configAccountPropKey, false);
        String accountPropKey = null;
        Object accountPropValue = null;
        for (String property : storedAccountProperties) {
            //strip the account prefix to get account specific keys (example: Encodings.GSM/8000=13 
        	//Here the key is Encodings.GSM/8000 and the value is 13
        	accountPropKey = property.substring(configAccountPropKey.length() + 1);
        	accountPropValue = configService.getProperty(property);
        	accId.putAccountProperty(accountPropKey, accountPropValue);
        }
        
        return accId;
	}
	
	private Map<String, String> getAccountProps(User user) {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ProtocolProviderFactory.SERVER_ADDRESS, user.getServer());
		props.put(ProtocolProviderFactory.SERVER_PORT, user.getPort());
		props.put(ProtocolProviderFactory.USER_ID, user.getUserName());
		return props;
	}
	
	private AccountID getNewAccount(User user) {
		Map<String, String> props = getAccountProps(user);
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();		
		AccountID accId = factory.loadAccount(props);

		return accId;
	}
	
	
	/**
	 * This method enables/disables codecs. You need to unregister/register user to make it effective
	 * NOTE - the configuration gets persisted only when calling saveAccountProperties(User user) 
	 * @param user
	 * @param keysToBeDisabled - if empty codecs will be reset, meaning that all codecs will be active
	 */
	public void overrideEncodings(User user, Collection<String> keysToBeDisabled) {
		AccountID accId = user.getAccountId();
		if (accId == null) {
			System.out.println("Account is not loaded!");
			return;
		}		
		accId.putAccountProperty(EncodingDefaults.OVERRIDE_ENCODINGS, true);
		accId.putAccountProperty(EncodingDefaults.KEY_G722_8000, EncodingDefaults.G722_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_GSM_8000, EncodingDefaults.GSM_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_H263_1998_90000, EncodingDefaults.H263_1998_90000);
		accId.putAccountProperty(EncodingDefaults.KEY_H264_90000, EncodingDefaults.H264_90000);
		accId.putAccountProperty(EncodingDefaults.KEY_iLBC_8000, EncodingDefaults.iLBC_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_opus_48000, EncodingDefaults.opus_48000);
		accId.putAccountProperty(EncodingDefaults.KEY_PCMA_8000, EncodingDefaults.PCMA_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_PCMU_8000, EncodingDefaults.PCMU_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_SILK_12000, EncodingDefaults.SILK_12000);
		accId.putAccountProperty(EncodingDefaults.KEY_SILK_16000, EncodingDefaults.SILK_16000);
		accId.putAccountProperty(EncodingDefaults.KEY_SILK_24000, EncodingDefaults.SILK_24000);
		accId.putAccountProperty(EncodingDefaults.KEY_SILK_8000, EncodingDefaults.SILK_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_speex_16000, EncodingDefaults.speex_16000);
		accId.putAccountProperty(EncodingDefaults.KEY_speex_32000, EncodingDefaults.speex_32000);
		accId.putAccountProperty(EncodingDefaults.KEY_speex_8000, EncodingDefaults.speex_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_telephone_event_8000, EncodingDefaults.telephone_event_8000);
		accId.putAccountProperty(EncodingDefaults.KEY_VP8_90000, EncodingDefaults.VP8_90000);		
		for (String key : keysToBeDisabled) {
			if (key.equals(EncodingDefaults.KEY_G722_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_G722_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_GSM_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_GSM_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_H263_1998_90000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_H263_1998_90000, 0);
			} else if (key.equals(EncodingDefaults.KEY_H264_90000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_H264_90000, 0);
			} else if (key.equals(EncodingDefaults.KEY_iLBC_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_iLBC_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_opus_48000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_opus_48000, 0);
			} else if (key.equals(EncodingDefaults.KEY_PCMA_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_PCMA_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_PCMU_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_PCMU_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_SILK_12000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_SILK_12000, 0);
			} else if (key.equals(EncodingDefaults.KEY_SILK_16000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_SILK_16000, 0);
			} else if (key.equals(EncodingDefaults.KEY_SILK_24000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_SILK_24000, 0);
			} else if (key.equals(EncodingDefaults.KEY_SILK_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_SILK_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_speex_16000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_speex_16000, 0);
			} else if (key.equals(EncodingDefaults.KEY_speex_32000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_speex_32000, 0);
			} else if (key.equals(EncodingDefaults.KEY_speex_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_speex_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_telephone_event_8000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_telephone_event_8000, 0);
			} else if (key.equals(EncodingDefaults.KEY_VP8_90000)) {
				accId.putAccountProperty(EncodingDefaults.KEY_VP8_90000, 0);
			}
		}
		saveAccountProperties(user);

	}
	
	public boolean isEncodingActive(User user, String encodingKey) {
		AccountID accId = user.getAccountId();
		if (accId == null) {
			System.out.println("Account is not loaded!");
			return false;
		}

		boolean overrideEncodings = accId.getAccountPropertyBoolean("OVERRIDE_ENCODINGS", false);
		int keyValue = accId.getAccountPropertyInt(encodingKey, 1);
		return !overrideEncodings || keyValue > 0;
	}
	
	public void setH264KeyFrameRequester(String keyFrameRequester) {
		if (StringUtils.isEquals(keyFrameRequester, KeyFrameControl.KeyFrameRequester.SIGNALING)) {
			ServiceContext.getConfigurationService().setProperty(KeyFrameControl.KeyFrameRequester.PREFERRED_PNAME, 
				KeyFrameControl.KeyFrameRequester.SIGNALING);
		} else if (StringUtils.isEquals(keyFrameRequester, KeyFrameControl.KeyFrameRequester.RTCP)) {
			ServiceContext.getConfigurationService().setProperty(KeyFrameControl.KeyFrameRequester.PREFERRED_PNAME, 
					KeyFrameControl.KeyFrameRequester.RTCP);
		}
	}
	
	public String getH264KeyFrameRequester() {
		return ServiceContext.getConfigurationService().getString(KeyFrameControl.KeyFrameRequester.PREFERRED_PNAME, 
				KeyFrameControl.KeyFrameRequester.DEFAULT_PREFERRED);
	}
	
	public void setH264Profile(String profile) {
		if (StringUtils.isEquals(profile, JNIEncoder.HIGH_PROFILE)) {
			ServiceContext.getConfigurationService().setProperty(JNIEncoder.DEFAULT_PROFILE_PNAME, JNIEncoder.HIGH_PROFILE);
		} else if (StringUtils.isEquals(profile, JNIEncoder.BASELINE_PROFILE)) {
			ServiceContext.getConfigurationService().setProperty(JNIEncoder.DEFAULT_PROFILE_PNAME, JNIEncoder.BASELINE_PROFILE);
		} else if (StringUtils.isEquals(profile, JNIEncoder.MAIN_PROFILE)) {
			ServiceContext.getConfigurationService().setProperty(JNIEncoder.DEFAULT_PROFILE_PNAME, JNIEncoder.MAIN_PROFILE);
		}
	}
	
	public String getH264Profile() {
		return ServiceContext.getConfigurationService().getString(JNIEncoder.DEFAULT_PROFILE_PNAME, JNIEncoder.DEFAULT_DEFAULT_PROFILE);
	}
		
	public void setH264Preset(String preset) {		
		ServiceContext.getConfigurationService().setProperty(JNIEncoder.PRESET_PNAME, preset);
	}
	
	public String getH264Preset() {
		return ServiceContext.getConfigurationService().getString(JNIEncoder.PRESET_PNAME, JNIEncoder.DEFAULT_PRESET);
	}
	
	public void setH264DefaultIntraRefresh(boolean defaultIntraRefresh) {
		ServiceContext.getConfigurationService().setProperty(JNIEncoder.DEFAULT_INTRA_REFRESH_PNAME, defaultIntraRefresh);
	}
	
	public boolean getH264DefaultIntraRefresh() {
		return ServiceContext.getConfigurationService().getBoolean(JNIEncoder.DEFAULT_INTRA_REFRESH_PNAME, 
				JNIEncoder.DEFAULT_DEFAULT_INTRA_REFRESH);
	}
	
	public void setH264KeyInt(int keyInt) {
		ServiceContext.getConfigurationService().setProperty(JNIEncoder.KEYINT_PNAME, keyInt);
	}
	
	public int getH264KeyInt() {
		return ServiceContext.getConfigurationService().getInt(JNIEncoder.KEYINT_PNAME, JNIEncoder.DEFAULT_KEYINT);
	}
	
	public void saveAccountProperties(User user) {
		AccountID accId = user.getAccountId();
		if (accId == null) {
			System.out.println("Account is not loaded!");
			return;
		}

		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		try {
			ServiceContext.getAccountManager().storeAccount(factory, accId);
			System.out.println("Account properties after codecs rewrite " + accId.getAccountProperties());
		} catch (OperationFailedException e) {
			System.out.println("Cannot store account " + e.getMessage());
		}
	}
	
	public void saveConfiguration() {
		try {
			ServiceContext.getConfigurationService().storeConfiguration();
		} catch (IOException e) {
			System.out.println("Configuration cannot be saved" + e.getMessage());
			e.printStackTrace();
		}
	}
			
	public void registerUser(User user) {
		AccountID accId = user.getAccountId();
		if (user.getAccountId() == null) {
			System.out.println("The user is not loaded or installed");
			return;
		}		
		
		ProtocolProviderService provider = (ProtocolProviderService) ServiceContext.getProtocolProviderService(accId);
		try {
			DefaultSecurityAuthority authority = new DefaultSecurityAuthority(user.getUserName(), user.getPassword());
			provider.register(authority);
			registerKeyEventDispatcher(user);
			registerRegistrationStateChangeListener(user, provider);
		} catch (OperationFailedException e) {
			e.printStackTrace();
			System.out.println("Provider register failed: [" + e.getMessage() + "]");
		}
	}
	
	private void registerKeyEventDispatcher(User user) {
		if (m_keyDispatcher == null) {
			m_keyDispatcher = new KeyEventDispatcherImpl(user);
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(m_keyDispatcher);
		}
	}
	
	private void unregisterKeyEventDispatcher(User user) {
		if (m_keyDispatcher != null) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(m_keyDispatcher);
			m_keyDispatcher = null;
		}
	}
	
	private void registerRegistrationStateChangeListener(User user, ProtocolProviderService provider) {
		if (m_regStateChangeListener == null) {
			m_regStateChangeListener = new RegistrationListener(user, provider);
			provider.addRegistrationStateChangeListener(m_regStateChangeListener);
		}
	}
	
	private void unregisterRegistrationStateChangeListener(User user, ProtocolProviderService provider) {
		if (m_regStateChangeListener != null) {
			provider.removeRegistrationStateChangeListener(m_regStateChangeListener);
			m_regStateChangeListener = null;
		}
	}
	
	public void unregisterUser(User user) {
		AccountID accId = getUserAccountId(user.getUserName());
		if (accId == null || !user.getAccountId().equals(accId)) {
			System.out.println("The user: " + user.getUserName() + " is not registered");
			return;
		}
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		ServiceReference sr = factory.getProviderForAccount(accId);
		ProtocolProviderService provider = (ProtocolProviderService) ServiceContext.getService(sr);
		try {
			unregisterKeyEventDispatcher(user);
			provider.unregister();
		} catch (OperationFailedException e) {
			System.out.println("Unregistration failed: [" + e.getMessage() + "]");
		}
	}
	
	public void uninstallAccount(User user) {
		AccountID accId = user.getAccountId();
		if (accId == null) {
			System.out.println("The user: " + user.getUserName() + " is not installed");
		}
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		ServiceReference sr = factory.getProviderForAccount(accId);
		ProtocolProviderService provider = (ProtocolProviderService) ServiceContext.getService(sr);
		unregisterRegistrationStateChangeListener(user, provider);	
		factory.unloadAccount(accId);
		factory.uninstallAccount(accId);
		user.setAccountId(null);	
	}
		
	private AccountID getUserAccountId(String userId) {
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		ArrayList<AccountID> accounts = factory.getRegisteredAccounts();
		for (AccountID account : accounts) {
			if (StringUtils.isEquals(account.getUserID(), userId)) {
				return account;
			}
		}
		return null;
	}
	
	public boolean isUserRegistered(User user) {
		AccountID accId = user.getAccountId();
		if (user.getAccountId() == null) {
			System.out.println("The user is not loaded or installed, therefore is not registered");
			return false;
		}
		ProtocolProviderService provider = (ProtocolProviderService) ServiceContext.getProtocolProviderService(accId);
		System.out.println("Provider is REGISTERED " + provider.isRegistered());
		return provider.isRegistered();
	}
	
	public void makeCall(User user, String userToCall) {
		String stringContact = "sip:" + userToCall + "@" + user.getServer();
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		ServiceReference sr = factory.getProviderForAccount(getUserAccountId(user.getUserName()));
		ProtocolProviderService protocolProvider = (ProtocolProviderService) ServiceContext.getService(sr);
		OperationSetBasicTelephony<?> telephonyOpSet = protocolProvider
				.getOperationSet(OperationSetBasicTelephony.class);

		if (telephonyOpSet != null) {
			Throwable exception = null;
			try {
				if (stringContact != null) {
					System.out.println("Calling [" + stringContact + "]");				
					telephonyOpSet.createCall(stringContact);					
				}
			} catch (OperationFailedException e) {
				exception = e;
			} catch (ParseException e) {
				exception = e;
			}
			if (exception != null) {
				System.out.println("The call could not be created: " + exception.getMessage());
			}
		}
	}
	
	public void makeVideoCall(User user, String userToCall) {
		String stringContact = "sip:" + userToCall + "@" + user.getServer();
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		ServiceReference sr = factory
				.getProviderForAccount(getUserAccountId(user.getUserName()));
		ProtocolProviderService protocolProvider = (ProtocolProviderService) ServiceContext
				.getService(sr);
		OperationSetVideoTelephony telephony = protocolProvider
				.getOperationSet(OperationSetVideoTelephony.class);

		if (telephony != null) {
			Throwable exception = null;
			try {
				if (stringContact != null) {
					Call call = telephony.createVideoCall(stringContact);
				}
			} catch (OperationFailedException e) {
				exception = e;
			} catch (ParseException e) {
				exception = e;
			}
			if (exception != null) {
				System.out.println("The video call could not be created: " + exception.getMessage());
			}
		}
	}
	
	public void answerIncomingVideoCall(Call call) {
        ProtocolProviderService pps = call.getProtocolProvider();
        Iterator<? extends CallPeer> peers = call.getCallPeers();
        while (peers.hasNext()) {
            OperationSetVideoTelephony telephony = pps.getOperationSet(OperationSetVideoTelephony.class);
            CallPeer peer = peers.next();
            try {
                telephony.answerVideoCallPeer(peer);
            }
            catch (OperationFailedException ofe)
            {
                System.out.println(
                        "Could not answer "
                            + peer
                            + " with video"
                            + " because of the following exception: "
                            + ofe);
            }
        }
	}
	
	public void answerIncomingCall(Call call) {
		ProtocolProviderService pps = call.getProtocolProvider();
		Iterator<? extends CallPeer> peers = call.getCallPeers();
		OperationSetBasicTelephony<?> telephony = pps
				.getOperationSet(OperationSetBasicTelephony.class);
		while (peers.hasNext()) {
			CallPeer peer = peers.next();
			try {
				telephony.answerCallPeer(peer);
			} catch (Exception ex) {
				System.out.println("Could not answer " + peer
						+ " because of the following exception: "
						+ ex.getMessage());
			}
		}

	}
	
	public void hangupCall(Call call) {
		Iterator<? extends CallPeer> peerIter = call.getCallPeers();
		while (peerIter.hasNext()) {
			CallPeer peer = peerIter.next();
            OperationSetBasicTelephony<?> basicTelephony = peer.getProtocolProvider().getOperationSet(
                    OperationSetBasicTelephony.class);

            try {
                basicTelephony.hangupCallPeer(peer);
            }
            catch (OperationFailedException ofe) {
                System.out.println("cannot hangup "+ofe.getMessage());
            }
		}
	}
	
	public Iterator<? extends Call> getActiveCalls(User user) {
		ProtocolProviderFactory factory = ServiceContext.getProtocolProviderFactory();
		ServiceReference sr = factory.getProviderForAccount(getUserAccountId(user.getUserName()));
		ProtocolProviderService protocolProvider = (ProtocolProviderService) ServiceContext.getService(sr);
		OperationSetBasicTelephony<?> telephonyOpSet = protocolProvider
				.getOperationSet(OperationSetBasicTelephony.class);
		 return telephonyOpSet.getActiveCalls();
	}
	
	public void putOnOrOffHold(Call call) {
		Iterator<? extends CallPeer> peers = call.getCallPeers();

		OperationSetBasicTelephony<?> telephony = call.getProtocolProvider()
				.getOperationSet(OperationSetBasicTelephony.class);

		while (peers.hasNext()) {
			CallPeer callPeer = peers.next();

			try {
                if(callPeer instanceof MediaAwareCallPeer) {
                    boolean onHold = ((MediaAwareCallPeer<?,?,?>) callPeer)
                            .getMediaHandler().isLocallyOnHold();
                    if (!onHold) {
                    	telephony.putOnHold(callPeer);   	
                    } else {
                    	telephony.putOffHold(callPeer);
                    }
                }
			} catch (OperationFailedException ofex) {
				System.out.println("Failed to put on/off hold. "
						+ ofex.getMessage());
			}
		}
	}
	
	/**
	 * Puts call on hold if not already on hold
	 * @param call
	 */
	public void putOnHold(Call call) {
		Iterator<? extends CallPeer> peers = call.getCallPeers();

		OperationSetBasicTelephony<?> telephony = call.getProtocolProvider()
				.getOperationSet(OperationSetBasicTelephony.class);

		while (peers.hasNext()) {
			CallPeer callPeer = peers.next();

			try {
                if(callPeer instanceof MediaAwareCallPeer) {
                    boolean onHold = ((MediaAwareCallPeer<?,?,?>) callPeer)
                            .getMediaHandler().isLocallyOnHold();
                    if (!onHold) {
                    	telephony.putOnHold(callPeer);   	
                    }
                }
			} catch (OperationFailedException ofex) {
				System.out.println("Failed to put on/off hold. "
						+ ofex.getMessage());
			}
		}
	}
	
	public void enableDesktopSharing(Call call, MediaDevice device, boolean enable) {
		OperationSetDesktopSharingServer desktopOpSet = call
				.getProtocolProvider().getOperationSet(
						OperationSetDesktopSharingServer.class);

		// This shouldn't happen at this stage, because we disable the button
		// if the operation set isn't available.
		if (desktopOpSet != null) {
			try {
				desktopOpSet.setLocalVideoAllowed(call, device, enable);
				enableDesktopRemoteControl(call.getCallPeers().next(), false);
			} catch (OperationFailedException ex) {
				ex.printStackTrace();
			}
		}	
	}
	
	/**
	 * This method is meant  to be used only if you have only one Media Device
	 * @param call
	 * @param enable
	 */
	public void enableDesktopSharing(Call call, boolean enable) {
		OperationSetDesktopSharingServer desktopOpSet = call
				.getProtocolProvider().getOperationSet(
						OperationSetDesktopSharingServer.class);

		// This shouldn't happen at this stage, because we disable the button
		// if the operation set isn't available.
		if (desktopOpSet != null) {
			try {
				desktopOpSet.setLocalVideoAllowed(call, enable);
				if (call.getCallPeers().hasNext()) {
					enableDesktopRemoteControl(call.getCallPeers().next(), false);
				}
			} catch (OperationFailedException ex) {
				ex.printStackTrace();
			}
		}	
	}
	
	public void enableDesktopRemoteControl(CallPeer callPeer, boolean enable) {
		OperationSetDesktopSharingServer sharingOpSet = callPeer
				.getProtocolProvider().getOperationSet(
						OperationSetDesktopSharingServer.class);

		if (sharingOpSet == null) {
			return;
		}

		if (enable) {
			sharingOpSet.enableRemoteControl(callPeer);
		}
		else {
			sharingOpSet.disableRemoteControl(callPeer);
		}
	}
	
	public LocalRemoteVideo retrieveVideoStreamingComponents(User user) {
		if (user.getConnectedCall() == null) {
			return new LocalRemoteVideo(null, null);
		}
		return retrieveVideoStreamingComponents(user.getConnectedCall().getCall());
	}
	
	public LocalRemoteVideo retrieveVideoStreamingComponents(Call call) {
        Component remoteVideo = null;
        Component localVideo = null;
		if (call == null) {
			return new LocalRemoteVideo(remoteVideo, localVideo);
		}
		CallPeer callPeer = call.getCallPeers().next();
        OperationSetVideoTelephony videoTelephony = callPeer.getProtocolProvider().
        		getOperationSet(OperationSetVideoTelephony.class);
        if (videoTelephony != null) {
            List<Component> remoteVideos = videoTelephony.getVisualComponents(callPeer);
            if (remoteVideos != null && !remoteVideos.isEmpty()) {
            	remoteVideo = remoteVideos.get(0);
            }
            try {
				localVideo = videoTelephony.getLocalVisualComponent(callPeer);
			} catch (OperationFailedException e) {
				System.out.println("Failed to retrieve local video");
			}
        }
        return new LocalRemoteVideo(localVideo, remoteVideo);
	}	
	
	public boolean enableLocalVideo(Call call, boolean enable) {
        OperationSetVideoTelephony telephony = call.getProtocolProvider()
            .getOperationSet(OperationSetVideoTelephony.class);
        boolean enableSucceeded = false;

        if (telephony != null) {
        	try {
        		telephony.setLocalVideoAllowed(call, enable);
        		enableSucceeded = true;
        	}
        	catch (Exception ex) {
        		System.out.println(
        				"Failed to toggle the streaming of local video." + ex.getMessage());
        	}
        }
        return enableSucceeded;
	}
	
    public boolean isLocalVideoEnabled(Call call) {
        OperationSetVideoTelephony telephony
            = call.getProtocolProvider().getOperationSet(
                    OperationSetVideoTelephony.class);

        return (telephony != null) && telephony.isLocalVideoAllowed(call);
    }
	
    public boolean isDesktopSharingEnabled(Call call) {
        OperationSetDesktopSharingServer desktopOpSet
            = call.getProtocolProvider().getOperationSet(
                    OperationSetDesktopSharingServer.class);

        if (desktopOpSet != null
            && desktopOpSet.isLocalVideoAllowed(call)) {
            return true;
        }
        return false;
    }
    
    public void transferCall(Call call, String target) { 
    	CallPeer initialPeer = call.getCallPeers().next();
        OperationSetAdvancedTelephony<?> telephony = call.getProtocolProvider().getOperationSet(OperationSetAdvancedTelephony.class);

        if (telephony != null) {
        	try {
        		telephony.transfer(initialPeer, target);
        	}
        	catch (OperationFailedException ex) {
        		System.out.println("Call transfer");
        	}
        }
    }
	
	public void startRecording(Call call, File file) throws Exception {
        if (m_recorder == null) {
            OperationSetBasicTelephony<?> telephony
                = call.getProtocolProvider().getOperationSet(
                        OperationSetBasicTelephony.class);

            m_recorder = telephony.createRecorder(call);
        } else {
        	//TODO - create a dedicated Exception class for unite media and replace all system.out things
        	System.out.println("Already recording...");
        	return;
        }
        if (!SoundFileUtils.isSoundFile(file)) {
        	System.out.println("Not a sound file");
        	return;
        }
        m_recorder.start(SoundFileUtils.getExtension(file), file.getAbsolutePath());
	}
	
	public void stopRecording() {
		if (m_recorder != null) {
			try {
				m_recorder.stop();
			} finally {
				m_recorder = null;
			}
		} else {
			System.out.println("No recording in progress");
		}
	}
	
	public void setMuteUnmute(Call call) {
        OperationSetBasicTelephony<?> telephony = call.getProtocolProvider().getOperationSet(
                OperationSetBasicTelephony.class);
        telephony.setMute(call, !((MediaAwareCall<?,?,?>)call).isMute());
	}
}
