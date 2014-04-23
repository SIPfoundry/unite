package org.ezuce.common.ui.listener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.ezuce.common.call.events.SoundEvent;
import org.ezuce.common.call.events.SoundListener;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Rox
 */
public class PlayCallListener extends SoundListener
{
    String numeFisier = null;
    public BufferedInputStream bis1;
    private File f;
   
    public PlayCallListener(File file)
    {
        this.f = file;
    }
   
    @Override
    public boolean IsMP3()
    {
        return true;
    }
   
    @Override
    public void preparePlay(SoundEvent e) {

            FileInputStream fis = null;
           
            try {
                numeFisier = f.getAbsolutePath();
                fis = new FileInputStream(numeFisier);
            } catch (FileNotFoundException ex) {
                Log.error(ex.getMessage());
            }
           
            bis = new BufferedInputStream(fis);
            bis1 = new BufferedInputStream(fis);
            try
            {               
                initPlayer(fis, numeFisier, IsMP3());             
            }
            catch (Exception ex)
            {
                Log.error(ex.getMessage());
            }
       
        setInitialized(true);

    }

    @Override
    public void preparePlay(SoundEvent e, long start) {
        try
        {
            InputStream is = null;
            initPlayer(is, numeFisier, IsMP3());
            bis.skip(start);
            setInitialized(true);

        }
        catch (Exception ex)
        {
            Log.error(ex.getMessage());
        }
    }
 
   
}