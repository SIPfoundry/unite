package org.ezuce.archive.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class TestXmlUtil {

	public static XmlPullParser parseXML(String xml)
			throws ParserConfigurationException, SAXException, IOException,
			UnsupportedEncodingException, XmlPullParserException {

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser p = factory.newPullParser();
		StringReader reader = new StringReader(xml);
		p.setInput(reader);
		return p;
	}

	public static boolean isRequestPreferences(Document doc) {
		try {
			if (doc.getElementsByTagName("iq").getLength() < 1)
				return false;

			Element iqNode = (Element) doc.getElementsByTagName("iq").item(0);
			String iqType = iqNode.getAttribute("type");

			if (iqNode.getElementsByTagName("pref").getLength() < 1)
				return false;

			Element prefNode = (Element) iqNode.getElementsByTagName("pref")
					.item(0);
			String xmlns = prefNode.getAttribute("xmlns");

			return "get".equalsIgnoreCase(iqType)
					&& xmlns.equalsIgnoreCase("urn:xmpp:archive");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Document createDoc(String xml)
			throws ParserConfigurationException, SAXException, IOException,
			UnsupportedEncodingException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(
				xml.getBytes("utf-8"))));
		doc.getDocumentElement().normalize();
		return doc;
	}

	public static String getPacketId(Document doc) {
		NodeList iq = doc.getElementsByTagName("iq");
		String packetId = ((Element) iq.item(0)).getAttribute("id");
		return packetId;
	}

	public static String parseRequest(Document doc) {

		// if Client Requests Archiving Preferences
		if (isRequestPreferences(doc))
			// Server Returns Preferences
			return MockResponseFactory.DefaultPreference;

		return null;
	}

	public static String getCurrentJID(Document doc) {
		NodeList iq = doc.getElementsByTagName("iq");
		String jid = ((Element) iq.item(0)).getAttribute("from");
		return jid;
	}

}
