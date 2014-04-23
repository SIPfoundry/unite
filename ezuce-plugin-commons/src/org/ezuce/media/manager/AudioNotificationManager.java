package org.ezuce.media.manager;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.commons.io.IOUtils;
import org.ezuce.common.preference.sound.EzuceSoundPreference;
import org.ezuce.common.preference.sound.EzuceSoundPreferences;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.preference.sounds.SoundPreference;

/**
 *
 * @author Razvan
 */
public class AudioNotificationManager {
    private Clip clip;
	private boolean repeatSound;
	private EzuceSoundPreferences soundPrefs;
	private static final AudioNotificationManager singleton = new AudioNotificationManager();
	
    public static synchronized AudioNotificationManager getInstance() {
        return singleton;
    }
    
    private AudioNotificationManager() {
        setRepeatSound(true);
		EzuceSoundPreference soundPreference = (EzuceSoundPreference) SparkManager.getPreferenceManager().getPreference(SoundPreference.NAMESPACE);
		soundPrefs = (EzuceSoundPreferences) soundPreference.getPreferences();
    }
	
	public void playRingingSound() {
		if (!soundPrefs.isPlayIncomingCallSound()) {
		    return;
		}
        playSound();
	}
	
	public void playCallingSound() {
		//always play the calling sound. For the moment we use the same sound for both incoming (ringing) and outgoing (calling) calls
		//the preference menu should be extended to make possible select different sounds for incoming and outgoing calls
		playSound();
	}
	
	private void playSound() {		
		if (clip != null && (clip.isActive() || clip.isOpen())) {
			Log.warning("Sound is already running we cannot start it again");
			return;
		}
		final AudioInputStream stream = getAudioStream();
		if (stream == null) {
			return;
		}

		try {
			clip = AudioSystem.getClip();
			clip.open(stream);
			if (repeatSound) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		} catch (Exception e) {
			//on Ubuntu this fails with  java.lang.IllegalArgumentException: Invalid format
			//java.lang.IllegalArgumentException: Invalid format
			Log.error("Audio play failed", e);			
		} finally {
			IOUtils.closeQuietly(stream);
		}

	}

	public void stopSound() {
		if (clip == null) {
			return;
		}
		try {
			clip.stop();
			clip.close();
		} catch (Exception ex) {
			//It fails on ubuntu. we need to choose a different sound clip
			Log.error("Stop sound failed", ex);			
		}
	}

	protected AudioInputStream getAudioStream() {
		if (soundPrefs.getIncomingCallSound() == null) {
			return null;
		}

		AudioInputStream inputStream;
		try {
			inputStream = AudioSystem.getAudioInputStream(new File(soundPrefs.getIncomingCallSound()));
			return inputStream;
		} catch (Exception e) {
			Log.error("Cannot play sound ", e);
		}
		return null;
	}

    public void setRepeatSound(boolean repeat) {
	    this.repeatSound = repeat;
	}
}
