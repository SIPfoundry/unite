package org.ezuce.common.io;

import java.util.List;

import org.jivesoftware.spark.ui.status.CustomStatusItem;

public class CustomMessageIO extends XStreamIO {

	@Override
	public void setAliases() {
		setAlias("custom-items", List.class);
		setAlias("custom-status", CustomStatusItem.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object parse(String xml) throws Exception {
		return (List<CustomStatusItem>) parseXml(xml);
	}

	@Override
	public String toXml(Object objectToParse) {
		return super.toXml(objectToParse);
	}
}
