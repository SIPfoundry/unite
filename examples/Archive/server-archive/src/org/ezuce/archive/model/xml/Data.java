package org.ezuce.archive.model.xml;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;

public class Data extends IXmlElement<Data> {

	public static final String Element = "x";
	public static final String Field = "field";

	public static class Attr {
		public static final String save = "save";
		public static final String scope = "scope"; // optional
	}

	private static final MessageFormat gXml = new MessageFormat("<" + Element
			+ "  xmlns='jabber:x:data' type=\"submit\">{0}</" + Element + ">");

	private static final MessageFormat gFieldXml = new MessageFormat("<"
			+ Field + " var='{0}'><value>{1}</value></" + Field + ">");

	private final Map<String, Object> mFieldMap = new HashMap<String, Object>();

	@Override
	public String toXml() {
		StringBuilder fields = new StringBuilder();
		for (Entry<String, Object> el : mFieldMap.entrySet()) {
			fields.append(gFieldXml.format(new String[] { el.getKey(),
					(String) el.getValue() }));
		}
		return gXml.format(new String[] { fields.toString() });
	}

	@Override
	public Data fromXml(XmlPullParser parser) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setField(String var, String value) {
		if (var == null)
			return;

		mFieldMap.put(var, value);
	}

	public Object getField(String var) {
		return mFieldMap.get(var);
	}

	public void removeField(String var) {
		mFieldMap.remove(var);
	}
}
