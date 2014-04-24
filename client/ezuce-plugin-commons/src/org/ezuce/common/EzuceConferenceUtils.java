package org.ezuce.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.common.xml.MyConferenceXML;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.bookmark.BookmarkedConference;
import org.jivesoftware.spark.ui.conferences.ConferenceUtils;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceConferenceUtils {

	/**
	 * Make sure that the default bookmarked conference set is the audio one - if any
	 */
	public static BookmarkedConferenceWrapper getAudioBookmarkedConf() {
		BookmarkedConferenceWrapper audioBookmarkedConference = null;
		try {
			Collection<BookmarkedConference> bookmarkedConfs = ConferenceUtils.retrieveBookmarkedConferences();
			Iterator<BookmarkedConference> confIterator = bookmarkedConfs.iterator();
			List<MyConferenceXML> conferences = retrieveOwnedConferencesList();
			if (conferences == null || conferences.isEmpty()) {
				return null;
			}
			BookmarkedConference bookmarkedConference = null;
			while (confIterator.hasNext()) {
				bookmarkedConference = confIterator.next();
				if (audioBookmarkedConference != null) {
					break;
				}
				for (MyConferenceXML conf : conferences) {
					if (bookmarkedConference.getName().equalsIgnoreCase(conf.getName())) {
						audioBookmarkedConference = new BookmarkedConferenceWrapper(bookmarkedConference, conf.getExtension(), conf.getName());
						break;
					}
				}				
			}
		} catch (Exception ex) {
			Log.warning("There is no audio bookmarked conference", ex);
		}
		return audioBookmarkedConference;
	}
	
    public static String getDefaultBookmarkedConferenceJid() {
        String bConfJid = SettingsManager.getLocalPreferences().getDefaultBookmarkedConf();
        return bConfJid;
    }
    
    public static void setDefaultBookmarkedConferenceJid(String bookmarkedConferenceJid) {
    	SettingsManager.getLocalPreferences().setDefaultBookmarkedConf(bookmarkedConferenceJid);
    }
    
	public static BookmarkedConference getBookmarkToUse() throws XMPPException {
		BookmarkedConference bookmarkedConference = null;
		Collection<BookmarkedConference> bookmarkedConfs = ConferenceUtils.retrieveBookmarkedConferences();
		if (bookmarkedConfs != null && !bookmarkedConfs.isEmpty()) {
			String implicitBookmarkedJID = getDefaultBookmarkedConferenceJid();

			// check if the "default" bookmarked conference is still in the
			// bookmarks list:
			if (implicitBookmarkedJID != null
					&& implicitBookmarkedJID.trim().length() > 0) {
				for (BookmarkedConference bc : bookmarkedConfs) {
					if (implicitBookmarkedJID.equalsIgnoreCase(bc.getJid())) {
						bookmarkedConference = bc;
						break;
					}
				}
			}
			// if no match was found, or no "default" bookmark could be
			// retrieved-use the first bookmark:
			if (bookmarkedConference == null) {
				bookmarkedConference = bookmarkedConfs.iterator().next();
			}

		}

		return bookmarkedConference;
	}
	
	public static boolean isImUserInConference(List<ConferenceMemberXML> participants, String imId) {
		if (participants == null || participants.isEmpty()) {
			return false;
		}
		for (ConferenceMemberXML participant : participants) {
			//MIRCEA:
			if (StringUtils.equals(participant.getImId(), imId)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPhonebookUserInConference(List<ConferenceMemberXML> participants, String number) {
		if (participants == null || participants.isEmpty()) {
			return false;
		}
		String participantNumber = null;
		for (ConferenceMemberXML participant : participants) {
			participantNumber = participant.getName();
			if (StringUtils.equals(participantNumber, number)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isGroupChatAudio(String name) {
		try {
			List<MyConferenceXML> conferences = retrieveOwnedConferencesList();
			for (MyConferenceXML conference : conferences) {
				if (StringUtils.equalsIgnoreCase(conference.getName(), name)) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public static List<MyConferenceXML> retrieveOwnedConferencesList() throws Exception {
		return RestManager.getInstance().getConferences(true);
	}
}
