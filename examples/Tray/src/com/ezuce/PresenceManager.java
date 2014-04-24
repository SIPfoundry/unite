package com.ezuce;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Presence;

public class PresenceManager {

	public static final List<Presence> PRESENCES = new ArrayList<Presence>();

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
			Presence.Mode.away);
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
			Presence.Mode.away);
	private static final Presence offWork = new Presence(
			Presence.Type.available, Res.getString("status.off.work"), 0,
			Presence.Mode.away);

	// Smack doesn't have Invisible presence. We use available type + invisible
	// status.
	private static final Presence invisible = new Presence(
			Presence.Type.unavailable, Res.getString("status.invisible"), 0,
			Presence.Mode.available);

	static {

		PRESENCES.add(availablePresence);
		PRESENCES.add(freeToChatPresence);
		PRESENCES.add(awayPresence);
		PRESENCES.add(brbPresence);
		PRESENCES.add(busyPresence);
		PRESENCES.add(dndPresence);
		PRESENCES.add(phonePresence);
		PRESENCES.add(extendedAway);
		PRESENCES.add(inMeeting);
		PRESENCES.add(offWork);
		PRESENCES.add(invisible);
	}

	/**
	 * Building Presence related data.
	 */
	private PresenceManager() {

	}

	public static boolean isInvisible(Presence presence) {
		return presence != null
				&& presence.getType() == Presence.Type.unavailable
				&& Res.getString("status.invisible").equalsIgnoreCase(
						presence.getStatus())
				&& Presence.Mode.available == presence.getMode();
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

	public static boolean isAvailable(Presence presence) {
		return presence.isAvailable() && !presence.isAway();
	}

	public static Presence copy(Presence presence) {
		if (presence == null)
			return null;
		Presence copy = new Presence(presence.getType());
		copy.setMode(presence.getMode());
		copy.setStatus(presence.getStatus());
		return copy;
	}

	public static boolean isInvisibleWithOfflineStatus(Presence cachedPresence) {
		// TODO Auto-generated method stub
		return false;
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
			presenceMode = Presence.Mode.available;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(
						Res.getString("status.in.a.meeting"))
				&& presenceMode.equals(Presence.Mode.away)) {
			return true;
		}
		return false;
	}

	public static boolean isOffWork(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			presenceMode = Presence.Mode.available;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(
						Res.getString("status.off.work"))
				&& presenceMode.equals(Presence.Mode.away)) {
			return true;
		}
		return false;
	}

	public static boolean isOnPhone(Presence presence) {
		Presence.Mode presenceMode = presence.getMode();
		if (presenceMode == null) {
			presenceMode = Presence.Mode.available;
		}
		if (presence.getStatus() != null
				&& presence.getStatus().contains(
						Res.getString("status.on.phone"))
				&& presenceMode.equals(Presence.Mode.away)) {
			return true;
		}
		return false;
	}

}
