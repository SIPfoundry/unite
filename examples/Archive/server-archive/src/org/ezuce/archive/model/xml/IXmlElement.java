package org.ezuce.archive.model.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

public abstract class IXmlElement<T> {

	public static final SimpleDateFormat gUtcFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	public static final String gUtcZero = "0000-01-01T00:00:00Z";
	public static final String gUtcFuture = "2099-01-01T00:00:00Z";

	private static final String gEmpty = "";
	private static final String gEq = "=";
	private static final String gQuote = "\"";

	public abstract String toXml();

	public abstract T fromXml(XmlPullParser parser);

	public static String asString(Object obj) {
		return obj == null ? gEmpty : obj.toString();
	}

	public static String asString(Object obj, String prefixKey) {
		if (obj == null)
			return gEmpty;

		StringBuilder xml = new StringBuilder(prefixKey);
		xml.append(gEq);
		xml.append(gQuote);
		xml.append(asString(obj));
		xml.append(gQuote);
		return xml.toString();
	}

	public static String asUTCString(Long time) {
		if (time == null)
			return null;

		return gUtcFormat.format(new Date(time));
	}

	public static Long fromUTCString(String utc) {
		if (utc == null)
			return null;
		try {
			return gUtcFormat.parse(utc).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
