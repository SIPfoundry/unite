/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Timer;

import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.common.preference.sound.EzuceSoundPreference;
import org.ezuce.common.preference.sound.EzuceSoundPreferences;
import org.ezuce.media.ui.GraphicUtils;
import org.ezuce.media.ui.listeners.NotificationMouseHoverListener;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.sparkimpl.preference.sounds.SoundPreference;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.PreferenceListener;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
class NotificationManager implements PreferenceListener {

	private static final int DELAY_MOUSE_EXITED = 1000;
	private static final int DELAY = 300;
	private static final LinkedList<PopupWindow> windowList;
	private static final java.util.concurrent.locks.Lock LOCK;

	private boolean isOpened;
	private int mCloseDelay;
	private NotificationPosition mPosition;
	private boolean isMouseOverWindow;
	private PopupWindow detachedWindow;

	private boolean playUserOnlineNotifySound;
	private File soundUserOnline;
	private File soundIncomingMsg;
	private boolean playMessageReceivedSound;
	private boolean playUserOfflineNotifySound;
	private File soundUserOffline;

	NotificationManager() {
		SettingsManager.addPreferenceListener(this);

		// set up default notifications.
		updateNotificationPreference();
		updateSoundsPreferences();
	}

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

	public void showDetached(PopupWindow window) {
		// only one detached window can be shown.
		// actually it is incoming call.
		if (null != detachedWindow && null == window)
			return;

		try {
			LOCK.lock();
			detachedWindow = window;
			detachedWindow.showWindow(mPosition);
			moveDetachedWindowOnTop();
		} finally {
			LOCK.unlock();
		}
	}

	public void closeDetached() {
		if (detachedWindow == null)
			return;
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

				playNotificationSound(window);
				window.showWindow(mPosition);
				moveDetachedWindowOnTop();
				addMouseHoverListener(window);

				final Timer closeTimer = new Timer(mCloseDelay, null);
				closeTimer.addActionListener(closeWindowAction(window));
				closeTimer.start();
			}

		};
	}

	/**
	 * After CLOSE_DELAY is elapsed and mouse is not over the window then the
	 * window will be closed.
	 * 
	 * @param window
	 * @return
	 */
	private ActionListener closeWindowAction(final PopupWindow window) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Timer t = (Timer) e.getSource();
				t.stop();

				if (!isOpen(window)) {
					isMouseOverWindow = false;
				}

				if (isMouseOverWindow) {
					t.restart();
					return;
				}
				removeMouseHoverListener(window);
				isOpened = false;
				window.closeWindow();

				next();
			}

		};
	}

	private boolean isOpen(PopupWindow window) {
		return isOpened && window.isOpen();
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

	private void moveDetachedWindowOnTop() {
		if (detachedWindow == null)
			return;
		detachedWindow.toFront();
		detachedWindow.repaint();
	}

	private void playNotificationSound(PopupWindow window) {
		if (window instanceof EventPopupWindow) {

			EventPopupWindow popup = (EventPopupWindow) window;

			// TODO: this part needs to be improved.
			boolean isOffline = popup.getTitle().contains(
					Res.getString("status.offline"));
			if (!isOffline)
				playOnlineSound();

			if (isOffline)
				playOfflineSound();
		}

		if (window instanceof TextPopupWindow) {
			if (playMessageReceivedSound && soundIncomingMsg != null)
				SparkManager.getSoundManager().playClip(soundIncomingMsg);
		}
	}

	private void updateSoundsPreferences() {
		EzuceSoundPreference soundPreference = (EzuceSoundPreference) SparkManager
				.getPreferenceManager()
				.getPreference(SoundPreference.NAMESPACE);

		EzuceSoundPreferences soundPrefs = (EzuceSoundPreferences) soundPreference
				.getPreferences();
		playUserOnlineNotifySound = soundPrefs.isPlayOnlineSound();
		playUserOfflineNotifySound = soundPrefs.isPlayOfflineSound();
		playMessageReceivedSound = soundPrefs.isPlayIncomingSound();

		String onlineSoundPath = soundPrefs.getOnlineSound();
		String offlineSoundPath = soundPrefs.getOfflineSound();

		String incomingMsgSoundPath = soundPrefs.getIncomingSound();

		if (onlineSoundPath != null) {
			soundUserOnline = new File(onlineSoundPath);
		}

		if (offlineSoundPath != null) {
			soundUserOffline = new File(offlineSoundPath);
		}

		if (incomingMsgSoundPath != null) {
			soundIncomingMsg = new File(incomingMsgSoundPath);
		}
	}

	private void updateNotificationPreference() {
		EzuceLocalPreferences localPreferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();
		mCloseDelay = localPreferences.getNotificationDurationTime() * 1000;
		mPosition = NotificationPosition.fromInt(localPreferences
				.getNotificationPosition());

	}

	@Override
	public void preferencesChanged(LocalPreferences preference) {
		updateNotificationPreference();
		updateSoundsPreferences();
	}

	public void playOnlineSound() {
		if (playUserOnlineNotifySound && soundUserOnline != null)
			SparkManager.getSoundManager().playClip(soundUserOnline);
	}

	public void playOfflineSound() {
		if (playUserOfflineNotifySound && soundUserOffline != null)
			SparkManager.getSoundManager().playClip(soundUserOffline);
	}

}
