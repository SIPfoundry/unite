package org.ezuce.archive.ui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.ezuce.archive.model.DateRange;
import org.ezuce.archive.model.xml.Body;
import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.ui.SearchPanel.SearchPanelEvent;
import org.ezuce.archive.ui.util.ArchiveUtil;
import org.jivesoftware.smack.packet.Message;

public abstract class UISearchManager implements Observer {

	private static final TextHighlighter gTextHighlighter = new TextHighlighter();
	private final List<Integer> mOccurrencePositions = new ArrayList<Integer>();

	private static final int gHistoryScrollStep = -24;
	private static final int gMinScrollPosition = 1;
	private static final int gLimitHistoryCollectionAtStart = 25;

	private int mPrevScrollPos = -1;
	private DateRange mHistoryRange;
	private SearchPanel mSearchPanel;
	private DateRange mStartRange;
	private DateRange mCalendarDateRange;
	private boolean isScrollEventAttached;
	private boolean isSearchStarted;
	private String mParticipantJID;
	private String mLastMessageJIDOnStart;

	public UISearchManager(String userDirectory, String participantJid) {
		mParticipantJID = participantJid;
		mSearchPanel = new SearchPanel(userDirectory, participantJid);
		ArchiveUtil.debugBorder(mSearchPanel);
		mSearchPanel.addCalendarObserver(this);
		mSearchPanel.addSearchPanelObserver(this);
	}

	public SearchPanel getSearchPanelUI() {
		return mSearchPanel;
	}

