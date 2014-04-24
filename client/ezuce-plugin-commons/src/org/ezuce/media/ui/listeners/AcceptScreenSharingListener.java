package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.StreamingManager;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.jivesoftware.spark.util.log.Log;

public class AcceptScreenSharingListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			StreamingManager strManager = StreamingManager.getInstance();
			StreamingManager.getInstance().acceptReceiving(strManager.getReceiverMediaChannel());
		} catch (Exception ex) {
			Log.error("Exception accepting screen sharing ", ex);
		}
	}
}