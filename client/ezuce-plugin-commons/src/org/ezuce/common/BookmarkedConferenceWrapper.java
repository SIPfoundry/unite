package org.ezuce.common;

import org.jivesoftware.smackx.bookmark.BookmarkedConference;

public class BookmarkedConferenceWrapper {
	private BookmarkedConference bookmarkConference;
	private String extension;
	private String name;
		
	public BookmarkedConferenceWrapper(BookmarkedConference bookmarkConference, String extension, String name) {
		this.bookmarkConference = bookmarkConference;
		this.extension = extension;
		this.name = name;
	}
	public BookmarkedConference getBookmarkConference() {
		return bookmarkConference;
	}
	public String getExtension() {
		return extension;
	}
	public String getName() {
		return name;
	}
}
