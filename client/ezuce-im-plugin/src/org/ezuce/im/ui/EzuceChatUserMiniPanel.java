/*
 * EzuceChatUserMiniPanel.java
 *
 * Created on Aug 8, 2011, 11:55:26 PM
 */
package org.ezuce.im.ui;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.panels.Images;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.im.action.DeafUserTask;
import org.ezuce.im.action.KickUserTask;
import org.ezuce.im.action.MuteUserTask;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactInfoWindow;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.UIComponentRegistry;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class EzuceChatUserMiniPanel extends javax.swing.JPanel implements VCardLoaderCallback {
	
    private JButton jButtonAudio;
    private JButton jButtonDisconect;
    private JButton jButtonMic;
    private JButton jButtonVCard;
    private JLabel jLabelBgImage;
    private JLabel jLabelPosition;
    private JLabel jLabelStatus;
    private JLabel jLabelUserAvatar;
    private JLabel jLabelUsername;
    private JLayeredPane jLayeredPane;
    private JPanel jPanelBackground;
    private JPanel jPanelButtons;

    private VCard userVCard;
    private String userJid;
    private int confId;
    private String conferenceName;
    
    private boolean isDeaf=false;
    private boolean isMute=false;
    private boolean localUserIsOwner=false;
    
    private final Dimension iconDimension=new Dimension(67,67);
    
    private ConferenceMemberXML confMemberXml;
    /** Creates new form EzuceChatUserMiniPanel */
    public EzuceChatUserMiniPanel() {
        initComponents();
        customize();
    }
    
    public EzuceChatUserMiniPanel(boolean localUserIsOwner)
    {
        this.localUserIsOwner=localUserIsOwner;
        initComponents();
        customize();
    }

    protected void customize()
    {
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.
                                    getInstance().getContext().getActionMap(this);
        final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
        
        if (localUserIsOwner)
        {
            javax.swing.Action action=actionMap.get("deafUserAction");
            action.putValue(action.SMALL_ICON, getJButtonAudio().getIcon());
            getJButtonAudio().setAction(action);
            getJButtonAudio().setToolTipText(resMap.getString("jButtonAudio.Tooltip"));

            action=actionMap.get("muteUserAction");
            action.putValue(action.SMALL_ICON, getJButtonMic().getIcon());
            getJButtonMic().setAction(action);
            getJButtonMic().setToolTipText(resMap.getString("jButtonMic.Tooltip"));

            action=actionMap.get("kickUserAction");
            action.putValue(action.SMALL_ICON, getJButtonDisconect().getIcon());
            getJButtonDisconect().setAction(action);
            getJButtonDisconect().setToolTipText(resMap.getString("jButtonDisconect.Tooltip"));
        }
        else
        {
            getJButtonAudio().setVisible(false);
            getJButtonMic().setVisible(false);
            getJButtonDisconect().setVisible(false);
        }
        
        getJButtonVCard().setToolTipText(resMap.getString("jButtonVCard.Tooltip"));
    }
    
    public void setUserName(String userName)
    {
        this.jLabelUsername.setText(userName);
    }

    public String getUserName()
    {
        return this.jLabelUsername.getText();
    }

    public void setUserDetail(String detail)
    {
        this.jLabelPosition.setText(detail);
    }

    public String getUserDetail()
    {
        return this.jLabelPosition.getName();
    }

    public void setUserAvatar(ImageIcon avatarIcon) {
        if (avatarIcon!=null) {
            try {
                jLabelUserAvatar.setIcon(Images.ensureSizeScaling(avatarIcon, iconDimension.width, iconDimension.height, jLabelUserAvatar));
            }
            catch(IOException ioe) {
                Log.warning("Cannot scale group chat icon for "+getUserName());
                jLabelUserAvatar.setIcon(avatarIcon);
            }            
        }
    }

    public void setStatusIcon(Icon statusIcon) {
        this.jLabelStatus.setIcon(statusIcon);
    }

    public JButton getJButtonAudio() {
        return this.jButtonAudio;
    }

    public JButton getJButtonMic() {
        return this.jButtonMic;
    }

    public JButton getJButtonDisconect() {
        return this.jButtonDisconect;
    }

    public JButton getJButtonVCard() {
        return this.jButtonVCard;
    }
    
    public void setUserVCard(VCard vc) {
        this.userVCard=vc;
    }
    public VCard getUserVCard() {
        return this.userVCard;
    }
    
    public void setUserJid(String jid) {
        this.userJid=jid;  
        this.updateStatus(EzucePresenceManager.getPresence(userJid));
    }
    
    public String getUserJid() {
        return this.userJid;
    }
    
    private void showVCard(MouseEvent e) {
        if (this.userVCard==null) {
            return;
        }
        final ContactList cList=SparkManager.getContactList();
        final ContactItem ci=cList.getContactItemByJID(userJid);
        ContactGroup cg = null;
        for (ContactGroup g : cList.getContactGroups()) {
            ContactItem item = g.getContactItemByJID(userJid, true);
            if (item != null) {
                cg = g;
                break;
            }
        }
        if (ci==null || cg==null) {
            return;
        }
        ContactInfoWindow info = UIComponentRegistry.getContactInfoWindow();
        info.display(cg, e);
        info.setContactItem(ci);
        info.setWindowLocation(e.getXOnScreen(), e.getYOnScreen());
    }
    
    public void setMemberMuteStatus(boolean isMute)
    {
        this.isMute = isMute;
        if (isMute)
        {
            final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
            getJButtonMic().setIcon(resMap.getIcon("jButtonMic.rolloverIcon"));
            getJButtonMic().setRolloverIcon(resMap.getIcon("jButtonMic.icon"));
        }
        else
        {
            final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
            getJButtonMic().setIcon(resMap.getIcon("jButtonMic.icon"));
            getJButtonMic().setRolloverIcon(resMap.getIcon("jButtonMic.rolloverIcon"));
        }
    }
    
    public void setMemberDeafStatus(boolean isDeaf) {
        this.isDeaf = isDeaf;
        if (isDeaf) {
            final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
            getJButtonAudio().setIcon(resMap.getIcon("jButtonAudio.rolloverIcon"));
            getJButtonAudio().setRolloverIcon(resMap.getIcon("jButtonAudio.icon"));
        }
        else {
            final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
            getJButtonAudio().setIcon(resMap.getIcon("jButtonAudio.icon"));
            getJButtonAudio().setRolloverIcon(resMap.getIcon("jButtonAudio.rolloverIcon"));
        }
    }
    
    
    @Action
	public void deafUserAction(ActionEvent ae) {
		DeafUserTask dut = new DeafUserTask(Application.getInstance(), conferenceName, confId, !isDeaf);
		dut.execute();
		isDeaf = !isDeaf;
		if (isDeaf) {
			final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
			getJButtonAudio().setIcon(resMap.getIcon("jButtonAudio.rolloverIcon"));
			getJButtonAudio().setRolloverIcon(resMap.getIcon("jButtonAudio.icon"));
		} else {
			final ResourceMap resMap = Application.getInstance().getContext().getResourceMap(this.getClass());
			getJButtonAudio().setIcon(resMap.getIcon("jButtonAudio.icon"));
			getJButtonAudio().setRolloverIcon(resMap.getIcon("jButtonAudio.rolloverIcon"));
		}
	}
    
    @Action
	public void muteUserAction(ActionEvent ae) {
		MuteUserTask mut = new MuteUserTask(Application.getInstance(),
				conferenceName, confId, !isMute);
		mut.execute();
		isMute = !isMute;
		if (isMute) {
			final ResourceMap resMap = Application.getInstance().getContext()
					.getResourceMap(this.getClass());
			getJButtonMic().setIcon(resMap.getIcon("jButtonMic.rolloverIcon"));
			getJButtonMic().setRolloverIcon(resMap.getIcon("jButtonMic.icon"));
		} else {
			final ResourceMap resMap = Application.getInstance().getContext()
					.getResourceMap(this.getClass());
			getJButtonMic().setIcon(resMap.getIcon("jButtonMic.icon"));
			getJButtonMic().setRolloverIcon(
					resMap.getIcon("jButtonMic.rolloverIcon"));
		}
	}
    
	@Action
	public void kickUserAction(ActionEvent ae) {
		KickUserTask kut = new KickUserTask(Application.getInstance(), conferenceName, confId);
		kut.execute();
	}

	public void setConferenceName(String confName) {
		this.conferenceName = confName;
	}

	public String getConferenceName() {
		return this.conferenceName;
	}
    
    public void updateStatus(Presence presence) {
        this.setStatusIcon(EzucePresenceUtils.getUpdatedUserStatusIconLarge(presence));
    }

    public void setConfId(int confId) {
		this.confId = confId;
	}

    public ConferenceMemberXML getConfMemberXml() {
        return confMemberXml;
    }

    public void setConfMemberXml(ConferenceMemberXML confMemberXml) {
        this.confMemberXml = confMemberXml;
    }
    
    

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLayeredPane = new JLayeredPane();
        jPanelButtons = new JPanel();
        jButtonAudio = new JButton();
        jButtonMic = new JButton();
        jButtonDisconect = new JButton();
        jButtonVCard = new JButton();
        jLabelStatus = new JLabel();
        jLabelUsername = new JLabel();
        jLabelPosition = new JLabel();
        jPanelBackground = new JPanel();
        jLabelBgImage = new JLabel();
        jLabelUserAvatar = new JLabel();

        setMaximumSize(new Dimension(117, 126));
        setMinimumSize(new Dimension(117, 126));
        setOpaque(false);
        setPreferredSize(new Dimension(117, 126));

        jLayeredPane.setDoubleBuffered(true);
        jLayeredPane.setName("jLayeredPane"); // NOI18N

        jPanelButtons.setMaximumSize(new Dimension(18, 80));
        jPanelButtons.setMinimumSize(new Dimension(18, 80));
        jPanelButtons.setName("jPanelButtons"); // NOI18N
        jPanelButtons.setOpaque(false);
        jPanelButtons.setPreferredSize(new Dimension(18, 80));
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setGap(8);
        jPanelButtons.setLayout(verticalLayout1);

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceChatUserMiniPanel.class);
        jButtonAudio.setIcon(resourceMap.getIcon("jButtonAudio.icon")); // NOI18N
        jButtonAudio.setBorder(null);
        jButtonAudio.setBorderPainted(false);
        jButtonAudio.setContentAreaFilled(false);
        jButtonAudio.setDoubleBuffered(true);
        jButtonAudio.setFocusPainted(false);
        jButtonAudio.setHideActionText(true);
        jButtonAudio.setName("jButtonAudio"); // NOI18N
        jButtonAudio.setRolloverIcon(resourceMap.getIcon("jButtonAudio.rolloverIcon")); // NOI18N
        jPanelButtons.add(jButtonAudio);

        jButtonMic.setIcon(resourceMap.getIcon("jButtonMic.icon")); // NOI18N
        jButtonMic.setBorder(null);
        jButtonMic.setBorderPainted(false);
        jButtonMic.setContentAreaFilled(false);
        jButtonMic.setDoubleBuffered(true);
        jButtonMic.setFocusPainted(false);
        jButtonMic.setHideActionText(true);
        jButtonMic.setName("jButtonMic"); // NOI18N
        jButtonMic.setRolloverIcon(resourceMap.getIcon("jButtonMic.rolloverIcon")); // NOI18N
        jPanelButtons.add(jButtonMic);

        jButtonDisconect.setIcon(resourceMap.getIcon("jButtonDisconect.icon")); // NOI18N
        jButtonDisconect.setBorder(null);
        jButtonDisconect.setBorderPainted(false);
        jButtonDisconect.setContentAreaFilled(false);
        jButtonDisconect.setDoubleBuffered(true);
        jButtonDisconect.setFocusPainted(false);
        jButtonDisconect.setHideActionText(true);
        jButtonDisconect.setName("jButtonDisconect"); // NOI18N
        jButtonDisconect.setRolloverIcon(resourceMap.getIcon("jButtonDisconect.rolloverIcon")); // NOI18N
        jPanelButtons.add(jButtonDisconect);

        jButtonVCard.setIcon(resourceMap.getIcon("jButtonVCard.icon")); // NOI18N
        jButtonVCard.setBorder(null);
        jButtonVCard.setBorderPainted(false);
        jButtonVCard.setContentAreaFilled(false);
        jButtonVCard.setDoubleBuffered(true);
        jButtonVCard.setFocusPainted(false);
        jButtonVCard.setHideActionText(true);
        jButtonVCard.setName("jButtonVCard"); // NOI18N
        jButtonVCard.setRolloverIcon(resourceMap.getIcon("jButtonVCard.rolloverIcon")); // NOI18N
        jButtonVCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jButtonVCardMouseClicked(evt);
            }
        });
        jPanelButtons.add(jButtonVCard);

        jPanelButtons.setBounds(86, 8, 18, 84);
        jLayeredPane.add(jPanelButtons, JLayeredPane.DEFAULT_LAYER);

        jLabelStatus.setIcon(resourceMap.getIcon("jLabelStatus.icon")); // NOI18N
        jLabelStatus.setText("jLabel1");
        jLabelStatus.setMaximumSize(new Dimension(21, 5));
        jLabelStatus.setMinimumSize(new Dimension(21, 5));
        jLabelStatus.setName("jLabelStatus"); // NOI18N
        jLabelStatus.setPreferredSize(new Dimension(21, 5));
        jLabelStatus.setBounds(53, 79, 21, 5);
        jLayeredPane.add(jLabelStatus, JLayeredPane.DEFAULT_LAYER);
        jLabelStatus.getAccessibleContext().setAccessibleName("jLabelStatus");

        jLabelUsername.setFont(new Font("Arial", 1, 10));
        jLabelUsername.setForeground(new Color(46, 85, 102));
        jLabelUsername.setText("User Name");
        jLabelUsername.setMaximumSize(new Dimension(95, 14));
        jLabelUsername.setMinimumSize(new Dimension(95, 14));
        jLabelUsername.setName("jLabelUsername"); // NOI18N
        jLabelUsername.setPreferredSize(new Dimension(95, 14));
        jLabelUsername.setBounds(8, 95, 95, 14);
        jLayeredPane.add(jLabelUsername, JLayeredPane.DEFAULT_LAYER);
        jLabelUsername.getAccessibleContext().setAccessibleName("jLabelUsername");

        jLabelPosition.setFont(new Font("Arial Narrow", 0, 9));
        jLabelPosition.setForeground(new Color(153, 153, 153));
        jLabelPosition.setText("Position in company");
        jLabelPosition.setVerticalAlignment(SwingConstants.TOP);
        jLabelPosition.setDoubleBuffered(true);
        jLabelPosition.setMaximumSize(new Dimension(100, 14));
        jLabelPosition.setMinimumSize(new Dimension(100, 14));
        jLabelPosition.setName("jLabelPosition"); // NOI18N
        jLabelPosition.setPreferredSize(new Dimension(100, 14));
        jLabelPosition.setBounds(8, 108, 100, 14);
        jLayeredPane.add(jLabelPosition, JLayeredPane.DEFAULT_LAYER);
        jLabelPosition.getAccessibleContext().setAccessibleName("jLabelPosition");

        jPanelBackground.setMaximumSize(new Dimension(116, 126));
        jPanelBackground.setMinimumSize(new Dimension(116, 126));
        jPanelBackground.setName("jPanelBackground"); // NOI18N
        jPanelBackground.setOpaque(false);
        jPanelBackground.setPreferredSize(new Dimension(116, 126));

        jLabelBgImage.setIcon(resourceMap.getIcon("jLabelBgImage.icon")); // NOI18N
        jLabelBgImage.setDoubleBuffered(true);
        jLabelBgImage.setName("jLabelBgImage"); // NOI18N

        GroupLayout jPanelBackgroundLayout = new GroupLayout(jPanelBackground);
        jPanelBackground.setLayout(jPanelBackgroundLayout);
        jPanelBackgroundLayout.setHorizontalGroup(
            jPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelBackgroundLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabelBgImage, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
        );
        jPanelBackgroundLayout.setVerticalGroup(
            jPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLabelBgImage, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
        );

        jPanelBackground.setBounds(0, 0, 116, 126);
        jLayeredPane.add(jPanelBackground, JLayeredPane.DEFAULT_LAYER);

        jLabelUserAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelUserAvatar.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/im/ui/resources/testavatar.png"))); // NOI18N
        jLabelUserAvatar.setDoubleBuffered(true);
        jLabelUserAvatar.setMaximumSize(new Dimension(67, 67));
        jLabelUserAvatar.setMinimumSize(new Dimension(67, 67));
        jLabelUserAvatar.setName("jLabelUserAvatar"); // NOI18N
        jLabelUserAvatar.setPreferredSize(new Dimension(67, 67));
        jLabelUserAvatar.setBounds(9, 8, 67, 67);
        jLayeredPane.add(jLabelUserAvatar, JLayeredPane.DEFAULT_LAYER);
        jLabelUserAvatar.getAccessibleContext().setAccessibleName("jLabelUserAvatar");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLayeredPane, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
        );
    }

	
	
	private void jButtonVCardMouseClicked(MouseEvent evt) {
		this.showVCard(evt);
	}

	@Override
	public void vcardLoaded(VCard vCard) {
		String displayName=vCard.getNickName();
    	String detail = org.apache.commons.lang3.StringUtils.EMPTY;
		if (displayName == null || displayName.trim().length()<1) {
			displayName=(vCard.getFirstName()==null || vCard.getFirstName().trim().length()<1)?"":vCard.getFirstName();
			displayName+=(vCard.getMiddleName()==null || vCard.getMiddleName().trim().length()<1)?" ":vCard.getMiddleName();
			displayName+=(vCard.getLastName()==null || vCard.getLastName().trim().length()<1)?" ":vCard.getLastName();
		}
            
		detail=vCard.getOrganizationUnit();		    		
		setUserVCard(vCard);
        setUserName(displayName);
        setUserDetail(defaultIfEmpty(detail, EMPTY));
		setUserAvatar(Utils.retrieveAvatar(vCard));
		revalidate();
		repaint();
	}
}
