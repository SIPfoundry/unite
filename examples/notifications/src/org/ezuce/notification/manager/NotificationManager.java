/**
 * 
 */
package org.ezuce.notification.manager;

import org.ezuce.notification.position.NotificationPosition;
import org.ezuce.notification.window.PopupWindow;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public interface NotificationManager {

	void show(PopupWindow window);

	void showDetached(PopupWindow window, NotificationPosition position);

	void closeDetached();

}
