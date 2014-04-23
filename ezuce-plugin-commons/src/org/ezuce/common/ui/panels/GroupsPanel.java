package org.ezuce.common.ui.panels;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.ezuce.common.CachePhoneBook;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.AlphabetFilterPanel;
import org.ezuce.common.ui.EzuceContactInfoWindow;
import org.ezuce.common.ui.EzuceScrollBarUI;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;


/**
 * Accommodates user group panels, laid on a vertical layout. Each user group
 * panel should contain user mini panels, grouped in a collapsible container.
 * Each such group would display the group name, and the number of entries in
 * the group.
 *
 */
public class GroupsPanel extends javax.swing.JPanel {
    boolean onlyRoster;
    MouseListener mouseListener;
    private boolean tabParent;
    private JPanel jPanel;
    private JScrollPane jScrollPane;    
    protected AlphabetFilterPanel alphabetFilterPanel;
    private final LoadingPanel loading=new LoadingPanel();
    private HashSet<String> lettersToFilterBy=new HashSet<String>();
    
    protected ActionListener alphabetActionListener=new ActionListener() {		
		@Override
		public void actionPerformed(ActionEvent e) {
			String letter=e.getActionCommand();
			if (letter!=null){
				if (e.getSource() instanceof JToggleButton){
					if (((JToggleButton)(e.getSource())).isSelected()){
						lettersToFilterBy.add(letter);
					}
					else{
						lettersToFilterBy.remove(letter);
					}
				}
				
				for (UserGroupPanel ugp : getUserGrupPanelList()){
					ugp.filterGroupItemsByLetters(lettersToFilterBy);
				}
			}
		}
	};
	
