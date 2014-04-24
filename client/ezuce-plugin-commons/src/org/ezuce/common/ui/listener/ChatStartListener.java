package org.ezuce.common.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactItem;

public class ChatStartListener implements ActionListener {

	private final ContactItem contact;

	public ChatStartListener(ContactItem contact) {
		this.contact = contact;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		final ChatManager chatManager = SparkManager.getChatManager();
		final boolean handled = chatManager.fireContactItemDoubleClicked(contact);

		if (!handled) {
			chatManager.activateChat(contact.getJID(), contact.getDisplayName());
		}
	}

}
