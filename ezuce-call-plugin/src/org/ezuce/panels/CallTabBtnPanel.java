package org.ezuce.panels;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.Spark;

/**
 * Contains the three buttons used in the Call tab to switch from one screen to
 * the other (Make a call - Find me - Call history).
 * @author Razvan
 */
public class CallTabBtnPanel extends javax.swing.JPanel {

    /** Creates new form CallTabBtnPanel */
    public CallTabBtnPanel() {
        initComponents();
        this.jPanelFilterHistory.setVisible(false);
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new ButtonGroup();
        jToggleButtonMakeACall = new JToggleButton();
        jToggleButtonFindMe = new JToggleButton();
        jToggleButtonMissedCalls = new JToggleButton();
        jPanelFilterHistory = new JPanel();
        jToggleButtonFilterReceivedCalls = new JToggleButton();
        jToggleButtonFilterMissedCalls = new JToggleButton();
        jToggleButtonFilterVoiceMails = new JToggleButton();
        jToggleButtonFilterDialedCalls = new JToggleButton();
        jPanelConfigCallTab = new JPanel();
        jToggleButtonShowDialPad = new JToggleButton();

        setOpaque(false);

        buttonGroup.add(jToggleButtonMakeACall);
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallTabBtnPanel.class);
        jToggleButtonMakeACall.setIcon(resourceMap.getIcon("jToggleButtonMakeACall.icon")); // NOI18N
        jToggleButtonMakeACall.setSelected(true);
        jToggleButtonMakeACall.setToolTipText("Make a call");
        jToggleButtonMakeACall.setBorder(null);
        jToggleButtonMakeACall.setBorderPainted(false);
        jToggleButtonMakeACall.setContentAreaFilled(false);
        jToggleButtonMakeACall.setDoubleBuffered(true);
        jToggleButtonMakeACall.setFocusPainted(false);
        jToggleButtonMakeACall.setHideActionText(true);
        jToggleButtonMakeACall.setName("jToggleButtonMakeACall"); // NOI18N
        jToggleButtonMakeACall.setPressedIcon(resourceMap.getIcon("jToggleButtonMakeACall.pressedIcon")); // NOI18N
        jToggleButtonMakeACall.setRolloverIcon(resourceMap.getIcon("jToggleButtonMakeACall.rolloverIcon")); // NOI18N
        jToggleButtonMakeACall.setSelectedIcon(resourceMap.getIcon("jToggleButtonMakeACall.selectedIcon")); // NOI18N

        buttonGroup.add(jToggleButtonFindMe);
        jToggleButtonFindMe.setIcon(resourceMap.getIcon("jToggleButtonFindMe.icon")); // NOI18N
        jToggleButtonFindMe.setToolTipText("Find me");
        jToggleButtonFindMe.setBorder(null);
        jToggleButtonFindMe.setBorderPainted(false);
        jToggleButtonFindMe.setContentAreaFilled(false);
        jToggleButtonFindMe.setDoubleBuffered(true);
        jToggleButtonFindMe.setFocusPainted(false);
        jToggleButtonFindMe.setHideActionText(true);
        jToggleButtonFindMe.setName("jToggleButtonFindMe"); // NOI18N
        jToggleButtonFindMe.setPressedIcon(resourceMap.getIcon("jToggleButtonFindMe.pressedIcon")); // NOI18N
        jToggleButtonFindMe.setRolloverIcon(resourceMap.getIcon("jToggleButtonFindMe.rolloverIcon")); // NOI18N
        jToggleButtonFindMe.setSelectedIcon(resourceMap.getIcon("jToggleButtonFindMe.selectedIcon")); // NOI18N

