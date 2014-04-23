/**
 * 
 */
package org.ezuce.common.phone.notifications;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ezuce.common.SearchUtils;
import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.MugshotPanel;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.commons.ui.location.LocationManager;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.media.ui.listeners.AcceptCallListener;
import org.ezuce.media.ui.listeners.HangupCallListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.ChatMessageHandler;
import org.jivesoftware.spark.NativeHandler;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.ui.themes.ThemePreference;
import org.jivesoftware.spark.util.SwingTimerTask;
import org.jivesoftware.spark.util.TaskEngine;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 * 
 */
public class EzuceNotification extends JFrame implements PacketListener,
		NativeHandler, ChatMessageHandler {

	private static final long serialVersionUID = -3463562495679407243L;

	private Set<String> onlineUsers = new HashSet<String>();
	private EzuceLocalPreferences preferences;
	private NotificationManager nManager;
	private EzuceNotificationPreference notifications;
	private Message latestMessage;
	private static final EzuceNotification instance = new EzuceNotification();

	public void initialize() {
		// it's taken from Notification plugin.
		// We have to wait for a while before contact List is initialised.
		final TimerTask registerTask = new SwingTimerTask() {
			public void doRun() {
				registerListener();
			}
		};
		TaskEngine.getInstance().schedule(registerTask, 4000);
		SparkManager.getNativeManager().addNativeHandler(this);
		ChatManager.getInstance().addChatMessageHandler(this);
	}

	public static EzuceNotification getInstance() {
		return instance;
	}

	public EzuceNotification() {
		notifications = new EzuceNotificationPreference();
		SparkManager.getPreferenceManager().addPreference(notifications);
		notifications.load();
		preferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();
	}

	@Override
	public void processPacket(Packet packet) {
		final Presence presence = (Presence) packet;
		String jid = presence.getFrom();
		if (org.apache.commons.lang3.StringUtils.isEmpty(jid)) {
			return;
		}

		ContactItem contactItem = SparkManager.getWorkspace().getContactList()
				.getContactItemByJID(jid);
		if (contactItem == null) {
			return;
		}

		jid = StringUtils.parseBareAddress(jid);
		boolean isOnline = onlineUsers.contains(jid);

		if (presence.isAvailable()) {
			if (!isOnline) {
				if (preferences.isOnlineNotificationsOn()) {
					showEventNotification(presence, jid);
				}
				getNotificationManager().playOnlineSound();
			}

			onlineUsers.add(jid);
		} else {
			if (preferences.isOfflineNotificationsOn() && isOnline) {
				showEventNotification(presence, jid);
			}

			getNotificationManager().playOfflineSound();
			onlineUsers.remove(jid);
		}
	}

	public void showText(final String jid, final String from, final String text) {
		if (isEmpty(text))
			return;

		// build msg window
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final TextPopupWindow window = new TextPopupWindow();
				window.setFrom(from);
				window.setText(text);
				window.addOnClickAction(new NotificationClickListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						String nickname = SparkManager.getUserManager()
								.getUserNicknameFromJID(jid);
						SparkManager.getChatManager().activateChat(jid,
								nickname);
						window.closeWindow();
					}
				});
				getNotificationManager().show(window);
			}
		});

	}

	public void showCall(final String callId, final String name,
			final boolean video) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String extension = name;
					if (extension.contains("@")) {
						extension = extension.substring(0,
								extension.indexOf("@"));
					}
					// It duplicates SearchPhonebookUserTask functionality.
					// Probably we should unify SearchPhonebookUserTask to
					// return result and let use it from outside.

					SearchUtils su = SearchUtils.getInstance();
					ContactListEntry cle = su.findExactPhonebookUser(extension);

					// If the calling user is not inside the user, do not show
					// the popup window ?!?
					// if (cle == null){
					// return;
					// }

					// IF cle IS null, it could be a call from the conference
					// room,
					// or from an unknown user.

					String from = (cle == null ? extension : cle
							.getUserDisplayName());
					String title = (cle == null ? "" : cle.getDescription());
					String phone = "";

					MugshotPanel userAvatar = new MugshotPanel(true);
					userAvatar.setPresence(cle == null ? null : cle
							.getPresence());
					if (cle != null) {
						ImageIcon avatarImage = cle.getUserAvatar();
						userAvatar.setMugshot(avatarImage);
					}
					UIMediaManager.getInstance().getVideoWindowManager()
							.getVideoWindowByCallID(callId)
							.setOpponentName(from);
					CallPopupWindow window = new CallPopupWindow(video);
					window.setFrom(from);
					window.setTitle(title);
					window.setPhone(phone);
					window.addOnAcceptAction(new AcceptCallListener(callId,
							false));
					if (video) {
						window.addOnAcceptVideoAction(new AcceptCallListener(
								callId, true));
					}
					window.addOnDeclineAction(new HangupCallListener(callId));
					getNotificationManager().showDetached(window);
				} catch (Exception e) {
					Log.error("Cannot show call window for " + name, e);
					e.printStackTrace();
				}
			}
		});
	}

	public void hideCall() {
		getNotificationManager().closeDetached();
	}

	public void showEvent(final String jid, final String from,
			final String title, final String status, final String locationName,
			final Icon iconPath, final MugshotPanel userAvatar) {
		// build event window

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final EventPopupWindow window = new EventPopupWindow();
				window.setFrom(from);
				window.setTitle(title);
				window.setHeaderText(status);
				window.setStatus(status);
				window.setLocationName(locationName);
				window.setHeaderIcon(iconPath);
				window.setUserAvatar(userAvatar);
				window.addOnClickAction(new NotificationClickListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						String nickname = SparkManager.getUserManager()
								.getUserNicknameFromJID(jid);
						SparkManager.getChatManager().activateChat(jid,
								nickname);
						window.closeWindow();
					}
				});
				getNotificationManager().show(window);
			}
		});
	}

	private void showEventNotification(final Presence presence, String jid) {

		jid = StringUtils.parseBareAddress(jid);
		final VCard vCard = SparkManager.getVCardManager().getVCardFromMemory(
				jid);

		if (null == vCard.getLastName())
			return;

		ImageIcon avatar = Utils.retrieveAvatar(vCard);

		MugshotPanel mug = new MugshotPanel(true);
		mug.setPresence(presence);
		mug.setMugshot(avatar);

		String from = vCard.getFirstName() + " " + vCard.getLastName();
		String jobTitle = vCard.getField("TITLE");
		jobTitle = isEmpty(jobTitle) ? "No job title" : jobTitle;
		Icon icon = EzucePresenceManager.getIconFromPresence(presence);
		String status = EzucePresenceUtils.getUpdatedUserStatus(presence);
		String locationName = LocationManager
				.getLocationNameFromPresence(presence);
		showEvent(jid, from, jobTitle, status, locationName, icon, mug);
	}

	/**
	 * It is taken from NotificationPlugin. After contact list is created we
	 * iterate it and save who is online.
	 */
	private void registerListener() {
		preferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();

		// Iterate through all online users and add them to the list.
		ContactList contactList = SparkManager.getWorkspace().getContactList();
		for (ContactGroup contactGroup : contactList.getContactGroups()) {
			for (ContactItem item : contactGroup.getContactItems()) {
				if (item != null && item.getJID() != null
						&& item.getPresence().isAvailable()) {
					String bareJID = StringUtils
							.parseBareAddress(item.getJID());
					onlineUsers.add(bareJID);
				}
			}
		}
		ThemePreference themePreference = (ThemePreference) SparkManager
				.getPreferenceManager()
				.getPreference(ThemePreference.NAMESPACE);
		if (themePreference != null)
			themePreference.load();
		// Add Presence Listener
		SparkManager.getConnection().addPacketListener(this,
				new PacketTypeFilter(Presence.class));
	}

	@Override
	public void messageReceived(Message message) {
		if (!handleNotification())
			return;

		latestMessage = message;
	}

	private void showTextPopup() {
		if (!preferences.isShowNotificationPopup() || latestMessage == null
				|| isEmpty(latestMessage.getBody()))
			return;

		String jid = StringUtils.parseBareAddress(latestMessage.getFrom());
		final VCard vCard = SparkManager.getVCardManager().getVCardFromMemory(
				jid);

		String from = jid;
		if (vCard != null && vCard.getFirstName() != null)
			from = vCard.getFirstName() + " " + vCard.getLastName();

		showText(jid, from, latestMessage.getBody());
	}

	@Override
	public void flashWindow(Window window) {
		showTextPopup();
		latestMessage = null;
	}

	@Override
	public void flashWindowStopWhenFocused(Window window) {
	}

	@Override
	public void stopFlashing(Window window) {
	}

	@Override
	public boolean handleNotification() {
		return ((EzuceLocalPreferences) SettingsManager.getLocalPreferences())
				.isShowNotificationPopup();
	}

	public NotificationManager getNotificationManager() {
		// sync
		if (nManager == null) {
			nManager = new NotificationManager();
		}
		return nManager;
	}

}
