package org.ezuce.common.io;

import java.util.List;

import org.ezuce.common.xml.VoicemailMessageXML;

public class VoicemailMessagesIO extends XStreamIO {

	@Override
	public void setAliases() {
		setAlias("messages", List.class);
		setAlias("message", VoicemailMessageXML.class);		
		setAttributeFor("id", String.class);
		setAttributeFor("heard", boolean.class);
		setAttributeFor("urgent", boolean.class);
		setAttributeFor("folder", String.class);
		setAttributeFor("duration", String.class);
		setAttributeFor("received", long.class);
		setAttributeFor("author", String.class);
		setAttributeFor("authorExtension", String.class);
		setAttributeFor("username", String.class);
        setAttributeFor("format", String.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VoicemailMessageXML> parse(String xml) throws Exception {
		return (List<VoicemailMessageXML>)parseXml(xml);
	}

}
