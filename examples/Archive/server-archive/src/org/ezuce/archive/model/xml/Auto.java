package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.ezuce.archive.model.Scope;
import org.xmlpull.v1.XmlPullParser;

/**
 * The <auto/> element specifies the current Automatic Archiving settings for
 * this stream.
 * */
public class Auto extends IXmlElement<Auto> {

	public static final String Element = "auto";

	public static class Attr {
		public static final String save = "save";
		public static final String scope = "scope"; // optional
		public static final String xmlns = "xmlns"; // optional
	}

	private static final MessageFormat gXml = new MessageFormat(
			"<auto save=\"{0}\" {1} {2}/>");

	private boolean save;
	private Scope scope;
	private String xmlns;

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public Scope getScope() {
		return scope;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	@Override
	public String toXml() {
		return gXml.format(new String[] { asString(isSave()),
				asString(getScope(), Attr.scope),
				asString(getXmlns(), Attr.xmlns) });
	}

	@Override
	public Auto fromXml(XmlPullParser parser) {
		String save = parser.getAttributeValue(null, Auto.Attr.save);
		String scope = parser.getAttributeValue(null, Auto.Attr.scope);
		String xmlns = parser.getAttributeValue(null, Auto.Attr.xmlns);
		return from(save, scope, xmlns);
	}

	public Auto from(String save, String scope, String xmlns) {
		this.save = Boolean.parseBoolean(save);
		this.scope = Scope.parseScope(scope);
		this.xmlns = xmlns;
		return this;
	}

}