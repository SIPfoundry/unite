package org.ezuce.tasks;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezuce.common.io.FindMeFileIO;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.rest.CallSequence;
import org.ezuce.common.rest.FindMeGroup;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.rest.Ring;
import org.ezuce.common.rest.Ring.RingType;
import org.ezuce.panels.FindMeMiniPanel;
import org.ezuce.panels.FindMePanel;
import org.ezuce.panels.FindMeRosterPanel;
import org.ezuce.panels.interfaces.FindMePanelInterface;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class LoadFindMeGroupsTask extends org.jdesktop.application.Task<List<FindMeRosterPanel>, Void> {

    private Map<FindMeRosterPanel, List<FindMeMiniPanel>> groupsMap=new HashMap<FindMeRosterPanel, List<FindMeMiniPanel>>();
    private CallSequence callSequence;
    private final FindMePanel findMePanel;

    public LoadFindMeGroupsTask(org.jdesktop.application.Application app, FindMePanel fmp) {
        //EDT - read GUI info:
        super(app);
        findMePanel=fmp;
        findMePanel.setCursor(hourglassCursor);
        findMePanel.showLoading();
    }

    @Override
    protected List<FindMeRosterPanel> doInBackground() {
        //NOT EDT:
        List<FindMeRosterPanel> groupList=new ArrayList<FindMeRosterPanel>();
        List<FindMeMiniPanel> miniPanelList=new ArrayList<FindMeMiniPanel>();
        try {
            String findMeConfigurationXml=RestManager.getInstance().retrieveForwarding();
            callSequence = new FindMeFileIO().parseFindMeXml(findMeConfigurationXml);
            if (callSequence==null || callSequence.getGroups().isEmpty()) { //nothing is configured for Find Me/Follow Me
                //build an empty initial group.
                //no ring should be saved for the initial group - the current user rings by default
                FindMeRosterPanel fmrp=findMePanel.getFindMeRosterPanelInitial();
                fmrp.setFindMeGroup(new FindMeGroup());
                final FindMeMiniPanel fmmp=FindMeMiniPanel.createPanelFromCurrentUser(callSequence);
                findMePanel.setCurrentUserPanel(fmmp);
                miniPanelList.add(fmmp);
                groupList.add(fmrp);
                groupsMap.put(fmrp, miniPanelList);
            }
            else {
                List<FindMeGroup> list = new ArrayList<FindMeGroup> ();
                FindMeGroup firstGroup = callSequence.getGroups().get(0);
                //in this case all returned groups are new, we have to create the empty initial group from scratch
                //no ring should be saved for the initial group - the current user rings by default
                if (firstGroup.getRings().get(0).getType().equals(Ring.ringTypes.get(RingType.IF_NO_RESPONSE))) {
                        list.add(new FindMeGroup());
                }
                list.addAll(callSequence.getGroups());
                int nGroups=list.size();
                //System.out.println("Nr groups ="+nGroups);

                for (int i = 0;i < nGroups;i++) {
                    FindMeRosterPanel fmrp=null;
                    FindMeGroup fmg=list.get(i);
                    //System.out.format("\nGoing through groups. i=%d \n",i);
                    if (i==0) { //if processing the first group:                    
                        fmrp=findMePanel.getFindMeRosterPanelInitial();
                        fmrp.setFindMeGroup(fmg);
                        if (!findMePanel.containsCurrentUser(fmg)) {//check if first group contains the current user:                        
                            //System.out.format("\nBuild group for current user. \n");
                            final FindMeMiniPanel fmmpLocal=FindMeMiniPanel.createPanelFromCurrentUser(callSequence);
                            findMePanel.setCurrentUserPanel(fmmpLocal);
                            miniPanelList.add(fmmpLocal);
                        }
                    }
                    //System.out.format("\nCheck if CallSequence is empty. \n");
                    if (i!=0 && fmg.getRings().isEmpty()) {
                        //skip empty files  (groups).
                        groupList.add(fmrp);
                        groupsMap.put(fmrp, miniPanelList);
                        miniPanelList=new ArrayList<FindMeMiniPanel>();
                        continue;
                    }

                    if (fmrp==null) { //if current group has not been created yet (in if(i==0)):                    
                        fmrp=new FindMeRosterPanel(findMePanel);
                        fmrp.setName(fmrp.NAME_PREFIX+i);
                        fmrp.setFindMeGroup(fmg);
                        //FindMePanel.this.addRosterPanel(fmrp);
                        System.out.format("\nAdded new RosterPanel. \n");
                    }

                    List<Ring> rings=fmg.getRings();
                    //System.out.println("Group "+fmrp.getName()+" contains "+rings.size());
                    for (Ring ring:rings) {
                        //System.out.format("\nGoing through group %d , ring %s \n", i,ring.getNumber());
                        final FindMeMiniPanel fmmp=new FindMeMiniPanel(ring);
                        //FindMePanel.this.addContactToPanel(fmrp, fmmp);
                        miniPanelList.add(fmmp);
                    }

                    if (i==0) { //if this is the initial group                    
                        //if first group does NOT contain the current user,
                        //it means it has been created as UI at the test from above,
                        //so we must add it also as a Ring to the initial FindMeGroup:
                        if (!findMePanel.containsCurrentUser(fmg)) {
                            //System.out.format("\nBuild group for current user. \n");
//                            final FindMeMiniPanel fmmp=miniPanelList.get(0);
//                            List<Ring> rgs=new ArrayList<Ring>();
//                            rgs.add(fmmp.getRing());
//                            rgs.addAll(fmg.getRings());
//                            fmg.setRings(rgs);
                        }
                        else { //the first group contains the current user                        
                            final FindMeMiniPanel fmmp=miniPanelList.get(0);
                            fmmp.getDeleteButton().setIcon(null);
                            fmmp.getDeleteButton().setRolloverIcon(null);
                            fmmp.getDeleteButton().setPressedIcon(null);
                            fmmp.getDeleteButton().setAction(null);
                            fmmp.setLoggedUser(true);
                            int expMins=callSequence.getExpiration()/60;
                            int expSecs=callSequence.getExpiration()%60;
                            fmmp.setExpiration(expMins, expSecs);
                        }
                    }
                    fmrp.setFindMeGroup(fmg);
                    groupList.add(fmrp);
                    groupsMap.put(fmrp, miniPanelList);
                    miniPanelList=new ArrayList<FindMeMiniPanel>();
                }
            }

        }
        catch(Exception e) {
            Log.error(e);
        }
        return groupList;
    }

    @Override
    protected void succeeded(List<FindMeRosterPanel> result) {
        //EDT:
        findMePanel.hideLoading();
        if (result==null || result.isEmpty() || groupsMap==null || groupsMap.isEmpty()) return;
        final FindMePanelInterface parent=result.get(0).getFindMePanelParent();
        for (FindMeRosterPanel fmrp : result) {
            //System.out.println(" >>>> Group:"+fmrp.getName());
            if (!findMePanel.getFindMeRosterPanelInitial().getName().equals(fmrp.getName())) {
                //Add roster panel only if it is NOT the first one (the first one is already there):
                findMePanel.addRosterPanel(fmrp,null);
            }
            final List<FindMeMiniPanel> miniPanels=groupsMap.get(fmrp);
            if (miniPanels!=null && !miniPanels.isEmpty()) {
                for (FindMeMiniPanel fmmp : miniPanels) {
                    //System.out.println(" >>>>>>>> User:"+fmmp.getRing().getNumber());
                    findMePanel.addContactToPanel(fmrp, fmmp);
                }
            }
        }

        //Next, on the last place add, if necessary, the VoiceMail icon inside its own group:
        if (findMePanel.isVoiceMailServiceAvailable() && callSequence.isWithVoiceMail()) {
            FindMeRosterPanel vmFmrp=new FindMeRosterPanel(parent);
            vmFmrp.setName(findMePanel.VOICEMAIL_ROSTER_PANEL_NAME);
            vmFmrp.getAddNrButton().setVisible(false);
            vmFmrp.getNewGroupButton().setVisible(false);
            FindMeMiniPanel vmFmmp=FindMeMiniPanel.createVoiceMailPanel(callSequence);

            findMePanel.addRosterPanel(vmFmrp,null);
            findMePanel.addContactToPanel(vmFmrp, vmFmmp);
        }

        EzucePresenceUtils.registerRosterListener(findMePanel);
    }

    @Override
    protected void finished() {
         findMePanel.setCursor(defaultCursor);
    }

    private final Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
}
