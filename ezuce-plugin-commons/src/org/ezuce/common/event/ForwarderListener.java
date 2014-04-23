package org.ezuce.common.event;

import java.awt.AWTEvent;

public interface ForwarderListener {
	void missedCallsClicked(AWTEvent evt);

	void voiceMessageClicked(AWTEvent evt);
}
