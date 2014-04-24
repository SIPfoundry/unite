package sources;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Map;
import javax.swing.*;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXTitledSeparator;

public class AudioCallJPanel extends javax.swing.JPanel {

    /**
     * Creates new form AudioCallJPanel
     */
    public AudioCallJPanel() {
        
        initComponents();                               
        if (!Beans.isDesignTime())
        {
            initVisibility(); //Comment this line for test.
            configureTimer();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        jXTitledSeparatorMore = new org.jdesktop.swingx.JXTitledSeparator();
        jPanelMoreBtns = new javax.swing.JPanel();
        btnSearchTransfer = new javax.swing.JButton();
        btnChatOff = new javax.swing.JButton();
        tglBtnRec = new javax.swing.JToggleButton();
        btnTransfer = new javax.swing.JButton();
        btnConference = new javax.swing.JButton();
        tglBtnScreenSharing = new javax.swing.JToggleButton();
        txtTransfer = new javax.swing.JTextField();

        setBackground(new java.awt.Color(215, 232, 236));
        setMaximumSize(new java.awt.Dimension(32767, 135));
        setMinimumSize(new java.awt.Dimension(323, 104));
        setName("Form"); // NOI18N
        setOpaque(false);

        jPanelCallControl.setName("jPanelCallControl"); // NOI18N
        jPanelCallControl.setOpaque(false);

        jPanelCallStatus.setMaximumSize(new java.awt.Dimension(64, 53));
        jPanelCallStatus.setMinimumSize(new java.awt.Dimension(64, 53));
        jPanelCallStatus.setName("jPanelCallStatus"); // NOI18N
        jPanelCallStatus.setOpaque(false);
        jPanelCallStatus.setPreferredSize(new java.awt.Dimension(64, 53));
        jPanelCallStatus.setLayout(new javax.swing.BoxLayout(jPanelCallStatus, javax.swing.BoxLayout.Y_AXIS));

        btnCallStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_ringing.png"))); // NOI18N
        btnCallStatus.setBorder(null);
        btnCallStatus.setBorderPainted(false);
        btnCallStatus.setContentAreaFilled(false);
        btnCallStatus.setDoubleBuffered(true);
        btnCallStatus.setFocusPainted(false);
        btnCallStatus.setHideActionText(true);
        btnCallStatus.setName("btnCallStatus"); // NOI18N
        jPanelCallStatus.add(btnCallStatus);

        panelAvatar.setBackground(new java.awt.Color(204, 204, 255));
        panelAvatar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelAvatar.setName("panelAvatar"); // NOI18N
        panelAvatar.setOpaque(false);

        org.jdesktop.layout.GroupLayout panelAvatarLayout = new org.jdesktop.layout.GroupLayout(panelAvatar);
        panelAvatar.setLayout(panelAvatarLayout);
        panelAvatarLayout.setHorizontalGroup(
            panelAvatarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        panelAvatarLayout.setVerticalGroup(
            panelAvatarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        btnAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/add-contact_off.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("sources/resources/AudioCallJPanel"); // NOI18N
        btnAddUser.setText(bundle.getString("AudioCallJPanel.btnAddUser.text")); // NOI18N
        btnAddUser.setToolTipText(bundle.getString("AudioCallJPanel.btnAddUser.toolTipText")); // NOI18N
        btnAddUser.setBorder(null);
        btnAddUser.setBorderPainted(false);
        btnAddUser.setContentAreaFilled(false);
        btnAddUser.setDoubleBuffered(true);
        btnAddUser.setFocusPainted(false);
        btnAddUser.setHideActionText(true);
        btnAddUser.setName("btnAddUser"); // NOI18N
        btnAddUser.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/add-contact_on.png"))); // NOI18N

        jPanelMainControls.setName("jPanelMainControls"); // NOI18N
        jPanelMainControls.setOpaque(false);
        jPanelMainControls.setLayout(new javax.swing.BoxLayout(jPanelMainControls, javax.swing.BoxLayout.LINE_AXIS));

        btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end.png"))); // NOI18N
        btnEnd.setToolTipText(bundle.getString("AudioCallJPanel.btnEnd.toolTipText")); // NOI18N
        btnEnd.setBorder(null);
        btnEnd.setBorderPainted(false);
        btnEnd.setContentAreaFilled(false);
        btnEnd.setDoubleBuffered(true);
        btnEnd.setFocusPainted(false);
        btnEnd.setHideActionText(true);
        btnEnd.setName("btnEnd"); // NOI18N
        btnEnd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end.png"))); // NOI18N
        btnEnd.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end_on.png"))); // NOI18N
        jPanelMainControls.add(btnEnd);

        filler1.setDoubleBuffered(true);
        filler1.setName("filler1"); // NOI18N
        filler1.setRequestFocusEnabled(false);
        jPanelMainControls.add(filler1);

        txtCallDuration.setEditable(false);
        txtCallDuration.setForeground(new java.awt.Color(102, 102, 102));
        txtCallDuration.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCallDuration.setText(bundle.getString("AudioCallJPanel.txtCallDuration.text")); // NOI18N
        txtCallDuration.setAutoscrolls(false);
        txtCallDuration.setBorder(null);
        txtCallDuration.setDoubleBuffered(true);
        txtCallDuration.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        txtCallDuration.setMinimumSize(new java.awt.Dimension(6, 24));
        txtCallDuration.setName("txtCallDuration"); // NOI18N
        txtCallDuration.setPreferredSize(new java.awt.Dimension(56, 24));
        jPanelMainControls.add(txtCallDuration);

        filler2.setName("filler2"); // NOI18N
        jPanelMainControls.add(filler2);

        tglBtnMicVolume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/mute_off.png"))); // NOI18N
        tglBtnMicVolume.setBorder(null);
        tglBtnMicVolume.setBorderPainted(false);
        tglBtnMicVolume.setContentAreaFilled(false);
        tglBtnMicVolume.setDoubleBuffered(true);
        tglBtnMicVolume.setFocusPainted(false);
        tglBtnMicVolume.setHideActionText(true);
        tglBtnMicVolume.setName("tglBtnMicVolume"); // NOI18N
        tglBtnMicVolume.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/mute_on.png"))); // NOI18N
        tglBtnMicVolume.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/mute_on.png"))); // NOI18N
        tglBtnMicVolume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnMicVolumeActionPerformed(evt);
            }
        });
        jPanelMainControls.add(tglBtnMicVolume);

        filler3.setName("filler3"); // NOI18N
        jPanelMainControls.add(filler3);

        tglBtnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/hold_off.png"))); // NOI18N
        tglBtnPause.setBorder(null);
        tglBtnPause.setBorderPainted(false);
        tglBtnPause.setContentAreaFilled(false);
        tglBtnPause.setDoubleBuffered(true);
        tglBtnPause.setFocusPainted(false);
        tglBtnPause.setHideActionText(true);
        tglBtnPause.setName("tglBtnPause"); // NOI18N
        tglBtnPause.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/hold_on1.png"))); // NOI18N
        tglBtnPause.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/hold_on2.png"))); // NOI18N
        tglBtnPause.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/hold_on1.png"))); // NOI18N
        tglBtnPause.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/hold_on1.png"))); // NOI18N
        tglBtnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnPauseActionPerformed(evt);
            }
        });
        jPanelMainControls.add(tglBtnPause);

        filler4.setName("filler4"); // NOI18N
        jPanelMainControls.add(filler4);

        tglBtnCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/video_off.png"))); // NOI18N
        tglBtnCam.setBorder(null);
        tglBtnCam.setBorderPainted(false);
        tglBtnCam.setContentAreaFilled(false);
        tglBtnCam.setDoubleBuffered(true);
        tglBtnCam.setFocusPainted(false);
        tglBtnCam.setHideActionText(true);
        tglBtnCam.setName("tglBtnCam"); // NOI18N
        tglBtnCam.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/video_on.png"))); // NOI18N
        tglBtnCam.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/video_on.png"))); // NOI18N
        jPanelMainControls.add(tglBtnCam);

        filler5.setName("filler5"); // NOI18N
        jPanelMainControls.add(filler5);

        btnAnswer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_start.png"))); // NOI18N
        btnAnswer.setToolTipText(bundle.getString("AudioCallJPanel.btnAnswer.toolTipText")); // NOI18N
        btnAnswer.setBorder(null);
        btnAnswer.setBorderPainted(false);
        btnAnswer.setContentAreaFilled(false);
        btnAnswer.setDoubleBuffered(true);
        btnAnswer.setFocusPainted(false);
        btnAnswer.setHideActionText(true);
        btnAnswer.setName("btnPause"); // NOI18N
        btnAnswer.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_start.png"))); // NOI18N
        btnAnswer.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_start_on.png"))); // NOI18N
        jPanelMainControls.add(btnAnswer);

        jXTitledSeparatorMore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/arrow_collapsed.png"))); // NOI18N
        jXTitledSeparatorMore.setTitle(bundle.getString("AudioCallJPanel.jXTitledSeparatorMore.title")); // NOI18N
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

        btnSearchTransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/transfer-ask_off.png"))); // NOI18N
        btnSearchTransfer.setBorder(null);
        btnSearchTransfer.setBorderPainted(false);
        btnSearchTransfer.setContentAreaFilled(false);
        btnSearchTransfer.setDoubleBuffered(true);
        btnSearchTransfer.setFocusPainted(false);
        btnSearchTransfer.setHideActionText(true);
        btnSearchTransfer.setName("btnQ"); // NOI18N
        btnSearchTransfer.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/transfer-ask_off.png"))); // NOI18N
        btnSearchTransfer.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/transfer-ask_on.png"))); // NOI18N

        btnChatOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/chat_off.png"))); // NOI18N
        btnChatOff.setBorder(null);
        btnChatOff.setBorderPainted(false);
        btnChatOff.setContentAreaFilled(false);
        btnChatOff.setDoubleBuffered(true);
        btnChatOff.setFocusPainted(false);
        btnChatOff.setHideActionText(true);
        btnChatOff.setName("btnChatOff"); // NOI18N
        btnChatOff.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/chat_off.png"))); // NOI18N
        btnChatOff.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/chat_on.png"))); // NOI18N

        tglBtnRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/record_off.png"))); // NOI18N
        tglBtnRec.setBorder(null);
        tglBtnRec.setBorderPainted(false);
        tglBtnRec.setContentAreaFilled(false);
        tglBtnRec.setDoubleBuffered(true);
        tglBtnRec.setFocusPainted(false);
        tglBtnRec.setHideActionText(true);
        tglBtnRec.setName("tglBtnRec"); // NOI18N

        btnTransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/transfer_off.png"))); // NOI18N
        btnTransfer.setBorder(null);
        btnTransfer.setBorderPainted(false);
        btnTransfer.setContentAreaFilled(false);
        btnTransfer.setDoubleBuffered(true);
        btnTransfer.setFocusPainted(false);
        btnTransfer.setHideActionText(true);
        btnTransfer.setName("btnTransfer"); // NOI18N
        btnTransfer.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/transfer_off.png"))); // NOI18N
        btnTransfer.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/transfer_on.png"))); // NOI18N

        btnConference.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/conference_off.png"))); // NOI18N
        btnConference.setBorder(null);
        btnConference.setBorderPainted(false);
        btnConference.setContentAreaFilled(false);
        btnConference.setDoubleBuffered(true);
        btnConference.setFocusPainted(false);
        btnConference.setHideActionText(true);
        btnConference.setName("btnConference"); // NOI18N
        btnConference.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/conference_off.png"))); // NOI18N
        btnConference.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/conference-call_on.png"))); // NOI18N

        tglBtnScreenSharing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/screen-sharing_off.png"))); // NOI18N
        tglBtnScreenSharing.setBorder(null);
        tglBtnScreenSharing.setBorderPainted(false);
        tglBtnScreenSharing.setContentAreaFilled(false);
        tglBtnScreenSharing.setDoubleBuffered(true);
        tglBtnScreenSharing.setFocusPainted(false);
        tglBtnScreenSharing.setHideActionText(true);
        tglBtnScreenSharing.setName("tglBtnScreenSharing"); // NOI18N

        txtTransfer.setName("txtTransfer"); // NOI18N

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
                .add(btnTransfer)
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
                        .add(btnTransfer)
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
                                .add(0, 9, Short.MAX_VALUE))
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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initializes the visibility of all child-components of AudioCallJPanel.
     * This is the initial state of this control. From this state, 
     * the component can go to:
     * - call incoming;
     * - call on hold;
     * - call outgoing;
     */
    public void initVisibility(){                
        getjXTitledSeparatorMore().setEnabled(false); //TRUE : just for test
        getjXTitledSeparatorMore().setForeground(Color.GRAY);
        getBtnAddUser().setVisible(false);
        getTxtCallDuration().setVisible(false); //TRUE : just for test
        getBtnMicVolume().setVisible(false);
        getBtnPause().setVisible(false); //TRUE : just for test
        getBtnAnswer().setVisible(false);
        getBtnEnd().setVisible(true);
        getTglBtnScreenSharing().setVisible(false);
        getTglBtnCam().setVisible(false);
        getjPanelMoreBtns().setVisible(false);
        
        //startCallDurationTimer(); //UNCOMMENT THIS LINE : just for test
    }
    
    /**
     * Modifies the AudioCallJPanel GUI to display the call in progress state.
     * This should receive, in the future, an indication if the call is an
     * incoming one, or an outgoing one, so that the correct icon is used.
     */
    public void updateGuiCallInProgress(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {  
                setCallStatusIcon(CallStatus.INCOMING);
                getBtnAnswer().setVisible(false);
                getBtnPause().setVisible(true);
                getjXTitledSeparatorMore().setEnabled(true);
                getjXTitledSeparatorMore().setForeground(Color.BLACK);
                getTxtCallDuration().setVisible(true);
                getBtnMicVolume().setVisible(true);
                getBtnEnd().setVisible(true);
                getTglBtnScreenSharing().setVisible(true);
                getTglBtnCam().setVisible(true);
            }
        });
    }
    
    /**
     * Modifies the AudioCallJPanel GUI to display the incoming call state.
     */
    public void updateGuiCallIsIncoming(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {  
                setCallStatusIcon(CallStatus.RINGING);
                getBtnAnswer().setVisible(true);
                getBtnPause().setVisible(false);
                getTxtCallDuration().setVisible(false);
                getBtnMicVolume().setVisible(false);
                getjXTitledSeparatorMore().setEnabled(false);
                getjXTitledSeparatorMore().setForeground(Color.GRAY);
                getBtnEnd().setVisible(true);
                getTglBtnScreenSharing().setVisible(false);
                getTglBtnCam().setVisible(false);
            }
        });
    }
            
     /**
     * Modifies the AudioCallJPanel GUI to display the call on hold state.
     * The hangup button is split into two - hangup and pause.
     * The call status is replaced with the duration text field, featuring a 
     * yellow background.
     */
    public void updateGuiCallIsOnHold(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {                                                              
                btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end_small.png"))); // NOI18N
                btnEnd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end_small.png"))); // NOI18N
                btnEnd.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end_small_on.png"))); // NOI18N                

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

                jPanelCallStatus.remove(btnCallStatus);

                
                jPanelCallStatus.add(txtCallDuration);
                
                stopPauseJPanel = new JPanel();        
                stopPauseJPanel.setLayout(new BoxLayout(stopPauseJPanel, BoxLayout.LINE_AXIS));
                stopPauseJPanel.setOpaque(false);
                
                stopPauseJPanel.add(btnEnd);
                stopPauseJPanel.add(filler2);
                stopPauseJPanel.add(tglBtnPause);
                jPanelCallStatus.add(stopPauseJPanel);
                
                AudioCallJPanel.this.revalidate();
                AudioCallJPanel.this.repaint();

                }
                        }); 
    }
    
    /**
     * Modifies the AudioCallJPanel GUI to display the call on hold state.
     */
    public void updateGuiCallIsOutgoing(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {  
                setCallStatusIcon(CallStatus.OUTGOING);
                getjXTitledSeparatorMore().setEnabled(true); 
                getjXTitledSeparatorMore().setForeground(Color.BLACK);
                getBtnAddUser().setVisible(false);
                getTxtCallDuration().setVisible(true); 
                getBtnMicVolume().setVisible(true);
                getBtnPause().setVisible(true); 
                getBtnAnswer().setVisible(false);
                getBtnEnd().setVisible(true);
                getTglBtnScreenSharing().setVisible(false);
                getTglBtnCam().setVisible(true);                
            }
        });
    }
    
    /**
     * Rebuilds the panel holding the main controls of the call, displayed just
     * below the avatar.
     */
    private void rebuildJPanelMainControls(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    jPanelCallStatus.remove(txtCallDuration);
                    jPanelCallStatus.remove(stopPauseJPanel);

                    jPanelCallStatus.add(btnCallStatus);                

                    btnEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end.png"))); // NOI18N
                    btnEnd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end.png"))); // NOI18N
                    btnEnd.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/call_end_on.png"))); // NOI18N

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

                    AudioCallJPanel.this.revalidate();
                    AudioCallJPanel.this.repaint();                                       
                }
            }); 
    }
        
    public void setCallStatusIcon(CallStatus status)
    {
        btnCallStatus.setIcon(callStatusIcons.get(status)); // NOI18N
    }
    
    private void tglBtnMicVolumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnMicVolumeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglBtnMicVolumeActionPerformed

    private void jXTitledSeparatorMoreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jXTitledSeparatorMoreMouseClicked
        if (this.jXTitledSeparatorMore.isEnabled())
        {
            if (!this.jPanelMoreBtns.isVisible())
            {
                this.jXTitledSeparatorMore.setIcon(resourceMap.getIcon("arrow_uncollapsed"));
            }
            else
            {
                this.jXTitledSeparatorMore.setIcon(resourceMap.getIcon("arrow_collapsed"));
            }
            this.jPanelMoreBtns.setVisible(!this.jPanelMoreBtns.isVisible());      
        }
    }//GEN-LAST:event_jXTitledSeparatorMoreMouseClicked

    /**
     * This method should be overridden in the container of AudioCallJPanel.
     * @param evt 
     */
    private void tglBtnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnPauseActionPerformed
        if (tglBtnPause.isSelected())
        {
            //Put call on Hold.
            
            //When the corresponding event is generated, the method updateGuiCallIsOnHold
            //will be called.
            updateGuiCallIsOnHold(); // JUST FOR TEST!            
        }
        else
        {
            //Resume call.
            
            // JUST FOR TEST - until the corresponding event is generated, 
            //rebuild the GUI as if the call is resumed.
           rebuildJPanelMainControls();//THIS IS JUST FOR TEST           
        }
    }//GEN-LAST:event_tglBtnPauseActionPerformed
   
    
    private void configureTimer() {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            showCallDurationTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                callDuration.setTimeInMillis(callDuration.getTimeInMillis()+1000);
                                String duration = sdf.format(callDuration.getTime());
                                txtCallDuration.setText(duration);
                            }
                        });
                        
                    }
            });
            showCallDurationTimer.setInitialDelay(0);            
    }
   
    public void startCallDurationTimer()
    {
        if (showCallDurationTimer==null)
        {
            configureTimer();
        }
        callDuration = new GregorianCalendar(1970, 0, 0, 0, 0, 0);        
        showCallDurationTimer.start();
    }
   
   public void stopCallDurationTimer()
   {
       showCallDurationTimer.stop();
   }
       
    public JPanel getPanelAvatar()
   {
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

    public JTextField getTxtTransfer() {
        return txtTransfer;
    }                
    
    public JButton getBtnAddUser() {
        return btnAddUser;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnAnswer;
    private javax.swing.JButton btnCallStatus;
    private javax.swing.JButton btnChatOff;
    private javax.swing.JButton btnConference;
    private javax.swing.JButton btnEnd;
    private javax.swing.JButton btnSearchTransfer;
    private javax.swing.JButton btnTransfer;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
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
    private javax.swing.JTextField txtTransfer;
    // End of variables declaration//GEN-END:variables
    private final java.awt.Color onHoldBkgColor = new java.awt.Color(250, 245, 125);
    private JPanel stopPauseJPanel;
    private Timer showCallDurationTimer;
    private GregorianCalendar callDuration;
    final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(AudioCallJPanel.class);
    public static enum CallStatus { RINGING, INCOMING, INCOMING_HD, INCOMING_SECURED, INCOMING_HD_SECURED, OUTGOING, OUTGOING_HD, OUTGOING_SECURED, OUTGOING_HD_SECURED, ONHOLD }
    public static final Map<CallStatus,Icon> callStatusIcons;
    static {
        Map<CallStatus, Icon> tmp=new EnumMap<CallStatus, Icon>(CallStatus.class);
        tmp.put(CallStatus.RINGING, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/call_ringing.png")));
        tmp.put(CallStatus.INCOMING, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/incoming.png")));
        tmp.put(CallStatus.INCOMING_HD, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/incoming_hd.png")));
        tmp.put(CallStatus.INCOMING_SECURED, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/incoming_secure.png")));
        tmp.put(CallStatus.INCOMING_HD_SECURED, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/incoming_secure_hd.png")));
        tmp.put(CallStatus.OUTGOING, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/outgoing.png")));
        tmp.put(CallStatus.OUTGOING_HD, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/outgoing_hd.png")));
        tmp.put(CallStatus.OUTGOING_SECURED, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/outgoing_secure.png")));
        tmp.put(CallStatus.OUTGOING_HD_SECURED, new javax.swing.ImageIcon(AudioCallJPanel.class.getResource("/sources/resources/outgoing_secure_hd.png")));
        tmp.put(CallStatus.ONHOLD, null);
        callStatusIcons=Collections.unmodifiableMap(tmp);
    }
    
    

}