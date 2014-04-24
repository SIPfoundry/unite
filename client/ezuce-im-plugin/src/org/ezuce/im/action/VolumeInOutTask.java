package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class VolumeInOutTask extends Task<Object,Void> {
    private String conferenceName;
    private int userId;
    private boolean in;
    
    public VolumeInOutTask(org.jdesktop.application.Application app, String confName, int userId, boolean in)
    {
        super(app);
        this.conferenceName=confName;
        this.userId=userId;
        this.in = in;
    }

    @Override
    protected Object doInBackground()
    {
        try
        {
        	if (in) {
        		RestManager.getInstance().confVolumeIn(conferenceName, userId);
        	} else {
        		RestManager.getInstance().confVolumeOut(conferenceName, userId);
        	}
        }
        catch(Exception ex){
            ex.printStackTrace(System.err);
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
