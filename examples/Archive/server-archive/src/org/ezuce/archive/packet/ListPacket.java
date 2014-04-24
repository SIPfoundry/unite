package org.ezuce.archive.packet;

import static org.ezuce.archive.model.xml.IXmlElement.asString;
import static org.ezuce.archive.model.xml.IXmlElement.asUTCString;

import java.text.MessageFormat;

import org.ezuce.archive.model.xml.Set;

public class ListPacket extends ArchivePacket {

	public static final String Element = "list";

	public static class Attr {
		public static final String with = "with";
		public static final String start = "start";
		public static final String end = "end";
	}

	private static final MessageFormat gXml = new MessageFormat("<" + Element
			+ " xmlns=\"{0}\" {1} {2} {3} >{4}</" + Element + ">");

	private String with;
	private String startUTC;
	private String endUTC;

	private Set set;

	public void setSet(Set set) {
		this.set = set;
	}

	public Set getSet() {
		return set;
	}

	public String getWith() {
		return with;
	}

	public void setWith(String with) {
		this.with = with;
	}

	public String getStartUTC() {
		return startUTC;
	}

	public void setStartUTC(String startUTC) {
		this.startUTC = startUTC;
	}

	public void setStart(Long start) {
		setStartUTC(asUTCString(start));
	}

	public String getEndUTC() {
		return endUTC;
	}

	public void setEndUTC(String endUTC) {
		this.endUTC = endUTC;
	}

	public void setEnd(Long end) {
		setEndUTC(asUTCString(end));
	}

	@Override
	public String getChildElementXML() {
		return gXml.format(new String[] { gXmlns,
				asString(getWith(), Attr.with),
				asString(getStartUTC(), Attr.start),
				asString(getEndUTC(), Attr.end), asString(getSet()) });
	}

}
