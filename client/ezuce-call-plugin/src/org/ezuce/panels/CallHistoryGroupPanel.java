package org.ezuce.panels;

import org.ezuce.common.ui.panels.UserGroupPanel;
import org.ezuce.wrappers.interfaces.HistoryItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.ezuce.common.rest.Cdr;
import org.ezuce.common.rest.Voicemail;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;

/**
 * Contains history items: voice messages,missed calls, received calls
 * and dialed numbers, grouped in a collapsible container.
 * @author Razvan
 */
public class CallHistoryGroupPanel extends javax.swing.JPanel {
	ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(UserGroupPanel.class);

    /** Creates new form CallHistoryGroupPanel */
    public CallHistoryGroupPanel()
    {
        initComponents();
        configureHideButton();
    }

    /**
     * Create a CallHistoryGroupPanel instance, from the list of Cdrs received
     * as an argument, for the period indicated by the second parameter.
     * @param cdrs
     * @param historyPeriod
     */
    public CallHistoryGroupPanel(List<Cdr> cdrs, List<Voicemail> voiceMails,
                                 String historyPeriod)
    {
        this.cdrs=cdrs;
        this.voicemails=voiceMails;
        initComponents();
        configureHideButton();
        setGroupName(resourceMap.getString(historyPeriod));
        customize();
    }

