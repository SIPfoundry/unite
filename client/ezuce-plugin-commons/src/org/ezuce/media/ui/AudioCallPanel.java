package org.ezuce.media.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.ezuce.common.EzuceButtonFactory;
import org.ezuce.common.EzuceChatRoomButton;
import org.ezuce.common.ui.wrappers.interfaces.AudioCallPanelCommonInterface;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.media.manager.UnitemediaEventListener;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.util.log.Log;

public class AudioCallPanel extends JPanel implements AudioCallPanelCommonInterface {
	private boolean connected = false;
	private boolean initiating = false;
	private boolean onHold = false;
	private boolean m_video = false;
	private ActionListener screenSharingAction = null;
	private UnitemediaEventListener parentToUpdate = null;
	
	// Variables declaration - do not modify                     
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnAnswer;
    private javax.swing.JButton btnAnswerVideo;
    private javax.swing.JButton btnCallStatus;
    private javax.swing.JButton btnChatOff;
    private javax.swing.JButton btnConference;
    private javax.swing.JButton btnEnd;
    private javax.swing.JButton btnSearchTransfer;
    private javax.swing.JButton btnTransferOrAddToConf;
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
    private EzuceChatRoomButton tglBtnScreenSharing;
    private javax.swing.JTextField txtCallDuration;
    private javax.swing.JTextField txtTransfer;
    // End of variables declaration                   
    private final java.awt.Color onHoldBkgColor = new java.awt.Color(250, 245, 125);    
    private final ImageIcon backgroundIcon = GraphicUtils.createImageIcon("/resources/images/background_call.png");
    private final ImageIcon backgroundIconOnHold = GraphicUtils.createImageIcon("/resources/images/background_call_hold.png");
    private JPanel stopPausePanel;
    private JPanel spp;
    private Timer showCallDurationTimer;
    private int callDuration;
	
