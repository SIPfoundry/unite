/*
 * EzuceGroupChatParticipantMiniPanel.java
 *
 * Created on Aug 24, 2011, 11:21:20 PM
 */
package org.ezuce.im.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.ezuce.common.EzuceButtonFactory;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.MugshotPanel;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.ButtonFactory;
import org.jivesoftware.spark.util.log.Log;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author Razvan
 */
public class EzuceGroupChatParticipantMiniPanel extends javax.swing.JPanel implements VCardLoaderCallback {
    private JLabel jLabelPosition;
    private JLabel jLabelStatus;
    private JLabel jLabelUserDisplayName;
    private MugshotPanel imgPanel;
    // End of variables declaration//GEN-END:variables
    private String nickname;
    private String userJid;
    private ImageIcon userAvatar;
    private EzuceGroupChatParticipantList parent = null;
    
    // list of constatns, 
    private final int MAIN_PANEL_HEIGHT = 53;
    private final Dimension MAIN_PANEL_MIN_SIZE = new Dimension(0, MAIN_PANEL_HEIGHT);
    private final Dimension MAIN_PANEL_MAX_SIZE = new Dimension(Short.MAX_VALUE, MAIN_PANEL_HEIGHT);
    private final Color MAIN_PANEL_BACKGROUND_COLOR = new Color(255, 255, 255);

    private final Font DISPLAY_NAME_FONT = new Font("Arial", Font.BOLD, 12);
    private final Font POSITION_FONT = new Font("Tahoma", Font.PLAIN, 10);
    private final Font STATUS_FONT = POSITION_FONT;
    private final Dimension LABEL_POSITION_SIZE = new Dimension(35, 11);
    private final Dimension LABEL_STATUS_SIZE = LABEL_POSITION_SIZE;
    private final Color LABEL_STATUS_FOREGROUND_COLOR = new Color(153, 153, 153);
    private final Color DISPLAY_NAME_FOREGROUND_COLOR = new Color(46, 85, 102);
    private final Color LABEL_POSITION_FOREGROUND_COLOR = LABEL_STATUS_FOREGROUND_COLOR;
    private JButton screenSharingButton;
    
    /** 
     * Creates new form EzuceGroupChatParticipantMiniPanel 
     */
    public EzuceGroupChatParticipantMiniPanel(EzuceGroupChatParticipantList parent) {
            this.parent = parent;
            initComponents();
            initPanel();
    }

    /**
     * @wbp.parser.constructor
     */
    public EzuceGroupChatParticipantMiniPanel(String jid, EzuceGroupChatParticipantList parent) {
            userJid = jid;
            this.parent = parent;
            initComponents();
            initPanel();
            AsyncLoader.getInstance().executeJob(userJid, this);
    }

