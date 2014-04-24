package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.ezuce.archive.model.Otr;
import org.ezuce.archive.model.Save;
import org.xmlpull.v1.XmlPullParser;

/**
 * The <default/> element specifies the default settings for both OTR Mode and
 * Save Mode
 * */
public class Default extends IXmlElement<Default> {

	private static final MessageFormat gXml = new MessageFormat(
			"<default otr=\"{0}\" save=\"{1}\" {2} {3}/>");

	public static class Attr {
		public static final String expire = "expire"; // optional
		public static final String unset = "unset";// optional
		public static final String otr = "otr";
		public static final String save = "save";
	}

	public static final String Element = "default";

	private Long expire;
	private Otr otr;
	private Save save;
	private Boolean unset;

	public Long getExpire() {
		return expire;
	}

	public Otr getOtr() {
		return otr;
	}

	public Save getSave() {
		return save;
	}

	public Boolean getUnset() {
		return unset;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public void setOtr(Otr otr) {
		this.otr = otr;
	}

	public void setSave(Save save) {
		this.save = save;
	}

	public void setUnset(Boolean unset) {
		this.unset = unset;
	}

	@Override
	public String toXml() {
		return gXml.format(new String[] { asString(getOtr()),
				asString(getSave()), asString(getExpire(), Attr.expire),
				asString(getUnset(), Attr.unset) });
	}

	@Override
	public Default fromXml(XmlPullParser parser) {
		String expire = parser.getAttributeValue(null, Default.Attr.expire);
		String otr = parser.getAttributeValue(null, Default.Attr.otr);
		String save = parser.getAttributeValue(null, Default.Attr.save);
		String unset = parser.getAttributeValue(null, Default.Attr.unset);
		return from(otr, save, expire, unset);
	}

	public Default from(String otr, String save, String expire, String unset) {
		this.otr = Otr.parseOtr(otr);
		this.save = Save.parseSave(save);
		if (expire != null)
			this.expire = Long.parseLong(expire);
		if (unset != null)
			this.unset = Boolean.parseBoolean(unset);

		return this;
	}
}