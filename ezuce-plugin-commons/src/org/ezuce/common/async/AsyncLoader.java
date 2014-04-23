package org.ezuce.common.async;

import org.ezuce.common.resource.EzucePresenceUtils;
import org.jivesoftware.smackx.packet.VCard;


public class AsyncLoader {
	private static AsyncLoader instance;
	private final VCardLoader service;

	private AsyncLoader() {
		service = new VCardLoader();
		EzucePresenceUtils.registerRosterListener(service);
	}

	public static AsyncLoader getInstance() {
		if (instance == null) {
			synchronized (new byte[0]) {
				if (instance == null) {
					instance = new AsyncLoader();
				}
			}
		}

		return instance;
	}

	public void execute(String jid, VCardLoaderCallback callback) {
		service.add(jid, callback);
	}
	
	public void execute(String jid, VCardLoaderCallback callback, boolean forceReload) {
		service.add(jid, callback, forceReload);
	}
	
	public VCard executeJob(String jid, VCardLoaderCallback callback) {
		return service.executeJob(jid, callback);
	}
	
	public VCard executeJob(String jid, VCardLoaderCallback callback, boolean forceReload) {
		return service.executeJob(jid, callback);
	}

	public void shutdown() {
		EzucePresenceUtils.unregisterRosterListener(service);
		service.shutdown();		
	}
}
