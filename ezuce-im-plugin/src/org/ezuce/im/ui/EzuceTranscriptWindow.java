package org.ezuce.im.ui;

import static org.ezuce.im.html.HtmlFormatter.formatBottomHistorySeparator;
import static org.ezuce.im.html.HtmlFormatter.formatEmptyLine;
import static org.ezuce.im.html.HtmlFormatter.formatEmptyLine2;
import static org.ezuce.im.html.HtmlFormatter.formatHistorySeparator;
import static org.ezuce.im.html.HtmlFormatter.formatInitialContent;
import static org.ezuce.im.html.HtmlFormatter.formatMessage;
import static org.ezuce.im.html.HtmlFormatter.formatNotificationMessage;
import static org.ezuce.im.html.HtmlFormatter.formatPartySeparator;
import static org.ezuce.im.html.HtmlFormatter.formatStyleSheets;
import static org.jivesoftware.smack.util.StringUtils.parseBareAddress;
import static org.jivesoftware.smack.util.StringUtils.parseResource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.im.html.VergeHtmlEditorKit;
import org.jdesktop.swingx.calendar.DateUtils;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.ChatRoomNotFoundException;
import org.jivesoftware.spark.ui.TranscriptWindow;
import org.jivesoftware.spark.ui.TranscriptWindowInterceptor;
import org.jivesoftware.spark.ui.rooms.GroupChatRoom;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.plugin.transcripts.HistoryMessage;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceTranscriptWindow extends TranscriptWindow {

	private static final long serialVersionUID = 6530664508504846596L;

	private static final String TABLE_ID = "tid";

	private final String timeFormat;
	private final boolean showTime;
	private final HTMLDocument doc;
	private final Element table;
	protected final Desktop desktop;
	private String lastMessage = "";
	private boolean customized = false;

	public EzuceTranscriptWindow() {
		setBorder(BorderFactory.createEmptyBorder());
		// super.setBackground(new Color(0xEDF6F7));
		super.setBackground(new Color(0xFFFFFF));

		LocalPreferences pref = SettingsManager.getLocalPreferences();
		timeFormat = pref.getTimeFormat();
		showTime = pref.isTimeDisplayedInChat();
		HTMLEditorKit hed = new VergeHtmlEditorKit();
		StyleSheet ss = hed.getStyleSheet();

		for (String css : formatStyleSheets()) {
			ss.addRule(css);
		}

		doc = (HTMLDocument) hed.createDefaultDocument();
		addMouseMotionListener(new TextMotionListener());
		setEditorKit(hed);
		setDocument(doc);
		setInitialContent(formatInitialContent(TABLE_ID));
		table = doc.getElement(TABLE_ID);

		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		} else {
			desktop = null;
		}

		if (desktop != null) {
			addHyperlinkListener(new HyperlinkListener() {

				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
						try {
							String desc = e.getDescription();
							String httpPrefix = "http://";
							if (!StringUtils.startsWith(desc, httpPrefix)
									&& !StringUtils
											.startsWith(desc, "https://")
									&& !StringUtils.startsWith(desc, "ftp://")) {
								desc = new String(httpPrefix + desc);
							}
							desktop.browse(new URI(desc));
						} catch (IOException e1) {
							Log.warning("URL not valid");
						} catch (URISyntaxException e1) {
							Log.warning("URL not valid");
						}
					}
				}
			});
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!customized) {
			this.setBorder(BorderFactory.createLineBorder(new Color(192, 192,
					192)));
			customized = true;
		}
	}

	@Override
	public void setBackground(Color bg) {
		// super.setBackground(new Color(0xEDF6F7));
		super.setBackground(new Color(0xFFFFFF));
	}

	@Override
	protected Color getMessageColor() {
		return new Color(0x5B5D5E);
	}

	@Override
	public void insertHistoryMessage(String from, String nickname,
			String message, Date date, boolean groupChat) {

		long lastPostTime = getLastPost() != null ? getLastPost().getTime() : 0;
		long lastPostStartOfDay = DateUtils.startOfDayInMillis(lastPostTime);
		long newPostStartOfDay = DateUtils.startOfDayInMillis(date.getTime());
		if (groupChat) {
			from = participantToJid(from);
		}
		long diff = Utils.getDaysDiff(lastPostStartOfDay, newPostStartOfDay);
		if (diff != 0) {
			appendHtml(getHistorySeparatorHTML(date));
		}

		if (!nickname.equals(lastMessage)) {
			appendHtml(formatPartySeparator());
			if (showTime) {
				appendHtml(formatMessage(nickname, message, getDate(date),
						Color.BLACK, getAvatar(from)));
			} else {
				appendHtml(formatMessage(nickname, message, Color.BLACK,
						getAvatar(from)));
			}

			lastMessage = nickname;
		} else {
			if (showTime) {
				appendHtml(formatMessage(message, getDate(date)));
			} else {
				appendHtml(formatMessage(message));
			}
		}

		setLastPost(date);

	}

	private String getAvatar(String jid) {
		if (jid == null)
			return null;
		URL url = SparkManager.getVCardManager().getAvatarURL(jid, true);
		// Verify if comes from a chat window opened from a group-chat
		if (url == null) {
			String jidUser = participantToJid(jid);
			if (jidUser != null) {
				url = SparkManager.getVCardManager()
						.getAvatarURL(jidUser, true);
			}
		}
		return url != null ? url.toString() : getClass().getClassLoader()
				.getResource("resources/images/default_avatar_64x64.png")
				.toString();
	}

	@Override
	public void insertMessage(String nickname, Message message,
			Color foreground, Color background) {
		// Check interceptors.
		for (TranscriptWindowInterceptor interceptor : SparkManager
				.getChatManager().getTranscriptWindowInterceptors()) {
			boolean handled = interceptor.isMessageIntercepted(this, nickname,
					message);
			if (handled) {
				// Do nothing.
				return;
			}
		}

		String body = message.getBody();
		String jid;
		String from = message.getFrom();

		if (from != null) {
			if (message.getType().equals(Message.Type.groupchat)) {
				jid = participantToJid(from);
			} else {
				jid = parseBareAddress(from);
			}
		} else {
			jid = SparkManager.getSessionManager().getBareAddress();
		}

		if (jid != null && !jid.equals(lastMessage)) {
			appendHtml(formatPartySeparator());

			if (showTime) {
				DelayInformation inf = (DelayInformation) message.getExtension(
						"x", "jabber:x:delay");
				Date sentDate;
				if (inf != null) {
					sentDate = inf.getStamp();
					body = "(Offline) " + body;
				} else {
					sentDate = new Date();
				}
				appendHtml(formatMessage(nickname, body, getDate(sentDate),
						foreground, getAvatar(jid)));
			} else {
				appendHtml(formatMessage(nickname, body, foreground,
						getAvatar(jid)));
			}

			lastMessage = parseBareAddress(jid);
		} else {
			if (showTime) {
				DelayInformation inf = (DelayInformation) message.getExtension(
						"x", "jabber:x:delay");
				Date sentDate;
				if (inf != null) {
					sentDate = inf.getStamp();
					body = "(Offline) " + body;
				} else {
					sentDate = new Date();
				}
				appendHtml(formatMessage(body, getDate(sentDate)));
			} else {
				appendHtml(formatMessage(body));
			}
		}
		appendHtml(formatEmptyLine());
		if (getRoom().isAtBottom(
				getRoom().getScrollPaneForTranscriptWindow()
						.getVerticalScrollBar())) {
			getRoom().scrollToBottom();
		}
	}

	@Override
	public void insertHorizontalLine() {
		appendHtml(formatBottomHistorySeparator());
		lastMessage = "";
	}

	@Override
	public synchronized void insertNotificationMessage(String message,
			Color foregroundColor) {
		String text = formatNotificationMessage(message, foregroundColor);
		appendHtml(text);
		appendHtml(formatEmptyLine());
		if (getRoom().isAtBottom(
				getRoom().getScrollPaneForTranscriptWindow()
						.getVerticalScrollBar())) {
			getRoom().scrollToBottom();
		}
		// UN-420 persist events to show them in history
		Message notificationMessage = new Message();
		notificationMessage.setBody(message);
		notificationMessage.setFrom(getRoom().getRoomname());
		notificationMessage.setTo(getRoom().getRoomname());
		notificationMessage.setProperty("event", true);
		getRoom().getTranscripts().add(notificationMessage);
		SparkManager.getWorkspace().getTranscriptPlugin()
				.persistChatRoom(getRoom());
	}

	private String getHistorySeparatorHTML(Date sentDate) {
		return formatHistorySeparator(getNotificationDateFormatter().format(
				sentDate));
	}

	private void appendHtml(String htmlText) {
		try {
			doc.insertBeforeEnd(table, htmlText);
		} catch (BadLocationException e) {
			Log.error("Error inserting text " + e.getMessage());
		} catch (IOException e) {
			Log.error("IO error " + e.getMessage());
		}
	}

	private class TextMotionListener extends MouseInputAdapter {
		public void mouseMoved(MouseEvent e) {
			Element elem = doc.getCharacterElement(EzuceTranscriptWindow.this
					.viewToModel(e.getPoint()));
			AttributeSet as = elem.getAttributes();
			if (testAttributeNames(as, "a")) {
				EzuceTranscriptWindow.this.setCursor(new Cursor(
						Cursor.HAND_CURSOR));
			} else {
				EzuceTranscriptWindow.this.setCursor(new Cursor(
						Cursor.DEFAULT_CURSOR));
			}
		}
	}

	public static boolean testAttributeNames(AttributeSet as,
			String attributeName) {
		if (as != null && attributeName != null) {
			Enumeration<?> names = as.getAttributeNames();
			while (names.hasMoreElements()) {
				if ("a".equals(names.nextElement().toString())) {
					return true;
				}
			}
		}
		return false;
	}

	private String getDate(Date insertDate) {
		Date date = insertDate == null ? new Date() : insertDate;

		if (showTime) {
			final SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
			final String dateStr = formatter.format(date);
			return dateStr;
		}

		setLastUpdated(insertDate);
		return "";
	}

	/**
	 * Applies for messages that come from group chat participants
	 * 
	 * @param participant
	 * @return
	 */
	public String participantToJid(String participantFull) {
		String groupChatRoomName = Utils
				.getFirstSubstring(participantFull, "/");
		ChatManager chatManager = SparkManager.getChatManager();
		GroupChatRoom groupChatRoom;
		try {
			ChatRoom room = chatManager.getChatContainer().getChatRoom(
					groupChatRoomName);
			if (room instanceof GroupChatRoom) {
				groupChatRoom = (GroupChatRoom) room;
				String participantNick = parseResource(participantFull);
				if (participantNick == null) {
					return null;
				}
				return ((EzuceGroupChatParticipantList) groupChatRoom
						.getConferenceRoomInfo()).getNicknameMap().get(
						participantNick);
			} else {
				return null;
			}
		} catch (ChatRoomNotFoundException ex) {
			return null;
		}
	}

	@Override
	public void addComponent(final Component component) {
		appendHtml(formatEmptyLine2());
		super.addComponent(component);

	}

	public HTMLDocument getDoc() {
		return doc;
	}

	public void appendHtmlAfterStart(String htmlText) {
		try {
			doc.insertAfterStart(table, htmlText);
		} catch (BadLocationException e) {
			Log.error("Error inserting text " + e.getMessage());
		} catch (IOException e) {
			Log.error("IO error " + e.getMessage());
		}
	}

	public void insertHistoryMessage(List<HistoryMessage> historyTranscripts,
			Long historyEndsTimestamp, boolean isGroup) {
		if (historyTranscripts == null || historyTranscripts.isEmpty())
			return;

		String lastHistoryMessage = null;
		Calendar prevDay = Calendar.getInstance();
		prevDay.set(Calendar.YEAR, 1900);
		Calendar curDay = Calendar.getInstance();
		boolean insertHeader = true;

		StringBuilder html = new StringBuilder();
		int historySize = historyTranscripts.size();
		for (int i = 0; i < historySize; i++) {
			HistoryMessage message = historyTranscripts.get(i);

			String nickname = getNickname(message.getFrom());
			String messageBody = message.getBody();

			boolean containsMeEvent = messageBody.startsWith("/me ");
			if (ModelUtil.hasLength(messageBody) && containsMeEvent) {
				messageBody = messageBody.replaceFirst("/me", nickname);
			}

			boolean isEvent = message.isEvent();
			boolean isNewDay = i == 0;
			String from = isGroup ? participantToJid(message.getFrom())
					: message.getFrom();
			String bareFrom = isGroup ? from : parseBareAddress(from);
			String date = getDate(message.getDate());
			String avatar = getAvatar(from);
			String body = messageBody;

			// if message's date has changed then add a header
			curDay.setTime(message.getDate());
			if (prevDay.get(Calendar.DATE) != curDay.get(Calendar.DATE)) {
				insertHeader = true;
				prevDay.setTime(curDay.getTime());
			} else
				insertHeader = false;

			if (insertHeader) {
				html.append(getHistorySeparatorHTML(message.getDate()));
			}

			boolean isFromRoom = isGroup
					&& message.getFrom().indexOf("/") == -1;
			boolean isHistory = historyEndsTimestamp != null
					&& message.getDate().getTime() < historyEndsTimestamp;
			boolean isSameSender = lastHistoryMessage != null
					&& lastHistoryMessage.equals(bareFrom);
			Color foreground = isHistory ? Color.BLACK
					: getColor(from, isGroup);

			// add event "join, leave etc"
			if (containsMeEvent || isEvent) {
				html.append(formatNotificationMessage(messageBody,
						containsMeEvent ? Color.MAGENTA
								: ChatManager.NOTIFICATION_COLOR));
			} else {
				// add room msg e.g. "This room is not anonymous" etc
				if (isFromRoom) {
					html.append(formatMessage(body, date));

				} else {

					// add message from the previous person
					if (isSameSender && !isNewDay) {
						html.append(formatMessage(body, date));

						// add message from a new person
					} else {
						html.append(formatMessage(nickname, body, date,
								foreground, avatar));
					}
				}
			}
			lastHistoryMessage = bareFrom;
		}
		appendHtmlAfterStart(html.toString());
		repaint();
	}

	@Override
	public void poppingUp(final Object object, JPopupMenu popup) {
		// super.poppingUp(object, popup);
		Action printAction = new AbstractAction() {
			private static final long serialVersionUID = -244227593637660347L;

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				SparkManager.printChatTranscript((TranscriptWindow) object);
			}
		};

		Action clearAction = new AbstractAction() {
			private static final long serialVersionUID = -5664307353522844588L;

			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				String user = null;
				try {
					ChatManager manager = SparkManager.getChatManager();
					ChatRoom room = manager.getChatContainer()
							.getActiveChatRoom();
					user = room.getRoomname();

				} catch (ChatRoomNotFoundException e) {
					e.printStackTrace();
				}

				int ok = JOptionPane
						.showConfirmDialog((TranscriptWindow) object,
								Res.getString("delete.permanently"),
								Res.getString("delete.log.permanently"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (ok == JOptionPane.YES_OPTION) {
					if (user != null) {
						// This actions must be move into Transcript Plugin!
						File transcriptDir = new File(
								SparkManager.getUserDirectory(), "transcripts");
						File transcriptFile = new File(transcriptDir, user
								+ ".xml");
						transcriptFile.delete();
						transcriptFile = new File(transcriptDir, user
								+ "_current.xml");
						transcriptFile.delete();
						clear();
					}
				}
			}
		};

		printAction.putValue(Action.NAME, Res.getString("action.print"));
		printAction.putValue(Action.SMALL_ICON,
				SparkRes.getImageIcon(SparkRes.PRINTER_IMAGE_16x16));

		clearAction.putValue(Action.NAME, Res.getString("action.clear"));
		clearAction.putValue(Action.SMALL_ICON,
				SparkRes.getImageIcon(SparkRes.ERASER_IMAGE));
		popup.addSeparator();
		popup.add(printAction);

		popup.add(clearAction);
	}

	public Color getColor(String nickname) {
		return getColor(nickname, false);
	}

	public Color getColor(String nickname, boolean isGroup) {
		String bareJID = parseBareAddress(nickname);
		if (bareJID == null)
			return Color.BLACK;

		String currentJID = parseBareAddress(SparkManager.getSessionManager()
				.getJID());

		if (bareJID.equals(currentJID))
			return ChatManager.TO_COLOR;

		if (isGroup
				&& SettingsManager.getLocalPreferences().isMucRandomColors()) {
			int index = 0;
			for (int i = 0; i < nickname.length(); i++) {
				index += nickname.charAt(i) * i;
			}
			return ChatManager.COLORS[index % ChatManager.COLORS.length];
		}

		return ChatManager.FROM_COLOR;
	}

	public String getNickname(String from) {
		String personalNickname = SparkManager.getUserManager().getNickname();

		String nickname = SparkManager.getUserManager().getUserNicknameFromJID(
				from);

		if (nickname.equals(from)) {
			String otherJID = parseBareAddress(from);
			String myJID = SparkManager.getSessionManager().getBareAddress();

			if (otherJID.equals(myJID)) {
				nickname = personalNickname;
			} else {
				try {
					nickname = from.substring(from.indexOf("/") + 1);
				} catch (Exception e) {
					nickname = org.jivesoftware.smack.util.StringUtils
							.parseName(nickname);
				}
			}
		}
		return nickname;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
}
