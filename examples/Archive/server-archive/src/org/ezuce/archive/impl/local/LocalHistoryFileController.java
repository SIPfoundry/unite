package org.ezuce.archive.impl.local;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ezuce.archive.ArchiveController;
import org.ezuce.archive.model.DateRange;
import org.ezuce.archive.model.xml.Body;
import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.model.xml.IXmlElement;
import org.ezuce.archive.model.xml.Item;
import org.ezuce.archive.model.xml.Session;

public class LocalHistoryFileController implements ArchiveController {

	private static final MessageFormat gHistoryFilePath = new MessageFormat(
			"{0}/transcripts/{1}.xml");
	private boolean mDebug = false;
	private String mWith;
	private XMLHistoryFile mHistoryFile;

	/**
	 * ctor.
	 * */
	public LocalHistoryFileController(String userHomeDir, String with) {
		this.mWith = with;
		String filePath = gHistoryFilePath.format(new String[] { userHomeDir,
				with });
		mHistoryFile = new XMLHistoryFile(filePath);
	}

	@Override
	public List<Chat> listCollections(String jid, String with1, long start,
			long end, String after) {

		final DateRange range = new DateRange(start, end);
		if (mDebug)
			System.out
					.println("LocalHistoryFileController#listCollections(range="
							+ range + ");");

		List<Chat> chats = findHistory(start, end, mHistoryFile, null);

		if (mDebug) {
			if (isChatEmpty(chats))
				System.out
						.println("LocalHistoryFileController#listCollections History is empty");
		}

		return chats;
	}

	@Override
	public List<Chat> search(String with1, String text) {
		DateRange entireHistoryRange = DateRange.getLast20Years();
		return search(mWith, text, entireHistoryRange.getStart(),
				entireHistoryRange.getEnd());
	}

