package org.ezuce.archive.util;

import org.ezuce.archive.provider.ArchiveMessageProvider;
import org.hamcrest.Description;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;

public abstract class MockAction<T extends IQ> implements Action {

	private IQ[] responseRef;
	private ArchiveMessageProvider provider;
	protected T packet;

	public MockAction(ArchiveMessageProvider provider, IQ[] respRef) {
		this.responseRef = respRef;
		this.provider = provider;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(Invocation invocation) throws Throwable {

		packet = (T) invocation.getParameter(0);
		if (!validatePacket(packet))
			throw new IllegalStateException("Packet is not valid ");

		String xmlRequest = packet.toXML();
		Document doc = TestXmlUtil.createDoc(xmlRequest);
		String packetId = TestXmlUtil.getPacketId(doc);
		String currentJID = TestXmlUtil.getCurrentJID(doc);

		// server answers
		XmlPullParser parser = TestXmlUtil.parseXML(getXmlResponse());
		responseRef[0] = provider.parseIQ(parser);

		// return result
		if (responseRef[0] != null)
			MockResponseFactory.updateResponse(responseRef[0], packetId,
					currentJID, Type.RESULT);
		else {
			responseRef[0] = MockResponseFactory.createSimpleXmlResponse(
					getXmlResponse(), currentJID, packetId);
		}
		return null;
	}

	protected abstract String getXmlResponse();

	protected abstract boolean validatePacket(T packet);

	@Override
	public void describeTo(Description arg0) {
		System.out.println("Description=" + arg0);
	}

}
