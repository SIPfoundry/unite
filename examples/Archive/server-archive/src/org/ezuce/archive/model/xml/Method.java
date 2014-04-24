package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.ezuce.archive.model.Type;
import org.ezuce.archive.model.Use;
import org.xmlpull.v1.XmlPullParser;

/**
 * Each <method/> element specifies the the user's preferences for one available
 * archiving method. The <method/> element MUST be empty and MUST include both
 * the 'type' and 'use' attributes.
 * */
public class Method extends IXmlElement<Method> {

	private static final MessageFormat gXml = new MessageFormat(
			"<method type=\"{0}\" use=\"{1}\" />");

	public static class Attr {
		public static final String type = "type";
		public static final String use = "use";
	}

	public static final String Element = "method";

	public Type type;
	public Use use;

	public Type getType() {
		return type;
	}

	public Use getUse() {
		return use;
	}

	@Override
	public String toXml() {
		return gXml.format(new String[] { asString(getType()),
				asString(getUse()) });
	}

	@Override
	public Method fromXml(XmlPullParser parser) {
		String type = parser.getAttributeValue(null, Method.Attr.type);
		String use = parser.getAttributeValue(null, Method.Attr.use);
		return from(type, use);
	}

	public Method from(String type, String use) {
		this.type = Type.parseType(type);
		this.use = Use.parseUse(use);
		return this;
	}
}