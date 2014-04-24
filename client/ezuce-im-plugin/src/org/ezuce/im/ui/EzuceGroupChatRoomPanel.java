/*
 * EzuceGroupChatRoomPanel.java
 *
 * Created on Aug 8, 2011, 11:54:02 PM
 */
package org.ezuce.im.ui;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.ezuce.common.EzuceConferenceUtils;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.PhoneBookEntryWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.windows.ContactSearchDialog;
import org.ezuce.common.windows.ContactSearchDialog.OKButtonType;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.im.action.DeafAllTask;
import org.ezuce.im.action.DeafUserTask;
import org.ezuce.im.action.EscalateConfToChatTask;
import org.ezuce.im.action.KickAllTask;
import org.ezuce.im.action.MuteAllTask;
import org.ezuce.im.action.MuteUserTask;
import org.ezuce.im.action.RebuildConfParticipantsListTask;
import org.ezuce.im.action.RecordConferenceTask;
import org.ezuce.media.ui.AudioCallAvatarPanel;
import org.ezuce.media.ui.AudioCallPanel;
import org.ezuce.media.ui.GraphicUtils;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jivesoftware.Spark;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.spark.ChatManager;

/**
 *
 * @author Razvan
 */
public class EzuceGroupChatRoomPanel extends javax.swing.JPanel implements RosterListener {

    //private EzuceGroupChatBtnPanel ezuceGroupChatBtnPanel;
    private GroupChatConferencePanel audioCallPanel; 
    private EzuceSubjectPanel ezuceSubjectPanel;
    private JButton jButtonLeft;
    private JButton jButtonRight;
    private JPanel jPanel;
    private JPanel jPanelButtons;
    private JPanel jPanelContainer;
    private JPanel jPanelUsers;
    private JScrollPane jScrollPane;
    private JSeparator jSeparator;
    // End of variables declaration//GEN-END:variables

