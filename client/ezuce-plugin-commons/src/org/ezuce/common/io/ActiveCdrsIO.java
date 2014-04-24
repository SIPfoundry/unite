package org.ezuce.common.io;

import java.util.List;

import org.ezuce.common.xml.ActiveCdrXML;

public class ActiveCdrsIO extends XStreamIO {
	@Override
	public void setAliases() {
    	setAlias("cdrs", List.class);
    	setAlias("cdr", ActiveCdrXML.class);    	
    	setAliasField("from-aor", ActiveCdrXML.class, "fromAor");
    	setAliasField("to-aor", ActiveCdrXML.class, "toAor");    	
    	setAliasField("start-time", ActiveCdrXML.class, "startTime");    	    	    	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveCdrXML> parse(String xml) throws Exception {		
		return (List<ActiveCdrXML>)parseXml(xml);
	}
}