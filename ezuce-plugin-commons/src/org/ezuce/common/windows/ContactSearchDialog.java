package org.ezuce.common.windows;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.ezuce.common.AWTUtilitiesWrapper;
import org.ezuce.common.ComponentMover;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.ui.actions.task.SearchTask;
import org.ezuce.common.ui.panels.MakeCallPanel;
import org.ezuce.common.ui.panels.UserGroupPanel;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;
import org.ezuce.common.ui.popup.UserMiniPanelPopupInvite;
import org.ezuce.common.ui.wrappers.AnyNumberWrapper;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.SearchInterface;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXSearchField.LayoutStyle;

/**
 *
 * @author Razvan
 */
public class ContactSearchDialog extends javax.swing.JDialog implements SearchInterface {
	private boolean onlyRoster;
        private final ComponentMover componentMover;
	private OKButtonType type;
	public enum OKButtonType {
		INVITE,
		OK;
	}
    private UserMiniPanelPopupInvite invitePopup;
    /** Creates new form ContactSearchDialog */
    public ContactSearchDialog(java.awt.Frame parent, boolean modal, boolean onlyRoster, OKButtonType type, boolean performSearch) {    	
        super(parent, modal);
        this.onlyRoster = onlyRoster;
        this.type = type;
        setDefaultLookAndFeelDecorated(false);
        
        
        initComponents();
        componentMover=new ComponentMover(this, jLayeredPane);

        if ((AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.TRANSLUCENT))
                &&
             (AWTUtilitiesWrapper.isTranslucencyCapable(getGraphicsConfiguration()))) {
            AWTUtilitiesWrapper.setWindowOpaque(this, false);
        }
        wireupActions();
        //Make sure to first display the data
        if (performSearch) {
        	performSearch().execute();
        }
    }

    private void wireupActions() {
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.
                                    getInstance().getContext().getActionMap(this);

        javax.swing.Action action=actionMap.get("performSearch");
        this.getjXSearchField().setAction(action);

        action = actionMap.get("okAction");
        action.putValue(action.SMALL_ICON, this.jButtonOk.getIcon());
        jButtonOk.setAction(action);
        action = actionMap.get("inviteAction");
        action.putValue(action.NAME, "Invite");
        invitePopup=new UserMiniPanelPopupInvite(action);        
    }        
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {                          
    	setResizable(false);
        jLayeredPane = new JLayeredPane();
        jPanelContents = new JPanel();
        jXSearchField = new JXSearchField();
        jButtonOk = new JButton();
        jButtonClose = new JButton();
        jLabelSeparator = new JLabel();
        makeCallPanel = new MakeCallPanel(mouseListener, onlyRoster, false);        
        jPanelBackground = new JPanel();
        jLabelBackground = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setName("contactSearchDialog"); // NOI18N
        setUndecorated(true);

        jLayeredPane.setDoubleBuffered(true);
        jLayeredPane.setName("jLayeredPane"); // NOI18N

        jPanelContents.setMinimumSize(new Dimension(375, 330));
        jPanelContents.setName("jPanelContents"); // NOI18N
        jPanelContents.setOpaque(false);
        jPanelContents.setPreferredSize(new Dimension(375, 330));

        jXSearchField.setInstantSearchDelay(600);
        jXSearchField.setLayoutStyle(LayoutStyle.MAC);
        jXSearchField.setPrompt("Find a user or dial a number");
        jXSearchField.setPromptFontStyle(Font.PLAIN);
        jXSearchField.setUseNativeSearchFieldIfPossible(false);
        jXSearchField.setDoubleBuffered(true);
        jXSearchField.setFont(new Font("Arial", 0, 12));
        jXSearchField.setName("jXSearchField"); // NOI18N

        ResourceMap resMap = Application.getInstance().getContext().getResourceMap(ContactSearchDialog.class);
        if (type.equals(OKButtonType.INVITE)) {
        	jButtonOk.setIcon(resMap.getIcon("jButtonInvite.icon"));
        } else {
        	jButtonOk.setIcon(resMap.getIcon("jButtonOk.icon"));
        }
        jButtonOk.setBorder(null);
        jButtonOk.setBorderPainted(false);
        jButtonOk.setContentAreaFilled(false);
        jButtonOk.setDoubleBuffered(true);
        jButtonOk.setFocusPainted(false);
        jButtonOk.setHideActionText(true);
        jButtonOk.setName("jButtonOk"); // NOI18N

        ActionMap actionMap = Application.getInstance().getContext().getActionMap(ContactSearchDialog.class, this);
        jButtonClose.setAction(actionMap.get("closeAction")); // NOI18N
        jButtonClose.setIcon(resMap.getIcon("jButtonClose.icon")); // NOI18N
        jButtonClose.setBorder(null);
        jButtonClose.setBorderPainted(false);
        jButtonClose.setContentAreaFilled(false);
        jButtonClose.setFocusPainted(false);
        jButtonClose.setHideActionText(true);
        jButtonClose.setName("jButtonClose"); // NOI18N

        jLabelSeparator.setIcon(resMap.getIcon("jLabelSeparator.icon")); // NOI18N
        jLabelSeparator.setDoubleBuffered(true);
        jLabelSeparator.setName("jLabelSeparator"); // NOI18N

        makeCallPanel.setName("makeCallPanel"); // NOI18N

        GroupLayout jPanelContentsLayout = new GroupLayout(jPanelContents);
        jPanelContents.setLayout(jPanelContentsLayout);
        jPanelContentsLayout.setHorizontalGroup(
            jPanelContentsLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanelContentsLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelContentsLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPanelContentsLayout.createSequentialGroup()
                        .addComponent(jXSearchField, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jButtonOk, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jButtonClose))
                    .addComponent(jLabelSeparator)
                    .addComponent(makeCallPanel, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanelContentsLayout.setVerticalGroup(
            jPanelContentsLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelContentsLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanelContentsLayout.createParallelGroup(Alignment.TRAILING, false)
                    .addComponent(jButtonClose)
                    .addComponent(jButtonOk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXSearchField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jLabelSeparator)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(makeCallPanel, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanelContents.setBounds(0, 0, 375, 330);
        jLayeredPane.add(jPanelContents, JLayeredPane.DEFAULT_LAYER);

        jPanelBackground.setName("jPanelBackground"); // NOI18N
        jPanelBackground.setOpaque(false);
        jPanelBackground.setPreferredSize(new Dimension(375, 330));

        jLabelBackground.setIcon(resMap.getIcon("jLabelBackground.icon")); // NOI18N
        jLabelBackground.setName("jLabelBackground"); // NOI18N

        GroupLayout jPanelBackgroundLayout = new GroupLayout(jPanelBackground);
        jPanelBackground.setLayout(jPanelBackgroundLayout);
        jPanelBackgroundLayout.setHorizontalGroup(
            jPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLabelBackground, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );
        jPanelBackgroundLayout.setVerticalGroup(
            jPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLabelBackground, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        jPanelBackground.setBounds(0, 0, 375, 330);
        jLayeredPane.add(jPanelBackground, JLayeredPane.DEFAULT_LAYER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        pack();
    }

    @Override
    public JXSearchField getjXSearchField() {
        return this.jXSearchField;
    }

    @Override
    public MakeCallPanel getMakeCallPanel() {
        return this.makeCallPanel;
    }

    @Action
    public void closeAction() {
        dispose();        
        selectedContact=null;
        selectedContacts.clear();        
    }
    
    @Override
    public void dispose() {    	
    	super.dispose();
    }

    @Action
    public void okAction() {
        if (selectedContact == null) {
            if (this.jXSearchField.getText()!=null && this.jXSearchField.getText().trim().length()>0) {                    
            	final AnyNumberWrapper noContactCle = new AnyNumberWrapper();
            	noContactCle.setNumber(this.jXSearchField.getText().trim());
            	this.selectedContact = noContactCle;
            	this.selectedContacts.add(selectedContact);
            }
        }
        this.dispose();
    }
    
    @Action
    public void inviteAction() {
    	okAction();
    }

	private MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent me) {
			final JList usersList = (JList) me.getSource();
			int[] selection = usersList.getSelectedIndices();
			if (selection.length == 0) {
				return;
			}
			
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (me.getSource() instanceof JList) {
					final int selectedIndex = selection[0];
					final UserMiniPanelGloss focusGainer = (UserMiniPanelGloss) usersList.getModel().getElementAt(selectedIndex);					
					selectedContact = focusGainer.getContact();
					selectedContacts.clear();
					for (int index : selection) {
                         final UserMiniPanelGloss fg = (UserMiniPanelGloss) usersList.getModel().getElementAt(index);
                         selectedContacts.add(fg.getContact());
					}
					if (me.getClickCount() == 2) {
						okAction();
					}
				}
			} else if (me.getButton() == MouseEvent.BUTTON3) {
				final int selectedIndex=usersList.locationToIndex(me.getPoint());
                final UserMiniPanelGloss focusGainer = (UserMiniPanelGloss)usersList.getModel().getElementAt(selectedIndex);
				ContactListEntry rightSelectedContact = focusGainer.getContact();
				//show popup only over selection
				if (selectedContacts.contains(rightSelectedContact)) {
                    invitePopup.displayPopup(usersList, me);
				}
			}
		}
	};

     @Action
    public Task performSearch() {
        if (this.searchTask!=null){
        	this.searchTask.cancel(true);
        }
        this.selectedContact=null;
        this.selectedContacts.clear();
        boolean showPopup = type.equals(OKButtonType.OK);
        this.searchTask=new SearchTask(Application.getInstance(),this, false, onlyRoster, showPopup, false);
        return searchTask;
    }     

    @Action
    public void cancelSearch() {
        if (this.searchTask!=null && !this.searchTask.isDone()) {
            this.searchTask.cancel(true);
        }
    }

    public ContactListEntry getSelectedContact() {
        return this.selectedContact;
    }
    
    public List<ContactListEntry> getSelectedContacts() {
        return this.selectedContacts;
    }

    public static ContactListEntry showContactSearchDialog(Frame parentFrame, boolean modal, boolean onlyRoster, OKButtonType type) {
        ContactListEntry cle=null;
        ContactSearchDialog csd=new ContactSearchDialog(parentFrame, modal, onlyRoster, type, false);
        //Undecoration work-around for Luna:
        csd.setVisible(false);
        csd.dispose();
        csd=new ContactSearchDialog(parentFrame, modal, onlyRoster, type, true);
        //end workaround.
    	csd.setLocationRelativeTo(parentFrame);
        csd.setVisible(true);
        cle=csd.getSelectedContact();
        csd.dispose();
        return cle;
    }

    public static List<ContactListEntry> showMultiContactSearchDialog(Frame parentFrame, boolean modal, boolean onlyRoster, OKButtonType type) {    	
        List<ContactListEntry> cles=new ArrayList<ContactListEntry>();
        ContactSearchDialog csd=new ContactSearchDialog(parentFrame, modal, onlyRoster, type, false);
        //Undecoration work-around for Luna:
        csd.setVisible(false);
        csd.dispose();
        csd=new ContactSearchDialog(parentFrame, modal, onlyRoster, type, true);
        //end workaround.
    	csd.setLocationRelativeTo(parentFrame);
        csd.setVisible(true);
        cles=csd.getSelectedContacts();
        csd.dispose();
        return cles;
    }
    
    private JButton jButtonClose;
    private JButton jButtonOk;
    private JLabel jLabelBackground;
    private JLabel jLabelSeparator;
    private JLayeredPane jLayeredPane;
    private JPanel jPanelBackground;
    private JPanel jPanelContents;
    private JXSearchField jXSearchField;
    private MakeCallPanel makeCallPanel;    
    private Task searchTask;
    private ContactListEntry selectedContact=null;
    private List<ContactListEntry> selectedContacts=new ArrayList<ContactListEntry>();
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(UserGroupPanel.class);
	@Override
	public MouseListener getMouseListener() {		
		return mouseListener;
	}
}
