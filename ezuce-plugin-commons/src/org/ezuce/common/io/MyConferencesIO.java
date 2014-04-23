package org.ezuce.common.io;

import java.util.List;

import org.ezuce.common.xml.MyConferenceXML;

public class MyConferencesIO extends XStreamIO {
	@Override
	public void setAliases() {
    	setAlias("conferences", List.class);
    	setAlias("conference", MyConferenceXML.class);    	    	    	    
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyConferenceXML> parse(String xml) throws Exception {		
		return (List<MyConferenceXML>)parseXml(xml);
	}
}
