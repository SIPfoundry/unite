package org.ezuce.common.call.events;

import java.io.File;
import javazoom.jl.decoder.JavaLayerException;
import static org.ezuce.common.IOUtils.closeStreamQuietly;

import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.player.Player;

public abstract class SoundListener extends PlayerVoiceMail implements EventListener {

    int audioLength;
    private boolean initialized = false;

    
    protected void initPlayer(InputStream stream, String numeFisier, boolean b) throws
            IOException, UnsupportedAudioFileException, LineUnavailableException, JavaLayerException {


        if (!IsMP3()) {
            AudioInputStream ain = AudioSystem.getAudioInputStream(stream);
            try {
                if (clip == null || !clip.isActive()) {
                    DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat());
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(ain);

                    //audioLength=(int) (clip.getMicrosecondLength()/1000);
                }
            } finally { // We're done with the input stream.
                closeStreamQuietly(ain);
            }
            audioLength = (int) (clip.getMicrosecondLength() / 1000);
        } else {

            player = new Player(bis);
            //calculateLengthInMilis(new File(numeFisier));
            extractMp3Duration(new File(numeFisier));
            //calculateLengthInMilis(stream);
            audioLength = getLenghtInMilis();

        }
        // Get the clip length in microseconds and convert to milliseconds
        //audioLength = (int) clip.getLengthInMilis(stream);
        // audioLength = getLenghtInMilis();

    }

    public int getAudioLength() {
        return audioLength;
    }

    public Clip getClip() {
        return clip;
    }

    public abstract void preparePlay(SoundEvent e);

    public abstract void preparePlay(SoundEvent e, long start);

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}