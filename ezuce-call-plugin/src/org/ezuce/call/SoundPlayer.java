package org.ezuce.call;

import java.io.*;
import java.util.Hashtable;
import java.awt.event.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;

import javazoom.jl.decoder.JavaLayerException;
import org.ezuce.common.call.events.SoundEvent;
import org.ezuce.common.call.events.SoundListener;

/**
 * Sound player component that lazily loads data to play (when play button is
 * clicked)
 */
public class SoundPlayer extends JComponent {
    //Clip clip;               // Contents of a sampled audio file

    Object clip;               // Contents of a sampled audio file
    boolean playing = false; // whether the sound is currently playing
    int audioLength;         // Length of the sound.
    int audioPosition = 0;   // Current position within the sound
    // The following fields are for the GUI
    JButton play;             // The Play/Stop button
    JSlider progress;         // Shows and sets current position in sound
    JLabel time;              // Displays audioPosition as a number
    Timer timer;              // Updates slider every 100 milliseconds
    //Contains information about the stream to play
    private SoundListener soundListener = null;

    // Create a SoundPlayer component for the specified stream.
    public SoundPlayer(SoundListener soundListener)
            throws IOException,
            UnsupportedAudioFileException,
            LineUnavailableException,
            MidiUnavailableException,
            InvalidMidiDataException {

        // Now create the basic GUI
        play = new JButton("Play");                // Play/stop button
        progress = new JSlider(0, 0, 0); // Shows position in sound
        time = new JLabel("0");                    // Shows position as a #
        this.soundListener = soundListener;

        // When clicked, start or stop playing the sound
        play.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (playing) {
                    stop();
                } else {
                    try {
                        play();
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        // Whenever the slider value changes, first update the time label.
        // Next, if we're not already at the new position, skip to it.
        progress.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int value = progress.getValue();
                // Update the time label
                time.setText(value / 1000 + "." + (value % 1000) / 100);
                // If we're not already there, skip there.
                if (value != audioPosition) {
                    skip(value);
                }
            }
        });

        // This timer calls the tick( ) method 10 times a second to keep
        // our slider in sync with the music.
        timer = new javax.swing.Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    tick();
                } catch (IOException ex) {
                    Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // put those controls in a row
        Box row = Box.createHorizontalBox();
        row.add(play);
        row.add(progress);
        row.add(time);

        // And add them to this component.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(row);
    }

    /**
     * start playing the sound at the current position
     */
    public void play() throws JavaLayerException {
        if (!soundListener.isInitialized()) {
            soundListener.preparePlay(new SoundEvent(this));
            progress.setMaximum(soundListener.getAudioLength());
            progress.updateUI();
            clip = soundListener.getAudioPlayer();
        }
        soundListener.start();

        timer.start();
        play.setText("Stop");
        playing = true;
    }

    /**
     * stop playing the sound, but retain the current position
     */
    public void stop() {
        timer.stop();
        soundListener.stop();
        play.setText("Play");
        playing = false;
    }

    /**
     * stop playing the sound and reset the position to 0
     */
    public void reset() {
        stop();
        //clip.setMicrosecondPosition(0);
        soundListener.reset();
        audioPosition = 0;
        progress.setValue(0);
    }

    /**
     * Skip to the specified position
     */
    public void skip(int position) { // Called when user drags the slider
        if (position < 0 || position > audioLength) {
            return;
        }
        audioPosition = position;
        ///clip.setMicrosecondPosition(position * 1000);============de completat
        soundListener.setMicrosecondPosition(position * 1000);
        progress.setValue(position); // in case skip( ) is called from outside
    }

    /**
     * Return the length of the sound in ms or ticks
     */
    public int getLength() {
        return audioLength;
    }

    // An internal method that updates the progress bar.
    // The Timer object calls it 10 times a second.
    // If the sound has finished, it resets to the beginning
    void tick() throws IOException {

        if (soundListener.isActive()) {
            audioPosition = (int) (soundListener.getMicrosecondPosition() / 1000);
            progress.setValue(audioPosition);
        } else {
            reset();
        }
    }
    // Return a JSlider component to manipulate the supplied FloatControl

    JSlider createSlider(final FloatControl c) {
        if (c == null) {
            return null;
        }
        final JSlider s = new JSlider(0, 1000);
        final float min = c.getMinimum();
        final float max = c.getMaximum();
        final float width = max - min;
        float fval = c.getValue();
        s.setValue((int) ((fval - min) / width * 1000));

        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        labels.put(new Integer(0), new JLabel(c.getMinLabel()));
        labels.put(new Integer(500), new JLabel(c.getMidLabel()));
        labels.put(new Integer(1000), new JLabel(c.getMaxLabel()));
        s.setLabelTable(labels);
        s.setPaintLabels(true);

        s.setBorder(new TitledBorder(c.getType().toString() + " "
                + c.getUnits()));

        s.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int i = s.getValue();
                float f = min + (i * width / 1000.0f);
                c.setValue(f);
            }
        });
        return s;
    }
}