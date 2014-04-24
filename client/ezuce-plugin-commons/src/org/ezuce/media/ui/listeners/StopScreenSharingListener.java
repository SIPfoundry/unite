package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;

import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.manager.StreamingManager;
import org.jivesoftware.spark.util.log.Log;
/**
 * This will evolve in receiver and transmitter call screen sharing when the AudioVideoScreenSharing
 * panel will be split into pieces
 *
 */
public class StopScreenSharingListener extends AbstractStopScreenSharingListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			PhoneManager phManager = PhoneManager.getInstance();
			boolean transmitting = false;
			if (phManager.getConnectedCall() != null && phManager.getConnectedCallFullJID() == null) {
				PhoneManager.getInstance().disableScreenSharing();
			} else {
				StreamingManager strManager = StreamingManager.getInstance();
				if (isCanReject()) {
					strManager.rejectReceiving();
				} else {
					transmitting = strManager.isInviting();
					strManager.stopScreenSharing();					
				}
			}
			removeScreenSharingUI(transmitting);
		} catch (Exception ex) {
			Log.error("Cannon stop screen sharing", ex);
		}
	}
}
