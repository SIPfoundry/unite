package org.ezuce.common.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Beans;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ezuce.common.ui.ContactProfileTabbedPaneUI;
import org.ezuce.common.ui.EzuceContactProfileField;
import org.ezuce.common.ui.EzuceContactInfoWindow;
import org.ezuce.common.ui.actions.ContactActions;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;
import org.ezuce.common.ui.popup.AlertOnlineBuilder;
import org.ezuce.common.ui.popup.InviteBuilder;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.status.StatusBar;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.profile.VCardManager;
import java.awt.Container;

public class EzuceContactProfile extends JPanel {
	
	protected static Color LBL_VALUE_FORE_LIGHT_COLOR = new Color(60, 165, 195);
	protected static Color LBL_VALUE_FORE_DARK_COLOR = Color.DARK_GRAY;
	protected static Color LBL_FORE_COLOR = Color.GRAY;
	protected static Font LBL_FONT = new Font("Arial", Font.BOLD, 10); 
	protected static Font LBL_VALUE_FONT = new Font("Arial", Font.PLAIN, 14);
	
	protected JPanel panelContactDetails;
	protected UserMiniPanelGloss userAvatarPanel;
	protected ContactListEntry contactListEntry;
	protected JPanel panelContact;
	protected JPanel panelBusiness;
	protected JPanel panelDetailsInternal;
	protected JPanel panelDetailsOfficeDid;
	protected JPanel panelDetailsCell;
	protected JPanel panelDetailsEmail;
	protected JPanel panelDetailsCorpIm;
	protected JPanel panelDetailsConfBridge;
	protected JPanel panelDetailsConfChat;
	protected Component verticalGluePnlContact;
	protected JLabel lblDetailsInternal;
	protected JLabel lblDetailsIcnInternal;
	protected JLabel lblDetailsValueInternal;
	protected JSeparator separatorDetailsInternal;
	protected JPanel panelInternal;
	protected JPanel panelOfficeDid;
	protected JSeparator separatorOfficeDid;
	protected JPanel panelCell;
	protected JSeparator separatorCell;
	protected JPanel panelField;
	protected JPanel panelCorpIm;
	protected JPanel panelConfBridge;
	protected JPanel panelConfChat;
	protected JSeparator separatorEmail;
	protected JSeparator separatorCorpIm;
	protected JSeparator separatorConfBridge;
	protected JSeparator separatorConfChat;
	protected JLabel lblDetailsOffideDid;
	protected JLabel lblDetailsIcnOfficeDid;
	protected JLabel lblDetailsValueOfficeDid;
	protected JPanel panelAvatar;
	protected JButton btnAddUserToRoster;
	protected JLabel lblDetailsCell;
	protected JLabel lblDetailsIcnCell;
	protected JLabel lblDetailsValueCell;
	protected JLabel lblDetailsEmail;
	protected JLabel lblDetailsIcnEmail;
	protected JLabel lblDetailsValueEmail;
	protected JLabel lblDetailsCorpIm;
	protected JLabel lblDetailsIcnCorpIm;
	protected JLabel lblDetailsValueCorpIm;
	protected JLabel lblDetailsConfBridge;
	protected JLabel lblDetailsIcnConfBridge;
	protected JLabel lblDetailsValueConfBridge;
	protected JLabel lblDetailsConfChat;
	protected JLabel lblDetailsIcnConfChat;
	protected JLabel lblDetailsValueConfChat;
	protected JPanel panelDetailsConfBrInvite;
	protected JPanel panelConfBrInvite;
	protected JLabel lblDetailsConfBrInvite;
	protected JLabel lblDetailsIcnConfBrInvite;
	protected JLabel lblDetailsValueConfBrInvite;
	protected JSeparator separatorConfBrInvite;
	protected Component hGlueInternal;
	protected Component hGlueOfficeDid;
	protected Component hGlueCell;
	protected Component hGlueEmail;
	protected Component hGlueCorpIm;
	protected Component hGlueConfBridge;
	protected Component hGlueConfBrInvite;
	protected Component hGlueConfChat;
	protected Component hStrutInternal;
	protected Component hStrutOfficeDid;
	protected Component hStrutCell;
	protected Component hStrutEmail;
	protected Component hSrutCorpIm;
	protected Component hStrutConfBridge;
	protected Component hStrutConfBrInvite;
	protected Component hStrutConfChat;
	protected Component horizontalStrut;
	protected Component verticalStrut;
	protected JLabel lblContactTabHeader;
	protected JLabel lblBusinessTabHeader;
	protected JPanel panelBackButton;
	protected JButton btnCloseProfile;
	protected JPanel panelAddToRoster;    
	protected JPanel panelDetailsWork;
	protected Component verticalGluePnlBusiness;
	protected JPanel panelWork;
	protected JSeparator separatorWork;
	protected JPanel panelDetailsBusinessAddress;
	protected JPanel panelBusinessAddress;
	protected JSeparator separatorBusinessAddress;
	protected JPanel panelDetailsManager;
	protected JPanel panelManager;
	protected JSeparator separatorManager;
	protected JPanel panelDetailsTeam;
	protected JPanel panelJob;
	protected JPanel panelCompany;
	protected JPanel panelDepartment;
	protected Component hStrutJob;
	protected JLabel lblDetailsJob;
	protected JLabel lblDetailsIcnJob;
	protected JLabel lblDetailsValueJob;
	protected Component hGlueJob;
	protected Component hStrutCompany;
	protected JLabel lblDetailsCompany;
	protected JLabel lblDetailsIcnCompany;
	protected JLabel lblDetailsValueCompany;
	protected Component hGlueCompany;
	protected Component hStrutDepartment;
	protected JLabel lblDetailsDepartment;
	protected JLabel lblDeatilsIcnDepartment;
	protected JLabel lblDetailsValueDepartment;
	protected Component hGlueDepartment;
	protected Component hStrutBusinessAddress;
	protected JLabel lblDetailsBusinessAddress;
	protected JLabel lblDetailsIcnBusinessAddress;
	protected JLabel lblDetailsValueBusinessAdr1;
	protected Component hGlueBusinessAddress;
	protected Component hStrutBusiness;
	protected Component hStrutManager;
	protected JLabel lblDetailsManager;
	protected JLabel lblDetailsIcnManager;
	protected JLabel lblDetailsValueManager;
	protected Component hGlueManager;
	protected Component hStrutTeam;
	protected JLabel lblDetailsTeam;
	protected JLabel lblDetailsIcnTeam;
	protected JLabel lblDetailsValueTeam;
	protected Component horizontalGlue;
	protected JPanel panelBusinessAdrFields;
	protected JLabel lblDetailsValueBusinessAdr2;
	protected JLabel lblDetailsValueBusinessAdr3;
	protected Component hGlueBtnAddToRoster2;
    protected JPanel panelDetailsConfChatInvite;
    protected JPanel panelDetailsCompany;
    protected JPanel panelDetailsTwitter;
    protected JPanel panelDetailsLinkedIn;
    protected JPanel panelDetailsFacebook;
    protected JPanel panelDetailsAlertOnline;
    protected JPanel panelConfChatInvite;
    protected JSeparator separatorConfChatInvite;
    protected JPanel panelCompanyUrl;
    protected JSeparator separatorCompany;
    protected JPanel panelTwitter;
    protected JSeparator separatorTwitter;
    protected JPanel panelLinkedIn;
    protected JSeparator separatorLinkedIn;
    protected JPanel panelFacebook;
    protected JSeparator separatorFacebook;
    protected Component hStrutConfChatInvite;
    protected JLabel lblDetailsConfChatInvite;
    protected JLabel lblDetailsIcnConfChatInvite;
    protected JLabel lblDetailsValueConfChatInvite;
    protected Component hGlueConfChatInvite;
    protected Component hStrutCompanyUrl;
    protected JLabel lblDetailsCompanyUrl;
    protected JLabel lblDetailsIcnCompanyUrl;
    protected JLabel lblDetailsValueCompanyUrl;
    protected Component hGlueCompanyUrl;
    protected Component hStrutTwitter;
    protected JLabel lblDetailsTwitter;
    protected JLabel lblDetailsIcnTwitter;
    protected JLabel lblDetailsValueTwitter;
    protected Component hGlueTwitter;
    protected Component hStrutLinkedIn;
    protected JLabel lblDetailsLinkedIn;
    protected JLabel lblDetailsIcnLinkedIn;
    protected JLabel lblDetailsValueLinkedIn;
    protected Component hGlueLinkedIn;
    protected Component hStrutFacebook;
    protected JLabel lblDetailsFacebook;
    protected JLabel lblDetailsIcnFacebook;
    protected JLabel lblDetailsValueFacebook;
    protected Component hGlueFacebook;
    protected Component hStrutAlerOnline;
    protected JLabel lblDetailsAlertOnline;
    protected Component hGlueAlertOnline;
    protected JToggleButton btnAlertOnline;
    protected JPanel panelDetailsGoogle;
    protected JSeparator separatorGoogle;
    protected JPanel panelGoogle;
    protected Component hStrutGoogle;
    protected JLabel lblDetailsGoogle;
    protected JLabel lblDetailsIcnGoogle;
    protected JLabel lblDetailsValueGoogle;
    protected Component hGlueGoogle;
    protected JPanel panelDetailsXing;
    protected JPanel panelXing;
    protected JSeparator separatorXing;
    protected Component hStrutXing;
    protected JLabel lblDetailsXing;
    protected JLabel lblDetailsIcnXing;
    protected JLabel lblDetailsValueXing;
    protected Component hGlueXing;
    protected Component horizontalStrut_1;
    
    protected EzuceContactProfileField txtDetailsEmail;
    protected EzuceContactProfileField txtDetailsCell;
    protected EzuceContactProfileField txtDetailsInternal;
    protected EzuceContactProfileField txtDetailsDid;
    protected EzuceContactProfileField txtDetailsTwitter;
    protected EzuceContactProfileField txtDetailsLinkedIn;
    protected EzuceContactProfileField txtDetailsFacebook;
    protected EzuceContactProfileField txtDetailsGoogle;
    protected EzuceContactProfileField txtDetailsXing;
    protected JPanel panelSaveButtons;
    protected JButton btnCancel;
    protected JButton btnSave;
    protected JPanel panelBtns;
    protected Component hStrutSave;
    protected Component hStrutCancel;
    protected boolean selfProfile;
    protected String CONTACT_LPANE_NAME="contactLayeredPane";
    protected String BUSINESS_LPANE_NAME="businessLayeredPane";
        
    protected JToggleButton btnEnterEditMode;
    
