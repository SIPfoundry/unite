package org.ezuce.common.io;

import java.util.List;

import org.ezuce.commons.ui.location.Location;

public class LocationIO extends XStreamIO {

	@Override
	public void setAliases() {
		setAlias("location-items", List.class);
		setAlias("location", Location.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object parse(String xml) throws Exception {
		return (List<Location>) parseXml(xml);
	}

	@Override
	public String toXml(Object objectToParse) {
		return super.toXml(objectToParse);
	}
}
