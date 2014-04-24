package org.ezuce.im.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.ezuce.common.resource.Utils;
import org.ezuce.media.manager.StreamingManager;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.media.manager.UnitemediaEventListener;
import org.ezuce.media.manager.UnitemediaEventManager;
import org.ezuce.media.ui.VideoWindow;
import org.ezuce.media.ui.VideoWindowManager;
import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.ChatRoomListener;
import org.jivesoftware.spark.ui.ChatRoomNotFoundException;
import org.jivesoftware.spark.util.log.Log;

public class EzuceChatRoomListener implements ChatRoomListener, UnitemediaEventListener {
	@Override
	public void chatRoomOpened(ChatRoom chatRoom) {
		if (!(chatRoom instanceof EzuceGroupChatRoom)) {
			return;
		}
		final EzuceGroupChatRoom room = (EzuceGroupChatRoom) chatRoom;
		room.buildChatHeader();
		room.getSplitPane().setDividerLocation(.75);
		//Lazily build audio header to ensure group chat to show up faster
		//audioHeader creation includes a REST api call which might take few seconds
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Log.warning("Buildinging AUDIO Header ...");
				room.buildToolbarSubjectPanel();
				room.buildAudioHeader();
			}
		});
	}

	@Override
	public void chatRoomLeft(ChatRoom room) {
		
	}

	@Override
	public void chatRoomClosed(ChatRoom room) {
		if (!(room instanceof EzuceChatRoom)) {
			return;
		}
		final EzuceChatRoom chatRoom = (EzuceChatRoom)room;
		detachVideoWindow(chatRoom);
	}

	@Override
	public void chatRoomActivated(ChatRoom room) {
		if (room instanceof EzuceChatRoom) {
			final EzuceChatRoom chatRoom = (EzuceChatRoom)room;
			Log.warning("Chat Room Activated");
			customizeChatPanel(chatRoom);
		} else if (room instanceof EzuceGroupChatRoom) {
			customizeActiveGroupChat();
		}
	}

	@Override
	public void userHasJoined(ChatRoom room, String userid) {
		
	}

	@Override
	public void userHasLeft(ChatRoom room, String userid) {
		
	}

	@Override
	public void callOutgoing(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void callIncoming(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void callEnded(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void incomingCallInProgress(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void outgoingCallInProgress(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void locallyOnHold(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void mutuallyOnHold(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void remotelyOnHold(UniteCallEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void localVideoStreaming(UniteVideoEvent event) {
		
	}

	@Override
	public void remoteScreenAdded(UniteVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void remoteScreenRemoved(UniteVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void localScreenAdded(UniteVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void localScreenRemoved(UniteVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void transmitterStreamInitialized(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void transmitterStreamDenied(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void transmitterStreamStarted(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
		customizeActiveGroupChat();
	}

	@Override
	public void transmitterStreamEnded(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
		customizeActiveGroupChat();
	}

	@Override
	public void receiverStreamInitialized(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void receiverStreamStarted(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
	}

	@Override
	public void receiverStreamEnded(UniteXMPPVideoEvent event) {
		customizeActiveChatPanel();
		customizeActiveGroupChat();
	}
	
	private void addPanelWrapperToChat(JPanel chatPanel) {
		JPanel panelWrapper = UIMediaManager.getInstance().getChatPanelWrapper();
		chatPanel.add(panelWrapper,
				new GridBagConstraints(1, 1, 1, 2, 0.1, 1.0,
						GridBagConstraints.NORTH, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
	}
        
        private void addPanelWrapperToChat(JSplitPane splitPane){
            JPanel panelWrapper = UIMediaManager.getInstance().getChatPanelWrapper();
            splitPane.setDividerSize(5);
            splitPane.setResizeWeight(.60);
            splitPane.setDividerLocation(.80);
            splitPane.setRightComponent(panelWrapper);             
        }
	
	private void customizeChatPanel(EzuceChatRoom room) {
		StreamingManager streamingManager = null;
		try {
			streamingManager = StreamingManager.getInstance();
		} catch (Exception ex) {
			Log.warning("Cannot retrieve streaming manager ", ex);
		}
		UIMediaManager uiManager = UIMediaManager.getInstance();
		UnitemediaEventManager umManager = UnitemediaEventManager.getInstance();
		VideoWindowManager vwManager = VideoWindowManager.getCreatedInstance();

		if (vwManager == null) {
			Log.warning("No Video window created...");
		}
		
		boolean onCall = umManager.isOnCall();
		boolean transmittingXMPPVideo = umManager.isTransmittingXMPPVideo();
		boolean receivingXMPPVideo = umManager.isReceivingXMPPVideo();
		boolean remoteVideoCallAdded = umManager.isRemoteVideoCallAdded();
		boolean localVideoCallAdded = umManager.isLocalVideoCallAdded();
		String jid = room.getParticipantJID();
		//JPanel chatPanel = room.getChatPanel();
                JSplitPane splitPane = room.getSplitPane();

		String callId = !onCall ? null : UnitemediaEventManager.getInstance().onCall(jid);
		boolean displayAudioPanel = (callId != null);
		boolean displayScTransmitterPanel = (transmittingXMPPVideo) && streamingManager != null && streamingManager.isTransmittingTo(jid);
		boolean displayScReceiverPanel = !displayScTransmitterPanel && receivingXMPPVideo && streamingManager != null && streamingManager.isReceivingFrom(jid);
		
		boolean displayXMPPVideoPanel = (receivingXMPPVideo && streamingManager != null && streamingManager.isReceivingFrom(jid)) && (vwManager != null && vwManager.getExistingVideoWindowByXMPPJid(Utils.getImId(jid)) != null);
		boolean displaySIPVideoPanel = ((displayAudioPanel && (remoteVideoCallAdded || localVideoCallAdded))) && (vwManager != null && vwManager.getExistingVideoWindowByCallID(callId) != null);
		boolean displayVideoPanel = vwManager != null && (displaySIPVideoPanel || displayXMPPVideoPanel);
		boolean displayBoth = vwManager != null && displaySIPVideoPanel && displayXMPPVideoPanel;
		
		JPanel audioPanel = !displayAudioPanel ? null : uiManager.getAudioChatCallPanel(callId);
		
		JPanel scPanel = displayScTransmitterPanel ? uiManager.getTransmitterChatScreenSharingPanel() : (displayScReceiverPanel ? uiManager.getReceiverChatScreenSharingPanel() : null);
		if (displayVideoPanel && room.isActive()) {
			VideoWindow videoWindow = null;
			if (displayBoth) {
				videoWindow = vwManager.getExistingVideoWindowByXMPPJid(Utils.getImId(jid));
				videoWindow.attachVideoTo(uiManager.getAttachedXMPPChatVideo());			
			} else {
				if (displayXMPPVideoPanel) {
					videoWindow = vwManager.getExistingVideoWindowByXMPPJid(Utils.getImId(jid));
					videoWindow.attachVideoTo(uiManager.getAttachedXMPPChatVideo());
				}
				if (displaySIPVideoPanel) {
					videoWindow = vwManager.getExistingVideoWindowByCallID(callId);
					videoWindow.attachVideoTo(uiManager.getAttachedSIPChatVideo());
				}
			}
		}
		JPanel videoPanel = displayBoth ? uiManager.getAttachedXMPPChatVideo() : (displayXMPPVideoPanel ? uiManager.getAttachedXMPPChatVideo() : (displaySIPVideoPanel ? uiManager.getAttachedSIPChatVideo() : null));	
		uiManager.customizeChatPanelWrapper(scPanel, videoPanel, audioPanel);
		
		if (audioPanel != null || scPanel != null || videoPanel != null) {
			//addPanelWrapperToChat(chatPanel);
                        addPanelWrapperToChat(splitPane);
		} else {
			//chatPanel.remove(uiManager.getChatPanelWrapper());
                        splitPane.remove(uiManager.getChatPanelWrapper());
		}
                
                splitPane.revalidate();
                splitPane.repaint();
		//chatPanel.revalidate();
		//chatPanel.repaint();
	}
	
	private void customizeActiveChatPanel() {
		try {
			ChatRoom chatRoom = SparkManager.getChatManager().getChatContainer().getActiveChatRoom();
			if (!(chatRoom instanceof EzuceChatRoom)) {
				return;
			}
			customizeChatPanel((EzuceChatRoom)chatRoom);
		} catch (ChatRoomNotFoundException e) {
			Log.error("There is no active chat room: no media customization performed");
		}
	}
	
	private void customizeGroupChat(EzuceGroupChatRoom room) {
		room.configureXMPPScreenSharing();
	}
	
	private void customizeActiveGroupChat() {
		try {
			ChatRoom chatRoom = SparkManager.getChatManager().getChatContainer().getActiveChatRoom();
			if (!(chatRoom instanceof EzuceGroupChatRoom)) {
				return;
			}
			customizeGroupChat((EzuceGroupChatRoom)chatRoom);
		} catch (ChatRoomNotFoundException e) {
			Log.error("There is no active chat room: no media customization performed");
		}
	}
	
	private void detachVideoWindow(EzuceChatRoom room) {
		UnitemediaEventManager umManager = UnitemediaEventManager.getInstance();
		VideoWindowManager vwManager = VideoWindowManager.getCreatedInstance();
		StreamingManager strManager = null;
		try {
			strManager = StreamingManager.getInstance();
		} catch (Exception ex) {
			Log.error("Cannot get streaming manager ", ex);
		}
		
		if (vwManager == null) {
			Log.warning("No Video window created...");
		}
		
		String jid = room.getParticipantJID();
		
		boolean onCall = umManager.isOnCall();
		String callId = !onCall ? null : UnitemediaEventManager.getInstance().onCall(jid);
		boolean receivingXMPPVideo = umManager.isReceivingXMPPVideo();
		boolean remoteVideoCallAdded = umManager.isRemoteVideoCallAdded();
		boolean localVideoCallAdded = umManager.isLocalVideoCallAdded();
		boolean displayAudioPanel = (callId != null);
		boolean displayXMPPVideoPanel = (receivingXMPPVideo && strManager != null && strManager.isReceivingFrom(jid));
		boolean displaySIPVideoPanel = (displayAudioPanel && (remoteVideoCallAdded || localVideoCallAdded));
		boolean displayVideoPanel = vwManager != null && (displaySIPVideoPanel || displayXMPPVideoPanel);
		if (displayVideoPanel) {
			VideoWindow videoWindow = null;
			if (displayXMPPVideoPanel) {
				videoWindow = vwManager.getVideoWindowByXMPPJid(Utils.getImId(jid));
				videoWindow.attachVideoToMainWindow();
			}
			if (displaySIPVideoPanel) {
				videoWindow = vwManager.getVideoWindowByCallID(callId);
				videoWindow.attachVideoToMainWindow();
			}
		}
	}

	@Override
	public void customize() {		
	}
}
