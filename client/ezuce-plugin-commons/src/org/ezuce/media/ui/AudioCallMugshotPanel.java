package org.ezuce.media.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.Beans;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.spark.util.ImageCombiner;

public class AudioCallMugshotPanel extends JPanel {

	private static final long serialVersionUID = -9049772138996039114L;

        private final Image BLOCKED_IMAGE = (Beans.isDesignTime())?null:SparkRes.getImageIcon(SparkRes.SMALL_CIRCLE_DELETE).getImage();
        protected Dimension defaultSize = new Dimension(46,53);
	private Presence presence;
	private Image background;
	private int statusXOffset;
	private int statusYOffset;
	private int mugshotXOffset;
	private int mugshotYOffset;
        private int stateXOffset;
        private int stateYOffset;
	private Image status;
	private Image mugshot;
        private Image state;
        
        /**
         * Represents an icon that is placed in bottom-left corner.
         * We will be able extend this State list with other states in the future.
         */
        public enum State { NONE, BLOCKED }

	public AudioCallMugshotPanel() {

            final String resource =  "/resources/images/avatar-call_frame.png";
            final ImageIcon icon = new ImageIcon(getClass().getResource(resource));
            background = icon.getImage();
            setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
            setPreferredSize(new Dimension(icon.getIconWidth(),
                            icon.getIconHeight()));

            statusXOffset = (int) (icon.getIconWidth() * .67f);
            statusYOffset = (int) (icon.getIconHeight() * .9f);

            stateXOffset = 0;
            stateYOffset = (int) (icon.getIconHeight() * .65f);            
            setMugshot(new ImageIcon(getClass().getResource("/resources/images/unknown_user.png")));
	}

	@Override
	protected void paintComponent(Graphics g) {
//            if (!Beans.isDesignTime())
//            {              
		g.drawImage(background, 0, 0, null);
		if (status != null) {
			g.drawImage(status, statusXOffset, statusYOffset, null);
		}
		if (mugshot != null) {
			g.drawImage(mugshot, mugshotXOffset, mugshotYOffset, null);
		}
                if (state != null){
                        g.drawImage(state, stateXOffset, stateYOffset, null);
                }
//            }
//            else
//            {
//                super.paintComponent(g);
//            }
	}

	public void setMugshot(ImageIcon mugshot) {                		
            if (mugshot!=null){
                this.mugshot = GraphicUtils.scale(mugshot, getWidth()-2, getHeight()-2).getImage();
                
		mugshotXOffset = (getWidth() - this.mugshot.getWidth(null)) / 2;
		// width again - we're looking at the upper square only
		mugshotYOffset = (getWidth() - this.mugshot.getHeight(null)) / 2;
		revalidate();
		repaint();
            }
            else{
                this.mugshot=null;
                repaint();
            }
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

		if (presence.getType() == Presence.Type.available) {
			if (presence.getMode() == Presence.Mode.away
					|| presence.getMode() == Presence.Mode.xa) {
				final String resource = "resources/images/mini_avatar_status_away.png";
				img = new ImageIcon(getClass().getClassLoader().getResource(
						resource)).getImage();
			} else if (presence.getMode() == Presence.Mode.dnd) {
				final String resource = "resources/images/mini_avatar_status_busy.png";
				img = new ImageIcon(getClass().getClassLoader().getResource(
						resource)).getImage();
			} else {
				final String resource = "resources/images/mini_avatar_status_online.png";
				img = new ImageIcon(getClass().getClassLoader().getResource(
						resource)).getImage();
			}
		} else {
			final String resource = "resources/images/mini_avatar_status_offline.png";
			img = new ImageIcon(getClass().getClassLoader().getResource(
					resource)).getImage();
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
}
