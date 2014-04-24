package org.ezuce.archive.impl.server;

import java.util.Collections;
import java.util.List;

import org.ezuce.archive.ArchiveController;
import org.ezuce.archive.model.xml.Auto;
import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.model.xml.Default;
import org.ezuce.archive.model.xml.IXmlElement;
import org.ezuce.archive.model.xml.Item;
import org.ezuce.archive.model.xml.Method;
import org.ezuce.archive.model.xml.Session;
import org.ezuce.archive.model.xml.Set;
import org.ezuce.archive.packet.ArchivePacket;
import org.ezuce.archive.packet.ItemRemovePacket;
import org.ezuce.archive.packet.ListPacket;
import org.ezuce.archive.packet.PreferencePacket;
import org.ezuce.archive.packet.RemovePacket;
import org.ezuce.archive.packet.RetrievePacket;
import org.ezuce.archive.packet.SaveChatPacket;
import org.ezuce.archive.packet.SessionRemovePacket;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ.Type;

public class ServerArchiveController implements ArchiveController {

	private XMPPConnection mConnection;
	private boolean mDebug;

	public ServerArchiveController(XMPPConnection con) {
		if (con == null)
			throw new IllegalArgumentException("Connection cannot be null");

		this.mConnection = con;
	}

	// ### preferences ###

	@Override
	public void retrievePreference() {
		final PreferencePacket iqPref = new PreferencePacket();
		iqPref.setType(Type.GET);
		debug("retrievePreference: " + iqPref.toXML());
		mConnection.sendPacket(iqPref);
	}

	@Override
	public void addPreference(String jid, IXmlElement<?> pref) {
		final PreferencePacket iqPref = new PreferencePacket();
		iqPref.setType(Type.SET);
		iqPref.setFrom(jid);
		if (pref instanceof Default)
			iqPref.setDefault((Default) pref);
		else if (pref instanceof Auto)
			iqPref.setAuto((Auto) pref);
		else if (pref instanceof Session)
			iqPref.getSessions().add((Session) pref);
		else if (pref instanceof Item)
			iqPref.getItems().add((Item) pref);
		else if (pref instanceof Method)
			iqPref.addMethod((Method) pref);

		debug("addPreference: " + iqPref.toXML());
		mConnection.sendPacket(iqPref);
	}

	@Override
	public void removeItems(String jid, List<Item> items) {
		final ItemRemovePacket iq = new ItemRemovePacket();
		iq.setType(Type.SET);
		iq.setFrom(jid);
		iq.getItems().addAll(items);
		debug("removeItems: " + iq.toXML());
		mConnection.sendPacket(iq);
	}

	@Override
	public void removeSessions(String jid, List<Session> sessions) {
		final SessionRemovePacket iq = new SessionRemovePacket();
		iq.setType(Type.SET);
		iq.setFrom(jid);
		iq.getSessions().addAll(sessions);
		debug("removeSessions: " + iq.toXML());
		mConnection.sendPacket(iq);
	}

	@Override
	public void enableAutoArchiving(String jid, final boolean flag) {
		ArchivePacket iq = new ArchivePacket() {

			@Override
			public String getChildElementXML() {
				Auto auto = new Auto();
				auto.setSave(flag);
				auto.setXmlns(ArchivePacket.gXmlns);
				return auto.toXml();
			}
		};
		iq.setType(Type.SET);
		iq.setFrom(jid);
		debug("enableAutoArchiving: " + iq.toXML());
		mConnection.sendPacket(iq);
	}

	// ### collection management ###

	@Override
	public List<Chat> listCollectionsWithLimit(String jid, String with,
			Long start, Long end, String after, Integer limit) {
		ListPacket iq = new ListPacket();
		iq.setType(Type.GET);
		iq.setWith(with);
		iq.setFrom(jid);
		iq.setStart(start);
		iq.setEnd(end);
		iq.setSet(new Set(after, limit));
		debug("retrieveLast: " + iq.toXML());
		mConnection.sendPacket(iq);
		return Collections.emptyList(); // TODO
	}

	@Override
	public List<Chat> listCollectionsWithLimit(String jid, String with,
			int limit) {
		return listCollectionsWithLimit(jid, with, null, null, null, limit);
	}

	@Override
	public List<Chat> listCollections(String jid, String with, long start,
			long end, String after) {
		return listCollectionsWithLimit(jid, with, start, end, after, null);
	}

	// ### messages management ###

	@Override
	public void changeSubject(String jid, Chat chat) {
		final SaveChatPacket iq = new SaveChatPacket();
		iq.setType(Type.SET);
		iq.setFrom(jid);
		iq.setChat(chat);
		mConnection.sendPacket(iq);
		debug("changeSubject: " + iq.toXML());
	}

	@Override
	public Chat retrieveMessages(String jid, String with, long start, int limit) {
		return retrieveMessages(jid, with, start, null, limit);
	}

	@Override
	public Chat retrieveMessages(String jid, String with, long start,
			String after, Integer limit) {
		RetrievePacket iq = new RetrievePacket();
		iq.setType(Type.GET);
		iq.setFrom(jid);
		iq.setWith(with);
		iq.setStart(start);
		iq.setSet(new Set(after, limit));
		debug("retrieveMessages: " + iq.toXML());
		mConnection.sendPacket(iq);
		return null;
	}

	@Override
	public void uploadMessages(String jid, Chat chat) {
		final SaveChatPacket iq = new SaveChatPacket();
		iq.setType(Type.SET);
		iq.setFrom(jid);
		iq.setChat(chat);
		debug("uploadMessages: " + iq.toXML());
		mConnection.sendPacket(iq);
	}

	@Override
	public void remove(String jid, String with, long start, Long end) {
		remove(jid, with, start, end, null);
	}

	@Override
	public void remove(String jid, String with, long start, Long end,
			Boolean removeOnlyRecordedByServer) {
		RemovePacket iq = new RemovePacket();
		iq.setType(Type.SET);
		iq.setFrom(jid);
		iq.setWith(with);
		iq.setStart(start);
		iq.setEnd(end);
		iq.setOpen(removeOnlyRecordedByServer);
		debug("remove: " + iq.toXML());
		mConnection.sendPacket(iq);
	}

	@Override
	public void remove(String jid, String with, long start) {
		remove(jid, with, start, null);
	}

	@Override
	public void removeAll(String jid, long end) {
		long start = IXmlElement.fromUTCString(IXmlElement.gUtcZero);
		remove(jid, null, start, end, false);
	}

	@Override
	public void removeAll(String jid) {
		long start = IXmlElement.fromUTCString(IXmlElement.gUtcZero);
		long end = IXmlElement.fromUTCString(IXmlElement.gUtcFuture);
		remove(jid, null, start, end, false);
	}

	@Override
	public List<Chat> search(String with, String text, long start, long end) {
		// TODO:
		return null;
	}

	@Override
	public List<Chat> search(String with, String text) {
		// TODO
		return null;
	}

	@Override
	public void enableDebugOutput(boolean debug) {
		mDebug = debug;
	}

	private void debug(String string) {
		if (!mDebug)
			return;

		System.out.println("ServerArchiveController#" + string);
	}

}
