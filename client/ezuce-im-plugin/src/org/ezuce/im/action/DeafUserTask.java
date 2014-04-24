package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class DeafUserTask extends Task<Object,Void>
{
    private String conferenceName;
    private int userJid;
    private boolean deaf;

    public DeafUserTask(org.jdesktop.application.Application app, String confName, int userId, boolean deaf)
    {
        super(app);
        this.conferenceName=confName;
        this.userJid=userId;
        this.deaf=deaf;
    }

    @Override
    protected Object doInBackground() {
        try {
            if (!deaf)
            {
                RestManager.getInstance().confUndeafUser(conferenceName, userJid);
            }
            else
            {
                RestManager.getInstance().confDeafUser(conferenceName, userJid);
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
