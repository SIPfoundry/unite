package com.ezuce.util;

import java.util.HashMap;
import java.util.Map;

public class Res {

	private static Map<String, String> texts = new HashMap<String, String>();
	static {
		texts.put("status.away", "Away");
		texts.put("status.custom.messages", "Custom messages");
		texts.put("status.do.not.disturb", "Do not disturb");
		texts.put("status.extended.away", "Extended away");
		texts.put("status.free.to.chat", "Free to chat");
		texts.put("status.on.phone", "On the phone");
		texts.put("status.online", "Online");
		texts.put("status.pending", "Pending");
		texts.put("status.invisible", "Invisible");
		texts.put("status.offline", "Offline");

		texts.put("menuitem.open", "Open");
		texts.put("menuitem.hide", "Hide");
		texts.put("menuitem.exit", "Exit");
		texts.put("menuitem.status", "Status");
		texts.put("menuitem.logout.no.status", "Logout");
	}

	public static String getString(String key) {
		return texts.get(key);
	}

}
