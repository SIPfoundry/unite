package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.xmlpull.v1.XmlPullParser;

public class Set extends IXmlElement<Set> {

	public static final String Element = "set";
	private static final String gXmlns = "http://jabber.org/protocol/rsm";

	public static class Attr {
		public static final String max = "max";
		public static final String after = "after";
		public static final String first = "first";
		public static final String last = "last";
		public static final String count = "count";
	}

	private static final MessageFormat gXml = new MessageFormat("<" + Element
			+ " xmlns=\"{0}\" {1} {2} {3} {4} {5}></" + Element + ">");

	private Integer max;
	private String after;
	private Integer first;
	private Integer last;
	private Integer count;

	public Set() {

	}

	public Set(String after, Integer limit) {
		setAfter(after);
		setMax(limit);
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getAfter() {
		return after;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Integer getFirst() {
		return first;
	}

	public void setLast(Integer last) {
		this.last = last;
	}

	public Integer getLast() {
		return last;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toXml() {
		return gXml.format(new String[] { gXmlns, asString(getMax(), Attr.max),
				asString(getAfter(), Attr.after),
				asString(getFirst(), Attr.first),
				asString(getLast(), Attr.last),
				asString(getCount(), Attr.count), });
	}

	@Override
	public Set fromXml(XmlPullParser parser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return toXml();
	}
}