        buttonGroup.add(jToggleButtonMissedCalls);
        jToggleButtonMissedCalls.setIcon(resourceMap.getIcon("jToggleButtonMissedCalls.icon")); // NOI18N
        jToggleButtonMissedCalls.setToolTipText("Missed calls and voice messages");
        jToggleButtonMissedCalls.setBorder(null);
        jToggleButtonMissedCalls.setBorderPainted(false);
        jToggleButtonMissedCalls.setContentAreaFilled(false);
        jToggleButtonMissedCalls.setDoubleBuffered(true);
        jToggleButtonMissedCalls.setFocusPainted(false);
        jToggleButtonMissedCalls.setHideActionText(true);
        jToggleButtonMissedCalls.setName("jToggleButtonMissedCalls"); // NOI18N
        jToggleButtonMissedCalls.setPressedIcon(resourceMap.getIcon("jToggleButtonMissedCalls.pressedIcon")); // NOI18N
        jToggleButtonMissedCalls.setRolloverIcon(resourceMap.getIcon("jToggleButtonMissedCalls.rolloverIcon")); // NOI18N
        jToggleButtonMissedCalls.setSelectedIcon(resourceMap.getIcon("jToggleButtonMissedCalls.selectedIcon")); // NOI18N

        jPanelFilterHistory.setName("jPanelFilterHistory"); // NOI18N
        jPanelFilterHistory.setOpaque(false);

        jToggleButtonFilterReceivedCalls.setIcon(resourceMap.getIcon("jToggleButtonFilterReceivedCalls.icon")); // NOI18N
        jToggleButtonFilterReceivedCalls.setSelected(true);
        jToggleButtonFilterReceivedCalls.setBorder(null);
        jToggleButtonFilterReceivedCalls.setBorderPainted(false);
        jToggleButtonFilterReceivedCalls.setContentAreaFilled(false);
        jToggleButtonFilterReceivedCalls.setDoubleBuffered(true);
        jToggleButtonFilterReceivedCalls.setFocusPainted(false);
        jToggleButtonFilterReceivedCalls.setFocusable(false);
        jToggleButtonFilterReceivedCalls.setHideActionText(true);
        jToggleButtonFilterReceivedCalls.setName("jToggleButtonFilterReceivedCalls"); // NOI18N
        jToggleButtonFilterReceivedCalls.setPressedIcon(resourceMap.getIcon("jToggleButtonFilterReceivedCalls.pressedIcon")); // NOI18N
        jToggleButtonFilterReceivedCalls.setRolloverIcon(resourceMap.getIcon("jToggleButtonFilterReceivedCalls.rolloverIcon")); // NOI18N
        jToggleButtonFilterReceivedCalls.setSelectedIcon(resourceMap.getIcon("jToggleButtonFilterReceivedCalls.selectedIcon")); // NOI18N

        jToggleButtonFilterMissedCalls.setIcon(resourceMap.getIcon("jToggleButtonFilterMissedCalls.icon")); // NOI18N
        jToggleButtonFilterMissedCalls.setSelected(true);
        jToggleButtonFilterMissedCalls.setBorder(null);
        jToggleButtonFilterMissedCalls.setBorderPainted(false);
        jToggleButtonFilterMissedCalls.setContentAreaFilled(false);
        jToggleButtonFilterMissedCalls.setDoubleBuffered(true);
        jToggleButtonFilterMissedCalls.setFocusPainted(false);
        jToggleButtonFilterMissedCalls.setFocusable(false);
        jToggleButtonFilterMissedCalls.setHideActionText(true);
        jToggleButtonFilterMissedCalls.setName("jToggleButtonFilterMissedCalls"); // NOI18N
        jToggleButtonFilterMissedCalls.setPressedIcon(resourceMap.getIcon("jToggleButtonFilterMissedCalls.pressedIcon")); // NOI18N
        jToggleButtonFilterMissedCalls.setRolloverIcon(resourceMap.getIcon("jToggleButtonFilterMissedCalls.rolloverIcon")); // NOI18N
        jToggleButtonFilterMissedCalls.setSelectedIcon(resourceMap.getIcon("jToggleButtonFilterMissedCalls.selectedIcon")); // NOI18N

