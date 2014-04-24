package com.ezuce;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

public class StatusBar {

	private static Collection<CustomStatusItem> customStatusList = new ArrayList<CustomStatusItem>();
	private static final MessageFormat gPresenceIconPath = new MessageFormat(
			"/resources/images/tray/{0}/{1}.png");
	private static Collection<StatusItem> statusList = new ArrayList<StatusItem>();
	static {

		statusList
				.add(new StatusItem(PresenceManager.PRESENCES.get(0),
						GraphicUtils.createImageIcon(gPresenceIconPath
								.format(new String[] { String.valueOf(16),
										"available" }))));

		statusList
				.add(new StatusItem(PresenceManager.PRESENCES.get(1),
						GraphicUtils.createImageIcon(gPresenceIconPath
								.format(new String[] { String.valueOf(16),
										"chatty" }))));

		statusList.add(new StatusItem(PresenceManager.PRESENCES.get(2),
				GraphicUtils.createImageIcon(gPresenceIconPath
						.format(new String[] { String.valueOf(16), "away" }))));

		statusList.add(new StatusItem(PresenceManager.PRESENCES.get(3),
				GraphicUtils.createImageIcon(gPresenceIconPath
						.format(new String[] { String.valueOf(16),
								"be-right-back" }))));

		statusList.add(new StatusItem(PresenceManager.PRESENCES.get(4),
				GraphicUtils.createImageIcon(gPresenceIconPath
						.format(new String[] { String.valueOf(16), "busy" }))));

		statusList.add(new StatusItem(PresenceManager.PRESENCES.get(5),
				GraphicUtils.createImageIcon(gPresenceIconPath
						.format(new String[] { String.valueOf(16),
								"do-not-disturb" }))));

		statusList.add(new StatusItem(PresenceManager.PRESENCES.get(6),
				GraphicUtils.createImageIcon(gPresenceIconPath
						.format(new String[] { String.valueOf(16),
								"on_the_phone" }))));

		statusList.add(new StatusItem(PresenceManager.PRESENCES.get(7),
				GraphicUtils.createImageIcon(gPresenceIconPath
						.format(new String[] { String.valueOf(16),
								"away-extended" }))));

		statusList
				.add(new StatusItem(PresenceManager.PRESENCES.get(8),
						GraphicUtils.createImageIcon(gPresenceIconPath
								.format(new String[] { String.valueOf(16),
										"meeting" }))));

		statusList
				.add(new StatusItem(PresenceManager.PRESENCES.get(9),
						GraphicUtils.createImageIcon(gPresenceIconPath
								.format(new String[] { String.valueOf(16),
										"off-work" }))));

		statusList
				.add(new StatusItem(PresenceManager.PRESENCES.get(10),
						GraphicUtils.createImageIcon(gPresenceIconPath
								.format(new String[] { String.valueOf(16),
										"invisible" }))));

		//
		CustomStatusItem item1 = new CustomStatusItem();
		item1.setStatus("Super");
		item1.setType("Online");
		customStatusList.add(item1);
	}

	public Collection<StatusItem> getStatusList() {
		return statusList;
	}

	public Collection<CustomStatusItem> getCustomStatusList() {
		return customStatusList;
	}

	public void setStatus(String text) {
		// TODO: to be implemented
		System.out.println("TODO: StatusBar#setStatus()");

	}
}
