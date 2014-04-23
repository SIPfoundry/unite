package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.UnitemediaEventManager;
import org.ezuce.media.manager.UnitemediaRegistry;

import net.sf.fmj.media.Log;

public abstract class AbstractStopScreenSharingListener implements ActionListener {
	
	//Specifies if the screen sharing session was accepted or not. If not yet accepted, m_canReject is true and you can reject the session
	private boolean m_canReject;
	
	@Override
	public abstract void actionPerformed(ActionEvent arg0);

	public boolean isCanReject() {
		return m_canReject;
	}

	public void setCanReject(boolean canReject) {
		m_canReject = canReject;
	}
	
	public void removeScreenSharingUI(boolean transmitting) {
		Log.warning("Remove screen sharing UI");
		UnitemediaRegistry registry = UnitemediaRegistry.getInstance();
		UnitemediaEventManager umEventManager = UnitemediaEventManager.getInstance();
		if (transmitting) {
			umEventManager.setTransmittingXMPPVideo(false);
			registry.transmitterStreamEnded(null);
		} else {
			umEventManager.setReceivingXMPPVideo(false);
			registry.receiverStreamEnded(null);
		}
	}
}
