/**
 * 
 */
package com.ezuce.history.model;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author slava
 * 
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

	protected List<HistoryEntry> entries;

	protected abstract long getSize();

	protected abstract List<HistoryEntry> createEntries();

	public String getFormatSize() {
		return SIZE_FORMAT.format(new Object[] { getSize() });
	}

	public Collection<HistoryEntry> getHistoryEntries() {
		if (entries == null)
			entries = createEntries();
		return entries;
	}

	public List<HistoryEntry> search(String occurrence) {
		return search(entries, occurrence);
	}

	// ///////// UTILS

	private List<HistoryEntry> search(List<HistoryEntry> entries,
			String occurrence) {

		List<HistoryEntry> result = new ArrayList<HistoryEntry>();
		for (HistoryEntry historyEntry : entries) {

			if (!historyEntry.hasRecords()) {

				if (hasOccurrence(historyEntry, occurrence)) {
					HistoryEntry copy = new HistoryEntry(historyEntry);
					highlihght(copy, occurrence);
					result.add(copy);
				}

			} else {
				result.addAll(search(historyEntry.getEntries(), occurrence));
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

		Collections.sort(entries, new Comparator<HistoryEntry>() {

			@Override
			public int compare(HistoryEntry o1, HistoryEntry o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});

		return entries;
	}

	protected void printTree(Map<Date, HistoryEntry> months,
			Map<Date, HistoryEntry> days) {
		for (Entry<Date, HistoryEntry> month : months.entrySet()) {
			System.out.println("== MONTH =" + month.getKey());
			for (Entry<Date, HistoryEntry> day : days.entrySet()) {
				System.out.println("=== DAY ==" + day.getKey());
				if (day.getValue() == null)
					continue;
				for (HistoryMessage msg : day.getValue().getMessages()) {
					System.out.println("== MSG =" + msg);
				}
			}
		}
	}

	protected List<HistoryEntry> createTestHistoryEntries() {
		List<HistoryEntry> entries = new ArrayList<HistoryEntry>();

		// entry 1
		HistoryEntry entry = new HistoryEntry();
		entry.setName("April 2013");

		HistoryEntry subEntry = new HistoryEntry();
		subEntry.setName("Mon 15 Apr 2013 05:02:35 PM");

		HistoryMessage msg = new HistoryMessage();
		msg.setTo("2067@openuc.ezuce.com/Unite 4.6.0 build #103");
		msg.setFrom("mirceac@openuc.ezuce.com/6137ff6c");
		msg.setBody("hm");
		msg.setDate("2013-02-26 23:33:36.226 NOVT");
		subEntry.getMessages().add(msg);

		HistoryMessage msg2 = new HistoryMessage();
		msg2.setTo("2067@openuc.ezuce.com/Unite 4.6.0 build #103");
		msg2.setFrom("mirceac@openuc.ezuce.com/6137ff6c");
		msg2.setBody("It happens to me on Fedora 16");
		msg2.setDate("2013-02-26 23:33:36.226 NOVT");
		subEntry.getMessages().add(msg2);

		entry.getEntries().add(subEntry);
		entries.add(entry);

		return entries;
	}

}