    public static enum CallStatus { RINGING, INCOMING, INCOMING_HD, INCOMING_SECURED, INCOMING_HD_SECURED, OUTGOING, OUTGOING_HD, OUTGOING_SECURED, OUTGOING_HD_SECURED, ONHOLD, CONFERENCE, CONFERENCE_HD, CONFERENCE_SECURED, CONFERENCE_SECURED_HD }
    public static final Map<CallStatus,Icon> callStatusIcons;
    static {
        Map<CallStatus, Icon> tmp=new EnumMap<CallStatus, Icon>(CallStatus.class);
        tmp.put(CallStatus.RINGING, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/call_ringing.png")));
        tmp.put(CallStatus.INCOMING, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming.png")));
        tmp.put(CallStatus.INCOMING_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming_hd.png")));
        tmp.put(CallStatus.INCOMING_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming_secure.png")));
        tmp.put(CallStatus.INCOMING_HD_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/incoming_secure_hd.png")));
        tmp.put(CallStatus.OUTGOING, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing.png")));
        tmp.put(CallStatus.OUTGOING_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing_hd.png")));
        tmp.put(CallStatus.OUTGOING_SECURED,new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing_secure.png")));
        tmp.put(CallStatus.OUTGOING_HD_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/outgoing_secure_hd.png")));
        tmp.put(CallStatus.ONHOLD, null);
        tmp.put(CallStatus.CONFERENCE, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference.png")));        
        tmp.put(CallStatus.CONFERENCE_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference_hd.png")));
        tmp.put(CallStatus.CONFERENCE_SECURED, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference_secure.png")));
        tmp.put(CallStatus.CONFERENCE_SECURED_HD, new javax.swing.ImageIcon(AudioCallPanel.class.getResource("/resources/images/conference_secure_hd.png")));
        callStatusIcons=Collections.unmodifiableMap(tmp);
    }  
    
    /**
     * Creates new form AudioCallPanel
     */
    public AudioCallPanel(boolean video) {
    	m_video = video;
        initComponents(); 
        if (!Beans.isDesignTime()){
            initVisibility();
            configureTimer();            
        }
        
        this.txtTransfer.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
              updateButton();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
              updateButton();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
              updateButton();
            }

            public void updateButton() {
               btnTransferOrAddToConf.setActionCommand(txtTransfer.getText());
            }
          });      
    }

    @Override
    public void setVisible(boolean v){
    	super.setVisible(v);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        btnSearchTransfer = new javax.swing.JButton();
        btnChatOff = new javax.swing.JButton();
        tglBtnRec = new javax.swing.JToggleButton();
        btnTransferOrAddToConf = new javax.swing.JButton();
        btnConference = new javax.swing.JButton();
        tglBtnScreenSharing = EzuceButtonFactory.getInstance().createScreenSharingButton();
        txtTransfer = new javax.swing.JTextField();

        setBackground(new java.awt.Color(215, 232, 236));
        setMaximumSize(new java.awt.Dimension(32767, 135));
        setMinimumSize(new java.awt.Dimension(323, 104));
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
        btnCallStatus.setBorder(BorderFactory.createEmptyBorder());
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

        btnAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add-contact_off.png"))); // NOI18N
        btnAddUser.setToolTipText(Res.getString("button.addUserToPhonebook"));
        btnAddUser.setBorder(null);
        btnAddUser.setBorderPainted(false);
        btnAddUser.setContentAreaFilled(false);
        btnAddUser.setDoubleBuffered(true);
        btnAddUser.setFocusPainted(false);
        btnAddUser.setHideActionText(true);
        btnAddUser.setName("btnAddUser"); // NOI18N
        btnAddUser.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add-contact_on.png"))); // NOI18N

        jPanelMainControls.setName("jPanelMainControls"); // NOI18N
        jPanelMainControls.setOpaque(false);
        jPanelMainControls.setLayout(new javax.swing.BoxLayout(jPanelMainControls, javax.swing.BoxLayout.LINE_AXIS));

        btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/call_end.png"))); // NOI18N
        btnEnd.setToolTipText(Res.getString("button.endCall"));
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
        tglBtnMicVolume.setToolTipText(Res.getString("button.microphoneVolume"));
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
        tglBtnPause.setToolTipText(Res.getString("button.onHold"));
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
        tglBtnCam.setToolTipText(Res.getString("button.startVideoCall"));
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
        btnAnswer.setToolTipText(Res.getString("button.answerCall"));
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

        btnSearchTransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/transfer-ask_off.png"))); // NOI18N
        btnSearchTransfer.setBorder(null);
        btnSearchTransfer.setBorderPainted(false);
        btnSearchTransfer.setContentAreaFilled(false);
        btnSearchTransfer.setDoubleBuffered(true);
        btnSearchTransfer.setFocusPainted(false);
        btnSearchTransfer.setHideActionText(true);
        btnSearchTransfer.setName("btnQ"); // NOI18N
        btnSearchTransfer.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/transfer-ask_off.png"))); // NOI18N
        btnSearchTransfer.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/transfer-ask_on.png"))); // NOI18N
        btnSearchTransfer.setToolTipText(Res.getString("button.search.transfer"));
        
        btnChatOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/phone_chat_off.png"))); // NOI18N
        btnChatOff.setBorder(null);
        btnChatOff.setBorderPainted(false);
        btnChatOff.setContentAreaFilled(false);
        btnChatOff.setDoubleBuffered(true);
        btnChatOff.setFocusPainted(false);
        btnChatOff.setHideActionText(true);
        btnChatOff.setName("btnChatOff"); // NOI18N
        btnChatOff.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/phone_chat_off.png"))); // NOI18N
        btnChatOff.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/phone_chat_on.png"))); // NOI18N
        btnChatOff.setToolTipText(Res.getString("button.toggleChat"));

        tglBtnRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/record_off.png"))); // NOI18N
        tglBtnRec.setBorder(null);
        tglBtnRec.setBorderPainted(false);
        tglBtnRec.setContentAreaFilled(false);
        tglBtnRec.setDoubleBuffered(true);
        tglBtnRec.setFocusPainted(false);
        tglBtnRec.setHideActionText(true);
        tglBtnRec.setName("tglBtnRec"); // NOI18N
        tglBtnRec.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/record_grey.png"))); // NOI18N
        tglBtnRec.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/record_on.png"))); // NOI18N
        tglBtnRec.setToolTipText(Res.getString("button.recordCall"));

        btnTransferOrAddToConf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/transfer_off.png"))); // NOI18N
        btnTransferOrAddToConf.setBorder(null);
        btnTransferOrAddToConf.setBorderPainted(false);
        btnTransferOrAddToConf.setContentAreaFilled(false);
        btnTransferOrAddToConf.setDoubleBuffered(true);
        btnTransferOrAddToConf.setFocusPainted(false);
        btnTransferOrAddToConf.setHideActionText(true);
        btnTransferOrAddToConf.setName("btnTransfer"); // NOI18N
        btnTransferOrAddToConf.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/transfer_off.png"))); // NOI18N
        btnTransferOrAddToConf.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/transfer_on.png"))); // NOI18N
        btnTransferOrAddToConf.setToolTipText(Res.getString("button.transferOrAddToConf"));

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
        btnConference.setToolTipText(Res.getString("button.startConf"));
        
        tglBtnScreenSharing.setName("tglBtnScreenSharing"); // NOI18N        
        tglBtnScreenSharing.setBorder(null);
        tglBtnScreenSharing.setBorderPainted(false);
        tglBtnScreenSharing.setContentAreaFilled(false);
        tglBtnScreenSharing.setDoubleBuffered(true);
        tglBtnScreenSharing.setFocusPainted(false);
        tglBtnScreenSharing.setHideActionText(true);
        tglBtnScreenSharing.setToolTipText(Res.getString("button.toggleScreenSharing"));
        

        txtTransfer.setName("txtTransfer"); // NOI18N
        txtTransfer.setToolTipText(Res.getString("textfield.transferToNumber"));
        
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
                .add(btnTransferOrAddToConf)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtTransfer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .add(2, 2, 2)
                .add(btnSearchTransfer)
                .add(0, 0, 0))
        );
        jPanelMoreBtnsLayout.setVerticalGroup(
            jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelMoreBtnsLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelMoreBtnsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(btnTransferOrAddToConf)
                        .add(txtTransfer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnSearchTransfer))
                    .add(btnConference)
                    .add(btnChatOff)
                    .add(tglBtnScreenSharing)
                    .add(tglBtnRec))
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
                .add(1, 1, 1)
                .add(jPanelMoreBtns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2))
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

    public JButton getBtnChatOff() {
        return btnChatOff;
    }

    public JButton getBtnConference() {
        return btnConference;
    }

    public JButton getBtnSearchTransfer() {
        return btnSearchTransfer;
    }

    public JButton getBtnTransferOrAddToConf() {
        return btnTransferOrAddToConf;
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
    
    public void addInviteNewUserToConferenceAction(ActionListener inviteConfAction){
    	btnTransferOrAddToConf.addActionListener(inviteConfAction);
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
    
    public void addScreenSharingAction(ActionListener action) {
    	if (screenSharingAction == null) {
    		screenSharingAction = action;
    	    tglBtnScreenSharing.addActionListener(screenSharingAction);
    	} else {
    		Log.warning("Screen sharing listener already added");
    	}
    }
    
    public void addOnHoldAction(ActionListener onOffAction) {
    	tglBtnPause.addActionListener(onOffAction);
    }
    
    public void addRecordCallAction(ActionListener recordAction) {
    	tglBtnRec.addActionListener(recordAction);
    }
    
    public void addTransferCallAction(ActionListener transferAction){        
        this.btnTransferOrAddToConf.addActionListener(transferAction);
    }
    
    public void initVisibility(){  
        onHold=false;
        getjXTitledSeparatorMore().setEnabled(false); //TRUE : just for test
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
    }
    
    public void updateGuiCallIsInConference() {
        setCallStatusIcon(AudioCallPanel.CallStatus.CONFERENCE);        
                
        getBtnConference().setVisible(false);               
        
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
        getTglBtnScreenSharing().setEnabled(false);
        getTglBtnScreenSharing().setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/screen-sharing_grey.png"))); // NOI18N
              
        getTglBtnCam().setVisible(true);
        getTglBtnRec().setVisible(true);
        
        getBtnAddUser().setVisible(false);
        
        getBtnTransferOrAddToConf().setVisible(true);
        getBtnTransferOrAddToConf().setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add-to-conference_off.png"))); // NOI18N
        getBtnTransferOrAddToConf().setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add-to-conference_on.png"))); // NOI18N
        getBtnTransferOrAddToConf().setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add-to-conference_grey.png")));
        //TODO: ! MUST SET ACTION LISTENER BUT NEED REFERENCE TO CONF !//
        
                
        getBtnChatOff().setVisible(true);
        getBtnChatOff().setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/group-chat_off.png"))); // NOI18N
        getBtnChatOff().setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/group-chat_off.png"))); // NOI18N
        getBtnChatOff().setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/group-chat_on.png"))); // NOI18N
        
        getTxtTransfer().setVisible(false);
        getBtnSearchTransfer().setVisible(false);
                       
        
        AudioCallAvatarPanel umpg = new AudioCallAvatarPanel();
        umpg.setUserDisplayName("Conf owner");
        umpg.setDescription("Conference room");
        umpg.setUserStatus("");
        umpg.setUserPicture(null);

        this.setAvatarPanel(umpg);
        connected = true;
        initiating = false;
        onHold = false;
    }
    
    /**
     * Modifies the AudioCallPanel GUI to display the call in progress state.
     * This should receive, in the future, an indication if the call is an
     * incoming one, or an outgoing one, so that the correct icon is used.
     */
    public void updateGuiIncomingCallInProgress() {
		setCallStatusIcon(CallStatus.INCOMING);
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
		connected = true;
		initiating = false;
		onHold = false;
	}
    
    
    public void updateGuiOutgoingCallInProgress() {
		setCallStatusIcon(CallStatus.OUTGOING);
		getBtnAnswer().setVisible(false);
                if (getBtnAnswerVideo() != null) {
                        getBtnAnswerVideo().setVisible(false);
                }
		filler1.setVisible(true);
		filler2.setVisible(true);
		filler3.setVisible(true);
		filler4.setVisible(true);
		filler5.setVisible(true);
                filler6.setVisible(false);
		getBtnPause().setVisible(true);
		getjXTitledSeparatorMore().setVisible(true);
		getjXTitledSeparatorMore().setEnabled(true);
		getjXTitledSeparatorMore().setForeground(Color.BLACK);
		getTxtCallDuration().setVisible(true);
		getBtnMicVolume().setVisible(true);
		getBtnEnd().setVisible(true);
		getTglBtnScreenSharing().setVisible(true);
		getTglBtnCam().setVisible(true);
		connected = true;
		initiating = false;
		onHold = false;
	}

    
    /**
     * Modifies the AudioCallPanel GUI to display the incoming call state.
     */
    public void updateGuiCallIsIncoming() {
		setCallStatusIcon(CallStatus.RINGING);
		getBtnAnswer().setVisible(true);
                if (getBtnAnswerVideo() != null) {
                        getBtnAnswerVideo().setVisible(true);
                }
		filler1.setVisible(true);
		filler2.setVisible(false);
		filler3.setVisible(false);
		filler4.setVisible(false);
		filler5.setVisible(false);
                
                filler6.setVisible(true);
                
		getBtnPause().setVisible(false);
		getTxtCallDuration().setVisible(false);
		getBtnMicVolume().setVisible(false);
		getjXTitledSeparatorMore().setEnabled(false);
		getjXTitledSeparatorMore().setVisible(false);
		getjXTitledSeparatorMore().setForeground(Color.GRAY);
		getjPanelMoreBtns().setVisible(false);
		getBtnEnd().setVisible(true);
		getTglBtnScreenSharing().setVisible(false);
		getTglBtnCam().setVisible(false);
		revalidate();
		repaint();
		connected = false;
		initiating = true;
		onHold = false;
	}
            
     /**
     * Modifies the AudioCallPanel GUI to display the call on hold state.
     * The hangup button is split into two - hangup and pause.
     * The call status is replaced with the duration text field, featuring a 
     * yellow background.
     */
    public void updateGuiCallIsOnHold() {
		btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/resources/images/call_end_small.png"))); // NOI18N
		btnEnd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/resources/images/call_end_small.png"))); // NOI18N
		btnEnd.setRolloverIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/resources/images/call_end_small_on.png"))); // NOI18N

		txtCallDuration.setBackground(onHoldBkgColor);

		jPanelMainControls.remove(btnEnd);
		jPanelMainControls.remove(filler1);
		jPanelMainControls.remove(txtCallDuration);
		jPanelMainControls.remove(filler2);
		jPanelMainControls.remove(tglBtnMicVolume);
		jPanelMainControls.remove(filler3);
		jPanelMainControls.remove(tglBtnPause);
		jPanelMainControls.remove(filler4);
		jPanelMainControls.remove(tglBtnCam);
		jPanelMainControls.remove(filler5);
		jPanelMainControls.remove(btnAnswer);
                jPanelMainControls.remove(filler6);
		jPanelMainControls.remove(btnAnswerVideo);

		jPanelCallStatus.remove(btnCallStatus);
		jPanelCallStatus.add(txtCallDuration);
                
		stopPausePanel = new JPanel();
		stopPausePanel.setLayout(new BoxLayout(stopPausePanel,
				BoxLayout.LINE_AXIS));
		stopPausePanel.setOpaque(false);
                
		stopPausePanel.add(btnEnd);
		stopPausePanel.add(filler1);
		stopPausePanel.add(tglBtnPause);
                
                spp = new JPanel();
                spp.setLayout(new BoxLayout(spp, BoxLayout.PAGE_AXIS));
                spp.setOpaque(false);
                
                Box.Filler fill = new Box.Filler(new Dimension(0,1), new Dimension(0,1), new Dimension(0,1));
                spp.add(fill);
                spp.add(stopPausePanel);
                
                
		jPanelCallStatus.add(spp);
                
		getjXTitledSeparatorMore().setEnabled(false);
		getjXTitledSeparatorMore().setVisible(false);
		getjXTitledSeparatorMore().setForeground(Color.GRAY);
		getjPanelMoreBtns().setVisible(false);
		jPanelMainControls.repaint();
		jPanelMainControls.revalidate();
		revalidate();
		repaint();
		connected = false;
		initiating = false;
		onHold = true;
	}
    
    /**
     * Modifies the AudioCallPanel GUI to display the call on hold state.
     */
    public void updateGuiCallIsOutgoing() {
		setCallStatusIcon(CallStatus.RINGING);
		getBtnAnswer().setVisible(false);
                if (getBtnAnswerVideo() != null) {
                        getBtnAnswerVideo().setVisible(false);
                }
		getBtnPause().setVisible(false);
		getTxtCallDuration().setVisible(false);
		filler1.setVisible(false);
		filler2.setVisible(false);
		filler3.setVisible(false);
		filler4.setVisible(false);
		filler5.setVisible(false);
                filler6.setVisible(false);
		getBtnMicVolume().setVisible(false);
		getjXTitledSeparatorMore().setVisible(false);
		getjXTitledSeparatorMore().setEnabled(false);
		getjXTitledSeparatorMore().setForeground(Color.GRAY);
		getjPanelMoreBtns().setVisible(false);
		getBtnEnd().setVisible(true);
		getTglBtnScreenSharing().setVisible(false);
		getTglBtnCam().setVisible(false);
		repaint();
		connected = false;
		initiating = true;
		onHold = false;
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

    public void setCallStatusIcon(CallStatus status){
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
            if(parentToUpdate != null) {
            	parentToUpdate.customize();
            }
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
    public EzuceChatRoomButton getTglBtnScreenSharing() {
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

    public JTextField getTxtTransfer() {
        return txtTransfer;
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
          
    @Override
    public void displayPhonebookUser(final ContactListEntry cle) {
            SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                       AudioCallAvatarPanel umpg = new AudioCallAvatarPanel(cle);
                       umpg.setUserDisplayName(cle.getUserDisplayName());
                       umpg.setDescription(cle.getDescription());
                       umpg.setUserStatus("");
                       setAvatarPanel(umpg);
                    }
            });
    }

    @Override
    public void displayNonPhonebookUser(final String nbr) {
            SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                            AudioCallAvatarPanel umpg = new AudioCallAvatarPanel();
                            umpg.setUserDisplayName(nbr);
                            umpg.setDescription("");
                            umpg.setUserStatus("");
                            umpg.setUserPicture(GraphicUtils
                                            .createImageIcon("/resources/images/unknown_user.png"));

                            AudioCallPanel.this.setAvatarPanel(umpg);
                    }
            });
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

    public ActionListener getScreenSharingAction() {
		return screenSharingAction;
	}

	public UnitemediaEventListener getParentToUpdate() {
		return parentToUpdate;
	}

	public void setParentToUpdate(UnitemediaEventListener parentToUpdate) {
		this.parentToUpdate = parentToUpdate;
	}
    
}
