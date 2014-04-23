package org.ezuce.common.ui.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.ezuce.common.ui.EzuceScrollBarUI;
import org.ezuce.common.ui.wrappers.interfaces.CallPanelInterface;

/**
 *
 *
 */
public class MakeCallPanel extends javax.swing.JPanel {
	private boolean onlyRoster;
	private MouseListener mouseListener;
	private boolean tabParent;
	private GroupsPanel groupsPanel;
    private JPanel jPanelContent;
    private JPanel jMultiPane;
    private JPanel jPanelContactProfileContainer;
    private JScrollPane jScrollPane;
    private EzuceContactProfile ezuceContactProfile;
	
    /** Creates new form MakeCallPanel */
    public MakeCallPanel(MouseListener mouseListener, boolean onlyRoster, boolean tabParent) {
    	this.mouseListener = mouseListener;
    	this.onlyRoster = onlyRoster;
    	this.tabParent = tabParent;
        initComponents();
    }

    public CallPanelInterface getCallPanel2Parent()
    {
        CallPanelInterface parent=null;
        Component p=this.getParent();
        while (!(p instanceof CallPanelInterface))
        {
            p=p.getParent();
            if (p==null) break;
        }
        if (p!=null) parent=(CallPanelInterface)p;
        return parent;
    }

    public GroupsPanel getGroupsPanel() {
        return groupsPanel;
    }
    
    public JPanel getJPanelContent()
    {
        return jPanelContent;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

    	setBackground(new Color(255, 255, 255));
        setMinimumSize(new Dimension(308, 101));
        setPreferredSize(new Dimension(314, 201));
        
        jPanelContent = new JPanel();
        jMultiPane = new JPanel();
        groupsPanel = new GroupsPanel(mouseListener, onlyRoster, tabParent);
        
        jMultiPane.setName("jPanelMultiPane"); // NOI18N        
        jMultiPane.setPreferredSize(null);
        jMultiPane.setLayout(new CardLayout());
        
        groupsPanel.setMinimumSize(new Dimension(21, 40));
        groupsPanel.setName("groupsPanel"); // NOI18N
        groupsPanel.setPreferredSize(new Dimension(300, 200));
        
        jMultiPane.add(groupsPanel, "card2");
        
        
        jPanelContactProfileContainer = new JPanel();
        jPanelContactProfileContainer.setLayout(new BorderLayout());         
        jPanelContactProfileContainer.setBorder(null);
        
        jScrollPane=new JScrollPane();
        jScrollPane.setBackground(new Color(255, 255, 255));
        jScrollPane.setBorder(null);
        jScrollPane.setName("jScrollPane"); // NOI18N
        jScrollPane.getVerticalScrollBar().setVisible(false);
        //jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.getVerticalScrollBar().setSize(0, 0);
        jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,5));        
        jScrollPane.getVerticalScrollBar().setUI(new EzuceScrollBarUI());
        jScrollPane.setViewportView(jPanelContactProfileContainer);
        
        jMultiPane.add(jScrollPane, "card3");
                       
        setEzuceContactProfile(new EzuceContactProfile());
        
        
        jPanelContent.setBackground(new Color(255, 255, 255));
        jPanelContent.setMinimumSize(new Dimension(0, 100));
        jPanelContent.setName("jPanelContent"); // NOI18N
        jPanelContent.setPreferredSize(new Dimension(308, 200));        

        GroupLayout jPanelContentLayout = new GroupLayout(jPanelContent);
        jPanelContent.setLayout(jPanelContentLayout);
        jPanelContentLayout.setHorizontalGroup(
            jPanelContentLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jMultiPane, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
        );
        jPanelContentLayout.setVerticalGroup(
            jPanelContentLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelContentLayout.createSequentialGroup()
                .addComponent(jMultiPane, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jPanelContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jPanelContent, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
        );

        //this.setBorder(new DropShadowBorder(Color.GRAY, 3, 0.3f, 2, true, true, true, true));
    }

    public void showContactProfile(boolean show, UserMiniPanelGloss umpg){
    	if (show){
    		ezuceContactProfile.setUserAvatarPanel(umpg);
    		((CardLayout)jMultiPane.getLayout()).show(jMultiPane,"card3");
    	}
    	else{
    		((CardLayout)jMultiPane.getLayout()).show(jMultiPane,"card2");
    	}
    }
    
    
    public EzuceContactProfile getEzuceContactProfile(){
    	return this.ezuceContactProfile;
    }
    
    public void setEzuceContactProfile(final EzuceContactProfile ecp){
    	SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				jPanelContactProfileContainer.removeAll();
		    	ezuceContactProfile=ecp;
		    	ezuceContactProfile.getBtnCloseProfile().addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						showContactProfile(false, null);
					}
				});
		    	jPanelContactProfileContainer.add(ezuceContactProfile, BorderLayout.CENTER);
		    	jPanelContactProfileContainer.revalidate();
		    	jPanelContactProfileContainer.repaint();
			}
		});
    	
    }
    
}
