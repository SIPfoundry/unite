package com.ezuce.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateListener;

import com.ezuce.ChatManager;
import com.ezuce.ChatMessageHandlerImpl;
import com.ezuce.CustomStatusItem;
import com.ezuce.Default;
import com.ezuce.GraphicUtils;
import com.ezuce.LocalPreferences;
import com.ezuce.MainWindow;
import com.ezuce.NativeHandler;
import com.ezuce.PresenceListener;
import com.ezuce.PresenceManager;
import com.ezuce.Res;
import com.ezuce.SettingsManager;
import com.ezuce.Spark;
import com.ezuce.SparkManager;
import com.ezuce.SparkManager.Workspace;
import com.ezuce.StatusBar;
import com.ezuce.StatusItem;

/**
 * @author Vyacheslav Durin
 * 
 *         Mar 7, 2013
 * @version 0.1
 */
public class TrayManager implements NativeHandler, ChatManagerListener,
		ChatStateListener {

	public enum TrayEvent {
		NEW_MESSAGE, TYPING
	}

	private static String MESSAGE_COUNTER_REG_EXP = "\\[\\d+\\] ";
	private static final String APPLICATION_NAME = "Unite2";
	private TrayIcon trayIcon;
	public boolean isRegisteredAsPhone;
	private Presence cachedPresence;
	private boolean isInitialized;
	private boolean newMessage;
	private JPopupMenu popupMenu = new JPopupMenu();
	private LocalPreferences pref = SettingsManager.getLocalPreferences();
	private ChatMessageHandlerImpl chatMessageHandler = new ChatMessageHandlerImpl();
	private static final TrayManager trayManager = new TrayManager();

	public static TrayManager getInstance() {
		return trayManager;
	}

	public TrayManager() {
	}

	public void update(Presence presence) {
		if (presence == null)
			return;

		cachedPresence = PresenceManager.copy(presence);
		buildIcon();
	}

	public void updateRegisteredAsPhone(boolean registeredAsPhone) {
		isRegisteredAsPhone = registeredAsPhone;
		buildIcon();
	}

	public void update(TrayEvent event) {
		switch (event) {
		case NEW_MESSAGE:
			buildIcon(PresenceIconProducer.getMessageReceivedIcons());
			break;
		case TYPING:
			buildIcon(PresenceIconProducer.getWritingIcons());
			break;

		default:
			break;
		}
	}

	/**
	 * This method create tray icon itself and put it into Tray spot.
	 * Nevertheless TrayManager can store any events that came before tray icon
	 * is created. This method called right after Workspace is created and
	 * EzuceLoginDialog is closed.
	 */
	public void init() {

		if (!isInitialized && SystemTray.isSupported()) {

			try {
				SystemTray tray = SystemTray.getSystemTray();
				trayIcon = new TrayIcon(
						PresenceIconProducer.getAvailableIcons(false)[0],
						APPLICATION_NAME, null);
				trayIcon.setImageAutoSize(true);
				tray.add(trayIcon);
				buildPopupMenu(popupMenu);

				addConnectionListener();
				addPresenceListener();
				addMouseListener();
				SparkManager.getNativeManager().addNativeHandler(this);
				ChatManager.getInstance().addChatMessageHandler(
						chatMessageHandler);
				// XEP-0085 support (replaces the obsolete XEP-0022)
				SparkManager.getConnection().getChatManager()
						.addChatListener(this);

				buildIcon();
				isInitialized = true;

			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fill the given popup menu with items
	 * 
	 * @param popupMenu
	 */
	private void buildPopupMenu(JPopupMenu popupMenu) {
		popupMenu.removeAll();

		JMenuItem openMenu = new JMenuItem(Res.getString("menuitem.open"));
		JMenuItem minimizeMenu = new JMenuItem(Res.getString("menuitem.hide"));
		JMenuItem exitMenu = new JMenuItem(Res.getString("menuitem.exit"));
		JMenuItem statusMenu = new JMenu(Res.getString("menuitem.status"));
		JMenuItem logoutMenu = new JMenuItem(
				Res.getString("menuitem.logout.no.status"));

		openMenu.addActionListener(openMenuAction());
		minimizeMenu.addActionListener(mininizeAction());
		statusMenu.addActionListener(statusAction());

		popupMenu.add(openMenu);
		popupMenu.add(minimizeMenu);

		popupMenu.addSeparator();
		buildStatusMenu(statusMenu);
		popupMenu.add(statusMenu);

		if (Spark.isWindows() && !Default.getBoolean("DISABLE_EXIT")) {
			logoutMenu.addActionListener(logoutAction());
			popupMenu.add(logoutMenu);
		}

		if (!Default.getBoolean("DISABLE_EXIT")) {
			exitMenu.addActionListener(exitAction());
			popupMenu.add(exitMenu);
		}
	}

	/**
	 * Fill the given statuses menu with items
	 * 
	 * @param statusMenu
	 */
	private void buildStatusMenu(JMenuItem statusMenu) {
		StatusBar statusBar = Workspace.getInstance().getStatusBar();
		for (final StatusItem statusItem : statusBar.getStatusList()) {

			AbstractAction action = createStatusAction(
					statusItem.getPresence(), statusItem.getText(),
					statusItem.getIcon());

			if (hasChildren(statusItem)) {
				JMenu status = buildSubStatusMenu(statusItem, action);
				statusMenu.add(status);

			} else {
				statusMenu.add(new JMenuItem(action));
			}
		}
	}

	/**
	 * Fill the given status item with sub-statuses.
	 * 
	 * @param statusItem
	 * @param action
	 * @return
	 */
	private JMenu buildSubStatusMenu(final StatusItem statusItem,
			final AbstractAction action) {

		JMenu status = new JMenu(action);
		addHidePopupOnClickAction(action, status);

		for (final CustomStatusItem customItem : Workspace.getInstance()
				.getStatusBar().getCustomStatusList()) {

			String type = customItem.getType();
			if (!type.equals(statusItem.getText()))
				continue;

			AbstractAction customAction = createCustomStatusItemAction(
					statusItem, customItem);
			JMenuItem menuItem = new JMenuItem(customAction);
			status.add(menuItem);
		}
		return status;
	}

	/**
	 * Create an action which will be performed when custom status is clicked.
	 * 
	 * @param statusItem
	 * @param customItem
	 * @return
	 */
	private AbstractAction createCustomStatusItemAction(
			final StatusItem statusItem, final CustomStatusItem customItem) {

		AbstractAction customAction = new AbstractAction() {

			/**  */
			private static final long serialVersionUID = 5755335173523626622L;

			@Override
			public void actionPerformed(ActionEvent e) {
				StatusBar statusBar = Workspace.getInstance().getStatusBar();

				Presence oldPresence = statusItem.getPresence();
				Presence presence = PresenceManager.copy(oldPresence);
				presence.setStatus(customItem.getStatus());
				presence.setPriority(customItem.getPriority());
				SparkManager.getSessionManager().changePresence(presence);
				statusBar.setStatus(statusItem.getName() + " - "
						+ customItem.getStatus());
			}
		};
		customAction.putValue(Action.NAME, customItem.getStatus());
		customAction.putValue(Action.SMALL_ICON, statusItem.getIcon());
		return customAction;
	}

	/**
	 * Hide the popup menu when the given action is performed
	 * 
	 * @param action
	 * @param status
	 */
	private void addHidePopupOnClickAction(final AbstractAction action,
			JMenuItem status) {
		status.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				action.actionPerformed(null);
				popupMenu.setVisible(false);
			}
		});
	}

	/**
	 * Create an action which will be performed when status is clicked.
	 * 
	 * @param presence
	 * @param status
	 * @param icon
	 * @return
	 */
	private AbstractAction createStatusAction(final Presence presence,
			final String status, Icon icon) {
		final AbstractAction action = new AbstractAction() {

			/**  */
			private static final long serialVersionUID = -353012624677651459L;

			@Override
			public void actionPerformed(ActionEvent e) {

				StatusBar statusBar = Workspace.getInstance().getStatusBar();

				SparkManager.getSessionManager().changePresence(presence);
				statusBar.setStatus(status);
			}
		};
		action.putValue(Action.NAME, status);
		action.putValue(Action.SMALL_ICON, icon);
		return action;
	}

	/**
	 * Check whether the given status has substatuses or not.
	 * 
	 * @param status
	 * @return
	 */
	private boolean hasChildren(StatusItem status) {
		for (CustomStatusItem custom : Workspace.getInstance().getStatusBar()
				.getCustomStatusList()) {

			String type = custom.getType();
			if (type.equals(status.getText())) {
				return true;
			}
		}
		return false;
	}

	private void buildIcon(Image[] icons) {
		if (!isInitialized)
			return;

		MainWindow.getInstance().setIconImages(null);

		Image icon = Spark.isWindows() ? icons[3] : icons[1];

		// 16 for Windows and 24 for iOS and Unix
		trayIcon.setImage(icon);

		// set the biggest one
		MainWindow.getInstance().setIconImage(icons[3]);
	}

	/**
	 * Create a tray icon based on the current status, event or
	 * register/unregister state.
	 */
	private void buildIcon() {
		Image[] icons = PresenceIconProducer.getPresenceIcons(cachedPresence,
				isRegisteredAsPhone);
		buildIcon(icons);
	}

	/**
	 * Handle mouse clicks on the tray icon.
	 */
	private void addMouseListener() {
		trayIcon.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (isClickedTwice(event) || isClickedOnce(event)) {

					if (MainWindow.getInstance().isVisible()) {
						MainWindow.getInstance().setVisible(false);
					} else {
						MainWindow.getInstance().setVisible(true);
						MainWindow.getInstance().toFront();
					}

				} else if (event.getButton() == MouseEvent.BUTTON1) {
					MainWindow.getInstance().toFront();

				} else if (event.getButton() == MouseEvent.BUTTON3) {
					if (popupMenu.isVisible()) {
						popupMenu.setVisible(false);
					} else {
						showPopupMenu(event);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent event) {

			}

			@Override
			public void mouseExited(MouseEvent event) {

			}

			@Override
			public void mousePressed(MouseEvent event) {
				if (GraphicUtils.isMac()
						&& event.getButton() != MouseEvent.BUTTON3) {
					MainWindow.getInstance().setVisible(false);
					MainWindow.getInstance().setVisible(true);
					MainWindow.getInstance().requestFocusInWindow();
					MainWindow.getInstance().bringFrameIntoFocus();
					MainWindow.getInstance().toFront();
					MainWindow.getInstance().requestFocus();
				}
			}

			@Override
			public void mouseReleased(MouseEvent event) {

			}

		});
	}

	private void showPopupMenu(MouseEvent event) {
		double x = MouseInfo.getPointerInfo().getLocation().getX();
		double y = MouseInfo.getPointerInfo().getLocation().getY();

		x = GraphicUtils.isMac() ? x : event.getX();
		y = GraphicUtils.isMac() ? y : event.getY();

		popupMenu.setLocation((int) x, (int) y);
		popupMenu.setInvoker(popupMenu);
		popupMenu.setVisible(true);
	}

	private boolean isClickedOnce(MouseEvent event) {
		return isUsingSingleTrayClick()
				&& event.getButton() == MouseEvent.BUTTON1
				&& event.getClickCount() == 1;
	}

	private boolean isClickedTwice(MouseEvent event) {
		return !isUsingSingleTrayClick()
				&& event.getButton() == MouseEvent.BUTTON1
				&& event.getClickCount() % 2 == 0;
	}

	private boolean isUsingSingleTrayClick() {
		return pref.isUsingSingleTrayClick();
	}

	private ActionListener openMenuAction() {
		return new AbstractAction() {
			/**  */
			private static final long serialVersionUID = -796282880819503019L;

			@Override
			public void actionPerformed(ActionEvent event) {
				MainWindow.getInstance().setVisible(true);
				MainWindow.getInstance().toFront();
			}
		};
	}

	private ActionListener mininizeAction() {
		return new AbstractAction() {
			/**  */
			private static final long serialVersionUID = 3870098205324240189L;

			@Override
			public void actionPerformed(ActionEvent event) {
				MainWindow.getInstance().setVisible(false);
			}
		};
	}

	private ActionListener exitAction() {
		return new AbstractAction() {

			/**  */
			private static final long serialVersionUID = 5035538323637982496L;

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.getInstance().shutdown();
			}
		};
	}

	private ActionListener statusAction() {
		return new AbstractAction() {

			/**  */
			private static final long serialVersionUID = 5035538323637982496L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// intentionally empty
			}
		};
	}

	private ActionListener logoutAction() {
		return new AbstractAction() {

			/**  */
			private static final long serialVersionUID = 5035538323637982496L;

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.getInstance().logout(false);
			}
		};
	}

	private void addConnectionListener() {
		SparkManager.getConnection().addConnectionListener(
				new ConnectionListener() {

					@Override
					public void connectionClosed() {
						buildIcon(PresenceIconProducer.getOfflineIcons());
					}

					@Override
					public void connectionClosedOnError(Exception arg0) {
						buildIcon(PresenceIconProducer.getOfflineIcons());
					}

					@Override
					public void reconnectingIn(int arg0) {
						buildIcon(PresenceIconProducer.getConnectingIcons());
					}

					@Override
					public void reconnectionSuccessful() {
						buildIcon(PresenceIconProducer.getConnectingIcons());
					}

					@Override
					public void reconnectionFailed(Exception arg0) {
						buildIcon(PresenceIconProducer.getOfflineIcons());
					}
				});
	}

	private void addPresenceListener() {
		SparkManager.getSessionManager().addPresenceListener(
				new PresenceListener() {
					@Override
					public void presenceChanged(Presence presence) {
						update(presence);
					}
				});
	}

	// Chat events handlers
	// Copy-Pasted from SysTrayPlugin.java

	@Override
	public void processMessage(Chat arg0, Message arg1) {
		// Do nothing - stateChanged is in charge
	}

	@Override
	public void stateChanged(Chat chat, ChatState state) {
		if (ChatState.composing.equals(state)) {

			if (pref.isTypingNotificationShown()) {
				update(TrayEvent.TYPING);
			}

		} else {

			if (newMessage)
				update(TrayEvent.NEW_MESSAGE);
			else
				update(cachedPresence);
		}

	}

	@Override
	public void chatCreated(Chat chat, boolean isLocal) {
		chat.addMessageListener(this);
	}

	@Override
	public void flashWindow(Window window) {
		if (pref.isSystemTrayNotificationEnabled()) {
			update(TrayEvent.NEW_MESSAGE);
			setWindowTitle(window);
			newMessage = true;
		}
	}

	@Override
	public void flashWindowStopWhenFocused(Window window) {
		stopFlashing(window);
	}

	@Override
	public void stopFlashing(Window window) {
		update(cachedPresence);
		newMessage = false;
		chatMessageHandler.clearUnreadMessages();
	}

	@Override
	public boolean handleNotification() {
		return true;
	}

	private String getCounteredTitle(String title, int counter) {
		String stringCounter = String.format("[%s] ", counter);
		return counter > 0 ? stringCounter
				+ title.replaceFirst(MESSAGE_COUNTER_REG_EXP, "") : title
				.replaceFirst(MESSAGE_COUNTER_REG_EXP, "");
	}

	private void setWindowTitle(Window window) {
		if (window instanceof JFrame) {
			((JFrame) window).setTitle(getCounteredTitle(
					((JFrame) window).getTitle(),
					chatMessageHandler.getUnreadMessages()));
		}
	}
}