    private final int HORIZONTAL_UNIT_INCREMENT=117;
    private int jPanelUsersPrefferedWidth = 0;
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceGroupChatRoomPanel.class);
    private final ImageIcon background;
    private EzuceGroupChatParticipantList roomInfo;
    private EzuceGroupChatRoom groupChatRoom;
    private EzuceChatHeaderContainer parentHeaderContainer;
    private List<ConferenceMemberXML> confUsers=new ArrayList<ConferenceMemberXML>();
    private ConferenceMemberXML localConfMemberXml;
    private boolean deaf=false;
    private boolean mute=false;
    private boolean deafAll = false;
    private boolean muteAll = false;
    private boolean recording=false;
    private Timer timerLeftScroll;
    private Timer timerRightScroll;
    public EzuceGroupChatRoomPanel() {
        background=resourceMap.getImageIcon("headerBackground");
        initComponents();
        
        initTimers();
        this.jPanelUsers.setPreferredSize(new Dimension(0, (int)this.jPanelUsers.getPreferredSize().getHeight()));
        
        EzucePresenceUtils.registerRosterListener(this); 
    }

    public EzuceGroupChatRoomPanel(EzuceGroupChatParticipantList participants, EzuceGroupChatRoom egcr) {
            this.roomInfo = participants;
            this.groupChatRoom = egcr;
            this.roomInfo.setGroupChatPanel(this);
            background = resourceMap.getImageIcon("headerBackground");

            initComponents();

            this.jScrollPane.getHorizontalScrollBar().setUnitIncrement(HORIZONTAL_UNIT_INCREMENT);
            setActions();
            this.roomInfo.setTransferHandler(this.groupChatRoom
                            .getTranscriptWindow().getTransferHandler());

            initTimers();
            makeVisible(false);
            this.jPanelUsers.setPreferredSize(new Dimension(0,
                            (int) this.jPanelUsers.getPreferredSize().getHeight()));

            EzucePresenceUtils.registerRosterListener(this);
    }

    private void initTimers() {
            timerLeftScroll = new Timer(250, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            EzuceGroupChatRoomPanel.this.movePanelUsers(-1);
                    }
            });
            timerLeftScroll.setInitialDelay(0);

            timerRightScroll = new Timer(250, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            EzuceGroupChatRoomPanel.this.movePanelUsers(1);
                    }
            });
            timerRightScroll.setInitialDelay(0);
    }

    private void setActions() {
            ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(this);

            Action action = actionMap.get("inviteUsersAction");
            action.putValue(Action.SMALL_ICON, audioCallPanel.getBtnAddUser().getIcon());
            audioCallPanel.getBtnAddUser().setAction(action);
            audioCallPanel.getBtnAddUser().setToolTipText(resourceMap.getString("jButtonAddUser.tooltip"));

            action = actionMap.get("escalateToGroupChatAction");
            action.putValue(Action.SMALL_ICON, audioCallPanel.getBtnChatOff().getIcon());
            audioCallPanel.getBtnChatOff().setAction(action);
            audioCallPanel.getBtnChatOff().setToolTipText(resourceMap.getString("jButtonConversation.tooltip"));

            action = actionMap.get("deafAllAction"); 
            action.putValue(Action.SMALL_ICON, audioCallPanel.getBtnPause().getIcon());
            audioCallPanel.getBtnPause().setAction(action);
            audioCallPanel.getBtnPause().setToolTipText(resourceMap.getString("jButtonDeafen.tooltip"));

            action = actionMap.get("muteAllAction");
            action.putValue(Action.SMALL_ICON, audioCallPanel.getBtnMicVolume().getIcon());
            audioCallPanel.getBtnMicVolume().setAction(action);
            audioCallPanel.getBtnMicVolume().setToolTipText(resourceMap.getString("jButtonMicrophone.tooltip"));

            action = actionMap.get("recordAction");
            action.putValue(Action.SMALL_ICON, audioCallPanel.getTglBtnRec().getIcon());
            audioCallPanel.getTglBtnRec().setAction(action);
            audioCallPanel.getTglBtnRec().setToolTipText(resourceMap.getString("jButtonRecord.tooltip"));

            action = actionMap.get("kickAllAction");
            action.putValue(Action.SMALL_ICON, audioCallPanel.getBtnEnd().getIcon());
            audioCallPanel.getBtnEnd().setAction(action);
            audioCallPanel.getBtnEnd().setToolTipText(resourceMap.getString("jbuttonEndCall.tooltip"));

//                ezuceGroupChatBtnPanel.getjButtonAudio().setToolTipText("jButtonAudio.tooltip");

    }

    public EzuceGroupChatRoom getParentChatRoom() {
        return this.groupChatRoom;
    }

    public void refreshParticipantsList() {
        if (this.roomInfo==null) {
            return;
        }
        RebuildConfParticipantsListTask rcplt=new RebuildConfParticipantsListTask(Application.getInstance(), this);            
        rcplt.execute();
    }
    
    public void rebuildParticipantsListGui() {        
        //if there is no user to show for this conference (for some reason), hide the panel and return:
        boolean iamOwner = groupChatRoom.iAmOwner();

		if (!iamOwner || confUsers.isEmpty()) {			
                    //ezuceGroupChatBtnPanel.resetDuration();
                    audioCallPanel.stopCallDurationTimer(); 
                    makeVisible(false);
                    return;
		} 
        else {			
			// If the header panel is not visible yet, make it visible:
			if (!isVisible()) {
				makeVisible(true);
				//verify if recording is in progress...
				boolean notRecording = RestManager.getInstance().isNoRecording(groupChatRoom.getNaturalLanguageName());
                                setRecording(!notRecording);

                                //this.ezuceGroupChatBtnPanel.getjToggleButtonRecord().setSelected(!notRecording);
                                this.audioCallPanel.getTglBtnRec().setSelected(!notRecording);

                                //this.ezuceGroupChatBtnPanel.startCountingDuration();
                                this.audioCallPanel.startCallDurationTimer();  

                                this.audioCallPanel.showHideMoreButtons();
                                 

                                SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                                AudioCallAvatarPanel umpg = new AudioCallAvatarPanel();
                                                umpg.setUserDisplayName(groupChatRoom.getNaturalLanguageName());
                                                umpg.setDescription("Conference room");
                                                umpg.setUserStatus("");
                                                umpg.setUserPicture(null);

                                                audioCallPanel.setAvatarPanel(umpg);
                                        }
                                });
			}			
		}
               
        this.jPanelUsers.removeAll();
                
        for (ConferenceMemberXML cp : confUsers) {        
            String imId = cp.getImId();            
            EzuceChatUserMiniPanel ecump = buildChatUserPanelFromJid(cp, imId, iamOwner);
            ecump.setMemberMuteStatus(!cp.isCanSpeak()); //negate because mute!=canSpeak;
            ecump.setMemberDeafStatus(!cp.isCanHear()); //negate because deaf!=canHear;
            this.addChatUserPanel(ecump);
            ecump.revalidate();
            ecump.repaint();
        }
        this.jPanelUsers.setPreferredSize(this.jPanelUsers.getSize());        
        this.jPanelUsers.revalidate();
        this.jPanelUsers.repaint();
    }    
    
    public void makeVisible(boolean visible) {
            setVisible(visible);
            if (parentHeaderContainer != null) {
                    if (visible) {
                            parentHeaderContainer.setMinimumSize(new Dimension(22, 178));
                            parentHeaderContainer.setSize(parentHeaderContainer.getWidth(), 178);
                            groupChatRoom.scrollToBottom();
                    } else {
                            parentHeaderContainer.setMinimumSize(new Dimension(22, 22));
                            parentHeaderContainer.setMaximumSize(new Dimension(32222, 22));
                            parentHeaderContainer.setSize(parentHeaderContainer.getWidth(), 22);
                    }
                    parentHeaderContainer.repaint();
            }
    }   

    public EzuceChatHeaderContainer getParentHeaderContainer() {
        return parentHeaderContainer;
    }
    public void setParentHeaderContainer(EzuceChatHeaderContainer ecc) {
        this.parentHeaderContainer=ecc;
    }
    
    private EzuceChatUserMiniPanel buildChatUserPanelFromJid(ConferenceMemberXML cp, String jid, boolean localUserIsOwner) {
    	String displayName = cp.getName();
    	String detail = org.apache.commons.lang3.StringUtils.EMPTY;
    	
    	EzuceChatUserMiniPanel ecump=new EzuceChatUserMiniPanel(localUserIsOwner);
        ecump.setConfMemberXml(cp);        
        ecump.setConferenceName(this.groupChatRoom.getNaturalLanguageName());
        ecump.setUserName(displayName);
        ecump.setUserDetail(detail);
        ecump.setConfId(cp.getId());
        
    	if (jid != null) {
    		if (!jid.contains("@")) {
    			jid+=Utils.getJidSuffix();
    		}
    		String localImId=Config.getInstance().getImId()+Utils.getJidSuffix();
    		if (localImId.equals(jid)) {
    			localConfMemberXml = cp;
    		}    		
    		ecump.setUserJid(jid);
    		AsyncLoader.getInstance().execute(jid, ecump);
    	} else {
    		ecump.setUserAvatar(Utils.getDefaultAvatar());
    	}
        
        return ecump;
    }    

    @org.jdesktop.application.Action
    public void inviteUsersAction(ActionEvent ae) 
    {
        //Open ContactSearchDialog, where the user is allowed to use the context menu and invite
        //any contact to bridge:
    	List<ContactListEntry> cles = ContactSearchDialog.showMultiContactSearchDialog(
    			(Frame)SwingUtilities.windowForComponent(this), true, false, OKButtonType.INVITE);
        
    	if (cles == null || cles.isEmpty()) {
        	return;
        }
        
        for (ContactListEntry cle:cles) {
            if (cle instanceof PhoneBookEntryWrapper) {
                String toUser = cle.getNumber();
                String userDisplayName = cle.getUserDisplayName();            	
                if (!EzuceConferenceUtils.isPhonebookUserInConference(confUsers, toUser)) {
                	if (RestManager.getInstance().inviteToBridge(toUser, groupChatRoom.getNaturalLanguageName())) {
                		groupChatRoom.getTranscriptWindow().insertNotificationMessage(resourceMap.getString("invited.message.audiobridge", userDisplayName), ChatManager.NOTIFICATION_COLOR);
                	} else {
                		groupChatRoom.getTranscriptWindow().insertNotificationMessage(resourceMap.getString("not.invited.message.audiobridge", userDisplayName), ChatManager.ERROR_COLOR);
                	}
                }
            } else if (cle instanceof ContactItemWrapper) {
                String toUser = cle.getImId();
            	//this checking applies when no user is selected in contact-search dialog.
            	//We assume that the entered value, in the text field is an IM id
                toUser = isEmpty(toUser) ? cle.getNumber() : toUser; 
                String userDisplayName = cle.getUserDisplayName();
                if (!EzuceConferenceUtils.isImUserInConference(confUsers, toUser)) {
                	if (RestManager.getInstance().inviteToBridgeIm(toUser, groupChatRoom.getNaturalLanguageName())) {
                		groupChatRoom.getTranscriptWindow().insertNotificationMessage(resourceMap.getString("invited.message.audiobridge", userDisplayName), ChatManager.NOTIFICATION_COLOR);
                	} else {
                		groupChatRoom.getTranscriptWindow().insertNotificationMessage(resourceMap.getString("not.invited.message.audiobridge", userDisplayName), ChatManager.ERROR_COLOR);
                	}
                }
            }
         }       
    }    
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(),
                    0,0, background.getIconWidth(), background.getIconHeight(), null);

    }

    public void addChatUserPanel(EzuceChatUserMiniPanel ecump)
    {
        if (ecump==null)
        {
            return;
        }
        this.jPanelUsers.add(ecump);    
        
        int currentWSize=jPanelUsersPrefferedWidth;
        currentWSize+=this.jPanelUsers.getComponentCount()*ecump.getPreferredSize().getWidth();
        
        this.jPanelUsers.setPreferredSize(new Dimension(currentWSize, (int)this.jPanelUsers.getPreferredSize().getHeight()));
        this.jPanelUsers.setSize(this.jPanelUsers.getPreferredSize());
        this.jPanelUsers.setMaximumSize(this.jPanelUsers.getPreferredSize());
        this.jPanelUsers.revalidate();
        this.jPanelUsers.repaint();
        this.jScrollPane.revalidate();
        this.jScrollPane.repaint();        
    }

    public void removeChatUserPanel(EzuceChatUserMiniPanel ecump)
    {
        if (ecump==null)
        {
            return;
        }
        this.jPanelUsers.remove(ecump);
                
        int currentWSize=jPanelUsersPrefferedWidth;
        currentWSize-=this.jPanelUsers.getComponentCount()*ecump.getPreferredSize().getWidth();
        
        if (currentWSize<0)
        {
            currentWSize=0;
        }
        this.jPanelUsers.setPreferredSize(new Dimension(currentWSize, (int)this.jPanelUsers.getPreferredSize().getHeight()));
        this.jPanelUsers.setSize(this.jPanelUsers.getPreferredSize());
        this.jPanelUsers.revalidate();
        this.jPanelUsers.repaint();
        this.jScrollPane.revalidate();
        this.jScrollPane.repaint(); 
    }

    public void removeChateUserPanelAt(int position)
    {
        this.jPanelUsers.remove(position);
       
        int currentWSize=jPanelUsersPrefferedWidth;
        currentWSize-=this.jPanelUsers.getComponentCount()*(new EzuceChatUserMiniPanel()).getPreferredSize().getWidth();
        if (currentWSize<0)
        {
            currentWSize=0;
        }
        this.jPanelUsers.setPreferredSize(new Dimension(currentWSize, (int)this.jPanelUsers.getPreferredSize().getHeight()));
        this.jPanelUsers.setSize(this.jPanelUsers.getPreferredSize());
        this.jPanelUsers.revalidate();
        this.jPanelUsers.repaint();
        this.jScrollPane.revalidate();
        this.jScrollPane.repaint(); 
    }


     protected void movePanelUsers(int direction)
     {
        int newValue=this.jScrollPane.getHorizontalScrollBar().getValue();
        newValue+=(Math.abs(direction)/(direction==0?1:direction))*this.jScrollPane.getHorizontalScrollBar().getUnitIncrement();
        if (newValue<this.jScrollPane.getHorizontalScrollBar().getMinimum())
        {
            newValue=this.jScrollPane.getHorizontalScrollBar().getMinimum();
        }
        else if(newValue>this.jScrollPane.getHorizontalScrollBar().getMaximum())
        {
            newValue=this.jScrollPane.getHorizontalScrollBar().getMaximum();
        }
        this.jScrollPane.getHorizontalScrollBar().setValue(newValue);
      }     

     @org.jdesktop.application.Action
     public void deafSelfAction(ActionEvent ae) {
         DeafUserTask dut=new DeafUserTask(Application.getInstance(), this.groupChatRoom.getNaturalLanguageName(), localConfMemberXml.getId(), !deaf);
         dut.execute();
         deaf=!deaf;
         final String localImId=Config.getInstance().getImId()+Utils.getJidSuffix();
         final EzuceChatUserMiniPanel selfMiniPanel = this.getUserMiniPanelByJid(localImId);
         if (deaf)
         {
//             final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.ezuceGroupChatBtnPanel.getClass());
//             this.ezuceGroupChatBtnPanel.getJButtonDeafen().setIcon(resMap.getIcon("jButtonDeaf.rolloverIcon"));
//             this.ezuceGroupChatBtnPanel.getJButtonDeafen().setRolloverIcon(resMap.getIcon("jButtonDeaf.icon"));
             
             if (selfMiniPanel!=null)
             {
                 final ResourceMap rm = Application.getInstance().getContext().getResourceMap(selfMiniPanel.getClass());
                 selfMiniPanel.getJButtonAudio().setIcon(rm.getIcon("jButtonAudio.rolloverIcon"));
                 selfMiniPanel.getJButtonAudio().setRolloverIcon(rm.getIcon("jButtonAudio.icon"));
             }
             
         }
         else
         {
//             final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.ezuceGroupChatBtnPanel.getClass());
//             this.ezuceGroupChatBtnPanel.getJButtonDeafen().setIcon(resMap.getIcon("jButtonDeaf.icon"));
//             this.ezuceGroupChatBtnPanel.getJButtonDeafen().setRolloverIcon(resMap.getIcon("jButtonDeaf.rolloverIcon"));
             
             if (selfMiniPanel!=null)
             {
                 final ResourceMap rm = Application.getInstance().getContext().getResourceMap(selfMiniPanel.getClass());
                 selfMiniPanel.getJButtonAudio().setIcon(rm.getIcon("jButtonAudio.icon"));
                 selfMiniPanel.getJButtonAudio().setRolloverIcon(rm.getIcon("jButtonAudio.rolloverIcon"));
             }
         }
     }
     
     @org.jdesktop.application.Action
     public void muteSelfAction(ActionEvent ae)
     {
         MuteUserTask mut=new MuteUserTask(Application.getInstance(), this.groupChatRoom.getNaturalLanguageName(), localConfMemberXml.getId(), !mute);
         mut.execute();
         mute=!mute;
         final String localImId=Config.getInstance().getImId()+Utils.getJidSuffix();
         final EzuceChatUserMiniPanel selfMiniPanel = this.getUserMiniPanelByJid(localImId);
         if (mute)
         {
//             final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.ezuceGroupChatBtnPanel.getClass());
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setIcon(resMap.getIcon("jButtonMicrophone.rolloverIcon"));
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setRolloverIcon(resMap.getIcon("jButtonMicrophone.icon"));
             
             if (selfMiniPanel!=null)
             {
                 final ResourceMap rm = Application.getInstance().getContext().getResourceMap(selfMiniPanel.getClass());
                 selfMiniPanel.getJButtonMic().setIcon(rm.getIcon("jButtonMic.rolloverIcon"));
                 selfMiniPanel.getJButtonMic().setRolloverIcon(rm.getIcon("jButtonMic.icon"));
             }
         }
         else
         {
//             final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.ezuceGroupChatBtnPanel.getClass());
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setIcon(resMap.getIcon("jButtonMicrophone.icon"));
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setRolloverIcon(resMap.getIcon("jButtonMicrophone.rolloverIcon"));
             
             if (selfMiniPanel!=null)
             {
                 final ResourceMap rm = Application.getInstance().getContext().getResourceMap(selfMiniPanel.getClass());
                 selfMiniPanel.getJButtonMic().setIcon(rm.getIcon("jButtonMic.icon"));
                 selfMiniPanel.getJButtonMic().setRolloverIcon(rm.getIcon("jButtonMic.rolloverIcon"));
             }
         }
     }
     
     @org.jdesktop.application.Action
     public void deafAllAction(ActionEvent ae)
     {
    	 DeafAllTask dut=new DeafAllTask(Application.getInstance(), this.groupChatRoom.getNaturalLanguageName(), !deafAll);
         dut.execute();
         deafAll=!deafAll;
     }
     
     @org.jdesktop.application.Action
     public void muteAllAction(ActionEvent ae)
     {
         MuteAllTask mut=new MuteAllTask(Application.getInstance(), this.groupChatRoom.getNaturalLanguageName(), !muteAll);
         mut.execute();
         muteAll=!muteAll;
         if (muteAll)
         {
//             final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.ezuceGroupChatBtnPanel.getClass());
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setIcon(resMap.getIcon("jButtonMicrophone.rolloverIcon"));
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setRolloverIcon(resMap.getIcon("jButtonMicrophone.icon"));
         }
         else
         {
//             final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.ezuceGroupChatBtnPanel.getClass());
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setIcon(resMap.getIcon("jButtonMicrophone.icon"));
//             this.ezuceGroupChatBtnPanel.getjButtonMicrophone().setRolloverIcon(resMap.getIcon("jButtonMicrophone.rolloverIcon"));
         }
     }     
     
     @org.jdesktop.application.Action
     public void recordAction(ActionEvent ae)
     {
         RecordConferenceTask rct=new RecordConferenceTask(Application.getInstance(), groupChatRoom.getNaturalLanguageName(), recording);
         rct.execute();
         recording=!recording;
     }
     
     @org.jdesktop.application.Action
     public void kickAllAction(ActionEvent ae)
     {
         KickAllTask kat=new KickAllTask(Application.getInstance(), groupChatRoom.getNaturalLanguageName());
         kat.execute();
     }
          
     @org.jdesktop.application.Action
     public void escalateToGroupChatAction(ActionEvent ae)
     {
         EscalateConfToChatTask ecct=new EscalateConfToChatTask(Application.getInstance(), groupChatRoom);
         ecct.execute();
     }
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ezuceSubjectPanel = new EzuceSubjectPanel(resourceMap.getString("type.conference"), groupChatRoom.getNaturalLanguageName());
        jPanelContainer = new JPanel();
        jPanelButtons = new JPanel();
        
        audioCallPanel = new GroupChatConferencePanel(false);

        jSeparator = new JSeparator();
        jPanel = new JPanel();
        jButtonLeft = new JButton();
        jScrollPane = new JScrollPane();
        jPanelUsers = new JPanel();
        jButtonRight = new JButton();

        setMaximumSize(new Dimension(32767, 150));
        setMinimumSize(new Dimension(20, 150));
        setPreferredSize(new Dimension(439, 150));                

        ezuceSubjectPanel.setBackground(new Color(230, 236, 237));
        ezuceSubjectPanel.setName("ezuceSubjectPanel"); // NOI18N

        jPanelContainer.setBackground(new Color(255, 255, 255));
        jPanelContainer.setBorder(BorderFactory.createLineBorder(new Color(192,192,192)));
        jPanelContainer.setMinimumSize(new Dimension(439, 133));
        jPanelContainer.setName("jPanelContainer"); // NOI18N
        jPanelContainer.setPreferredSize(new Dimension(439, 133));

        customizeAudioCallPanel(); 
        
        jPanelButtons.setOpaque(false);
        jPanelButtons.setFocusable(false);
        jPanelButtons.setMaximumSize(new Dimension(232, 96));
        jPanelButtons.setMinimumSize(new Dimension(204, 96));
        jPanelButtons.setName("jPanelButtons"); // NOI18N
        if (Spark.isMac())
        {
            //jPanelButtons.setPreferredSize(new Dimension(204, 96));
        }
        else
        {
            //jPanelButtons.setPreferredSize(new Dimension(232, 96));
        }
        jPanelButtons.setLayout(new BoxLayout(jPanelButtons, BoxLayout.PAGE_AXIS));

        //ezuceGroupChatBtnPanel.setName("ezuceGroupChatBtnPanel"); // NOI18N
        audioCallPanel.setName("audioCallPanel");
        
        //jPanelButtons.add(ezuceGroupChatBtnPanel);
        jPanelButtons.add(audioCallPanel);

        jSeparator.setOrientation(SwingConstants.VERTICAL);
        jSeparator.setMaximumSize(new Dimension(2, 145));
        jSeparator.setMinimumSize(new Dimension(2, 145));
        jSeparator.setPreferredSize(new Dimension(2, 145));
        jSeparator.setName("jSeparator"); // NOI18N        
        jSeparator.setBorder(BorderFactory.createLineBorder(ColorConstants.SEPARATOR_COLOR_2, 3));

        jPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        jPanel.setOpaque(false);
        jPanel.setName("jPanel"); // NOI18N
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceGroupChatRoomPanel.class);
        jButtonLeft.setIcon(resourceMap.getIcon("jButtonLeft.icon")); // NOI18N
        jButtonLeft.setText(" ");
        jButtonLeft.setBorder(null);
        jButtonLeft.setBorderPainted(false);
        jButtonLeft.setContentAreaFilled(false);
        jButtonLeft.setDoubleBuffered(true);
        jButtonLeft.setFocusPainted(false);
        jButtonLeft.setHideActionText(true);
        jButtonLeft.setHorizontalTextPosition(SwingConstants.LEADING);
        jButtonLeft.setIconTextGap(0);
        jButtonLeft.setName("jButtonLeft"); // NOI18N
        jButtonLeft.setRolloverIcon(resourceMap.getIcon("jButtonLeft.rolloverIcon")); // NOI18N
        jButtonLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonLeftMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonLeftMousePressed(evt);
            }
        });
        jPanel.add(jButtonLeft);

        jScrollPane.setBackground(new Color(255, 255, 255));
        jScrollPane.setBorder(null);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setOpaque(false);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane.setDoubleBuffered(true);
        jScrollPane.setFocusable(false);
        jScrollPane.setName("jScrollPane"); // NOI18N

        jPanelUsers.setBackground(new Color(255, 255, 255));
        jPanelUsers.setMaximumSize(new Dimension(32000, 127));
        jPanelUsers.setMinimumSize(new Dimension(290, 127));
        jPanelUsers.setName("jPanelUsers"); // NOI18N
        jPanelUsers.setPreferredSize(new Dimension(290, 127));
        jPanelUsersPrefferedWidth = 0;//(int)(jPanelUsers.getPreferredSize().getWidth());
        jPanelUsers.setLayout(new BoxLayout(jPanelUsers, BoxLayout.X_AXIS));
        jScrollPane.setViewportView(jPanelUsers);
        this.jScrollPane.getViewport().setOpaque(false);

        jPanel.add(jScrollPane);

        jButtonRight.setIcon(resourceMap.getIcon("jButtonRight.icon")); // NOI18N
        jButtonRight.setText(" ");
        jButtonRight.setBorder(null);
        jButtonRight.setBorderPainted(false);
        jButtonRight.setContentAreaFilled(false);
        jButtonRight.setDoubleBuffered(true);
        jButtonRight.setFocusPainted(false);
        jButtonRight.setHideActionText(true);
        jButtonRight.setIconTextGap(0);
        jButtonRight.setName("jButtonRight"); // NOI18N
        jButtonRight.setRolloverIcon(resourceMap.getIcon("jButtonRight.rolloverIcon")); // NOI18N
        jButtonRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonRightMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonRightMousePressed(evt);
            }
        });
        jPanel.add(jButtonRight);

        GroupLayout jPanelContainerLayout = new GroupLayout(jPanelContainer);
        jPanelContainer.setLayout(jPanelContainerLayout);
        jPanelContainerLayout.setHorizontalGroup(
            jPanelContainerLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelContainerLayout.createSequentialGroup()
                .addComponent(jPanelButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(jSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPanel, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
        );
        jPanelContainerLayout.setVerticalGroup(
            jPanelContainerLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelContainerLayout.createSequentialGroup()
                .addGroup(jPanelContainerLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                )
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(ezuceSubjectPanel, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
            .addComponent(jPanelContainer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ezuceSubjectPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelContainer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	
    private void jButtonLeftMouseReleased(MouseEvent evt) {                                            
        if (this.timerLeftScroll.isRunning())
        {
            this.timerLeftScroll.stop();
        }
    }                                           

    private void jButtonLeftMousePressed(MouseEvent evt) {                                            
        if (!this.timerLeftScroll.isRunning())
        {
            this.timerLeftScroll.start();
        }
    }                                           

    private void jButtonRightMouseReleased(MouseEvent evt) {                                            
        if (this.timerRightScroll.isRunning())
        {
            this.timerRightScroll.stop();
        }
    }                                           

    private void jButtonRightMousePressed(MouseEvent evt) {//GEN-FIRST:event_jButtonLeftActionPerformed
        if (!this.timerRightScroll.isRunning())
        {
            this.timerRightScroll.start();
        }
    }//GEN-LAST:event_jButtonLeftActionPerformed

    public EzuceChatUserMiniPanel getUserMiniPanelByJid(String jid)
    {
        for (Component c:this.jPanelUsers.getComponents())
        {
            if (c instanceof EzuceChatUserMiniPanel)
            {
                if (((EzuceChatUserMiniPanel)c).getUserJid().equals(jid))
                {
                    return ((EzuceChatUserMiniPanel)c);
                }
            }
        }
        return null;
    }

    @Override
    public void entriesAdded(Collection<String> clctn) {

    }

    @Override
    public void entriesUpdated(Collection<String> clctn) {

    }

    @Override
    public void entriesDeleted(Collection<String> clctn) {

    }

    @Override
	public void presenceChanged(Presence prsnc) {
		String bareJid = StringUtils.parseBareAddress(prsnc.getFrom());
		EzuceChatUserMiniPanel ecump = this.getUserMiniPanelByJid(bareJid);
		if (ecump == null) {
			return;
		}
		ecump.updateStatus(prsnc);
		if(Utils.isVCardUpdated(prsnc)) {
			AsyncLoader.getInstance().execute(bareJid, ecump);
		}
	}

    public boolean isDeaf() {
        return deaf;
    }

    public void setDeaf(boolean deaf) {
        this.deaf = deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }
        
    public List<ConferenceMemberXML> getConfUsers() {
        return confUsers;
    }
    
    public void setConfUsers(List<ConferenceMemberXML> conferenceUsers) {
        if (conferenceUsers==null) {
            return;
        }
        this.confUsers=conferenceUsers;
    }

    public EzuceSubjectPanel getEzuceSubjectPanel() {
        return ezuceSubjectPanel;
    }

    public void setEzuceSubjectPanel(EzuceSubjectPanel ezuceSubjectPanel) {
        this.ezuceSubjectPanel = ezuceSubjectPanel;
    }
    
    private void customizeAudioCallPanel() {
        audioCallPanel.customizeForGroupChatHeader();
    } 
}

class GroupChatConferencePanel extends JPanel{
    
    private boolean connected = false;
    private boolean initiating = false;
    private boolean onHold = false;
    private boolean m_video = false;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnAnswer;
    private javax.swing.JButton btnAnswerVideo;
    private javax.swing.JButton btnCallStatus;
    private javax.swing.JButton btnChatOff;
    private javax.swing.JButton btnConference;
    private javax.swing.JButton btnEnd;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanelCallControl;
    private javax.swing.JPanel jPanelCallStatus;
    private javax.swing.JPanel jPanelMainControls;
    private javax.swing.JPanel jPanelMoreBtns;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparatorMore;
    private javax.swing.JPanel panelAvatar;
    private javax.swing.JToggleButton tglBtnCam;
    private javax.swing.JToggleButton tglBtnMicVolume;
    private javax.swing.JToggleButton tglBtnPause;
    private javax.swing.JToggleButton tglBtnRec;
    private javax.swing.JToggleButton tglBtnScreenSharing;
    private javax.swing.JTextField txtCallDuration;                
    private final ImageIcon backgroundIcon = GraphicUtils.createImageIcon("/resources/images/background_call.png");
    private final ImageIcon backgroundIconOnHold = GraphicUtils.createImageIcon("/resources/images/background_call_hold.png");
    private JPanel spp;
    private Timer showCallDurationTimer;
    private int callDuration;        
	    
    public static final Map<AudioCallPanel.CallStatus,Icon> callStatusIcons;
    static {
        Map<AudioCallPanel.CallStatus, Icon> tmp=new EnumMap<AudioCallPanel.CallStatus, Icon>(AudioCallPanel.CallStatus.class);
        tmp.put(AudioCallPanel.CallStatus.RINGING, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/call_ringing.png")));
        tmp.put(AudioCallPanel.CallStatus.INCOMING, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming.png")));
        tmp.put(AudioCallPanel.CallStatus.INCOMING_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming_hd.png")));
        tmp.put(AudioCallPanel.CallStatus.INCOMING_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming_secure.png")));
        tmp.put(AudioCallPanel.CallStatus.INCOMING_HD_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming_secure_hd.png")));
        tmp.put(AudioCallPanel.CallStatus.OUTGOING, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing.png")));
        tmp.put(AudioCallPanel.CallStatus.OUTGOING_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing_hd.png")));
        tmp.put(AudioCallPanel.CallStatus.OUTGOING_SECURED,new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing_secure.png")));
        tmp.put(AudioCallPanel.CallStatus.OUTGOING_HD_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing_secure_hd.png")));
        tmp.put(AudioCallPanel.CallStatus.ONHOLD, null);
        tmp.put(AudioCallPanel.CallStatus.CONFERENCE, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference.png")));        
        tmp.put(AudioCallPanel.CallStatus.CONFERENCE_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference_hd.png")));
        tmp.put(AudioCallPanel.CallStatus.CONFERENCE_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference_secure.png")));
        tmp.put(AudioCallPanel.CallStatus.CONFERENCE_SECURED_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference_secure_hd.png")));
        callStatusIcons=Collections.unmodifiableMap(tmp);
    }  
    
    /**
     * Creates new form GroupChatConferencePanel
     */
    public GroupChatConferencePanel(boolean video) {
    	m_video = video;
        initComponents(); 
        if (!Beans.isDesignTime()){
            initVisibility();
            configureTimer();            
        }  
        this.jXTitledSeparatorMore.setEnabled(true);
    }
        

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="initComponents()">                          
    private void initComponents() {

        jPanelCallControl = new javax.swing.JPanel();
        jPanelCallStatus = new javax.swing.JPanel();
        btnCallStatus = new javax.swing.JButton();
        panelAvatar = new javax.swing.JPanel();
        btnAddUser = new javax.swing.JButton();
        jPanelMainControls = new javax.swing.JPanel();
        btnEnd = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 32767));
        txtCallDuration = new javax.swing.JTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 32767));
        tglBtnMicVolume = new javax.swing.JToggleButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 32767));
        tglBtnPause = new javax.swing.JToggleButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 32767));
        tglBtnCam = new javax.swing.JToggleButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 0), new java.awt.Dimension(3, 32767));
        btnAnswer = new javax.swing.JButton();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767));
        btnAnswerVideo = new javax.swing.JButton();
        jXTitledSeparatorMore = new org.jdesktop.swingx.JXTitledSeparator();
        jPanelMoreBtns = new javax.swing.JPanel();
        btnChatOff = new javax.swing.JButton();
        tglBtnRec = new javax.swing.JToggleButton();        
        btnConference = new javax.swing.JButton();
        tglBtnScreenSharing = new javax.swing.JToggleButton();

        setBackground(new java.awt.Color(215, 232, 236));
        setMaximumSize(new java.awt.Dimension(32767, 135));
        setMinimumSize(new java.awt.Dimension(323, 135));
        setName("Form"); // NOI18N

        jPanelCallControl.setName("jPanelCallControl"); // NOI18N
        jPanelCallControl.setOpaque(false);

        jPanelCallStatus.setMaximumSize(new java.awt.Dimension(64, 53));
        jPanelCallStatus.setMinimumSize(new java.awt.Dimension(64, 53));
        jPanelCallStatus.setName("jPanelCallStatus"); // NOI18N
        jPanelCallStatus.setOpaque(false);
        jPanelCallStatus.setPreferredSize(new java.awt.Dimension(64, 53));
        jPanelCallStatus.setLayout(new javax.swing.BoxLayout(jPanelCallStatus, javax.swing.BoxLayout.Y_AXIS));

        btnCallStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_ringing.png"))); // NOI18N
        btnCallStatus.setBorder(null);
        btnCallStatus.setBorderPainted(false);
        btnCallStatus.setContentAreaFilled(false);
        btnCallStatus.setDoubleBuffered(true);
        btnCallStatus.setFocusPainted(false);
        btnCallStatus.setHideActionText(true);
        btnCallStatus.setName("btnCallStatus"); // NOI18N
        jPanelCallStatus.add(btnCallStatus);

        panelAvatar.setBackground(new java.awt.Color(204, 204, 255));
        panelAvatar.setName("panelAvatar"); // NOI18N
        panelAvatar.setOpaque(false);
        panelAvatar.setLayout(new javax.swing.BoxLayout(panelAvatar, javax.swing.BoxLayout.LINE_AXIS));

        btnAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_add-to-conference_off.png"))); // NOI18N
        btnAddUser.setToolTipText("Add user to conference");
        btnAddUser.setBorder(null);
        btnAddUser.setBorderPainted(false);
        btnAddUser.setContentAreaFilled(false);
        btnAddUser.setDoubleBuffered(true);
        btnAddUser.setFocusPainted(false);
        btnAddUser.setHideActionText(true);
        btnAddUser.setName("btnAddUser"); // NOI18N
        btnAddUser.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_add-to-conference_on.png"))); // NOI18N

        jPanelMainControls.setName("jPanelMainControls"); // NOI18N
        jPanelMainControls.setOpaque(false);
        jPanelMainControls.setLayout(new javax.swing.BoxLayout(jPanelMainControls, javax.swing.BoxLayout.LINE_AXIS));

        btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_end.png"))); // NOI18N
        btnEnd.setToolTipText("Hangup");
        btnEnd.setBorder(null);
        btnEnd.setBorderPainted(false);
        btnEnd.setContentAreaFilled(false);
        btnEnd.setDoubleBuffered(true);
        btnEnd.setFocusPainted(false);
        btnEnd.setHideActionText(true);
        btnEnd.setName("btnEnd"); // NOI18N
        btnEnd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_end.png"))); // NOI18N
        btnEnd.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_end_on.png"))); // NOI18N
        jPanelMainControls.add(btnEnd);

        filler1.setDoubleBuffered(true);
        filler1.setName("filler1"); // NOI18N
        filler1.setRequestFocusEnabled(false);
        jPanelMainControls.add(filler1);

        txtCallDuration.setEditable(false);
        txtCallDuration.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCallDuration.setText("00:00");
        txtCallDuration.setAutoscrolls(false);
        txtCallDuration.setBorder(null);
        txtCallDuration.setDoubleBuffered(true);
        txtCallDuration.setForeground(new java.awt.Color(102, 102, 102));
        txtCallDuration.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        txtCallDuration.setMinimumSize(new java.awt.Dimension(6, 24));
        txtCallDuration.setName("txtCallDuration"); // NOI18N
        txtCallDuration.setPreferredSize(new java.awt.Dimension(56, 24));
        jPanelMainControls.add(txtCallDuration);

        filler2.setName("filler2"); // NOI18N
        jPanelMainControls.add(filler2);

        tglBtnMicVolume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/mute_off.png"))); // NOI18N
        tglBtnMicVolume.setBorder(null);
        tglBtnMicVolume.setBorderPainted(false);
        tglBtnMicVolume.setContentAreaFilled(false);
        tglBtnMicVolume.setDoubleBuffered(true);
        tglBtnMicVolume.setFocusPainted(false);
        tglBtnMicVolume.setHideActionText(true);
        tglBtnMicVolume.setName("tglBtnMicVolume"); // NOI18N
        tglBtnMicVolume.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/mute_on.png"))); // NOI18N
        tglBtnMicVolume.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/mute_on.png"))); // NOI18N
        tglBtnMicVolume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnMicVolumeActionPerformed(evt);
            }
        });
        jPanelMainControls.add(tglBtnMicVolume);

        filler3.setName("filler3"); // NOI18N
        jPanelMainControls.add(filler3);

        tglBtnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/hold_off.png"))); // NOI18N
        tglBtnPause.setBorder(null);
        tglBtnPause.setBorderPainted(false);
        tglBtnPause.setContentAreaFilled(false);
        tglBtnPause.setDoubleBuffered(true);
        tglBtnPause.setFocusPainted(false);
        tglBtnPause.setHideActionText(true);
        tglBtnPause.setName("tglBtnPause"); // NOI18N
        tglBtnPause.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/hold_on1.png"))); // NOI18N
        tglBtnPause.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/hold_on2.png"))); // NOI18N
        tglBtnPause.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/hold_on1.png"))); // NOI18N
        tglBtnPause.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/hold_on1.png"))); // NOI18N
        tglBtnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnPauseActionPerformed(evt);
            }
        });
        jPanelMainControls.add(tglBtnPause);

        filler4.setName("filler4"); // NOI18N
        jPanelMainControls.add(filler4);

        tglBtnCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/video_off.png"))); // NOI18N
        tglBtnCam.setBorder(null);
        tglBtnCam.setBorderPainted(false);
        tglBtnCam.setContentAreaFilled(false);
        tglBtnCam.setDoubleBuffered(true);
        tglBtnCam.setFocusPainted(false);
        tglBtnCam.setHideActionText(true);
        tglBtnCam.setName("tglBtnCam"); // NOI18N
        tglBtnCam.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/video_on.png"))); // NOI18N
        tglBtnCam.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/video_on.png"))); // NOI18N
        jPanelMainControls.add(tglBtnCam);

        filler5.setName("filler5"); // NOI18N
        jPanelMainControls.add(filler5);

        btnAnswer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_start.png"))); // NOI18N
        btnAnswer.setToolTipText("Answer call");
        btnAnswer.setBorder(null);
        btnAnswer.setBorderPainted(false);
        btnAnswer.setContentAreaFilled(false);
        btnAnswer.setDoubleBuffered(true);
        btnAnswer.setFocusPainted(false);
        btnAnswer.setHideActionText(true);
        btnAnswer.setName("btnAnswer"); // NOI18N
        btnAnswer.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_start.png"))); // NOI18N
        btnAnswer.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_start_on.png"))); // NOI18N
        jPanelMainControls.add(btnAnswer);

        filler6.setName("filler6"); // NOI18N
        jPanelMainControls.add(filler6);

        if (m_video) {
            btnAnswerVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_video_start.png"))); // NOI18N
            btnAnswerVideo.setBorder(null);
            btnAnswerVideo.setBorderPainted(false);
            btnAnswerVideo.setContentAreaFilled(false);
            btnAnswerVideo.setDoubleBuffered(true);
            btnAnswerVideo.setFocusPainted(false);
            btnAnswerVideo.setName("btnAnswerVideo"); // NOI18N
            btnAnswerVideo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_video_start.png"))); // NOI18N
            btnAnswerVideo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_video_start_on.png"))); // NOI18N
            jPanelMainControls.add(btnAnswerVideo);
        }

        jXTitledSeparatorMore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/arrow_collapsed.png"))); // NOI18N
        jXTitledSeparatorMore.setTitle("more");
        jXTitledSeparatorMore.setMaximumSize(new java.awt.Dimension(2147483647, 12));
        jXTitledSeparatorMore.setMinimumSize(new java.awt.Dimension(36, 12));
        jXTitledSeparatorMore.setName("jXTitledSeparatorMore"); // NOI18N
        jXTitledSeparatorMore.setPreferredSize(new java.awt.Dimension(36, 12));
        jXTitledSeparatorMore.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jXTitledSeparatorMoreMouseClicked(evt);
            }
        });

        jPanelMoreBtns.setName("jPanelMoreBtns"); // NOI18N
        jPanelMoreBtns.setOpaque(false);
        
        btnChatOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_group-chat_off.png"))); // NOI18N
        btnChatOff.setBorder(null);
        btnChatOff.setBorderPainted(false);
        btnChatOff.setContentAreaFilled(false);
        btnChatOff.setDoubleBuffered(true);
        btnChatOff.setFocusPainted(false);
        btnChatOff.setHideActionText(true);
        btnChatOff.setName("btnChatOff"); // NOI18N
        btnChatOff.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_group-chat_off.png"))); // NOI18N
        btnChatOff.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_group-chat_on.png"))); // NOI18N

        tglBtnRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_record_off.png"))); // NOI18N
        tglBtnRec.setBorder(null);
        tglBtnRec.setBorderPainted(false);
        tglBtnRec.setContentAreaFilled(false);
        tglBtnRec.setDoubleBuffered(true);
        tglBtnRec.setFocusPainted(false);
        tglBtnRec.setHideActionText(true);
        tglBtnRec.setName("tglBtnRec"); // NOI18N
        tglBtnRec.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_record_grey.png"))); // NOI18N
        tglBtnRec.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_record_on.png"))); // NOI18N
        
        btnConference.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/conference_off.png"))); // NOI18N
        btnConference.setBorder(null);
        btnConference.setBorderPainted(false);
        btnConference.setContentAreaFilled(false);
        btnConference.setDoubleBuffered(true);
        btnConference.setFocusPainted(false);
        btnConference.setHideActionText(true);
        btnConference.setName("btnConference"); // NOI18N
        btnConference.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/conference_off.png"))); // NOI18N
        btnConference.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/conference-call_on.png"))); // NOI18N

        tglBtnScreenSharing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/gc_screen-sharing_grey.png"))); // NOI18N
        tglBtnScreenSharing.setBorder(null);
        tglBtnScreenSharing.setBorderPainted(false);
        tglBtnScreenSharing.setContentAreaFilled(false);
        tglBtnScreenSharing.setDoubleBuffered(true);
        tglBtnScreenSharing.setFocusPainted(false);
        tglBtnScreenSharing.setHideActionText(true);
        tglBtnScreenSharing.setName("tglBtnScreenSharing"); // NOI18N
        
        org.jdesktop.layout.GroupLayout jPanelMoreBtnsLayout = new org.jdesktop.layout.GroupLayout(jPanelMoreBtns);
        jPanelMoreBtns.setLayout(jPanelMoreBtnsLayout);
        jPanelMoreBtnsLayout.setHorizontalGroup(
            jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelMoreBtnsLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(btnChatOff)
                .add(2, 2, 2)
                .add(tglBtnScreenSharing, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(btnConference)
                .add(2, 2, 2)
                .add(tglBtnRec)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnAddUser)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)                
                .add(2, 2, 2)
                .add(0, 0, 0))
        );
        jPanelMoreBtnsLayout.setVerticalGroup(
            jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelMoreBtnsLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(btnAddUser)
                        )
                    .add(btnConference)
                    .add(btnChatOff)
                    .add(tglBtnScreenSharing)
                    .add(tglBtnRec)
                    )
                .add(0, 0, 0))
        );

        org.jdesktop.layout.GroupLayout jPanelCallControlLayout = new org.jdesktop.layout.GroupLayout(jPanelCallControl);
        jPanelCallControl.setLayout(jPanelCallControlLayout);
        jPanelCallControlLayout.setHorizontalGroup(
            jPanelCallControlLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCallControlLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(jPanelCallControlLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelCallControlLayout.createSequentialGroup()
                        .add(jPanelMainControls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanelCallControlLayout.createSequentialGroup()
                        .add(jPanelCallControlLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jXTitledSeparatorMore, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanelCallControlLayout.createSequentialGroup()
                                .add(jPanelMoreBtns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)                                
                                .add(0, 0, Short.MAX_VALUE))
                            .add(jPanelCallControlLayout.createSequentialGroup()
                                .add(jPanelCallStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(panelAvatar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(2, 2, 2)
                                .add(btnAddUser)))
                        .add(2, 2, 2))))
        );
        jPanelCallControlLayout.setVerticalGroup(
            jPanelCallControlLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCallControlLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(jPanelCallControlLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelCallControlLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(panelAvatar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanelCallStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(btnAddUser))
                .add(3, 3, 3)
                .add(jPanelMainControls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(jXTitledSeparatorMore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(jPanelMoreBtns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)                
                .add(1, 1, 1))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jPanelCallControl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jPanelCallControl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(3, 3, 3))
        );
    }// </editor-fold>                        

    public void initVisibility()
    {  
        onHold=false;
        getjXTitledSeparatorMore().setEnabled(true); //TRUE : just for test
        getjXTitledSeparatorMore().setForeground(Color.GRAY);
        getBtnAddUser().setVisible(false);
        filler1.setVisible(false);
        filler2.setVisible(false);
        filler3.setVisible(false);
        filler4.setVisible(false);
        filler5.setVisible(false);
        filler6.setVisible(false);
        getTxtCallDuration().setVisible(false); //TRUE : just for test
        getBtnMicVolume().setVisible(false);
        getBtnPause().setVisible(false); //TRUE : just for test
        getBtnAnswer().setVisible(false);
        if (getBtnAnswerVideo() != null) {
        	getBtnAnswerVideo().setVisible(false);
        }
        getBtnEnd().setVisible(true);
        getTglBtnScreenSharing().setVisible(false);
        getTglBtnCam().setVisible(false);
        getjPanelMoreBtns().setVisible(false);
        repaint();
        //startCallDurationTimer(); //UNCOMMENT THIS LINE : just for test
    }
    
    public void customizeForGroupChatHeader()
    {
        setCallStatusIcon(AudioCallPanel.CallStatus.CONFERENCE);
        
        getBtnConference().setVisible(false);
        getTglBtnScreenSharing().setEnabled(false);
        getTglBtnCam().setEnabled(false);              
        
        getBtnAnswer().setVisible(false);
        if (getBtnAnswerVideo() != null) {
        	getBtnAnswerVideo().setVisible(false);
        }
        getBtnPause().setVisible(true);
        getjXTitledSeparatorMore().setEnabled(true);
        getjXTitledSeparatorMore().setVisible(true);
        getjXTitledSeparatorMore().setForeground(Color.BLACK);
        getTxtCallDuration().setVisible(true);
        filler1.setVisible(true);
        filler2.setVisible(true);
        filler3.setVisible(true);
        filler4.setVisible(true);
        filler5.setVisible(true);
        filler6.setVisible(false);
        getBtnMicVolume().setVisible(true);
        getBtnEnd().setVisible(true);
        getTglBtnScreenSharing().setVisible(true);
        getTglBtnCam().setVisible(true);
        getTglBtnRec().setVisible(true);
        getBtnAddUser().setVisible(true);
        getBtnChatOff().setVisible(true);
        
        repaint();
        connected = true;
        initiating = false;
        onHold = false;
    }
    
    
    public JButton getBtnChatOff() {
        return btnChatOff;
    }

    public JButton getBtnConference() {
        return btnConference;
    }

    public JToggleButton getTglBtnRec() {
        return tglBtnRec;
    }
    
    public void addAnswerAction(ActionListener answerAction) {
    	btnAnswer.addActionListener(answerAction);
    }
    
    public void addMuteUnmuteAction(ActionListener muteUnmuteAction) {
    	tglBtnMicVolume.addActionListener(muteUnmuteAction);
    }
    
    public void addInviteToConferenceAction(ActionListener inviteConfAction) {
    	btnConference.addActionListener(inviteConfAction);
    }
    
    public void addAnswerVideoAction(ActionListener answerAction) {
    	if (btnAnswerVideo != null) {
    		btnAnswerVideo.addActionListener(answerAction);
    	}
    }
    
    public void addHangupAction(ActionListener hangupAction) {
    	btnEnd.addActionListener(hangupAction);
    }
    
    public void addCameraAction(ActionListener cameraAction) {
    	tglBtnCam.addActionListener(cameraAction);
    }
    
    public void addScreenSharingAction(ActionListener screenSharingAction) {
    	tglBtnScreenSharing.addActionListener(screenSharingAction);
    }
    
    public void addOnHoldAction(ActionListener onOffAction) {
    	tglBtnPause.addActionListener(onOffAction);
    }
    
    public void addRecordCallAction(ActionListener recordAction) {
    	tglBtnRec.addActionListener(recordAction);
    }

    
    
    /**
     * Rebuilds the panel holding the main controls of the call, displayed just
     * below the avatar.
     */
    public void rebuildJPanelMainControls() {
        onHold = false;
        jPanelCallStatus.remove(txtCallDuration);
        jPanelCallStatus.remove(spp);

        jPanelCallStatus.add(btnCallStatus);

        btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/resources/images/call_end.png"))); // NOI18N
        btnEnd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/resources/images/call_end.png"))); // NOI18N
        btnEnd.setRolloverIcon(new javax.swing.ImageIcon(getClass()
                        .getResource("/resources/images/call_end_on.png"))); // NOI18N

        txtCallDuration.setBackground(null);

        jPanelMainControls.add(btnEnd);
        jPanelMainControls.add(filler1);
        jPanelMainControls.add(txtCallDuration);
        jPanelMainControls.add(filler2);
        jPanelMainControls.add(tglBtnMicVolume);
        jPanelMainControls.add(filler3);
        jPanelMainControls.add(tglBtnPause);
        jPanelMainControls.add(filler4);
        jPanelMainControls.add(tglBtnCam);
        jPanelMainControls.add(filler5);
        jPanelMainControls.add(btnAnswer);
        jPanelMainControls.add(filler6);
        jPanelMainControls.add(btnAnswerVideo);

        filler1.setVisible(true);
        filler2.setVisible(true);
        filler3.setVisible(true);
        filler4.setVisible(true);
        filler5.setVisible(true);
        filler6.setVisible(true);
        jPanelMainControls.repaint();
        jPanelMainControls.revalidate();
        revalidate();
        repaint();
    }
        
    public void setCallStatusIcon(AudioCallPanel.CallStatus status){
        btnCallStatus.setIcon(callStatusIcons.get(status)); // NOI18N
    }                
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon ii=null;
        if (!onHold)
        {
            ii=backgroundIcon;
        }
        else
        {
            ii=backgroundIconOnHold;
        }
        if (ii != null) {
            Image i = ii.getImage();
            if (i != null) {
                g.drawImage(i, 0, 0, getWidth(), getHeight(),
                        0, 0, ii.getIconWidth(), ii.getIconHeight(), null);
            }
        }
        
    }

    private void tglBtnPauseActionPerformed(java.awt.event.ActionEvent evt) {
        
    }
    
    private void tglBtnMicVolumeActionPerformed(java.awt.event.ActionEvent evt) {                                                
        
    }                                               

    private void jXTitledSeparatorMoreMouseClicked(java.awt.event.MouseEvent evt) {                                                   
        if (this.jXTitledSeparatorMore.isEnabled()) {
            showHideMoreButtons();
        }
    }                                                  
    
    public void showHideMoreButtons() {
		if (!this.jPanelMoreBtns.isVisible()) {
			this.jXTitledSeparatorMore
					.setIcon(GraphicUtils
							.createImageIcon("/resources/images/arrow_uncollapsed.png"));
		} else {
			this.jXTitledSeparatorMore.setIcon(new javax.swing.ImageIcon(
					getClass().getResource(
							"/resources/images/arrow_collapsed.png")));
		}
		this.jPanelMoreBtns.setVisible(!this.jPanelMoreBtns.isVisible());
                
                this.revalidate();
                this.repaint();		
	}
    
    private void configureTimer() {
            final NumberFormat sdf = new  DecimalFormat("#00");
            showCallDurationTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                            callDuration++;
                                            txtCallDuration.setText(sdf.format(callDuration/60)+
                            ":"+
                            sdf.format(callDuration%60));
                                    }
                            });

                    }
            });
            showCallDurationTimer.setInitialDelay(0);
    }

    public void startCallDurationTimer() {
		if (showCallDurationTimer == null) {
			configureTimer();
		}
		showCallDurationTimer.start();
	}
   
    public void stopCallDurationTimer() {
	   if (showCallDurationTimer != null) {
		   showCallDurationTimer.stop();
	   }
   }
     
    public JPanel getPanelAvatar() {
       return this.panelAvatar;
    }    

    /**
     * @return the btnOutgoingCall
     */
    public javax.swing.JButton getBtnCallStatus() {
        return btnCallStatus;
    }

    /**
     * @return the tglBtnCam
     */
    public javax.swing.JToggleButton getTglBtnCam() {
        return tglBtnCam;
    }
    
    public JToggleButton getBtnPause() {
        return tglBtnPause;
    }

    /**
     * @return the btnScreenSharing
     */
    public javax.swing.JToggleButton getTglBtnScreenSharing() {
        return tglBtnScreenSharing;
    }

    /**
     * @return the btnEnd
     */
    public javax.swing.JButton getBtnEnd() {
        return btnEnd;
    }
    
    public JButton getBtnAnswer() {
        return btnAnswer;
    }
    
    public JToggleButton getBtnMicVolume() {
        return tglBtnMicVolume;
    }

    public JPanel getjPanelMoreBtns() {
        return jPanelMoreBtns;
    }

    public JXTitledSeparator getjXTitledSeparatorMore() {
        return jXTitledSeparatorMore;
    }

    public JTextField getTxtCallDuration() {
        return txtCallDuration;
    }
    
    public JButton getBtnAddUser() {
    	return btnAddUser;
    }     
    
    public javax.swing.JButton getBtnAnswerVideo() {
		return btnAnswerVideo;
	}

    public void setAvatarPanel(Component c)
    {
        this.getPanelAvatar().removeAll();
        this.getPanelAvatar().add(c);
        this.getPanelAvatar().revalidate();
        this.getPanelAvatar().repaint();
    }
    
    public boolean isConnected() {
		return connected;
	}

    public boolean isInitiating() {
            return initiating;
    }

    public boolean isOnHold() {
            return onHold;
    }            
    
}
