package org.ezuce.im.ui.history;

import javax.swing.JScrollPane;

import org.ezuce.im.ui.EzuceTranscriptWindow;
import org.jivesoftware.spark.ui.ChatInputEditor;

public class ChatHistoryManager extends AbstractChatHistoryManager {

	public ChatHistoryManager(String participantJID) {
		super(false, participantJID);
	}

	public static ChatHistoryManager create(String participantJID,
			EzuceTranscriptWindow transcriptWindow,
			JScrollPane scrollPaneForTranscriptWindow,
			ChatInputEditor chatInputEditor) {
		ChatHistoryManager m = new ChatHistoryManager(participantJID);
		m.setTranscriptWindow(transcriptWindow);
		m.setTextScroller(scrollPaneForTranscriptWindow);
		m.addHistoryControlKeysTo(chatInputEditor);
		return m;
	}
}
