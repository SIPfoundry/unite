package com.ezuce;

import java.io.File;

/**
 * @author Vyacheslav Durin
 * 
 *         Apr 17, 2013
 * @version 0.1
 */
public class SparkManager {

	private static String sessionJID;

	public static void setSessionJID(String jid) {
		sessionJID = jid;
	}

	public static File getUserDirectory() {
		String userHome = "";
		if (System.getenv("APPDATA") != null
				&& !System.getenv("APPDATA").equals("")) {
			userHome = System.getenv("APPDATA") + "/" + getUserConf();
		} else {
			userHome = System.getProperties().getProperty("user.home") + "/"
					+ getUserConf();
		}

		File userDirectory = new File(userHome, "/user/" + sessionJID);
		if (!userDirectory.exists()) {
			userDirectory.mkdirs();
		}
		return userDirectory;
	}

	private static String getUserConf() {
		// USER_DIRECTORY_WINDOWS = eZuce/Unite
		// USER_DIRECTORY_LINUX = .eZuce/Unite
		// USER_DIRECTORY_MAC = Library/Application Support/eZuce/Unite
		return ".eZuce/Unite";
	}
}
