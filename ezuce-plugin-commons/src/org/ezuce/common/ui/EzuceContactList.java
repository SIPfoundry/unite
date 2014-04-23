package org.ezuce.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.impl.DummyCommonInterface;
import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.EzuceScrollBarUI;
import org.ezuce.common.ui.panels.EzuceContactProfile;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;
import org.ezuce.common.ui.popup.UserMiniPanelPopup;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.UserGroupCommonInterface;
import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.manager.StreamingManager;
import org.ezuce.unitemedia.context.ServiceContext;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.PreferenceListener;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceContactList extends ContactList implements UserGroupCommonInterface, 
															 PopupMenuListener,
															 PreferenceListener {

	private static final long serialVersionUID = -5136689515967458217L;
    private ContactItem selectedItem;
    private JPanel jPanelContactProfileContainer;
    private JScrollPane jScrollPane;
    private EzuceContactProfile ezuceContactProfile;
    private Component originalListContent;
    
    public EzuceContactList() {
		super();
		SettingsManager.addPreferenceListener(this);
		
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
        
        
	}
    
    public void setEzuceContactProfile(final EzuceContactProfile ecp, final EzuceContactItem ci){
    	SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {				
		    	ezuceContactProfile=ecp;
		    	//TODO: set the user avatar as the ContactItem reference (need to modify EzuceContactProfile).
		    	ContactItemWrapper ciw = new ContactItemWrapper(ci, null);
		    	UserMiniPanelGloss umpg = new UserMiniPanelGloss(ciw);
		    	ezuceContactProfile.setUserAvatarPanel(umpg);
		    	ezuceContactProfile.getBtnCloseProfile().addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						SwingUtilities.invokeLater(new Runnable() {							
							@Override
							public void run() {
								EzuceContactList.this.remove(jScrollPane);
								EzuceContactList.this.add(originalListContent, BorderLayout.CENTER);
								EzuceContactList.this.revalidate();
								EzuceContactList.this.repaint();								
							}
						});					
					}
				});
		    	jPanelContactProfileContainer.removeAll();
		    	jPanelContactProfileContainer.add(ezuceContactProfile, BorderLayout.CENTER);
		    	jPanelContactProfileContainer.revalidate();
		    	jPanelContactProfileContainer.repaint();
		    	
		    	BorderLayout layout = (BorderLayout) EzuceContactList.this.getLayout();		    	
		    	originalListContent = layout.getLayoutComponent(BorderLayout.CENTER);
		    	EzuceContactList.this.remove(originalListContent);
		    	EzuceContactList.this.add(jScrollPane, BorderLayout.CENTER);
		    	EzuceContactList.this.revalidate();
		    	EzuceContactList.this.repaint();
			}
		});
    	
    }
        
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showPopup(Component component, MouseEvent e, final ContactItem item) {
		if (item.getJID() == null) {
			return;
		}

		selectedItem = item;
		String groupName = item.getGroupName();
		ContactGroup c = getContactGroup(groupName);
		final UserMiniPanelPopup popup = new UserMiniPanelPopup(this, selectedItem, c);
		popup.addPopupMenuListener(this);

		popup.displayPopup(new DummyCommonInterface(item), c, e);
	}
        
    @Override  
    public void contactItemClicked(ContactItem item) {
        if (getActiveItem()!=null){
            getActiveItem().setBorder(null);
        }
        super.contactItemClicked(item); 
        //If clicked on the mugshot - display the contact profile:
        boolean canShow = false;
        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        if (mouseX >= this.getLocationOnScreen().x
        	&& mouseX <= this.getLocationOnScreen().x + ((EzuceContactItem)item).getAvatarContainerSize().width){
        	canShow=true;
        }
        if (canShow){                	
        	setEzuceContactProfile(new EzuceContactProfile(), (EzuceContactItem)item);
        }
    }

	@Override
	public boolean isInsideRosterGroup() {
		return true;
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public void initialize() {
		super.initialize();
	}

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (selectedItem!=null){
            //selectedItem.setBorder(borderFocused);
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if (selectedItem!=null){
            selectedItem.setBorder(null);
            this.repaint();
        }
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        
    }
    
    @Override
    public void presenceChanged(Presence presence) {
    	Object item = getContactItemByJID(StringUtils.parseBareAddress(presence.getFrom()));
    	if (item instanceof VCardLoaderCallback && Utils.isVCardUpdated(presence)) {
    		AsyncLoader.getInstance().execute(presence.getFrom(), (VCardLoaderCallback)item);
    	}
    }
    
    public void updateLook() {
    	SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				int fontSize = SettingsManager.getLocalPreferences().getContactListFontSize();
				Font font = new Font(((EzuceLocalPreferences)SettingsManager.getLocalPreferences()).getContactListFontName(), Font.PLAIN, fontSize);
				for (ContactGroup group : getContactGroups()) {
					ListModel model = group.getModel();
					for (int i = 0; i < model.getSize(); i++) {
						EzuceContactItem item = (EzuceContactItem) model.getElementAt(i);
						item.getDisplayNameLabel().setFont(font);
						item.getDescriptionLabel().setFont(font);
						item.getStatusLabel().setFont(font);
						item.getLocationLabel().setFont(font);
					}
				}
				// a small hack to make list's interval updates
				updateListInterval();
				Workspace.getInstance().getWorkspacePane().repaint();
			}
		});
	}
	
	public void updateListInterval() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				for (ContactGroup group : getContactGroups()) {
					DefaultListModel defaultListModel = (DefaultListModel) group.getList().getModel();
					if (defaultListModel.getSize() > 0)
						for (ListDataListener listener : defaultListModel.getListDataListeners())
							listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, 1));
				}
			}
		});
	}

	@Override
	public void preferencesChanged(LocalPreferences preference) {
		updateLook();
	}
        
    @Override
    public void clientReconnected() {
        super.clientReconnected();	
        try {		   		
            if (Config.getInstance().isRegisterAsPhone()) {
            	PhoneManager phManager = PhoneManager.getInstance();
            	//make sure trigger registration task by unregistering
            	phManager.unregisterUser();
            }
        } catch (Exception ex) {
                Log.error("Cannot initialize media", ex);
        }

    }

        
}
