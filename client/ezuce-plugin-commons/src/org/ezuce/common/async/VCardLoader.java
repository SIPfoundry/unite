package org.ezuce.common.async;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import org.ezuce.common.resource.Utils;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class VCardLoader implements RosterListener {
	private LinkedBlockingQueue<Job> jobsQueue = new LinkedBlockingQueue<Job>();

	public VCardLoader() {
		new JobExecutor().start();
	}
	
	public void add(String fullyQualifiedJID, VCardLoaderCallback callback) {
		add(fullyQualifiedJID, callback, false);
	}
	
	public void add(String fullyQualifiedJID, VCardLoaderCallback callback, boolean forceReload) {
		jobsQueue.add(new Job(fullyQualifiedJID, callback, forceReload));		
	}	

	public void shutdown() {
		jobsQueue.clear();		
	}
	
	public VCard executeJob(String fullyQualifiedJID, VCardLoaderCallback callback) {
		return executeJob(fullyQualifiedJID, callback, false);
	}
	
	public VCard executeJob(String fullyQualifiedJID, final VCardLoaderCallback callback, boolean forceReload) {
		VCard vCard = new VCard();
		try {
			if (org.apache.commons.lang3.StringUtils.isEmpty(fullyQualifiedJID)) {
				return vCard;
			}
			if (Utils.isVCardSaved(StringUtils.parseBareAddress(fullyQualifiedJID)) && !forceReload) {
				vCard = SparkManager.getVCardManager().getVCard(fullyQualifiedJID, true);				
			}
			else {
				if (!forceReload) {
					Log.warning("Reload vcard - cannot retrieve it from file " + fullyQualifiedJID);
				} else {
					Log.warning("Reload vcard - vcard might have been updated " + fullyQualifiedJID);
				}
				try {
					vCard = SparkManager.getVCardManager().getVCard(fullyQualifiedJID, false);
				} catch (Exception ex) {
					Log.error("Vcard cannot be reloaded from server ", ex);
					//get whatever we have in cache
					try {
						vCard = SparkManager.getVCardManager().getVCard(fullyQualifiedJID, true);
					} catch (Exception unknown){
						Log.error("Vcard cannot be loaded from cache ", unknown);
					}
				}
			}	
			XMPPError error = vCard != null ? vCard.getError() : new XMPPError(
					XMPPError.Condition.item_not_found, "Retrieved vcard is NULL for: " + fullyQualifiedJID);
			if (error != null) {
			    Log.error("VCard loading failed with message: " + error.getMessage());			   
			}			
		} catch (Exception ex) {
			Log.error("VCard load failed: " + fullyQualifiedJID);
			return vCard;
		} finally {
			if (callback != null) {
				callback.vcardLoaded(vCard);
			}
		}
		return vCard;
	}		

	private class Job {
		private final String fullyQualifiedJID;
		private final VCardLoaderCallback callback;
		private final boolean forceReload;

		Job(String fullyQualifiedJID, VCardLoaderCallback callback, boolean forceReload) {
			this.fullyQualifiedJID = fullyQualifiedJID;
			this.callback = callback;
			this.forceReload = forceReload;
		}
		
		public void execute() {
			executeJob(fullyQualifiedJID, callback, forceReload);
		}
	}
	
	private class JobExecutor extends Thread {
		public void run() {
			while(true) {
				try {
					jobsQueue.take().execute();				
				} catch (Exception e) {
					Log.error("Cannot execute job", e);
			    }
			}
		}
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void presenceChanged(Presence presence) {
		String bareAddress = StringUtils.parseBareAddress(presence.getFrom());
        final PacketExtension packetExtension = presence.getExtension("x", "vcard-temp:x:update");
        // Handle vCard update packet.
        if (Utils.isVCardUpdated(presence)) {
        	//do not attempt to load vcard two times
        	SparkManager.getVCardManager().removeFromQueue(bareAddress);
        	//marks vcard for reloading
        	add(bareAddress, null, true);
        }
	}
}
