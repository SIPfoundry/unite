package org.ezuce.common.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ezuce.common.resource.Utils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public abstract class MessageNotifier {
	private Pattern ENTERED_CONFERENCE = Pattern.compile(".* \\(.*\\) .* \\[[0-9]*\\] at .*");
	private Pattern LEFT_CONFERENCE = Pattern.compile(".* \\(.*\\) [^\\[\\]]*");
	
	public abstract void userLeftMyConference(String message);
	public abstract void userEnteredMyConference(String message);
	public abstract void userEnteredConference(String message);
	public abstract void userLeftConference(String message);
	
	public void processMessage(ChatRoom chat, Message message) {
		LocalPreferences pref = SettingsManager.getLocalPreferences();
		String myBuddyId = pref.getProperties().getProperty("mybuddy.id");
		if (myBuddyId == null) {
			myBuddyId = "MyBuddy";
			pref.getProperties().setProperty("mybuddy.id", myBuddyId);
			SettingsManager.saveSettings();
		}
		
        String from = message.getFrom();
        String clientId = Utils.getImId(from);
        String body = message.getBody();
        Matcher matcherEnter = null;
        Matcher matcherLeft = null;
        if (body != null) {
        	matcherEnter = ENTERED_CONFERENCE.matcher(body);
        	matcherLeft = LEFT_CONFERENCE.matcher(body);
        }
        if (clientId.equalsIgnoreCase(myBuddyId)) {
        	if (matcherEnter!=null && matcherEnter.matches() && matcherEnter.group(0).equals(body)) {
        		userEnteredMyConference(body);
        	}
        	if (matcherLeft != null && matcherLeft.matches() && matcherLeft.group(0).equals(body)) {
        		userLeftMyConference(body);
        	}
        } else {
        	if (matcherEnter!=null && matcherEnter.matches() && matcherEnter.group(0).equals(body)) {
        		userEnteredConference(body);
        	}
        	if (matcherLeft != null && matcherLeft.matches() && matcherLeft.group(0).equals(body)) {
        		userLeftConference(body);
        	}
        }
	}
}
