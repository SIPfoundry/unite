package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class MuteUserTask extends Task<Object,Void>
{
    private String conferenceName;
    private int userJid;
    private boolean mute;

    public MuteUserTask(org.jdesktop.application.Application app, String confName, int userId, boolean mute)
    {
        super(app);
        this.conferenceName=confName;
        this.userJid=userId;
        this.mute=mute;
    }

    @Override
    protected Object doInBackground() {
        try {
            if (!mute) {
                RestManager.getInstance().confUnmuteUser(conferenceName, userJid);
            }
            else {
                RestManager.getInstance().confMuteUser(conferenceName, userJid);
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
