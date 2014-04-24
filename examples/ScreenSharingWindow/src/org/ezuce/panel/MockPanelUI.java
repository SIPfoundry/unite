package org.ezuce.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ezuce.video.window.ChatWindow;
import org.ezuce.video.window.MainWindow;
import org.ezuce.video.window.VideoCallWindowManager;
import org.ezuce.video.window.VideoWindow;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class MockPanelUI extends JFrame {

	private static final long serialVersionUID = -1794011744208706492L;

	/** Constants */

	private static final String WINDOW_TITLE = "Screen Sharing Panel.";
	private static final Dimension WINDOW_SIZE = new Dimension(400, 200);
	private static final int LOCATION_X = 400;
	private static final int LOCATION_Y = 300;
	private static final Insets LEFT_PADDING = new Insets(10, 10, 10, 10);

	private static final String BTN_ENABLE_REMOTE_VIDEO_TEXT = "Enable Remote Video";
	private static final String BTN_ENABLE_LOCAL_VIDE_TEXT = "Enable Local Video";

	private static final String BTN_DISABLE_REMOTE_VIDEO_TEXT = "Disable Remote Video";
	private static final String BTN_DISABLE_LOCAL_VIDEO_TEXT = "Disable Local Video";

	private static final String BTN_OPEN_CHAT_WINDOW_TEXT = "Open Chat";

	/** Components */
	private JButton btnEnableRemoteVideo;
	private JButton btnEnableLocalVideo;
	private JButton btnDisableRemoteVideo;
	private JButton btnDisableLocalVideo;
	private JButton btnOpenChatWindow;

	private VideoCallWindowManager videoManager;

	protected String callId = "#call01234";

	/**
	 * Default ctor.
	 */
	public MockPanelUI() {
		initComponents();
	}

	/**
	 * Initialize all components.
	 */
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		setSize(WINDOW_SIZE);
		setResizable(false);
		setLocation(LOCATION_X, LOCATION_Y);

		btnEnableRemoteVideo = new JButton();
		btnEnableLocalVideo = new JButton();
		btnDisableRemoteVideo = new JButton();
		btnDisableLocalVideo = new JButton();
		btnOpenChatWindow = new JButton();

		btnEnableRemoteVideo.setText(BTN_ENABLE_REMOTE_VIDEO_TEXT);
		btnEnableLocalVideo.setText(BTN_ENABLE_LOCAL_VIDE_TEXT);
		btnDisableRemoteVideo.setText(BTN_DISABLE_REMOTE_VIDEO_TEXT);
		btnDisableLocalVideo.setText(BTN_DISABLE_LOCAL_VIDEO_TEXT);
		btnOpenChatWindow.setText(BTN_OPEN_CHAT_WINDOW_TEXT);

		btnEnableRemoteVideo.addActionListener(actionEnableRemoteVideo());
		btnEnableLocalVideo.addActionListener(actionEnableLocalVideo());
		btnDisableRemoteVideo.addActionListener(actionDisableRemoteVideo());
		btnDisableLocalVideo.addActionListener(actionDisableLocalVideo());
		btnOpenChatWindow.addActionListener(actionOpenChatWindow());

		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		add(btnEnableRemoteVideo, c);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = LEFT_PADDING;
		add(btnEnableLocalVideo, c);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = LEFT_PADDING;
		add(btnDisableRemoteVideo, c);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = LEFT_PADDING;
		add(btnDisableLocalVideo, c);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = LEFT_PADDING;
		add(btnOpenChatWindow, c);

		videoManager = VideoCallWindowManager.getInstance(MainWindow
				.getInstance().getContainerToAttachVideo());
		MainWindow.getInstance().setVisible(true);

	}

	private ActionListener actionEnableLocalVideo() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				videoManager.addLocalVideo(getLocalVideoComponent(), callId);
			}
		};
	}

	private ActionListener actionEnableRemoteVideo() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				videoManager.addRemoteVideo(getRemoteVideoComponent(), callId);
			}
		};
	}

	private ActionListener actionDisableLocalVideo() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				videoManager.removeLocalVideo(callId);
			}
		};
	}

	private ActionListener actionDisableRemoteVideo() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				videoManager.removeRemoteVideo(callId);
			}
		};
	}

	public static Component getLocalVideoComponent() {
		return getVideoComponent("file://home/slava/Soft/Unite/unitemedia/ScreenSharingWindow/src/resources/video/local.mpeg");
	}

	public static Component getRemoteVideoComponent() {
		return getVideoComponent("file://home/slava/Soft/Unite/unitemedia/ScreenSharingWindow/src/resources/video/remote.mpeg");
	}

	public static Component getVideoComponent(String url) {
		try {
			// URL mediaURL = new URL(url);
			MediaLocator mediaLoc = new MediaLocator(url);
			Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
			Player player = Manager.createRealizedPlayer(mediaLoc);
			Component video = player.getVisualComponent();
			player.start();
			return video;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NoPlayerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CannotRealizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private ActionListener actionOpenChatWindow() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChatWindow chat = new ChatWindow();
				chat.openWindow();

				VideoWindow vw = videoManager.getVideoWindowByCallID(callId);
				vw.attachVideoTo((JPanel) chat.getVideoContainer(),
						new Dimension(640, 480));
			}
		};
	}
}