	public void showHistoryAtStart() {
		isSearchStarted = false;
		mPrevScrollPos = -1;
		mHistoryRange = mStartRange != null ? mStartRange.copy() : DateRange
				.getTomorrow();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					List<Chat> chats = mSearchPanel.retreiveLast(
							mParticipantJID, gLimitHistoryCollectionAtStart);
					clearTranscript();
					setLastMessageJID(chats);
					addChat(chats);
					scrollToBottom();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});

		if (!isScrollEventAttached) {
			attachScrollEvent();
			isScrollEventAttached = true;
		}
	}

	public void setStartRange(DateRange range) {
		this.mStartRange = range;
	}

	private AdjustmentListener scrollUpListener() {
		return new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {

				if (mCalendarDateRange != null)
					return;

				boolean isKnob = e.getValueIsAdjusting();

				if (!isKnob)
					return;

				scrollUpWithKnob(e);
			}

		};
	}

	protected void scrollUpWithWheel(MouseWheelEvent e) {
		if (mCalendarDateRange != null)
			return;

		int vPos = getTranscriptScrollPane().getVerticalScrollBar().getValue();
		int notches = e.getWheelRotation();

		// top
		if (notches < 0 && vPos == 0) {
			// System.out.println(" === mouseWheelMoved == ");
			// System.out.println("mouse vPos=" + vPos);
			// System.out.println("notches= " + notches);
			mSearchPanel.setVisible(true);
			retreiveHistory();
		}

	}

	protected void scrollUpWithKnob(AdjustmentEvent e) {
		// System.out.println("== Scroll Up Listener ==");
		if (mCalendarDateRange != null)
			return;
		int vPos = getTranscriptScrollPane().getVerticalScrollBar().getValue();

		// System.out.println("=vPos=" + vPos + " mPrevScrollPos=" +
		// mPrevScrollPos + " gMinScrollPosition=" + gMinScrollPosition);

		if (vPos < mPrevScrollPos && vPos < gMinScrollPosition) {
			// System.out.println("== retrieve history ==");
			mSearchPanel.setVisible(true);
			retreiveHistory();
		}
		mPrevScrollPos = vPos;

		if (mPrevScrollPos == 0) {
			int newPos = gMinScrollPosition + 1;
			getTranscriptScrollPane().getVerticalScrollBar().setValue(newPos);
		}
	}

	private void retreiveHistory() {
		if (mHistoryRange == null) {
			mHistoryRange = DateRange.getToday();
		} else {
			mHistoryRange.setEnd(mHistoryRange.getStart() - 1000); // - 1 sec
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mHistoryRange.getStart());
			c.add(Calendar.HOUR_OF_DAY, gHistoryScrollStep);
			mHistoryRange.setStart(c.getTimeInMillis());
		}

		mSearchPanel.setInfoText(ArchiveUtil.gInfoPanelDateFormat
				.format(mHistoryRange.getStartDate()));

		if (!isSearchStarted) {
			showHistory(mHistoryRange.getStart(), mHistoryRange.getEnd(), true);
			isSearchStarted = true;
		} else {
			showHistory(mHistoryRange.getStart(), mHistoryRange.getEnd(), false);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Date) {
			Date changedDate = (Date) arg;
			Calendar tmrw = Calendar.getInstance();
			tmrw.setTime(changedDate);
			tmrw.add(Calendar.DATE, 1);

			mSearchPanel.setInfoText(ArchiveUtil.gInfoPanelDateFormat
					.format(changedDate));
			mCalendarDateRange = new DateRange(changedDate.getTime(),
					tmrw.getTimeInMillis() - 1000);
			showHistory(mCalendarDateRange.getStart(),
					mCalendarDateRange.getEnd(), true);
			scrollToBottom();
		}

		if (arg instanceof SearchPanelEvent) {

			SearchPanelEvent event = (SearchPanelEvent) arg;
			Object[] data = event.getData();

			switch (event.getType()) {
			case SearchPanelEvent.PANEL_HIDDEN:
				// all messages will be removed and we should retrieve
				// the entire history
				// mStartRange = null;
				mCalendarDateRange = null;
				showHistoryAtStart();
				getTranscriptScrollPane().repaint();
				scrollToBottom();
				break;

			case SearchPanelEvent.MOVE_TO_PREV_OCCURRENCE:
			case SearchPanelEvent.MOVE_TO_NEXT_OCCURRENCE:
				if (data != null && data.length > 0 && data[0] != null) {
					getTranscriptWindow().setCaretPosition(
							mOccurrencePositions.get((Integer) data[0]));
				}
				break;

			case SearchPanelEvent.CLEAR_HIGHLIGHTS:
				getTranscriptWindow().getHighlighter().removeAllHighlights();
				break;

			case SearchPanelEvent.SEARCH_FOR_TEXT:
				if (data != null && data.length > 0) {
					searchInHistory((String) data[0]);
				}
				break;
			default:
				break;
			}
		}
	}

	private void scrollToBottom() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getTranscriptScrollPane().getVerticalScrollBar().setValue(
						getTranscriptScrollPane().getVerticalScrollBar()
								.getMaximum());
			}
		});

	}

	private void showHistory(final long start, final long end,
			final boolean clearTranscriptWindowContent) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					List<Chat> chats = mSearchPanel.retreiveHistory(
							mParticipantJID, start, end);
					if (clearTranscriptWindowContent)
						clearTranscript();
					addChat(chats);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void highlightOccurrance(int start, int end,
			TextHighlighter highlighter) {
		try {
			getTranscriptWindow().getHighlighter().addHighlight(start, end,
					highlighter);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void toggleHistoryPanelVisibility() {
		mSearchPanel.toggleHistoryPanelVisibility();
	}

	private void searchInHistory(final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					List<Chat> chats = mSearchPanel.searchFor(mParticipantJID,
							text);
					clearTranscript();
					addChat(chats);

					// after we update the history we should highlight the
					// occurrences
					searchInHtmlDoc(text);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void searchInHtmlDoc(String text) {

		mOccurrencePositions.clear();
		Pattern pattern = Pattern.compile(text);

		for (HTMLDocument.Iterator it = getHtmlDocument().getIterator(
				HTML.Tag.CONTENT); it.isValid(); it.next()) {
			try {
				String fragment = getHtmlDocument().getText(
						it.getStartOffset(),
						it.getEndOffset() - it.getStartOffset());
				Matcher matcher = pattern.matcher(fragment);
				while (matcher.find()) {
					int start = it.getStartOffset() + matcher.start();
					int end = it.getStartOffset() + matcher.end();

					// if any new text is added to textPane then the position
					// becomes incorrect
					mOccurrencePositions.add(start);
					highlightOccurrance(start, end, gTextHighlighter);
				}
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		mSearchPanel.setSearchText(text, mOccurrencePositions.size());
	}

	private void addChat(List<Chat> chats) throws Exception {
		if (chats == null || chats.isEmpty())
			return;

		List<Body> conversation = chats.get(0).getConversation();
		if (conversation == null || conversation.isEmpty()) {
			return;
		}

		List<Message> historyTranscript = new ArrayList<Message>();

		for (Chat chat : chats) {
			Calendar chatTime = Calendar.getInstance();
			chatTime.setTimeInMillis(chat.getStart());

			for (Body c : chat.getConversation()) {
				chatTime.add(Calendar.SECOND, c.getSec());
				Message message = new Message();
				message.setBody(c.getBody());
				message.setFrom(c.getFrom());
				message.setTo(c.getTo());
				message.setProperty("date", chatTime.getTime());
				message.setProperty("event", c.isEvent());
				historyTranscript.add(message);
			}
		}
		insertHistoryTranscript(historyTranscript);

	}

	private void clearTranscript() throws Exception {
		if (getHtmlDocument() != null)
			getHtmlDocument().remove(0, getHtmlDocument().getLength());
	}

	public void enableDebugOutput(boolean debug) {
		mSearchPanel.enableDebugOutput(debug);
	}

	private void setLastMessageJID(List<Chat> chats) {
		if (chats == null || chats.isEmpty())
			return;
		Chat last = chats.get(chats.size() - 1);
		if (last.getConversation().isEmpty())
			return;
		mLastMessageJIDOnStart = last.getConversation()
				.get(last.getConversation().size() - 1).getFrom();
	}

	public String getLastMessageJIDOnStart() {
		return mLastMessageJIDOnStart;
	}

	private void attachScrollEvent() {
		getTranscriptScrollPane().addMouseWheelListener(
				new MouseWheelListener() {

					@Override
					public void mouseWheelMoved(MouseWheelEvent e) {
						scrollUpWithWheel(e);
					}
				});
		getTranscriptScrollPane().getVerticalScrollBar().addAdjustmentListener(
				scrollUpListener());
	}

	public abstract JScrollPane getTranscriptScrollPane();

	public abstract JTextPane getTranscriptWindow();

	public abstract HTMLDocument getHtmlDocument();

	public abstract void insertHistoryTranscript(List<Message> historyTranscript)
			throws Exception;

}
