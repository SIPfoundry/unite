package org.ezuce.media.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.media.manager.StreamingManager;
import org.ezuce.media.manager.UnitemediaRegistry;
import org.ezuce.media.ui.VideoWindowManager.TYPE;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.jivesoftware.Spark;
import org.jivesoftware.spark.util.SwingTimerTask;
import org.jivesoftware.spark.util.TaskEngine;
import org.jivesoftware.spark.util.log.Log;

/**
 * @author Vyacheslav Durin
 * 
 *         Feb 21, 2013
 * @version 0.1
 */
public class VideoWindow extends JFrame {

	public enum VideoState {
		DEFAULT, ATTACHED, DETACHED, ATTACHED_TO_CHAT, DETACHED_FROM_CHAT;
	}

	public static final Color COLOR_BG = new Color(215, 232, 236);
	private static final int ATTACH_BTN_INSET = 30;
	private final int ATTACH_BTN_BORDER;
	private static final String INITIAL_TIMER_TEXT = "00:00";
	private static final int SEC = 1000;
	private static final Dimension TIMER_SIZE = new Dimension(60, 26);
	private static final String TITLE_XMPP = "Screen Sharing";
	private static final String TITLE_SIP = "Video Call";
	private static final long serialVersionUID = 9000482520403555918L;
	private static final float RATIO_LOCAL_VIDEO_SIZE = 3.2f;
	private static final Dimension SIZE_REMOTE_VIDEO_DEFAULT = new Dimension(640, 480);
	public static final String NAME_PANE_VIDEO_LAYERED = "mainlayeredVideoPane";
	private static final String NAME_PANEL_LOCAL_VIDEO = "localVideoPanel";
	private static final String NAME_PANEL_VIDEO_ATTACH_DETACH = "attachDetachButtonPanel";
	private static final String NAME_REMOTE_VIDEO_COMPONENT = "remoteVideoComponent";
	private static final String NAME_LOCAL_VIDEO_COMPONENT = "localVideoComponent";
	private static final String NAME_PANEL_CLOSE_BUTTON = "localVideoCloseButtonPanel";
	private static final Dimension SIZE_CLOSE_BTN = new Dimension(20, 20);

	private JButton mHangupBtn;
	private JTextField mDurationTime;
	private JToggleButton mMuteBtn;
	private JToggleButton mPauseBtn;
	private JToggleButton mVideoBtn;
	private JLabel mOpponentName;
	private JLabel mNotificationLabel;
	private JToggleButton mMouseBtn;
	private JButton mCloseLocalVideoBtn;
	private Timer mDurationTimer;
	private Component remoteVideo;
	private Component localVideo;

	private JToggleButton mExpandVideoBtn;
	private JPanel expandVideoBtnPanel;
	private JPanel bottomLeftPanel;
	private JPanel bottomRightPanel;
	private JLayeredPane videoLayeredPane;
	private JPanel localVideoPanel;
	private JPanel localVideoCloseBtnPanel;

	private JPanel containerToAttach;
	private JPanel chatWindowContainerToAttach;
	private Dimension attachedSize; // it is fixed atm, not sure if we should
									// allow stretch main window tho
	private NoVideoAvailablePanel noVideoAvailablePanel;
	private VideoState videoState = VideoState.DEFAULT;
	private ComponentListener resizeVideoPanelListener;
	private VideoWindowManager.TYPE wType;

