/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Insets;
import java.awt.Point;

import javax.swing.JWindow;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public interface Position {

	public static final Insets SCREEN_INSETS = new Insets(50, 50, 50, 30);

	public Point getPosition(JWindow window);
}
