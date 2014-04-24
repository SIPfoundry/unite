package org.ezuce.im.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.SearchUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.im.ui.EzuceGroupChatParticipantList;
import org.ezuce.im.ui.EzuceGroupChatRoom;
import org.jdesktop.application.Task;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SessionManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.UserManager;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 *
 * @author Razvan
 */
public class EscalateConfToChatTask extends Task<Object,Void>
{
    private EzuceGroupChatRoom confRoom;
    private List<ConferenceMemberXML> confUsers;

    public EscalateConfToChatTask(org.jdesktop.application.Application app, EzuceGroupChatRoom conf)
    {
        super(app);
        this.confRoom=conf;
        confUsers=conf.getGroupChatRoomPanel().getConfUsers();
    }

    @Override
    protected Object doInBackground()
    {
        try {
           if (!confUsers.isEmpty()) {
        	   Collection<String> occupantJIDs = confRoom.getOccupantJIDs();
               for (ConferenceMemberXML cmx:confUsers) {                   
            	   //MIRCEA
            	   String id = cmx.getImId();                                      
                   confRoom.inviteUser(occupantJIDs, id);
               }
           }
        }
        catch(Exception ex){
        	Log.error("Error sending invitation in group chat", ex);            
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