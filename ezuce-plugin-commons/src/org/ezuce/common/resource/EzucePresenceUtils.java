package org.ezuce.common.resource;

import javax.swing.ImageIcon;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import org.ezuce.common.ui.panels.Images;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.SparkManager;

public class EzucePresenceUtils {
	
	public static void registerRosterListener(RosterListener listener) {
		final XMPPConnection con = SparkManager.getConnection();
		final Roster roster = con.getRoster();
		roster.addRosterListener(listener);
	}

	public static void unregisterRosterListener(RosterListener listener) {
		final XMPPConnection con = SparkManager.getConnection();
		final Roster roster = con.getRoster();
		roster.removeRosterListener(listener);
	}
	
	public static String getUpdatedUserStatus(Presence presence) {
		String status = Res.getString("status.offline");
		if (presence == null) {
			return status;
		}
		if (presence.isAvailable()) {
			status = Res.getString("status.online");
		}
        if (isEmpty(presence.getStatus())) {
            Presence.Mode mode = presence.getMode();
            if (mode == Presence.Mode.available) {
                status = Res.getString("status.online");
            }
            else if (mode == Presence.Mode.away) {
                status = Res.getString("status.away");                
            }
            else if (mode == Presence.Mode.chat) {
                status = Res.getString("status.free.to.chat");
            }
            else if (mode == Presence.Mode.dnd) {
                status = Res.getString("status.do.not.disturb");                
            }
            else if (mode == Presence.Mode.xa) {
                status = Res.getString("status.extended.away");                
            }           
        } else {
        	status = presence.getStatus();
        }
        
		return status;
	}
	
    public static ImageIcon getUpdatedUserStatusIcon(Presence presence) {
    	if (presence == null) {
    		return Images.userStatusOffline;
    	}
        ImageIcon statusIcon = Images.userStatusAvailable;       
        if (presence.isAvailable()) {
            Presence.Mode mode = presence.getMode();
            if (mode == Presence.Mode.away || mode == Presence.Mode.xa) {
                statusIcon = Images.userStatusAway;
            }
            else if (mode == Presence.Mode.dnd) {
                statusIcon = Images.userStatusDnd;
            }
        } else {
        	statusIcon = Images.userStatusOffline;
        }
        return statusIcon;
    }	
    
    public static ImageIcon getUpdatedUserStatusIconLarge(Presence presence) {
    	if (presence == null) {
    		return Images.userStatusOfflineLarge;
    	}
        ImageIcon statusIcon = Images.userStatusAvailableLarge;       
        if (presence.isAvailable()) {
            Presence.Mode mode = presence.getMode();
            if (mode == Presence.Mode.away || mode == Presence.Mode.xa) {
                statusIcon = Images.userStatusAwayLarge;
            }
            else if (mode == Presence.Mode.dnd) {
                statusIcon = Images.userStatusDndLarge;
            }
        } else {
        	statusIcon = Images.userStatusOfflineLarge;
        }
        return statusIcon;
    }

}
