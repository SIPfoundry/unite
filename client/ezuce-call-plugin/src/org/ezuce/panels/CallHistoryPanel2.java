package org.ezuce.panels;

import org.ezuce.common.ui.panels.LoadingPanel;
import org.ezuce.tasks.PopulateHistoryListTask;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.spark.util.log.Log;

/**
 * Contains several CallHistoryGroupPanel instances, grouped in a jScrollPane,
 * representing groups of call history events (calls from the day before, calls
 * from the week before, calls from the month before).
 * @author Razvan
 */
public class CallHistoryPanel2 extends javax.swing.JPanel implements HierarchyListener {
	private boolean reload;	    
    private CallPanel2 parentPanel;

    /** Creates new form CallHistoryPanel2 */
    public CallHistoryPanel2() {
        initComponents();
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        addHierarchyListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jScrollPane = new JScrollPane();
        jPanel = new JPanel();

        setBackground(new Color(255, 255, 255));

        jScrollPane.setBackground(new Color(255, 255, 255));
        jScrollPane.setBorder(null);
        jScrollPane.setDoubleBuffered(true);
        jScrollPane.setName("jScrollPane"); // NOI18N

        jPanel.setBackground(new Color(255, 255, 255));
        jPanel.setName("jPanel"); // NOI18N
        jPanel.setLayout(new VerticalLayout());
        jScrollPane.setViewportView(jPanel);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
    }//GEN-END:initComponents

    public List<CallHistoryGroupPanel> getHistoryGroupPanelList()
    {
       Component[] cmps = this.jPanel.getComponents();
       ArrayList<CallHistoryGroupPanel> ugps=
                            new ArrayList<CallHistoryGroupPanel>(cmps.length);
       for (Component c:cmps)
       {
        ugps.add((CallHistoryGroupPanel)c);
       }
       return ugps;
    }

    /**
     * Filters the items, making invisible those
     * types whose corresponding arguments are set to FALSE.
     * @param showMissedCalls If FALSE, this group hides the missed calls items.
     * @param showReceivedCalls If FALSE, this group hides the received calls items.
     * @param showDialedCalls If FALSE, this group hides the dialed calls items.
     * @param showVoiceMails If FALSE, this group hides the voice mail items.
     */
    public void filterHistoryItems(boolean showMissedCalls, boolean showReceivedCalls, boolean showDialedCalls, boolean showVoiceMails)
    {
        for (CallHistoryGroupPanel chgp : getHistoryGroupPanelList())
        {
            chgp.filterHistoryItems(showMissedCalls, showReceivedCalls, showDialedCalls, showVoiceMails);
        }
    }

    /**
     * Adds a group of call history items to the current panel.
     * @param chgp The group of call history items to add.
     */
    public void addHistoryGroupPanel(final CallHistoryGroupPanel chgp)
    {
        this.jPanel.add(chgp);
        this.validate();
    }

    /**
     * Removes the group of call history items from the current panel.
     * @param chgp The group of call history items to remove.
     */
    public void removeHistoryGroupPanel(CallHistoryGroupPanel chgp)
    {
        chgp.clearGroup();
        this.jPanel.remove(chgp);
        this.validate();
    }

    /**
     * Clears the history panel of all groups displayed.
     */
    public void removeAllHistoryGroups()
    {
        for (CallHistoryGroupPanel chgp:getHistoryGroupPanelList())
        {
            chgp.clearGroup();
        }
        this.jPanel.removeAll();
        for (PropertyChangeListener pcl : jPanel.getPropertyChangeListeners())
        {
                jPanel.removePropertyChangeListener(pcl);
        }
        this.validate();
        this.repaint();
        System.gc();
    }

    /**
     * Displays the "Loading" message.
     */
    public void showLoading()
    {
        //loading=new LoadingPanel();
        this.jPanel.add(loading);
        loading.setVisible(true);
        this.jPanel.revalidate();
    }

    /**
     * Hides the "Loading" message.
     */
    public void hideLoading()
    {
        if (loading!=null)
        {
            loading.setVisible(false);
            this.jPanel.remove(loading);
            this.jPanel.revalidate();
            //loading=null;
        }
    }
    
	@Override
	public void hierarchyChanged(HierarchyEvent arg0) {
		if(isShowing() && reload) {
			Log.warning("Reload history...");
	        parentPanel.getMakeCallTopPanel().getCallTabBtnPanel().getjToggleButtonFilterMissedCalls().setSelected(true);
	        parentPanel.getMakeCallTopPanel().getCallTabBtnPanel().getjToggleButtonFilterReceivedCalls().setSelected(true);
	        parentPanel.getMakeCallTopPanel().getCallTabBtnPanel().getjToggleButtonFilterDialedCalls().setSelected(true);
	        parentPanel.getMakeCallTopPanel().getCallTabBtnPanel().getjToggleButtonFilterVoicemails().setSelected(true);			
			PopulateHistoryListTask task = new PopulateHistoryListTask(
                    org.jdesktop.application.Application.getInstance(),
                    parentPanel,this,
                    true,
                    true,
                    true,
                    true);
			task.execute();
		}	
	}

    public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	public CallPanel2 getParentPanel() {
		return parentPanel;
	}
	
	public void setParentPanel(CallPanel2 parent) {
		this.parentPanel = parent;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables
    private final LoadingPanel loading=new LoadingPanel();
}
