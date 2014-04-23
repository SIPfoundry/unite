package org.ezuce.common.ui.popup;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.actions.ContactActions;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.UserGroupCommonInterface;
import org.ezuce.common.ui.wrappers.interfaces.UserMiniPanelCommonInterface;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class UserMiniPanelPopup extends UserMiniPanelPopupBase implements PopupMenuListener
{
	private static final long serialVersionUID = 1492191688937504208L;



    private JMenu callMenuJMenu;
    private JMenu transferToJMenu;
    private JMenu inviteIntoBridgeJMenu;
    private JMenuItem startChatJMenuItem;
    private JMenuItem startGroupChatJMenuItem;
    private JMenuItem sendEmailJMenuItem;
    private JMenuItem sendFileJMenuItem;
    private JSeparator separator1;
    private JMenuItem removeFromRosterJMenuItem;
    private JMenuItem renameJMenuItem;
    private JMenuItem viewProfileJMenuItem;
    private JSeparator separator2;
    private JMenu addToRosterJMenu;
    private ContactItem contactItem;
    private ContactGroup contactGroup;


    private final UserGroupCommonInterface parent;
    private UserMiniPanelCommonInterface targetUmpg;

	private javax.swing.ActionMap actionMap;

    public UserMiniPanelPopup()
    {
        super();
        parent=null;
        addPopupMenuListener(this);
    }

    public UserMiniPanelPopup(UserGroupCommonInterface invoker, ContactItem contactItem, ContactGroup contactGroup)
    {
        super();
        parent=invoker;
        this.contactItem = contactItem;
        this.contactGroup = contactGroup;
        addPopupMenuListener(this);
    }

	public void displayPopup(final UserMiniPanelCommonInterface umpg, final MouseEvent me) {
		displayPopup(umpg, parent.getComponent(), me.getPoint().x, me.getPoint().y);
	}

	public void displayPopup(final UserMiniPanelCommonInterface umpg, final Component comp, final MouseEvent me)
    {
		displayPopup(umpg, comp, me.getPoint().x, me.getPoint().y);
	}

	public void displayPopup(final UserMiniPanelCommonInterface umpg, final Component comp, final int x, final int y) {
        if (parent!=null)
        {
            this.targetUmpg=umpg;
			ContactActions actions = new ContactActions(targetUmpg, comp);
			actionMap = org.jdesktop.application.Application.getInstance().getContext()
					.getActionMap(ContactActions.class, actions);
            initComponents();
			show(comp, x, y);
        }
    }

    private void initComponents()
    {
        this.removeAll();
        if (parent!=null)
        {
            if (parent.isInsideRosterGroup() &&
                targetUmpg.getContact().getImId()!=null &&
                targetUmpg.getContact().getImId().length()>0)
            {
                /// if user inside roster => initComponentsInsideRoster
                initComponentsInsideRoster();
            }
            else if (!parent.isInsideRosterGroup() &&
                    targetUmpg.getContact().getNumber()!=null &&
                    targetUmpg.getContact().getNumber().length()>0)
            {
                /// if user outside roster => initComponentsOutsideRoster
                initComponentsOutsideRoster();
            }
        setDoubleBuffered(true);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBorderPainted(true);
        setOpaque(true);
        }
    }

    private void initComponentsInsideRoster() {
		final boolean sipLoggedIn = RestManager.getInstance().isLoggedIn();
        final VCard vCard = SparkManager.getVCardManager().getVCard(targetUmpg.getJID());

        callMenuJMenu=new JMenu(resourceMap.getString("callMenuJMenu.text"));
        callMenuJMenu.setIcon(resourceMap.getIcon("callMenuJMenu.icon"));

		transferToJMenu = new JMenu(resourceMap.getString("transferToJMenu.text"));
		transferToJMenu.setIcon(resourceMap.getIcon("transferToJMenu.icon"));

		inviteIntoBridgeJMenu = new JMenu(resourceMap.getString("invite.to.bridge"));
		inviteIntoBridgeJMenu.setIcon(resourceMap.getIcon("inviteIntoBridgeAction.icon"));

		if (sipLoggedIn) {
			final List<JMenuItem> items = CallMenuBuilder.buildCallMenuItemsFromId(targetUmpg.getJID(), actionMap);
			for (JMenuItem jmi : items) {
				callMenuJMenu.add(jmi);
			}

			transferToJMenu.add(new JMenuItem(resourceMap.getString("loading.text")));
			buildTransferToMenuItems();

			inviteIntoBridgeJMenu.add(new JMenuItem(resourceMap.getString("loading.text")));
			buildInviteToBridgeMenuItems();
		} else {
			callMenuJMenu.setEnabled(false);
			transferToJMenu.setEnabled(false);
			inviteIntoBridgeJMenu.setEnabled(false);
		}

        startChatJMenuItem=new JMenuItem(actionMap.get("startChatAction"));
        startChatJMenuItem.setText(resourceMap.getString("startChatAction.text"));
        startChatJMenuItem.setIcon(resourceMap.getIcon("startChatAction.icon"));

        startGroupChatJMenuItem=new JMenuItem(actionMap.get("startGroupChatAction"));
        startGroupChatJMenuItem.setText(resourceMap.getString("startGroupChatAction.text"));
        startGroupChatJMenuItem.setIcon(resourceMap.getIcon("startGroupChatAction.icon"));

        sendEmailJMenuItem=new JMenuItem(actionMap.get("sendEmailAction"));        
        sendEmailJMenuItem.setText(resourceMap.getString("sendEmailAction.text"));
        sendEmailJMenuItem.setIcon(resourceMap.getIcon("sendEmailAction.icon"));
        if (vCard!=null) {
            String destEmail=vCard.getEmailWork()!=null?vCard.getEmailWork():vCard.getEmailHome();            
            sendEmailJMenuItem.setActionCommand(destEmail);
        }
        if (contactGroup != null) {
        	parent.addRemoveFromGroupJMenuItem(contactGroup, this, contactItem);
        }
        sendFileJMenuItem=new JMenuItem(actionMap.get("sendFileAction"));
        sendFileJMenuItem.setText(resourceMap.getString("sendFileAction.text"));
        sendFileJMenuItem.setIcon(resourceMap.getIcon("sendFileAction.icon"));

		separator1 = new JSeparator(SwingConstants.HORIZONTAL);

        removeFromRosterJMenuItem=new JMenuItem(actionMap.get("removeFromRosterAction"));
        removeFromRosterJMenuItem.setText(resourceMap.getString("removeFromRosterAction.text"));
        removeFromRosterJMenuItem.setIcon(resourceMap.getIcon("removeFromRosterAction.icon"));

        renameJMenuItem=new JMenuItem(actionMap.get("renameAction"));
        //The text of this menu item MUST be set in this manner
        //in order for the Copy to/Move to menu items to work properly,
        //because their functionality is reused from a plugin.
        renameJMenuItem.setText(Res.getString("menuitem.rename"));
        renameJMenuItem.setIcon(resourceMap.getIcon("renameAction.icon"));

        viewProfileJMenuItem=new JMenuItem(actionMap.get("viewProfileAction"));
        viewProfileJMenuItem.setText(resourceMap.getString("viewProfileAction.text"));
        viewProfileJMenuItem.setIcon(resourceMap.getIcon("viewProfileAction.icon"));

		separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        JMenuItem item = CallMenuBuilder.buildCallSipMenuItem(targetUmpg.getExtension(), actionMap, resourceMap);
        if (item != null) {
        	this.add(item);
        }
        this.add(callMenuJMenu);
        //Temporary remove transfer to function
        //this.add(transferToJMenu);
        this.add(inviteIntoBridgeJMenu);
        this.add(startChatJMenuItem);
        this.add(startGroupChatJMenuItem);
        this.add(sendEmailJMenuItem);
        if (targetUmpg.getContact().getPresence()!=null) {
            this.add(sendFileJMenuItem);
        }
        this.add(separator1);


        // Check if user is in shared group.
        boolean isSharedGroup = false;
        if (targetUmpg.getContact() instanceof ContactItemWrapper)
        {
           final ContactGroup contactGroup=((ContactItemWrapper)targetUmpg.getContact()).getContactGroup();
           if (contactGroup!=null && contactGroup.isSharedGroup())
           {
               isSharedGroup=true;
           }
        }

		boolean isInSharedGroup = false;
		for (final ContactGroup cGroup : SparkManager.getContactList().getContactGroups()) {
			if (cGroup.isSharedGroup()) {
				final ContactItem it = cGroup.getContactItemByJID(targetUmpg.getJID());
				if (it != null) {
					isInSharedGroup = true;
				}
			}
		}
		if (!isSharedGroup && !isInSharedGroup) {
			this.add(removeFromRosterJMenuItem);
		}

        this.add(renameJMenuItem);

        final ContactList contactList = SparkManager.getContactList();
        final ContactListEntry cle = targetUmpg.getContact();
        final ContactItemWrapper ciw;

		if (cle instanceof ContactItemWrapper) {
			ciw = (ContactItemWrapper) cle;
			ciw.getContactGroup().getList().setSelectedValue(ciw.getContactItem(), false);
		} else {
			ciw = null;
		}


        this.add(viewProfileJMenuItem);
        this.add(separator2);

        if (ciw != null) {
                //The following will cause the View History and View Client Version options to be added to the popup menu :			
                contactList.fireContextMenuListenerPopup(this, ciw.getContactItem());
        }
    }

    private void initComponentsOutsideRoster()
    {
        callMenuJMenu=new JMenu(resourceMap.getString("callMenuJMenu.text"));
        callMenuJMenu.setIcon(resourceMap.getIcon("callMenuJMenu.icon"));

		final List<JMenuItem> items = CallMenuBuilder.buildCallMenuItemsFromId(targetUmpg.getJID(), actionMap);
		for (JMenuItem jmi : items)
        {
            callMenuJMenu.add(jmi);
        }

        transferToJMenu=new JMenu(resourceMap.getString("transferToJMenu.text"));
        transferToJMenu.setIcon(resourceMap.getIcon("transferToJMenu.icon"));
		transferToJMenu.add(new JMenuItem(resourceMap.getString("loading.text")));
		buildTransferToMenuItems();

        inviteIntoBridgeJMenu=new JMenu(resourceMap.getString("invite.to.bridge"));
        inviteIntoBridgeJMenu.setIcon(resourceMap.getIcon("inviteIntoBridgeAction.icon"));
		inviteIntoBridgeJMenu.add(new JMenuItem(resourceMap.getString("loading.text")));
		buildInviteToBridgeMenuItems();

        sendEmailJMenuItem=new JMenuItem(actionMap.get("sendEmailAction"));
        sendEmailJMenuItem.setText(resourceMap.getString("sendEmailAction.text"));
        sendEmailJMenuItem.setIcon(resourceMap.getIcon("sendEmailAction.icon"));

        addToRosterJMenu=new JMenu(resourceMap.getString("addToRosterJMenu.text"));
        addToRosterJMenu.setIcon(resourceMap.getIcon("addToRosterJMenu.icon"));
        List<JMenuItem> addToRosterJMenuItems=this.buildAddToRosterJMenuItems();
        for (JMenuItem jmi:addToRosterJMenuItems)
        {
            addToRosterJMenu.add(jmi);
        }
        JMenuItem item = CallMenuBuilder.buildCallSipMenuItem(targetUmpg.getExtension(), actionMap, resourceMap);
        if (item != null) {
        	this.add(item);
        }
        this.add(callMenuJMenu);
        //temporary remove transferTo function
        //this.add(transferToJMenu);
        this.add(inviteIntoBridgeJMenu);
        this.add(sendEmailJMenuItem);
        this.add(addToRosterJMenu);
    }

	private void buildTransferToMenuItems() {
		TransferToBuilder tBuilder = new TransferToBuilder(transferToJMenu, actionMap.get("transferToAction"),
				targetUmpg.getContact().getNumber());
		tBuilder.execute();
	}

	private void buildInviteToBridgeMenuItems() {
		InviteBuilder iBuilder = new InviteBuilder(inviteIntoBridgeJMenu, actionMap.get("inviteIntoBridgeAction"),
				resourceMap.getIcon("inviteIntoBridgeAction.icon"));
		iBuilder.execute();
	}

    private List<JMenuItem> buildAddToRosterJMenuItems()
    {
		final List<JMenuItem> menuItems = new ArrayList<JMenuItem>();

        final ContactList contactList = SparkManager.getContactList();
        final List<ContactGroup> contactGroups = contactList.getContactGroups();
        Collections.sort(contactGroups, ContactList.GROUP_COMPARATOR);

        for (final ContactGroup group : contactGroups) {
            if (group.isUnfiledGroup() || group.isOfflineGroup()) {
                continue;
            }

            JMenuItem jmi=new JMenuItem(actionMap.get("addToRosterAction"));
            jmi.setText(group.getGroupName());
            jmi.setActionCommand(group.getGroupName());
            menuItems.add(jmi);
        }
        return menuItems;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (targetUmpg!=null)
        {
            targetUmpg.focusGained(null);
        }
        
        if (e.getSource() instanceof JPopupMenu)
        {
            if (targetUmpg.getContact().getPresence() != null &&(!targetUmpg.getContact().getPresence().isAvailable() 
              || !targetUmpg.getContact().getPresence().isAway())) {            
                JPopupMenu jpm=(JPopupMenu)e.getSource();
                final MenuElement[] items = jpm.getSubElements();
                for (MenuElement item:items)
                {
                    if (item instanceof JMenuItem)
                    {
                        final JMenuItem jmi = (JMenuItem)item;
                        if (jmi.getText().equals(Res.getString("menuitem.alert.when.online"))
                            || jmi.getText().equals(Res.getString("menuitem.remove.alert.when.online")))
                        {
                            item.getComponent().setVisible(false);
                            break;
                        }
                    }
                }
            }            
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if (targetUmpg!=null)
        {
            targetUmpg.focusLost(null);
        }
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        
    }

    
}
