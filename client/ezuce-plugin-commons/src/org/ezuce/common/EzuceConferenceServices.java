package org.ezuce.common;

import org.ezuce.common.EzuceConferenceUtils;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.bookmark.BookmarkedConference;
import org.jivesoftware.spark.ui.conferences.BookmarksUI;
import org.jivesoftware.spark.ui.conferences.ConferenceServices;
import org.jivesoftware.spark.util.log.Log;

public class EzuceConferenceServices extends ConferenceServices {
	
    private static BookmarkedConferenceWrapper audioBookmarkedConference ;
    
    public EzuceConferenceServices() {
    	super();
    	audioBookmarkedConference = EzuceConferenceUtils.getAudioBookmarkedConf();
    }

	public static BookmarksUI getBookmarksUI() {
		return getBookmarkedConferences();
	}

	@Override
	protected void addBookmarksUI() {
		// disable adding bookmarks UI to the workspace
	}
	
	@Override
    protected BookmarkedConference getDefaultBookmark() {
		return getDefaultBookmarkToUse();
    }

	public static BookmarkedConference getDefaultBookmarkToUse() {
    	try {
    		return audioBookmarkedConference == null ? EzuceConferenceUtils.getBookmarkToUse() : audioBookmarkedConference.getBookmarkConference();
    	} catch (XMPPException ex) {
    		Log.warning("There is no bookmarked conference to use", ex);
    		return null;
    	}
	}
	
	public static String getDefaultBookmarkToUseExtension() {
		return audioBookmarkedConference == null ? null : audioBookmarkedConference.getExtension();
	}
	
	public static String getDefaultBookmarkToUseName() {
		return audioBookmarkedConference == null ? null : audioBookmarkedConference.getName();
	}
}
