package org.ezuce.archive.impl.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * 
 *         Apr 15, 2013
 * @version 0.1
 */
public class XMLHistoryFile extends AbstractHistoryFile {

	private static final String MESSAGE_TAG = "message";
	private long mTimeStamp;
	private File mHistoryFile;
	private long mSize;
	private Document historyXML;

	public XMLHistoryFile(String file) {
		if (file == null || "".equals(file))
			throw new IllegalArgumentException("History file cannot be null");

		mHistoryFile = new File(file);
	}

	@Override
	protected long getSize() {
		return mSize;

	}

	/**
	 * Days contain messages.
	 * */
	@Override
	protected Map<Date, HistoryEntry> createDayEntries() {
		if (!isModified()) {
			return entries;
		}

		historyXML = read();
		if (historyXML == null)
			return Collections.emptyMap();

		Element document = historyXML.getDocumentElement();
		Map<Date, HistoryEntry> days = new TreeMap<Date, HistoryEntry>();

		NodeList nl = document.getElementsByTagName(MESSAGE_TAG);

		for (int i = 0; i < nl.getLength(); i++) {
			Element messageElement = (Element) nl.item(i);
			HistoryMessage message = new HistoryMessage(messageElement);

			Date normalizedDate = message.getNormalizedDate();

			// create a day and put it in a month
			if (!days.containsKey(normalizedDate)) {
				HistoryEntry dayEntry = new HistoryEntry();
				dayEntry.setDate(normalizedDate);
				dayEntry.setName(DAY_NAME_FORMAT.format(normalizedDate));
				days.put(normalizedDate, dayEntry);
			}

			// add messages to a day
			HistoryEntry entry = days.get(normalizedDate);
			entry.getMessages().add(message);
		}

		return days;

	}

	public File getHistoryFile() {
		return mHistoryFile;
	}

	private Document read() {
		FileInputStream fis = null;
		try {
			mSize = mHistoryFile.length();
			fis = new FileInputStream(mHistoryFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fis);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private boolean isModified() {
		long timeStamp = mHistoryFile.lastModified();
		if (mTimeStamp != timeStamp) {
			mTimeStamp = timeStamp;
			return true;
		}
		return false;
	}
}