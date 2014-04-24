package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

public class DeafAllTask extends Task<Object,Void> {
    private String conferenceName;
    private boolean deafAll;

    public DeafAllTask(org.jdesktop.application.Application app, String confName, boolean deafAll)
    {
        super(app);
        this.conferenceName=confName;
        this.deafAll = deafAll;
    }

    @Override
    protected Object doInBackground()
    {
        try
        {
        	if (!deafAll) {
        		RestManager.getInstance().confUndeafAll(conferenceName);
        	} else {
        		RestManager.getInstance().confDeafAll(conferenceName);
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
