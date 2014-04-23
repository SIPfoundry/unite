package org.ezuce.call;

import org.ezuce.call.events.ForwarderListenerImpl;
import org.ezuce.common.event.EventForwarder;
import org.ezuce.common.event.ForwarderListener;
import org.ezuce.common.rest.RestManager;
import org.ezuce.panels.CallPanel2;

/**
 * This class works around a Spark limitation. The class describing a plugin
 * cannot reference any classes in dependency plugins, because those are not
 * loaded at instantiation time.
 *
 * @author alex
 *
 */
public class CallPluginHelper {
	public void addListener(CallPanel2 panel) {
		ForwarderListener listener = new ForwarderListenerImpl(panel);
		EventForwarder.getInstance().addListener(listener);
	}

	public boolean sipUserIsAuthenticated() {
		return RestManager.getInstance().isLoggedIn();
	}
}