    /**
     * Filters the items displayed in this group, making invisible those
     * types whose corresponding arguments are set to FALSE.
     * @param showMissedCalls If FALSE, this group hides the missed calls items.
     * @param showReceivedCalls If FALSE, this group hides the received calls items.
     * @param showDialedCalls If FALSE, this group hides the dialed calls items.
     * @param showVoiceMails If FALSE, this group hides the voice mail items.
     */
    public void filterHistoryItems(boolean showMissedCalls, boolean showReceivedCalls, boolean showDialedCalls, boolean showVoiceMails)
    {
        for (Component c:this.jXCollapsiblePane.getContentPane().getComponents())
        {
            if (c instanceof HistoryItem)
            {
                final HistoryItem hi=(HistoryItem)c;
                if (hi.getHistoryItemType()==HistoryItem.HistoryItemTypes.MISSED_CALL)
                {
                    c.setVisible(showMissedCalls);
                }
                else if (hi.getHistoryItemType()==HistoryItem.HistoryItemTypes.RECEIVED_CALL)
                {
                    c.setVisible(showReceivedCalls);
                }
                else if (hi.getHistoryItemType()==HistoryItem.HistoryItemTypes.DIALED_CALL)
                {
                    c.setVisible(showDialedCalls);
                }
                else if (hi.getHistoryItemType()==HistoryItem.HistoryItemTypes.VOICE_MAIL)
                {
                    c.setVisible(showVoiceMails && !hi.isDeleted());
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jPanelGroupDescr = new JPanel();
        jButtonGroupName = new JButton();
        jXTitledSeparatorGroupSize = new JXTitledSeparator();
        jXCollapsiblePane = new JXCollapsiblePane();

        setBackground(new Color(255, 255, 255));
        VerticalLayout verticalLayout2 = new VerticalLayout();
        verticalLayout2.setGap(1);
        setLayout(verticalLayout2);

        jPanelGroupDescr.setName("jPanelGroupDescr"); // NOI18N
        jPanelGroupDescr.setOpaque(false);
        jPanelGroupDescr.setPreferredSize(new Dimension(264, 25));

        jButtonGroupName.setFont(new Font("Arial", 0, 11));
        jButtonGroupName.setForeground(Color.lightGray);
        jButtonGroupName.setText("Show / hide group");
        jButtonGroupName.setBorder(null);
        jButtonGroupName.setBorderPainted(false);
        jButtonGroupName.setContentAreaFilled(false);
        jButtonGroupName.setDoubleBuffered(true);
        jButtonGroupName.setFocusPainted(false);
        jButtonGroupName.setHideActionText(true);
        jButtonGroupName.setHorizontalAlignment(SwingConstants.LEADING);
        jButtonGroupName.setHorizontalTextPosition(SwingConstants.LEADING);
        jButtonGroupName.setMaximumSize(new Dimension(12100, 22000));
        jButtonGroupName.setMinimumSize(new Dimension(0, 0));
        jButtonGroupName.setName("jButtonGroupName"); // NOI18N

        jXTitledSeparatorGroupSize.setFont(new Font("Arial", 0, 10));
        jXTitledSeparatorGroupSize.setHorizontalAlignment(SwingConstants.TRAILING);
        jXTitledSeparatorGroupSize.setTitle("0");
        jXTitledSeparatorGroupSize.setToolTipText("Number of entries in group");
        jXTitledSeparatorGroupSize.setForeground(Color.lightGray);
        jXTitledSeparatorGroupSize.setName("jXTitledSeparatorGroupSize"); // NOI18N

        GroupLayout jPanelGroupDescrLayout = new GroupLayout(jPanelGroupDescr);
        jPanelGroupDescr.setLayout(jPanelGroupDescrLayout);
        jPanelGroupDescrLayout.setHorizontalGroup(
            jPanelGroupDescrLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelGroupDescrLayout.createSequentialGroup()
                .addComponent(jButtonGroupName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jXTitledSeparatorGroupSize, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelGroupDescrLayout.setVerticalGroup(
            jPanelGroupDescrLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jButtonGroupName, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
            .addComponent(jXTitledSeparatorGroupSize, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        add(jPanelGroupDescr);

        jXCollapsiblePane.setBackground(new Color(255, 255, 255));
        jXCollapsiblePane.setName("jXCollapsiblePane"); // NOI18N
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setGap(2);
        jXCollapsiblePane.getContentPane().setLayout(verticalLayout1);
        add(jXCollapsiblePane);
    }//GEN-END:initComponents

    /**
     * Connect the show/hide button to the collapse/uncollapse action.
     */
    private void configureHideButton() {        
        Action toggleAction=this.jXCollapsiblePane.getActionMap().
                                            get(JXCollapsiblePane.TOGGLE_ACTION);
        toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON,
                              resourceMap.getIcon("icon.uncollapsed"));
        toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON,
                              resourceMap.getIcon("icon.collapsed"));
        this.jButtonGroupName.setAction(toggleAction);
        this.jButtonGroupName.setText(resourceMap.getString("show.hide"));
    }

    //TODO: implement a new Constructor and the method 'customize' to be used in that constructor.
    //The new constructor must be able to display history items based on the argument(s) received.
    private void customize()
    {
        if ((cdrs==null || cdrs.isEmpty()) &&
            (voicemails==null || voicemails.isEmpty()))
        {
            return;
        }

        ArrayList<HistoryItem> historyItems=new ArrayList<HistoryItem>(voicemails.size()+cdrs.size());
        for (Voicemail voicemail:voicemails)
        {
            //this.addHistoryItem(createVoiceMailEntry(voicemail));
            final HistoryItem hi=createVoiceMailEntry(voicemail);
            if (hi!=null) historyItems.add(hi);
        }
        for (Cdr cdr : cdrs)
        {
            //this.addHistoryItem(createCallEntry(cdr));
            final HistoryItem hi=createCallEntry(cdr);
            if (hi!=null) historyItems.add(hi);
        }


        this.setGroupSize(historyItems.size()+"");

        //sort the history items, from most recent to older ones:
        if (!historyItems.isEmpty()) Collections.sort(historyItems);

        for (HistoryItem hi : historyItems)
        {
            this.addHistoryItem(hi);
        }
        voicemails=null;
        cdrs=null;
        System.gc();
    }

    /**
     * Builds a HistoryItem from a Voicemail instance.
     * @param voicemail
     * @return
     */
    private HistoryItem createVoiceMailEntry(Voicemail voicemail)
    {
        HistoryItem hi=new VoiceMessageMiniPanel(voicemail);
        return hi;
    }

    /**
     * Builds a HistoryItem from a Cdr instance.
     * @param cdr
     * @return
     */
    private HistoryItem createCallEntry(Cdr cdr ) {
        HistoryItem hi=null;
        if (cdr.isDialed()) {
            hi=new DialedCallMiniPanel(cdr);
        } else if (cdr.isReceived()) {
            hi=new ReceivedCallMiniPanel(cdr);
        } else if (cdr.isMissed()) {
            hi=new MissedCallMiniPanel(cdr);
        }
        return hi;
    }

    /**
     * Adds the specified history item to the group.
     * @param hi
     */
    public void addHistoryItem(final HistoryItem hi)
    {
        if (hi instanceof JPanel)
        {
            if (hi instanceof MissedCallMiniPanel) {
                this.jXCollapsiblePane.add((MissedCallMiniPanel)hi);
            }
            else if(hi instanceof ReceivedCallMiniPanel) {
                this.jXCollapsiblePane.add((ReceivedCallMiniPanel) hi);
            }
            else if(hi instanceof DialedCallMiniPanel) {
                this.jXCollapsiblePane.add((DialedCallMiniPanel) hi);
            }
            else if(hi instanceof VoiceMessageMiniPanel) {
                this.jXCollapsiblePane.add((VoiceMessageMiniPanel) hi);
            }
        }
    }

    /**
     * Removes the specified history item from the group.
     * @param hi
     */
    public void removeHistoryItem(HistoryItem hi)
    {
        if (hi instanceof Component)
        {
            this.jXCollapsiblePane.remove((Component)hi);
        }
    }

    /**
     * Gets the actual collapsible container holding the various call history
     * items.
     * @return
     */
    public JXCollapsiblePane getCollapsiblePane()
    {
        return this.jXCollapsiblePane;
    }

    /**
     * Sets the name displayed for this group.
     * @param grName
     */
    public void setGroupName(String grName)
    {
        this.jButtonGroupName.setText(grName);
    }
    public String getGroupName()
    {
        return this.jButtonGroupName.getText();
    }

    /**
     * Sets the size displayed for this group.
     * @param availableEntries
     */
    public void setGroupSize(String availableEntries)
    {
        this.jXTitledSeparatorGroupSize.setTitle(availableEntries);
    }
    public void getGroupSize()
    {
        this.jXTitledSeparatorGroupSize.getTitle();
    }

    public void clearGroup()
    {
        this.jXCollapsiblePane.removeAll();
        cdrs=null;
        voicemails=null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButtonGroupName;
    private JPanel jPanelGroupDescr;
    private JXCollapsiblePane jXCollapsiblePane;
    private JXTitledSeparator jXTitledSeparatorGroupSize;
    // End of variables declaration//GEN-END:variables
    private List<Cdr> cdrs=new ArrayList<Cdr>();//TODO: Should be observed by
                                                //this instance for changes !
    private List<Voicemail> voicemails=new ArrayList<Voicemail>();


}
