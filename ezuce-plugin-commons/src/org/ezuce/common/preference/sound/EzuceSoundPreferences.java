package org.ezuce.common.preference.sound;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.Spark;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.preference.sounds.SoundPreferences;

public class EzuceSoundPreferences extends SoundPreferences {

	private static String gDefaultOutgoingSound;
	private static String gDefaultIncomingSound;
	private static String gDefaultIncomingInvitationSound;
	private static String gDefaultOfflineSound;
	private static String gDefaultOnlineSound;
	private static String gDefaultIncomingCallSound;

	private String outgoingSound;
	private String incomingSound;
	private String offlineSound;
	private String onlineSound;
	private String incomingInvitationSound;
	private String incomingCallSound;

	private boolean playOutgoingSound = true;
	private boolean playIncomingSound = true;
	private boolean playOfflineSound = true;
	private boolean playIncomingInvitationSound = true;
	private boolean playOnlineSound = true;
	private boolean playIncomingCallSound = true;

	// Set default sounds
	static {
		try {
			gDefaultOutgoingSound = new File(Spark.getResourceDirectory(),
					"sounds/outgoing.wav").getCanonicalPath();
			gDefaultIncomingSound = new File(Spark.getResourceDirectory(),
					"sounds/incoming.wav").getCanonicalPath();
			gDefaultIncomingInvitationSound = new File(
					Spark.getResourceDirectory(), "sounds/incoming.wav")
					.getCanonicalPath();
			gDefaultOfflineSound = new File(Spark.getResourceDirectory(),
					"sounds/presence_changed.wav").getCanonicalPath();
			gDefaultOnlineSound = new File(Spark.getResourceDirectory(),
					"sounds/presence_changed.wav").getCanonicalPath();
			gDefaultIncomingCallSound = new File(Spark.getResourceDirectory(),
					"sounds/ring.wav").getCanonicalPath();
		} catch (IOException e) {
			Log.error(e);
		}
	}

	public EzuceSoundPreferences() {
	}

	public String getOutgoingSound() {
		return getSoundIfNotEmptyOrReturnDefault(outgoingSound,
				gDefaultOutgoingSound);
	}

	public void setOutgoingSound(String outgoingSound) {
		this.outgoingSound = outgoingSound;
	}

	public String getIncomingSound() {
		return getSoundIfNotEmptyOrReturnDefault(incomingSound,
				gDefaultIncomingSound);
	}

	public void setIncomingSound(String incomingSound) {
		this.incomingSound = incomingSound;
	}

	public String getOfflineSound() {
		return getSoundIfNotEmptyOrReturnDefault(offlineSound,
				gDefaultOfflineSound);
	}

	public void setOfflineSound(String offlineSound) {
		this.offlineSound = offlineSound;
	}

	public boolean isPlayOutgoingSound() {
		return playOutgoingSound;
	}

	public void setPlayOutgoingSound(boolean playOutgoingSound) {
		this.playOutgoingSound = playOutgoingSound;
	}

	public boolean isPlayIncomingSound() {
		return playIncomingSound;
	}

	public void setPlayIncomingSound(boolean playIncomingSound) {
		this.playIncomingSound = playIncomingSound;
	}

	public boolean isPlayOfflineSound() {
		return playOfflineSound;
	}

	public void setPlayOfflineSound(boolean playOfflineSound) {
		this.playOfflineSound = playOfflineSound;
	}

	public void setIncomingInvitationSoundFile(String sound) {
		incomingInvitationSound = sound;
	}

	public String getIncomingInvitationSoundFile() {
		return getSoundIfNotEmptyOrReturnDefault(incomingInvitationSound,
				gDefaultIncomingInvitationSound);
	}

	public boolean playIncomingInvitationSound() {
		return playIncomingInvitationSound;
	}

	public void setPlayIncomingInvitationSound(boolean play) {
		this.playIncomingInvitationSound = play;
	}

	public String getOnlineSound() {
		return getSoundIfNotEmptyOrReturnDefault(onlineSound,
				gDefaultOnlineSound);
	}

	public boolean isPlayOnlineSound() {
		return playOnlineSound;
	}

	public void setOnlineSound(String onlineSound) {
		this.onlineSound = onlineSound;
	}

	public void setPlayOnlineSound(boolean playOnlineSound) {
		this.playOnlineSound = playOnlineSound;
	}

	public String getIncomingCallSound() {
		return getSoundIfNotEmptyOrReturnDefault(incomingCallSound,
				gDefaultIncomingCallSound);
	}

	public boolean isPlayIncomingCallSound() {
		return playIncomingCallSound;
	}

	public void setIncomingCallSound(String path) {
		this.incomingCallSound = path;
	}

	public void setPlayIncomingCallSound(boolean play) {
		this.playIncomingCallSound = play;
	}

	private String getSoundIfNotEmptyOrReturnDefault(String sound,
			String defaultSound) {
		return sound != null && !"".equals(sound) ? sound : defaultSound;
	}

}
