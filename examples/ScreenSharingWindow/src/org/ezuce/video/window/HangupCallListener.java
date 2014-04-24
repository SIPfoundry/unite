package org.ezuce.video.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HangupCallListener implements ActionListener {
	String m_callId;

	public HangupCallListener(String callId) {
		m_callId = callId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//
	}

}