	public VideoWindow(VideoWindowManager.TYPE type, Container comp, Dimension size) {

		if (!Spark.isMac()){
			ATTACH_BTN_BORDER = 0;
		}else{
			ATTACH_BTN_BORDER = ATTACH_BTN_INSET + 5;
		}
		wType = type;
		containerToAttach = (JPanel) comp;
		attachedSize = size;

		setLocation(500, 100);
		setLayout(new GridBagLayout());

		mDurationTime = new JTextField(INITIAL_TIMER_TEXT);
		mDurationTime.setHorizontalAlignment(JTextField.CENTER);
		mDurationTime.setPreferredSize(TIMER_SIZE);

		mHangupBtn = WidgetBuilder.createButton("/resources/images/call_end.png",
				"/resources/images/call_end_on.png");
		mMuteBtn = WidgetBuilder.createToggleButton("mute_off.png",
				"mute_on.png");
		mPauseBtn = WidgetBuilder.createToggleButton("hold_off.png",
				"hold_on1.png");
		mVideoBtn = WidgetBuilder.createToggleButton("video_off.png",
				"video_on.png");
		mOpponentName = new JLabel();
		Font font = mOpponentName.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		mOpponentName.setFont(boldFont);
		mNotificationLabel = new JLabel("");
		mMouseBtn = WidgetBuilder.createToggleButton("mouse-control_off.png",
				"mouse-control_on.png");
		mCloseLocalVideoBtn = WidgetBuilder.createButton("/resources/images/webcam_close.png",
				"/resources/images/webcam_close.png");
		mExpandVideoBtn = WidgetBuilder.createToggleButton(
				"expand-video_off.png", "expand-video_on.png");

		noVideoAvailablePanel = new NoVideoAvailablePanel();
		noVideoAvailablePanel.setName(NAME_REMOTE_VIDEO_COMPONENT);

		addWindowListener(windowListener());
		resizeVideoPanelListener = resizeVideoComponent();
		if (wType == TYPE.SIP) {
			setTitle(TITLE_SIP);
		} else if (wType == TYPE.XMPP) {
			setTitle(TITLE_XMPP);
		}

		init();

		getContentPane().setBackground(COLOR_BG);
		if (containerToAttach != null) {
			containerToAttach.setBackground(COLOR_BG);
		}
	}

	private void init() {
		GridBagConstraints c = new GridBagConstraints();

		// put video panel first
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(getVideoPanel(SIZE_REMOTE_VIDEO_DEFAULT), c);

		c.fill = GridBagConstraints.NONE;
		// bottom left
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.SOUTHWEST;
		add(getBottomLeftPanel(), c);

		// gap

		// bottom right
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.SOUTHEAST;
		add(getBottomRightPanel(), c);

		pack();
		setVisible(false);
	}

	public void addExpandVideoBtnListener(ActionListener expandVideoBtnAction) {
		mExpandVideoBtn.addActionListener(expandVideoBtnAction);
	}
        
        

	public void setLocalVideoComponent(Component localVideoComponent) {
		this.localVideo = localVideoComponent;
		if (localVideo != null) {
			localVideo.setName(NAME_LOCAL_VIDEO_COMPONENT);
			removeAllMouseListeners(localVideo);
		}
		update();
	}

	public void setRemoteVideoComponent(Component remoteVideoComponent) {
		this.remoteVideo = remoteVideoComponent;
		if (remoteVideo != null) {
			remoteVideo.setName(NAME_REMOTE_VIDEO_COMPONENT);
		}
		update();
	}

	public void setOpponentName(String name) {
		mOpponentName.setText(name);
	}

	public boolean isAttached() {
		return videoState == VideoState.ATTACHED;
	}

	public boolean isDefaultState() {
		return videoState == VideoState.DEFAULT;
	}		

	public boolean isDetached() {
		return videoState == VideoState.DETACHED;
	}

	public boolean isAttachedToChat() {
		return videoState == VideoState.ATTACHED_TO_CHAT;
	}

	public boolean isDetachedFromChat() {
		return videoState == VideoState.DETACHED_FROM_CHAT;
	}
	
	public void resetVideoState() {
		videoState = VideoState.DEFAULT;
	}