	@Override
	public List<Chat> search(String with1, final String text, long start,
			long end) {

		final DateRange range = new DateRange(start, end);
		if (mDebug)
			System.out.println("LocalHistoryFileController#search(start="
					+ new Date(start) + ", end=" + new Date(end) + ");");

		List<Chat> chats = findHistory(start, end, mHistoryFile,
				new IHistoryCriteria() {

					@Override
					public boolean isSuitable(HistoryMessage msg) {
						return (msg.getBody().contains(text))
								&& range.isInRange(msg.getDate());
					}
				});

		if (mDebug) {
			if (isChatEmpty(chats))
				System.out
						.println("LocalHistoryFileController#search: Nothing is found");
		}
		return chats;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chat> listCollectionsWithLimit(String jid, String with1,
			int limit) {
		if (mDebug)
			System.out.println("LocalHistoryFileController#retrieveLast("
					+ mWith + ", " + limit + ")");

		List<Chat> chats = new ArrayList<Chat>();
		try {

			if (mDebug)
				System.out.println("retrieveLast# retrieving history from "
						+ mHistoryFile.getHistoryFile().getPath());

			Map<Date, HistoryEntry> entries = mHistoryFile.getHistoryEntries();

			if (entries.isEmpty())
				return chats;

			Date prevDate = null;
			Chat chat = null;
			int messagesCount = 0;

			// iterate over days backwards
			Set<Entry<Date, HistoryEntry>> daysEntrySet = entries.entrySet();
			Entry<Date, HistoryEntry>[] daysEntry = daysEntrySet
					.toArray(new Entry[daysEntrySet.size()]);
			for (int i = daysEntry.length - 1; i >= 0; i--) {

				// prevent inserting of an empty chat
				if (messagesCount >= limit)
					break;

				Entry<Date, HistoryEntry> dayEntry = daysEntry[i];
				List<HistoryMessage> dayMessages = dayEntry.getValue()
						.getMessages();

				int dayMessagesCount = dayMessages.size();

				chat = new Chat();
				chat.setVersion(0);
				chat.setStart(dayMessagesCount > 0 ? dayMessages.get(0)
						.getDate().getTime() : 0);
				chat.setWith(mWith);
				chat.setConversation(new ArrayList<Body>());
				chats.add(chat);
				prevDate = new Date(chat.getStart());

				// iterate over day's messages straightforward
				// and count how many messages already taken to calculate start
				// message from the next day
				int startIndex = dayMessagesCount - limit + messagesCount;
				startIndex = startIndex < 0 ? 0 : startIndex;

				for (int k = startIndex; k < dayMessagesCount
						&& messagesCount < limit; k++) {

					HistoryMessage mes = dayMessages.get(k);
					createMessageBody(prevDate, chat, mes);
					prevDate = mes.getDate();

					messagesCount++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.reverse(chats);
		return chats;
	}

	@Override
	public void enableDebugOutput(boolean debug) {
		this.mDebug = debug;
	}

	// ############### Helpers ##############

	public static int calculateSec(Date date, Date prevDate) {
		if (prevDate == null || date == null)
			return 0;
		long current = date.getTime();
		long prev = prevDate.getTime();
		float diff = current - prev;
		float sec = (diff) / (1000f);
		return (int) Math.round(sec);
	}

	private static void createMessageBody(Date prevDate, Chat chat,
			HistoryMessage mes) {
		Body body = new Body();
		body.setBody(mes.getBody());
		body.setSec(calculateSec(mes.getDate(), prevDate));
		body.setFrom(mes.getFrom());
		body.setTo(mes.getTo());
		body.setEvent(mes.isEvent());
		chat.getConversation().add(body);
	}

	private List<Chat> findHistory(long start, long end,
			XMLHistoryFile historyFile, IHistoryCriteria checkCallback) {
		List<Chat> chats = new ArrayList<Chat>();
		try {
			DateRange searchDateRange = new DateRange(start, end);

			Map<Date, HistoryEntry> historyDayEntries = historyFile
					.getHistoryEntries();
			if (historyDayEntries.isEmpty())
				return chats;

			Date prevDate = null;
			Chat chat = null;

			// iterate over all days
			for (Entry<Date, HistoryEntry> dayEntry : historyDayEntries
					.entrySet()) {

				// check if this day suits us
				if (!searchDateRange.isInRange(dayEntry.getKey()))
					continue;

				// create a new chat from messages
				List<HistoryMessage> dayMessages = dayEntry.getValue()
						.getMessages();

				int dayMessagesCount = dayMessages.size();

				// new day - new chat
				chat = new Chat();
				chat.setVersion(0);
				chat.setWith(mWith);
				chat.setStart(dayMessagesCount > 0 ? dayMessages.get(0)
						.getDate().getTime() : 0);
				chat.setConversation(new ArrayList<Body>());
				chats.add(chat);
				prevDate = new Date(chat.getStart());

				// iterate over all day's messages
				for (HistoryMessage mes : dayMessages) {

					// check if this message suits us
					if (checkCallback != null && !checkCallback.isSuitable(mes))
						continue;

					createMessageBody(prevDate, chat, mes);
					prevDate = mes.getDate();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chats;
	}

	private boolean isChatEmpty(List<Chat> chats) {
		return chats.isEmpty() || chats.get(0).getConversation().isEmpty();
	}

	// ###### TODO: to be implemented

	@Override
	public void retrievePreference() {
	}

	@Override
	public Chat retrieveMessages(String jid, String with, long start, int limit) {
		return new Chat();
	}

	@Override
	public void remove(String jid, String with, long start, Long end) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeAll(String jid, long end) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addPreference(String jid, IXmlElement<?> preference) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeItems(String jid, List<Item> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSessions(String jid, List<Session> sessions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadMessages(String jid, Chat chat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeSubject(String currentAccount, Chat chat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableAutoArchiving(String gcurrentjid, boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Chat> listCollectionsWithLimit(String jid, String with,
			Long start, Long end, String after, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chat retrieveMessages(String jid, String with, long start,
			String after, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String jid, String with, long start) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String jid, String with, long start, Long end,
			Boolean removeOnlyRecordedByServer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAll(String jid) {
		// TODO Auto-generated method stub

	}

}
