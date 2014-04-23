package org.ezuce.im;

import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatFrame;

public class IMCommons {
	private static final int MINIMUM_WIDTH = 643;
	public static void setChatFrameMinimumSize() {
    	ChatManager chatManager = SparkManager.getChatManager();
    	ChatFrame chatFrame = chatManager.getChatContainer().getChatFrame();
        if (chatFrame.getWidth() < MINIMUM_WIDTH) {
        	chatFrame.setSize(MINIMUM_WIDTH, chatFrame.getHeight());               
        } 
	}
}
