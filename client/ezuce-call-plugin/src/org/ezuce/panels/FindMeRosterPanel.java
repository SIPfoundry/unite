package org.ezuce.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.cellrenderers.FindMeRosterCellRenderer;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.FindMeGroup;
import org.ezuce.common.rest.Ring;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.paints.LinearGradientPainter;
import org.ezuce.panels.interfaces.FindMePanelInterface;
import org.ezuce.common.windows.ContactSearchDialog;
import org.ezuce.common.windows.ContactSearchDialog.OKButtonType;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.MattePainter;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;

/**
 *
 * @author Razvan
 */
public class FindMeRosterPanel extends javax.swing.JPanel
                               implements ActionListener {

    /** Creates new form FindMeRosterPanel */
    public FindMeRosterPanel(FindMePanelInterface parent) {
        findMePanelParent=parent;
        initComponents();
        this.findMeGroup=new FindMeGroup();
        wireupActions();
    }

    private void wireupActions() {
        final javax.swing.ActionMap actionMap = org.jdesktop.application.Application.
                                    getInstance().getContext().getActionMap(this);

        javax.swing.Action action=actionMap.get("addNumberAction");
        action.putValue(action.SMALL_ICON, jButtonAddNr.getIcon());
        jButtonAddNr.setAction(action);

        action=actionMap.get("addNewGroupAction");
        action.putValue(action.SMALL_ICON, jButtonNewGroup.getIcon());
        jButtonNewGroup.setActionCommand(this.getName());
        jButtonNewGroup.setAction(action);
    }


    @Override
    public void setName(String name) {
        super.setName(name);
        jButtonNewGroup.setActionCommand(this.getName());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        findMeRosterCellRenderer = new FindMeRosterCellRenderer();
        jXCollapsiblePane = new JXCollapsiblePane();
        jPanelBottom = new JPanel();
        jLayeredPaneBottom = new JLayeredPane();
        jButtonNewGroup = new JButton();
        jButtonAddNr = new JButton();
        jXLabelBackground = new JXLabel();

        findMeRosterCellRenderer.setName("findMeRosterCellRenderer"); // NOI18N

        setBorder(new LineBorder(new Color(204, 204, 204), 1, true));
        setMaximumSize(new Dimension(298, 32767));
        setMinimumSize(new Dimension(296, 35));
        setLayout(new VerticalLayout());

        jXCollapsiblePane.setName("jXCollapsiblePane"); // NOI18N
        add(jXCollapsiblePane);

        jPanelBottom.setMaximumSize(new Dimension(294, 31));
        jPanelBottom.setMinimumSize(new Dimension(294, 31));
        jPanelBottom.setName("jPanelBottom"); // NOI18N
        jPanelBottom.setPreferredSize(new Dimension(294, 31));
        jPanelBottom.setLayout(new CardLayout());

        jLayeredPaneBottom.setDoubleBuffered(true);
        jLayeredPaneBottom.setMaximumSize(new Dimension(294, 31));
        jLayeredPaneBottom.setMinimumSize(new Dimension(294, 31));
        jLayeredPaneBottom.setName("jLayeredPaneBottom"); // NOI18N

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(FindMeRosterPanel.class);
        jButtonNewGroup.setIcon(resourceMap.getIcon("jButtonNewGroup.icon")); // NOI18N
        jButtonNewGroup.setRolloverIcon(resourceMap.getIcon("jButtonNewGroup.rollowerIcon"));
        jButtonNewGroup.setBorder(null);
        jButtonNewGroup.setBorderPainted(false);
        jButtonNewGroup.setContentAreaFilled(false);
        jButtonNewGroup.setDoubleBuffered(true);
        jButtonNewGroup.setFocusPainted(false);
        jButtonNewGroup.setHideActionText(true);
        jButtonNewGroup.setName("jButtonNewGroup"); // NOI18N
        jButtonNewGroup.setBounds(2, 5, 70, 20);
        jButtonNewGroup.setToolTipText(Res.getString("findme.followme.newgroup"));
        jLayeredPaneBottom.add(jButtonNewGroup, JLayeredPane.DEFAULT_LAYER);

        jButtonAddNr.setIcon(resourceMap.getIcon("jButtonAddNr.icon")); // NOI18N
        jButtonAddNr.setBorder(null);
        jButtonAddNr.setBorderPainted(false);
        jButtonAddNr.setContentAreaFilled(false);
        jButtonAddNr.setDoubleBuffered(true);
        jButtonAddNr.setFocusPainted(false);
        jButtonAddNr.setHideActionText(true);
        jButtonAddNr.setName("jButtonAddNr"); // NOI18N
        jButtonAddNr.setRolloverIcon(resourceMap.getIcon("jButtonAddNr.rolloverIcon")); // NOI18N
        jButtonAddNr.setBounds(265, 3, 26, 26);
        jLayeredPaneBottom.add(jButtonAddNr, JLayeredPane.DEFAULT_LAYER);

        jXLabelBackground.setIcon(resourceMap.getIcon("jXLabelBackground.icon")); // NOI18N
        jXLabelBackground.setDoubleBuffered(true);
        jXLabelBackground.setName("jXLabelBackground"); // NOI18N
        jXLabelBackground.setOpaque(true);
        jXLabelBackground.setBounds(0, 0, 294, 31);
        jLayeredPaneBottom.add(jXLabelBackground, JLayeredPane.DEFAULT_LAYER);

        jPanelBottom.add(jLayeredPaneBottom, "card2");

        add(jPanelBottom);
    }//GEN-END:initComponents

    @Action
    public void addNumberAction(ActionEvent ae) {
       addNumber();
    }

    public void addNumber() {
    	List<ContactListEntry> cles = ContactSearchDialog.showMultiContactSearchDialog(SparkManager.getMainWindow(), true, false, OKButtonType.OK);
       
    	if (cles==null || cles.isEmpty()) {
            //check if there are any other entries in this roster panel;
            //if this roster panel is empty, it has to be removed from its parent.
            if (numberOfMiniPanels==0) {
                this.getFindMePanelParent().removeRosterPanel(this);
            }
            return;
        }
        
        //Get the FindMePanel parent container:
        final FindMePanel parent=this.findParent();
        for (ContactListEntry cle:cles) {
            final FindMeMiniPanel fmmp=FindMeMiniPanel.createMiniPanel(cle);
            //if this is not the first mini panel in the roster panel, 
            //set its type to AT SAME TIME
            if (numberOfMiniPanels!=0) {
                fmmp.getRing().setType(Ring.RingType.AT_THE_SAME_TIME);
            }

            if (parent!=null)
            {
                parent.addContactToPanel(this, fmmp);

                //add the Ring ref. to the local CallSequence:
                //REMARK: DO NOT MAKE THIS CALL in method addContactToPanel(),
                //       because the method is also used when loading configuration
                //       from XML files (and then there is no need to update the CallSequence)
                this.findMeGroup.getRings().add(fmmp.getRing());
            }
        }
        
        //addUserToRosterPanel(fmmp);
        //System.out.println("Selected contact: "+cle.getNumber());
    }

    @Action
    public void addNewGroupAction(ActionEvent ae) {
        final FindMePanel parent=this.findParent();
        if (parent!=null) {
            FindMeRosterPanel fmrp=new FindMeRosterPanel(findMePanelParent);
            fmrp.setName(NAME_PREFIX+(parent.getNrRosterGroups()+1));
            parent.addRosterPanel(fmrp, ae.getActionCommand());
            fmrp.addNumber();
        }
    }

    private FindMePanel findParent() {
        FindMePanel parent=null;
        Component c=this.getParent();
        while (c!=null) {
            if (!(c instanceof FindMePanel)) {
                c=c.getParent();
            }
            else {
                parent=(FindMePanel)c;
                break;
            }
        }
        return parent;
    }



    public JButton getNewGroupButton() {
        return this.jButtonNewGroup;
    }

    public JButton getAddNrButton() {
        return this.jButtonAddNr;
    }

    /**
     * Adds a FindMeMiniPanel component to this panel. ALWAYS use this method to
     * add a component to this panel, as it updates the number of FindMeMiniPanel
     * instances in this container.
     * @param fmmp
     */
    public synchronized void addUserToRosterPanel(FindMeMiniPanel fmmp) {
        fmmp.getDeleteButton().addActionListener(this);
        fmmp.getDeleteButton().setActionCommand(REMOVE_USER);
        this.jXCollapsiblePane.getContentPane().add(fmmp);
        this.numberOfMiniPanels++;
        this.revalidate();
    }

    /**
     * Removes a FindMeMiniPanel component from this panel. ALWAYS use this method to
     * remove a component from this panel, as it updates the number of FindMeMiniPanel
     * instances in this container.
     * @param fmmp
     */
    public synchronized void removeUserFromRosterPanel(FindMeMiniPanel fmmp) {
        this.jXCollapsiblePane.getContentPane().remove(fmmp);
        this.numberOfMiniPanels--;
        this.revalidate();
    }

    /**
     * Removes a FindMeMiniPanel component from this panel. ALWAYS use this method to
     * remove a component from this panel, as it updates the number of FindMeMiniPanel
     * instances in this container.
     * @param index
     */
    public synchronized void removeUserFromRosterPanel(int index) {
        this.jXCollapsiblePane.getContentPane().remove(index);
        this.numberOfMiniPanels--;
        this.revalidate();
    }
    
    public synchronized FindMeMiniPanel getMiniPanelByJID(String jid) {
        FindMeMiniPanel fmmp=null;
        
        Component[] comps=this.jXCollapsiblePane.getContentPane().getComponents();
        for (Component c : comps) {
            if (c instanceof FindMeMiniPanel) {
                final FindMeMiniPanel f=(FindMeMiniPanel)c;
                if (StringUtils.equals(f.getRing().getNumber(), Utils.getExtension(jid))) {
                    fmmp=f;
                    break;
                }
            }
        }

        return fmmp;
    }

    public Object getUserFromRoster(int index) {
        return this.jXCollapsiblePane.getContentPane().getComponent(index);
    }

    public JXCollapsiblePane getCollapsiblePane()
    {
        return this.jXCollapsiblePane;
    }

    public int getNumberOfMiniPanels()
    {
        return this.numberOfMiniPanels;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals(REMOVE_USER))
        {
            if (ae.getSource() instanceof JButton)
            {
                //Get a reference to the FindMeMiniPanel instance hosting this
                //JButton - the source of the ActionEvent.
                Component c=(JButton)ae.getSource();
                while (c!=null)
                {
                    if (c instanceof FindMeMiniPanel)
                    {
                        break;
                    }
                    else
                    {
                        c=c.getParent();
                    }
                }
                if (c==null)
                {
                    return;
                }
                boolean isFirstInFindMePanel=this.getFindMePanelParent().isFirstInRoster(this);
                final FindMeMiniPanel x=(FindMeMiniPanel)c;
                if (x.isLoggedUser() && isFirstInFindMePanel)
                {
                    return;//one cannot delete themselves from the first roster panel.
                }
                x.getDeleteButton().removeActionListener(this);
                //also remove the Ring reference from local list:
                this.findMeGroup.getRings().remove(x.getRing());
                //this.removeUserFromRosterPanel(x);
                this.getFindMePanelParent().removeContactFromPanel(this, x);

                //next, check if there are any other entries in this roster panel;
                //if this roster panel is empty, it has to be removed from its parent.
                if (numberOfMiniPanels==0)
                {
                    this.getFindMePanelParent().removeRosterPanel(this);
                }
                else //if there are some entries left, set the RingType of the first one to "If no response"
                {
                    if (this.getCollapsiblePane().getContentPane().getComponent(0) instanceof FindMeMiniPanel)
                    {
                        final FindMeMiniPanel fmmp=
                                (FindMeMiniPanel)this.getCollapsiblePane().getContentPane().getComponent(0);
                        fmmp.getRing().setType(Ring.RingType.IF_NO_RESPONSE);
                    }
                }
            }
        }
    }

    public FindMePanelInterface getFindMePanelParent()
    {
        return this.findMePanelParent;
    }

    public FindMeGroup getFindMeGroup()
    {
        return this.findMeGroup;
    }

    public void setFindMeGroup(FindMeGroup fmg)
    {
        this.findMeGroup=fmg;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private FindMeRosterCellRenderer findMeRosterCellRenderer;
    private JButton jButtonAddNr;
    private JButton jButtonNewGroup;
    private JLayeredPane jLayeredPaneBottom;
    private JPanel jPanelBottom;
    private JXCollapsiblePane jXCollapsiblePane;
    private JXLabel jXLabelBackground;
    // End of variables declaration//GEN-END:variables
    private MattePainter jXPanelActiveBackgroundPainter=
                                LinearGradientPainter.findMeRosterPanelActBgPainter();
    private MattePainter jXPanelInactiveBackgroundPainter=
                                LinearGradientPainter.findMeRosterPanelInactBgPainter();
    private int numberOfMiniPanels=0;
    private final String REMOVE_USER="REMOVE_USER";
    public final String NAME_PREFIX="findMeRosterPanel";
    public final String LINK_NAME_SUFFIX="VLink";
    public final String GAP_NAME_SUFFIX="VGap";
    private FindMeGroup findMeGroup;
    private FindMePanelInterface findMePanelParent;

}
