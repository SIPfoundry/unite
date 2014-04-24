/**
 * 
 */
package org.ezuce.notification.manager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Timer;

import org.ezuce.notification.effect.FadeEffect;
import org.ezuce.notification.listener.NotificationMouseHoverListener;
import org.ezuce.notification.position.NotificationPosition;
import org.ezuce.notification.window.PopupWindow;

import com.ezuce.GraphicUtils;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class NotificationManagerImpl implements NotificationManager {

	private static final int DELAY_MOUSE_EXITED = 1000;
	private static final int DELAY = 300;
	private static final int CLOSE_DELAY = 3000;

	private static final NotificationPosition POSITION = NotificationPosition.SOUTHEAST;
	private static final LinkedList<PopupWindow> windowList;
	private static final java.util.concurrent.locks.Lock LOCK;
	private static boolean isOpened;
	private boolean isMouseOverWindow;
	private PopupWindow detachedWindow;

	static {
		LOCK = new ReentrantLock(true);
		windowList = new LinkedList<PopupWindow>();
	}

	/**
	 * Add the given window in the queue and call next() method.
	 * */
	public void show(final PopupWindow window) {
		try {
			LOCK.lock();
			windowList.addLast(window);
			// TODO: show next window when user clicks on the "cross" button.
			next();
		} finally {
			LOCK.unlock();
		}
	}

	public void showDetached(PopupWindow window, NotificationPosition position) {
		// only one detached window can be shown.
		// actually it is incoming call.
		if (null != detachedWindow && null == window)
			return;

		try {
			LOCK.lock();
			detachedWindow = window;
			detachedWindow.showWindow(position);
		} finally {
			LOCK.unlock();
		}
	}

	public void closeDetached() {
		try {
			LOCK.lock();
			detachedWindow.closeWindow();
			detachedWindow = null;
		} finally {
			LOCK.unlock();
		}
	}

	/**
	 * Open next window.
	 */
	private void next() {
		if (isOpened)
			return;

		try {
			LOCK.lock();
			if (windowList.size() > 0) {
				isOpened = true;
				final PopupWindow window = windowList.removeFirst();
				Timer t = new Timer(DELAY, showWindowAction(window));
				t.start();
			}
		} finally {
			LOCK.unlock();
		}
	}

	/**
	 * Actually show the window and start countdown before it closes.
	 * 
	 * @param window
	 * @return ActionListener
	 */
	private ActionListener showWindowAction(final PopupWindow window) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Timer t = (Timer) e.getSource();
				t.stop();

				window.showWindow(POSITION);
				addMouseHoverListener(window);

				final Timer closeTimer = new Timer(CLOSE_DELAY, null);
				closeTimer.addActionListener(closeWindowAction(window,
						closeTimer));
				closeTimer.start();
			}

		};
	}

	/**
	 * After CLOSE_DELAY is elapsed and mouse is not over the window then the
	 * window will be closed.
	 * 
	 * @param window
	 * @param closeTimer
	 * @return
	 */
	private ActionListener closeWindowAction(final PopupWindow window,
			final Timer closeTimer) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Timer t = (Timer) e.getSource();
				t.stop();

				if (!isOpened)
					return;

				if (isMouseOverWindow) {
					closeTimer.restart();
					return;
				}
				removeMouseHoverListener(window);
				closeTimer.stop();

				// fire effect and after it's done get next window.
				new FadeEffect(window)
						.fire(new org.ezuce.notification.listener.Callback() {

							public void doCall() {
								window.closeWindow();
								isOpened = false;
								next();
							}
						});
				// window.closeWindow();
				// isOpened = false;
				// next();
			}
		};
	}

	private void removeMouseHoverListener(PopupWindow window) {
		// TODO it'd be better if we remove listeners.
		// Anyway after window is disposed then there no references to those
		// listeners should remain.

	}

	/**
	 * It starts the timer which indicates whether we are over the window or
	 * not. If we are out of window's bound longer that 1 sec then the window
	 * will be closed.
	 * 
	 * @param mouseExitedTimer
	 * @return the action.
	 */
	private void addMouseHoverListener(final PopupWindow window) {
		final Timer mouseExitedTimer = new Timer(DELAY_MOUSE_EXITED, null);
		mouseExitedTimer.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Timer t = (Timer) e.getSource();
				t.stop();
				isMouseOverWindow = false;
			}
		});

		// we add the same action for each child element to ensure that mouse
		// is over the window.
		for (Component c : GraphicUtils.getChildrenRecursively(window)) {
			c.addMouseListener(mouseHoverAction(mouseExitedTimer));
		}
	}

	/**
	 * It starts or stops the timer. will be closed.
	 * 
	 * @param mouseExitedTimer
	 * @return the action.
	 */
	private NotificationMouseHoverListener mouseHoverAction(
			final Timer mouseExitedTimer) {
		return new NotificationMouseHoverListener() {

			@Override
			public void mouseExited(MouseEvent e) {
				mouseExitedTimer.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseExitedTimer.stop();
				isMouseOverWindow = true;
			}
		};
	}
}
