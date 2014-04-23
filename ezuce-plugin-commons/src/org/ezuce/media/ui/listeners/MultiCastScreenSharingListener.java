package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.StreamingManager;
import org.ezuce.media.manager.UIMediaManager;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.util.log.Log;

public class MultiCastScreenSharingListener implements ActionListener {
	private ScreenSharingInvitees m_inviteesObject;
	
	public MultiCastScreenSharingListener(ScreenSharingInvitees inviteesObject) {
		m_inviteesObject = inviteesObject;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			StreamingManager strManager = StreamingManager.getInstance();
			if (strManager.isScreenSharingEnabled()) {
				strManager.stopScreenSharing();
			} else {
				strManager.initializeScreenSharing(m_inviteesObject.getInvitees());
			}
		} catch (Exception ex) {
			Log.error("Screen sharing performing failed", ex);
		}		
	}

}