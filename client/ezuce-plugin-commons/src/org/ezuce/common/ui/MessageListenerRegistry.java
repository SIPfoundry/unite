package org.ezuce.common.ui;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.spark.ui.ChatRoom;

public class MessageListenerRegistry {
	private static List<MessageNotifier> notifiers = new ArrayList<MessageNotifier>();
	
	public static void addNotifier(MessageNotifier notifier) {
		notifiers.add(notifier);
	}
	
	public static void processAll(ChatRoom chat, Message message) {
		for (MessageNotifier notifier : notifiers) {
			notifier.processMessage(chat, message);
		}
	}
}
