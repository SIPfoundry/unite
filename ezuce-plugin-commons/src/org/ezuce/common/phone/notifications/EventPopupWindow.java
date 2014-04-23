/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Color;
import java.awt.Component;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class EventPopupWindow extends PopupWindow {

	private static final Color EVENT_POPUP_BACKGROUND = new Color(244, 244, 244);
	private static final long serialVersionUID = 6397927661097501343L;
	private NotificationClickListener onClickAction;

	public EventPopupWindow() {
	}

	@Override
	protected Color getContentBackgroundColor() {
		return EVENT_POPUP_BACKGROUND;
	}

	@Override
	protected Component getContentPanelContent() {
		Component userInfoPanel = getUserInfoPanel(getUserAvatar(), getFrom(),
				getTitle(), getStatus(), getLocationName(), onClickAction);
		return userInfoPanel;
	}

	public void addOnClickAction(
			NotificationClickListener notificationClickListener) {
		this.onClickAction = notificationClickListener;
	}

}
