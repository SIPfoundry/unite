package org.ezuce.im.ui.history;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.im.ui.EzuceTranscriptWindow;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.spark.ui.ChatInputEditor;
import org.jivesoftware.sparkimpl.plugin.transcripts.ChatTranscript;
import org.jivesoftware.sparkimpl.plugin.transcripts.ChatTranscripts;
import org.jivesoftware.sparkimpl.plugin.transcripts.HistoryMessage;

public class GroupChatHistoryManager extends AbstractChatHistoryManager {

	private Date mCurrentDate;
	private Timer mRetrieveHistoryTimer;
	private boolean mHistoryRetrieved;

	private List<HistoryMessage> mHistoryMessages = new ArrayList<HistoryMessage>(
			25);
	private Comparator<? super HistoryMessage> historyMessageComparator = new Comparator<HistoryMessage>() {

		@Override
		public int compare(HistoryMessage o1, HistoryMessage o2) {
			return o1.getDate().compareTo(o2.getDate());
		}

	};

	private GroupChatHistoryManager(String participantJID) {
		super(true, participantJID);

		mCurrentDate = new Date();
		mRetrieveHistoryTimer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!mHistoryRetrieved) {

					if (mHistoryMessages != null && !mHistoryMessages.isEmpty()) {
						updateHistoryFile(mHistoryMessages);
						mHistoryMessages.clear();
						mHistoryMessages = null;
					}

					mSearchManager.showHistoryAtStart();
					mHistoryRetrieved = true;
				}
			}
		});
	}

	private void addToHistory(Message message) {
		HistoryMessage history = toHistoryMessage(message);
		mHistoryMessages.add(history);
	}

	private boolean contains(ChatTranscript transcript, HistoryMessage h) {
		List<HistoryMessage> messages = transcript.getMessages();
		int size = messages.size();
		for (int i = size - 1; i >= 0; i--) {
			HistoryMessage m = messages.get(i);
			if (StringUtils.equals(h.getBody(), m.getBody())
					&& equalsDate(h.getDate(), m.getDate())
					&& StringUtils.equals(h.getTo(), m.getTo())
					&& StringUtils.equals(h.getFrom(), m.getFrom()))
				return true;
		}
		return false;
	}

	private boolean equalsDate(Date server, Date local) {
		if (server == null || local == null)
			return false;

		Calendar localCal = Calendar.getInstance();
		localCal.setTime(local);
		localCal.set(Calendar.MILLISECOND, 0);

		Calendar serverCal = Calendar.getInstance();
		serverCal.setTime(server);
		serverCal.set(Calendar.MILLISECOND, 0);

		if (localCal.compareTo(serverCal) == 0)
			return true;

		// we should check the message within N second because
		// first we add a message to the transcript
		// after it is saved on the server.
		localCal.add(Calendar.SECOND, -5);
		Date before = localCal.getTime();

		localCal.add(Calendar.SECOND, 10);
		Date after = localCal.getTime();

		// check if it is in a range
		Date t = serverCal.getTime();
		return t.before(after) && t.after(before);
	}

	public HistoryMessage toHistoryMessage(Message msg) {
		HistoryMessage m = new HistoryMessage();
		m.setBody(msg.getBody());
		m.setFrom(msg.getFrom());
		m.setTo(msg.getTo());
		m.setDate((Date) msg.getProperty("date"));
		m.setEvent(msg.getProperty("event") == null ? false : (Boolean) msg
				.getProperty("event"));
		return m;
	}

	private void updateHistoryFile(List<HistoryMessage> historyMessages) {

		if (historyMessages.isEmpty())
			return;

		ChatTranscript transcript = ChatTranscripts
				.getChatTranscript(mParticipantJID);
		int historyLength = historyMessages.size();

		for (int hm = historyLength - 1; hm >= 0; hm--) {
			HistoryMessage fromServer = historyMessages.get(hm);
			if (!contains(transcript, fromServer)) {
				transcript.addHistoryMessage(fromServer);
			}
		}

		Collections.sort(transcript.getMessages(), historyMessageComparator);
		File transcriptFile = ChatTranscripts
				.getTranscriptFile(mParticipantJID);
		ChatTranscripts.writeToFile(transcriptFile, transcript.getMessages(),
				false);
	}

	public boolean isHistoryMessage(Message message) {
		Date messageSentDate = (Date) message.getProperty("date");

		boolean isFromRoom = message.getFrom().indexOf("/") == -1;
		if (messageSentDate.before(mCurrentDate) || isFromRoom) {
			addToHistory(message);
			mRetrieveHistoryTimer.restart();
			return true;
		}

		// start it when first Message packet comes if
		if (!mHistoryRetrieved) {
			mRetrieveHistoryTimer.start();
			return true;
		}
		return false;

	}

	public static GroupChatHistoryManager create(String participantJID,
			EzuceTranscriptWindow transcriptWindow,
			JScrollPane scrollPaneForTranscriptWindow,
			ChatInputEditor chatInputEditor) {
		GroupChatHistoryManager m = new GroupChatHistoryManager(participantJID);
		m.setTranscriptWindow(transcriptWindow);
		m.setTextScroller(scrollPaneForTranscriptWindow);
		m.addHistoryControlKeysTo(chatInputEditor);
		return m;
	}
}
