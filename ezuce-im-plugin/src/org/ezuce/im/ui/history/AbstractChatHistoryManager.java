package org.ezuce.im.ui.history;

import static org.jivesoftware.smack.util.StringUtils.parseBareAddress;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.html.HTMLDocument;

import org.ezuce.archive.ui.SearchPanel;
import org.ezuce.archive.ui.UISearchManager;
import org.ezuce.im.ui.EzuceTranscriptWindow;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatInputEditor;
import org.jivesoftware.sparkimpl.plugin.transcripts.HistoryMessage;

public abstract class AbstractChatHistoryManager {

	protected UISearchManager mSearchManager;
	protected EzuceTranscriptWindow transcriptWindow;
	protected JScrollPane textScroller;
	private Long historyDelimiterTimestamp;
	protected String mParticipantJID;

	public AbstractChatHistoryManager(boolean isGroup, String participantJID) {
		mParticipantJID = participantJID;
		historyDelimiterTimestamp = Calendar.getInstance().getTimeInMillis();
		createUIHistoryManager(isGroup);
	}

	private void createUIHistoryManager(final boolean isGroup) {
		mSearchManager = new UISearchManager(SparkManager.getUserDirectory()
				.getPath(), mParticipantJID) {

			@Override
			public void insertHistoryTranscript(List<Message> messages)
					throws Exception {
				insertHistoryMessageTranscript(messages,
						historyDelimiterTimestamp, isGroup);
			}

			@Override
			public JTextPane getTranscriptWindow() {
				return transcriptWindow;
			}

			@Override
			public JScrollPane getTranscriptScrollPane() {
				return textScroller;
			}

			@Override
			public HTMLDocument getHtmlDocument() {
				return ((EzuceTranscriptWindow) getTranscriptWindow()).getDoc();
			}

		};
		// mSearchManager.enableDebugOutput(true);
	}

	public void insertHistoryMessageTranscript(List<Message> messages,
			Long historyEndsTimestamp, boolean isGroup) {
		List<HistoryMessage> historyTranscripts = new ArrayList<HistoryMessage>();
		for (Message message : messages) {
			HistoryMessage history = new HistoryMessage();
			history.setFrom(message.getFrom());
			history.setTo(message.getTo());
			history.setDate((Date) message.getProperty("date"));
			history.setEvent((Boolean) message.getProperty("event"));
			history.setBody(message.getBody());
			historyTranscripts.add(history);
		}
		EzuceTranscriptWindow ezuceTranscriptWindow = (EzuceTranscriptWindow) mSearchManager
				.getTranscriptWindow();
		ezuceTranscriptWindow.insertHistoryMessage(historyTranscripts,
				historyEndsTimestamp, isGroup);
		ezuceTranscriptWindow.repaint();

		//
		if (!isGroup) {
			String from = mSearchManager.getLastMessageJIDOnStart();
			String barLastMessageJID = isGroup ? ezuceTranscriptWindow
					.participantToJid(from) : parseBareAddress(from);
			ezuceTranscriptWindow.setLastMessage(barLastMessageJID);
		}
	}

	public void addHistoryControlKeysTo(ChatInputEditor chatInputEditor) {
		// history panel
		KeyStroke openHistoryKey = KeyStroke.getKeyStroke("ctrl G");
		javax.swing.Action openHistoryAction = new AbstractAction("history") {

			private static final long serialVersionUID = 6284956204483406061L;

			public void actionPerformed(ActionEvent evt) {
				mSearchManager.toggleHistoryPanelVisibility();
			}
		};
		chatInputEditor.getActionMap().put("openHistory", openHistoryAction);
		chatInputEditor.getKeymap().addActionForKeyStroke(openHistoryKey,
				openHistoryAction);
	}

	public void showHistory() {
		mSearchManager.showHistoryAtStart();
	}

	public SearchPanel getUI() {
		return mSearchManager.getSearchPanelUI();
	}

	public void setTranscriptWindow(EzuceTranscriptWindow transcriptWindow) {
		this.transcriptWindow = transcriptWindow;
	}

	public void setTextScroller(JScrollPane scrollPaneForTranscriptWindow) {
		this.textScroller = scrollPaneForTranscriptWindow;
	}

	public Long getHistoryDelimiterTimestamp() {
		return historyDelimiterTimestamp;
	}
}
