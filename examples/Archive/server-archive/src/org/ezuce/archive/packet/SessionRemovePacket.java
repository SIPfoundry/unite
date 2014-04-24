package org.ezuce.archive.packet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.ezuce.archive.model.xml.IXmlElement;

public class SessionRemovePacket extends ArchivePacket {

	private static final MessageFormat gElement = new MessageFormat(
			"<sessionremove xmlns=\"{0}\">{1}</sessionremove>");

	private List<IXmlElement<?>> mSessions = new ArrayList<IXmlElement<?>>();

	@Override
	public String getChildElementXML() {
		StringBuilder sessionsXml = new StringBuilder();
		for (IXmlElement<?> item : getSessions())
			sessionsXml.append(item.toXml());

		return gElement.format(new String[] { gXmlns,
				sessionsXml.toString() });
	}

	public List<IXmlElement<?>> getSessions() {
		return mSessions;
	}

}
