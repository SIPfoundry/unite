package org.ezuce.archive;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.ezuce.archive.ui.util.ArchiveUtil;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class Util {

	private static final int gPort = 5222;
	private static final String gServer = "openuc.ezuce.com";
	private static final String gUsername = "2066";
	private static final String gPassword = "B5Ph3qKN";

	public static final String gCurrentUser = "2066@ezuce.com";
	public static final String gWith = "2067@ezuce.com";

	private static final MessageFormat gMessagesItem = new MessageFormat(""
			+ "<message>" + " <to>{0}</to>" + " <from>{1}</from>"
			+ "<event>{2}</event>" + " <body>{3}</body>" + " <date>{4}</date>"
			+ "</message>" + "\n");
	private static final MessageFormat gTranscript = new MessageFormat(""
			+ "<transcript>" + " 	<messages>" + "		{0}   " + " 	</messages>"
			+ "</transcript>");

	public static void connect() throws XMPPException {
		ConnectionConfiguration config = new ConnectionConfiguration(gServer,
				gPort);
		config.setDebuggerEnabled(true);
		Connection connection = new XMPPConnection(config);
		connection.connect();
		connection.login(gUsername, gPassword);

		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus("Test client");
		connection.sendPacket(presence);
	}

	public static void saveTodayHistory(int minItems, int maxItems) {
		int days = 4;
		StringBuilder str = new StringBuilder();
		Calendar cal = Calendar.getInstance();

		// #1
		cal.add(Calendar.DATE, -days);
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		System.out.println("= Add days for " + cal.getTime());
		addMessages(str, cal, minItems, maxItems);

		// #2
		cal.add(Calendar.DATE, days - 1);
		cal.set(Calendar.HOUR_OF_DAY, 11);
		System.out.println("= Add days for " + cal.getTime());
		addMessages(str, cal, minItems, maxItems);

		// #3
		cal.add(Calendar.DATE, +1);
		cal.set(Calendar.HOUR_OF_DAY, 15);
		System.out.println("= Add days for " + cal.getTime());
		addMessages(str, cal, minItems, maxItems);

		// save
		String transcript = gTranscript.format(new String[] { str.toString() });
		File historyFile = new File(getUserHomeDir(), "transcripts"
				+ File.separator + "2067@ezuce.com.xml");

		try {
			FileUtils.writeStringToFile(historyFile, transcript);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected static void addMessages(StringBuilder str, Calendar cal,
			int minItems, int maxItems) {
		for (int i = 0; i < random(minItems, maxItems); i++) {
			boolean even = i % 2 == 0;
			boolean odd = !even;

			cal.add(Calendar.MINUTE, random(0, 9));
			str.append(gMessagesItem.format(new String[] {
					even ? gWith : gCurrentUser, odd ? gWith : gCurrentUser,
					i % 10 == 0 ? "true" : "false", "body" + i,
					ArchiveUtil.gMessageItemDate.format(cal.getTime()) }));
		}
	}

	public static int random(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	public static String getUserHomeDir() {
		String dir = MainClient.class
				.getClassLoader()
				.getResource(
						MainClient.class.getName().replace(".", File.separator)
								+ ".class").toString();
		dir = dir.substring(0, dir.lastIndexOf(File.separator));
		dir = dir.substring(dir.lastIndexOf(":") + 1);
		return dir;
	}
}
