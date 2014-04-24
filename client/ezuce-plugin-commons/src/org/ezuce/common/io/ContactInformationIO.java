package org.ezuce.common.io;

import org.ezuce.common.xml.AddressXML;
import org.ezuce.common.xml.ContactInformationXML;

public class ContactInformationIO extends XStreamIO {

	@Override
	public void setAliases() {
        setAlias("contact-information", ContactInformationXML.class);
        setAlias("homeAddress", AddressXML.class);
        setAlias("officeAddress", AddressXML.class);
        setAlias("branchAddress", AddressXML.class);		
	}

	@Override
	public Object parse(String xml) throws Exception {
		return (ContactInformationXML)parseXml(xml);
	}
}