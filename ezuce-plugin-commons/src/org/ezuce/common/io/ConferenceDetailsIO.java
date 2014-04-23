package org.ezuce.common.io;

import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.common.xml.ConferenceXML;
/**
 *
 * @author Razvan
 */
public class ConferenceDetailsIO extends XStreamIO {            
	@Override
	public void setAliases() {
        setAlias("conference", ConferenceXML.class);
        setAlias("member", ConferenceMemberXML.class); 		
	}

	@Override
	public ConferenceXML parse(String xml) throws Exception {
		return (ConferenceXML)parseXml(xml);
	}    
}
