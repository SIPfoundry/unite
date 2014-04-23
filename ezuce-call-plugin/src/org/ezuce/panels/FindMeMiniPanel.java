package org.ezuce.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.call.CallPlugin;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.CallSequence;
import org.ezuce.common.rest.Ring;
import org.ezuce.common.ui.wrappers.AnyNumberWrapper;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.paints.Colors;
import org.ezuce.paints.GlossAngleGradientPainter;
import org.ezuce.paints.LinearGradientPainter;
import org.ezuce.popups.FrequencyPopup;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLabel.TextAlignment;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.MattePainter;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class FindMeMiniPanel extends JPanel implements DocumentListener, VCardLoaderCallback {
    private CounterJPanel counterJPanel1;
    private FrequencyPopup frequencyPopup;
    private JButton jButtonContact;
    private JButton jButtonDelete;
    private JButton jButtonNumber;
    private JButton jButtonWhen;
    private JLayeredPane jLayeredPane;
    private JLayeredPane jLayeredPaneIcon;
    private JPanel jPanelBkg;
    private JPanel jPanelPic;
    private JXLabel jXLabelBackground;
    private JXLabel jXLabelPic;
    private JXLabel jXLabelStatus;
    private JXPanel jXPanelBackground;
    private JXPanel jXPanelContent;
    private JXPanel jXPanelDetails;
    private JXPanel jXPanelGlass;
    private final MattePainter jxLabelStatusBackground=
                            LinearGradientPainter.jxLabelStatusBackgroundSmall();
    private ContactListEntry contact = null;
    private final Dimension iconDimension=new Dimension(38,38);
    private final Color picBorderColor=Colors.userMiniPanelIconBorderColor;
    private Ring ring;
    private boolean loggedUser=false;
    final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPanel2.class);

    /** Creates new form FindMeMiniPanel */
    public FindMeMiniPanel() {
        initComponents();
        ring=new Ring();

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
        setExpiration(resourceMap.getInteger("findMe.expiration.default.minutes"),
                           resourceMap.getInteger("findMe.expiration.default.seconds"));
        ring.setEnabled(Boolean.TRUE);
        ring.setType(resourceMap.getString("findMe.ringType.default"));
    }

	public FindMeMiniPanel(Ring ring) {
		initComponents();
		this.ring = ring;
		String localUser = Config.getInstance().getSipUserId();
		if (localUser.contains("@"))
			localUser = localUser.substring(0, localUser.indexOf("@"));
		if (ring.getNumber().equals(localUser)) {
			this.setLoggedUser(true);
		}
		customize();
		customizePresence();
		if (contact instanceof ContactItemWrapper && ((ContactItemWrapper) contact).getContactItem().getJID() != null) {
			AsyncLoader.getInstance().execute(((ContactItemWrapper) contact).getContactItem().getJID(), this);
		}
	}

	public final void customizePresence() {
		if (contact == null) {
			return;
		}
		if (contact instanceof ContactItemWrapper) {
			setUserPicture(contact.getImId());
			updateStatus(contact.getPresence());		
		} else {
            setDefaultUserPicture();
			setUserStatusIcon(Images.userStatusOffline);
			setUserStatusIcon(null);
		}
	}

    public void updateStatus(Presence presence) {
        this.setUserStatusIcon(EzucePresenceUtils.getUpdatedUserStatusIcon(presence));
    }

    /**
     * Obtains all necessary info from the Ring instance reference received as
     * an argument. In order to obtain the details of the user identified by the
     * phonebook extension, the component will perform an exact search based on
     * that extension.
     * @param ring
     */    
    private void customize() {
        //Get the ContactListEntry that matches the phone number from
        //the Ring instance reference. Then, customize this FindMeMiniPanel
        //by the ContactListEntry.
        ContactListEntry cle=null;
        String localUserId = Config.getInstance().getSipUserId();
        String number = ring.getNumber();
        if (number.equalsIgnoreCase(localUserId)) {//if this Ring is not for the current user, then search it on the server:
            final VCard currentVCard=SparkManager.getVCardManager().getVCard();
            setNameOrNumberText(String.format("%s %s %s",currentVCard.getFirstName(),
                                                  currentVCard.getMiddleName(),
                                                  currentVCard.getLastName()));
            String nr = Config.getInstance().getSipUserId();
            if (nr.contains("@")) { 
            	nr=nr.substring(0,nr.indexOf("@"));
            }
            setNumber(nr);
            ResourceMap rm = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
            setExpiration(rm.getInteger("findMe.expiration.default.minutes"), rm.getInteger("findMe.expiration.default.seconds"));
            setUserPicture(currentVCard);                    
        } else {
        	ContactList contactList = SparkManager.getWorkspace().getContactList();
        	ContactItem contactItem = Utils.getContactItemWithExtension(number);
        	if (contactItem != null) {
        		cle = new ContactItemWrapper(contactItem, 
    					contactList.getContactGroup(contactItem.getGroupName()));
        	} else {
        		cle = new AnyNumberWrapper();
        		((AnyNumberWrapper)cle).setNumber(number);
        	}        				
        }
        contact = cle;
        
        //------------------- START BUILDING THE UI: -------------------------
        setNameOrNumberText(cle.getUserDisplayName());
        setDescriptionText(cle.getDescription());
        setNumber(cle.getNumber());              
        int mins=ring.getExpiration() / 60;
        int secs=ring.getExpiration() % 60;
        setExpiration(mins, secs);              
    }

    public JLayeredPane getIconPane() {
        return this.jLayeredPane;
    }

    public JPanel getIconPanel() {
        return this.jPanelPic;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        frequencyPopup = new FrequencyPopup();
        jPanelBkg = new JPanel();
        jLayeredPane = new JLayeredPane();
        jXPanelContent = new JXPanel();
        jLayeredPaneIcon = new JLayeredPane();
        jXPanelGlass = new JXPanel();
        jPanelPic = new JPanel();
        jXLabelPic = new JXLabel();
        jXLabelStatus = new JXLabel();
        jXPanelDetails = new JXPanel();
        jButtonContact = new JButton();
        jButtonNumber = new JButton();
        jButtonWhen = new JButton();
        jButtonDelete = new JButton();
        counterJPanel1 = new CounterJPanel();
        counterJPanel1.addDocumentListener(this);
        
        jXPanelBackground = new JXPanel();
        jXLabelBackground = new JXLabel();

        frequencyPopup.setName("frequencyPopup"); // NOI18N

        setMaximumSize(new Dimension(291, 50));
        setMinimumSize(new Dimension(291, 50));
        setPreferredSize(new Dimension(291, 50));

        jPanelBkg.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanelBkg.setName("jPanelBkg"); // NOI18N
        jPanelBkg.setPreferredSize(new Dimension(292, 50));

        jLayeredPane.setDoubleBuffered(true);
        jLayeredPane.setMaximumSize(new Dimension(291, 49));
        jLayeredPane.setMinimumSize(new Dimension(291, 49));
        jLayeredPane.setName("jLayeredPane"); // NOI18N

        jXPanelContent.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        jXPanelContent.setOpaque(false);
        jXPanelContent.setMaximumSize(new Dimension(291, 49));
        jXPanelContent.setMinimumSize(new Dimension(291, 49));
        jXPanelContent.setName("jXPanelContent"); // NOI18N
        jXPanelContent.setPreferredSize(new Dimension(291, 49));

        jLayeredPaneIcon.setMaximumSize(new Dimension(40, 45));
        jLayeredPaneIcon.setMinimumSize(new Dimension(40, 45));
        jLayeredPaneIcon.setName("jLayeredPaneIcon"); // NOI18N

        jXPanelGlass.setAlpha(0.6F);
        jXPanelGlass.setBackgroundPainter(GlossAngleGradientPainter.painter());
        jXPanelGlass.setMaximumSize(new Dimension(40, 45));
        jXPanelGlass.setMinimumSize(new Dimension(40, 45));
        jXPanelGlass.setName("jXPanelGlass"); // NOI18N

        GroupLayout jXPanelGlassLayout = new GroupLayout(jXPanelGlass);
        jXPanelGlass.setLayout(jXPanelGlassLayout);
        jXPanelGlassLayout.setHorizontalGroup(
            jXPanelGlassLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jXPanelGlassLayout.setVerticalGroup(
            jXPanelGlassLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jXPanelGlass.setBounds(0, 0, 40, 45);
        jLayeredPaneIcon.add(jXPanelGlass, JLayeredPane.DEFAULT_LAYER);

        jPanelPic.setBorder(BorderFactory.createLineBorder(new Color(177, 177, 177)));
        jPanelPic.setName("jPanelPic"); // NOI18N
        jPanelPic.setLayout(new BorderLayout());

        jXLabelPic.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, picBorderColor));
        jXLabelPic.setHorizontalAlignment(SwingConstants.CENTER);
        jXLabelPic.setIcon(Images.userGreenSmall);
        jXLabelPic.setAlignmentX(0.5F);
        jXLabelPic.setDoubleBuffered(true);
        jXLabelPic.setIconTextGap(0);
        jXLabelPic.setName("jXLabelPic"); // NOI18N
        jXLabelPic.setOpaque(true);
        jXLabelPic.setTextAlignment(TextAlignment.CENTER);
        jPanelPic.add(jXLabelPic, BorderLayout.CENTER);

        jXLabelStatus.setBackground(new Color(204, 204, 204));
        jXLabelStatus.setBackgroundPainter(this.jxLabelStatusBackground);
        jXLabelStatus.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        jXLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
        jXLabelStatus.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/panels/resources/availableIconSmall.png"))); // NOI18N
        jXLabelStatus.setDoubleBuffered(true);
        jXLabelStatus.setName("jXLabelStatus"); // NOI18N
        jXLabelStatus.setOpaque(true);
        jPanelPic.add(jXLabelStatus, BorderLayout.PAGE_END);

        jPanelPic.setBounds(0, 0, 40, 45);
        jLayeredPaneIcon.add(jPanelPic, JLayeredPane.DEFAULT_LAYER);

        jXPanelDetails.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        jXPanelDetails.setOpaque(false);
        jXPanelDetails.setMaximumSize(new Dimension(189, 48));
        jXPanelDetails.setMinimumSize(new Dimension(189, 48));
        jXPanelDetails.setPreferredSize(new Dimension(189, 48));
        jXPanelDetails.setName("jXPanelDetails"); // NOI18N
        jXPanelDetails.setLayout(new VerticalLayout());

        jButtonContact.setFont(new Font("Arial", 1, 14));
        jButtonContact.setForeground(new Color(46, 85, 102));
        jButtonContact.setText("Contact");
        jButtonContact.setAlignmentY(0.0F);
        jButtonContact.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        jButtonContact.setBorderPainted(true);
        jButtonContact.setContentAreaFilled(false);
        jButtonContact.setDoubleBuffered(true);
        jButtonContact.setFocusPainted(false);
        jButtonContact.setHideActionText(true);
        jButtonContact.setHorizontalAlignment(SwingConstants.LEADING);
        jButtonContact.setHorizontalTextPosition(SwingConstants.LEADING);
        jButtonContact.setMargin(new Insets(2, 14, 0, 14));
        jButtonContact.setName("jButtonContact"); // NOI18N
        jXPanelDetails.add(jButtonContact);

        jButtonNumber.setFont(new Font("Arial", 0, 12));
        jButtonNumber.setForeground(new Color(143, 143, 143));

        jButtonNumber.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        jButtonNumber.setBorderPainted(true);
        jButtonNumber.setContentAreaFilled(false);
        jButtonNumber.setDoubleBuffered(true);
        jButtonNumber.setFocusPainted(false);
        jButtonNumber.setHideActionText(true);
        jButtonNumber.setHorizontalAlignment(SwingConstants.LEADING);
        jButtonNumber.setHorizontalTextPosition(SwingConstants.LEADING);
        jButtonNumber.setName("jButtonNumber"); // NOI18N
        jXPanelDetails.add(jButtonNumber);

        jButtonWhen.setFont(new Font("Arial", 0, 11));
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(FindMeMiniPanel.class);
        jButtonWhen.setIcon(resourceMap.getIcon("jButtonWhen.icon")); // NOI18N
        jButtonWhen.setText(Res.getString("findme.followme.always"));
        jButtonWhen.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        jButtonWhen.setBorderPainted(true);
        jButtonWhen.setComponentPopupMenu(frequencyPopup);
        jButtonWhen.setContentAreaFilled(false);
        jButtonWhen.setDoubleBuffered(true);
        jButtonWhen.setFocusPainted(false);
        jButtonWhen.setForeground(new Color(153, 153, 153));
        jButtonWhen.setHideActionText(true);
        jButtonWhen.setHorizontalAlignment(SwingConstants.LEADING);
        jButtonWhen.setHorizontalTextPosition(SwingConstants.LEADING);
        jButtonWhen.setName("jButtonWhen"); // NOI18N
        jButtonWhen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonWhenActionPerformed(evt);
            }
        });
        jXPanelDetails.add(jButtonWhen);

        jButtonDelete.setIcon(resourceMap.getIcon("jButtonDelete.icon")); // NOI18N
        jButtonDelete.setBorder(null);
        jButtonDelete.setBorderPainted(false);
        jButtonDelete.setContentAreaFilled(false);
        jButtonDelete.setDoubleBuffered(true);
        jButtonDelete.setFocusPainted(false);
        jButtonDelete.setHideActionText(true);
        jButtonDelete.setMaximumSize(new Dimension(23, 23));
        jButtonDelete.setMinimumSize(new Dimension(23, 23));
        jButtonDelete.setName("jButtonDelete"); // NOI18N
        jButtonDelete.setPreferredSize(new Dimension(23, 23));
        jButtonDelete.setRolloverIcon(resourceMap.getIcon("jButtonDelete.rolloverIcon")); // NOI18N
        jButtonDelete.setVerticalAlignment(SwingConstants.BOTTOM);

        counterJPanel1.setName("counterJPanel1"); // NOI18N

        GroupLayout jXPanelContentLayout = new GroupLayout(jXPanelContent);
        jXPanelContent.setLayout(jXPanelContentLayout);
        jXPanelContentLayout.setHorizontalGroup(
            jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelContentLayout.createSequentialGroup()
                .addContainerGap(221, Short.MAX_VALUE)
                .addGroup(jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jButtonDelete, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(counterJPanel1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)))
            .addGroup(jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jXPanelContentLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jXPanelDetails, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(73, Short.MAX_VALUE)))
            .addGroup(jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jXPanelContentLayout.createSequentialGroup()
                    .addComponent(jLayeredPaneIcon, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(247, Short.MAX_VALUE)))
        );
        jXPanelContentLayout.setVerticalGroup(
            jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelContentLayout.createSequentialGroup()
                .addComponent(jButtonDelete, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(counterJPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jXPanelContentLayout.createSequentialGroup()
                    .addComponent(jXPanelDetails, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jXPanelContentLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(jXPanelContentLayout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(jLayeredPaneIcon, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jXPanelContent.setBounds(0, 0, 291, 49);
        jLayeredPane.add(jXPanelContent, JLayeredPane.DEFAULT_LAYER);

        jXPanelBackground.setInheritAlpha(false);
        jXPanelBackground.setMaximumSize(new Dimension(291, 49));
        jXPanelBackground.setMinimumSize(new Dimension(291, 49));
        jXPanelBackground.setName("jXPanelBackground"); // NOI18N
        jXPanelBackground.setPreferredSize(new Dimension(291, 49));

        jXLabelBackground.setIcon(resourceMap.getIcon("jXLabelBackground.icon")); // NOI18N
        jXLabelBackground.setName("jXLabelBackground"); // NOI18N

        GroupLayout jXPanelBackgroundLayout = new GroupLayout(jXPanelBackground);
        jXPanelBackground.setLayout(jXPanelBackgroundLayout);
        jXPanelBackgroundLayout.setHorizontalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelBackgroundLayout.createSequentialGroup()
                .addComponent(jXLabelBackground, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jXPanelBackgroundLayout.setVerticalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelBackgroundLayout.createSequentialGroup()
                .addComponent(jXLabelBackground, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXPanelBackground.setBounds(0, 0, 291, 49);
        jLayeredPane.add(jXPanelBackground, JLayeredPane.DEFAULT_LAYER);

        GroupLayout jPanelBkgLayout = new GroupLayout(jPanelBkg);
        jPanelBkg.setLayout(jPanelBkgLayout);
        jPanelBkgLayout.setHorizontalGroup(
            jPanelBkgLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelBkgLayout.createSequentialGroup()
                .addComponent(jLayeredPane, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelBkgLayout.setVerticalGroup(
            jPanelBkgLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanelBkgLayout.createSequentialGroup()
                .addComponent(jLayeredPane, GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addContainerGap())
        );

        //this.setBackgroundPainter(jXPanelBackgroundPainter);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelBkg, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelBkg, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
    }//GEN-END:initComponents

    private void jButtonWhenActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonWhenActionPerformed
        jButtonWhen.getComponentPopupMenu().show(jButtonWhen, 0, 0);
    }//GEN-LAST:event_jButtonWhenActionPerformed

    /**
     * Set the picture of the user to display.
     * @param picture
     */
    public void setUserPicture(String imId) {
    	if (StringUtils.isEmpty(imId)) {
    		return;
    	}
    	String bareAddress = Utils.getJidFromId(imId);
		VCard vCard = SparkManager.getVCardManager().getVCard(bareAddress, true);
		if (vCard == null) {
			vCard = SparkManager.getVCardManager().reloadVCard(bareAddress);
		}
		setUserPicture(vCard);
    }
    
    public void setUserPicture(VCard vCard) {
		jXLabelPic.setIcon(Images.ensureSizeScaling(Utils.retrieveAvatar(vCard),
				iconDimension.width, iconDimension.height, jXLabelPic));
    }
    
    public void setDefaultUserPicture() {
		jXLabelPic.setIcon(Images.ensureSizeScaling(Utils.getDefaultAvatar(),
				iconDimension.width, iconDimension.height, jXLabelPic));
    }
    
    public void setUserStatusIcon(final Icon picture) {
        this.jXLabelStatus.setIcon(picture);
    }

    /**
     * Sets the name or number of the user, which will be displayed at the right
     * of the avatar, at the top.
     * @param nameOrNumber
     */
    private void setNameOrNumberText(String nameOrNumber) {
        jButtonContact.setText(nameOrNumber != null ? nameOrNumber : "");
    }

    /**
     * Sets the number of the user, which will be displayed at the right of the
     * avatar, below the name of the user.
     * @param number
     */
    private void setDescriptionText(String description) {
        jButtonNumber.setText(description != null ? description : "Unknown");
    }

    /**
     * Sets the number that will be saved in callforwarding scheme
     * @param number
     */
    public void setNumber(String number) {
        if (number==null || number.trim().length()==0) {
            ring.setNumber(this.jButtonContact.getText());
        }
        else {
            ring.setNumber(number);
        }
    }

    public JButton getDeleteButton() {
        return jButtonDelete;
    }

    public JButton getContactButton() {
        return jButtonContact;
    }

    public JButton getNumberButton() {
        return jButtonNumber;
    }

    public JButton getWhenButton() {
        return jButtonWhen;
    }

    public Ring getRing() {
        return this.ring;
    }

    public void setRing(Ring r) {
        this.ring=r;
    }

    public CounterJPanel getCounterJPanel() {
        return this.counterJPanel1;
    }

    public void setExpiration(int minutes, int seconds) {
        this.counterJPanel1.setMinutes(minutes);
        this.counterJPanel1.setSeconds(seconds);
        this.ring.setExpiration(minutes*60+seconds);
    }

    public int[] getExpiration() {
        return new int[]{this.counterJPanel1.getMinutes(),this.counterJPanel1.getSeconds()};
    }

    public static FindMeMiniPanel createMiniPanel(ContactListEntry cle) {
        FindMeMiniPanel fmmp=new FindMeMiniPanel();
        
        fmmp.setNameOrNumberText(cle.getUserDisplayName());
        fmmp.setDescriptionText(cle.getDescription());
        
        fmmp.setNumber(cle.getNumber());
        
        String localUser = Config.getInstance().getSipUserId();
        if (localUser.contains("@")) {
        	localUser=localUser.substring(0,localUser.indexOf("@"));
        }       
        if (cle.getNumber().equals(localUser)) {
            fmmp.setLoggedUser(true);
        }
        fmmp.contact = cle;
        if (fmmp.contact instanceof ContactItemWrapper) {
            fmmp.setUserPicture(cle.getImId());
            fmmp.updateStatus(cle.getPresence());
        }
        else {
            fmmp.setDefaultUserPicture();
            fmmp.setUserStatusIcon(null);
        }             
        return fmmp;
    }

    public static FindMeMiniPanel createPanelFromCurrentUser(final CallSequence callSequence) {
        System.out.format("\nCreating FindMeMiniPanel from local user...");
        FindMeMiniPanel fmmp=new FindMeMiniPanel();
        final VCard currentVCard=SparkManager.getVCardManager().getVCard();
        fmmp.setNameOrNumberText(String.format("%s %s %s",currentVCard.getFirstName(),
                                              currentVCard.getMiddleName(),
                                              currentVCard.getLastName()));
        String nr = Config.getInstance().getSipUserId();
        if (nr.contains("@")) nr=nr.substring(0,nr.indexOf("@"));
        fmmp.getDeleteButton().setIcon(null);
        fmmp.getDeleteButton().setRolloverIcon(null);
        fmmp.getDeleteButton().setPressedIcon(null);
        fmmp.getDeleteButton().setAction(null);
        fmmp.setNumber(nr);
        int expMins=callSequence.getExpiration()/60;
        int expSecs=callSequence.getExpiration()%60;
        fmmp.setExpiration(expMins,expSecs);
        fmmp.setLoggedUser(true);

        try {
            if (currentVCard.getAvatar()!=null && currentVCard.getAvatar().length > 0) {
                fmmp.setUserPicture(currentVCard);
            }
        }
        catch(Exception e) {
            Log.error(e);
        }

        return fmmp;
    }


    public static FindMeMiniPanel createVoiceMailPanel(final CallSequence callSequence) {
        System.out.format("\nCreating FindMeMiniPanel for Voice mail...");
        FindMeMiniPanel fmmp=new FindMeMiniPanel();
        fmmp.setNameOrNumberText("   ");
        fmmp.setNumber("   ");
        fmmp.getDeleteButton().setVisible(false);
        fmmp.getWhenButton().setVisible(false);
        fmmp.getCounterJPanel().setVisible(false);
        final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
        fmmp.setExpiration(0,0);
        try {
            final JPanel iconPanel=fmmp.getIconPanel();
            iconPanel.removeAll();

            final JToggleButton vmIconBtn=new JToggleButton();

            vmIconBtn.setIcon(resourceMap.getImageIcon("findMe.voiceMail.icon.yellow"));
            vmIconBtn.setPressedIcon(resourceMap.getImageIcon("findMe.voiceMail.icon.yellow"));
            vmIconBtn.setSelectedIcon(resourceMap.getImageIcon("findMe.voiceMail.icon.yellow"));
            vmIconBtn.setFocusPainted(false);
            vmIconBtn.setContentAreaFilled(false);
            vmIconBtn.setBorder(null);
            vmIconBtn.setBorderPainted(false);

            vmIconBtn.setSelected(true);

            vmIconBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //callSequence.setWithVoiceMail(vmIconBtn.isSelected());
                    callSequence.setWithVoiceMail(true);
                }
            });

            iconPanel.add(vmIconBtn,BorderLayout.CENTER);
        }
        catch(Exception e){
            Log.error(e);
        }

        return fmmp;
    }

    public boolean isLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(boolean loggedUser) {
        this.loggedUser = loggedUser;
    }

    public ContactListEntry getContact() {
        return contact;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
    	int[] exp=this.getExpiration();
    	this.ring.setExpiration(exp[0]*60 + exp[1]);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

	@Override
	public void vcardLoaded(VCard vCard) {
		customize();
		customizePresence();
	}
}
