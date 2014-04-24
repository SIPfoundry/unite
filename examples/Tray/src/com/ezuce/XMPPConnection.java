package com.ezuce;

import org.jivesoftware.smack.ConnectionListener;

public class XMPPConnection {
	
	private static XMPPConnection instance = new XMPPConnection();

	public static XMPPConnection getInstance() {
		return instance;
	}

	public ChatManager getChatManager() {
		return ChatManager.getInstance();
	}

	public void addConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
		
	}

}
