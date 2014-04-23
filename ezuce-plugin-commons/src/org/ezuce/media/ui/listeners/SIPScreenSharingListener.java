package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.manager.UIMediaManager;

public class SIPScreenSharingListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		PhoneManager.getInstance().enableOrDisableScreenSharing();
	}

}