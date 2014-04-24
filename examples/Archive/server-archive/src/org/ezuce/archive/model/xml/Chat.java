package org.ezuce.archive.model.xml;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

public class Chat extends IXmlElement<Chat> {

	public static class Attr {
		public static final String with = "with";
		public static final String start = "start";
		public static final String thread = "thread";
		public static final String subject = "subject";
		public static final String version = "version";
	}

	public static final String Element = "chat";

	private static final MessageFormat gXml = new MessageFormat(
			"<{0} with=\"{1}\" start=\"{2}\" {3} {4} {5} >{6} {7} {8} {9} {10}</{0}>");

	// specifies the start time of the conversation thread, which MUST be UTC
	private long start;

	// optional
	private String subject;

	// A client SHOULD include a thread ID in each <message/> element it sends
	// that is part of a conversation it expects will be archived. RECOMMENDED.
	private String threadId;

	private int version; // REQUIRED

	// specifies the JID with which the messages were exchanged. REQUIRED.
	private String with;

	private List<Body> conversation;

	private Note note;// OPTIONAL.

	private Chat previous;

	private Chat next;

	private Data data;

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	public void setPrevious(Chat previous) {
		this.previous = previous;
	}

	public Chat getPrevious() {
		return previous;
	}

	public void setNext(Chat next) {
		this.next = next;
	}

	public Chat getNext() {
		return next;
	}

	public long getStart() {
		return start;
	}

	public String getStartUTC() {
		return asUTCString(getStart());
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setUTCStart(String start) {
			setStart(fromUTCString(start));
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getWith() {
		return with;
	}

	public void setWith(String with) {
		this.with = with;
	}

	public List<Body> getConversation() {
		return conversation;
	}

	public void setConversation(List<Body> fromTo) {
		this.conversation = fromTo;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	@Override
	public String toXml() {
		StringBuilder conversation = new StringBuilder();
		if (getConversation() != null) {
			for (Body body : getConversation()) {
				if (body == null)
					continue;

				conversation.append(body.toXml());
			}
		}

		// order
		// previous
		// next
		// <x xmlns='jabber:x:data' type='submit'>
		return gXml.format(new String[] { Element, asString(getWith()),
				asUTCString(getStart()),
				asString(getThreadId(), Attr.thread),
				asString(getSubject(), Attr.subject),
				asString(getVersion(), Attr.version),
				// body
				asString(getPrevious()), asString(getNext()),
				asString(getData()), asString(conversation),
				asString(getNote()) });
	}

	@Override
	public Chat fromXml(XmlPullParser parser) {
		String with = parser.getAttributeValue(null, Attr.with);
		String start = parser.getAttributeValue(null, Attr.start);
		String thread = parser.getAttributeValue(null, Attr.thread);
		String subject = parser.getAttributeValue(null, Attr.subject);
		String version = parser.getAttributeValue(null, Attr.version);

		System.out.println("== fromXml chat =");

		return from(with, start, thread, subject, version);
	}

	public Chat from(String with, String start, String thread, String subject,
			String version) {
		this.with = with;
		try {
			this.start = gUtcFormat.parse(start).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.threadId = thread;
		this.subject = subject;
		if (version != null)
			this.version = Integer.parseInt(version);
		return this;
	}

	@Override
	public String toString() {
		return toXml();
	}

}
