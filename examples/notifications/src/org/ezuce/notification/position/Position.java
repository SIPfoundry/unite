/**
 * 
 */
package org.ezuce.notification.position;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JWindow;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public interface Position {

	public static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static final Insets SCREEN_INSETS = new Insets(50, 50, 50, 30);

	public Point getPosition(JWindow window);
}
