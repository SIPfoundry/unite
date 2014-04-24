package org.ezuce.media.manager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.java.sip.communicator.service.protocol.Call;

import org.ezuce.common.phone.notifications.EzuceNotification;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.actions.task.SearchPhonebookUserTask;
import org.ezuce.common.ui.wrappers.interfaces.AudioCallPanelCommonInterface;
import org.ezuce.media.ui.AudioCallPanel;
import org.ezuce.media.ui.ScreenSharingPanel;
import org.ezuce.media.ui.VideoWindow;
import org.ezuce.media.ui.VideoWindowManager;
import org.ezuce.media.ui.listeners.AcceptCallListener;
import org.ezuce.media.ui.listeners.CameraListener;
import org.ezuce.media.ui.listeners.HangupCallListener;
import org.ezuce.media.ui.listeners.InviteToConfListener;
import org.ezuce.media.ui.listeners.MultiCastScreenSharingListener;
import org.ezuce.media.ui.listeners.MuteUmuteCallListener;
import org.ezuce.media.ui.listeners.OnOffHoldCallListener;
import org.ezuce.media.ui.listeners.RecordingCallListener;
import org.ezuce.media.ui.listeners.SIPScreenSharingListener;
import org.ezuce.media.ui.listeners.StopScreenSharingListener;
import org.ezuce.media.ui.listeners.TransferCallListener;
import org.ezuce.unitemedia.phone.LocalRemoteVideo;
import org.ezuce.unitemedia.phone.UniteCall;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class UIMediaManager {
    
    private static UIMediaManager singleton = new UIMediaManager();
    
    private Map<String,AudioCallPanel> audioChatCallPanelMap = new HashMap<String,AudioCallPanel>();
    private Map<String,AudioCallPanel> audioCallTabCallPanelMap = new HashMap<String,AudioCallPanel>();
    private ScreenSharingPanel transmitterChatScreenSharingPanel;
    private ScreenSharingPanel receiverChatScreenSharingPanel;
    private ScreenSharingPanel transmitterCallTabScreenSharingPanel;
    private ScreenSharingPanel receiverCallTabScreenSharingPanel;
    private JPanel audioChatCallsPanel;
    private JPanel audioCallTabCallsPanel;

    private JPanel attachedSIPCallTabVideo;
    private JPanel attachedXMPPCallTabVideo;
    private JPanel attachedSIPChatVideo;
    private JPanel attachedXMPPChatVideo;
    
	private JPanel chatPanelWrapper = new JPanel(new BorderLayout());
	private JPanel callPanelWrapper = new JPanel(new BorderLayout());
	
    private UIMediaManager() {
    	//initialize video containers
        attachedSIPCallTabVideo = new JPanel(new GridLayout(1, 1));
        attachedSIPCallTabVideo.setBackground(VideoWindow.COLOR_BG);
        attachedXMPPCallTabVideo = new JPanel(new GridLayout(1, 1));
        attachedXMPPCallTabVideo.setBackground(VideoWindow.COLOR_BG);
        attachedSIPChatVideo = new JPanel(new GridLayout(1, 1));
        attachedSIPChatVideo.setBackground(VideoWindow.COLOR_BG);
        attachedXMPPChatVideo = new JPanel(new GridLayout(1, 1));
        attachedXMPPChatVideo.setBackground(VideoWindow.COLOR_BG);

        audioCallTabCallsPanel = new JPanel();
		VerticalLayout vLayout1 = new VerticalLayout();
		vLayout1.setGap(2);
		audioCallTabCallsPanel.setLayout(vLayout1);
        audioChatCallsPanel = new JPanel();
		VerticalLayout vLayout2 = new VerticalLayout();
		vLayout1.setGap(2);
		audioChatCallsPanel.setLayout(vLayout2);
		transmitterChatScreenSharingPanel = new ScreenSharingPanel();
		transmitterChatScreenSharingPanel.addStopScreenSharingAction(new StopScreenSharingListener());
		receiverChatScreenSharingPanel = new ScreenSharingPanel();
		receiverChatScreenSharingPanel.addStopScreenSharingAction(new StopScreenSharingListener());
		transmitterCallTabScreenSharingPanel = new ScreenSharingPanel();
		transmitterCallTabScreenSharingPanel.addStopScreenSharingAction(new StopScreenSharingListener());
		receiverCallTabScreenSharingPanel = new ScreenSharingPanel();
		receiverCallTabScreenSharingPanel.addStopScreenSharingAction(new StopScreenSharingListener());
		
		chatPanelWrapper.setBackground(VideoWindow.COLOR_BG);
		callPanelWrapper.setBackground(VideoWindow.COLOR_BG);
    }
    
    public static synchronized UIMediaManager getInstance() {
        return singleton;
    }
    
    public void cleanAttachedSIPCallTabVideo() {
    	attachedSIPCallTabVideo.removeAll();
    }
    
	private void addAudioCallPanel(final String callId, boolean video, Map<String,AudioCallPanel> audioPanelMap, JPanel audioCallsPanel) {		
		AudioCallPanel panel = audioPanelMap.get(callId);
		if (panel == null) {			
			panel = new AudioCallPanel(video);
			panel.addAnswerAction(new AcceptCallListener(callId, false));
			if (video) {
				panel.addAnswerVideoAction(new AcceptCallListener(callId, true));
			}
			panel.addHangupAction(new HangupCallListener(callId));
			panel.addCameraAction(new CameraListener(callId));
			String fullJID = PhoneManager.getInstance().getCallFullJID(callId);
			if (fullJID == null) {
				panel.addScreenSharingAction(new SIPScreenSharingListener());
			} else {
				panel.addScreenSharingAction(new MultiCastScreenSharingListener(new ScreenSharingInviteesImpl(fullJID)));
			}
			panel.addOnHoldAction(new OnOffHoldCallListener(callId));                        
                        panel.addRecordCallAction(new RecordingCallListener(SparkManager.getUserDirectory()));
                        panel.addTransferCallAction(new TransferCallListener(callId));
			panel.addMuteUnmuteAction(new MuteUmuteCallListener(callId));
			panel.addInviteToConferenceAction(new InviteToConfListener(callId));
			
			panel.setVisible(true);
			audioPanelMap.put(callId, panel);
			audioCallsPanel.add(panel, 0);
			audioCallsPanel.revalidate();
			audioCallsPanel.repaint();
		}
	}
	
	public void addAudioChatCallPanel(final String callId, boolean video) {
		addAudioCallPanel(callId, video, audioChatCallPanelMap, audioChatCallsPanel);
	}
	
	public void addAudioCallTabCallPanel(final String callId, boolean video) {
		addAudioCallPanel(callId, video, audioCallTabCallPanelMap, audioCallTabCallsPanel);
	}
	
	private void removeAudioCallPanel(final String callId, Map<String,AudioCallPanel> audioCallPanelMap, JPanel audioCallsPanel) {
		AudioCallPanel audioCallPanel = (AudioCallPanel) audioCallPanelMap.get(callId);
		if (audioCallPanel != null) {
			audioCallsPanel.remove(audioCallPanel);
			audioCallPanelMap.remove(callId);
			audioCallsPanel.revalidate();
			audioCallsPanel.repaint();
		}
	}
	
	public void removeChatAudioCallPanel(final String callId) {
		removeAudioCallPanel(callId, audioChatCallPanelMap, audioChatCallsPanel);
	}
	
	public void removeCallTabAudioCallPanel(final String callId) {
		removeAudioCallPanel(callId, audioCallTabCallPanelMap, audioCallTabCallsPanel);
	}
	
	public VideoWindowManager getVideoWindowManager(JPanel panelSIPToAttach, JPanel panelXMPPToAttach) {       
        return VideoWindowManager.getInstance(panelSIPToAttach, panelXMPPToAttach);
	}

    private void updateIncomingCallInProgress(JPanel audioCallsPanel, Map<String,AudioCallPanel> map, String callId, boolean callInConference) {
    	audioCallsPanel.setVisible(true);
    	AudioCallPanel audioPanel = getAudioCallPanel(map, callId);
    	audioPanel.setVisible(true);
		if (!callInConference) {
			audioPanel.updateGuiIncomingCallInProgress();
		} else {
			audioPanel.updateGuiCallIsInConference();
		}

		audioPanel.startCallDurationTimer();
    }
    
    public void updateIncomingCallTabCallInProgress(String callId, boolean callInConference) {
    	updateIncomingCallInProgress(audioCallTabCallsPanel, audioCallTabCallPanelMap, callId, callInConference);
    }
    
    public void updateIncomingChatCallInProgress(String callId, boolean callInConference) {
    	updateIncomingCallInProgress(audioChatCallsPanel, audioChatCallPanelMap, callId, callInConference);
    }
    
    private void rebuildJPanelMainControls(Map<String,AudioCallPanel> map, String callId) {
    	getAudioCallPanel(map, callId).rebuildJPanelMainControls();
    }
    
    public void rebuildJPanelCallTabMainControls(String callId) {
    	rebuildJPanelMainControls(audioCallTabCallPanelMap, callId);
    }
    
    public void rebuildJPanelChatMainControls(String callId) {
    	rebuildJPanelMainControls(audioChatCallPanelMap, callId);
    }
    
    private void updateOutgoingCallInProgress(JPanel audioCallsPanel, Map<String,AudioCallPanel> map, String callId, boolean callInConference) {
    	audioCallsPanel.setVisible(true);
    	AudioCallPanel audioPanel = getAudioCallPanel(map, callId);
    	audioPanel.setVisible(true);
		if(!callInConference) {
			audioPanel.updateGuiOutgoingCallInProgress();
		} else {
			audioPanel.updateGuiCallIsInConference();
		}
		audioPanel.startCallDurationTimer();       
    }
    
    public void updateOutgoingCallTabCallInProgress(String callId, boolean callInConference) {
    	updateOutgoingCallInProgress(audioCallTabCallsPanel, audioCallTabCallPanelMap, callId, callInConference);
    }
    
    public void updateOutgoingChatCallInProgress(String callId, boolean callInConference) {
        updateOutgoingCallInProgress(audioChatCallsPanel, audioChatCallPanelMap, callId, callInConference);
    }
    
    private void updateCallOnHold(JPanel audioCallsPanel, Map<String,AudioCallPanel> map, String callId) {
    	audioCallsPanel.setVisible(true);
    	AudioCallPanel audioPanel = getAudioCallPanel(map, callId);
    	audioPanel.setVisible(true);
    	audioPanel.updateGuiCallIsOnHold();
    	audioPanel.stopCallDurationTimer();
    }
    
    public void updateCallTabCallOnHold(String callId) {
    	updateCallOnHold(audioCallTabCallsPanel, audioCallTabCallPanelMap, callId);
    }
    
    public void updateChatCallOnHold(String callId) {
    	updateCallOnHold(audioChatCallsPanel, audioChatCallPanelMap, callId);
    }
    
    private void updateCallEnded(JPanel audioCallsPanel, Map<String,AudioCallPanel> map, String callId) {
    	AudioCallPanel audioPanel = getAudioCallPanel(map, callId);
    	audioPanel.stopCallDurationTimer();
    	audioPanel.initVisibility();
    	removeAudioCallPanel(callId, map, audioCallsPanel);
		if(PhoneManager.getInstance().getActiveCalls().isEmpty()) {
			audioCallsPanel.setVisible(false);
		}
    }
    
    public void updateCallTabCallEnded(String callId) {
    	updateCallEnded(audioCallTabCallsPanel, audioCallTabCallPanelMap, callId);
    }
    
    public void updateChatCallEnded(String callId) {
    	updateCallEnded(audioChatCallsPanel, audioChatCallPanelMap, callId);
    }
    
    private AudioCallPanel updateCallOutgoing(JPanel audioCallsPanel, Map<String,AudioCallPanel> map, String nbr, String callId, boolean video) {
        audioCallsPanel.setVisible(true);
        addAudioCallPanel(callId, video, map, audioCallsPanel);
        AudioCallPanel audioCallPanel = getAudioCallPanel(map, callId);
        Log.warning("########################## OUTGOING CALL: "+callId);
        audioCallPanel.stopCallDurationTimer();
        audioCallPanel.updateGuiCallIsOutgoing();
        audioCallPanel.setVisible(true);
        
        return audioCallPanel;
    }
    
    public AudioCallPanel updateCallTabCallOutgoing(String nbr, String callId, boolean video) {
    	return updateCallOutgoing(audioCallTabCallsPanel, audioCallTabCallPanelMap, nbr, callId, video);
    }
    
    public AudioCallPanel updateChatCallOutgoing(String nbr, String callId, boolean video) {
    	return updateCallOutgoing(audioChatCallsPanel, audioChatCallPanelMap, nbr, callId, video);
    }
    
    public void updateAllCallOutgoing(String nbr, String callId, boolean video) {
        List<AudioCallPanelCommonInterface> audioCallPanels=new ArrayList<AudioCallPanelCommonInterface>();
        audioCallPanels.add(updateCallTabCallOutgoing(nbr, callId, video));
        audioCallPanels.add(updateChatCallOutgoing(nbr, callId, video));
        SearchPhonebookUserTask sput = new SearchPhonebookUserTask(org.jdesktop.application.Application.getInstance(), 
                audioCallPanels, 
                nbr);
        sput.execute();        
    }
    

    private AudioCallPanel updateCallIncoming(JPanel audioCallsPanel, Map<String,AudioCallPanel> map, String nbr, String callId, boolean video) {
        audioCallsPanel.setVisible(true);
        addAudioCallPanel(callId, video, map, audioCallsPanel);
        AudioCallPanel audioCallPanel = getAudioCallPanel(map, callId);
        Log.warning("########################## INCOMING CALL: "+callId);
        audioCallPanel.stopCallDurationTimer();
        audioCallPanel.updateGuiCallIsIncoming();
        audioCallPanel.setVisible(true);
        
        return audioCallPanel;
    }
    
    public AudioCallPanel updateCallTabCallIncoming(String nbr, String callId, boolean video) {
    	return updateCallIncoming(audioCallTabCallsPanel, audioCallTabCallPanelMap, nbr, callId, video);
    }
    
    public AudioCallPanel updateChatCallIncoming(String nbr, String callId, boolean video) {
    	return updateCallIncoming(audioChatCallsPanel, audioChatCallPanelMap, nbr, callId, video);
    }
    
    public void updateAllCallIncoming(String nbr, String callId, boolean video) {
        List<AudioCallPanelCommonInterface> audioCallPanels=new ArrayList<AudioCallPanelCommonInterface>();
        audioCallPanels.add(updateCallTabCallIncoming(nbr, callId, video));
        audioCallPanels.add(updateChatCallIncoming(nbr, callId, video));
        SearchPhonebookUserTask sput = new SearchPhonebookUserTask(org.jdesktop.application.Application.getInstance(), 
                audioCallPanels, 
                nbr);
        sput.execute();
        //Show notification window:
        EzuceNotification.getInstance().showCall(callId, nbr, video);         
    }
    
    public VideoWindowManager getVideoWindowManager() {
    	return getVideoWindowManager(attachedSIPCallTabVideo, attachedXMPPCallTabVideo);
    }

	private void updateStartScreenSharing(ScreenSharingPanel scPanel) {
		showScreenSharingPanel(scPanel);
		showConnectingScreenSharingPanel(scPanel);
	}
	
	public void updateTransmitterCallTabStartScreenSharing() {
		updateStartScreenSharing(transmitterCallTabScreenSharingPanel);
	}
	
	public void updateReceiverCallTabStartScreenSharing() {
		updateStartScreenSharing(receiverCallTabScreenSharingPanel);
	}
	
	public void updateTransmitterChatStartScreenSharing() {
		updateStartScreenSharing(transmitterChatScreenSharingPanel);
	}
	
	public void updateReceiverChatStartScreenSharing() {
		updateStartScreenSharing(receiverChatScreenSharingPanel);
	}
    
    public void removeRemoteSIPScreen(String callId) {
    	getVideoWindowManager().removeRemoteSIPVideo(callId);
    }
    
    public void removeRemoteXMPPScreen(String jid) {
    	getVideoWindowManager().removeRemoteXMPPVideo(jid);
    }    
    
	public void addLocalScreen(Component video, Call call) {
		getVideoWindowManager().addLocalVideo(video, call.getCallID());
	}

	public void removeLocalScreen(String callId) {
		getVideoWindowManager().removeLocalVideo(callId);
	}
	
	public void removeAllLocalScreens() {
		getVideoWindowManager().removeAllLocalVideos();
	}
	
	public void removeAllRemoteScreens() {
		getVideoWindowManager().removeAllRemoteVideos();
	}
  
    private void addRemoteSIPScreen(Component video, Call call) {
    	getVideoWindowManager().addRemoteSIPVideo(video, call.getCallID());
    }
    
    public void addRemoteXMPPScreen(final Component component, final String jid) {
    	getVideoWindowManager().addRemoteXMPPVideo(component, jid);
    }
	
	public void refreshSIPVideo() {
		LocalRemoteVideo lrv = PhoneManager.getInstance().retireveVideoWindows();
		UniteCall uniteCall = PhoneManager.getInstance().getConnectedCall();
		//Wnen there is no connected call, remove all video components
		if (uniteCall == null || uniteCall.getCall() == null) {
			removeAllLocalScreens();
			removeAllRemoteScreens();
			return;
		}
		if (lrv.getLocal() == null) {
			removeLocalScreen(uniteCall.getCall().getCallID());
		} else {
			addLocalScreen(lrv.getLocal(), uniteCall.getCall());
		}
		if (lrv.getRemote() == null) {
			removeRemoteSIPScreen(uniteCall.getCall().getCallID());
			getVideoWindowManager().getVideoWindowByCallID(uniteCall.getCall().getCallID()).stopDurationTimer();
		} else {
			addRemoteSIPScreen(lrv.getRemote(), uniteCall.getCall());
			getVideoWindowManager().getVideoWindowByCallID(uniteCall.getCall().getCallID()).restartDurationTimer();
		}
	}
	
	/**
	 * add video from videobridge inside call panel. When possible we are using videobridge for screensharing
	 * meaning in case the call is with a internal user that is XMPP user too. for non-XMPP users calls
	 * or external calls we keep the old mechanism (refreshSIPVideo) 
	 */
	public void addXMPPVideo(Component videoComponent) {
		String fromJid = StreamingManager.getInstance().getFromJID();
		addRemoteXMPPScreen(videoComponent, Utils.getImId(fromJid));
	}
	
	public void removeXMPPVideo(Component videoComponent) {
		String fromJid = StreamingManager.getInstance().getFromJID();
		removeRemoteXMPPScreen(Utils.getImId(fromJid));
	}
	
    private void showScreenSharingPanel(final ScreenSharingPanel scPanel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (scPanel != null) {
                	scPanel.setVisible(true);
                }
            }
        });
    }
    
    private void showEnabledScreenSharingPanel(final ScreenSharingPanel scPanel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (scPanel != null) {
                    scPanel.updateEnabledScreenSharing();
                }
            }
        });
    }
    
    private void showConnectingScreenSharingPanel(final ScreenSharingPanel scPanel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (scPanel != null) {
                	scPanel.setVisible(true);
                	scPanel.updateConnectingScreenSharing();
                }
            }
        });
    }
    
    private void showAcceptScreenSharingPanel(final ScreenSharingPanel scPanel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (scPanel != null) {
                	scPanel.setVisible(true);
                	scPanel.updateAcceptScreenSharing();
                }
            }
        });
    }
    
	private void hideScreenSharingPanel(final ScreenSharingPanel scPanel) {
		scPanel.setVisible(false);
	}    
    
    public void showTransmitterChatScreenSharingPanel() {
    	showScreenSharingPanel(transmitterChatScreenSharingPanel);
    }
    
    public void showReceiverChatScreenSharingPanel() {
    	showScreenSharingPanel(receiverChatScreenSharingPanel);
    }
    
    public void showTransmitterCallTabScreenSharingPanel() {
    	showScreenSharingPanel(transmitterCallTabScreenSharingPanel);
    }
    
    public void showReceiverCallTabScreenSharingPanel() {
    	showScreenSharingPanel(receiverChatScreenSharingPanel);
    }
    
    public void showTransmitterCallTabEnabledScreenSharingPanel() {
    	showEnabledScreenSharingPanel(transmitterCallTabScreenSharingPanel);
    }
    
    public void showReceiverCallTabEnabledScreenSharingPanel() {
    	showEnabledScreenSharingPanel(receiverCallTabScreenSharingPanel);
    }
    
    public void showTransmitterChatEnabledScreenSharingPanel() {
    	showEnabledScreenSharingPanel(transmitterChatScreenSharingPanel);
    }
    
    public void showReceiverChatEnabledScreenSharingPanel() {
    	showEnabledScreenSharingPanel(receiverChatScreenSharingPanel);
    }
    
    public void showTransmitterChatConnectingScreenSharingPanel() {
    	showConnectingScreenSharingPanel(transmitterChatScreenSharingPanel);
    }
    
    public void showReceiverChatConnectingScreenSharingPanel() {
    	showConnectingScreenSharingPanel(receiverChatScreenSharingPanel);
    }
    
    public void showTransmitterCallTabConnectingScreenSharingPanel() {
    	showConnectingScreenSharingPanel(transmitterCallTabScreenSharingPanel);
    }
    
    public void showReceiverCallTabConnectingScreenSharingPanel() {
    	showConnectingScreenSharingPanel(receiverCallTabScreenSharingPanel);
    }
    
    public void showTransmitterChatAcceptScreenSharingPanel() {
    	showAcceptScreenSharingPanel(transmitterChatScreenSharingPanel);
    }
    
    public void showReceiverChatAcceptScreenSharingPanel() {
    	showAcceptScreenSharingPanel(receiverChatScreenSharingPanel);
    }
    
    public void showTransmitterCallTabAcceptScreenSharingPanel() {
    	showAcceptScreenSharingPanel(transmitterCallTabScreenSharingPanel);
    }
    
    public void showReceiverCallTabAcceptScreenSharingPanel() {
    	showAcceptScreenSharingPanel(receiverCallTabScreenSharingPanel);
    }
    
    public void hideTransmitterCallTabScreenSharingPanel() {
    	hideScreenSharingPanel(transmitterCallTabScreenSharingPanel);
    }
    
    public void hideReceiverCallTabScreenSharingPanel() {
    	hideScreenSharingPanel(receiverCallTabScreenSharingPanel);
    }
    
    public void hideTransmitterChatScreenSharingPanel() {
    	hideScreenSharingPanel(transmitterChatScreenSharingPanel);
    }
    
    public void hideReceiverChatScreenSharingPanel() {
    	hideScreenSharingPanel(receiverChatScreenSharingPanel);
    }
    
	private void customizePanelWrapper(JPanel firstPanel, JPanel panelWrapper) {
		panelWrapper.removeAll();
		if (firstPanel != null) {
			panelWrapper.add(firstPanel, BorderLayout.NORTH);
		}
		panelWrapper.validate();
	}


	private void customizePanelWrapper(JPanel firstPanel, JPanel secondPanel, JPanel panelWrapper) {
		panelWrapper.removeAll();
		if (firstPanel == null) {
			firstPanel = secondPanel;
			secondPanel = null;
		}
		if (firstPanel != null) {
			panelWrapper.add(firstPanel, BorderLayout.NORTH);
		}
		if (secondPanel != null) {
			panelWrapper.add(secondPanel, BorderLayout.CENTER);
		}
		panelWrapper.validate();
	}

	private void customizePanelWrapper(JPanel firstPanel, JPanel secondPanel,
			JPanel thirdPanel, JPanel panelWrapper) {
		panelWrapper.removeAll();
		if (firstPanel == null) {
			firstPanel = secondPanel;
			secondPanel = thirdPanel;
			thirdPanel = null;
		}
		if (firstPanel == null && secondPanel == null) {
			firstPanel = thirdPanel;
			thirdPanel = null;
		}
		if (firstPanel != null) {
			panelWrapper.add(firstPanel, BorderLayout.NORTH);
		}
		if (secondPanel != null) {
			panelWrapper.add(secondPanel, BorderLayout.CENTER);
		}
		if (thirdPanel != null) {
			panelWrapper.add(thirdPanel, BorderLayout.SOUTH);
		}
		panelWrapper.validate();
	}
	
	public void customizeChatPanelWrapper(JPanel firstPanel) {
		customizePanelWrapper(firstPanel, chatPanelWrapper);
	}
	
	public void customizeChatPanelWrapper(JPanel firstPanel, JPanel secondPanel) {
		customizePanelWrapper(firstPanel, secondPanel, chatPanelWrapper);
	}
	
	public void customizeChatPanelWrapper(JPanel firstPanel, JPanel secondPanel, JPanel thirdPanel) {
		customizePanelWrapper(firstPanel, secondPanel, thirdPanel, chatPanelWrapper);
	}
	
	public void customizeCallPanelWrapper(JPanel firstPanel) {
		customizePanelWrapper(firstPanel, callPanelWrapper);
	}
	
	public void customizeCallPanelWrapper(JPanel firstPanel, JPanel secondPanel) {
		customizePanelWrapper(firstPanel, secondPanel, callPanelWrapper);
	}
	
	public void customizeCallPanelWrapper(JPanel firstPanel, JPanel secondPanel, JPanel thirdPanel) {
		customizePanelWrapper(firstPanel, secondPanel, thirdPanel, callPanelWrapper);
	}	
    
    private AudioCallPanel getAudioCallPanel(Map<String,AudioCallPanel> map, String callId) {
    	return map.get(callId);
    }
	
    public AudioCallPanel getAudioCallTabCallPanel(String callId) {
        return audioCallTabCallPanelMap.get(callId);
    }
    
    public Collection<AudioCallPanel> getAudioCallTabCallPanels() {
    	return audioCallTabCallPanelMap.values();
    }
    
    public AudioCallPanel getAudioChatCallPanel(String callId) {
        return audioChatCallPanelMap.get(callId);
    }
    
    public Collection<AudioCallPanel> getAudioChatCallPanels() {
    	return audioChatCallPanelMap.values();
    }    

	public JPanel getAttachedSIPCallTabVideo() {
		return attachedSIPCallTabVideo;
	}
	
	public JPanel getAttachedXMPPCallTabVideo() {
		return attachedXMPPCallTabVideo;
	}	

	public JPanel getAttachedSIPChatVideo() {
		return attachedSIPChatVideo;
	}
	
	public JPanel getAttachedXMPPChatVideo() {
		return attachedXMPPChatVideo;
	}

	public JPanel getAudioChatCallsPanel() {
		return audioChatCallsPanel;
	}

	public JPanel getAudioCallTabCallsPanel() {
		return audioCallTabCallsPanel;
	}

	public ScreenSharingPanel getTransmitterChatScreenSharingPanel() {
		return transmitterChatScreenSharingPanel;
	}

	public ScreenSharingPanel getReceiverChatScreenSharingPanel() {
		return receiverChatScreenSharingPanel;
	}

	public ScreenSharingPanel getTransmitterCallTabScreenSharingPanel() {
		return transmitterCallTabScreenSharingPanel;
	}

	public ScreenSharingPanel getReceiverCallTabScreenSharingPanel() {
		return receiverCallTabScreenSharingPanel;
	}

	public JPanel getChatPanelWrapper() {
		return chatPanelWrapper;
	}
	
	public JPanel getCallPanelWrapper() {
		return callPanelWrapper;
	}

	public void setCallTabAudioPanelParentToUpdate(UnitemediaEventListener parent) {
		for (AudioCallPanel audioCallPanel : audioCallTabCallPanelMap.values()) {
			audioCallPanel.setParentToUpdate(parent);
		}
	}
 }
