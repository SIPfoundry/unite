package org.ezuce.common.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


import org.ezuce.common.cellrenderers.ContactListCellRenderer;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.ui.AlphabetFilterPanel;
import org.ezuce.common.ui.EzuceContactInfoWindow;
import org.ezuce.common.ui.popup.UserMiniPanelPopup;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.UserGroupCommonInterface;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.MattePainter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactInfoWindow;
import org.jivesoftware.spark.ui.ContactItem;
import org.xbill.DNS.utils.base32.Alphabet;

/**
 * Accommodates user mini panels, grouped in a collapsible container. The name
 * of the group and the number of entries in the group are displayed. Each user
 * mini panel displays brief information about a user in the current group.
 * 
 */
public class UserGroupPanel extends JPanel implements UserGroupCommonInterface {
	
	private static final javax.swing.ImageIcon BTN_BG_OPEN = 
			new javax.swing.ImageIcon(UserGroupPanel.class.getResource("/resources/images/contact-group_open_btn.png"));
	private static final javax.swing.ImageIcon BTN_BG_CLOSED = 
			new javax.swing.ImageIcon(UserGroupPanel.class.getResource("/resources/images/contact-group_closed_btn.png"));
	
	private ContactListCellRenderer contactListCellRenderer;
	private JLabel lblGroupName;
	private JLabel lblGroupSize;
	private JList jList;
	private GroupTitleJPanel jPanelGroupDescr;
	private JXCollapsiblePane jXCollapsiblePane;
	private JToggleButton btnOpenClose;
	protected DefaultListModel listModel = new DefaultListModel();
	protected final ListMotionListener motionListener = new ListMotionListener();	
	private boolean mouseDragged = false;
	protected boolean canShowPopup;
	private boolean addContextMenu = true;	

	protected UserMiniPanelPopup userMiniPanelPopUp;	

	private final Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
	private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	protected boolean contactInfoEnabled = true;
	private final ResourceMap resourceMap = Application.getInstance()
			.getContext().getResourceMap(UserGroupPanel.class);
	
	private boolean canShowVCard;
	private DisplayWindowTask timerTask = null;
    private Timer timer = new Timer();
        
    
	/** Creates new form UserGroupPanel */
	public UserGroupPanel() {
		initComponents();
		configureHideButton("Group name");
	}

	/**
	 * Creates a new UserGroupPanel instance, based on the ContactGroup received
	 * as an argument.
	 * 
	 * @param cg
	 */
	public UserGroupPanel(String groupName, boolean canShowVCard,boolean addContextMenu) {
		this.addContextMenu = addContextMenu;
		this.canShowVCard = canShowVCard;
		initComponents();
		configureHideButton(groupName);
		customize();
	}

	private void configureHideButton(String groupName) {
		//ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
		//Action toggleAction = this.jXCollapsiblePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
		//toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON,resourceMap.getIcon("icon.uncollapsed"));
		//toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON,resourceMap.getIcon("icon.collapsed"));
		//this.jLabelGroupName.setAction(toggleAction);
		this.setGroupName(groupName);
	}

	/**
	 * Reads the information stored in ContactGroup property, then builds the
	 * required graphical elements in order to display the ContactGroup
	 * information.
	 */
	private void customize() {
		updateGroupSize();
		jList.addMouseListener(mouseListener);
		jList.addMouseMotionListener(motionListener);
		if (addContextMenu) {
			this.addContextMenu();
		}
	}

