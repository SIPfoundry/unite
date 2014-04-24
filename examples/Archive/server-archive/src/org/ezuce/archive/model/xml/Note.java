package org.ezuce.archive.model.xml;

import java.text.MessageFormat;
import java.text.ParseException;

import org.xmlpull.v1.XmlPullParser;

public class Note extends IXmlElement<Note> {

	private static final MessageFormat gXml = new MessageFormat(
			"<note utc=\"{0}\">{1}</note>");

	public static class Attr {
		public static final String utc = "utc";
	}

	private String mNote;
	private Long mUtc;

	@Override
	public String toXml() {
		return gXml.format(new String[] { asUTCString(getUtc()),
				asString(getNote()) });
	}

	@Override
	public Note fromXml(XmlPullParser parser) {
		String utc = parser.getAttributeValue(null, Attr.utc);
		String note = parser.getAttributeValue(null, Attr.utc);
		return from(utc, note);
	}

	public Note from(String utc, String note) {
		setNote(note);
		try {
			setUtc(gUtcFormat.parse(utc).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getNote() {
		return mNote;
	}

	public void setNote(String mNote) {
		this.mNote = mNote;
	}

	public Long getUtc() {
		return mUtc;
	}

	public void setUtc(Long utc) {
		this.mUtc = utc;
	}

	@Override
	public String toString() {
		return toXml();
	}

}
