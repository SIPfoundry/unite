package org.ezuce.common.call.events;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.player.Player;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jivesoftware.spark.util.log.Log;

public class PlayerVoiceMail {
    public Clip clip;
    public Player player;
    public int bytesLeft;
    public BufferedInputStream bis;
    public int currentPositionInBytes;
    private int totalBytes;
    private boolean stopped = true;
    private int lenghtInMilis;

    public boolean IsMP3() {
        return true;
    }

    public Object getAudioPlayer() {
        return (IsMP3() ? player : clip);
    }

    public Boolean isActive() {
        if (!IsMP3() && clip != null) {
            return clip.isActive();
        } else {            
            if (player != null) {
                return true;
            }
            return false;
        }
    }

    protected int computeCurrentPosition() throws IOException {
        //System.out.println("Compute current pos");
        if (player == null || bis == null || totalBytes == 0) {

            return 0;
        }

        bytesLeft = bis.available();
        currentPositionInBytes = totalBytes - bytesLeft;        
       
        return currentPositionInBytes;
    }

    public synchronized void setStopped(boolean s) {
        stopped = s;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public void playMp3() {
        try {
            if (totalBytes == 0) {
                //totalBytes=3653760;
                totalBytes = bis.available();
            }
            if (currentPositionInBytes > 0) {
                bis.skip(currentPositionInBytes);
            }
        } catch (IOException ioe) {
        }


        setStopped(false);

        // run in new thread to play in background
        new Thread() {

            @Override
            public void run() {
                try {
                    player.play();
                    //computeCurrentPosition();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

        new Thread() {

            @Override
            public void run() {
                while (!isStopped()) {
                    try {
                        //Compute the current position:
                        computeCurrentPosition();
                        Thread.sleep(200);                            
                    } catch (Exception ex) {
                        Log.error("Cannot compute current position "+ex.getMessage());
                    }                  
                }
            }
        }.start();
    }

    public void setMicrosecondPosition(int position) {
        if (!IsMP3() && clip != null) {
            clip.setMicrosecondPosition(position * 1000);
        } else {
            //currentPositionInBytes=position*1000;
            //System.out.println("Current poz "+currentPositionInBytes);
            currentPositionInBytes = (int) (((double) position * (double) totalBytes) / (double) lenghtInMilis);
            System.out.println("Formula II " + currentPositionInBytes);
            //microsecondPosition=position;
        }
    }

    public long getMicrosecondPosition() throws IOException {
        if (!IsMP3() && clip != null && clip.isActive()) {
            return (int) (clip.getMicrosecondPosition() / 1000);
        } else {

            {
                // System.out.println("Curent poz "+currentPositionInBytes);
                //System.out.println("Total Bytes "+totalBytes);
                //System.out.println("lenghtInMilis "+lenghtInMilis);
                return (int) (((double) currentPositionInBytes / (double) totalBytes) * (double) lenghtInMilis);
            }
        }
    }

    public void start() {
        if (!IsMP3() && clip != null) {
            clip.start();
        } else {
            //currentPositionInBytes=0;
            playMp3();
        }
    }

    public void stop() {
        if (!IsMP3() && clip != null) {
            clip.stop();
        } else {
            if (this.player != null) {
                player.close();
                setStopped(true);
            }
        }
    }

    public void reset() {
        if (!IsMP3() && clip != null) {
            clip.setMicrosecondPosition(0);
        } else if (player != null) {
            player.close();
            currentPositionInBytes = 0;
        }


    }

    /**
     * @return the lenghtInMilis
     */
    public int getLenghtInMilis() {
        return lenghtInMilis;
    }

    /**
     * @param lenghtInMilis the lenghtInMilis to set
     */
    public void setLenghtInMilis(int lenghtInMilis) {
        this.lenghtInMilis = lenghtInMilis;
    }

    public void calculateLengthInMilis(File filename) throws IOException {
        if (!IsMP3() && clip != null) {
            lenghtInMilis = (int) (clip.getMicrosecondLength() / 1000);
        } else if (player != null) {
            try {
                AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(filename);
                Map properties = baseFileFormat.properties();
                Long duration = (Long) properties.get("duration");
                lenghtInMilis = (int) (duration / 1000);
            } catch (UnsupportedAudioFileException uafe) {
                System.err.println(uafe);
                lenghtInMilis = 0;
            }
        }
    }
    
    public void extractMp3Duration(File audioFile) {        
        if (!IsMP3() && clip != null) {
            lenghtInMilis = (int) (clip.getMicrosecondLength() / 1000);
        } else if (player != null) {
            try 
            {            
                AudioFile mp3File = AudioFileIO.read(audioFile);
                int duration = mp3File.getAudioHeader().getTrackLength();//duration in seconds
                lenghtInMilis = (int) (duration * 1000);
            } 
            catch (Exception e) {            
                System.err.println(e);    
                lenghtInMilis = 0;
            }            
        }
    }

    public void calculateLengthInMilis(InputStream is) throws IOException {
        if (!IsMP3() && clip != null) {
            lenghtInMilis = (int) (clip.getMicrosecondLength() / 1000);
        } else if (player != null) {
            try {                               
                
                AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(is);
                Map properties = baseFileFormat.properties();
                Long duration = (Long) properties.get("duration");
                if (duration == null) {
                    duration = 0L;
                }
                lenghtInMilis = (int) (duration / 1000);
            } catch (UnsupportedAudioFileException uafe) {
                System.err.println(uafe);
                lenghtInMilis = 0;
            }
        }
    }
}