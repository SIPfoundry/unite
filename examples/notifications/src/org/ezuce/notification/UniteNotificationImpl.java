/**
 * 
 */
package org.ezuce.notification;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ezuce.notification.listener.NotificationClickListener;
import org.ezuce.notification.manager.NotificationManager;
import org.ezuce.notification.manager.NotificationManagerImpl;
import org.ezuce.notification.position.NotificationPosition;
import org.ezuce.notification.window.CallPopupWindow;
import org.ezuce.notification.window.EventPopupWindow;
import org.ezuce.notification.window.TextPopupWindow;

import com.ezuce.GraphicUtils;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 * 
 */
public class UniteNotificationImpl extends JFrame implements UniteNotification,
		Observer {

	private static final long serialVersionUID = -3463562495679407243L;

	private NotificationManager nManager = new NotificationManagerImpl();

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	public void showText(final String from, final String text,
			final NotificationClickListener onClickAction) {
		// build msg window
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TextPopupWindow window = new TextPopupWindow();
				window.setFrom(from);
				window.setText(text);
				window.addOnClickAction(onClickAction);
				nManager.show(window);
			}
		});

	}

	public void showCall(final String from, final String title,
			final String phone, final Component userAvatar,
			final ActionListener onAcceptAction,
			final ActionListener onDeclineAction) {
		// build call window
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CallPopupWindow window = new CallPopupWindow(false);
				window.setFrom(from);
				window.setTitle(title);
				window.setPhone(phone);
				window.setUserAvatar(userAvatar);
				window.addOnAcceptAction(onAcceptAction);
				window.addOnDeclineAction(onDeclineAction);
				nManager.showDetached(window, NotificationPosition.CENTER);
			}
		});
	}

	public void hideCall() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				nManager.closeDetached();
			}
		});
	}

	public void showEvent(final String from, final String title,
			final String status, final String iconPath,
			final Component userAvatar) {
		// build event window

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EventPopupWindow window = new EventPopupWindow();
				window.setFrom(from);
				window.setTitle(title);
				window.setHeaderText(status);
				window.setStatus(status);
				window.setHeaderIcon(GraphicUtils.createImageIcon(iconPath));
				window.setUserAvatar(userAvatar);
				nManager.show(window);
			}
		});

	}

}