        jToggleButtonFilterVoiceMails.setIcon(resourceMap.getIcon("jToggleButtonFilterVoiceMails.icon")); // NOI18N
        jToggleButtonFilterVoiceMails.setSelected(true);
        jToggleButtonFilterVoiceMails.setBorder(null);
        jToggleButtonFilterVoiceMails.setBorderPainted(false);
        jToggleButtonFilterVoiceMails.setContentAreaFilled(false);
        jToggleButtonFilterVoiceMails.setDoubleBuffered(true);
        jToggleButtonFilterVoiceMails.setFocusPainted(false);
        jToggleButtonFilterVoiceMails.setFocusable(false);
        jToggleButtonFilterVoiceMails.setHideActionText(true);
        jToggleButtonFilterVoiceMails.setName("jToggleButtonFilterVoiceMails"); // NOI18N
        jToggleButtonFilterVoiceMails.setPressedIcon(resourceMap.getIcon("jToggleButtonFilterVoiceMails.pressedIcon")); // NOI18N
        jToggleButtonFilterVoiceMails.setRolloverIcon(resourceMap.getIcon("jToggleButtonFilterVoiceMails.rolloverIcon")); // NOI18N
        jToggleButtonFilterVoiceMails.setSelectedIcon(resourceMap.getIcon("jToggleButtonFilterVoiceMails.selectedIcon")); // NOI18N

        jToggleButtonFilterDialedCalls.setIcon(resourceMap.getIcon("jToggleButtonDialedCalls.icon")); // NOI18N
        jToggleButtonFilterDialedCalls.setSelected(true);
        jToggleButtonFilterDialedCalls.setBorder(null);
        jToggleButtonFilterDialedCalls.setBorderPainted(false);
        jToggleButtonFilterDialedCalls.setContentAreaFilled(false);
        jToggleButtonFilterDialedCalls.setDoubleBuffered(true);
        jToggleButtonFilterDialedCalls.setFocusPainted(false);
        jToggleButtonFilterDialedCalls.setFocusable(false);
        jToggleButtonFilterDialedCalls.setHideActionText(true);
        jToggleButtonFilterDialedCalls.setName("jToggleButtonFilterDialedCalls"); // NOI18N
        jToggleButtonFilterDialedCalls.setPressedIcon(resourceMap.getIcon("jToggleButtonDialedCalls.pressedIcon")); // NOI18N
        jToggleButtonFilterDialedCalls.setRolloverIcon(resourceMap.getIcon("jToggleButtonDialedCalls.rolloverIcon")); // NOI18N
        jToggleButtonFilterDialedCalls.setSelectedIcon(resourceMap.getIcon("jToggleButtonDialedCalls.selectedIcon")); // NOI18N

