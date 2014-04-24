/**
 * 
 */
package org.ezuce.notification;

import java.awt.Component;
import java.awt.event.ActionListener;

import org.ezuce.notification.listener.NotificationClickListener;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public interface UniteNotification {

	void showText(String from, String text,
			NotificationClickListener onClickAction);

	void showCall(String from, String title, String phone,
			Component userAvatar, ActionListener onAcceptAction,
			ActionListener onDeclineAction);

	void hideCall();

	void showEvent(String from, String title, String status,
			String headerIconPath, Component userAvatar);

}
