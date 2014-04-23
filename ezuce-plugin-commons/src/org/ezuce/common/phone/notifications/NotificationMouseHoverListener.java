/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public abstract class NotificationMouseHoverListener implements MouseListener {

	public abstract void mouseEntered(MouseEvent e);

	public abstract void mouseExited(MouseEvent e);

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
