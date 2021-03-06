package org.ezuce.im.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Razvan
 */
public class EzuceGroupChatBtnPanel extends javax.swing.JPanel {

    /** Creates new form EzuceGroupChatBtnPanel */
    public EzuceGroupChatBtnPanel() {
        background=resourceMap.getImageIcon("background");
        initComponents();
        
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                incrementDuration();
            }
        });
    }    
    
    public JButton getjButtonAddUser() {
        return jButtonAddUser;
    }

    public JButton getjButtonAudio() {
        return jButtonAudio;
    }

    public JButton getjButtonConversation() {
        return jButtonConversation;
    }

    public JButton getjButtonEndCall() {
        return jButtonEndCall;
    }

    public JButton getjButtonMicrophone() {
        return jButtonMicrophone;
    }

    public JButton getJButtonDeafen()
    {
        return jButtonDeaf;
    }
    
    public JLabel getjLabelDuration() {
        return jLabelDuration;
    }

    public JToggleButton getjToggleButtonRecord() {
        return jToggleButtonRecord;
    }
    
    public void startCountingDuration()
    {
        if (timer.isRunning())
        {
            timer.stop();
        }
        resetDuration();
        timer.start();
    }
    
    public void resetDuration()
    {
        if (timer.isRunning())
        {
            timer.stop();
        }
        this.durationInSeconds=0;
        jLabelDuration.setText("00:00");
    }
    
    public void incrementDuration()
    {
        this.durationInSeconds++;
        
        jLabelDuration.setText(formatter.format(this.durationInSeconds/60)+
                                ":"+
                               formatter.format(this.durationInSeconds%60));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonEndCall = new JButton();
        jButtonConversation = new JButton();
        jButtonAddUser = new JButton();
        jButtonAudio = new JButton();
        jButtonMicrophone = new JButton();
        jToggleButtonRecord = new JToggleButton();
        jLabelDuration = new JLabel();
        jButtonDeaf = new JButton();

        setBackground(new Color(255, 255, 255));
        setFocusable(false);
        setMaximumSize(new Dimension(232, 76));
        setMinimumSize(new Dimension(200, 76));
        setOpaque(false);
        //setPreferredSize(new Dimension(232, 76));

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceGroupChatBtnPanel.class);
        jButtonEndCall.setIcon(resourceMap.getIcon("jButtonEndCall.icon")); // NOI18N
        jButtonEndCall.setBorder(null);
        jButtonEndCall.setBorderPainted(false);
        jButtonEndCall.setContentAreaFilled(false);
        jButtonEndCall.setDoubleBuffered(true);
        jButtonEndCall.setFocusPainted(false);
        jButtonEndCall.setHideActionText(true);
        jButtonEndCall.setName("jButtonEndCall"); // NOI18N

        jButtonConversation.setIcon(resourceMap.getIcon("jButtonConversation.icon")); // NOI18N
        jButtonConversation.setBorder(null);
        jButtonConversation.setBorderPainted(false);
        jButtonConversation.setContentAreaFilled(false);
        jButtonConversation.setDoubleBuffered(true);
        jButtonConversation.setFocusPainted(false);
        jButtonConversation.setHideActionText(true);
        jButtonConversation.setIconTextGap(0);
        jButtonConversation.setName("jButtonConversation"); // NOI18N
        jButtonConversation.setRolloverIcon(resourceMap.getIcon("jButtonConversation.rolloverIcon")); // NOI18N

        jButtonAddUser.setIcon(resourceMap.getIcon("jButtonAddUser.icon")); // NOI18N
        jButtonAddUser.setBorder(null);
        jButtonAddUser.setBorderPainted(false);
        jButtonAddUser.setContentAreaFilled(false);
        jButtonAddUser.setDoubleBuffered(true);
        jButtonAddUser.setHideActionText(true);
        jButtonAddUser.setName("jButtonAddUser"); // NOI18N
        jButtonAddUser.setRolloverIcon(resourceMap.getIcon("jButtonAddUser.rolloverIcon")); // NOI18N

        jButtonAudio.setIcon(resourceMap.getIcon("jButtonAudio.icon")); // NOI18N
        jButtonAudio.setBorder(null);
        jButtonAudio.setBorderPainted(false);
        jButtonAudio.setContentAreaFilled(false);
        jButtonAudio.setDoubleBuffered(true);
        jButtonAudio.setFocusPainted(false);
        jButtonAudio.setHideActionText(true);
        jButtonAudio.setName("jButtonAudio"); // NOI18N
        jButtonAudio.setRolloverIcon(resourceMap.getIcon("jButtonAudio.rolloverIcon")); // NOI18N

        jButtonMicrophone.setIcon(resourceMap.getIcon("jButtonMicrophone.icon")); // NOI18N
        jButtonMicrophone.setBorder(null);
        jButtonMicrophone.setBorderPainted(false);
        jButtonMicrophone.setContentAreaFilled(false);
        jButtonMicrophone.setDoubleBuffered(true);
        jButtonMicrophone.setFocusPainted(false);
        jButtonMicrophone.setHideActionText(true);
        jButtonMicrophone.setName("jButtonMicrophone"); // NOI18N
        jButtonMicrophone.setRolloverIcon(resourceMap.getIcon("jButtonMicrophone.rolloverIcon")); // NOI18N

        jToggleButtonRecord.setIcon(resourceMap.getIcon("jToggleButtonRecord.icon")); // NOI18N
        jToggleButtonRecord.setBorder(null);
        jToggleButtonRecord.setBorderPainted(false);
        jToggleButtonRecord.setContentAreaFilled(false);
        jToggleButtonRecord.setDoubleBuffered(true);
        jToggleButtonRecord.setFocusPainted(false);
        jToggleButtonRecord.setHideActionText(true);
        jToggleButtonRecord.setName("jToggleButtonRecord"); // NOI18N
        jToggleButtonRecord.setRolloverIcon(resourceMap.getIcon("jToggleButtonRecord.rolloverSelectedIcon")); // NOI18N
        jToggleButtonRecord.setSelectedIcon(resourceMap.getIcon("jToggleButtonRecord.selectedIcon")); // NOI18N

        jLabelDuration.setFont(new Font("Tahoma", 0, 14));
        jLabelDuration.setForeground(new Color(153, 153, 153));
        jLabelDuration.setText("00:00");
        jLabelDuration.setName("jLabelDuration"); // NOI18N

        jButtonDeaf.setIcon(resourceMap.getIcon("jButtonDeaf.icon")); // NOI18N
        jButtonDeaf.setBorder(null);
        jButtonDeaf.setBorderPainted(false);
        jButtonDeaf.setContentAreaFilled(false);
        jButtonDeaf.setDoubleBuffered(true);
        jButtonDeaf.setFocusPainted(false);
        jButtonDeaf.setHideActionText(true);
        jButtonDeaf.setName("jButtonDeaf"); // NOI18N
        jButtonDeaf.setRolloverIcon(resourceMap.getIcon("jButtonDeaf.rolloverIcon")); // NOI18N

        
        JPanel jPanel1 = new JPanel();
        jPanel1.setName("jPanel1");
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        
        jPanel1.add(jButtonDeaf);
        jPanel1.add(jButtonEndCall);
        jPanel1.add(jButtonConversation);
        jPanel1.add(jButtonAddUser);
        jPanel1.add(jToggleButtonRecord);
        
        
        
        
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        //.addComponent(jButtonAudio)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonMicrophone)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(jLabelDuration))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, Short.MAX_VALUE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jPanel1))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    //.addComponent(jButtonAudio)
                    .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(jButtonMicrophone)
                        .addComponent(jLabelDuration, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButtonAddUser;
    private JButton jButtonAudio;
    private JButton jButtonConversation;
    private JButton jButtonDeaf;
    private JButton jButtonEndCall;
    private JButton jButtonMicrophone;
    private JLabel jLabelDuration;
    private JToggleButton jToggleButtonRecord;
    // End of variables declaration//GEN-END:variables

    
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceGroupChatBtnPanel.class);
    private final ImageIcon background;
    private final Timer timer;
    private int durationInSeconds=0;
    private NumberFormat formatter=new DecimalFormat("#00");
}
