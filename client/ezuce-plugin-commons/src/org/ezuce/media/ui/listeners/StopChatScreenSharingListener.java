package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.StreamingManager;
import org.jivesoftware.spark.util.log.Log;

/**
 * This will evolve in receiver and transmitter call screen sharing when the AudioVideoScreenSharing
 * panel will be split into pieces
 *
 */
public class StopChatScreenSharingListener extends AbstractStopScreenSharingListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			StreamingManager strManager = StreamingManager.getInstance();
			boolean transmitting = strManager.isInviting();
			if (isCanReject()) {
				strManager.rejectReceiving();
			} else {
				strManager.stopScreenSharing();
			}
			removeScreenSharingUI(transmitting);
		} catch (Exception ex) {
			Log.error("Cannot stop screen sharing", ex);
		}
	}
}
