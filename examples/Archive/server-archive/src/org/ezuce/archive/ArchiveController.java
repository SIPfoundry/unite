package org.ezuce.archive;

import java.util.List;

import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.model.xml.IXmlElement;
import org.ezuce.archive.model.xml.Item;
import org.ezuce.archive.model.xml.Session;

/**
 * Archive Management
 * */
public interface ArchiveController {

	// ### preferences ###
	void retrievePreference();

	void addPreference(String jid, IXmlElement<?> preference);

	void removeItems(String jid, List<Item> items);

	void removeSessions(String jid, List<Session> sessions);

	void enableAutoArchiving(String jid, boolean flag);

	// ### collection management ###
	List<Chat> listCollections(String jid, String with, long start, long end, String after);

	List<Chat> listCollectionsWithLimit(String jid, String with, int limit);

	List<Chat> listCollectionsWithLimit(String jid, String with, Long start,
			Long end, String after, Integer limit);

	// ### messages management ###
	void changeSubject(String currentAccount, Chat chat);

	Chat retrieveMessages(String jid, String with, long start, int limit);

	Chat retrieveMessages(String jid, String with, long start, String after, Integer limit);

	void uploadMessages(String jid, Chat chat);

	void remove(String jid, String with, long start, Long end);

	void remove(String jid, String with, long start,
			Long end, Boolean removeOnlyRecordedByServer);

	void remove(String jid, String with, long start);

	void removeAll(String jid);

	void removeAll(String jid, long end);

	List<Chat> search(String with, String text);

	List<Chat> search(String with, String text, long start, long end);

	// ### misc ###
	void enableDebugOutput(boolean debug);

}
