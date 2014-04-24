package org.ezuce.archive.util;

import java.text.MessageFormat;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class MockResponseFactory {

	public final static String CompletePreference = ""
			+ "<pref  xmlns=\"urn:xmpp:archive\" >"
			+ "     <auto  save=\"false\" />"
			+ "     <default  expire=\"31536000\" otr=\"concede\" save=\"body\" />"
			+ "     <item  jid=\"2067@ezuce.com\" otr=\"require\" save=\"false\" />"
			+ "     <item  expire=\"630720000\" jid=\"2068@ezuce.com\" otr=\"forbid\" save=\"message\" />"
			+ "     <session  thread=\"ffd7076498744578d10edabfe7f4a866\" save=\"body\" />"
			+ "     <method  type=\"auto\" use=\"forbid\" />"
			+ "     <method  type=\"local\" use=\"concede\" />"
			+ "     <method  type=\"manual\" use=\"prefer\" />" + "</pref>";

	// Service Default Preferences
	public final static String DefaultPreference = ""
			+ "<pref  xmlns=\"urn:xmpp:archive\" >"
			+ "		<default  otr=\"concede\" save=\"false\" unset=\"true\" />"
			+ "		<method  type=\"auto\" use=\"concede\" />"
			+ "		<method  type=\"local\" use=\"concede\" />"
			+ "		<method  type=\"manual\" use=\"concede\" />"
			+ "		<auto  save=\"false\" />" + "</pref>";

	public final static String ServerPushesNewChatSessionPrefs = ""
			+ "		<pref  xmlns=\"urn:xmpp:archive\" >"
			+ "			<session  thread=\"ffd7076498744578d10edabfe7f4a866\" save=\"body\" timeout=\"3600\" otr=\"concede\" />"
			+ "		</pref>";

	public static final String ServerPushesNewDefaultMode = ""
			+ "		<pref  xmlns=\"urn:xmpp:archive\" >"
			+ "			<default  otr=\"prefer\" save=\"false\" />" + "		</pref>";

	public static final String UploadCollection = ""
			+ "<save xmlns=\"urn:xmpp:archive\">"
			+ "		<chat with=\"juliet@capulet.com/chamber\" start=\"1469-07-21T02:56:15Z\" thread=\"damduoeg08\" subject=\"She speaks!\">"
			+ " 		<from secs=\"0\"><body>Art thou not Romeo, and a Montague?</body></from>"
			+ " 		<to secs=\"11\"><body>Neither, fair saint, if either thee dislike.</body></to>"
			+ " 		<from secs=\"7\"><body>How cam\"st thou hither, tell me, and wherefore?</body></from>"
			+ " 		<note utc=\"1469-07-21T03:04:35Z\">I think she might fancy me.</note>"
			+ " 	</chat>" + "</save>";

	public static final String EmptyListOfCollections = "<list xmlns=\"urn:xmpp:archive\" />";

	public static final String FirstPageOfCollection = ""
			+ "<chat  xmlns=\"urn:xmpp:archive\" "
			+ "			with=\"juliet@capulet.com/chamber\""
			+ "			start=\"1469­07­21T02:56:15Z\""
			+ "			subject=\"She speaks!\""
			+ "			version=\"4\" >"
			+ "		<from  secs=\"0\" ><body> Art thou not Romeo, and a Montague? </body></from>"
			+ "		<to  secs=\"11\" ><body> Neither, fair saint, if either thee dislike. </body></to>"
			+ "		<from  secs=\"9\" ><body> How cam\"st thou hither, tell me, and wherefore? </body></from>"
			+ "		<set  xmlns=\"http://jabber.org/protocol/rsm\" >"
			+ "		<first  index=\"0\" > 0 </first>" + "<last> 99 </last>"
			+ "<count> 217 </count>" + "</set>" + "</chat>";

	public static final String EmptyCollection = ""
			+ "<chat xmlns=\"urn:xmpp:archive\""
			+ "			with=\"juliet@capulet.com/chamber\""
			+ "			start=\"1469­07­21T02:56:15Z\" subject=\"She speaks!\""
			+ "			version=\"5\" />";

	public static final String RemovedAllCollections = "<remove xmlns=\"urn:xmpp:archive\" />";
	public static final String AutomaticArchivingCompulsory = "<error type=\"cancel\"><not-allowed xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\" /></error>";

	public static IQ createServerAcknowledgesChangeResponse(String packetId,
			String jid) {
		IQ response = new IQ() {

			@Override
			public String getChildElementXML() {
				return null;
			}

		};
		updateResponse(response, packetId, jid, Type.RESULT);
		return response;
	}

	public static void updateResponse(IQ packet, String packetId, String jid,
			Type type) {
		if (packet == null)
			return;
		packet.setType(type);
		packet.setTo(jid);
		packet.setPacketID(packetId);
	}

	public static String collectionCreatedResponseString(String id, String jid,
			String with, String start, String thread, String subject,
			String version) {
		return new MessageFormat(
				"<save xmlns=\"urn:xmpp:archive\"><chat with=\"{0}\" start=\"{1}\" {2} subject=\"{3}\" version=\"{4}\"></chat></save>")
				.format(new String[] { with, start,
						thread != null ? "thread=\"" + thread + "\"" : "",
						subject, version });
	}

	public static IQ collectionCreatedResponse(final String packetId,
			final String jid, final String with, final String start,
			final String thread, final String subject, final String version) {

		IQ response = new IQ() {

			@Override
			public String getChildElementXML() {
				return collectionCreatedResponseString(packetId, jid, with,
						start, thread, subject, version);
			}

		};
		updateResponse(response, packetId, jid, Type.RESULT);
		return response;

	}

	public static String collectionUpdated(String packetId, String gcurrentjid,
			String with, String startUTC, String threadId, String subject,
			String version) {
		Integer newVersion = Integer.parseInt(version) + 1;
		return collectionCreatedResponseString(packetId, gcurrentjid, with,
				startUTC, null, subject, newVersion.toString());
	}

	public static IQ createSimpleXmlResponse(final String xmlResponse,
			String jid, String packetId) {
		IQ iq = new IQ() {

			@Override
			public String getChildElementXML() {
				return xmlResponse;
			}
		};
		Type type = Type.RESULT;
		if (xmlResponse.contains("<error"))
			type = Type.ERROR;

		updateResponse(iq, packetId, jid, type);
		return iq;
	}

}
