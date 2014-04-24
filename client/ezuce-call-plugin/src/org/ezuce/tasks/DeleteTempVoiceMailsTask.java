package org.ezuce.tasks;

import java.io.File;
import org.ezuce.call.CallPlugin;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jivesoftware.spark.SparkManager;

public class DeleteTempVoiceMailsTask extends Task<Object, Void>{
    
    
    public DeleteTempVoiceMailsTask(org.jdesktop.application.Application app)
    {
        super(app);        
    }
    
    @Override
    protected Object doInBackground() {
        
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
        String tempFolderName = resourceMap.getString("voicemail.mp3tempfolder");
        File userDir=SparkManager.getUserDirectory();
        tempFolderName = userDir.getAbsolutePath()+System.getProperty( "file.separator") +tempFolderName;
        
        File f = new File(tempFolderName);
        
        if (f.exists())
        {
            File[] files = f.listFiles();
            for (int i=0;i<files.length;i++)
            {
                files[i].delete();
            }
        }
        return null;
    }
    @Override
    protected void succeeded(Object result)
    {
        
    }
    @Override
    protected void finished()
    {
        
    }
}
