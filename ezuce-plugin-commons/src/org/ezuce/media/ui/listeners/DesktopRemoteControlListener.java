package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;
import org.jivesoftware.spark.util.log.Log;

public class DesktopRemoteControlListener implements ActionListener{
	private boolean m_enabled = false;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//TODO - this should be redesigned
		//PhoneManager.getInstance().enableDesktopRemoteControl(!m_enabled);
		m_enabled = !m_enabled;
	}
}
