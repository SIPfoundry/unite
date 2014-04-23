package org.ezuce.media.manager;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.CallPeer;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.EzuceConferenceServices;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.xml.MyConferenceXML;
import org.ezuce.media.observers.CallObserver;
import org.ezuce.media.observers.RegisterObserver;
import org.ezuce.media.observers.VideoObserver;
import org.ezuce.unitemedia.phone.LocalRemoteVideo;
import org.ezuce.unitemedia.phone.Phone;
import org.ezuce.unitemedia.phone.UniteCall;
import org.ezuce.unitemedia.phone.User;
import org.jivesoftware.smackx.bookmark.BookmarkedConference;
import org.jivesoftware.spark.ui.conferences.ConferenceUtils;
import org.jivesoftware.spark.util.log.Log;

public class PhoneManager {
	private static final String PORT = "5060";
	private static PhoneManager singleton;
	private Phone phone;
	private User user;
	private PhoneManager() {
		phone = new Phone();
		try {
			user = new User();
			user.setServer(Config.getInstance().getServerAddress());
			user.setPort(PORT);
			user.setPassword(Config.getInstance().getSipPassword());
			user.setUserName(Config.getInstance().getSipUserId());
			//add observers
			user.addObserver(new RegisterObserver());
			user.addObserver(new CallObserver());
			user.addObserver(new VideoObserver());
			
		} catch (Exception e) {
			Log.error("cannot init phone api ", e);
		}
	}
	
    public static synchronized PhoneManager getInstance() {    	
        if (null == singleton) {
            singleton = new PhoneManager();
            return singleton;
        }
        return singleton;
    }
    
    public void registerUser() {
		phone.registerUser(user);
    }
    
    public void loadUser() {
    	phone.loadAccount(user);
    }
    
    public void uninstallUser(){
        phone.uninstallAccount(user);
    }
    
    public void unregisterUser() {
    	phone.unregisterUser(user);
    }
    
    public boolean isUserRegistered() {
    	return phone.isUserRegistered(user);
    }
    
    public void overrideEncodings(Collection<String> codecsToDisable) {
    	phone.overrideEncodings(user, codecsToDisable);
    	unregisterUser();
    }
    
    public boolean isEncodingActive(String encodingKey) {
    	return phone.isEncodingActive(user, encodingKey);
    }
    
    public String getH264Profile() {
    	return phone.getH264Profile();
    }
    
    public String getH264KeyFrameRequester() {
    	return phone.getH264KeyFrameRequester();
    }
    
    public String getH264Preset() {
    	return phone.getH264Preset();
    }
    
    public int getH264KeyInt() {
    	return phone.getH264KeyInt();
    }
    
    public boolean getH264DefaultIntraRefresh() {
    	return phone.getH264DefaultIntraRefresh();
    }
    
    public void setH264Profile(String profile) {
    	phone.setH264Profile(profile);
    }
    
    public void setH264KeyFrameRequester(String keyFrameRequester) {
    	phone.setH264KeyFrameRequester(keyFrameRequester);
    }
    
    public void setH264Preset(String preset) {
    	phone.setH264Preset(preset);
    }
    
    public void setH264KeyInt(int keyInt) {
    	phone.setH264KeyInt(keyInt);
    }
    
    public void setH264DefaultIntraRefresh(boolean intraRefresh) {
    	phone.setH264DefaultIntraRefresh(intraRefresh);
    }    
    
    public void saveConfiguration() {
    	phone.saveConfiguration();
    }
    
    public void makeCall(String number) {
    	phone.makeCall(user, number);
    }
    
    public void makeVideoCall(String number) {
    	phone.makeVideoCall(user, number);
    }
    
    public void answerCall(String callId) {
    	UniteCall uniteCall = user.getUniteCall(callId);
		if (uniteCall != null)
			phone.answerIncomingCall(uniteCall.getCall());
    }
    
    public void answerVideoCall(String callId) {
    	UniteCall uniteCall = user.getUniteCall(callId);
    	if (uniteCall != null)
    		phone.answerIncomingVideoCall(uniteCall.getCall());
    }
    
	public void putAllOnHoldExceptId(String callId) {
		Call call = null;
		Collection<UniteCall> uniteCalls = user.getUniteCalls();
		for (UniteCall uniteCall : uniteCalls) {
			call = uniteCall.getCall();
			if (!call.getCallID().equals(callId)) {
				phone.putOnHold(call);
			}
		}
	}   
        
    public void hangupCall(String callId) {
    	UniteCall uniteCall = user.getUniteCall(callId);
    	if (uniteCall != null)
    		phone.hangupCall(uniteCall.getCall());
    }
    
