/**
 * Ezuce 2011
 */
package org.ezuce.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.ComponentFactory;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.MugshotPanel;
import org.ezuce.commons.ui.location.LocationManager;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceContactItem extends ContactItem implements VCardLoaderCallback {
	
	private static final long serialVersionUID = -3338168877828434400L;
	private static final Color DESCRIPTION_COLOR = new Color(46, 85, 102);

	private final JLabel statusLabel;
	private final MugshotPanel imgPanel;
	private final JLabel locationLabel;	

	/** Creates new form EzuceContactItem */
	public EzuceContactItem(String alias, String nickname, String fullyQualifiedJID) {
		super(alias, nickname, fullyQualifiedJID, false);

		imgPanel = new MugshotPanel(true);
		
		EzuceLocalPreferences pref = (EzuceLocalPreferences) SettingsManager.getLocalPreferences();
		
	    int fontSize = pref.getContactListFontSize();
	    Font font = new Font(pref.getContactListFontName(), Font.PLAIN, fontSize);
	    
		JLabel displayNameLabel = ComponentFactory.createNameLabel();
		displayNameLabel.setFont(font);

		setDisplayNameLabel(displayNameLabel);		
		
		JLabel descriptionLabel = ComponentFactory.createPositionLabel();
		descriptionLabel.setFont(font);
		setDescriptionLabel(descriptionLabel);

		statusLabel = ComponentFactory.createBottomLabel();
		locationLabel = ComponentFactory.createBottomLabel();
		
		statusLabel.setFont(font);
		locationLabel.setFont(font);
		
		setSpecialImageLabel(new JLabel());
		initComponents();
		initLayout();
	
	}
	

	@Override
	protected void updateAvatar() {

	}

	private void initComponents() {
		Dimension mugshotSize = new Dimension(40, 45);
		imgPanel.setMinimumSize(mugshotSize);
		//imgPanel.setPreferredSize(mugshotSize);
	//	imgPanel.setMaximumSize(mugshotSize);
		
		setDisplayName();
		AsyncLoader.getInstance().execute(getJID(), this);		
	}

	private void initLayout() {
		removeAll();
		setLayout(new GridBagLayout());
		
		// info
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setOpaque(false);
		infoPanel.add(getDisplayNameLabel(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 	new Insets(0, 1, 0, 1), 0, 0));
		infoPanel.add(getDescriptionLabel(), new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 	new Insets(1, 1, 0, 1), 0, 0));
		infoPanel.add(statusLabel, 			 new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 	new Insets(1, 1, 0, 1), 0, 0));
		infoPanel.add(locationLabel, 		 new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 	new Insets(1, 1, 0, 1), 0, 0));
		
		add(imgPanel, 						 new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, 	new Insets(5, 9, 0, 9), 0, 0));
		add(infoPanel, 						 new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 	new Insets(0, 0, 0, 0), 0, 0));
	}

	public Dimension getAvatarContainerSize(){
		return this.imgPanel.getSize();
	}
	
	@Override
	public void setIcon(Icon icon) {
		// just override default behaviour
	}

	@Override
	public void setSideIcon(Icon icon) {
	}
        
	@Override
	public void setSpecialIcon(Icon icon) {
		imgPanel.updateState(icon);
	}
    
	/**
	 * Shows that the user is coming online.
	 */
	@Override
	public void showUserComingOnline() {
		// Change Font
		getNicknameLabel().setForeground(new Color(255, 128, 0));
	}

	/**
	 * Shows that the user is going offline.
	 */
	@Override
	public void showUserGoingOfflineOnline() {
		// Change Font
		getNicknameLabel().setForeground(Color.red);
	}

	@Override
	public void updatePresenceIcon(Presence p) {
        ChatManager chatManager = SparkManager.getChatManager();
        boolean handled = chatManager.fireContactItemPresenceChanged(this, p);
        if (handled) {
            return;
        }

        String status = getStatusByPresence(p);
		statusLabel.setText(status);
		imgPanel.setPresence(p);
		getDisplayNameLabel().setForeground(DESCRIPTION_COLOR);
		setStatus(status);
	}

	@Override
	public void updateAvatarInSideIcon() {
		try {
			final URL url = getAvatarURL();
			if (url != null) {
				imgPanel.setMugshot(new ImageIcon(url));
			}
		} catch (MalformedURLException e) {
			Log.error(e);
		}
		
	}
	
	@Override
	public void setPresence(Presence presence) {
        this.presence = presence;
        final PacketExtension packetExtension = presence.getExtension("x", "vcard-temp:x:update");
        if (packetExtension != null && packetExtension instanceof DefaultPacketExtension) {
            DefaultPacketExtension o = (DefaultPacketExtension)packetExtension;
            String hash = o.getValue("photo");
            if (hash != null) {
                this.hash = hash;
            }
        }
        updatePresenceIcon(presence);
    	setLocationName(LocationManager.getLocationNameFromPresence(presence));
	}
	
	private void setLocationName(String name) {
		locationLabel.setText(name);
		if (!StringUtils.isEmpty(name))
			((EzuceContactList) SparkManager.getContactList()).updateListInterval();
	}

	@Override
	public void vcardLoaded(VCard vCard) {
		if (vCard.getError() != null) {
			Log.error("VCard error: " + vCard.getError());
		}
		ImageIcon mugshot = Utils.retrieveAvatar(vCard);
		imgPanel.setMugshot(mugshot);
		getDescriptionLabel().setText(vCard.getField("TITLE"));
		String alias = Utils.getAlias(vCard);
		if (alias != null) {
			setAlias(alias);
		}
		String nickName = vCard.getNickName();
		if (nickName != null) {
			setNickname(nickName);
		}				
		Workspace.getInstance().getWorkspacePane().revalidate();
		Workspace.getInstance().getWorkspacePane().repaint();
	}
	
	private String getStatusByPresence(Presence p) {
		String status = p.getStatus();
        if ((status == null || status.isEmpty())  && p.isAvailable()) {
            Presence.Mode mode = p.getMode();
			if (mode == Presence.Mode.available) {
				status = Res.getString("status.online");
			} else if (mode == Presence.Mode.away) {
				status = Res.getString("status.away");
			} else if (mode == Presence.Mode.chat) {
				status = Res.getString("status.free.to.chat");
			} else if (mode == Presence.Mode.dnd) {
				status = Res.getString("status.do.not.disturb");
			} else if (mode == Presence.Mode.xa) {
				status = Res.getString("status.extended.away");
			}
        } else if ((status == null || status.isEmpty()) && p.isAway()) {
        	status = Res.getString("status.away");
        } else if ((status == null || status.isEmpty()) && !p.isAvailable()) {
        	RosterEntry entry = SparkManager.getConnection().getRoster().getEntry(getJID());
			if (entry != null
					&& (entry.getType() == RosterPacket.ItemType.none || entry.getType() == RosterPacket.ItemType.from)
					&& RosterPacket.ItemStatus.SUBSCRIPTION_PENDING == entry.getStatus()) {
				status = Res.getString("status.pending");
			}
        }

		if (p.getType() == Presence.Type.available) {
			if (status == null || status.isEmpty()) {
				status = Res.getString("status.online");
			}
		} else {
			status = Res.getString("status.offline");
		}
		return status;
	}
	
	public JLabel getStatusLabel() {
		return statusLabel;
	}
	
	public JLabel getLocationLabel() {
		return locationLabel;
	}

	public MugshotPanel getImgPanel() {
		return imgPanel;
	}
}
