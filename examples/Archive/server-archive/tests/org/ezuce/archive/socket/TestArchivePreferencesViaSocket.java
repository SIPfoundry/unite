package org.ezuce.archive.socket;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.ezuce.archive.util.XmppSocket;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestArchivePreferencesViaSocket {

	private static Server server;
	private XMPPConnection connection;
	private static XmppSocket socket;

	@BeforeClass
	public static void startServer() throws Exception {
		socket = new XmppSocket();

		Server server = new Server(5222);
		WebSocketHandler wsHandler = new WebSocketHandler() {

			@Override
			public WebSocket doWebSocketConnect(HttpServletRequest req,
					String arg1) {
				System.out.println("====arg1=" + arg1);
				return socket;
			}
		};
		server.setHandler(wsHandler);
		server.start();
		System.out.println("Server started");
	}

	@AfterClass
	public static void stopServer() throws Exception {
		server.stop();
		System.out.println("Server stoped");
	}

	@Before
	public void setUp() throws Exception {
		ConnectionConfiguration conf = new ConnectionConfiguration("localhost",
				5222);
		conf.setDebuggerEnabled(true);
		connection = new XMPPConnection(conf);
		System.out.println("Connection to the server");
		connection.connect();
	}

	@After
	public void tearDown() throws Exception {
		if (connection != null)
			connection.disconnect();
	}

	@Test
	public void testConnection() throws Exception {
		System.out.println(connection);
	}
}
