package org.ezuce.media.observers;

import java.util.Observable;
import java.util.Observer;

import org.ezuce.media.manager.UnitemediaEventManager;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;

public class XMPPStreamingObserver implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		if(!(arg instanceof UniteXMPPVideoEvent)) {
			return;
		}
		UniteXMPPVideoEvent event = (UniteXMPPVideoEvent)arg;
		UnitemediaEventManager.getInstance().updateGivenXMPPVideoEvent(event);
	}
}
