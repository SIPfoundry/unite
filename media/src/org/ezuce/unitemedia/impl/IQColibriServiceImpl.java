package org.ezuce.unitemedia.impl;

import java.awt.Dimension;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.java.sip.communicator.impl.protocol.jabber.extensions.DefaultPacketExtensionProvider;
import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;
import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriIQProvider;
import net.java.sip.communicator.impl.protocol.jabber.extensions.jingle.ParameterPacketExtension;
import net.java.sip.communicator.impl.protocol.jabber.extensions.jingle.PayloadTypePacketExtension;

import org.ezuce.unitemedia.service.IQColibriService;
import org.ezuce.unitemedia.streaming.StreamType;
import org.ezuce.unitemedia.streaming.StreamUtils;
import org.jitsi.impl.neomedia.MediaUtils;
import org.jitsi.impl.neomedia.VideoMediaStreamImpl;
import org.jitsi.impl.neomedia.format.VideoMediaFormatImpl;
import org.jitsi.service.neomedia.format.MediaFormat;
import org.jitsi.service.neomedia.format.MediaFormatFactory;
import org.jitsi.util.StringUtils;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class IQColibriServiceImpl implements IQColibriService {
	private ProviderManager m_providerManager;
	private XmlPullParserFactory m_factory;
	private ExecutorService m_singleThreadExecutor;
	
	
	public IQColibriServiceImpl() throws Exception {
		//Register IQ Custom Provider with SMACK
		m_providerManager = ProviderManager.getInstance();
        m_providerManager.addIQProvider(ColibriConferenceIQ.ELEMENT_NAME, ColibriConferenceIQ.NAMESPACE, new ColibriIQProvider());
        m_providerManager.addExtensionProvider(
                PayloadTypePacketExtension.ELEMENT_NAME,
                ColibriConferenceIQ.NAMESPACE,
                new DefaultPacketExtensionProvider<PayloadTypePacketExtension>(
                        PayloadTypePacketExtension.class));
        m_providerManager.addExtensionProvider(
                ParameterPacketExtension.ELEMENT_NAME,
                ColibriConferenceIQ.NAMESPACE,
                new DefaultPacketExtensionProvider<ParameterPacketExtension>(
                        ParameterPacketExtension.class));
        m_factory = XmlPullParserFactory.newInstance();
        m_factory.setNamespaceAware(true);
        m_singleThreadExecutor = Executors.newSingleThreadExecutor();
	}
	
	public ColibriConferenceIQ createConferenceIQForInvitee(ColibriConferenceIQ.Channel channel, String moderator, String invitee, StreamType strType) {
		ColibriConferenceIQ conference = new ColibriConferenceIQ();
		ColibriConferenceIQ.Content content = new ColibriConferenceIQ.Content();
		content.setName(StreamUtils.getContentType(strType));
		content.addChannel(channel);
		conference.addContent(content);
		conference.setFrom(moderator);
		System.out.println("INVITEE " + invitee);
		conference.setType(Type.GET);
		conference.setTo(invitee);
		System.out.println("MIRCEA create packet for " + invitee);
		return conference;
	}
	
	public ColibriConferenceIQ createConferenceIQForStopStream(String conferenceId, Set<String> channelIds, String videobridgeJID, String from, StreamType strType) {
		ColibriConferenceIQ conference = new ColibriConferenceIQ();
		conference.setID(conferenceId);
		ColibriConferenceIQ.Content content = new ColibriConferenceIQ.Content();
		content.setName(StreamUtils.getContentType(strType));
		ColibriConferenceIQ.Channel channel = null;
		for (String id : channelIds) {
			channel = new ColibriConferenceIQ.Channel();
			channel.setID(id);
			channel.setExpire(0);
			content.addChannel(channel);
		}
		conference.addContent(content);
		conference.setTo(videobridgeJID);
		conference.setFrom(from);
		conference.setType(Type.SET);
		return conference;
	}
	
	public ColibriConferenceIQ createConferenceIQForDenyStreaming(String conferenceId, String from, String to) {
		ColibriConferenceIQ conference = new ColibriConferenceIQ();
		conference.setID(conferenceId);
		conference.setType(Type.SET);
		conference.setFrom(from);
		conference.setTo(to);
		return conference;		
	}
	
	public ColibriConferenceIQ.Channel createChannel(List<MediaFormat> formats, int id) {
		PayloadTypePacketExtension ptpe = null;
		ColibriConferenceIQ.Channel channel = new ColibriConferenceIQ.Channel();
		int idCounter = id;
		for (MediaFormat format : formats) {
			ptpe = new PayloadTypePacketExtension();
			ptpe.setId(idCounter++);
			ptpe.setName(format.getEncoding());
			ptpe.setClockrate(new Double(format.getClockRate()).intValue());
			addParameters(ptpe, format.getFormatParameters());
			addParameters(ptpe, format.getAdvancedAttributes());
			channel.addPayloadType(ptpe);
		}
		return channel;
	}
	
	public void addScreenSizeAdvancedAttr(ColibriConferenceIQ.Channel channel, Dimension size) {
		ParameterPacketExtension ppe = null;
		boolean added = false;
		for (PayloadTypePacketExtension ptpe : channel.getPayloadTypes()) {
			if (ptpe.getName().equalsIgnoreCase("H264")) {
				ppe = new ParameterPacketExtension();
				ppe.setName("imageattr");
				ppe.setValue(MediaUtils.createImageAttr(null, size));
				ptpe.addParameter(ppe);
				added = true;
			}
		}
		if (!added) {
			PayloadTypePacketExtension ptpe = new PayloadTypePacketExtension();
			ptpe.setId(PAYLOAD_ID);
			ptpe.setName("H264");
			ptpe.setClockrate((int)VideoMediaFormatImpl.DEFAULT_CLOCK_RATE);
			addParameter(ptpe, "imageattr", MediaUtils.createImageAttr(null, size));
			channel.addPayloadType(ptpe);
		}
	}
	
	public Dimension getRecvScreenSizeAdvancedAttr(ColibriConferenceIQ.Channel channel) {
		String value = null;
		for (PayloadTypePacketExtension ptpe : channel.getPayloadTypes()) {
			if (ptpe.getName().equalsIgnoreCase("H264")) {
				for (ParameterPacketExtension ppe : ptpe.getParameters()) {
					if (ppe.getName().equals("imageattr")) {
						value = ppe.getValue();
						return VideoMediaStreamImpl.parseSendRecvResolution(value)[1];
					}
				}
			}
		}
		return null;
	}	
	
	private void addParameters(PayloadTypePacketExtension ptpe, Map<String, String> params) {
		ParameterPacketExtension ppe = null;
		Set<String> paramKeys = params.keySet();
		for (String paramKey : paramKeys) {
			addParameter(ptpe, paramKey, params.get(paramKey));
		}
	}
	
	private void addParameter(PayloadTypePacketExtension ptpe, String key, String value) {
		ParameterPacketExtension ppe = new ParameterPacketExtension();
		ppe.setName(key);
		ppe.setValue(value);
		ptpe.addParameter(ppe);
	}
	
	public ColibriConferenceIQ parseConferenceIQ(IQ iq) throws Exception {
		IQ convertedIQ = null;
		XmlPullParser parser = m_factory.newPullParser();
		parser.setInput(new StringReader(iq.toXML()));
		int eventType = parser.next();
		if (XmlPullParser.START_TAG == eventType) {
			String name = parser.getName();
			if ("iq".equals(name)) {
				eventType = parser.next();
				if (ColibriConferenceIQ.ELEMENT_NAME.equals(parser.getName())
						&& ColibriConferenceIQ.NAMESPACE.equals(parser.getNamespace()) && XmlPullParser.START_TAG == eventType) {
					IQProvider iqProvider = (IQProvider) m_providerManager.getIQProvider(ColibriConferenceIQ.ELEMENT_NAME, ColibriConferenceIQ.NAMESPACE);
					convertedIQ = iqProvider.parseIQ(parser);
					if (convertedIQ != null) {
						eventType = parser.getEventType();
						if (XmlPullParser.END_TAG != eventType) {
							throw new IllegalStateException(
									Integer.toString(eventType) + " != XmlPullParser.END_TAG");
						}
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
		if (convertedIQ != null) {
			convertedIQ.setFrom(iq.getFrom());
			convertedIQ.setTo(iq.getTo());
			return (ColibriConferenceIQ) convertedIQ;
		}
		return null;
	}
	
	public void shutdownExecutor() {
		m_singleThreadExecutor.shutdown();
	}

	public ExecutorService getSingleThreadExecutor() {
		return m_singleThreadExecutor;
	}
}
