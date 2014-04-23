package org.ezuce.common.phone.tray;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.media.ui.GraphicUtils;
import org.jivesoftware.smack.packet.Presence;

public class PresenceIconProducer {

	private static final MessageFormat gPresenceIconPath = new MessageFormat(
			"/resources/images/tray/{0}/{1}.png");

	// /// not registered
	private static final Image[] gAvailableIcons = {
			iconUnreg("communicator", 16), iconUnreg("communicator", 24),
			iconUnreg("communicator", 32), iconUnreg("communicator", 64), };

	private static final Image[] gAwayIcons = { iconUnreg("away", 16),
			iconUnreg("away", 24), iconUnreg("away", 32),
			iconUnreg("away", 64), };

	private static final Image[] gAwayExtendedIcons = {
			iconUnreg("away-extended", 16), iconUnreg("away-extended", 24),
			iconUnreg("away-extended", 32), iconUnreg("away-extended", 64), };

	private static final Image[] gBrbIcons = { iconUnreg("be-right-back", 16),
			iconUnreg("be-right-back", 24), iconUnreg("be-right-back", 32),
			iconUnreg("be-right-back", 64), };

	private static final Image[] gBusyIcons = { iconUnreg("busy", 16),
			iconUnreg("busy", 24), iconUnreg("busy", 32),
			iconUnreg("busy", 64), };

	private static final Image[] gChattyIcons = { iconUnreg("chatty", 16),
			iconUnreg("chatty", 24), iconUnreg("chatty", 32),
			iconUnreg("chatty", 64), };

	private static final Image[] gDndIcons = { iconUnreg("do-not-disturb", 16),
			iconUnreg("do-not-disturb", 24), iconUnreg("do-not-disturb", 32),
			iconUnreg("do-not-disturb", 64), };

	private static final Image[] gInvisibleIcons = {
			iconUnreg("invisible", 16), iconUnreg("invisible", 24),
			iconUnreg("invisible", 32), iconUnreg("invisible", 64), };

	private static final Image[] gMeetingIcons = { iconUnreg("meeting", 16),
			iconUnreg("meeting", 24), iconUnreg("meeting", 32),
			iconUnreg("meeting", 64), };

	private static final Image[] gOffWorkIcons = { iconUnreg("off-work", 16),
			iconUnreg("off-work", 24), iconUnreg("off-work", 32),
			iconUnreg("off-work", 64), };

	private static final Image[] gOnPhoneIcons = {
			iconUnreg("on_the_phone", 16), iconUnreg("on_the_phone", 24),
			iconUnreg("on_the_phone", 32), iconUnreg("on_the_phone", 64), };

	// // reg
	private static final Image[] gAvailableIconsReg = {
			iconReg("communicator", 16), iconReg("communicator", 24),
			iconReg("communicator", 32), iconReg("communicator", 64), };

	private static final Image[] gAwayIconsReg = { iconReg("away", 16),
			iconReg("away", 24), iconReg("away", 32), iconReg("away", 64), };

	private static final Image[] gAwayExtendedIconsReg = {
			iconReg("away-extended", 16), iconReg("away-extended", 24),
			iconReg("away-extended", 32), iconReg("away-extended", 64), };

	private static final Image[] gBrbIconsReg = { iconReg("be-right-back", 16),
			iconReg("be-right-back", 24), iconReg("be-right-back", 32),
			iconReg("be-right-back", 64), };

	private static final Image[] gBusyIconsReg = { iconReg("busy", 16),
			iconReg("busy", 24), iconReg("busy", 32), iconReg("busy", 64), };

	private static final Image[] gChattyIconsReg = { iconReg("chatty", 16),
			iconReg("chatty", 24), iconReg("chatty", 32),
			iconReg("chatty", 64), };

	private static final Image[] gDndIconsReg = {
			iconReg("do-not-disturb", 16), iconReg("do-not-disturb", 24),
			iconReg("do-not-disturb", 32), iconReg("do-not-disturb", 64), };

	private static final Image[] gInvisibleIconsReg = {
			iconReg("invisible", 16), iconReg("invisible", 24),
			iconReg("invisible", 32), iconReg("invisible", 64), };

	private static final Image[] gMeetingIconsReg = { iconReg("meeting", 16),
			iconReg("meeting", 24), iconReg("meeting", 32),
			iconReg("meeting", 64), };

	private static final Image[] gOffWorkIconsReg = { iconReg("off-work", 16),
			iconReg("off-work", 24), iconReg("off-work", 32),
			iconReg("off-work", 64), };

