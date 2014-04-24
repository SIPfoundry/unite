package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.xmlpull.v1.XmlPullParser;

public class Body extends IXmlElement<Body> {

	public static class Attr {
		public static final String secs = "secs";
		public static final String utc = "utc";
		public static final String jid = "jid";
	}

	private static final MessageFormat gFromTag = new MessageFormat(
			"<from {0} {1} {2}><body>{3}</body></from>");
	private static final MessageFormat gToTag = new MessageFormat(
			"<to {0} {1} {2}><body>{3}</body></to>");

	private int sec;
	private String body;
	private String from;
	private String to;
	private String utc;
	private String name;
	private String jid;
	private boolean event;

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getJid() {
		return jid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUtc() {
		return utc;
	}

	public void setUtc(String utc) {
		this.utc = utc;
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toXml() {
		MessageFormat xml = isFrom() ? gFromTag : gToTag;
		String time = getUtc() != null ? asString(getUtc(), Attr.utc)
				: asString(getSec(), Attr.secs);
		return xml.format(new String[] { time, asString(getName(), "name"),
				asString(getJid(), Attr.jid), asString(getBody()) });
	}

	@Override
	public Body fromXml(XmlPullParser parser) {
		String with = parser.getAttributeValue(null, Attr.secs);
		String body = "";
		// TODO:
		return from(with, body);
	}

	private Body from(String with, String body) {
		return null;
	}

	@Override
	public String toString() {
		return toXml();
	}

	public boolean isFrom() {
		return from != null && !from.isEmpty();
	}

	public void setEvent(boolean event) {
		this.event = event;
	}
	
	public boolean isEvent() {
		return event;
	}
}
