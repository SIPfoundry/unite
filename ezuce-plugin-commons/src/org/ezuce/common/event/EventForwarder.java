package org.ezuce.common.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import org.jivesoftware.spark.util.log.Log;

public class EventForwarder extends MouseAdapter {
	private static final EventForwarder INSTANCE = new EventForwarder();

	private final Set<ForwarderListener> listeners = new HashSet<ForwarderListener>();

	private EventForwarder() {
		// hide constructor
	}

	public static EventForwarder getInstance() {
		return INSTANCE;
	}

	public void addListener(ForwarderListener listener) {
		listeners.add(listener);
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		final String sourceName = evt.getComponent().getName();
		if (sourceName != null) {
			for (ForwarderListener listener : listeners) {
				if ("voiceMessages".equals(sourceName)) {
					listener.voiceMessageClicked(evt);
				} else if ("missedCalls".equals(sourceName)) {
					listener.missedCallsClicked(evt);
				} else {
					Log.warning("Unsupported command [" + sourceName + "]");
				}
			}
		}
	}

}
