package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.ezuce.archive.model.Otr;
import org.ezuce.archive.model.Save;
import org.xmlpull.v1.XmlPullParser;

/**
 * The <item/> element specifies the settings for both the OTR Mode and Save
 * Mode with regard to a particular entity. The element MUST be empty and MUST
 * include a 'jid' attribute, an 'otr' attribute, and a 'save' attribute. The
 * element MAY include an 'expire' attribute.
 * */
public class Item extends IXmlElement<Item> {

	private static final MessageFormat gXml = new MessageFormat(
			"<item jid=\"{0}\" {1} {2} {3}/>");

	public static class Attr {
		public static final String expire = "expire"; // optional
		public static final String jid = "jid";
		public static final String otr = "otr";
		public static final String save = "save";
	}

	public static final String Element = "item";

	private String jid;
	private Otr otr;
	private Save save;
	private Long expire;

	public String getJid() {
		return jid;
	}

	public Otr getOtr() {
		return otr;
	}

	public Save getSave() {
		return save;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public void setOtr(Otr otr) {
		this.otr = otr;
	}

	public void setSave(Save save) {
		this.save = save;
	}

	@Override
	public String toXml() {
		return gXml.format(new String[] { asString(getJid()),
				asString(getOtr(), Attr.otr), asString(getSave(), Attr.save),
				asString(getExpire(), Attr.expire) });
	}

	@Override
	public Item fromXml(XmlPullParser parser) {
		String expire = parser.getAttributeValue(null, Item.Attr.expire);
		String jid = parser.getAttributeValue(null, Item.Attr.jid);
		String otr = parser.getAttributeValue(null, Item.Attr.otr);
		String save = parser.getAttributeValue(null, Item.Attr.save);
		return from(expire, jid, otr, save);
	}

	public Item from(String expire, String jid, String otr, String save) {
		if (expire != null)
			this.expire = Long.parseLong(expire);
		this.jid = jid;
		this.otr = Otr.parseOtr(otr);
		this.save = Save.parseSave(save);
		return this;
	}
}