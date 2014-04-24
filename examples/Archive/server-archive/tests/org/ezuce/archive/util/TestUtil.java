package org.ezuce.archive.util;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class TestUtil {

	private static final int gPort = 5222;
	// private static final String[] gCredential ={ "openuc.ezuce.com", "2066",
	// "B5Ph3qKN"};
	private static final String[] gCredential = { "localhost", "slava", "slava" };

	// private static final String[] gCredential = { "monk-vision.com",
	// "slava1", "slava1" };

	public static Connection connectReal() throws XMPPException {
		ConnectionConfiguration config = new ConnectionConfiguration(
				gCredential[0], gPort);
		config.setDebuggerEnabled(true);
		Connection connection = new XMPPConnection(config);
		connection.connect();
		connection.login(gCredential[1], gCredential[2]);

		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus("Test client");
		connection.sendPacket(presence);
		return connection;
	}

}
