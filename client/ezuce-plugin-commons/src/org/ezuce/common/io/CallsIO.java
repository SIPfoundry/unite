package org.ezuce.common.io;

import java.util.List;

import org.ezuce.common.xml.CallXML;

public class CallsIO extends XStreamIO {
	@Override
	public void setAliases() {
    	setAlias("Results", List.class);
    	setAlias("Row", CallXML.class);    	
    	setAliasField("caller_aor", CallXML.class, "callerAor");
    	setAliasField("callee_aor", CallXML.class, "calleeAor");
    	setAliasField("callee_contact", CallXML.class, "calleeContact");
    	setAliasField("start_time", CallXML.class, "startTime");    	    	
    	setAliasField("callee_route", CallXML.class, "calleeRoute");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CallXML> parse(String xml) throws Exception {		
		return (List<CallXML>)parseXml(xml);
	}
}
