package com.ezuce;

import org.jivesoftware.smack.packet.Presence;


public class SessionManager {

	private static final SessionManager instance = new SessionManager();

	public static SessionManager getInstance() {
		return instance;
	}

	public void changePresence(Presence presence) {
		// TODO: to be implemented
		System.out.println("TODO: SessionManager#changePresence()" );
		
	}

	public void addPresenceListener(PresenceListener presenceListener) {
		// TODO Auto-generated method stub
		
	}

}