	protected MouseListener showContactProfileMouseListener = new MouseAdapter(){
		@Override
		public void mouseClicked(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (me.getSource() instanceof JList) {
					final JList usersList = (JList) me.getSource();
					final int selectedIndex = usersList.locationToIndex(me.getPoint());
					final UserMiniPanelGloss focusGainer = (UserMiniPanelGloss) usersList.getModel().getElementAt(selectedIndex);						
					EzuceContactInfoWindow.getInstance().dispose();					
					int n = usersList.getModel().getSize();
					for (int i = 0; i < n; i++) {
						((UserMiniPanelGloss) (usersList.getModel()
								.getElementAt(i))).focusLost(null);
					}
					focusGainer.focusGained(null);
					boolean canShow = false;		
					int mouseX = MouseInfo.getPointerInfo().getLocation().x; 
					if(mouseX >= usersList.getLocationOnScreen().x
					   && mouseX <= usersList.getLocationOnScreen().x+focusGainer.getAvatarContainerSize().width)
					{
						canShow = true;
					}
					if (canShow){
						showContactProfile(true, focusGainer);
					}
				}
			}			
		}
	};
    
    public GroupsPanel(MouseListener mouseListener, boolean onlyRoster, boolean tabParent) {
    	this.onlyRoster = onlyRoster;
    	this.tabParent = tabParent;
        this.mouseListener = mouseListener;
        initComponents();
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

	public MakeCallPanel getMakeCallPanelParent(){
        MakeCallPanel parent=null;
        Component p=this.getParent();
        while (!(p instanceof MakeCallPanel))
        {
            p=p.getParent();
            if (p==null) break;
        }
        if (p!=null) parent=(MakeCallPanel)p;
        return parent;
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

    	alphabetFilterPanel=new AlphabetFilterPanel();
    	
        jScrollPane = new JScrollPane();
        jPanel = new JPanel();

        setBackground(new Color(255, 255, 255));
        setMinimumSize(new Dimension(340, 0));
        setPreferredSize(new Dimension(340, 220));

        jScrollPane.setBackground(new Color(255, 255, 255));
        jScrollPane.setBorder(null);
        jScrollPane.setName("jScrollPane"); // NOI18N
        jScrollPane.getVerticalScrollBar().setVisible(false);
        jScrollPane.getVerticalScrollBar().setSize(0, 0);
        jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,5));
        jScrollPane.getVerticalScrollBar().setUI(new EzuceScrollBarUI());
        jScrollPane.getVerticalScrollBar().addMouseListener(new MouseAdapter() {

        		@Override
        		public void mouseEntered(MouseEvent me){
        			SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,5));
							jScrollPane.getVerticalScrollBar().repaint();
						}
					});
        			
        		}
        		
        		@Override
        		public void mouseExited(MouseEvent me){
        			SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,5));
							jScrollPane.getVerticalScrollBar().repaint();
						}
					});
        			
        		}
        
        });

        jPanel.setBackground(new Color(255, 255, 255));
        jPanel.setName("jPanel"); // NOI18N
        jPanel.setLayout(new VerticalLayout());
        jScrollPane.setViewportView(jPanel);
        
        JPanel groupsContainerPanel=new JPanel();
		groupsContainerPanel.setBorder(null);
		groupsContainerPanel.setOpaque(false);
		groupsContainerPanel.setLayout(new BorderLayout());
		groupsContainerPanel.add(jScrollPane, BorderLayout.CENTER);
		groupsContainerPanel.add(alphabetFilterPanel, BorderLayout.EAST);
		alphabetFilterPanel.configureActionListenerForAll(alphabetActionListener);
		
		
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            //.addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
            .addComponent(groupsContainerPanel, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            //.addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
            .addComponent(groupsContainerPanel, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        
        FooterPanel footerPanel = new FooterPanel(mouseListener, this, onlyRoster);
        footerPanel.setName("footerPanel");
        footerPanel.setMaximumSize(new Dimension(3200, 30));
        footerPanel.setMinimumSize(new Dimension(10, 30));
        footerPanel.setPreferredSize(new Dimension(340, 30));
        
        jPanel.add(footerPanel);
       
        if (tabParent) {
        	CachePhoneBook.addTabListener(footerPanel);
        } else {
        	CachePhoneBook.dialogListener = footerPanel;
        }
    }//GEN-END:initComponents

    public List<UserGroupPanel> getUserGrupPanelList()
    {
       Component[] cmps = this.jPanel.getComponents();
       ArrayList<UserGroupPanel> ugps=new ArrayList<UserGroupPanel>(cmps.length);
       for (Component c:cmps)
       {
        if (c instanceof UserGroupPanel) {
            ugps.add((UserGroupPanel)c);
        }
       }
       return ugps;
    }
  
    public boolean isTabParent() {
		return tabParent;
	}

    public void showContactProfile(final boolean show, final UserMiniPanelGloss umpg){
    	SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				getMakeCallPanelParent().showContactProfile(show, umpg);
			}
		});
    	
    }
    
	/**
     * Replaces the current list of groups with the given list of user groups.
     * @param ugps The new groups to be displayed.
     */
	public void setUserGrupPanels(List<UserGroupPanel> ugps) {
		for (UserGroupPanel ugp : getUserGrupPanelList()) {
			ugp.clearGroup();
		}

		List<Component> toRemove = new ArrayList<Component>();
		MouseListener ml = null;
		for (Component c : this.jPanel.getComponents()) {
			if (c instanceof UserGroupPanel) {
				toRemove.add(c);
			} else if (ml == null && (c instanceof FooterPanel)) {
				ml = ((FooterPanel) c).getMouseListener();
			}
		}
		for (Component c : toRemove) {
			if (ml != null) {
				c.removeMouseListener(ml);
			}
			this.jPanel.remove(c);
		}
		System.gc();

				
		for (UserGroupPanel ugp : ugps) {
			ugp.addMouseListenersToMiniPanels(showContactProfileMouseListener);
			this.jPanel.add((Component) ugp);
		}
		this.validate();
		this.repaint();
	}

    public void addUserGroupPanel(final UserGroupPanel ugp) {
        this.jPanel.add(ugp);
        this.validate();
        this.repaint();
    }

    public void removeUserGroupPanel(UserGroupPanel ugp) {
        ugp.clearGroup();
        this.jPanel.remove(ugp);
        this.validate();
        this.repaint();
    }      

    /**
     * Clears all groups shown, and then displays the "Loading" message.
     */
	public void showLoading() {
		// clear the groups already displayed:
		MouseListener ml = null;
		for (Component c : this.jPanel.getComponents()) {
			if (c instanceof FooterPanel) {
				ml = ((FooterPanel) c).getMouseListener();
				break;
			}
		}
		List<UserGroupPanel> groups = this.getUserGrupPanelList();
		for (UserGroupPanel ugp : groups) {
			if (ml != null) {
				ugp.removeMouseListener(ml);
			}
			this.removeUserGroupPanel(ugp);
		}
		// loading=new LoadingPanel();
		this.jPanel.add(loading);
		loading.setVisible(true);
		this.jPanel.revalidate();
	}

    /**
     * Hides the "Loading" message.
     */
    public void hideLoading() {
        if (loading!=null) {
            loading.setVisible(false);
            this.jPanel.remove(loading);
            this.jPanel.revalidate();
            //loading=null;
        }
    }
}
