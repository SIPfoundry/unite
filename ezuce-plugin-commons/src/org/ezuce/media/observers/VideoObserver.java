/**
 *
 * Copyright (C) 2013 eZuce Inc.
 *
 * $
 */
package org.ezuce.media.observers;

import java.util.Observable;
import java.util.Observer;

import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.media.manager.UnitemediaEventManager;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteVideoEventType;

public class VideoObserver implements Observer {

	public void update(Observable o, Object arg) {
		if (!(arg instanceof UniteVideoEvent)) {
			return;
		}
		UniteVideoEvent event = (UniteVideoEvent) arg;
		UnitemediaEventManager.getInstance().updateGivenSIPVideoEvent(event);
	}
	

}
