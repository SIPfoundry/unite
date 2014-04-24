package org.ezuce.im;

import java.util.Collection;

import org.ezuce.common.ui.MessageNotifier;
import org.ezuce.im.ui.EzuceGroupChatRoom;
import org.ezuce.im.ui.EzuceGroupChatRoomPanel;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.rooms.GroupChatRoom;
import org.jivesoftware.spark.util.log.Log;

public class IMMessageNotifier extends MessageNotifier {

	@Override
	public void userLeftMyConference(String message) {
		//System.out.println("MIRCEA userLeftMyConference "+message);		
		updateHeader(message, true);
	}

	@Override
	public void userEnteredMyConference(String message) {
		//System.out.println("MIRCEA userEnteredMyConference "+message);
		updateHeader(message, true);
	}

	@Override
	public void userLeftConference(String message) {
		//System.out.println("MIRCEA userLeftConference "+message);
		updateHeader(message, false);
	}

	@Override
	public void userEnteredConference(String message) {
		//System.out.println("MIRCEA userEnteredConference "+message);
		updateHeader(message, false);
	}
	
	private void updateHeader(String messageBody, boolean resendMessage) {
		ChatManager chatManager = SparkManager.getChatManager();
		Collection<ChatRoom> rooms = chatManager.getChatContainer().getChatRooms();

		for(ChatRoom room : rooms) {			
			if (room instanceof EzuceGroupChatRoom && room.isActive()) {
				//System.out.println("MIRCEA update Header for: "+groupChatRoomName);
				EzuceGroupChatRoomPanel audioHeader = ((EzuceGroupChatRoom)room).getGroupChatRoomPanel();
				//If audio header is not null, it means that the group chat room instance is owned by the user
				//and has audio conference correspondent - only this room has to be updated
				if (audioHeader != null) {
					audioHeader.refreshParticipantsList();
				}
				//Resend messages to all participants of filtered rooms if needed
				if (resendMessage) {
					//TODO For the moment send message to all participants may be annoying for users
					//We need more exact server side sipXimbot message sending procedure in order for this to be effective
					//This cannot make it to 4.4 OpenUc release
					//sendMessageToAllParticipants(messageBody, groupChatRoomName);
				}
			}
		}		
	}
	
	private void sendMessage(String toJid, String message) {
		Connection connection = SparkManager.getConnection();
		org.jivesoftware.smack.ChatManager chatmanager = connection.getChatManager();
		Chat newChat = chatmanager.createChat(toJid, new MessageListener() {
		    public void processMessage(Chat chat, Message message) {
		    }
		});
		try {
		    newChat.sendMessage(message);
		}
		catch (XMPPException e) {
		    System.out.println("Error Delivering block");
		}

	}
	
	private void sendMessageToAllParticipants(String messageBody, String groupChatRoomName) {
		ChatManager chatManager = SparkManager.getChatManager();
		try {
			GroupChatRoom groupChatRoom = (GroupChatRoom)chatManager.getChatContainer().getChatRoom(groupChatRoomName);
			String jid = null;
			Occupant occupant = null;
			Collection<String> participants = groupChatRoom.getParticipants();
			for (String participant : participants) {
				occupant = groupChatRoom.getMultiUserChat().getOccupant(participant);
				jid = occupant.getJid();				
				if (!jid.equalsIgnoreCase(SparkManager.getSessionManager().getJID())) {
					System.out.println("MIRCEA re-sends mybuddy message to participant "+jid);
					sendMessage(jid, messageBody);
				}
			}	
		} catch (Exception ex) {
			Log.error("Room not found", ex);
		}
	}
}
