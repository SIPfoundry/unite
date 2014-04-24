/**
 * 
 */
package org.ezuce.archive.impl.local;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * 
 *         Apr 10, 2013
 * @version 0.1
 */
public abstract class AbstractHistoryFile {

	protected static final int SIZE_MULTIPLICATOR = 1024; // 1024-KB, 2048-MB
	protected static final MessageFormat SIZE_FORMAT = new MessageFormat(
			"{0,number,#.##} Kb");
	protected static final DateFormat DAY_NAME_FORMAT = DateFormat
			.getDateInstance(DateFormat.MEDIUM);
	protected static final SimpleDateFormat MONTH_NAME_FORMAT = new SimpleDateFormat(
			"MMMM yyyy");
	private static final MessageFormat replacementFormat = new MessageFormat(
			"<font color=\"#FF0000\" style=\"font-weight: bold;\">{0}</font>");

	private static final Comparator<HistoryEntry> gHistoryEntryComparator = new Comparator<HistoryEntry>() {

		@Override
		public int compare(HistoryEntry o1, HistoryEntry o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	};

	protected Map<Date, HistoryEntry> entries = Collections.emptyMap();

	protected abstract long getSize();

	protected abstract Map<Date, HistoryEntry> createDayEntries();

	public String getFormatSize() {
		return SIZE_FORMAT.format(new Object[] { getSize() });
	}

	public Map<Date, HistoryEntry> getHistoryEntries() {
		entries = createDayEntries();
		return entries;
	}

	public List<HistoryEntry> search(String occurrence) {
		return search(getHistoryEntries(), occurrence);
	}

	// ############# UTILS ##############

	private List<HistoryEntry> search(Map<Date, HistoryEntry> entries,
			String occurrence) {

		List<HistoryEntry> result = new ArrayList<HistoryEntry>();
		for (Entry<Date, HistoryEntry> entry : entries.entrySet()) {

			HistoryEntry historyEntry = entry.getValue();

			if (!historyEntry.getMessages().isEmpty()) {

				if (hasOccurrence(historyEntry, occurrence)) {
					HistoryEntry copy = new HistoryEntry(historyEntry);
					highlihght(copy, occurrence);
					result.add(copy);
				}

			}
		}
		return result;
	}

	private boolean hasOccurrence(HistoryEntry historyEntry, String occurrence) {
		if (historyEntry.isEmpty())
			return false;

		for (HistoryMessage msg : historyEntry.getMessages()) {
			String body = msg.getBody();
			if (body.contains(occurrence)) {
				return true;
			}
		}
		return false;
	}

	private void highlihght(HistoryEntry historyEntry, String occurrence) {
		// TODO: now replaced always with the occurrence in lowercase.
		String replacement = replacementFormat
				.format(new String[] { occurrence });
		String insentiveCase = "(?i)";

		for (HistoryMessage msg : historyEntry.getMessages()) {
			String body = msg.getBody();
			msg.setBody(body
					.replaceAll(insentiveCase + occurrence, replacement));
		}
	}

	protected List<HistoryEntry> toList(Map<Date, HistoryEntry> months) {
		List<HistoryEntry> entries = new ArrayList<HistoryEntry>();
		for (Entry<Date, HistoryEntry> historyEntry : months.entrySet()) {
			entries.add(historyEntry.getValue());
		}
		Collections.sort(entries, gHistoryEntryComparator);
		return entries;
	}
}
