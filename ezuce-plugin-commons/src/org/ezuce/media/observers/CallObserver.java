/**
 *
 * Copyright (C) 2013 eZuce Inc.
 *
 * $
 */
package org.ezuce.media.observers;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import net.java.sip.communicator.service.protocol.CallPeer;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.media.manager.UnitemediaEventManager;
import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteCallEventType;
import org.jivesoftware.spark.util.log.Log;


public class CallObserver implements Observer {

	private static final long serialVersionUID = -3463562495679407243L;

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof UniteCallEvent)) {
			return;
		}
		final UniteCallEvent event = (UniteCallEvent) arg;
		UnitemediaEventManager.getInstance().updateGivenCallEvent(event);
	}
}
