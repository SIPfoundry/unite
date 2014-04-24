package org.ezuce.archive.packet;

import java.text.MessageFormat;

import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.model.xml.IXmlElement;

public class SaveChatPacket extends ArchivePacket {

	public static final String Element = "save";
	private static final MessageFormat gElement = new MessageFormat(
			"<{0} xmlns=\"{1}\">{2}</{0}>");

	private Chat mChat;

	@Override
	public String getChildElementXML() {
		return gElement.format(new String[] { Element, ArchivePacket.gXmlns,
				IXmlElement.asString(mChat) });
	}

	public Chat getChat() {
		return mChat;
	}

	public void setChat(Chat chat) {
		this.mChat = chat;
	}
}
