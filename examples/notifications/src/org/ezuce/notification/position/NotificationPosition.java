/**
 * 
 */
package org.ezuce.notification.position;

import java.awt.Point;

import javax.swing.JWindow;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public enum NotificationPosition implements Position {

	NORTHEAST {

		public Point getPosition(JWindow window) {
			int x = SCREEN_SIZE.width - window.getWidth() - SCREEN_INSETS.right;
			int y = SCREEN_INSETS.top;
			return new Point(x, y);
		}

	},

	SOUTHEAST {

		public Point getPosition(JWindow window) {
			int x = SCREEN_SIZE.width - window.getWidth() - SCREEN_INSETS.right;
			int y = SCREEN_SIZE.height - SCREEN_INSETS.bottom
					- window.getHeight();
			return new Point(x, y);
		}
	},

	CENTER {

		public Point getPosition(JWindow window) {
			int x = SCREEN_SIZE.width / 2 - window.getWidth() / 2;
			int y = SCREEN_SIZE.height / 2 - window.getHeight() / 2;
			return new Point(x, y);
		}
	}

}