        if (!Spark.isMac())
        {
            GroupLayout jPanelFilterHistoryLayout = new GroupLayout(jPanelFilterHistory);
            jPanelFilterHistory.setLayout(jPanelFilterHistoryLayout);
            jPanelFilterHistoryLayout.setHorizontalGroup(
                jPanelFilterHistoryLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanelFilterHistoryLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jToggleButtonFilterMissedCalls)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jToggleButtonFilterReceivedCalls)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jToggleButtonFilterDialedCalls)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jToggleButtonFilterVoiceMails)
                    .addContainerGap())
            );
            jPanelFilterHistoryLayout.setVerticalGroup(
                jPanelFilterHistoryLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanelFilterHistoryLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jToggleButtonFilterReceivedCalls))
                .addComponent(jToggleButtonFilterMissedCalls)
                .addComponent(jToggleButtonFilterDialedCalls)
                .addComponent(jToggleButtonFilterVoiceMails)
            );
        }
        else
        {
            jPanelFilterHistory.setLayout(new BoxLayout(jPanelFilterHistory, BoxLayout.LINE_AXIS));
            jPanelFilterHistory.add(jToggleButtonFilterReceivedCalls);
            jPanelFilterHistory.add(jToggleButtonFilterMissedCalls);
            jPanelFilterHistory.add(jToggleButtonFilterDialedCalls);
            jPanelFilterHistory.add(jToggleButtonFilterVoiceMails);
        }
        jPanelConfigCallTab.setName("jPanelConfigCallTab"); // NOI18N
        jPanelConfigCallTab.setOpaque(false);
        jPanelConfigCallTab.setPreferredSize(new Dimension(56, 26));

        jToggleButtonShowDialPad.setIcon(resourceMap.getIcon("jToggleButtonShowDialPad.icon")); // NOI18N
        jToggleButtonShowDialPad.setBorder(null);
        jToggleButtonShowDialPad.setBorderPainted(false);
        jToggleButtonShowDialPad.setContentAreaFilled(false);
        jToggleButtonShowDialPad.setDoubleBuffered(true);
        jToggleButtonShowDialPad.setFocusPainted(false);
        jToggleButtonShowDialPad.setFocusable(false);
        jToggleButtonShowDialPad.setHideActionText(true);
        jToggleButtonShowDialPad.setName("jToggleButtonShowDialPad"); // NOI18N
        jToggleButtonShowDialPad.setPressedIcon(null);
        jToggleButtonShowDialPad.setRolloverIcon(resourceMap.getIcon("jToggleButtonShowDialPad.rolloverIcon")); // NOI18N
        jToggleButtonShowDialPad.setSelectedIcon(resourceMap.getIcon("jToggleButtonShowDialPad.selectedIcon")); // NOI18N

        GroupLayout jPanelConfigCallTabLayout = new GroupLayout(jPanelConfigCallTab);
        jPanelConfigCallTab.setLayout(jPanelConfigCallTabLayout);
        jPanelConfigCallTabLayout.setHorizontalGroup(
            jPanelConfigCallTabLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelConfigCallTabLayout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jToggleButtonShowDialPad)
                .addGap(0, 0, 0))
        );
        jPanelConfigCallTabLayout.setVerticalGroup(
            jPanelConfigCallTabLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jToggleButtonShowDialPad)
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5)
                .addComponent(jToggleButtonMakeACall)
                .addGap(5)
                /*.addPreferredGap(ComponentPlacement.RELATED)*/
                .addComponent(jToggleButtonFindMe)
                .addGap(5)
                /*.addPreferredGap(ComponentPlacement.RELATED)*/
                .addComponent(jToggleButtonMissedCalls)
                .addPreferredGap(ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jPanelFilterHistory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0,0,Short.MAX_VALUE)
                    .addComponent(jPanelConfigCallTab)
                    .addContainerGap(8,8)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
             .addGap(0, 0, 0)
            .addComponent(jToggleButtonMakeACall,Alignment.LEADING)
            .addComponent(jToggleButtonFindMe, Alignment.LEADING)
            .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)                
                .addComponent(jToggleButtonMissedCalls, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jPanelFilterHistory, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanelConfigCallTab, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
    }// </editor-fold>//GEN-END:initComponents

     public JToggleButton getjToggleButtonFindMe() {
        return jToggleButtonFindMe;
    }

    public JToggleButton getjToggleButtonMakeACall() {
        return jToggleButtonMakeACall;
    }

    public JToggleButton getjToggleButtonMissedCalls() {
        return jToggleButtonMissedCalls;
    }

    public JToggleButton getjToggleButtonFilterMissedCalls()
    {
        return this.jToggleButtonFilterMissedCalls;
    }
    
    public JToggleButton getjToggleButtonShowDialPad()
    {
        return this.jToggleButtonShowDialPad;
    }

    public JToggleButton getjToggleButtonFilterDialedCalls()
    {
        return this.jToggleButtonFilterDialedCalls;
    }

    public JToggleButton getjToggleButtonFilterReceivedCalls()
    {
        return this.jToggleButtonFilterReceivedCalls;
    }

    public JToggleButton getjToggleButtonFilterVoicemails()
    {
        return this.jToggleButtonFilterVoiceMails;
    }

    public JPanel getJPanelHistoryFilters()
    {
        return this.jPanelFilterHistory;
    }
    
    public JPanel getJPanelConfigCallTab()
    {
        return this.jPanelConfigCallTab;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonGroup buttonGroup;
    private JPanel jPanelConfigCallTab;
    private JPanel jPanelFilterHistory;
    private JToggleButton jToggleButtonFilterDialedCalls;
    private JToggleButton jToggleButtonFilterMissedCalls;
    private JToggleButton jToggleButtonFilterReceivedCalls;
    private JToggleButton jToggleButtonFilterVoiceMails;
    private JToggleButton jToggleButtonFindMe;
    private JToggleButton jToggleButtonMakeACall;
    private JToggleButton jToggleButtonMissedCalls;
    private JToggleButton jToggleButtonShowDialPad;
    // End of variables declaration//GEN-END:variables


}