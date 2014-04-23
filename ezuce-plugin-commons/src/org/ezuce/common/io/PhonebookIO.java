package org.ezuce.common.io;

import org.ezuce.common.xml.AddressXML;
import org.ezuce.common.xml.PhonebookContactInfoXML;
import org.ezuce.common.xml.PhonebookEntryXML;
import org.ezuce.common.xml.PhonebookXML;

public class PhonebookIO extends XStreamIO {

	@Override
	public void setAliases() {
    	setAlias("phonebook", PhonebookXML.class);
    	setAlias("entry", PhonebookEntryXML.class);
    	setAliasField("first-name", PhonebookEntryXML.class, "firstName");
    	setAliasField("last-name", PhonebookEntryXML.class, "lastName");
    	setAlias("contact-information", PhonebookContactInfoXML.class);
    	setAliasField("contact-information", PhonebookEntryXML.class, "phonebookContactInfoXML");
    	setAliasField("filtered-size", PhonebookXML.class, "filteredSize");
    	setAliasField("start-row", PhonebookXML.class, "startRow");
    	setAliasField("end-row", PhonebookXML.class, "endRow");
    	setAliasField("show-on-phone", PhonebookXML.class, "showOnPhone");
    	setAliasField("google-domain", PhonebookXML.class, "googleDomain");
    	setAlias("homeAddress", AddressXML.class);
    	setAlias("officeAddress", AddressXML.class);
	}

	@Override
	public PhonebookXML parse(String xml) throws Exception {
		return (PhonebookXML)parseXml(xml);
	}

}
