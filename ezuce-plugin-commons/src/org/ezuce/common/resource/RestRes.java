package org.ezuce.common.resource;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


public class RestRes {
    private static PropertyResourceBundle prb;

    public static final String CALL_PORT = "call.port";
    public static final String MISSED_CALLS_PORT = "historycalls.port";
    public static final String VOICEMAIL_PORT = "voicemail.port";
    public static final String SIPXCONFIG_PORT = "sipxconfig.port";

    public static final String CALL_URI = "call.uri";
    public static final String CALL_FORWARD_FINDME_URI = "call.forward.findme.uri";
    public static final String HISTORY_CALLS_URI = "historycalls.uri";
    public static final String VOICEMAIL_MESSAGES_URI = "voicemail.messages.uri";
    public static final String VOICEMAIL_MAILBOX = "voicemail.mailbox.uri";
    public static final String VOICEMAIL_MARK_HEARD = "voicemail.mark.heard";
    public static final String VOICEMAIL_DELETE = "voicemail.delete";
    public static final String VOICEMAIL_MOVE = "voicemail.move";
    public static final String PHONE_BOOK_URI="phonebook.uri";
    public static final String PHONE_BOOK_SEARCH_URI="phonebook.search.uri";
    public static final String CONFERENCE_LIST_URI="conferenceList.uri";
    public static final String CONTACT_INFO_URI="contactInfo.uri";
    public static final String LOGIN_DETAILS_URI="loginDetails.uri";
    
    public static final String CONFERENCE_LIST_USERS="conference.list.user";
    public static final String CONFERENCE_VOLUME_IN_URI="conference.volumeIn";
    public static final String CONFERENCE_VOLUME_OUT_URI="conference.volumeOut";
    public static final String CONFERENCE_KICK_URI="conference.kickUser";
    public static final String CONFERENCE_KICK_ALL_URI="conference.kickAllUsers";
    public static final String CONFERENCE_MUTE_URI="conference.muteUser";
    public static final String CONFERENCE_UNMUTE_URI="conference.unmuteUser";
    public static final String CONFERENCE_DEAF_URI="conference.deafUser";
    public static final String CONFERENCE_UNDEAF_URI="conference.undeafUser";
    
    public static final String CONFERENCE_MUTE_ALL_URI="conference.muteAll";
    public static final String CONFERENCE_UNMUTE_ALL_URI="conference.unmuteAll";
    public static final String CONFERENCE_DEAF_ALL_URI="conference.deafAll";
    public static final String CONFERENCE_UNDEAF_ALL_URI="conference.undeafAll";
    
    public static final String CONFERENCE_START_RECORD_URI="conference.startRecord";
    public static final String CONFERENCE_STOP_RECORD_URI="conference.stopRecord";
    public static final String CONFERENCE_STATUS_URI="conference.recordStatus";
    
    public static final String OPENUC_URI="openuc.uri";

    public static final String ACTIVE_CALL_URI="activeCall.uri";
    public static final String TRANSFER_CALL_URI="transferCall.uri";
    public static final String INVITE_TO_BRIDGE_URI="inviteToBridge.uri";
    public static final String INVITE_TO_BRIDGE_URI_IM="inviteToBridge.uri.im";
    
    public static final String USERS_PER_PAGE = "usersPerPage";

    static ClassLoader cl = RestRes.class.getClassLoader();

    static {
        prb = (PropertyResourceBundle)ResourceBundle.getBundle("org/ezuce/common/resource/rest");
    }

    public static String getValue(String key) {
        return prb.getString(key);
    }

    public static String getValue(String key, Object... params  ) {
        return MessageFormat.format(prb.getString(key), params);
    }
}
