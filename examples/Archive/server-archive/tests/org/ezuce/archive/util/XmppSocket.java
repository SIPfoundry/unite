package org.ezuce.archive.util;

import org.eclipse.jetty.websocket.WebSocket;

public class XmppSocket implements WebSocket {

	@Override
	public void onClose(int arg0, String arg1) {
		System.out.println("== close ");
	}

	@Override
	public void onOpen(Connection arg0) {
		System.out.println("== open ==");
	}

}
