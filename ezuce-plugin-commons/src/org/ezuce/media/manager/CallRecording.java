package org.ezuce.media.manager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class CallRecording
{
   
    private MP3File _mp3;
    private File _file;
       
    public CallRecording(File _file)
    {
        try
        {
            this._file = _file;
           _mp3= (MP3File) AudioFileIO.read(_file.getAbsoluteFile());
        }
        catch(Exception e)
        {
            Log.error(e.getMessage());
        }
    }
   
    /**
     * @return the _recordingDate
     */
    public String getRecordingDate()
    {
       Date dt = new Date(_file.lastModified());
       SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm a");
        return ft.format(dt);             
    }

    /**
     * @return the _duration
     */
    public String getDuration() {
        int duration= _mp3.getAudioHeader().getTrackLength()*1000;//duration in miliseconds
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        df.setTimeZone(tz);
        return df.format(new Date(duration)).toString();
    }

    public int getAudioLength()
    {
       return _mp3.getAudioHeader().getTrackLength();
    }
    
    /**
     * @return the _idUser
     */
    public String getIdUser() {
        if (this._mp3!=null && this._mp3.getID3v1Tag()!=null){
        	return this._mp3.getID3v1Tag().getComment().toString().replace("[", "").replace("]", "");
        }
        return null;
                
    }

    /**
     * @return the _displayName
     */
    public String getDisplayName() {
       
        return "";
    }
   
    public File getFile()
    {
        return this._file;
    }
    
    public static File getRecordingsFolder(File userDir)
    {
        String recFolderPath = userDir.getAbsolutePath()+"/Recordings";
               
        //Log.warning(">>>>>>>>>>>>>> Recordings folder: "+recFolderPath);

        File recordingsFolder = new File(recFolderPath);
        if (!recordingsFolder.exists()){
            recordingsFolder.mkdirs();
        }
        
        return recordingsFolder;
    }
    
    

}