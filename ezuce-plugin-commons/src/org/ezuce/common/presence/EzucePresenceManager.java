package org.ezuce.common.presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;

import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.spark.SparkManager;

public class EzucePresenceManager {

	private static final List<Presence> gPresences = new ArrayList<Presence>();

	// Add Available Presence
	private static final Presence availablePresence = new Presence(
			Presence.Type.available, Res.getString("status.online"), 1,
			Presence.Mode.available);
	private static final Presence freeToChatPresence = new Presence(
			Presence.Type.available, Res.getString("status.free.to.chat"), 1,
			Presence.Mode.chat);
	private static final Presence awayPresence = new Presence(
			Presence.Type.available, Res.getString("status.away"), 0,
			Presence.Mode.away);
	private static final Presence phonePresence = new Presence(
			Presence.Type.available, Res.getString("status.on.phone"), 0,
			Presence.Mode.dnd);
	private static final Presence dndPresence = new Presence(
			Presence.Type.available, Res.getString("status.do.not.disturb"), 0,
			Presence.Mode.dnd);
	private static final Presence extendedAway = new Presence(
			Presence.Type.available, Res.getString("status.extended.away"), 0,
			Presence.Mode.xa);

	private static final Presence brbPresence = new Presence(
			Presence.Type.available, Res.getString("status.brb"), 0,
			Presence.Mode.away);
	private static final Presence busyPresence = new Presence(
			Presence.Type.available, Res.getString("status.busy"), 0,
			Presence.Mode.away);
	private static final Presence inMeeting = new Presence(
			Presence.Type.available, Res.getString("status.in.a.meeting"), 0,
			Presence.Mode.xa);
	private static final Presence offWork = new Presence(
			Presence.Type.available, Res.getString("status.off.work"), 0,
			Presence.Mode.xa);

	// Smack doesn't have Invisible presence. We use available type + invisible
	// status.
	private static final Presence invisible = new Presence(
			Presence.Type.unavailable, Res.getString("status.invisible"), 0,
			Presence.Mode.available);

	static {

		gPresences.add(availablePresence);
		gPresences.add(freeToChatPresence);
		gPresences.add(awayPresence);
		gPresences.add(brbPresence);
		gPresences.add(busyPresence);
		gPresences.add(dndPresence);
		gPresences.add(phonePresence);
		gPresences.add(extendedAway);
		gPresences.add(inMeeting);
		gPresences.add(offWork);
		gPresences.add(invisible);
	}

	/**
	 * Returns true if the user is online.
	 * 
	 * @param jid
	 *            the jid of the user.
	 * @return true if online.
	 */
	public static boolean isOnline(String jid) {
		final Roster roster = SparkManager.getConnection().getRoster();
		Presence presence = roster.getPresence(jid);
		return presence.isAvailable();
	}

	/**
	 * Returns true if the user is online and their status is available or free
	 * to chat.
	 * 
	 * @param jid
	 *            the jid of the user.
	 * @return true if the user is online and available.
	 */
	public static boolean isAvailable(String jid) {
		final Roster roster = SparkManager.getConnection().getRoster();
		Presence presence = roster.getPresence(jid);
		return presence.isAvailable() && !presence.isAway();
	}

	/**
	 * Returns true if the user is online and their mode is available or free to
	 * chat.
	 * 
	 * @param presence
	 *            the users presence.
	 * @return true if the user is online and their mode is available or free to
	 *         chat.
	 */
	public static boolean isAvailable(Presence presence) {
		return presence.isAvailable() && !presence.isAway();
	}

	/**
	 * Returns the presence of a user.
	 * 
	 * @param jid
	 *            the users jid.
	 * @return the users presence.
	 */
	public static Presence getPresence(String jid) {
		if (jid != null
				&& jid.equals(SparkManager.getSessionManager().getBareAddress())) {
			return SparkManager.getWorkspace().getStatusBar().getPresence();
		} else {
			final Roster roster = SparkManager.getConnection().getRoster();
			return roster.getPresence(jid);
		}
	}

	/**
	 * Returns the fully qualified jid of a user.
	 * 
	 * @param jid
	 *            the users bare jid (ex. derek@jivesoftware.com)
	 * @return the fully qualified jid of a user (ex. derek@jivesoftware.com -->
	 *         derek@jivesoftware.com/spark)
	 */
	public static String getFullyQualifiedJID(String jid) {
		final Roster roster = SparkManager.getConnection().getRoster();
		Presence presence = roster.getPresence(jid);
		return presence.getFrom();
	}

	public static String getJidFromMUCPresence(Presence presence) {
		Collection<PacketExtension> extensions = presence.getExtensions();
		for (PacketExtension pe : extensions) {
			if (pe instanceof MUCUser) {
				final MUCUser mucUser = (MUCUser) pe;
				String fullJid = mucUser.getItem().getJid();
				String userJid = org.jivesoftware.smack.util.StringUtils
						.parseBareAddress(fullJid);
				return userJid;
			}
		}
		return null;
	}

	/**
	 * Returns the icon associated with a users presence.
	 * 
	 * @param presence
	 *            the users presence.
	 * @return the icon associated with it.
	 */
	public static Icon getIconFromPresence(Presence presence) {

		Icon icon = getIcon(presence);
		// Check For ContactItem handlers
		Icon handlerIcon = SparkManager.getChatManager()
				.getTabIconForContactHandler(presence);
		if (handlerIcon != null) {
			icon = handlerIcon;
		}
		return icon;
	}

	/**
	 * Returns the Presence Map.
	 * 
	 * @return the Presence Map.
	 */
	public static List<Presence> getPresences() {
		return gPresences;
	}

