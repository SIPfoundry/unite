package org.ezuce.common.io;

import org.ezuce.common.xml.LoginDetailsXML;

public final class LoginDetailsIO extends XStreamIO {	

	@Override
	public void setAliases() {
		setAlias("login-details", LoginDetailsXML.class);
	}

	@Override
	public LoginDetailsXML parse(String xml) throws Exception {
		return (LoginDetailsXML)parseXml(xml);
	}	
}
