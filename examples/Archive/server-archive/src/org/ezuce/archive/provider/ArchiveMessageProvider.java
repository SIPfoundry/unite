package org.ezuce.archive.provider;

import java.io.IOException;

import org.ezuce.archive.model.xml.Auto;
import org.ezuce.archive.model.xml.Default;
import org.ezuce.archive.model.xml.Item;
import org.ezuce.archive.model.xml.Method;
import org.ezuce.archive.model.xml.Session;
import org.ezuce.archive.packet.ArchivePacket;
import org.ezuce.archive.packet.PreferencePacket;
import org.ezuce.archive.packet.SaveChatPacket;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ArchiveMessageProvider implements IQProvider {

	private static final String gAttrXmlns = "urn:xmpp:archive";

	public ArchiveMessageProvider() {
		super();
	}

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		ArchivePacket response = null;
		int eventType = parser.getEventType();
		do {
			if (eventType == XmlPullParser.START_TAG) {

				String xmlns = parser.getAttributeValue(null, "xmlns");
				if (xmlns != null && xmlns.equals(gAttrXmlns)) {

					String tagName = parser.getName();

					if (PreferencePacket.Element.equals(tagName)) {
						response = processPrefElement(parser);

					} else if (SaveChatPacket.Element.equals(tagName)) {
						response = processSaveElement(parser);
					}
				}

			}
			eventType = parser.next();
		} while (eventType != XmlPullParser.END_DOCUMENT);

		return response;
	}

	private ArchivePacket processSaveElement(XmlPullParser parser) {
		SaveChatPacket chat = new SaveChatPacket();

		// TODO:

		return chat;
	}

	private ArchivePacket processPrefElement(XmlPullParser parser) {

		PreferencePacket prefs = new PreferencePacket();

		try {

			// move to "auto"
			int eventType = parser.next();
			do {
				String name = parser.getName();
				if (eventType == XmlPullParser.START_TAG
						&& Auto.Element.equals(name)) {
					prefs.setAuto(new Auto().fromXml(parser));

				} else if (eventType == XmlPullParser.START_TAG
						&& Default.Element.equals(name)) {
					prefs.setDefault(new Default().fromXml(parser));

				} else if (eventType == XmlPullParser.START_TAG
						&& Item.Element.equals(name)) {
					prefs.getItems().add(new Item().fromXml(parser));

				} else if (eventType == XmlPullParser.START_TAG
						&& Session.Element.equals(name)) {
					prefs.getSessions().add(new Session().fromXml(parser));

				} else if (eventType == XmlPullParser.START_TAG
						&& Method.Element.equals(name)) {
					prefs.addMethod(new Method().fromXml(parser));

				}
				eventType = parser.next();

				// stop when </pref> is reached
			} while (!(PreferencePacket.Element.equals(parser.getName()) && eventType == XmlPullParser.END_TAG));

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prefs;

	}
}