	private MouseAdapter mouseListener = new MouseAdapter() {
		@Override
        public void mouseEntered(MouseEvent mouseEvent) {               
        	canShowPopup = true;
        	timerTask = new DisplayWindowTask(mouseEvent);            
        	timer.schedule(timerTask, 500, 1000);
        }

		@Override
        public void mouseExited(MouseEvent mouseEvent) {               
            canShowPopup = false;
            EzuceContactInfoWindow.getInstance().dispose();
        }
        
		@Override
		public void mouseClicked(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON3 && addContextMenu) {
				if (me.getSource() instanceof JList) {
					final JList usersList = (JList) me.getSource();
					final int selectedIndex = usersList.locationToIndex(me
							.getPoint());
					final UserMiniPanelGloss focusGainer = (UserMiniPanelGloss) usersList
							.getModel().getElementAt(selectedIndex);
					UserGroupPanel.this.setCursor(hourglassCursor);
					EzuceContactInfoWindow.getInstance().dispose();
					int n = usersList.getModel().getSize();
					for (int i = 0; i < n; i++) {
						((UserMiniPanelGloss) (usersList.getModel()
								.getElementAt(i))).focusLost(null);
					}
					focusGainer.focusGained(null);
					UserGroupPanel.this.jList.repaint();
					UserGroupPanel.this.jXCollapsiblePane.revalidate();
					UserGroupPanel.this.userMiniPanelPopUp.displayPopup(
							focusGainer, me);
					UserGroupPanel.this.setCursor(defaultCursor);
				}
			}			
		}
	};

	private void addContextMenu() {
		this.userMiniPanelPopUp = new UserMiniPanelPopup(this, null, null);
	}

	@Override
	public boolean isInsideRosterGroup() {
		if (this.getGroupName().equalsIgnoreCase(
				resourceMap.getString("Call.contactList.insiders.title"))) {
			return true;
		}
		return false;
	}

	public void clearGroup() {
		this.jList.removeMouseListener(mouseListener);
		this.jList.removeMouseMotionListener(motionListener);
		this.listModel.clear();
	}

    private class DisplayWindowTask extends TimerTask {
        private MouseEvent event;
		private boolean newPopupShown = false;
        
		public DisplayWindowTask(MouseEvent e) {
			event = e;
		}	

		@Override
		public void run() {		
			if (canShowPopup) {
				if (!newPopupShown && !mouseDragged) {
					displayWindow(event);
					newPopupShown = true;					
				}
			}
		}
		
        public void setEvent(MouseEvent event) {
			this.event = event;
		}	

		public void setNewPopupShown(boolean popupChanged) {
			this.newPopupShown = popupChanged;
		}

		public boolean isNewPopupShown() {
			return newPopupShown;
		}		
    }
    
    private class ListMotionListener extends MouseMotionAdapter {
    	
    	@Override
        public void mouseMoved(MouseEvent e) {
            if (!canShowPopup) {
                return;
            }

            if (e == null) {
                return;
            }
            timerTask.setEvent(e);           
            if (needToChangePopup(e) && timerTask.isNewPopupShown()) {
            	EzuceContactInfoWindow.getInstance().dispose();            	
            	timerTask.setNewPopupShown(false);            	
            }
            mouseDragged = false;
        }
    	
    	@Override
    	public void mouseDragged(MouseEvent e) {
    		if(timerTask.isNewPopupShown()) {
    			EzuceContactInfoWindow.getInstance().dispose();    	    	
    		}
    		mouseDragged = true;
    	}
    }
    

    /**
     * Displays the <code>ContactInfoWindow</code>.
     *
     * @param e the mouseEvent that triggered this event.
     */
    private void displayWindow(MouseEvent e) {
    	if(canShowVCard) {
			EzuceContactInfoWindow.getInstance().display(this, e);			
    	}
    }
    
    private boolean needToChangePopup(MouseEvent e) {
    	ContactInfoWindow contact = EzuceContactInfoWindow.getInstance();
        int loc = jList.locationToIndex(e.getPoint());
        UserMiniPanelGloss item = (UserMiniPanelGloss)getList().getModel().getElementAt(loc);
        return item == null || contact == null || contact.getContactItem() == null ? true : !contact.getContactItem().getJID().equals(item.getJID());
    }

	public JList getList() {
		return jList;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
			
		contactListCellRenderer = new ContactListCellRenderer();
		
		jPanelGroupDescr = new GroupTitleJPanel();
		
		lblGroupName = new JLabel();
		
		lblGroupSize = new JLabel();
		
		btnOpenClose = new JToggleButton();
		jXCollapsiblePane = new JXCollapsiblePane();
		jList = new JList();

		setBackground(new Color(255, 255, 255));
		VerticalLayout verticalLayout1 = new VerticalLayout();
		verticalLayout1.setGap(0);
		setLayout(verticalLayout1);

		jPanelGroupDescr.setBackground(new Color(246, 245, 245));
		jPanelGroupDescr.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		jPanelGroupDescr.setMaximumSize(new Dimension(32767, 30));
		jPanelGroupDescr.setMinimumSize(new Dimension(1, 30));
		jPanelGroupDescr.setName("jPanelGroupDescr");
		jPanelGroupDescr.setPreferredSize(new Dimension(264, 30));

		Font font = new Font("Calibri", 0, 14);
		
		lblGroupName.setFont(font);
		lblGroupName.setForeground(Color.GRAY);
		lblGroupName.setText("Group name");
		lblGroupName.setBorder(null);
		lblGroupName.setHorizontalAlignment(SwingConstants.LEADING);
		lblGroupName.setVerticalAlignment(SwingConstants.CENTER);
		lblGroupName.setName("lblGroupName");
		
		lblGroupSize.setFont(font);
		lblGroupSize.setForeground(Color.GRAY);
		lblGroupSize.setText("(0)");
		lblGroupSize.setBorder(null);
		lblGroupSize.setHorizontalAlignment(SwingConstants.LEADING);
		lblGroupSize.setVerticalAlignment(SwingConstants.CENTER);
		lblGroupSize.setName("lblGroupSize");

		btnOpenClose.setName("btnOpenClose");
		btnOpenClose.setBorder(null);
		btnOpenClose.setBorderPainted(false);
		btnOpenClose.setContentAreaFilled(false);
		btnOpenClose.setDoubleBuffered(true);
		btnOpenClose.setFocusPainted(false);
		btnOpenClose.setHideActionText(true);
		btnOpenClose.setIcon(BTN_BG_CLOSED);
		btnOpenClose.setSelectedIcon(BTN_BG_OPEN);		
		btnOpenClose.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Action toggleAction = UserGroupPanel.this.jXCollapsiblePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
				toggleAction.actionPerformed(new ActionEvent(btnOpenClose, ActionEvent.ACTION_PERFORMED, null));
				if (jPanelGroupDescr instanceof GroupTitleJPanel){
					((GroupTitleJPanel)jPanelGroupDescr).setOpen(!((GroupTitleJPanel)jPanelGroupDescr).isOpen());
				}
			}
		});
				
		jPanelGroupDescr.setLayout(new BoxLayout(jPanelGroupDescr, BoxLayout.LINE_AXIS));
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 32767)));
		jPanelGroupDescr.add(lblGroupName);
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767)));
		jPanelGroupDescr.add(lblGroupSize);
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(32767, 32767)));
		jPanelGroupDescr.add(btnOpenClose);
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767)));

		add(jPanelGroupDescr);

		jXCollapsiblePane.setBackground(new Color(255, 255, 255));
		jXCollapsiblePane.setBackgroundPainter(new MattePainter(new Color(255,
				255, 255)));
		jXCollapsiblePane.setName("jXCollapsiblePane");

		jList.setModel(listModel);
		jList.setCellRenderer(contactListCellRenderer);
		jList.setDoubleBuffered(true);
		jList.setName("jList");
		jList.setPrototypeCellValue(new UserMiniPanelGloss());		

		jXCollapsiblePane.getContentPane().setLayout(new VerticalLayout(0));
		
		//JPanel listContainerPanel=new JPanel();
		//listContainerPanel.setBorder(null);
		//listContainerPanel.setOpaque(false);
		//listContainerPanel.setLayout(new BorderLayout());
		//listContainerPanel.add(jList, BorderLayout.CENTER);
		//listContainerPanel.add(alphabetFilterPanel, BorderLayout.EAST);
		//alphabetFilterPanel.configureActionListenerForAll(alphabetActionListener);
				
		//jXCollapsiblePane.getContentPane().add(listContainerPanel);
		jXCollapsiblePane.getContentPane().add(jList);
		
		add(jXCollapsiblePane);
	}
	

	/**
	 * Add a panel with user details (icon, name, status, etc) to the list
	 * displayed in the collapsible pane.
	 * 
	 * @param ump
	 */
	public void addUserMiniPanel(final UserMiniPanelGloss ump) {
		this.listModel.addElement(ump);
		ump.setParent(jList);
		this.jList.revalidate();
	}

	/**
	 * Remove a panel with user details from the list displayed in the
	 * collapsible pane.
	 * 
	 * @param ump
	 */
	public void removeUserMiniPanel(UserMiniPanelGloss ump) {
		this.listModel.removeElement(ump);
	}

	public void setGroupName(String groupName) {
		this.lblGroupName.setText(groupName);
	}

	public String getGroupName() {
		return this.lblGroupName.getText();
	}

	public void setGroupSize(int availableEntries) {
		this.lblGroupSize.setText("("+availableEntries+")");
	}

	public String getGroupSize() {
		return this.lblGroupSize.getText().replace("(", "").replace(")", "");
	}

	public void removeUserMiniPanel(String username) {
		for (Object o : this.listModel.toArray()) {
			if (o instanceof UserMiniPanelGloss) {
				UserMiniPanelGloss umpg = (UserMiniPanelGloss) o;
				if (umpg.getUserDisplayName().equals(username)) {
					this.removeUserMiniPanel(umpg);
				}
			}
		}
	}

	public UserMiniPanelGloss getUserMiniPanelByJID(String bareJid) {
		for (Object o : this.listModel.toArray()) {
			if (o instanceof UserMiniPanelGloss) {
				UserMiniPanelGloss umpg = (UserMiniPanelGloss) o;
				String jid = umpg.getJID();
				if (jid != null
						&& StringUtils.parseBareAddress(jid).equals(bareJid)) {
					return umpg;
				}
			}
		}
		return null;
	}

	public List<UserMiniPanelGloss> getUserMiniPanels() {
		List<UserMiniPanelGloss> userMiniPanels = new ArrayList<UserMiniPanelGloss>();
		for (Object o : this.listModel.toArray()) {
			if (o instanceof UserMiniPanelGloss) {
				userMiniPanels.add((UserMiniPanelGloss) o);
			}
		}
		return userMiniPanels;
	}

	/**
	 * Sets the list of user mini panels to be displayed in the collapsible
	 * pane.
	 * 
	 * @param userMiniPanels
	 */
	public void setUserMiniPanels(List<UserMiniPanelGloss> userMiniPanels) {
		this.listModel.clear();

		for (UserMiniPanelGloss u : userMiniPanels) {
			this.addUserMiniPanel(u);
		}
		this.updateGroupSize();
	}

	/**
	 * For each UserMiniPanelGloss instance displayed, adds the given
	 * FocusListener.
	 * 
	 * @param fl
	 */
	public void addMouseListenersToMiniPanels(MouseListener ml) {
		this.jList.addMouseListener(ml);
	}

	/**
	 * Hides the elements found in the list usersToHide, if they are present in
	 * the local container.
	 * 
	 * @param umpgList
	 */
	public void hideElements(final List<UserMiniPanelGloss> usersToHide) {
		for (Object o : this.listModel.toArray()) {
			if (usersToHide.contains(o)) {
				((UserMiniPanelGloss) o).setVisible(false);
			} else {
				((UserMiniPanelGloss) o).setVisible(true);
			}
		}
	}

	/**
	 * Computes and displays the number of items displayed in the current group.
	 * This method can be used instead of <code>setGroupSize(int)</code> to make
	 * this component compute the group size by itself.
	 */
	public void updateGroupSize() {
		this.setGroupSize(this.listModel.getSize());
	}

	@Override
	public Component getComponent() {
		return this;
	}

	public void setContactInfoEnabled(boolean b) {
		this.contactInfoEnabled = b;
	}

	public boolean isContactInfoEnabled() {
		return this.contactInfoEnabled;
	}

	@Override
	public void addRemoveFromGroupJMenuItem(ContactGroup contactGroup,
			JPopupMenu popup, ContactItem item) {
		// TODO Auto-generated method stub
		
	}
	
	public void filterGroupItemsByLetters(final HashSet<String> letters){
		if (letters==null || letters.isEmpty()){//if there are no letters to filter by, show the original, complete listModel.
			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					jList.setModel(listModel);
					jList.revalidate();
					jList.repaint();
				}
			});			
		}
		else
		{
			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					DefaultListModel filteredListModel=new DefaultListModel();
					for (Object o : listModel.toArray()) {
						UserMiniPanelGloss umpg = ((UserMiniPanelGloss)o);
						String firstLetter=umpg.getUserDisplayName().substring(0, 1).toLowerCase();
						if (letters.contains(firstLetter)){
							filteredListModel.addElement(umpg);
						}
					}									
					jList.setModel(filteredListModel);
					
					jList.revalidate();
					jList.repaint();
				}
			});			
		}
	}

	@Override
	public void updateListInterval() {

	}
	
}