	private static final Image[] gOnPhoneIconsReg = {
			iconReg("on_the_phone", 16), iconReg("on_the_phone", 24),
			iconReg("on_the_phone", 32), iconReg("on_the_phone", 64), };

	// ////////////////
	private static final Image[] gWritingIcons = {
			iconOrig("chat-status_writing", 16),
			iconOrig("chat-status_writing", 24),
			iconOrig("chat-status_writing", 32),
			iconOrig("chat-status_writing", 64), };

	private static final Image[] gMessageReceivedIcons = {
			iconOrig("message_received", 16), iconOrig("message_received", 24),
			iconOrig("message_received", 32), iconOrig("message_received", 64), };

	private static final Image[] gConnectionIcons = {
			iconOrig("connecting", 16), iconOrig("connecting", 24),
			iconOrig("connecting", 32), iconOrig("connecting", 64), };

	private static final Image[] gOfflineIcons = { iconOrig("offline", 16),
			iconOrig("offline", 24), iconOrig("offline", 32),
			iconOrig("offline", 64), };

	private static Image iconReg(String name, int size) {
		return icon(name, size, true);
	}

	private static Image iconUnreg(String name, int size) {
		return icon(name, size, false);
	}

	private static Image iconOrig(String name, int size) {
		Image img = GraphicUtils.createImageIcon(
				gPresenceIconPath.format(new String[] { String.valueOf(size),
						name })).getImage();
		return img;
	}

	private static Image icon(String name, int size, boolean isRegistered) {
		Image img = GraphicUtils.createImageIcon(
				gPresenceIconPath.format(new String[] { String.valueOf(size),
						name })).getImage();

		String regPath = isRegistered ? "tray_ready_for_communication"
				: "tray_not_ready_for_communication_red_cross";
		Image reg = GraphicUtils.createImageIcon(
				gPresenceIconPath.format(new String[] { String.valueOf(size),
						regPath })).getImage();

		int factor = 2;

		int iconWidth = img.getWidth(null);
		int iconHeight = img.getHeight(null);
		int startX = iconWidth - (iconWidth / factor);
		int startY = iconHeight - (iconHeight / factor);

		BufferedImage bi = new BufferedImage(iconWidth, iconHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(img, 0, 0, iconWidth, iconHeight, null);

		int h = reg.getHeight(null);
		int w = reg.getWidth(null);

		g.drawImage(reg, startX, startY, iconWidth, iconHeight, 0, 0, w, h,
				null);

		g.dispose();

		return bi;
	}

	public static Image[] getOfflineIcons() {
		return gOfflineIcons;
	}

	public static Image[] getConnectingIcons() {
		return gConnectionIcons;
	}

	public static Image[] getWritingIcons() {
		return gWritingIcons;
	}

	public static Image[] getMessageReceivedIcons() {
		return gMessageReceivedIcons;
	}

	public static Image[] getAvailableIcons(boolean isReg) {
		return isReg ? gAvailableIconsReg : gAvailableIcons;
	}

	public static Image[] getPresenceIcons(Presence p, boolean isReg) {
		if (p == null)
			return gAvailableIcons;

		if (EzucePresenceManager.isInvisible(p)) {
			return isReg ? gInvisibleIconsReg : gInvisibleIcons;

		} else if (EzucePresenceManager.isBrb(p)) {
			return isReg ? gBrbIconsReg : gBrbIcons;

		} else if (EzucePresenceManager.isBusy(p)) {
			return isReg ? gBusyIconsReg : gBusyIcons;

		} else if (EzucePresenceManager.isInAMeeting(p)) {
			return isReg ? gMeetingIconsReg : gMeetingIcons;

		} else if (EzucePresenceManager.isOffWork(p)) {
			return isReg ? gOffWorkIconsReg : gOffWorkIcons;

		} else if (EzucePresenceManager.isAvailable(p)) {
			return isReg ? gAvailableIconsReg : gAvailableIcons;

		} else if (EzucePresenceManager.isFreeToChat(p)) {
			return isReg ? gChattyIconsReg : gChattyIcons;

		} else if (EzucePresenceManager.isAway(p)) {
			return isReg ? gAwayIconsReg : gAwayIcons;

		} else if (EzucePresenceManager.isExtendedAway(p)) {
			return isReg ? gAwayExtendedIconsReg : gAwayExtendedIcons;

		} else if (EzucePresenceManager.isOnPhone(p)) {
			return isReg ? gOnPhoneIconsReg : gOnPhoneIcons;

		} else if (EzucePresenceManager.isDND(p)) {
			return isReg ? gDndIconsReg : gDndIcons;

		}
		return new Image[0];
	}
}