    public void enableOrDisableScreenSharing() {
		boolean enable = !phone.isDesktopSharingEnabled(getConnectedCall().getCall());
	    phone.enableDesktopSharing(getConnectedCall().getCall(), enable);
    }
    
    public void disableScreenSharing() {
    	if (!isScreenSharingEnabled()) {
    		phone.enableDesktopSharing(getConnectedCall().getCall(), false);
    	}
    }
    
    public boolean isScreenSharingEnabled() {
    	return phone.isDesktopSharingEnabled(getConnectedCall().getCall());
    }
    
    public void attachOrUnatachVideo(String callId) {
    	boolean enable = !phone.isLocalVideoEnabled(user.getUniteCall(callId).getCall());
    	phone.enableLocalVideo(user.getUniteCall(callId).getCall(), enable);
    }
    
    public boolean isVideoEnabled(String callId) {
    	return phone.isLocalVideoEnabled(user.getUniteCall(callId).getCall());
    }
    
    public void putOnOffHold(String callId) {    	
    	phone.putOnOrOffHold(user.getUniteCall(callId).getCall());
    }
    
    public void enableDesktopRemoteControl(boolean enable){
    	UniteCall uniteCall = user.getConnectedCall();
    	Iterator<? extends CallPeer> peers = uniteCall.getCall().getCallPeers();
    	if (peers.hasNext()) {
    		phone.enableDesktopRemoteControl(peers.next(), enable);
    	}
    }
    
    public UniteCall getConnectedCall() {
    	return user.getConnectedCall();
    }
    
    public String getConnectedCallFullJID() {
    	if (getConnectedCall() == null) {
    		return null;
    	}
    	String extension = getCaleeExtension(getConnectedCall().getCall());
    	return Utils.getFullJID(extension);
    }
    
    public String getConnectedCallJID () {
    	String callJID = getConnectedCallFullJID();
		return (callJID == null) ? null : Utils.getImId(callJID);
    }
    
    public String getCallFullJID(String callId) {
    	Call call = user.getUniteCall(callId).getCall();
       	String extension = getCaleeExtension(call);
    	return Utils.getFullJID(extension);
    }
    
    public Collection<UniteCall> getActiveCalls() {
    	return user.getUniteCalls();
    }
    
    public LocalRemoteVideo retireveVideoWindows() {
    	return phone.retrieveVideoStreamingComponents(user);
    }
    
	public void startRecording(File f) {
		try {
			phone.startRecording(getConnectedCall().getCall(), f);
		} catch (Exception ex) {
			Log.error(ex);
		}
	}

	public void stopRecording() {
		phone.stopRecording();
	}
	
	public void muteUnmuteCall(String callId) {
    	phone.setMuteUnmute(user.getUniteCall(callId).getCall());
	}
	
	public void transferCall(String callId, String target) {
		phone.transferCall(user.getUniteCall(callId).getCall(), target);
	}
        
	public String getCaleeExtension(Call call) {
		String displayName = "Unknown";
		
		Iterator<? extends CallPeer> peers = call.getCallPeers();
		CallPeer peer;
		if (peers.hasNext()) {
			peer = peers.next();
			if (peer.getContact() != null
					&& !StringUtils.isEmpty(peer.getContact().getDisplayName())) {
				displayName = peer.getContact().getDisplayName();
			} else {
				displayName = peer.getAddress();
			}
		}
		return Utils.getFirstSubstring(displayName, "@");
	}
	
	public boolean isCalledNumberOwnedConference(String number) {
		List<MyConferenceXML> confs = null;
		try {
			confs = RestManager.getInstance().getConferences(true);
		} catch (Exception e) {
			Log.error("Cannot retrieve user owned conferences ", e);
			return false;
		}
		for (MyConferenceXML conf : confs) {
			if (StringUtils.equals(conf.getExtension(), number) || StringUtils.equals(conf.getName(), number)) {
				return true;
			}
		}
		return false;
	}
	
	public void inviteIntoConference(String currentCallId) {
		String confName = EzuceConferenceServices.getDefaultBookmarkToUseName();
		String confExt = EzuceConferenceServices.getDefaultBookmarkToUseExtension();
		if (confName == null) {
			Log.error("Cannot get default user conference extension");
			return;
		}
		transferCall(currentCallId, confExt);

		BookmarkedConference bConf = EzuceConferenceServices.getDefaultBookmarkToUse();
		ConferenceUtils.joinConferenceOnSeperateThread(bConf.getName(), bConf.getJid(), bConf.getPassword());
	}
}
