package org.ezuce.im.action;

import java.util.List;

import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.im.ui.EzuceGroupChatRoomPanel;
import org.jdesktop.application.Task;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class RebuildConfParticipantsListTask extends Task<List<ConferenceMemberXML>,Void>
{
    private EzuceGroupChatRoomPanel confRoomHeaderPanel;

    public RebuildConfParticipantsListTask(org.jdesktop.application.Application app, EzuceGroupChatRoomPanel confHeaderPanel)
    {
        super(app);
        this.confRoomHeaderPanel=confHeaderPanel;
    }

    @Override
    protected List<ConferenceMemberXML> doInBackground() {
    	return retrieveMembers();
    }
    
    public List<ConferenceMemberXML> retrieveMembers() {
        try {
            String rn = confRoomHeaderPanel.getParentChatRoom().getNaturalLanguageName();
            if (rn != null) {
                rn=Utils.getImId(rn);
            }
            List<ConferenceMemberXML> confMembers=RestManager.getInstance().getConferenceParticipants(rn);           
            return confMembers;
        }
        catch(Exception ex){
            Log.error("Cannot retrieve members", ex);
        }
        return null;
    }
    
    @Override
    protected void succeeded(List<ConferenceMemberXML> result) {
    	confRoomHeaderPanel.setConfUsers(result);
    }
    
    @Override
    protected void finished() {
    	confRoomHeaderPanel.rebuildParticipantsListGui();
    	confRoomHeaderPanel.revalidate();
    	confRoomHeaderPanel.repaint();
    }
}