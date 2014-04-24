/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JWindow;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public enum NotificationPosition implements Position {

	NORTHEAST(0) {

		@Override
		public Point getPosition(JWindow window) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = screenSize.width - window.getWidth() - SCREEN_INSETS.right;
			int y = SCREEN_INSETS.top;
			return new Point(x, y);
		}

	},

	SOUTHEAST(1) {

		@Override
		public Point getPosition(JWindow window) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = screenSize.width - window.getWidth() - SCREEN_INSETS.right;
			int y = screenSize.height - SCREEN_INSETS.bottom
					- window.getHeight();
			return new Point(x, y);
		}
	},

	CENTER(2) {

		@Override
		public Point getPosition(JWindow window) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = screenSize.width / 2 - window.getWidth() / 2;
			int y = screenSize.height / 2 - window.getHeight() / 2;
			return new Point(x, y);
		}
	};

	private int id;

	private NotificationPosition(int id) {
		this.id = id;
	}

	public static NotificationPosition fromInt(int i) {
		for (NotificationPosition pos : values())
			if (pos.id == i)
				return pos;

		return null;
	}

	public int toInt() {
		return id;
	}

}
