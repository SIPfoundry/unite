package org.ezuce.video.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnOffHoldCallListener implements ActionListener {
	private String m_callId;

	public OnOffHoldCallListener(String callId) {
		m_callId = callId;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

}
