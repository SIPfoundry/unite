package org.ezuce.panels;

import javax.swing.JButton;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 *
 */
public class UserBtnPanel extends javax.swing.JPanel {

    /** Creates new form UserBtnPanel */
    public UserBtnPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jButtonVoiceMessage = new JButton();
        jButtonMissedCalls = new JButton();
        jButtonConference = new JButton();

        setOpaque(false);
        setLayout(new VerticalLayout());

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(UserBtnPanel.class);
        jButtonVoiceMessage.setIcon(resourceMap.getIcon("jButtonVoiceMessage.icon")); // NOI18N
        jButtonVoiceMessage.setBorder(null);
        jButtonVoiceMessage.setBorderPainted(false);
        jButtonVoiceMessage.setContentAreaFilled(false);
        jButtonVoiceMessage.setDoubleBuffered(true);
        jButtonVoiceMessage.setFocusPainted(false);
        jButtonVoiceMessage.setHideActionText(true);
        jButtonVoiceMessage.setIconTextGap(0);
        jButtonVoiceMessage.setName("jButtonVoiceMessage"); // NOI18N
        jButtonVoiceMessage.setRolloverIcon(resourceMap.getIcon("jButtonVoiceMessage.rolloverIcon")); // NOI18N
        add(jButtonVoiceMessage);

        jButtonMissedCalls.setIcon(resourceMap.getIcon("jButtonMissedCalls.icon")); // NOI18N
        jButtonMissedCalls.setBorder(null);
        jButtonMissedCalls.setBorderPainted(false);
        jButtonMissedCalls.setContentAreaFilled(false);
        jButtonMissedCalls.setDoubleBuffered(true);
        jButtonMissedCalls.setFocusPainted(false);
        jButtonMissedCalls.setHideActionText(true);
        jButtonMissedCalls.setIconTextGap(0);
        jButtonMissedCalls.setName("jButtonMissedCalls"); // NOI18N
        jButtonMissedCalls.setRolloverIcon(resourceMap.getIcon("jButtonMissedCalls.rolloverIcon")); // NOI18N
        add(jButtonMissedCalls);

        jButtonConference.setIcon(resourceMap.getIcon("jButtonConference.icon")); // NOI18N
        jButtonConference.setBorder(null);
        jButtonConference.setBorderPainted(false);
        jButtonConference.setContentAreaFilled(false);
        jButtonConference.setDoubleBuffered(true);
        jButtonConference.setFocusPainted(false);
        jButtonConference.setHideActionText(true);
        jButtonConference.setIconTextGap(0);
        jButtonConference.setName("jButtonConference"); // NOI18N
        jButtonConference.setRolloverIcon(resourceMap.getIcon("jButtonConference.rolloverIcon")); // NOI18N
        add(jButtonConference);
    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButtonConference;
    private JButton jButtonMissedCalls;
    private JButton jButtonVoiceMessage;
    // End of variables declaration//GEN-END:variables

}
