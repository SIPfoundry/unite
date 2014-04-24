package org.ezuce.common.resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SipUriUtil {
    private static final Pattern EXTRACT_USER_RE = Pattern.compile("\\s*<?(?:sip:)?(.+?)[@;].+");
    private static final Pattern EXTRACT_FULL_USER_RE = Pattern
            .compile("\\s*(?:\"?\\s*([^\"<]+?)\\s*\"?)?\\s*<(?:sip:)?(.+?)[@;].+");

    public static String extractUser(String uri) {
        if (uri == null) {
            return null;
        }
        Matcher full = EXTRACT_FULL_USER_RE.matcher(uri);
        if (full.matches()) {
            return full.group(2);
        }

        Matcher matcher = EXTRACT_USER_RE.matcher(uri);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * Extracts user name if available. Otherwise it returns the user id
     */
    public static String extractUserName(String uri) {
        if (uri == null) {
            return null;
        }
        Matcher matcher = EXTRACT_FULL_USER_RE.matcher(uri);
        if (!matcher.matches()) {
            matcher = EXTRACT_USER_RE.matcher(uri);
            if (matcher.matches()) {
                return matcher.group(1);
            }
            return null;
        }
        String fullName = matcher.group(1);
        String userId = matcher.group(2);

        if (fullName == null || fullName.equals(userId)) {
            return userId;
        }
        return fullName;
    }
}
