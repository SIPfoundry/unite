package org.ezuce.panels;

import java.io.IOException;
import org.ezuce.wrappers.interfaces.HistoryItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.Beans;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.sound.sampled.Clip;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.ezuce.common.call.events.SoundEvent;
import org.ezuce.common.call.events.SoundListener;
import org.ezuce.call.events.VoicemailListener;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.rest.Voicemail;
import org.ezuce.common.ui.CounterLabel;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.lafs.EzuceVmSliderUI;
import org.ezuce.paints.LinearGradientPainter;
import org.ezuce.popups.VoicemailPopUp;
import org.ezuce.utils.DateUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jivesoftware.Spark;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

/**
 * Displays information specific to voice message history event.
 * 
*/
public class VoiceMessageMiniPanel extends JPanel implements HistoryItem, ControllerListener {

    /**
     * Creates new form VoiceMessageMiniPanel
     */
    public VoiceMessageMiniPanel() {
        initComponents();
    }

    public VoiceMessageMiniPanel(Voicemail vm) {
        this.voicemail = vm;
        initComponents();
        customize();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        GridBagConstraints gridBagConstraints;

        jxPanelBackground = new JXPanel();
        jxLabelIconVoiceMessage = new JLabel();
        jXPanelDetails = new JXPanel();
        jPanelUsernameTimestamp = new JPanel();
        jLabelTimestamp = new JLabel();
        jLabelUsername = new JLabel();
        jPanelPlayMessage = new JPanel();
        jButtonPlay = new JButton();
        jSlider = new JSlider();
        jLabelDuration = new JLabel();
        jPanelButtons = new JPanel();
        jButtonCallBack = new JButton();
        jButtonDeleteMsg = new JButton();

        setBorder(new LineBorder(new Color(177, 177, 177), 1, true));
        setMaximumSize(new Dimension(32767, 59));
        setMinimumSize(new Dimension(356, 59));
        setPreferredSize(new Dimension(356, 59));
        addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                formMouseExited(evt);
            }
        });

        jxPanelBackground.setBackgroundPainter((this.voicemail != null && this.voicemail.isHeard()) ? matteBackgroundHeardLight : matteBackgroundLight);
        jxPanelBackground.setMaximumSize(new Dimension(39000, 57));
        jxPanelBackground.setMinimumSize(new Dimension(334, 57));
        jxPanelBackground.setName("jxPanelBackground"); // NOI18N
        jxPanelBackground.setPreferredSize(new Dimension(334, 57));

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(VoiceMessageMiniPanel.class);
        jxLabelIconVoiceMessage.setIcon(resourceMap.getIcon("jxLabelIconVoiceMessage.icon")); // NOI18N
        jxLabelIconVoiceMessage.setToolTipText("Voice message");
        jxLabelIconVoiceMessage.setVerticalAlignment(SwingConstants.TOP);
        jxLabelIconVoiceMessage.setDoubleBuffered(true);
        jxLabelIconVoiceMessage.setName("jxLabelIconVoiceMessage"); // NOI18N

        jXPanelDetails.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
        jXPanelDetails.setOpaque(false);
        jXPanelDetails.setMaximumSize(new Dimension(35300, 56));
        jXPanelDetails.setMinimumSize(new Dimension(280, 53));
        jXPanelDetails.setName("jXPanelDetails"); // NOI18N

        jPanelUsernameTimestamp.setOpaque(false);
        jPanelUsernameTimestamp.setName("jPanelUsernameTimestamp"); // NOI18N

        jLabelTimestamp.setForeground(new Color(153, 153, 153));
        jLabelTimestamp.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelTimestamp.setText("00:00 am");
        jLabelTimestamp.setDoubleBuffered(true);
        jLabelTimestamp.setFont(new Font("Arial", 0, 10));
        jLabelTimestamp.setName("jLabelTimestamp"); // NOI18N

        jLabelUsername.setForeground(new Color(46, 85, 102));
        jLabelUsername.setIcon(resourceMap.getIcon("jLabelUsername.icon")); // NOI18N
        jLabelUsername.setText("User name");
        jLabelUsername.setDoubleBuffered(true);
        jLabelUsername.setFont(new Font("Arial", 1, 12));
        jLabelUsername.setName("jLabelUsername"); // NOI18N

        GroupLayout jPanelUsernameTimestampLayout = new GroupLayout(jPanelUsernameTimestamp);
        jPanelUsernameTimestamp.setLayout(jPanelUsernameTimestampLayout);
        jPanelUsernameTimestampLayout.setHorizontalGroup(
                jPanelUsernameTimestampLayout.createParallelGroup(Alignment.LEADING).addGroup(jPanelUsernameTimestampLayout.createSequentialGroup().addComponent(jLabelUsername, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, 76, Short.MAX_VALUE).addComponent(jLabelTimestamp, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanelUsernameTimestampLayout.setVerticalGroup(
                jPanelUsernameTimestampLayout.createParallelGroup(Alignment.LEADING).addComponent(jLabelUsername, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE).addComponent(jLabelTimestamp, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE));

        jPanelPlayMessage.setOpaque(false);
        jPanelPlayMessage.setName("jPanelPlayMessage"); // NOI18N
        jPanelPlayMessage.setPreferredSize(new Dimension(250, 19));

        jButtonPlay.setIcon(resourceMap.getIcon("jButtonPlay.icon")); // NOI18N
        jButtonPlay.setToolTipText(resourceMap.getString("play.message"));
        jButtonPlay.setBorder(null);
        jButtonPlay.setBorderPainted(false);
        jButtonPlay.setContentAreaFilled(false);
        jButtonPlay.setDoubleBuffered(true);
        jButtonPlay.setFocusPainted(false);
        jButtonPlay.setHideActionText(true);
        jButtonPlay.setName("jButtonPlay"); // NOI18N
        jButtonPlay.setRolloverIcon(resourceMap.getIcon("jButtonPlay.rolloverIcon")); // NOI18N
        jButtonPlay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jButtonPlayActionPerformed(evt);
            }
        });

        jSlider.setOpaque(false);
        jSlider.setValue(0);
        jSlider.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jSlider.setDoubleBuffered(true);
        jSlider.setFocusable(false);
        jSlider.setMaximumSize(new Dimension(190, 23));
        jSlider.setMinimumSize(new Dimension(190, 23));
        jSlider.setName("jSlider"); // NOI18N
        jSlider.setPreferredSize(new Dimension(190, 23));
        jSlider.setUI(new EzuceVmSliderUI(jSlider));

        if (Spark.isLinux()) {
            jLabelDuration.setFont(new Font("Arial", 0, 11));
        }
        else{
            jLabelDuration.setFont(new Font("Arial", 0, 12));
        }
        jLabelDuration.setForeground(new Color(153, 153, 153));
        jLabelDuration.setText("00:00");
        jLabelDuration.setDoubleBuffered(true);
        jLabelDuration.setName("jLabelDuration"); // NOI18N

        GroupLayout jPanelPlayMessageLayout = new GroupLayout(jPanelPlayMessage);
        jPanelPlayMessage.setLayout(jPanelPlayMessageLayout);
        jPanelPlayMessageLayout.setHorizontalGroup(
                jPanelPlayMessageLayout.createParallelGroup(Alignment.LEADING).addGroup(jPanelPlayMessageLayout.createSequentialGroup().addComponent(jButtonPlay).addPreferredGap(ComponentPlacement.RELATED).addComponent(jSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(jLabelDuration, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)));
        jPanelPlayMessageLayout.setVerticalGroup(
                jPanelPlayMessageLayout.createParallelGroup(Alignment.LEADING).addComponent(jButtonPlay, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE).addComponent(jSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabelDuration, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE));

        //jPanelButtons.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        //jPanelButtons.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        jPanelButtons.setOpaque(false);
        jPanelButtons.setMaximumSize(new Dimension(60, 47));
        jPanelButtons.setPreferredSize(new Dimension(60, 32));
        jPanelButtons.setName("jPanelButtons"); // NOI18N
        //jPanelButtons.setLayout(new GridBagLayout());
        jPanelButtons.setLayout(new javax.swing.BoxLayout(jPanelButtons, javax.swing.BoxLayout.LINE_AXIS));

        ActionMap actionMap = Application.getInstance().getContext().getActionMap(VoiceMessageMiniPanel.class, this);
        jButtonCallBack.setAction(actionMap.get("Call")); // NOI18N
        jButtonCallBack.setIcon(resourceMap.getIcon("jButtonCallBack.icon")); // NOI18N
        jButtonCallBack.setBorder(null);
        jButtonCallBack.setBorderPainted(false);
        jButtonCallBack.setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        jButtonCallBack.setMargin(new java.awt.Insets(0, 2, 0, 8));
        jButtonCallBack.setMaximumSize(new java.awt.Dimension(36, 26));
        jButtonCallBack.setMinimumSize(new java.awt.Dimension(36, 26));
        jButtonCallBack.setPreferredSize(new java.awt.Dimension(36, 26));
        jButtonCallBack.setContentAreaFilled(false);
        jButtonCallBack.setDoubleBuffered(true);
        jButtonCallBack.setFocusPainted(false);
        jButtonCallBack.setHideActionText(true);
        jButtonCallBack.setName("jButtonCallBack"); // NOI18N
        jButtonCallBack.setRolloverIcon(resourceMap.getIcon("jButtonCallBack.rolloverIcon")); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        jPanelButtons.add(jButtonCallBack);//, gridBagConstraints);

        jButtonDeleteMsg.setIcon(resourceMap.getIcon("jButtonDeleteMsg.icon")); // NOI18N
        jButtonDeleteMsg.setToolTipText("Delete message");
        jButtonDeleteMsg.setBorder(null);
        jButtonDeleteMsg.setBorderPainted(false);
        jButtonDeleteMsg.setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        jButtonDeleteMsg.setMargin(new java.awt.Insets(0, 0, 0, 12));
        jButtonDeleteMsg.setMaximumSize(new java.awt.Dimension(38, 26));
        jButtonDeleteMsg.setMinimumSize(new java.awt.Dimension(38, 26));
        jButtonDeleteMsg.setPreferredSize(new java.awt.Dimension(38, 26));
        jButtonDeleteMsg.setContentAreaFilled(false);
        jButtonDeleteMsg.setDoubleBuffered(true);
        jButtonDeleteMsg.setFocusPainted(false);
        jButtonDeleteMsg.setHideActionText(true);
        jButtonDeleteMsg.setName("jButtonDeleteMsg"); // NOI18N
        jButtonDeleteMsg.setRolloverIcon(resourceMap.getIcon("jButtonDeleteMsg.rolloverIcon")); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        jPanelButtons.add(jButtonDeleteMsg);//, gridBagConstraints);

        GroupLayout jXPanelDetailsLayout = new GroupLayout(jXPanelDetails);
        jXPanelDetails.setLayout(jXPanelDetailsLayout);
        jXPanelDetailsLayout.setHorizontalGroup(
                jXPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jXPanelDetailsLayout.createSequentialGroup()
                            .addComponent(jPanelUsernameTimestamp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)     
                            .addContainerGap())
                .addGroup(jXPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
                            .addGroup(jXPanelDetailsLayout.createSequentialGroup()
                                        .addComponent(jPanelPlayMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(79, 79, 79)))
                .addGroup(jXPanelDetailsLayout.createParallelGroup(Alignment.TRAILING)
                            .addGroup(Alignment.TRAILING, jXPanelDetailsLayout.createSequentialGroup()
                                                            .addContainerGap(jPanelPlayMessage.getPreferredSize().width-10, Short.MAX_VALUE)
                                                            .addComponent(jPanelButtons, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))));
        jXPanelDetailsLayout.setVerticalGroup(
                jXPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(jXPanelDetailsLayout.createSequentialGroup()
                                                    .addComponent(jPanelUsernameTimestamp, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addContainerGap(33, Short.MAX_VALUE))
                                        .addGroup(jXPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
                                                    .addGroup(jXPanelDetailsLayout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addComponent(jPanelPlayMessage, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jXPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
                                                    .addGroup(jXPanelDetailsLayout.createSequentialGroup()
                                                                .addGap(21, 21, 21)
                                                                .addComponent(jPanelButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

        GroupLayout jxPanelBackgroundLayout = new GroupLayout(jxPanelBackground);
        jxPanelBackground.setLayout(jxPanelBackgroundLayout);
        jxPanelBackgroundLayout.setHorizontalGroup(
                jxPanelBackgroundLayout.createParallelGroup(Alignment.LEADING).addGroup(jxPanelBackgroundLayout.createSequentialGroup().addComponent(jxLabelIconVoiceMessage).addPreferredGap(ComponentPlacement.RELATED).addComponent(jXPanelDetails, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jxPanelBackgroundLayout.setVerticalGroup(
                jxPanelBackgroundLayout.createParallelGroup(Alignment.LEADING).addGroup(jxPanelBackgroundLayout.createSequentialGroup().addGroup(jxPanelBackgroundLayout.createParallelGroup(Alignment.LEADING).addComponent(jxLabelIconVoiceMessage).addComponent(jXPanelDetails, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING).addComponent(jxPanelBackground, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING).addComponent(jxPanelBackground, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        this.setComponentPopupMenu(new VoicemailPopUp(this));
    }//GEN-END:initComponents

    private void formMouseEntered(MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        if (this.voicemail != null && this.voicemail.isHeard()) {
            this.jxPanelBackground.setBackgroundPainter(matteBackgroundHeardDark);
        } else {
            this.jxPanelBackground.setBackgroundPainter(matteBackgroundDark);
        }
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        if (this.voicemail != null && this.voicemail.isHeard()) {
            this.jxPanelBackground.setBackgroundPainter(matteBackgroundHeardLight);
        } else {
            this.jxPanelBackground.setBackgroundPainter(matteBackgroundLight);
        }
    }//GEN-LAST:event_formMouseExited

    private void jButtonPlayActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonPlayActionPerformed
        if (this.soundListener != null) {
            if (!this.playing) {
                this.play();
            } else {
                this.stop();
            }
            updateMessageHeard(Boolean.TRUE);
        }
    }//GEN-LAST:event_jButtonPlayActionPerformed

    public void updateMessageHeard(Boolean heard) {
        this.voicemail.setHeard(heard);
        jxPanelBackground.setBackgroundPainter((this.voicemail != null && this.voicemail.isHeard()) ? matteBackgroundHeardLight : matteBackgroundLight);
        CounterLabel.getMissedVoicemailsLabel().refresh();
    }

    private void customize() {
        this.setUserNameOrNumber(voicemail.getAuthor());

        try {
            if (DateUtils.isToday(this.voicemail)
                    || DateUtils.isYesterday(this.voicemail)) {
                this.setTimeOfCall(outputFormatToday.format(inputFormat.parse(voicemail.getDate())));
            } else {
                this.setTimeOfCall(outputFormatOlder.format(inputFormat.parse(voicemail.getDate())));
            }
        } catch (ParseException pe) {
            pe.printStackTrace(System.err);
        }
        try {
            this.soundListener = new VoicemailListener(voicemail);
            this.timer = new javax.swing.Timer(100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        tick();
                    } catch (IOException ex) {
                        Logger.getLogger(VoiceMessageMiniPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            // Whenever the slider value changes, if we're not already
            // at the new position, skip to it.
            this.jSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!jSlider.getValueIsAdjusting()) {
                        //System.out.println("Slider poz "+jSlider.getValue());
                        int value = jSlider.getValue();
                        int secs = value / 1000;
                        //System.out.println("Min "+secs/60);
                        //System.out.println("Sec "+secs%60);
                        jLabelDuration.setText(String.format("%02d", secs / 60) + ":" + String.format("%02d", secs % 60));
                    }
                }
            });
            this.jSlider.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent me) {
                }

                @Override
                public void mousePressed(MouseEvent me) {
                    paused = true;
                    timer.stop();
                }

                @Override
                public void mouseEntered(MouseEvent me) {
                }

                @Override
                public void mouseExited(MouseEvent me) {
                }

                @Override
                public void mouseReleased(MouseEvent me) {
                    skipValue = jSlider.getValue();
                    skip();
                }
            });

            showVoicemailDuration();
            //jLabelDuration.setText("####");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        wireupActions();
    }

    public void showVoicemailDuration() {        
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int secs = VoiceMessageMiniPanel.this.audioLength / 1000;
                try {
                    secs = Integer.parseInt(VoiceMessageMiniPanel.this.voicemail.getDuration());
                } catch (NumberFormatException numberFormatException) {
                }
                //this.jLabelDuration.setText(this.voicemail.getDuration());
                VoiceMessageMiniPanel.this.jLabelDuration.setText(String.format("%02d", secs / 60) 
                                                                + ":" 
                                                                + String.format("%02d", secs % 60));
            }
         });
    }
    
    private void wireupActions() {
        ActionMap actionMap = Application.getInstance().getContext().getActionMap(this);

        javax.swing.Action action = actionMap.get("Call");
        action.putValue(action.SMALL_ICON, this.jButtonCallBack.getIcon());
        this.jButtonCallBack.setAction(action);

        action = actionMap.get("DeleteVoiceMessage");
        action.putValue(action.SMALL_ICON, this.jButtonDeleteMsg.getIcon());
        this.jButtonDeleteMsg.setAction(action);
    }

    @Override
    public HistoryItem.HistoryItemTypes getHistoryItemType() {
        return HistoryItem.HistoryItemTypes.VOICE_MAIL;
    }

    @Action(block = Task.BlockingScope.COMPONENT)
    public Task Call() {
    	MakeCallTask task = new MakeCallTask(org.jdesktop.application.Application.getInstance(), 
    			voicemail.getAuthorExtension());
        task.setIgnoreNumbersPattern(true);
        return task;
    }

    /**
     * Compares the current HistoryItem to another HistoryItem, inverting the
     * comparison result such that: [newer event] is smaller than [older event].
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        if (o == null || !(o instanceof HistoryItem)) {
            return 1;
        }
        final HistoryItem hi = (HistoryItem) o;
        return -(this.getTimestamp().compareTo(hi.getTimestamp()));
    }

    @Override
    public Date getTimestamp() {
        try {
            return inputFormat.parse(voicemail.getDate());
        } catch (ParseException pe) {
            pe.printStackTrace(System.err);
        }
        return null;
    }

    @Action(block = Task.BlockingScope.COMPONENT)
    public Task DeleteVoiceMessage() {
        if (!Beans.isDesignTime()) {
            return new VoiceMessageMiniPanel.DeleteVoiceMessageTask(
                    org.jdesktop.application.Application.getInstance());
        } else {
            return null;
        }
    }

    private class DeleteVoiceMessageTask extends org.jdesktop.application.Task<Boolean, Void> {
        String calee = null;

        DeleteVoiceMessageTask(org.jdesktop.application.Application app) {
            super(app);
            if (VoiceMessageMiniPanel.this.soundListener != null) {
                if (VoiceMessageMiniPanel.this.playing) {
                	VoiceMessageMiniPanel.this.stop();
                }
            }
        }

        @Override
        protected Boolean doInBackground() {            
            Boolean result = Boolean.FALSE;
            try {
                result = RestManager.getInstance().deleteVoicemail(voicemail);
            } catch (Exception e) {
                Log.error("Cannot delete voicemail", e);
                return Boolean.FALSE;
            }
            return result;
        }

        @Override
        protected void succeeded(Boolean result) {
            // EDT:
            ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(VoiceMessageMiniPanel.class);

            VoiceMessageMiniPanel.this.setVisible(!result);
            VoiceMessageMiniPanel.this.setDeleted(true);
            if (result) {
                JOptionPane.showMessageDialog(
                        SparkManager.getMainWindow(),
                        resourceMap.getString("voicemail.delete.confirmationMessage"),
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                CounterLabel.getMissedVoicemailsLabel().refresh();
            } else {
                JOptionPane.showMessageDialog(SparkManager.getMainWindow(),
                        resourceMap.getString("voicemail.delete.errorMessage"),
                        "Problem !", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //used if JMF (Java Media Framework) is used instead of a classic audio-player
    //need to investigate if we can use JMF (Manager.createPlayer)
    @Override
    public void controllerUpdate(ControllerEvent e) {
        if (player == null) {
            return;
        }
        if (e instanceof RealizeCompleteEvent) {
            Component cmp = player.getVisualComponent();
            if (cmp != null) {
                add(cmp, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(2, 0, 10, 0), 0, 0));
            }
            Component cmp2 = player.getControlPanelComponent();
            if (cmp2 != null) {
                add(cmp2, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(2, 0, 10, 0), 0, 0));
            }
        }
    }

    public Voicemail getVoicemail() {
        return voicemail;
    }

    public void setVoicemail(Voicemail vm) {
        voicemail = vm;
    }

    /**
     * Sets the icon representing user status.
     *
     * @param icon
     */
    public void setUserStatusIcon(Icon icon) {
        this.jLabelUsername.setIcon(icon);
    }

    /**
     * Sets the name of the user in order for it to be displayed.
     *
     * @param userDisplayName
     */
    public void setUserNameOrNumber(String userDisplayName) {
        this.jLabelUsername.setText(userDisplayName);
    }

    public String getUserNameOrNumber() {
        return this.jLabelUsername.getText();
    }

    /**
     * Returns a reference of the slider representing the message.
     *
     * @return
     */
    public JSlider getJSlider() {
        return this.jSlider;
    }

    /**
     * Sets the time at which the message was received.
     *
     * @param timeOfMessage
     */
    public void setTimeOfCall(String timeOfMessage) {
        this.jLabelTimestamp.setText(timeOfMessage);
    }

    public String getTimeOfCall() {
        return this.jLabelTimestamp.getText();
    }

    /**
     * Returns a reference of the button used for returning calls.
     *
     * @return
     */
    public JButton getCallBackButton() {
        return this.jButtonCallBack;
    }

    /**
     * Returns a reference of the button used for deleting messages.
     *
     * @return
     */
    public JButton getDeleteButton() {
        return this.jButtonDeleteMsg;
    }

    /**
     * Returns a reference of the button used for playing messages.
     *
     * @return
     */
    public JButton getPlaybackButton() {
        return this.jButtonPlay;
    }

    /**
     * Start playing the sound at the current position
     */
    private void play() {

		getParent().getParent().setCursor(Cursor.getPredefinedCursor((Cursor.WAIT_CURSOR)));				
    	
    	org.jdesktop.application.Task<Object, Void> playTask = 
    			new org.jdesktop.application.Task<Object, Void>(Application.getInstance()){
    		
    		@Override
    		protected Object doInBackground() throws Exception{
    			if (!soundListener.isInitialized()) {
    				 soundListener.preparePlay(new SoundEvent(VoiceMessageMiniPanel.this));    	          
    	        }
    			return null;
    		}
    		
    		@Override
    		protected void succeeded(Object result){
    			VoiceMessageMiniPanel.this.getParent().getParent().setCursor(Cursor.getDefaultCursor());    			  
    			
    			jSlider.setMaximum(soundListener.getAudioLength());
    			clip = soundListener.getClip();
    			    			
    			soundListener.start();

    	        timer.start();
    	        
    	        SwingUtilities.invokeLater(new Runnable() {
    	            @Override
    	            public void run() {
    	                jButtonPlay.setIcon(Images.stopVoicemailIcon);
    	                jButtonPlay.setRolloverIcon(Images.stopVoicemailRolloverIcon);
    	            }
    	        });
    	        playing = true;
    	        paused = false;
    			
    		}
    		
    		@Override
    		protected void finished(){
    			VoiceMessageMiniPanel.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    		}    		
    	};
    	
    	playTask.execute();          
    }

    /**
     * Stop playing the sound, but retain the current position
     */
    private void stop() {
        timer.stop();
        soundListener.stop();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jButtonPlay.setIcon(Images.playVoicemailIcon);
                jButtonPlay.setRolloverIcon(Images.playVoicemailRolloverIcon);
            }
        });
        playing = false;
        if (soundListener.IsMP3()) {
            soundListener.setInitialized(false);
        } else {
            soundListener.setInitialized(true);
        }
        paused = false;
        showVoicemailDuration();
    }

    /**
     * Stop playing the sound and reset the position to 0
     */
    private void reset() {
        stop();
        //clip.setMicrosecondPosition(0);
        soundListener.reset();
        audioPosition = 0;
        jSlider.setValue(0);
        paused = false;
        showVoicemailDuration();
    }

    /**
     * Skip to the specified position
     */
    private void skip() { // Called when user drags the slider
        soundListener.setStopped(true);
        int position = skipValue;
        audioPosition = position;
        //clip.setMicrosecondPosition(position * 1000);
        int x = soundListener.currentPositionInBytes;
        soundListener.setMicrosecondPosition(position);
        // System.out.println("AudioPosition - scroll position "+position);
        //System.out.println(" "+soundListener.currentPositionInBytes);
        jSlider.setValue(position); // in case skip( ) is called from outside
        soundListener.setInitialized(false);
        //System.out.println(" " + soundListener.currentPositionInBytes);
        soundListener.preparePlay(new SoundEvent(this), soundListener.currentPositionInBytes - x);

        paused = false;

        timer.start();
        soundListener.setStopped(false);
    }

    // An internal method that updates the progress bar.
    // The Timer object calls it 10 times a second.
    // If the sound has finished, it resets to the beginning
    private void tick() throws IOException {
        if (!paused) {
            if (soundListener.isActive()) {
                audioPosition = (int) (soundListener.getMicrosecondPosition());
                jSlider.setValue(audioPosition);
                if (audioPosition==soundListener.getAudioLength())
                {
                    reset();
                }
                //System.out.println("JSlider "+jSlider.getValue());
            } else {
                reset();
            }
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean d) {
        deleted = d;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButtonCallBack;
    private JButton jButtonDeleteMsg;
    private JButton jButtonPlay;
    private JLabel jLabelDuration;
    private JLabel jLabelTimestamp;
    private JLabel jLabelUsername;
    private JPanel jPanelButtons;
    private JPanel jPanelPlayMessage;
    private JPanel jPanelUsernameTimestamp;
    private JSlider jSlider;
    private JXPanel jXPanelDetails;
    private JLabel jxLabelIconVoiceMessage;
    private JXPanel jxPanelBackground;
    // End of variables declaration//GEN-END:variables
    private final MattePainter matteBackgroundLight = LinearGradientPainter.voiceMessageLightPainter();
    private final MattePainter matteBackgroundDark = LinearGradientPainter.voiceMessageDarkPainter();
    private final MattePainter matteBackgroundHeardLight = LinearGradientPainter.voiceMessageHeardLightPainter();
    private final MattePainter matteBackgroundHeardDark = LinearGradientPainter.voiceMessageHeardDarkPainter();
    private Voicemail voicemail = null;
    private Player player;
    private boolean playing = false; //indicates if the voice message is being played.
    //Contains information about the stream to play
    private SoundListener soundListener = null;
    private Timer timer = null; //Timer to synchronize the jSlider with the audio clip.
    private Clip clip;
    private int audioLength; // Length of the sound.
    private int audioPosition = 0; // Current position within the sound
    private final SimpleDateFormat outputFormatToday = DateUtils.outputFormatToday;
    private final SimpleDateFormat outputFormatOlder = DateUtils.outputFormatVoicemailOlder;
    private final SimpleDateFormat inputFormat = DateUtils.formatterFromCdr;
    private boolean deleted = false;
    private int skipValue;
    private volatile boolean paused = false;
}