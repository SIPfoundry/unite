/**
 *
 * Copyright (C) 2013 eZuce Inc.
 *
 * $
 */
package org.ezuce.media.observers;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ezuce.common.phone.tray.EzuceTrayManager;
import org.ezuce.media.manager.PhoneManager;
import org.ezuce.unitemedia.event.UniteRegistrationEvent;
import org.ezuce.unitemedia.event.UniteRegistrationEventType;
import org.jivesoftware.spark.util.log.Log;

public class RegisterObserver implements Observer {
	
	private ScheduledExecutorService scExecServ = Executors.newSingleThreadScheduledExecutor();
	private ReRegisterTask reRegisterTask = new ReRegisterTask();

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof UniteRegistrationEvent)) {
			return;
		}
		try {
			UniteRegistrationEvent event = (UniteRegistrationEvent) arg;
			switch ((UniteRegistrationEventType) event.getEventType()) {
				case REGISTERED:
					EzuceTrayManager.getInstance().updateRegisteredAsPhone(true);
					break;
				case UNREGISTERED:
					EzuceTrayManager.getInstance().updateRegisteredAsPhone(false);
					scExecServ.schedule(reRegisterTask, 5, TimeUnit.SECONDS);
					break;
			}
		} catch (Exception ex) {
			Log.error("System tray update error ", ex);
		}
	}
	
	private class ReRegisterTask implements Runnable {
		@Override
		public void run() {
			reregister();
		}
		
		private void reregister() {
			PhoneManager phManager = PhoneManager.getInstance();
			System.out.println("MANAGER MIRCEA " + phManager.isUserRegistered());
			if (!phManager.isUserRegistered()) {
				Log.warning("[UNREGISTERED] - try to register again");
				phManager.registerUser();
			}
		}
	}
}
