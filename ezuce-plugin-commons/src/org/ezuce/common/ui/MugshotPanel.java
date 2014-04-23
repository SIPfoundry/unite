package org.ezuce.common.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.spark.PresenceManager;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.spark.util.ImageCombiner;

public class MugshotPanel extends JPanel {

	private static final long serialVersionUID = -9049772138996039114L;

    private final Image BLOCKED_IMAGE = SparkRes.getImageIcon("BLOCKED_USER").getImage();
    protected final Image ALERT_ONLINE_IMAGE = SparkRes.getImageIcon("ALERT_USER_ONLINE").getImage();
    protected final Image UNREAD_MESSAGES_IMAGE = SparkRes.getImageIcon("UNREAD_MESSAGES").getImage();
        
	private Presence presence;
	private Image background;
	private final boolean smallSize;
	private final int statusXOffset;
	private final int statusYOffset;
	private int mugshotXOffset;
	private int mugshotYOffset;
    private final int stateXOffset;
    private final int stateYOffset;
	private Image status;
	private Image mugshot;
    private Image state;
        
    /**
      * Represents an icon that is placed in bottom-left corner.
      * We will be able extend this State list with other states in the future.
    */
    public enum State { NONE, BLOCKED, ALERT_ONLINE, UNREAD_MESSAGES }

	public MugshotPanel(boolean smallSize) {
		final String resource = smallSize ? "resources/images/mini_avatar_bg.png"
				: "resources/images/avatar.png";
		final ImageIcon icon = new ImageIcon(getClass().getClassLoader()
				.getResource(resource));
		background = icon.getImage();
		setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		setPreferredSize(new Dimension(icon.getIconWidth(),
				icon.getIconHeight()));

		this.smallSize = smallSize;
		statusXOffset = (int) (icon.getIconWidth() * .067f);
		statusYOffset = (int) (icon.getIconHeight() * .9f);
                
        stateXOffset = 0;
        stateYOffset = (int) (icon.getIconHeight() * .10f);
                
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		if (status != null) {
			g.drawImage(status, 0, statusYOffset, this.getWidth(), status.getHeight(null), null);
		}
		if (mugshot != null) {
			g.drawImage(mugshot, mugshotXOffset, mugshotYOffset, null);
		}
		
        if (state != null){
        	
                g.drawImage(state, stateXOffset, stateYOffset, null);
        }
                    
	}

	public void setMugshot(ImageIcon mugshot) {                		

        this.mugshot = GraphicUtils.scale(mugshot, getWidth()-2, getHeight()-2).getImage();
                
		mugshotXOffset = (getWidth() - this.mugshot.getWidth(null)) / 2;
		// width again - we're looking at the upper square only
		mugshotYOffset = (getWidth() - this.mugshot.getHeight(null)) / 2;
		revalidate();
		repaint();
	}

	public void setPresence(Presence presence) {
		if (changedPresence(presence)) {
			this.presence = presence;

			status = getStatusImage();
			invalidate();
			repaint();
		}
	}

	private Image getStatusImage() {
		Image img = null;

		// invisible / offline - grey
		if (presence.getType() == Presence.Type.unavailable) {
			final String resource = smallSize ? "resources/images/mini_avatar_status_offline.png"
					: "resources/images/avatar_status_offline.png";
			img = new ImageIcon(getClass().getClassLoader().getResource(
					resource)).getImage();
			return img;
		}
		
		// available
		Mode presenceMode = presence.getMode();
		if (presenceMode == null)
			presenceMode = Presence.Mode.available;
		
		// brb, busy, away - orange
		if (EzucePresenceManager.isBrb(presence) || EzucePresenceManager.isBusy(presence) || presenceMode.equals(Presence.Mode.away)) {
			final String resource = smallSize ? "resources/images/mini_avatar_status_away.png" : "resources/images/avatar_status_away.png";
			img = new ImageIcon(getClass().getClassLoader().getResource(resource)).getImage();
        } 
		
		// in a meeting, off work, extended away - blue
		if (EzucePresenceManager.isInAMeeting(presence) || EzucePresenceManager.isOffWork(presence) || presenceMode.equals(Presence.Mode.xa)) {
        	final String resource = smallSize ? "resources/images/mini_avatar_status_extended.png" : "resources/images/avatar_status_extended.png";
			img = new ImageIcon(getClass().getClassLoader().getResource(resource)).getImage();
        } 
		
		// on the phone, dnd - red	
		if (EzucePresenceManager.isOnPhone(presence) || presenceMode == Presence.Mode.dnd){
        	final String resource = smallSize ? "resources/images/mini_avatar_status_busy.png" : "resources/images/avatar_status_busy.png";
			img = new ImageIcon(getClass().getClassLoader().getResource(resource)).getImage();
		} 
		
		// available, free to chat - green
		if (presenceMode.equals(Presence.Mode.available) || presenceMode.equals(Presence.Mode.chat)) {
			final String resource = smallSize ? "resources/images/mini_avatar_status_online.png" : "resources/images/avatar_status_online.png";
			img = new ImageIcon(getClass().getClassLoader().getResource(resource)).getImage();
        }
       
		return img;
	}
	
	private boolean changedPresence(Presence newPresence) {
		boolean changed;		
		if (presence == null) {
			changed = newPresence != null;
		} else if (newPresence == null) {
			changed = true;
		} else {
			changed = !presence.toString().equals(newPresence.toString());
		}		
		return changed;
	}

    public void updateStatus(Presence prsnc) {
        this.status = EzucePresenceUtils.getUpdatedUserStatusIconLarge(prsnc).getImage();
        this.repaint();
        this.revalidate();
    }
    
    /**
     * Change the icon in the bottom-left corner.
     */
    public void updateState(State s){
        switch (s){
            case BLOCKED:
                state = BLOCKED_IMAGE;
                break; 
            case ALERT_ONLINE:
            	state = ALERT_ONLINE_IMAGE;
            	break;
            case UNREAD_MESSAGES:
            	state = UNREAD_MESSAGES_IMAGE;
            	break;
            case NONE:
            default:
                state = null;
        }
        this.repaint();
        this.revalidate();
    }
    
     // UN-11
    public void updateState(Icon icon){
        state = icon != null ? ImageCombiner.iconToImage(icon) : null;
        this.repaint();
        this.revalidate();
    }

	public Image getMugshot() {
		return mugshot;
	}

	public void setMugshot(Image mugshot) {
		this.mugshot = mugshot;
	}

	public Image getStatus() {
		return status;
	}

	public void setStatus(Image status) {
		this.status = status;
	}

	public Image getState() {
		return state;
	}

	public void setState(Image state) {
		this.state = state;
	}
	
	public void setBackgroundImage(Image background) {
		this.background = background;
	}
	
	public Image getBackgroundImage() {
		return background;
	}	
}
