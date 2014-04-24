package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class KickAllTask  extends Task<Object,Void>
{
    private String conferenceName;

    public KickAllTask(org.jdesktop.application.Application app, String confName)
    {
        super(app);
        this.conferenceName=confName;
    }

    @Override
    protected Object doInBackground()
    {
        try
        {
            RestManager.getInstance().confKickAll(conferenceName);
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
