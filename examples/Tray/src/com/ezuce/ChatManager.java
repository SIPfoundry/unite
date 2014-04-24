package com.ezuce;

import com.ezuce.tray.TrayManager;

public class ChatManager {

	private static ChatManager instance = new ChatManager();

	public static ChatManager getInstance() {
		return instance;
	}

	public void addChatMessageHandler(ChatMessageHandlerImpl chatMessageHandler) {
		// TODO Auto-generated method stub

	}

	public void addChatListener(ChatManager trayManager) {
		// TODO Auto-generated method stub

	}

	public void addChatListener(TrayManager trayManager) {
		// TODO Auto-generated method stub

	}

}