	public void detachVideo() {
	    Dimension size = null;
	    Integer width = null;
	    Integer height = null;
		MediaChannel receiverMediaChannel = StreamingManager.getInstance().getReceiverMediaChannel();
		if (receiverMediaChannel != null) {
			width = receiverMediaChannel.getWidth();
			height = receiverMediaChannel.getHeight();
		}
		if(width != null && height != null ) {
			size = GraphicUtils.getScaledDimension(new Dimension(width, height), Toolkit.getDefaultToolkit().getScreenSize());
		}
		Log.warning("Screen size received is: " + size);
		switch (videoState) {
		case ATTACHED:
			detachVideoFromMainWindow(size == null ? SIZE_REMOTE_VIDEO_DEFAULT : size);
			break;

		case ATTACHED_TO_CHAT:
			detachVideoFromChat(size == null ? SIZE_REMOTE_VIDEO_DEFAULT : size);
			break;

		default:
			break;
		}
	}

	public void attachVideo() {
		switch (videoState) {
		case DETACHED:
		case DEFAULT:
			attachVideoToMainWindow();
			break;
		case DETACHED_FROM_CHAT:
			attachVideoToChat();
			break;

		default:
			break;
		}
	}

	private void detachVideoFromMainWindow(final Dimension size) {
		if (containerToAttach == null || isDetached())
			return;
		videoState = VideoState.DETACHED;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				containerToAttach.remove(videoLayeredPane);
				containerToAttach.validate();

				TimerTask task0 = new SwingTimerTask() {
					@Override
					public void doRun() {
						containerToAttach.setVisible(false);
					}
				};
				TaskEngine.getInstance().schedule(task0, 150);

				TimerTask task1 = new SwingTimerTask() {

					@Override
					public void doRun() {
						videoLayeredPane.setPreferredSize(size == null ? SIZE_REMOTE_VIDEO_DEFAULT : size);
						init();
						update();
						setVisible(true);
						UnitemediaRegistry.getInstance().customize();                                                
					}
				};
				TaskEngine.getInstance().schedule(task1, 500);
			}
		});
	}

	public void attachVideoToMainWindow() {
		if (containerToAttach == null || isAttached())
			return;
		chatWindowContainerToAttach = null;
		setVisible(false);
		videoState = VideoState.ATTACHED;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				videoLayeredPane.setPreferredSize(attachedSize);
				localVideoPanel.setSize(getLocalVideoSize(attachedSize));
				updateAttachDetachButtonPanelLocation(attachedSize);

				containerToAttach.add(videoLayeredPane, 0);
				containerToAttach.setVisible(true);
				UnitemediaRegistry.getInstance().customize();
			}
		});
	}

	private void attachVideoToChat() {
		if (chatWindowContainerToAttach == null || isAttachedToChat())
			return;

                if (isDetachedFromChat())
                {
                    bottomRightPanel.remove(mExpandVideoBtn);
                    expandVideoBtnPanel.add(mExpandVideoBtn); 
                }
		setVisible(false);
		videoState = VideoState.ATTACHED_TO_CHAT;
		localVideoPanel.setSize(getLocalVideoSize(attachedSize));
		updateAttachDetachButtonPanelLocation(attachedSize);

		chatWindowContainerToAttach.add(videoLayeredPane, 0);
                                                                		
                chatWindowContainerToAttach.setVisible(true);
		UnitemediaRegistry.getInstance().customize();
	}

	private void detachVideoFromChat(final Dimension size) {
		if (chatWindowContainerToAttach == null || isDetachedFromChat())
			return;
		videoState = VideoState.DETACHED_FROM_CHAT;

		chatWindowContainerToAttach.remove(videoLayeredPane);
		chatWindowContainerToAttach.validate();

		TimerTask task0 = new SwingTimerTask() {
			@Override
			public void doRun() {
				chatWindowContainerToAttach.setVisible(false);
			}
		};
		TaskEngine.getInstance().schedule(task0, 150);

		TimerTask task1 = new SwingTimerTask() {

			@Override
			public void doRun() {
				videoLayeredPane.setPreferredSize(size == null ? SIZE_REMOTE_VIDEO_DEFAULT : size);
				init();
				update();
                                expandVideoBtnPanel.remove(mExpandVideoBtn); 
                                bottomRightPanel.add(mExpandVideoBtn);
				setVisible(true);
				UnitemediaRegistry.getInstance().customize();
			}
		};
		TaskEngine.getInstance().schedule(task1, 500);

	}

	public void addHangupBtnListener(ActionListener actionHangup) {
		mHangupBtn.addActionListener(actionHangup);
	}

	public void addMuteBtnListener(ActionListener actionMute) {
		mMuteBtn.addActionListener(actionMute);
	}

	public void addPauseBtnListener(ActionListener actionPause) {
		mPauseBtn.addActionListener(actionPause);
	}

	public void addVideoOnOffBtnListener(ActionListener actionVideoOnOffListener) {
		mVideoBtn.addActionListener(actionVideoOnOffListener);
		mCloseLocalVideoBtn.addActionListener(actionVideoOnOffListener);
	}

	public void addMouseControlAction(ActionListener actionMouseControl) {
		mMouseBtn.addActionListener(actionMouseControl);
	}

	public void updateStatus(String newStatus) {
		mNotificationLabel.setText(newStatus);
	}

	public void stopDurationTimer() {
		if (mDurationTimer != null)
			mDurationTimer.stop();
	}

	public void restartDurationTimer() {
		if (mDurationTime != null) {
			mDurationTimer = new Timer(SEC, durationClockListener());
		}
		mDurationTimer.restart();
	}
    
	public Component getVideoLayeredPane() {
		return videoLayeredPane;
	}
	
	protected Component getVideoPanel(final Dimension sizeRemoteVideo) {
		if (videoLayeredPane != null)
			return videoLayeredPane;

		videoLayeredPane = new JLayeredPane();
		videoLayeredPane.setOpaque(false);
		videoLayeredPane.setBackground(COLOR_BG);
		videoLayeredPane.setName(NAME_PANE_VIDEO_LAYERED);
		videoLayeredPane.setPreferredSize(sizeRemoteVideo);
		videoLayeredPane.addComponentListener(resizeVideoPanelListener);

		localVideoPanel = new JPanel(new BorderLayout());
		localVideoPanel.setName(NAME_PANEL_LOCAL_VIDEO);
		localVideoPanel.setOpaque(false);
		localVideoPanel.setBackground(COLOR_BG);
		localVideoPanel.setLocation(10, 10);
		videoLayeredPane.add(localVideoPanel, JLayeredPane.PALETTE_LAYER);

		// close button
		localVideoCloseBtnPanel = new JPanel(new BorderLayout());
		localVideoCloseBtnPanel.setName(NAME_PANEL_CLOSE_BUTTON);
		localVideoCloseBtnPanel.setOpaque(false);
		localVideoCloseBtnPanel.add(mCloseLocalVideoBtn);
		localVideoCloseBtnPanel.setSize(SIZE_CLOSE_BTN);
		localVideoCloseBtnPanel.setLocation(localVideoPanel.getLocation().x,
                                                    localVideoPanel.getLocation().y);

		videoLayeredPane.add(localVideoCloseBtnPanel, JLayeredPane.MODAL_LAYER);

		localVideoCloseBtnPanel.setVisible(false);

		// attach / detach button
		expandVideoBtnPanel = new JPanel(new BorderLayout());
		expandVideoBtnPanel.setName(NAME_PANEL_VIDEO_ATTACH_DETACH);
		expandVideoBtnPanel.setOpaque(false);
		expandVideoBtnPanel.add(mExpandVideoBtn);                
		expandVideoBtnPanel.setSize(new Dimension(26, 26));                
		videoLayeredPane.add(expandVideoBtnPanel, JLayeredPane.MODAL_LAYER);
                
                videoLayeredPane.revalidate();
                videoLayeredPane.repaint();
		return videoLayeredPane;
	}

	protected Component getBottomLeftPanel() {
		if (bottomLeftPanel != null)
			return bottomLeftPanel;

		bottomLeftPanel = new JPanel();
		bottomLeftPanel.setBackground(COLOR_BG);
		if (wType == TYPE.SIP) {
			bottomLeftPanel.add(mHangupBtn);
		}
		bottomLeftPanel.add(mDurationTime);
		if (wType == TYPE.SIP) {
			bottomLeftPanel.add(mMuteBtn);
		}
		if (wType == TYPE.SIP) {
			bottomLeftPanel.add(mPauseBtn);
		}
		if (wType == TYPE.SIP) {
			bottomLeftPanel.add(mVideoBtn);
		}
		bottomLeftPanel.add(mOpponentName);
		return bottomLeftPanel;
	}

	protected Component getBottomRightPanel() {
		if (bottomRightPanel != null)
			return bottomRightPanel;
		bottomRightPanel = new JPanel();
                bottomRightPanel.setLayout(new BoxLayout(bottomRightPanel, BoxLayout.X_AXIS));
		bottomRightPanel.setBackground(COLOR_BG);
		bottomRightPanel.add(mNotificationLabel);                
		//TODO uncomment this when remote controls will be available
		//if(wType == TYPE.XMPP) {
			//bottomRightPanel.add(mMouseBtn);	
		//}
		return bottomRightPanel;
	}

	private void update() {
		if (remoteVideo == null && localVideo == null) {
			closeWindow();
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// it happens by default only once
				if (videoState == VideoState.DEFAULT) {
					attachVideo();
				}

				// if any video exists and window should be detached.
				if ((remoteVideo != null || localVideo != null) && isDetached()) {
					setVisible(true);
				}

				// update remote video
				if (remoteVideo != null) {
					removeComponentByName(videoLayeredPane,
							NAME_REMOTE_VIDEO_COMPONENT);
					resizeVideo(remoteVideo, videoLayeredPane.getSize());
					videoLayeredPane.add(remoteVideo,
							JLayeredPane.DEFAULT_LAYER);

				} else {
					removeComponentByName(videoLayeredPane,
							NAME_REMOTE_VIDEO_COMPONENT);
					setVideoUnavailableImage(videoLayeredPane);
				}

				// update local video
				if (localVideo != null) {
					localVideoCloseBtnPanel.setVisible(true);

					Dimension localVideoSize = getLocalVideoSize(videoLayeredPane
							.getPreferredSize());
					localVideoPanel.add(localVideo, 0);
					localVideoPanel.setSize(localVideoSize);
					removeComponentByName(videoLayeredPane,
							NAME_PANEL_LOCAL_VIDEO);
					videoLayeredPane.add(localVideoPanel,
							JLayeredPane.PALETTE_LAYER);
				} else {
					localVideoCloseBtnPanel.setVisible(false);
					removeComponentByName(videoLayeredPane,
							NAME_PANEL_LOCAL_VIDEO);
				}
			}
		});
	}

	public void closeWindow() {
                if (isDetachedFromChat()){
                    mExpandVideoBtn.doClick();
                    return;
                }
                        
		setVisible(false);
		remoteVideo = null;
		localVideo = null;
		videoState = VideoState.DEFAULT;

		if (containerToAttach != null) {

			containerToAttach.setVisible(false);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					containerToAttach.remove(videoLayeredPane);
				}
			});
		}

		if (chatWindowContainerToAttach != null) {
			chatWindowContainerToAttach.setVisible(false);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					if (chatWindowContainerToAttach != null)
						chatWindowContainerToAttach.remove(videoLayeredPane);
				}
			});

		}
		dispose();
	}

	private void resizeVideo(Component video, Dimension size) {
		if (video != null) {			
			video.setBounds(0, 0, size.width-ATTACH_BTN_BORDER, size.height);
		} else if (noVideoAvailablePanel != null) {
			noVideoAvailablePanel.setSize(size);
		}
	}

	private Dimension getLocalVideoSize(Dimension sizeRemoteVideo) {
		if (sizeRemoteVideo == null)
			return new Dimension();

		float w = (float) (sizeRemoteVideo.getWidth() / RATIO_LOCAL_VIDEO_SIZE);
		float h = (float) (sizeRemoteVideo.getHeight() / RATIO_LOCAL_VIDEO_SIZE);
		return new Dimension((int) w, (int) h);
	}

	private ComponentListener resizeVideoComponent() {
		return new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Dimension size = e.getComponent().getSize();
				resizeVideo(remoteVideo, size);
				updateAttachDetachButtonPanelLocation(videoLayeredPane
						.getSize());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		};
	}

	private void removeComponentByName(JComponent panel, String nameToRemove) {
		if (panel == null || StringUtils.isEmpty(nameToRemove))
			return;

		Component[] components = panel.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (nameToRemove.equals(c.getName())) {
				panel.remove(c);
			}
		}
	}

	private void updateAttachDetachButtonPanelLocation(
			final Dimension windowSize) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				expandVideoBtnPanel.setLocation(windowSize.width
						- ATTACH_BTN_INSET, windowSize.height
						- ATTACH_BTN_INSET);
			}
		});

	}

	/**
	 * This timer is a very simple one. Just count min and secs.
	 * */
	private ActionListener durationClockListener() {
		return new ActionListener() {

			private int min;
			private int sec;

			@Override
			public void actionPerformed(ActionEvent e) {
				sec++;
				if (sec > 59) {
					min++;
					sec = 0;
				}
				mDurationTime.setText(String.format("%02d:%02d", min, sec));
			}
		};
	}

	private void removeAllMouseListeners(Component comp) {
		for (int i = 0; i < comp.getMouseListeners().length; i++) {
			comp.removeMouseListener(comp.getMouseListeners()[i]);
		}
	}

	private WindowListener windowListener() {
		return new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		};
	}

	private void setVideoUnavailableImage(JLayeredPane videoPane) {
		noVideoAvailablePanel.setSize(videoLayeredPane.getSize());
		videoLayeredPane.add(noVideoAvailablePanel, JLayeredPane.DEFAULT_LAYER);
	}

	public static class NoVideoAvailablePanel extends JPanel {

		private static final long serialVersionUID = -5974832777309837810L;
		private Image noVideoAvailableBig = GraphicUtils.createImageIcon(
				"/resources/images/no_video_available_big.png").getImage();

		private Image noVideoAvailableSmall = GraphicUtils.createImageIcon(
				"/resources/images/no_video_available_small.png").getImage();
		private Image currentImage;

		public NoVideoAvailablePanel() {
			currentImage = noVideoAvailableSmall;
			setBackground(COLOR_BG);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g2.drawImage(currentImage, 0, 0, getWidth(), getHeight(), 0, 0,
					currentImage.getWidth(null), currentImage.getHeight(null),
					null);
		}

		@Override
		public void setSize(Dimension d) {
			super.setSize(d);

			currentImage = d.getWidth() > 360 ? noVideoAvailableBig
					: noVideoAvailableSmall;
		}
	}

	public void attachVideoTo(final JPanel videoPanel) {

		if (videoPanel == null || (localVideo == null && remoteVideo == null)
				|| videoState == VideoState.ATTACHED_TO_CHAT) {
			return;
		}
		setVisible(false);
		chatWindowContainerToAttach = videoPanel;
		chatWindowContainerToAttach.setBackground(COLOR_BG);
		videoState = VideoState.ATTACHED_TO_CHAT;

		containerToAttach.remove(videoLayeredPane);
		containerToAttach.setVisible(false);
		videoPanel.add(videoLayeredPane, 0);
		videoPanel.setVisible(true);
		
		UnitemediaRegistry.getInstance().customize();
	}

	public boolean hasLocalVideo() {
		return localVideo != null;
	}

	public boolean hasRemoteVideo() {
		return remoteVideo != null;
	}

	public void updateUI() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				if (localVideo != null) {
					localVideo.setVisible(false);
					localVideo.setVisible(true);
				}
				if (remoteVideo != null) {
					remoteVideo.setVisible(false);
					remoteVideo.setVisible(true);
				}
			}
		});
	}

	public VideoWindowManager.TYPE getWType() {
		return wType;
	}
	
}
