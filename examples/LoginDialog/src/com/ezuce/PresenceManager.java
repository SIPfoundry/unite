/**
 * $RCSfile: ,v $
 * $Revision: $
 * $Date: $
 * 
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezuce;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.jivesoftware.smack.packet.Presence;

import com.ezuce.util.GraphicUtils;
import com.ezuce.util.Res;

/**
 * Handles the most common presence checks.
 * 
 * @author Derek DeMoro
 */
public class PresenceManager {

	private static final List<Presence> PRESENCES = new ArrayList<Presence>();

	static {
		// Add Available Presence
		final Presence availablePresence = new Presence(
				Presence.Type.available, Res.getString("status.online"), 1,
				Presence.Mode.available);
		final Presence freeToChatPresence = new Presence(
				Presence.Type.available, Res.getString("status.free.to.chat"),
				1, Presence.Mode.chat);
		final Presence awayPresence = new Presence(Presence.Type.available,
				Res.getString("status.away"), 0, Presence.Mode.away);
		final Presence phonePresence = new Presence(Presence.Type.available,
				Res.getString("status.on.phone"), 0, Presence.Mode.away);
		final Presence dndPresence = new Presence(Presence.Type.available,
				Res.getString("status.do.not.disturb"), 0, Presence.Mode.dnd);
		final Presence extendedAway = new Presence(Presence.Type.available,
				Res.getString("status.extended.away"), 0, Presence.Mode.xa);

		// Smack doesn't have Invisible presence. We use available type +
		// invisible status.
		final Presence invisible = new Presence(Presence.Type.unavailable,
				Res.getString("status.invisible"), 0, Presence.Mode.available);

		PRESENCES.add(freeToChatPresence);
		PRESENCES.add(availablePresence);
		PRESENCES.add(awayPresence);
		PRESENCES.add(extendedAway);
		PRESENCES.add(phonePresence);
		PRESENCES.add(dndPresence);
		PRESENCES.add(invisible);
	}

	/**
	 * Building Presence related data.
	 */
	private PresenceManager() {

	}

	public static Icon getIconFromPresence(Presence presence) {
		if (presence.getStatus().equals(Res.getString("status.online")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/available_16x16.png");

		if (presence.getStatus().equals(Res.getString("status.free.to.chat")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/free-to-chat_16x16.png");

		if (presence.getStatus().equals(Res.getString("status.away")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/away_16x16.png");

		if (presence.getStatus().equals(Res.getString("status.on.phone")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/phone_16x16.png");

		if (presence.getStatus().equals(Res.getString("status.do.not.disturb")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/do-not-disturb_16x16.png");

		if (presence.getStatus().equals(Res.getString("status.extended.away")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/extended-away_16x16.png");

		if (presence.getStatus().equals(Res.getString("status.invisible")))
			return GraphicUtils
					.createImageIcon("/resources/images/presence/invisible_16x16.png");

		return null;

	}

	public static List<Presence> getPresences() {
		return PRESENCES;
	}

}