	protected final MouseListener inviteToBridgeLblListener = new MouseAdapter() {

		ContactActions actions = new ContactActions(userAvatarPanel, EzuceContactProfile.this);
		javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(ContactActions.class, actions);
		
        @Override
        public void mouseClicked(MouseEvent e) {
        	actionMap.get("inviteIntoBridgeAction").actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null){});
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ((JLabel) e.getSource()).setCursor(GraphicUtils.HAND_CURSOR);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JLabel) e.getSource()).setCursor(GraphicUtils.DEFAULT_CURSOR);
        }
    };
    
    
    protected final ChangeListener tabChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {
        	final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceContactProfile.class);
            final JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            final int index = sourceTabbedPane.getSelectedIndex();                		    	    		     
    		
    		final int nTabs = sourceTabbedPane.getTabCount();
    		
    		SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					for (int i=0; i<nTabs; i++) {
		    			JLayeredPane layeredPane = ((JLayeredPane)sourceTabbedPane.getTabComponentAt(i));
		    			if (layeredPane == null) {
		    				continue;
		    			}
		    			JLabel tabLabel = (JLabel) layeredPane.getComponentsInLayer(1)[0];
		    			if (i==index){		    				
		    				tabLabel.setIcon(selfProfile ? resourceMap.getIcon("tabItem.header.orange") : resourceMap.getIcon("tabItem.header.addressbook"));
		    				if (selfProfile && layeredPane.getName().equals(CONTACT_LPANE_NAME)){
		    					btnEnterEditMode.setVisible(true);
		    				}
		    				else{
		    					btnEnterEditMode.setVisible(false);
		    				}
		    			}else{		    				
		    				tabLabel.setIcon(resourceMap.getIcon("tabItem.header.grey"));
		    			}
		    		}					
				}
			});
    		
          }
        };
    
    protected final ActionListener saveProfileActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			saveProfile();
			btnEnterEditMode.doClick();
		}
	};
	
	protected final ActionListener cancelChangeProfileActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			cancelChangeProfile();
			btnEnterEditMode.doClick();
		}
	};
    
	
	/**
	 * Create the panel.
	 */
	public EzuceContactProfile() {			
		initComponents();		
	}
	
	public EzuceContactProfile(boolean selfProfile){
		this.selfProfile = selfProfile;
		initComponents();
		if (selfProfile){
			customizeForSelfProfile();
		}
	}
	
	public EzuceContactProfile(ContactListEntry cle){
		this.contactListEntry = cle;
		initComponents();	
	}
	
	public EzuceContactProfile(ContactListEntry cle, boolean selfProfile){
		this.selfProfile = selfProfile;
		this.contactListEntry = cle;
		initComponents();		
		if (selfProfile){
			customizeForSelfProfile();
		}
	}
	
	protected void initComponents(){
		ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceContactProfile.class);
		setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panelBackButton = new JPanel();
		panelBackButton.setBackground(new Color(121, 151, 159));
		panelBackButton.setMaximumSize(new Dimension(32767, 30));
		add(panelBackButton);
		panelBackButton.setLayout(new BoxLayout(panelBackButton, BoxLayout.X_AXIS));
		
		btnCloseProfile = new JButton("PHONEBOOK: PROFILE VIEW");
		btnCloseProfile.setFocusPainted(false);
		btnCloseProfile.setContentAreaFilled(false);
		btnCloseProfile.setBorder(null);
		btnCloseProfile.setBorderPainted(false);
		btnCloseProfile.setFont(new Font("Arial", Font.BOLD, 14));
		btnCloseProfile.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCloseProfile.setForeground(Color.WHITE);
		btnCloseProfile.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/bar_addressbook-profile.png")));
		panelBackButton.add(btnCloseProfile);
		panelAvatar = new JPanel();
		panelAvatar.setMaximumSize(new Dimension(32767, 60));
		panelAvatar.setMinimumSize(new Dimension(10, 60));
		panelAvatar.setOpaque(false);
		//panelAvatar.setLayout(new BoxLayout(panelAvatar, BoxLayout.X_AXIS));
		panelAvatar.setLayout(new BorderLayout());
		add(panelAvatar);
		
		UserMiniPanelGloss umpg = (contactListEntry==null ? new UserMiniPanelGloss() : new UserMiniPanelGloss(contactListEntry));
		
		setUserAvatarPanel(umpg);
		
		panelAddToRoster=new JPanel();
		panelAddToRoster.setOpaque(false);
		panelAddToRoster.setLayout(new BoxLayout(panelAddToRoster, BoxLayout.X_AXIS));
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setPreferredSize(new Dimension(5, 50));
		horizontalStrut_1.setMinimumSize(new Dimension(5, 0));
		horizontalStrut_1.setMaximumSize(new Dimension(20, 60));
		panelAddToRoster.add(horizontalStrut_1);
		
		btnAddUserToRoster = new JButton("");
		btnAddUserToRoster.setRolloverIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/button_add-to-roster_on.png")));
		btnAddUserToRoster.setFocusPainted(false);
		btnAddUserToRoster.setDoubleBuffered(true);
		btnAddUserToRoster.setContentAreaFilled(false);
		btnAddUserToRoster.setBorderPainted(false);
		btnAddUserToRoster.setBorder(null);
		btnAddUserToRoster.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/button_add-to-roster_off.png")));
		panelAddToRoster.add(btnAddUserToRoster);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setMinimumSize(new Dimension(5, 0));
		horizontalStrut.setMaximumSize(new Dimension(20, 60));
		horizontalStrut.setPreferredSize(new Dimension(5, 50));
		panelAddToRoster.add(horizontalStrut);
		
		verticalStrut = Box.createVerticalStrut(10);
		verticalStrut.setMinimumSize(new Dimension(0,10));
		verticalStrut.setMaximumSize(new Dimension(32000,10));
		
		panelAvatar.add(verticalStrut, BorderLayout.NORTH);
		
		panelAvatar.add(panelAddToRoster, BorderLayout.CENTER);
		
		hGlueBtnAddToRoster2 = Box.createHorizontalGlue();
		panelAddToRoster.add(hGlueBtnAddToRoster2);
		
		panelContactDetails = new JPanel();
		panelContactDetails.setOpaque(false);
		panelContactDetails.setBorder(null);
		add(panelContactDetails);
		panelContactDetails.setLayout(new BoxLayout(panelContactDetails, BoxLayout.X_AXIS));
		
		JTabbedPane profileTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		profileTabbedPane.setOpaque(true);		
		profileTabbedPane.setBackground(Color.WHITE);		
		if (!Beans.isDesignTime()){
			profileTabbedPane.setUI(new ContactProfileTabbedPaneUI());
		}
		profileTabbedPane.addChangeListener(tabChangeListener);
		panelContactDetails.add(profileTabbedPane);
		
		createContactPane(profileTabbedPane);
		
		createBusinessPane(profileTabbedPane);
		
		panelSaveButtons = new JPanel();
		panelSaveButtons.setOpaque(false);
		add(panelSaveButtons);
		panelSaveButtons.setLayout(new BorderLayout(0, 0));
		
		panelBtns = new JPanel();
		panelBtns.setOpaque(false);
		panelSaveButtons.add(panelBtns, BorderLayout.EAST);
		panelBtns.setLayout(new BoxLayout(panelBtns, BoxLayout.X_AXIS));
		
		btnCancel = new JButton(resourceMap.getString("btn.cancel"));
		panelBtns.add(btnCancel);
		btnCancel.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCancel.setFocusPainted(false);
		btnCancel.setContentAreaFilled(false);
		btnCancel.setBorder(null);
		btnCancel.setBorderPainted(false);
		btnCancel.setDoubleBuffered(true);
		btnCancel.setRolloverIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/text-button_blue_95x36.png")));
		btnCancel.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/text-button_white_95x36.png")));
		btnCancel.addActionListener(cancelChangeProfileActionListener);
		
		hStrutCancel = Box.createHorizontalStrut(20);
		hStrutCancel.setMaximumSize(new Dimension(10, 32767));
		hStrutCancel.setMinimumSize(new Dimension(10, 0));
		hStrutCancel.setPreferredSize(new Dimension(10, 0));
		panelBtns.add(hStrutCancel);
		
		btnSave = new JButton(resourceMap.getString("btn.save"));
		panelBtns.add(btnSave);
		btnSave.setHorizontalTextPosition(SwingConstants.CENTER);
		btnSave.setRolloverIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/text-button_blue_95x36.png")));
		btnSave.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/text-button_white_95x36.png")));
		btnSave.setFocusPainted(false);
		btnSave.setDoubleBuffered(true);
		btnSave.setContentAreaFilled(false);
		btnSave.setBorderPainted(false);
		btnSave.setBorder(null);
		btnSave.addActionListener(saveProfileActionListener);
		
		hStrutSave = Box.createHorizontalStrut(20);
		panelBtns.add(hStrutSave);
		
		panelSaveButtons.setVisible(false);
	}
	
	
	protected void customizeForSelfProfile(){
		
	}
	
	
	protected void createContactPane(JTabbedPane tabbedPane){
		
		ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceContactProfile.class);
		
		lblContactTabHeader = new JLabel(resourceMap.getString("contact.tabItem.header")+"	");
		Icon i = selfProfile ? resourceMap.getIcon("tabItem.header.orange") : resourceMap.getIcon("tabItem.header.addressbook");
		lblContactTabHeader.setIcon(i); 
		lblContactTabHeader.setIconTextGap(0);
		lblContactTabHeader.setFont(LBL_VALUE_FONT);
		lblContactTabHeader.setForeground(Color.GRAY);
		lblContactTabHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblContactTabHeader.setBounds( 0, 0, i.getIconWidth(), i.getIconHeight());
		
		btnEnterEditMode = new JToggleButton();		 
        btnEnterEditMode.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/edit-profil-off.png")));
        btnEnterEditMode.setSelectedIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/edit-profil-on.png")));
        btnEnterEditMode.setFocusPainted(false);
        btnEnterEditMode.setBorder(null);
        btnEnterEditMode.setBorderPainted(false);
        btnEnterEditMode.setDoubleBuffered(true); 
        btnEnterEditMode.setContentAreaFilled(false);
        btnEnterEditMode.setBounds( 113, 1,  40, 29);
        btnEnterEditMode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				if (btnEnterEditMode.isSelected()){
					goEditMode(true);
				}else
				{
					goEditMode(false);
				}
				
				
			}
		});
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setName(CONTACT_LPANE_NAME);
        layeredPane.setPreferredSize(new Dimension(i.getIconWidth(), i.getIconHeight()));
                           
        layeredPane.add(lblContactTabHeader, new Integer(1));
        if (selfProfile){
        	layeredPane.add(btnEnterEditMode,new Integer(100));
        }
        
		panelContact = new JPanel();
		
		tabbedPane.addTab(resourceMap.getString("contact.tabItem.header"), null, panelContact, null);
		
		if (!Beans.isDesignTime()){			
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, layeredPane);
		}
		
		panelContact.setLayout(new BoxLayout(panelContact, BoxLayout.Y_AXIS));
		
		panelDetailsInternal = new JPanel();
		panelContact.add(panelDetailsInternal);
		panelDetailsInternal.setLayout(new BoxLayout(panelDetailsInternal, BoxLayout.Y_AXIS));
		
		panelInternal = new JPanel();
		panelDetailsInternal.add(panelInternal);
		panelInternal.setLayout(new BoxLayout(panelInternal, BoxLayout.X_AXIS));
		
		hStrutInternal = Box.createHorizontalStrut(20);
		hStrutInternal.setPreferredSize(new Dimension(20, 40));
		hStrutInternal.setMaximumSize(new Dimension(20, 40));
		panelInternal.add(hStrutInternal);
		
		lblDetailsInternal = new JLabel(resourceMap.getString("details.internal.label"));
		lblDetailsInternal.setMaximumSize(new Dimension(130, 14));
		panelInternal.add(lblDetailsInternal);
		lblDetailsInternal.setPreferredSize(new Dimension(130, 14));
		lblDetailsInternal.setDoubleBuffered(true);
		lblDetailsInternal.setFont(LBL_FONT);
		lblDetailsInternal.setForeground(Color.GRAY);
		
		lblDetailsIcnInternal = new JLabel("    ");
		panelInternal.add(lblDetailsIcnInternal);
		lblDetailsIcnInternal.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnInternal.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnInternal.setDoubleBuffered(true);
		lblDetailsIcnInternal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnInternal.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_phone.png")));
		
		lblDetailsValueInternal = new JLabel(resourceMap.getString("details.internal.defaultvalue"));
		panelInternal.add(lblDetailsValueInternal);
		lblDetailsValueInternal.setDoubleBuffered(true);
		lblDetailsValueInternal.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueInternal.setFont(LBL_VALUE_FONT);
		
		txtDetailsInternal = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_phone.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsInternal.setFont(LBL_VALUE_FONT);
		txtDetailsInternal.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsInternal.setVisible(false);
		panelInternal.add(txtDetailsInternal);
		
		hGlueInternal = Box.createHorizontalGlue();
		panelInternal.add(hGlueInternal);
		
		separatorDetailsInternal = new JSeparator();
		separatorDetailsInternal.setMaximumSize(new Dimension(32767, 2));
		panelDetailsInternal.add(separatorDetailsInternal);
		
		panelDetailsOfficeDid = new JPanel();
		panelContact.add(panelDetailsOfficeDid);
		panelDetailsOfficeDid.setLayout(new BoxLayout(panelDetailsOfficeDid, BoxLayout.Y_AXIS));
		
		panelOfficeDid = new JPanel();
		panelDetailsOfficeDid.add(panelOfficeDid);
		panelOfficeDid.setLayout(new BoxLayout(panelOfficeDid, BoxLayout.X_AXIS));
		
		hStrutOfficeDid = Box.createHorizontalStrut(20);
		hStrutOfficeDid.setPreferredSize(new Dimension(20, 40));
		hStrutOfficeDid.setMaximumSize(new Dimension(20, 40));
		panelOfficeDid.add(hStrutOfficeDid);
		
		lblDetailsOffideDid = new JLabel(resourceMap.getString("details.officedid.label"));
		lblDetailsOffideDid.setForeground(Color.GRAY);
		lblDetailsOffideDid.setFont(LBL_FONT);
		lblDetailsOffideDid.setMaximumSize(new Dimension(130, 14));
		lblDetailsOffideDid.setPreferredSize(new Dimension(130, 14));
		lblDetailsOffideDid.setDoubleBuffered(true);
		panelOfficeDid.add(lblDetailsOffideDid);
		
		lblDetailsIcnOfficeDid = new JLabel("    ");
		lblDetailsIcnOfficeDid.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnOfficeDid.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnOfficeDid.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnOfficeDid.setDoubleBuffered(true);
		lblDetailsIcnOfficeDid.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_phone.png")));
		panelOfficeDid.add(lblDetailsIcnOfficeDid);
		
		lblDetailsValueOfficeDid = new JLabel(resourceMap.getString("details.officedid.defaultvalue"));
		lblDetailsValueOfficeDid.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueOfficeDid.setFont(LBL_VALUE_FONT);
		lblDetailsValueOfficeDid.setDoubleBuffered(true);
		panelOfficeDid.add(lblDetailsValueOfficeDid);
		
		txtDetailsDid = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_phone.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
																							,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsDid.setFont(LBL_VALUE_FONT);
		txtDetailsDid.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsDid.setVisible(false);
		panelOfficeDid.add(txtDetailsDid);
		
		hGlueOfficeDid = Box.createHorizontalGlue();
		panelOfficeDid.add(hGlueOfficeDid);
		
		separatorOfficeDid = new JSeparator();
		separatorOfficeDid.setMaximumSize(new Dimension(32767, 2));
		panelDetailsOfficeDid.add(separatorOfficeDid);
		
		panelDetailsCell = new JPanel();
		panelContact.add(panelDetailsCell);
		panelDetailsCell.setLayout(new BoxLayout(panelDetailsCell, BoxLayout.Y_AXIS));
		
		panelCell = new JPanel();
		panelDetailsCell.add(panelCell);
		panelCell.setLayout(new BoxLayout(panelCell, BoxLayout.X_AXIS));
		
		hStrutCell = Box.createHorizontalStrut(20);
		hStrutCell.setPreferredSize(new Dimension(20, 40));
		hStrutCell.setMaximumSize(new Dimension(20, 40));
		panelCell.add(hStrutCell);
		
		lblDetailsCell = new JLabel(resourceMap.getString("details.cell.label"));
		lblDetailsCell.setPreferredSize(new Dimension(130, 14));
		lblDetailsCell.setMaximumSize(new Dimension(130, 14));
		lblDetailsCell.setForeground(Color.GRAY);
		lblDetailsCell.setFont(LBL_FONT);
		panelCell.add(lblDetailsCell);
		
		lblDetailsIcnCell = new JLabel("   ");
		lblDetailsIcnCell.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnCell.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnCell.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnCell.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_phone.png")));
		panelCell.add(lblDetailsIcnCell);
		
		lblDetailsValueCell = new JLabel(resourceMap.getString("details.cell.defaultvalue"));
		lblDetailsValueCell.setFont(LBL_VALUE_FONT);
		lblDetailsValueCell.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		panelCell.add(lblDetailsValueCell);
		
		txtDetailsCell = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_phone.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsCell.setFont(LBL_VALUE_FONT);
		txtDetailsCell.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsCell.setVisible(false);
		panelCell.add(txtDetailsCell);
		
		hGlueCell = Box.createHorizontalGlue();
		panelCell.add(hGlueCell);
		
		separatorCell = new JSeparator();
		separatorCell.setMaximumSize(new Dimension(32767, 2));
		panelDetailsCell.add(separatorCell);
		
		panelDetailsEmail = new JPanel();
		panelContact.add(panelDetailsEmail);
		panelDetailsEmail.setLayout(new BoxLayout(panelDetailsEmail, BoxLayout.Y_AXIS));
		
		panelField = new JPanel();
		panelDetailsEmail.add(panelField);
		panelField.setLayout(new BoxLayout(panelField, BoxLayout.X_AXIS));
		
		hStrutEmail = Box.createHorizontalStrut(20);
		hStrutEmail.setPreferredSize(new Dimension(20, 40));
		hStrutEmail.setMaximumSize(new Dimension(20, 40));
		panelField.add(hStrutEmail);
		
		lblDetailsEmail = new JLabel(resourceMap.getString("details.email.label"));
		lblDetailsEmail.setPreferredSize(new Dimension(130, 14));
		lblDetailsEmail.setMaximumSize(new Dimension(130, 14));
		lblDetailsEmail.setForeground(Color.GRAY);
		lblDetailsEmail.setFont(LBL_FONT);
		panelField.add(lblDetailsEmail);
		
		lblDetailsIcnEmail = new JLabel("    ");
		lblDetailsIcnEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnEmail.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnEmail.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnEmail.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_email.png")));
		panelField.add(lblDetailsIcnEmail);
		
		lblDetailsValueEmail = new JLabel(resourceMap.getString("details.email.defaultvalue"));
		lblDetailsValueEmail.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueEmail.setFont(LBL_VALUE_FONT);
		panelField.add(lblDetailsValueEmail);
		
		txtDetailsEmail = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_email.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsEmail.setFont(LBL_VALUE_FONT);
		txtDetailsEmail.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsEmail.setVisible(false);
		panelField.add(txtDetailsEmail);
		
		hGlueEmail = Box.createHorizontalGlue();
		panelField.add(hGlueEmail);
		
		separatorEmail = new JSeparator();
		separatorEmail.setMaximumSize(new Dimension(32767, 2));
		panelDetailsEmail.add(separatorEmail);
		
				
		panelDetailsCorpIm = new JPanel();
		panelContact.add(panelDetailsCorpIm);
		panelDetailsCorpIm.setLayout(new BoxLayout(panelDetailsCorpIm, BoxLayout.Y_AXIS));
		
		panelCorpIm = new JPanel();
		panelDetailsCorpIm.add(panelCorpIm);
		panelCorpIm.setLayout(new BoxLayout(panelCorpIm, BoxLayout.X_AXIS));
		
		hSrutCorpIm = Box.createHorizontalStrut(20);
		hSrutCorpIm.setPreferredSize(new Dimension(20, 40));
		hSrutCorpIm.setMaximumSize(new Dimension(20, 40));
		panelCorpIm.add(hSrutCorpIm);
		
		lblDetailsCorpIm = new JLabel(resourceMap.getString("details.corporateim.label"));
		lblDetailsCorpIm.setPreferredSize(new Dimension(130, 14));
		lblDetailsCorpIm.setMaximumSize(new Dimension(130, 14));
		lblDetailsCorpIm.setFont(LBL_FONT);
		lblDetailsCorpIm.setForeground(Color.GRAY);
		panelCorpIm.add(lblDetailsCorpIm);
		
		lblDetailsIcnCorpIm = new JLabel("    ");
		lblDetailsIcnCorpIm.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnCorpIm.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnCorpIm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnCorpIm.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_chat.png")));
		panelCorpIm.add(lblDetailsIcnCorpIm);
		
		lblDetailsValueCorpIm = new JLabel(resourceMap.getString("details.corporateim.defaultvalue"));
		lblDetailsValueCorpIm.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueCorpIm.setFont(LBL_VALUE_FONT);
		panelCorpIm.add(lblDetailsValueCorpIm);			
		
		hGlueCorpIm = Box.createHorizontalGlue();
		panelCorpIm.add(hGlueCorpIm);
		
		separatorCorpIm = new JSeparator();
		separatorCorpIm.setMaximumSize(new Dimension(32767, 2));
		panelDetailsCorpIm.add(separatorCorpIm);
		
		panelDetailsConfBridge = new JPanel();
		panelContact.add(panelDetailsConfBridge);
		panelDetailsConfBridge.setLayout(new BoxLayout(panelDetailsConfBridge, BoxLayout.Y_AXIS));
		
		panelConfBridge = new JPanel();
		panelDetailsConfBridge.add(panelConfBridge);
		panelConfBridge.setLayout(new BoxLayout(panelConfBridge, BoxLayout.X_AXIS));
		
		hStrutConfBridge = Box.createHorizontalStrut(20);
		hStrutConfBridge.setPreferredSize(new Dimension(20, 40));
		hStrutConfBridge.setMaximumSize(new Dimension(20, 40));
		panelConfBridge.add(hStrutConfBridge);
		
		lblDetailsConfBridge = new JLabel(resourceMap.getString("details.confbridge.label"));
		lblDetailsConfBridge.setMaximumSize(new Dimension(130, 14));
		lblDetailsConfBridge.setPreferredSize(new Dimension(130, 14));
		lblDetailsConfBridge.setFont(LBL_FONT);
		lblDetailsConfBridge.setForeground(Color.GRAY);
		panelConfBridge.add(lblDetailsConfBridge);
		
		lblDetailsIcnConfBridge = new JLabel("    ");
		lblDetailsIcnConfBridge.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnConfBridge.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnConfBridge.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnConfBridge.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_conference.png")));
		panelConfBridge.add(lblDetailsIcnConfBridge);
		
		lblDetailsValueConfBridge = new JLabel(resourceMap.getString("details.confbridge.defaultvalue"));
		lblDetailsValueConfBridge.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueConfBridge.setFont(LBL_VALUE_FONT);
		panelConfBridge.add(lblDetailsValueConfBridge);
		
		hGlueConfBridge = Box.createHorizontalGlue();
		panelConfBridge.add(hGlueConfBridge);
		
		separatorConfBridge = new JSeparator();
		separatorConfBridge.setMaximumSize(new Dimension(32767, 2));
		panelDetailsConfBridge.add(separatorConfBridge);
		
		panelDetailsConfBrInvite = new JPanel();
		panelContact.add(panelDetailsConfBrInvite);
		panelDetailsConfBrInvite.setLayout(new BoxLayout(panelDetailsConfBrInvite, BoxLayout.Y_AXIS));
		
		panelConfBrInvite = new JPanel();
		panelDetailsConfBrInvite.add(panelConfBrInvite);
		panelConfBrInvite.setLayout(new BoxLayout(panelConfBrInvite, BoxLayout.X_AXIS));
		
		hStrutConfBrInvite = Box.createHorizontalStrut(20);
		hStrutConfBrInvite.setPreferredSize(new Dimension(20, 40));
		hStrutConfBrInvite.setMaximumSize(new Dimension(20, 40));
		panelConfBrInvite.add(hStrutConfBrInvite);
		
		lblDetailsConfBrInvite = new JLabel("    ");
		lblDetailsConfBrInvite.setPreferredSize(new Dimension(130, 14));
		lblDetailsConfBrInvite.setMaximumSize(new Dimension(130, 14));
		panelConfBrInvite.add(lblDetailsConfBrInvite);
		
		lblDetailsIcnConfBrInvite = new JLabel("    ");
		lblDetailsIcnConfBrInvite.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnConfBrInvite.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnConfBrInvite.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnConfBrInvite.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_conference.png")));
		panelConfBrInvite.add(lblDetailsIcnConfBrInvite);
		
		lblDetailsValueConfBrInvite = new JLabel(resourceMap.getString("details.confbridge.invite"));
		lblDetailsValueConfBrInvite.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueConfBrInvite.setFont(LBL_VALUE_FONT);
		panelConfBrInvite.add(lblDetailsValueConfBrInvite);
		
		hGlueConfBrInvite = Box.createHorizontalGlue();
		panelConfBrInvite.add(hGlueConfBrInvite);
		
		separatorConfBrInvite = new JSeparator();
		separatorConfBrInvite.setMaximumSize(new Dimension(32767, 2));
		panelDetailsConfBrInvite.add(separatorConfBrInvite);
		
		panelDetailsConfChat = new JPanel();
		panelContact.add(panelDetailsConfChat);
		panelDetailsConfChat.setLayout(new BoxLayout(panelDetailsConfChat, BoxLayout.Y_AXIS));
		
		panelConfChat = new JPanel();
		panelDetailsConfChat.add(panelConfChat);
		panelConfChat.setLayout(new BoxLayout(panelConfChat, BoxLayout.X_AXIS));
		
		hStrutConfChat = Box.createHorizontalStrut(20);
		hStrutConfChat.setPreferredSize(new Dimension(20, 40));
		hStrutConfChat.setMaximumSize(new Dimension(20, 40));
		panelConfChat.add(hStrutConfChat);
		
		lblDetailsConfChat = new JLabel(resourceMap.getString("details.confchat.label"));
		lblDetailsConfChat.setPreferredSize(new Dimension(130, 14));
		lblDetailsConfChat.setMaximumSize(new Dimension(130, 14));
		lblDetailsConfChat.setFont(LBL_FONT);
		lblDetailsConfChat.setForeground(Color.GRAY);
		panelConfChat.add(lblDetailsConfChat);
		
		lblDetailsIcnConfChat = new JLabel("    ");
		lblDetailsIcnConfChat.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnConfChat.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnConfChat.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnConfChat.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_group-chat.png")));
		panelConfChat.add(lblDetailsIcnConfChat);
		
		lblDetailsValueConfChat = new JLabel(resourceMap.getString("details.confchat.defaultvalue"));
		lblDetailsValueConfChat.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueConfChat.setFont(LBL_VALUE_FONT);
		panelConfChat.add(lblDetailsValueConfChat);
		
		hGlueConfChat = Box.createHorizontalGlue();
		panelConfChat.add(hGlueConfChat);
		
		separatorConfChat = new JSeparator();
		separatorConfChat.setMaximumSize(new Dimension(32767, 2));
		panelDetailsConfChat.add(separatorConfChat);
		
		panelDetailsConfChatInvite = new JPanel();
		panelContact.add(panelDetailsConfChatInvite);
		panelDetailsConfChatInvite.setLayout(new BoxLayout(panelDetailsConfChatInvite, BoxLayout.Y_AXIS));
		
		panelConfChatInvite = new JPanel();
		panelDetailsConfChatInvite.add(panelConfChatInvite);
		panelConfChatInvite.setLayout(new BoxLayout(panelConfChatInvite, BoxLayout.X_AXIS));
		
		hStrutConfChatInvite = Box.createHorizontalStrut(20);
		hStrutConfChatInvite.setPreferredSize(new Dimension(20, 40));
		hStrutConfChatInvite.setMaximumSize(new Dimension(20, 40));
		panelConfChatInvite.add(hStrutConfChatInvite);
		
		lblDetailsConfChatInvite = new JLabel("");
		lblDetailsConfChatInvite.setPreferredSize(new Dimension(130, 14));
		lblDetailsConfChatInvite.setMaximumSize(new Dimension(130, 14));
		lblDetailsConfChatInvite.setForeground(Color.GRAY);
		lblDetailsConfChatInvite.setFont(new Font("Arial", Font.BOLD, 10));
		panelConfChatInvite.add(lblDetailsConfChatInvite);
		
		lblDetailsIcnConfChatInvite = new JLabel("    ");
		lblDetailsIcnConfChatInvite.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnConfChatInvite.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnConfChatInvite.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcnConfChatInvite.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_group-chat.png")));
		panelConfChatInvite.add(lblDetailsIcnConfChatInvite);
		
		lblDetailsValueConfChatInvite = new JLabel(resourceMap.getString("details.confchat.invite"));
		lblDetailsValueConfChatInvite.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueConfChatInvite.setFont(LBL_VALUE_FONT);
		panelConfChatInvite.add(lblDetailsValueConfChatInvite);
		
		hGlueConfChatInvite = Box.createHorizontalGlue();
		panelConfChatInvite.add(hGlueConfChatInvite);
		
		separatorConfChatInvite = new JSeparator();
		separatorConfChatInvite.setMaximumSize(new Dimension(32767, 2));
		panelDetailsConfChatInvite.add(separatorConfChatInvite);
		
		panelDetailsCompany = new JPanel();
		panelContact.add(panelDetailsCompany);
		panelDetailsCompany.setLayout(new BoxLayout(panelDetailsCompany, BoxLayout.Y_AXIS));
		
		panelCompanyUrl = new JPanel();
		panelDetailsCompany.add(panelCompanyUrl);
		panelCompanyUrl.setLayout(new BoxLayout(panelCompanyUrl, BoxLayout.X_AXIS));
		
		hStrutCompanyUrl = Box.createHorizontalStrut(20);
		hStrutCompanyUrl.setPreferredSize(new Dimension(20, 40));
		hStrutCompanyUrl.setMaximumSize(new Dimension(20, 40));
		panelCompanyUrl.add(hStrutCompanyUrl);
		
		lblDetailsCompanyUrl = new JLabel(resourceMap.getString("details.companyurl.label"));
		lblDetailsCompanyUrl.setPreferredSize(new Dimension(130, 14));
		lblDetailsCompanyUrl.setMaximumSize(new Dimension(130, 14));
		lblDetailsCompanyUrl.setForeground(Color.GRAY);
		lblDetailsCompanyUrl.setFont(LBL_FONT);
		panelCompanyUrl.add(lblDetailsCompanyUrl);
		
		lblDetailsIcnCompanyUrl = new JLabel("    ");
		lblDetailsIcnCompanyUrl.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_website.png")));
		lblDetailsIcnCompanyUrl.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnCompanyUrl.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnCompanyUrl.setHorizontalAlignment(SwingConstants.RIGHT);
		panelCompanyUrl.add(lblDetailsIcnCompanyUrl);
		
		lblDetailsValueCompanyUrl = new JLabel(resourceMap.getString("details.companyurl.defaultvalue"));
		lblDetailsValueCompanyUrl.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueCompanyUrl.setFont(LBL_VALUE_FONT);
		panelCompanyUrl.add(lblDetailsValueCompanyUrl);
		
		hGlueCompanyUrl = Box.createHorizontalGlue();
		panelCompanyUrl.add(hGlueCompanyUrl);
		
		separatorCompany = new JSeparator();
		separatorCompany.setMaximumSize(new Dimension(32767, 2));
		panelDetailsCompany.add(separatorCompany);
		
		panelDetailsTwitter = new JPanel();
		panelContact.add(panelDetailsTwitter);
		panelDetailsTwitter.setLayout(new BoxLayout(panelDetailsTwitter, BoxLayout.Y_AXIS));
		
		panelTwitter = new JPanel();
		panelDetailsTwitter.add(panelTwitter);
		panelTwitter.setLayout(new BoxLayout(panelTwitter, BoxLayout.X_AXIS));
		
		hStrutTwitter = Box.createHorizontalStrut(20);
		hStrutTwitter.setPreferredSize(new Dimension(20, 40));
		hStrutTwitter.setMaximumSize(new Dimension(20, 40));
		panelTwitter.add(hStrutTwitter);
		
		lblDetailsTwitter = new JLabel(resourceMap.getString("details.twitter.label"));
		lblDetailsTwitter.setPreferredSize(new Dimension(130, 14));
		lblDetailsTwitter.setMaximumSize(new Dimension(130, 14));
		lblDetailsTwitter.setForeground(Color.GRAY);
		lblDetailsTwitter.setFont(LBL_FONT);
		panelTwitter.add(lblDetailsTwitter);
		
		lblDetailsIcnTwitter = new JLabel("    ");
		lblDetailsIcnTwitter.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_twitter.png")));
		lblDetailsIcnTwitter.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnTwitter.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnTwitter.setHorizontalAlignment(SwingConstants.RIGHT);
		panelTwitter.add(lblDetailsIcnTwitter);
		
		lblDetailsValueTwitter = new JLabel(resourceMap.getString("details.twitter.defaultvalue"));
		lblDetailsValueTwitter.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueTwitter.setFont(LBL_VALUE_FONT);
		panelTwitter.add(lblDetailsValueTwitter);
		
		
		txtDetailsTwitter = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_twitter.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
															,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsTwitter.setFont(LBL_VALUE_FONT);
		txtDetailsTwitter.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsTwitter.setVisible(false);
		panelTwitter.add(txtDetailsTwitter);
		
		hGlueTwitter = Box.createHorizontalGlue();
		panelTwitter.add(hGlueTwitter);
		
		separatorTwitter = new JSeparator();
		separatorTwitter.setMaximumSize(new Dimension(32767, 2));
		panelDetailsTwitter.add(separatorTwitter);
		
		panelDetailsLinkedIn = new JPanel();
		panelContact.add(panelDetailsLinkedIn);
		panelDetailsLinkedIn.setLayout(new BoxLayout(panelDetailsLinkedIn, BoxLayout.Y_AXIS));
		
		panelLinkedIn = new JPanel();
		panelDetailsLinkedIn.add(panelLinkedIn);
		panelLinkedIn.setLayout(new BoxLayout(panelLinkedIn, BoxLayout.X_AXIS));
		
		hStrutLinkedIn = Box.createHorizontalStrut(20);
		hStrutLinkedIn.setPreferredSize(new Dimension(20, 40));
		hStrutLinkedIn.setMaximumSize(new Dimension(20, 40));
		panelLinkedIn.add(hStrutLinkedIn);
		
		lblDetailsLinkedIn = new JLabel(resourceMap.getString("details.linkedin.label"));
		lblDetailsLinkedIn.setPreferredSize(new Dimension(130, 14));
		lblDetailsLinkedIn.setMaximumSize(new Dimension(130, 14));
		lblDetailsLinkedIn.setForeground(Color.GRAY);
		lblDetailsLinkedIn.setFont(LBL_FONT);
		panelLinkedIn.add(lblDetailsLinkedIn);
		
		lblDetailsIcnLinkedIn = new JLabel("    ");
		lblDetailsIcnLinkedIn.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_linkedin.png")));
		lblDetailsIcnLinkedIn.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnLinkedIn.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnLinkedIn.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLinkedIn.add(lblDetailsIcnLinkedIn);
		
		lblDetailsValueLinkedIn = new JLabel(resourceMap.getString("details.linkedin.defaultvalue"));
		lblDetailsValueLinkedIn.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueLinkedIn.setFont(LBL_VALUE_FONT);
		panelLinkedIn.add(lblDetailsValueLinkedIn);
		
		txtDetailsLinkedIn = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_linkedin.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
															,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsLinkedIn.setFont(LBL_VALUE_FONT);
		txtDetailsLinkedIn.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsLinkedIn.setVisible(false);
		panelLinkedIn.add(txtDetailsLinkedIn);
		
		hGlueLinkedIn = Box.createHorizontalGlue();
		panelLinkedIn.add(hGlueLinkedIn);
		
		separatorLinkedIn = new JSeparator();
		separatorLinkedIn.setMaximumSize(new Dimension(32767, 2));
		panelDetailsLinkedIn.add(separatorLinkedIn);
		
		panelDetailsFacebook = new JPanel();
		panelContact.add(panelDetailsFacebook);
		panelDetailsFacebook.setLayout(new BoxLayout(panelDetailsFacebook, BoxLayout.Y_AXIS));
		
		panelFacebook = new JPanel();
		panelDetailsFacebook.add(panelFacebook);
		panelFacebook.setLayout(new BoxLayout(panelFacebook, BoxLayout.X_AXIS));
		
		hStrutFacebook = Box.createHorizontalStrut(20);
		hStrutFacebook.setPreferredSize(new Dimension(20, 40));
		hStrutFacebook.setMaximumSize(new Dimension(20, 40));
		panelFacebook.add(hStrutFacebook);
		
		lblDetailsFacebook = new JLabel(resourceMap.getString("details.facebook.label"));
		lblDetailsFacebook.setPreferredSize(new Dimension(130, 14));
		lblDetailsFacebook.setMaximumSize(new Dimension(130, 14));
		lblDetailsFacebook.setForeground(Color.GRAY);
		lblDetailsFacebook.setFont(LBL_FONT);
		panelFacebook.add(lblDetailsFacebook);
		
		lblDetailsIcnFacebook = new JLabel("    ");
		lblDetailsIcnFacebook.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_facebook.png")));
		lblDetailsIcnFacebook.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnFacebook.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnFacebook.setHorizontalAlignment(SwingConstants.RIGHT);
		panelFacebook.add(lblDetailsIcnFacebook);
		
		lblDetailsValueFacebook = new JLabel(resourceMap.getString("details.facebook.defaultvalue"));
		lblDetailsValueFacebook.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueFacebook.setFont(LBL_VALUE_FONT);
		panelFacebook.add(lblDetailsValueFacebook);
		
		
		txtDetailsFacebook = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_facebook.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
															,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsFacebook.setFont(LBL_VALUE_FONT);
		txtDetailsFacebook.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsFacebook.setVisible(false);
		panelFacebook.add(txtDetailsFacebook);
		
		hGlueFacebook = Box.createHorizontalGlue();
		panelFacebook.add(hGlueFacebook);
		
		separatorFacebook = new JSeparator();
		separatorFacebook.setMaximumSize(new Dimension(32767, 2));
		panelDetailsFacebook.add(separatorFacebook);
		
		panelDetailsGoogle = new JPanel();
		panelContact.add(panelDetailsGoogle);
		panelDetailsGoogle.setLayout(new BoxLayout(panelDetailsGoogle, BoxLayout.Y_AXIS));
		
		panelGoogle = new JPanel();
		panelDetailsGoogle.add(panelGoogle);
		panelGoogle.setLayout(new BoxLayout(panelGoogle, BoxLayout.X_AXIS));
		
		hStrutGoogle = Box.createHorizontalStrut(20);
		hStrutGoogle.setPreferredSize(new Dimension(20, 40));
		hStrutGoogle.setMaximumSize(new Dimension(20, 40));
		panelGoogle.add(hStrutGoogle);
		
		lblDetailsGoogle = new JLabel(resourceMap.getString("details.google.label"));
		lblDetailsGoogle.setPreferredSize(new Dimension(130, 14));
		lblDetailsGoogle.setMaximumSize(new Dimension(130, 14));
		lblDetailsGoogle.setForeground(Color.GRAY);
		lblDetailsGoogle.setFont(LBL_FONT);
		panelGoogle.add(lblDetailsGoogle);
		
		lblDetailsIcnGoogle = new JLabel("    ");
		lblDetailsIcnGoogle.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_google.png")));
		lblDetailsIcnGoogle.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnGoogle.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnGoogle.setHorizontalAlignment(SwingConstants.RIGHT);
		panelGoogle.add(lblDetailsIcnGoogle);
		
		lblDetailsValueGoogle = new JLabel(resourceMap.getString("details.google.defaultvalue"));
		lblDetailsValueGoogle.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueGoogle.setFont(LBL_VALUE_FONT);
		panelGoogle.add(lblDetailsValueGoogle);
		
		
		txtDetailsGoogle = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_google.png")),new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsGoogle.setFont(LBL_VALUE_FONT);
		txtDetailsGoogle.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsGoogle.setVisible(false);
		panelGoogle.add(txtDetailsGoogle);
		
		hGlueGoogle = Box.createHorizontalGlue();
		panelGoogle.add(hGlueGoogle);
		
		separatorGoogle = new JSeparator();
		separatorGoogle.setMaximumSize(new Dimension(32767, 2));
		panelDetailsGoogle.add(separatorGoogle);
		
		panelDetailsXing = new JPanel();
		panelContact.add(panelDetailsXing);
		panelDetailsXing.setLayout(new BoxLayout(panelDetailsXing, BoxLayout.Y_AXIS));
		
		panelXing = new JPanel();
		panelDetailsXing.add(panelXing);
		panelXing.setLayout(new BoxLayout(panelXing, BoxLayout.X_AXIS));
		
		hStrutXing = Box.createHorizontalStrut(20);
		hStrutXing.setPreferredSize(new Dimension(20, 40));
		hStrutXing.setMaximumSize(new Dimension(20, 40));
		panelXing.add(hStrutXing);
		
		lblDetailsXing = new JLabel(resourceMap.getString("details.xing.label"));
		lblDetailsXing.setPreferredSize(new Dimension(130, 14));
		lblDetailsXing.setMaximumSize(new Dimension(130, 14));
		lblDetailsXing.setForeground(Color.GRAY);
		lblDetailsXing.setFont(LBL_FONT);
		panelXing.add(lblDetailsXing);
		
		lblDetailsIcnXing = new JLabel("    ");
		lblDetailsIcnXing.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_xing.png")));
		lblDetailsIcnXing.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnXing.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnXing.setHorizontalAlignment(SwingConstants.RIGHT);
		panelXing.add(lblDetailsIcnXing);
		
		lblDetailsValueXing = new JLabel(resourceMap.getString("details.xing.defaultvalue"));
		lblDetailsValueXing.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueXing.setFont(LBL_VALUE_FONT);
		panelXing.add(lblDetailsValueXing);
		
		
		txtDetailsXing = new EzuceContactProfileField(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_xing.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text.png"))
														,new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/cancel_text_hover.png")));
		txtDetailsXing.setFont(LBL_VALUE_FONT);
		txtDetailsXing.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		txtDetailsXing.setVisible(false);
		panelXing.add(txtDetailsXing);
		
		hGlueXing = Box.createHorizontalGlue();
		panelXing.add(hGlueXing);
		
		separatorXing = new JSeparator();
		separatorXing.setMaximumSize(new Dimension(32767, 2));
		panelDetailsXing.add(separatorXing);
		
		panelDetailsAlertOnline = new JPanel();
		panelContact.add(panelDetailsAlertOnline);
		panelDetailsAlertOnline.setLayout(new BoxLayout(panelDetailsAlertOnline, BoxLayout.X_AXIS));
		
		hStrutAlerOnline = Box.createHorizontalStrut(20);
		hStrutAlerOnline.setPreferredSize(new Dimension(20, 50));
		hStrutAlerOnline.setMaximumSize(new Dimension(20, 50));
		panelDetailsAlertOnline.add(hStrutAlerOnline);
		
		lblDetailsAlertOnline = new JLabel(resourceMap.getString("details.alertonline.label"));
		lblDetailsAlertOnline.setPreferredSize(new Dimension(130, 14));
		lblDetailsAlertOnline.setMaximumSize(new Dimension(130, 14));
		lblDetailsAlertOnline.setForeground(Color.GRAY);
		lblDetailsAlertOnline.setFont(LBL_FONT);
		panelDetailsAlertOnline.add(lblDetailsAlertOnline);
		
		btnAlertOnline = new JToggleButton("");
		btnAlertOnline.setFocusPainted(false);
		btnAlertOnline.setSelectedIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/button_online-alert_on.png")));
		btnAlertOnline.setDoubleBuffered(true);
		btnAlertOnline.setContentAreaFilled(false);
		btnAlertOnline.setBorderPainted(false);
		btnAlertOnline.setBorder(null);
		btnAlertOnline.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/button_online-alert_off.png")));
		panelDetailsAlertOnline.add(btnAlertOnline);
		
		hGlueAlertOnline = Box.createHorizontalGlue();
		panelDetailsAlertOnline.add(hGlueAlertOnline);
		
		verticalGluePnlContact = Box.createVerticalGlue();
		panelContact.add(verticalGluePnlContact);
	}

	
	protected void createBusinessPane(JTabbedPane tabbedPane){
		
		ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceContactProfile.class);		
		lblBusinessTabHeader = new JLabel(resourceMap.getString("business.tabItem.header")+"	");
		Icon i = resourceMap.getIcon("tabItem.header.grey");
		lblBusinessTabHeader.setIcon(i); 
		lblBusinessTabHeader.setIconTextGap(0);
		lblBusinessTabHeader.setFont(LBL_VALUE_FONT);
		lblBusinessTabHeader.setForeground(Color.GRAY);
		lblBusinessTabHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblBusinessTabHeader.setBounds( 0, 0, i.getIconWidth(), i.getIconHeight());
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setName(BUSINESS_LPANE_NAME);
        layeredPane.setPreferredSize(new Dimension(i.getIconWidth(), i.getIconHeight()));
                           
        layeredPane.add(lblBusinessTabHeader, new Integer(1));
		
		panelBusiness = new JPanel();
		tabbedPane.addTab(resourceMap.getString("business.tabItem.header"), null, panelBusiness, null);		
		if (!Beans.isDesignTime()){
			//tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, lblBusinessTabHeader);
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, layeredPane);
		}
		
		panelBusiness.setLayout(new BoxLayout(panelBusiness, BoxLayout.Y_AXIS));
		
		hStrutBusiness = Box.createHorizontalStrut(20);
		hStrutBusiness.setPreferredSize(new Dimension(20, 10));
		hStrutBusiness.setMaximumSize(new Dimension(20, 10));
		panelBusiness.add(hStrutBusiness);
		
		panelDetailsWork = new JPanel();
		panelBusiness.add(panelDetailsWork);
		panelDetailsWork.setLayout(new BoxLayout(panelDetailsWork, BoxLayout.Y_AXIS));
		
		panelWork = new JPanel();
		panelDetailsWork.add(panelWork);
		panelWork.setLayout(new BoxLayout(panelWork, BoxLayout.Y_AXIS));
		
		panelJob = new JPanel();
		panelWork.add(panelJob);
		panelJob.setLayout(new BoxLayout(panelJob, BoxLayout.X_AXIS));
		
		hStrutJob = Box.createHorizontalStrut(20);
		hStrutJob.setPreferredSize(new Dimension(20, 20));
		hStrutJob.setMaximumSize(new Dimension(20, 20));
		panelJob.add(hStrutJob);
		
		lblDetailsJob = new JLabel(resourceMap.getString("details.jobtitle.label"));
		lblDetailsJob.setPreferredSize(new Dimension(85, 14));
		lblDetailsJob.setMaximumSize(new Dimension(85, 14));
		lblDetailsJob.setForeground(LBL_FORE_COLOR);
		lblDetailsJob.setFont(LBL_FONT);
		panelJob.add(lblDetailsJob);
		
		lblDetailsIcnJob = new JLabel("    ");
		lblDetailsIcnJob.setPreferredSize(new Dimension(50, 15));
		lblDetailsIcnJob.setMaximumSize(new Dimension(50, 15));
		lblDetailsIcnJob.setHorizontalAlignment(SwingConstants.RIGHT);
		panelJob.add(lblDetailsIcnJob);
		
		lblDetailsValueJob = new JLabel(resourceMap.getString("details.jobtitle.defaultvalue"));
		lblDetailsValueJob.setForeground(LBL_VALUE_FORE_DARK_COLOR);
		lblDetailsValueJob.setFont(LBL_VALUE_FONT);
		panelJob.add(lblDetailsValueJob);
		
		hGlueJob = Box.createHorizontalGlue();
		panelJob.add(hGlueJob);
		
		panelCompany = new JPanel();
		panelWork.add(panelCompany);
		panelCompany.setLayout(new BoxLayout(panelCompany, BoxLayout.X_AXIS));
		
		hStrutCompany = Box.createHorizontalStrut(20);
		hStrutCompany.setPreferredSize(new Dimension(20, 20));
		hStrutCompany.setMaximumSize(new Dimension(20, 20));
		panelCompany.add(hStrutCompany);
		
		lblDetailsCompany = new JLabel(resourceMap.getString("details.company.label"));
		lblDetailsCompany.setPreferredSize(new Dimension(85, 14));
		lblDetailsCompany.setMaximumSize(new Dimension(85, 14));
		lblDetailsCompany.setForeground(LBL_FORE_COLOR);
		lblDetailsCompany.setFont(LBL_FONT);
		panelCompany.add(lblDetailsCompany);
		
		lblDetailsIcnCompany = new JLabel("    ");
		lblDetailsIcnCompany.setPreferredSize(new Dimension(50, 15));
		lblDetailsIcnCompany.setMaximumSize(new Dimension(50, 15));
		lblDetailsIcnCompany.setHorizontalAlignment(SwingConstants.RIGHT);
		panelCompany.add(lblDetailsIcnCompany);
		
		lblDetailsValueCompany = new JLabel(resourceMap.getString("details.company.defaultvalue"));
		lblDetailsValueCompany.setForeground(Color.DARK_GRAY);
		lblDetailsValueCompany.setFont(LBL_VALUE_FONT);
		panelCompany.add(lblDetailsValueCompany);
		
		hGlueCompany = Box.createHorizontalGlue();
		panelCompany.add(hGlueCompany);
		
		panelDepartment = new JPanel();
		panelWork.add(panelDepartment);
		panelDepartment.setLayout(new BoxLayout(panelDepartment, BoxLayout.X_AXIS));
		
		hStrutDepartment = Box.createHorizontalStrut(20);
		hStrutDepartment.setPreferredSize(new Dimension(20, 20));
		hStrutDepartment.setMaximumSize(new Dimension(20, 20));
		panelDepartment.add(hStrutDepartment);
		
		lblDetailsDepartment = new JLabel(resourceMap.getString("details.department.label"));
		lblDetailsDepartment.setPreferredSize(new Dimension(85, 14));
		lblDetailsDepartment.setMaximumSize(new Dimension(85, 14));
		lblDetailsDepartment.setForeground(LBL_FORE_COLOR);
		lblDetailsDepartment.setFont(LBL_FONT);
		panelDepartment.add(lblDetailsDepartment);
		
		lblDeatilsIcnDepartment = new JLabel("    ");
		lblDeatilsIcnDepartment.setPreferredSize(new Dimension(50, 15));
		lblDeatilsIcnDepartment.setMaximumSize(new Dimension(50, 15));
		lblDeatilsIcnDepartment.setHorizontalAlignment(SwingConstants.RIGHT);
		panelDepartment.add(lblDeatilsIcnDepartment);
		
		lblDetailsValueDepartment = new JLabel(resourceMap.getString("details.department.defaultvalue"));
		lblDetailsValueDepartment.setForeground(Color.DARK_GRAY);
		lblDetailsValueDepartment.setFont(LBL_VALUE_FONT);
		panelDepartment.add(lblDetailsValueDepartment);
		
		hGlueDepartment = Box.createHorizontalGlue();
		panelDepartment.add(hGlueDepartment);
		
		separatorWork = new JSeparator();
		separatorWork.setMaximumSize(new Dimension(32767, 2));
		panelDetailsWork.add(separatorWork);
		
		panelDetailsBusinessAddress = new JPanel();
		panelBusiness.add(panelDetailsBusinessAddress);
		panelDetailsBusinessAddress.setLayout(new BoxLayout(panelDetailsBusinessAddress, BoxLayout.Y_AXIS));
		
		panelBusinessAddress = new JPanel();
		panelDetailsBusinessAddress.add(panelBusinessAddress);
		panelBusinessAddress.setLayout(new BoxLayout(panelBusinessAddress, BoxLayout.X_AXIS));
		
		hStrutBusinessAddress = Box.createHorizontalStrut(20);
		hStrutBusinessAddress.setPreferredSize(new Dimension(20, 40));
		hStrutBusinessAddress.setMaximumSize(new Dimension(20, 40));
		panelBusinessAddress.add(hStrutBusinessAddress);
		
		lblDetailsBusinessAddress = new JLabel(resourceMap.getString("details.address.label"));
		lblDetailsBusinessAddress.setPreferredSize(new Dimension(85, 14));
		lblDetailsBusinessAddress.setMaximumSize(new Dimension(85, 14));
		lblDetailsBusinessAddress.setForeground(LBL_FORE_COLOR);
		lblDetailsBusinessAddress.setFont(LBL_FONT);
		panelBusinessAddress.add(lblDetailsBusinessAddress);
		
		lblDetailsIcnBusinessAddress = new JLabel("    ");
		lblDetailsIcnBusinessAddress.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_address.png")));
		lblDetailsIcnBusinessAddress.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnBusinessAddress.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnBusinessAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		panelBusinessAddress.add(lblDetailsIcnBusinessAddress);
		
		panelBusinessAdrFields = new JPanel();
		panelBusinessAddress.add(panelBusinessAdrFields);
		panelBusinessAdrFields.setLayout(new BoxLayout(panelBusinessAdrFields, BoxLayout.Y_AXIS));
		
		lblDetailsValueBusinessAdr1 = new JLabel(resourceMap.getString("details.address.defaultvalue"));
		panelBusinessAdrFields.add(lblDetailsValueBusinessAdr1);
		lblDetailsValueBusinessAdr1.setForeground(Color.DARK_GRAY);
		lblDetailsValueBusinessAdr1.setFont(LBL_VALUE_FONT);
		
		lblDetailsValueBusinessAdr2 = new JLabel(" ");
		lblDetailsValueBusinessAdr2.setForeground(Color.DARK_GRAY);
		lblDetailsValueBusinessAdr2.setFont(LBL_VALUE_FONT);
		panelBusinessAdrFields.add(lblDetailsValueBusinessAdr2);
		
		lblDetailsValueBusinessAdr3 = new JLabel(" ");
		lblDetailsValueBusinessAdr3.setForeground(Color.DARK_GRAY);
		lblDetailsValueBusinessAdr3.setFont(LBL_VALUE_FONT);
		panelBusinessAdrFields.add(lblDetailsValueBusinessAdr3);
		
		hGlueBusinessAddress = Box.createHorizontalGlue();
		panelBusinessAddress.add(hGlueBusinessAddress);
		
		separatorBusinessAddress = new JSeparator();
		separatorBusinessAddress.setMaximumSize(new Dimension(32767, 2));
		panelDetailsBusinessAddress.add(separatorBusinessAddress);
		
		panelDetailsManager = new JPanel();
		panelBusiness.add(panelDetailsManager);
		panelDetailsManager.setLayout(new BoxLayout(panelDetailsManager, BoxLayout.Y_AXIS));
		
		panelManager = new JPanel();
		panelDetailsManager.add(panelManager);
		panelManager.setLayout(new BoxLayout(panelManager, BoxLayout.X_AXIS));
		
		hStrutManager = Box.createHorizontalStrut(20);
		hStrutManager.setPreferredSize(new Dimension(20, 40));
		hStrutManager.setMaximumSize(new Dimension(20, 40));
		panelManager.add(hStrutManager);
		
		lblDetailsManager = new JLabel(resourceMap.getString("details.manager.label"));
		lblDetailsManager.setPreferredSize(new Dimension(85, 14));
		lblDetailsManager.setMaximumSize(new Dimension(85, 14));
		lblDetailsManager.setForeground(LBL_FORE_COLOR);
		lblDetailsManager.setFont(LBL_FONT);
		panelManager.add(lblDetailsManager);
		
		lblDetailsIcnManager = new JLabel("    ");
		lblDetailsIcnManager.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/common/ui/panels/resources/icon_team.png")));
		lblDetailsIcnManager.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnManager.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnManager.setHorizontalAlignment(SwingConstants.RIGHT);
		panelManager.add(lblDetailsIcnManager);
		
		lblDetailsValueManager = new JLabel(resourceMap.getString("details.manager.defaultvalue"));
		lblDetailsValueManager.setForeground(new Color(60, 165, 195));
		lblDetailsValueManager.setFont(LBL_VALUE_FONT);
		panelManager.add(lblDetailsValueManager);
		
		hGlueManager = Box.createHorizontalGlue();
		panelManager.add(hGlueManager);
		
		separatorManager = new JSeparator();
		separatorManager.setMaximumSize(new Dimension(32767, 2));
		panelDetailsManager.add(separatorManager);
		
		panelDetailsTeam = new JPanel();
		panelBusiness.add(panelDetailsTeam);
		panelDetailsTeam.setLayout(new BoxLayout(panelDetailsTeam, BoxLayout.X_AXIS));
		
		hStrutTeam = Box.createHorizontalStrut(20);
		hStrutTeam.setPreferredSize(new Dimension(20, 40));
		hStrutTeam.setMaximumSize(new Dimension(20, 40));
		panelDetailsTeam.add(hStrutTeam);
		
		lblDetailsTeam = new JLabel(resourceMap.getString("details.team.label"));
		lblDetailsTeam.setPreferredSize(new Dimension(85, 14));
		lblDetailsTeam.setMaximumSize(new Dimension(85, 14));
		lblDetailsTeam.setForeground(LBL_FORE_COLOR);
		lblDetailsTeam.setFont(LBL_FONT);
		panelDetailsTeam.add(lblDetailsTeam);
		
		lblDetailsIcnTeam = new JLabel("    ");
		lblDetailsIcnTeam.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcnTeam.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcnTeam.setHorizontalAlignment(SwingConstants.RIGHT);
		panelDetailsTeam.add(lblDetailsIcnTeam);
		
		lblDetailsValueTeam = new JLabel("");
		lblDetailsValueTeam.setForeground(new Color(60, 165, 195));
		lblDetailsValueTeam.setFont(LBL_VALUE_FONT);
		panelDetailsTeam.add(lblDetailsValueTeam);
		
		horizontalGlue = Box.createHorizontalGlue();
		panelDetailsTeam.add(horizontalGlue);
		
		verticalGluePnlBusiness = Box.createVerticalGlue();
		panelBusiness.add(verticalGluePnlBusiness);
	}

	
	/**
	 * Creates a new Panel to be added for display in the list of details of the contact profile.
	 * @param label - The label to be shown to the left of the field.
	 * @param labelIcon - The icon to be shown between the label and the actual field.
	 * @param defaultValue - The value to be pre-filled in the field.
	 * @return
	 */
	public JPanel createFieldPanel(String label, ImageIcon labelIcon, String defaultValue){	
		
		JPanel panelDetailsContainer = new JPanel();		
		panelDetailsContainer.setLayout(new BoxLayout(panelDetailsContainer, BoxLayout.Y_AXIS));
		
		panelField = new JPanel();
		panelDetailsContainer.add(panelField);
		panelField.setLayout(new BoxLayout(panelField, BoxLayout.X_AXIS));
		
		Component hStrut = Box.createHorizontalStrut(20);
		hStrut.setPreferredSize(new Dimension(20, 40));
		hStrut.setMaximumSize(new Dimension(20, 40));
		panelField.add(hStrut);
		
		JLabel lblDetailsField = new JLabel(label);
		lblDetailsField.setPreferredSize(new Dimension(130, 14));
		lblDetailsField.setMaximumSize(new Dimension(130, 14));
		lblDetailsField.setForeground(Color.GRAY);
		lblDetailsField.setFont(LBL_FONT);
		panelField.add(lblDetailsField);
		
		JLabel lblDetailsIcn = new JLabel("    ");
		lblDetailsIcn.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetailsIcn.setPreferredSize(new Dimension(50, 30));
		lblDetailsIcn.setMaximumSize(new Dimension(50, 30));
		lblDetailsIcn.setIcon(labelIcon);
		panelField.add(lblDetailsIcn);
		
		JLabel lblDetailsValueField = new JLabel(defaultValue);
		lblDetailsValueField.setForeground(LBL_VALUE_FORE_LIGHT_COLOR);
		lblDetailsValueField.setFont(LBL_VALUE_FONT);
		panelField.add(lblDetailsValueField);
		
		Component hGlue = Box.createHorizontalGlue();
		panelField.add(hGlue);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(32767, 2));
		panelDetailsContainer.add(separator);
		
		return panelDetailsContainer;
	}
	
	public void goEditMode(final boolean editMode){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				lblDetailsValueCell.setVisible(!editMode);
				lblDetailsIcnCell.setVisible(!editMode);
				txtDetailsCell.setVisible(editMode);
				
				lblDetailsValueEmail.setVisible(!editMode);
				lblDetailsIcnEmail.setVisible(!editMode);
				txtDetailsEmail.setVisible(editMode);
				
				lblDetailsValueInternal.setVisible(!editMode);
				lblDetailsIcnInternal.setVisible(!editMode);
				txtDetailsInternal.setVisible(editMode);
				
			    lblDetailsValueOfficeDid.setVisible(!editMode);
			    lblDetailsIcnOfficeDid.setVisible(!editMode);
				txtDetailsDid.setVisible(editMode);
			    
				lblDetailsValueTwitter.setVisible(!editMode);
				lblDetailsIcnTwitter.setVisible(!editMode);
			    txtDetailsTwitter.setVisible(editMode);
			    
			    lblDetailsValueLinkedIn.setVisible(!editMode);
			    lblDetailsIcnLinkedIn.setVisible(!editMode);
			    txtDetailsLinkedIn.setVisible(editMode);
			    
			    lblDetailsValueFacebook.setVisible(!editMode);
			    lblDetailsIcnFacebook.setVisible(!editMode);
			    txtDetailsFacebook.setVisible(editMode);
			    
			    lblDetailsValueGoogle.setVisible(!editMode);
			    lblDetailsIcnGoogle.setVisible(!editMode);
			    txtDetailsGoogle.setVisible(editMode);
			    
			    lblDetailsValueXing.setVisible(!editMode);
			    lblDetailsIcnXing.setVisible(!editMode);
			    txtDetailsXing.setVisible(editMode);
				
				panelSaveButtons.setVisible(editMode);
			    
				if (editMode){
					
				}else{
					
				}				
			}
		});
		
	}
	
	public UserMiniPanelGloss getUserAvatarPanel() {
		return userAvatarPanel;
	}
		
	public void hideAvatarPanel(){		
		this.panelAvatar.setVisible(false);
		this.panelAvatar.setPreferredSize(new Dimension(0,0));
	}
	
	public void setUserAvatarPanel(UserMiniPanelGloss userAvatarPanel) {
		if (this.userAvatarPanel!=null) {
			this.panelAvatar.remove(this.userAvatarPanel);
		}
		final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceContactProfile.class);
		this.userAvatarPanel = userAvatarPanel;
		this.userAvatarPanel.setUserStatusVisible(false);
		this.userAvatarPanel.setBorder(null);
		this.userAvatarPanel.setPreferredSize(null);
		this.userAvatarPanel.removeMouseListener(this.userAvatarPanel.getLocalMouseListener());
		this.panelAvatar.add(this.userAvatarPanel, BorderLayout.WEST);
		
		String jid = this.userAvatarPanel.getJID();
		VCard targetVCard = (jid != null && jid.length()>0) ? SparkManager.getVCardManager().getVCard(jid) : null;
		if (targetVCard != null) {
			
			// ------------- CONTACT TAB -------------- //
			this.lblDetailsValueInternal.setText(targetVCard.getField("X-INTERN"));
			this.txtDetailsInternal.setPrompt(this.lblDetailsValueInternal.getText());
			
			this.lblDetailsValueOfficeDid.setText(targetVCard.getField("X-DID"));			
			this.txtDetailsDid.setPrompt(this.lblDetailsValueOfficeDid.getText());
			
			if (targetVCard.getPhoneHome("CELL") != null && !targetVCard.getPhoneHome("CELL").isEmpty()) {
				this.lblDetailsValueCell.setText(targetVCard.getPhoneHome("CELL"));
			}
			else {
				this.lblDetailsValueCell.setText("");
			}
			this.txtDetailsCell.setPrompt(this.lblDetailsValueCell.getText());
			
			if (targetVCard.getEmailHome()!=null && !targetVCard.getEmailHome().isEmpty()){
				this.lblDetailsValueEmail.setText(targetVCard.getEmailHome());
			}else{
				this.lblDetailsValueEmail.setText("");
			}
			this.txtDetailsEmail.setPrompt(this.lblDetailsValueEmail.getText());
			
			this.lblDetailsValueCorpIm.setText(this.userAvatarPanel.getJID());
			
			this.lblDetailsValueConfBridge.setText("...");
			buildInviteToBridgeLabel();	
			
			this.lblDetailsValueConfBrInvite.addMouseListener(inviteToBridgeLblListener);
			
			buildInviteToGroupChatLabel();
			
			this.lblDetailsValueCompanyUrl.setText(targetVCard.getField("URL"));
			
			String twitterText = targetVCard.getField("X-TWITTER");
            String linkedinText = targetVCard.getField("X-LINKEDIN");
            String facebookText = targetVCard.getField("X-FACEBOOK");
            String googleText = targetVCard.getField("X-GOOGLE");
            String xingText = targetVCard.getField("X-XING");
            
            this.txtDetailsTwitter.setPrompt(twitterText);
            this.txtDetailsLinkedIn.setPrompt(linkedinText);
            this.txtDetailsFacebook.setPrompt(facebookText);            
            this.txtDetailsGoogle.setPrompt(googleText);
            this.txtDetailsXing.setPrompt(xingText);
            
            if (twitterText==null || twitterText.isEmpty()){
            	twitterText = resourceMap.getString("details.twitter.defaultvalue");
            }
            if (linkedinText==null || linkedinText.isEmpty()){
            	linkedinText = resourceMap.getString("details.linkedin.defaultvalue");
            }
            if (facebookText==null || facebookText.isEmpty()){
            	facebookText = resourceMap.getString("details.facebook.defaultvalue");
            }
            if (googleText==null || googleText.isEmpty()){
            	googleText = resourceMap.getString("details.google.defaultvalue");            	
            }
            if (xingText==null || xingText.isEmpty()){
            	xingText = resourceMap.getString("details.xing.defaultvalue");
            }
            
            this.lblDetailsValueTwitter.setText(twitterText);
            this.lblDetailsValueFacebook.setText(facebookText);
            this.lblDetailsValueLinkedIn.setText(linkedinText);
            this.lblDetailsValueGoogle.setText(googleText);
            this.lblDetailsValueXing.setText(xingText);
            
			
			//----------------- BUSINESS TAB ------------------//
			this.lblDetailsValueJob.setText(targetVCard.getField("TITLE"));
			this.lblDetailsValueCompany.setText(targetVCard.getOrganization());
			this.lblDetailsValueDepartment.setText(targetVCard.getOrganizationUnit());
			
			this.lblDetailsValueBusinessAdr1.setText(targetVCard.getAddressFieldWork("STREET"));

			this.lblDetailsValueBusinessAdr2.setText(targetVCard.getAddressFieldWork("LOCALITY"));
			
			String adr=targetVCard.getAddressFieldWork("LOCALITY")+", "+targetVCard.getAddressFieldWork("REGION")+" "+targetVCard.getAddressFieldWork("PCODE")+", "+targetVCard.getAddressFieldWork("CTRY");
			adr = adr.replaceAll("null", "");
            this.lblDetailsValueBusinessAdr3.setText(adr);
            
            this.lblDetailsValueManager.setText(targetVCard.getField("X-MANAGER"));
            
            buildAlertToggle(userAvatarPanel.getJID());
		}
		
	}
		
	protected void buildInviteToBridgeLabel(){
		InviteBuilder iBuilder = new InviteBuilder(this.lblDetailsValueConfBridge, null, null);
		iBuilder.execute();
	}
		
	protected void buildInviteToGroupChatLabel(){
		ContactActions actions = new ContactActions(userAvatarPanel, EzuceContactProfile.this);
		javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(ContactActions.class, actions);
		
		this.lblDetailsValueConfChat.setText("...");
		InviteBuilder iBuilder = new InviteBuilder(this.lblDetailsValueConfChat, actionMap.get("startGroupChatAction"), null);
		iBuilder.execute();
		
		InviteBuilder iBuilder2 = new InviteBuilder(this.lblDetailsValueConfChatInvite, actionMap.get("startGroupChatAction"), null);
		iBuilder2.execute();
	}
	
	protected void buildAlertToggle(String userBareJid){
		AlertOnlineBuilder aBuilder = new AlertOnlineBuilder(this.btnAlertOnline, userBareJid);
		aBuilder.execute();
	}
	
	protected void saveProfile(){
		try {
			final VCard vcard = new VCard();
		    
		    //set new info:
			vcard.setPhoneHome("CELL", txtDetailsCell.getText());
			vcard.setEmailHome(txtDetailsEmail.getText());
			vcard.setField("X-INTERN",txtDetailsInternal.getText());						
			vcard.setField("X-DID",txtDetailsDid.getText());			
			vcard.setField("X-TWITTER",txtDetailsTwitter.getText());
            vcard.setField("X-LINKEDIN",txtDetailsLinkedIn.getText());
            vcard.setField("X-FACEBOOK",txtDetailsFacebook.getText());
            vcard.setField("X-GOOGLE",txtDetailsGoogle.getText());
            vcard.setField("X-XING",txtDetailsXing.getText());
		    		    
		    //save info:
			final VCardManager vcardManager = SparkManager.getVCardManager();
		    vcardManager.setPersonalVCard(vcard);
		    vcard.save(SparkManager.getConnection());

	        //make sure your vcard is up to date in the vcards directory in your profile
		    vcardManager.reloadVCard(SparkManager.getSessionManager().getJID());
		    // Notify listeners
		    SparkManager.getVCardManager().notifyVCardListeners();
		    setUserAvatarPanel(this.userAvatarPanel);
		} catch (XMPPException e) {
		    Log.error(e);
		    JOptionPane.showMessageDialog(SparkManager.getMainWindow(),
			    Res.getString("message.vcard.not.supported"),
			    Res.getString("title.error"), JOptionPane.ERROR_MESSAGE);
		}

	}
	
	protected void cancelChangeProfile(){
		this.txtDetailsInternal.setPrompt(this.lblDetailsValueInternal.getText());
		this.txtDetailsDid.setPrompt(this.lblDetailsValueOfficeDid.getText());
		this.txtDetailsCell.setPrompt(this.lblDetailsValueCell.getText());
		this.txtDetailsEmail.setPrompt(this.lblDetailsValueEmail.getText());
		String jid = this.userAvatarPanel.getJID();
		final VCard targetVCard = jid != null ? SparkManager.getVCardManager().getVCard(jid) : null;
		if (targetVCard != null) {
			String twitterText = targetVCard.getField("X-TWITTER");
            String linkedinText = targetVCard.getField("X-LINKEDIN");
            String facebookText = targetVCard.getField("X-FACEBOOK");
            String googleText = targetVCard.getField("X-GOOGLE");
            String xingText = targetVCard.getField("X-XING");
			this.txtDetailsTwitter.setPrompt(twitterText);
	        this.txtDetailsLinkedIn.setPrompt(linkedinText);
	        this.txtDetailsFacebook.setPrompt(facebookText);            
	        this.txtDetailsGoogle.setPrompt(googleText);
	        this.txtDetailsXing.setPrompt(xingText);
		}
	}
	
	
	public ContactListEntry getContactListEntry() {
		return contactListEntry;
	}

	public void setContactListEntry(ContactListEntry contactListEntry) {
		this.contactListEntry = contactListEntry;
	}

	public JLabel getLblDetailsInternal() {
		return lblDetailsInternal;
	}

	public JLabel getLblDetailsIcnInternal() {
		return lblDetailsIcnInternal;
	}

	public JLabel getLblDetailsValueInternal() {
		return lblDetailsValueInternal;
	}

	public JLabel getLblDetailsOffideDid() {
		return lblDetailsOffideDid;
	}

	public JLabel getLblDetailsIcnOfficeDid() {
		return lblDetailsIcnOfficeDid;
	}

	public JLabel getLblDetailsValueOfficeDid() {
		return lblDetailsValueOfficeDid;
	}

	public JButton getBtnAddUserToRoster() {
		return btnAddUserToRoster;
	}

	public JLabel getLblDetailsCell() {
		return lblDetailsCell;
	}

	public JLabel getLblDetailsIcnCell() {
		return lblDetailsIcnCell;
	}

	public JLabel getLblDetailsValueCell() {
		return lblDetailsValueCell;
	}

	public JLabel getLblDetailsEmail() {
		return lblDetailsEmail;
	}

	public JLabel getLblDetailsIcnEmail() {
		return lblDetailsIcnEmail;
	}

	public JLabel getLblDetailsValueEmail() {
		return lblDetailsValueEmail;
	}

	public JLabel getLblDetailsCorpIm() {
		return lblDetailsCorpIm;
	}

	public JLabel getLblDetailsIcnCorpIm() {
		return lblDetailsIcnCorpIm;
	}

	public JLabel getLblDetailsValueCorpIm() {
		return lblDetailsValueCorpIm;
	}

	public JLabel getLblDetailsConfBridge() {
		return lblDetailsConfBridge;
	}

	public JLabel getLblDetailsIcnConfBridge() {
		return lblDetailsIcnConfBridge;
	}

	public JLabel getLblDetailsValueConfBridge() {
		return lblDetailsValueConfBridge;
	}

	public JLabel getLblDetailsConfChat() {
		return lblDetailsConfChat;
	}

	public JLabel getLblDetailsIcnConfChat() {
		return lblDetailsIcnConfChat;
	}

	public JLabel getLblDetailsValueConfChat() {
		return lblDetailsValueConfChat;
	}

	public JLabel getLblDetailsConfBrInvite() {
		return lblDetailsConfBrInvite;
	}

	public JLabel getLblDetailsIcnConfBrInvite() {
		return lblDetailsIcnConfBrInvite;
	}

	public JLabel getLblDetailsValueConfBrInvite() {
		return lblDetailsValueConfBrInvite;
	}

	public JLabel getLblContactTabHeader() {
		return lblContactTabHeader;
	}

	public JLabel getLblBusinessTabHeader() {
		return lblBusinessTabHeader;
	}
	
	public JButton getBtnCloseProfile(){
		return btnCloseProfile;
	}
	
	public JToggleButton getBtnEnterEditMode(){
		return btnEnterEditMode;
	}
}
