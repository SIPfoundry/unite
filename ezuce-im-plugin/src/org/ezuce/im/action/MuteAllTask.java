package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

public class MuteAllTask extends Task<Object,Void> {
    private String conferenceName;
    private boolean muteAll;

    public MuteAllTask(org.jdesktop.application.Application app, String confName, boolean muteAll)
    {
        super(app);
        this.conferenceName=confName;
        this.muteAll = muteAll;
    }

    @Override
    protected Object doInBackground()
    {
        try
        {
        	if (!muteAll) {
        		RestManager.getInstance().confUnmuteAll(conferenceName);
        	} else {
        		RestManager.getInstance().confMuteAll(conferenceName);
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
