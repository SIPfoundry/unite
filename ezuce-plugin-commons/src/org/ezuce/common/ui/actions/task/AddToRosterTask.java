package org.ezuce.common.ui.actions.task;

import javax.swing.JOptionPane;

import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.UserManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.plugin.gateways.transports.Transport;
import org.jivesoftware.sparkimpl.plugin.gateways.transports.TransportUtils;

public class AddToRosterTask extends org.jdesktop.application.Task<RosterEntry, Void> {
	private String nickname;
	private final String groupName;
	private final ContactGroup group;
	private final String bareJID;
	private String errorMessage = null;

	public AddToRosterTask(org.jdesktop.application.Application app, String bareJID, String nickname, String groupName,
			ContactGroup group) {
		// EDT - read GUI info:
		super(app);
		this.bareJID = bareJID;
		this.nickname = nickname;
		this.groupName = groupName;
		this.group = group;
	}

	@Override
	protected RosterEntry doInBackground() {
		// NOT EDT:
		errorMessage = null;
		try {
			String jid = bareJID;
			String contact = UserManager.escapeJID(jid);

			Transport transport = TransportUtils.getTransport(SparkManager.getConnection().getServiceName());

			if (transport == null) {
				if (contact.indexOf("@") == -1) {
					contact = contact + "@" + SparkManager.getConnection().getServiceName();
				}
			} else {
				if (contact.indexOf("@") == -1) {
					contact = contact + "@" + transport.getServiceName();
				}
			}

			if (!ModelUtil.hasLength(nickname) && ModelUtil.hasLength(contact)) {
				// Try to load nickname from VCard
				VCard vcard = new VCard();
				try {
					vcard.load(SparkManager.getConnection(), contact);
					nickname = vcard.getNickName();
				} catch (XMPPException e1) {
					Log.error(e1);
				}
				// If no nickname, use first name.
				if (!ModelUtil.hasLength(nickname)) {
					nickname = StringUtils.parseName(contact);
				}
			}

			boolean isSharedGroup = group != null && group.isSharedGroup();

			if (isSharedGroup) {
				errorMessage = Res.getString("message.cannot.add.contact.to.shared.group");
			} else if (!ModelUtil.hasLength(contact)) {
				errorMessage = Res.getString("message.specify.contact.jid");
			} else if (StringUtils.parseBareAddress(contact).indexOf("@") == -1) {
				errorMessage = Res.getString("message.invalid.jid.error");
			} else if (!ModelUtil.hasLength(groupName)) {
				errorMessage = Res.getString("message.specify.group");
			} else if (ModelUtil.hasLength(contact) && ModelUtil.hasLength(groupName) && !isSharedGroup) {
				return addEntry(jid, nickname, groupName);
			}
			return null;
		} catch (NullPointerException ee) {
			Log.error(ee);
		}
		return null;
	}

	@Override
	protected void succeeded(RosterEntry result) {
		// EDT:
		if (result == null) {
			JOptionPane.showMessageDialog(SparkManager.getMainWindow(), errorMessage, Res.getString("title.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Adds a new entry to the users Roster.
	 *
	 * @param jid
	 *            the jid.
	 * @param nickname
	 *            the nickname.
	 * @param group
	 *            the contact group.
	 * @return the new RosterEntry.
	 */
	public RosterEntry addEntry(String jid, String nickname, String group) {
		String[] groups = { group };

		Roster roster = SparkManager.getConnection().getRoster();
		RosterEntry userEntry = roster.getEntry(jid);

		boolean isSubscribed = true;
		if (userEntry != null) {
			isSubscribed = userEntry.getGroups().isEmpty();
		}

		if (isSubscribed) {
			try {
				roster.createEntry(jid, nickname, new String[] { group });
			} catch (XMPPException e) {
				Log.error("Unable to add new entry " + jid, e);
			}
			return roster.getEntry(jid);
		}

		try {
			RosterGroup rosterGroup = roster.getGroup(group);
			if (rosterGroup == null) {
				rosterGroup = roster.createGroup(group);
			}

			if (userEntry == null) {
				roster.createEntry(jid, nickname, groups);
				userEntry = roster.getEntry(jid);
			} else {
				userEntry.setName(nickname);
				rosterGroup.addEntry(userEntry);
			}

			userEntry = roster.getEntry(jid);
		} catch (XMPPException ex) {
			Log.error(ex);
		}
		return userEntry;
	}
}
