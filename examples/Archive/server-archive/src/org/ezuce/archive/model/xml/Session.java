package org.ezuce.archive.model.xml;

import java.text.MessageFormat;

import org.ezuce.archive.model.Otr;
import org.ezuce.archive.model.Save;
import org.xmlpull.v1.XmlPullParser;

/**
 * The <session/> element specifies the settings for Save Mode with regard to a
 * particular chat session. The element MUST be empty and MUST include a
 * 'thread' attribute, and a 'save' attribute. The element MAY include a
 * 'timeout' attribute. Server implementations SHOULD remove all <session/>
 * elements when stream is closed.
 * */
public class Session extends IXmlElement<Session> {

	private static final MessageFormat gXml = new MessageFormat(
			"<session thread=\"{0}\" {1} {2} {3}/>");

	public static class Attr {
		public static final String thread = "thread";
		public static final String save = "save";
		public static final String timeout = "timeout"; // optional
		public static final String otr = "otr";
	}

	public static final String Element = "session";

	private String thread;
	private Save save;
	private Long timeout;
	private Otr otr;

	public String getThread() {
		return thread;
	}

	public Save getSave() {
		return save;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public void setSave(Save save) {
		this.save = save;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public Otr getOtr() {
		return otr;
	}

	public void setOtr(Otr otr) {
		this.otr = otr;
	}

	@Override
	public String toXml() {
		return gXml.format(new String[] { asString(getThread()),
				asString(getSave(), Attr.save),
				asString(getTimeout(), Attr.timeout),
				asString(getOtr(), Attr.otr) });
	}

	@Override
	public Session fromXml(XmlPullParser parser) {
		String thread = parser.getAttributeValue(null, Session.Attr.thread);
		String save = parser.getAttributeValue(null, Session.Attr.save);
		String timeout = parser.getAttributeValue(null, Session.Attr.timeout);
		return from(thread, save, timeout);
	}

	private Session from(String thread, String save, String timeout) {
		this.thread = thread;
		this.save = Save.parseSave(save);
		if (timeout != null)
			this.timeout = Long.parseLong(timeout);

		return this;
	}
}