    /**
     * Init the look of panel's itself.
     */
    private void initPanel() {
        setBackground(MAIN_PANEL_BACKGROUND_COLOR);
        setMinimumSize(MAIN_PANEL_MIN_SIZE);
        setPreferredSize(new Dimension(177, 51));
        setMaximumSize(MAIN_PANEL_MAX_SIZE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 0, Color.orange), //outer
            BorderFactory.createMatteBorder(0, 0, 1, 0, ColorConstants.SEPARATOR_COLOR_2)) //inner
        ); 
    }

    @Override
    public void vcardLoaded(VCard vCard) {
        if (vCard != null) {
                String jobTitle = vCard.getField("TITLE");
                jobTitle = (jobTitle == null || jobTitle.trim().isEmpty()) ? "No job title"
                                : jobTitle;
                String descr = jobTitle;
                jLabelPosition.setText(descr.equals("x") ? " " : descr);
                setUserAvatar(Utils.retrieveAvatar(vCard));
            parent.repaint();
            parent.revalidate();
        }
    }

    public void updateStatus(Presence presence) {
        imgPanel.setPresence(presence);
        jLabelStatus.setText(EzucePresenceUtils.getUpdatedUserStatus(presence));
        //update all data from vcard
        if (Utils.isVCardUpdated(presence)) {
            AsyncLoader.getInstance().execute(presence.getFrom(), this);
        }
    }

    public void setUserDisplayName(String name) {
        this.jLabelUserDisplayName.setText(name);
    }

    public String getUserDisplayName() {
        return this.jLabelUserDisplayName.getText();
    }

    public void setUserState(MugshotPanel.State state) {
        imgPanel.updateState(state);
    }

    /**
     * Set the user's avatar icon. Remark: the icon is scaled.
     * 
     * @param avatarBytes
     */
    public void setUserAvatar(ImageIcon im) {
        imgPanel.setMugshot(im);
        userAvatar = im;
    }
    
    public ImageIcon getUserAvatar(){
        return userAvatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserJid() {
        return userJid;
    }

    public void setUserJid(String userJid) {
        this.userJid = userJid;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabelUserDisplayName = new JLabel();
        jLabelPosition = new JLabel();
        jLabelStatus = new JLabel();
        imgPanel = new MugshotPanel(true);
        
        final GroupLayout jPanel1Layout = new GroupLayout(imgPanel);
	imgPanel.setLayout(jPanel1Layout);
		
        jLabelUserDisplayName.setFont(DISPLAY_NAME_FONT); // NOI18N
        jLabelUserDisplayName.setForeground(DISPLAY_NAME_FOREGROUND_COLOR);
        jLabelUserDisplayName.setText("Nickname");
        jLabelUserDisplayName.setDoubleBuffered(true);
        jLabelUserDisplayName.setFocusable(false);
        jLabelUserDisplayName.setIconTextGap(2);
        jLabelUserDisplayName.setName("jLabelUserDisplayName"); // NOI18N

        jLabelPosition.setFont(POSITION_FONT); // NOI18N
        jLabelPosition.setForeground(LABEL_POSITION_FOREGROUND_COLOR);        
        jLabelPosition.setDoubleBuffered(true);
        jLabelPosition.setMaximumSize(LABEL_POSITION_SIZE);
        jLabelPosition.setMinimumSize(LABEL_POSITION_SIZE);
        jLabelPosition.setPreferredSize(LABEL_POSITION_SIZE);
        jLabelPosition.setName("jLabelPosition"); // NOI18N

        jLabelStatus.setFont(STATUS_FONT); // NOI18N
        jLabelStatus.setForeground(LABEL_STATUS_FOREGROUND_COLOR);
        jLabelStatus.setText("Available");
        jLabelStatus.setDoubleBuffered(true);
        jLabelStatus.setMaximumSize(LABEL_STATUS_SIZE);
        jLabelStatus.setMinimumSize(LABEL_STATUS_SIZE);
        jLabelStatus.setPreferredSize(LABEL_STATUS_SIZE);
        jLabelStatus.setName("jLabelStatus"); // NOI18N
        
        screenSharingButton = EzuceButtonFactory.getInstance().createScreenSharingButton();
        screenSharingButton.setVisible(false);

        // main layout
        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(5)
        			.addComponent(imgPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(4)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabelPosition, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        				.addComponent(jLabelUserDisplayName, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(screenSharingButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
        		.addGroup(layout.createSequentialGroup()
        			.addGap(49)
        			.addComponent(jLabelStatus, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(4)
        					.addComponent(jLabelUserDisplayName)
        					.addGap(0)
        					.addComponent(jLabelPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(4)
        					.addComponent(imgPanel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(32)
        					.addComponent(jLabelStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(screenSharingButton)))
        			.addGap(0))
        );
        this.setLayout(layout);
        
        //We send the true presence of the jid instead of the MUC presence
        updateStatus(EzucePresenceManager.getPresence(userJid));
    }    
    
    public JButton getScreenSharingButton() {
		return screenSharingButton;
	}

	/**
     * Convert icon to image.
     */
    private BufferedImage iconToImage(Icon icon) throws HeadlessException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(icon.getIconWidth(), icon.getIconHeight());
        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

}
