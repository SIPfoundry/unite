package org.ezuce.call.events;

import org.ezuce.common.call.events.SoundEvent;
import org.ezuce.common.call.events.SoundListener;
import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ezuce.call.CallPlugin;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.rest.Voicemail;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.spark.SparkManager;

public class VoicemailListener extends SoundListener {

    private Voicemail voicemail;
    String numeFisier = null;
    private byte[] content;
    public BufferedInputStream bis1;
    
    public VoicemailListener(Voicemail voicemail) {
        this.voicemail = voicemail;
    }
    
    public byte[] getContent(){
    	return content;
    }
    
    public void setContent(byte[] c){
    	this.content = c;
    }
    
    public Voicemail getVoicemail(){
    	return voicemail;
    }
    
    @Override
    public boolean IsMP3()
    {
        if (this.voicemail.getFormat()!=null &&
            this.voicemail.getFormat().equals("mp3"))
        {
            return true;
        }
        return false;
    }
    
    @Override
    public void preparePlay(SoundEvent e) {
    	    	
		if (!this.IsMP3()) {
			content = RestManager.getInstance().getVoicemailContent(getVoicemail());
			InputStream stream = null;
			stream = new ByteArrayInputStream(content);
            try {
                initPlayer(stream, "", this.IsMP3());
                //Player started, mark voicemail as heard
                RestManager.getInstance().markVoicemailHeard(voicemail.getId());
            } catch (Exception ex) {
            	Logger.getLogger(VoicemailListener.class.getName()).log(Level.SEVERE, null, ex);
			}
        } else {
            FileInputStream fis = null;
            this.cleanMp3TempDirectory();
            File f=createFile(voicemail.getId());
            try
            {      
            	if (f.length()==0L){
            		content = RestManager.getInstance().getVoicemailContent(getVoicemail());
	                FileOutputStream fos = new FileOutputStream(f);
	                fos.write(content);
	                fos.flush();
	                fos.close();  
	                fos=null;
            	}
            }
            catch(IOException ioe)
            {
                System.err.println(ioe);
            }
            catch(Exception ex){
                System.err.println(ex);
            }                
            
            try {
                numeFisier = f.getAbsolutePath();
                fis = new FileInputStream(numeFisier);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(VoicemailListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            bis = new BufferedInputStream(fis);
            bis1 = new BufferedInputStream(fis);
            try {                
                initPlayer(fis, numeFisier, IsMP3());
                //Player started, mark voicemail as heard
                RestManager.getInstance().markVoicemailHeard(voicemail.getId());                
            } catch (Exception ex) {
            	Logger.getLogger(VoicemailListener.class.getName()).log(Level.SEVERE, null, ex);
			}
        }

		
		this.setInitialized(true);				
    	
    }

    public void startActualPlaying(){
    	    }
    
    @Override
    public void preparePlay(SoundEvent e, long start) {
        try {
            // ByteArrayInputStream bais =null;
            InputStream is = null;
            if (!IsMP3()) {
                ByteArrayInputStream bais = new ByteArrayInputStream(content);
                initPlayer(bais, "", IsMP3());
                bais.skip(start);

            } else {

                initPlayer(is, numeFisier, IsMP3());
                bis.skip(start);
            }
            setInitialized(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public File createFile(String voiceMailId)
    {
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
        String tempFolderName = resourceMap.getString("voicemail.mp3tempfolder");
        File userDir=SparkManager.getUserDirectory();
        tempFolderName = userDir.getAbsolutePath() + System.getProperty( "file.separator") + tempFolderName ;
        
        File tempFolder = new File(tempFolderName);
        if (!tempFolder.exists())
        {
            tempFolder.mkdirs();
        }
        
        tempFolderName += System.getProperty( "file.separator") +"tmpMp3_"+voiceMailId+".mp3";
        
        File f = new File(tempFolderName);
        if (!f.exists())
        {
            try
            {
                f.createNewFile();
            }
            catch(IOException ioe)
            {
                System.err.println(ioe);
            }
        }
        return f;
    }
    
    public void cleanMp3TempDirectory()
    {
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
        String tempFolderName = resourceMap.getString("voicemail.mp3tempfolder");
        File userDir=SparkManager.getUserDirectory();
        tempFolderName = userDir.getAbsolutePath()+System.getProperty( "file.separator") +tempFolderName;
        
        File f = new File(tempFolderName);
        
        if (f.exists())
        {
            File[] files = f.listFiles();
            Date now=new Date();
            long nowMilis=now.getTime();
            long maxAge = 30L*24L*60L*60L*1000L;
            
            for (int i=0;i<files.length;i++)
            {
            	if ((nowMilis - files[i].lastModified()) > maxAge){
            		files[i].delete();
            	}
            }
        }
    }
            
    
}