	public static boolean isBrb(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			presenceMode = Presence.Mode.available;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(Res.getString("status.brb"))
				&& presenceMode.equals(Presence.Mode.away)) {
			return true;
		}
		return false;
	}

	public static boolean isBusy(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			presenceMode = Presence.Mode.available;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(Res.getString("status.busy"))
				&& presenceMode.equals(Presence.Mode.away)) {
			return true;
		}
		return false;
	}

	public static boolean isInAMeeting(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			return false;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(
						Res.getString("status.in.a.meeting"))
				&& presenceMode.equals(Presence.Mode.xa)) {
			return true;
		}
		return false;
	}

	public static boolean isOffWork(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			return false;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(
						Res.getString("status.off.work"))
				&& presenceMode.equals(Presence.Mode.xa)) {
			return true;
		}
		return false;
	}

	public static boolean isOnPhone(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			return false;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(
						Res.getString("status.on.phone"))
				&& presenceMode.equals(Presence.Mode.dnd)) {
			return true;
		}
		return false;
	}

	public static boolean isInvisible(Presence presence) {
		return presence != null
				&& presence.getStatus() != null
				&& presence.getType() == Presence.Type.unavailable
				&& Presence.Mode.available == presence.getMode()
				&& (Res.getString("status.invisible").equalsIgnoreCase(
						presence.getStatus()) || Res
						.getString("status.offline").equalsIgnoreCase(
								presence.getStatus()));
	}

	public static Presence getAvailablePresence() {
		return availablePresence;
	}

	public static Presence getUnavailablePresence() {
		return invisible;
	}

	public static boolean areEqual(Presence p1, Presence p2) {
		if (p1 == p2)
			return true;

		if (p1 == null || p2 == null)
			return false;

		return p1.getType() == p2.getType() && p1.getMode() == p2.getMode()
				&& p1.getStatus().equals(p2.getStatus());
	}

	public static boolean isDND(Presence presence) {
		return presence != null && presence.getMode() == Presence.Mode.dnd;
	}

	public static boolean isExtendedAway(Presence presence) {
		return presence.isAvailable() && presence.getMode() == Presence.Mode.xa;
	}

	public static boolean isAway(Presence presence) {
		return presence.isAvailable()
				&& presence.getMode() == Presence.Mode.away
				&& presence.getStatus().contains(Res.getString("status.away"));
	}

	public static boolean isFreeToChat(Presence presence) {
		return presence.isAvailable()
				&& presence.getMode() == Presence.Mode.chat;
	}

	public static Presence copy(Presence presence) {
		if (presence == null)
			return null;
		Presence copy = new Presence(presence.getType());
		copy.setMode(presence.getMode());
		copy.setStatus(presence.getStatus());
		copy.setError(presence.getError());
		copy.setFrom(presence.getFrom());
		copy.setLanguage(presence.getLanguage());
		copy.setPacketID(presence.getPacketID());
		copy.setTo(presence.getTo());
		try {
			copy.setPriority(getValidPriority(presence));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return copy;
	}

	public static Presence getFreeToChatPresence() {
		return freeToChatPresence;
	}

	public static Presence getAwayPresence() {
		return awayPresence;
	}

	public static Presence getBrbPresence() {
		return brbPresence;
	}

	public static Presence getBusyPresence() {
		return busyPresence;
	}

	public static Presence getDndPresence() {
		return dndPresence;
	}

	public static Presence getOnPhonePresence() {
		return phonePresence;
	}

	public static Presence getExtendedAwayPresence() {
		return extendedAway;
	}

	public static Presence getInMeetingPresence() {
		return inMeeting;
	}

	public static Presence getOffWorkPresence() {
		return offWork;
	}

	public static Presence getInvisiblePresence() {
		return invisible;
	}

	public static Icon getIcon(Presence presence) {
		if (isInvisible(presence)) {
			return SparkRes.getImageIcon(SparkRes.INVISIBLE);
		}

		// Handle offline presence
		if (!presence.isAvailable()) {
			return SparkRes.getImageIcon(SparkRes.CLEAR_BALL_ICON);
		}

		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			presenceMode = Presence.Mode.available;
		}

		Icon icon = null;

		if (isBrb(presence)) {
			icon = SparkRes.getImageIcon("IM_BRB");
		} else if (isBusy(presence)) {
			icon = SparkRes.getImageIcon("IM_BUSY");
		} else if (isInAMeeting(presence)) {
			icon = SparkRes.getImageIcon("IM_MEETING");
		} else if (isOffWork(presence)) {
			icon = SparkRes.getImageIcon("IM_OFF_WORK");
		} else if (presenceMode.equals(Presence.Mode.available)) {
			icon = SparkRes.getImageIcon(SparkRes.GREEN_BALL);
		} else if (presenceMode.equals(Presence.Mode.chat)) {
			icon = SparkRes.getImageIcon(SparkRes.FREE_TO_CHAT_IMAGE);
		} else if (isOnPhone(presence)) {
			icon = SparkRes.getImageIcon(SparkRes.ON_PHONE_IMAGE);
		} else if (presenceMode.equals(Presence.Mode.away)) {
			icon = SparkRes.getImageIcon(SparkRes.IM_AWAY);
		} else if (presenceMode.equals(Presence.Mode.dnd)) {
			icon = SparkRes.getImageIcon(SparkRes.IM_DND);
		} else if (presenceMode.equals(Presence.Mode.xa)) {
			icon = SparkRes.getImageIcon(SparkRes.EXTENDED_AWAY);
		}

		return icon;
	}

	private static int getValidPriority(Presence presence) {
		int priority = presence.getPriority();
		if (priority >= -128 && priority <= 128)
			return priority;
		// TODO: we could check the type of presence in order to set up
		// priority if the given priority is unset.
		return 0;
	}

}
