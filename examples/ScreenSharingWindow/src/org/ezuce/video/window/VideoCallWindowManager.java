package org.ezuce.video.window;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Vyacheslav Durin
 */
public class VideoCallWindowManager extends javax.swing.JPanel {
	/**  */
	private static final long serialVersionUID = -4357923589347611932L;

	private static final Dimension SIZE_ATTACHED_VIDEO = new Dimension(360, 240);
	private Container containerToAttach;
	private static VideoCallWindowManager instance;
	private final Map<String, VideoWindow> mVideoWindows = new HashMap<String, VideoWindow>();

	/**
	 * Private ctor.
	 * */
	private VideoCallWindowManager(Container containerToAttach) {
		this.containerToAttach = containerToAttach;
	}

	/**
	 * @param containerToAttach
	 *            - the client's main window's panel where video windows will be
	 *            attached to.
	 * @return new instance of this class
	 */
	public synchronized static VideoCallWindowManager getInstance(
			Container containerToAttach) {
		if (instance == null)
			instance = new VideoCallWindowManager(containerToAttach);

		return instance;
	}

	/**
	 * Create a window for a video call.
	 * */
	private VideoWindow createVideoWindow(final String callId) {
		final VideoWindow videoWindow = new VideoWindow(containerToAttach,
				SIZE_ATTACHED_VIDEO);
		videoWindow.addHangupBtnListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new HangupCallListener(callId).actionPerformed(e);
				videoWindow.closeWindow();
				mVideoWindows.remove(callId);
			}
		});
		videoWindow.addPauseBtnListener(new OnOffHoldCallListener(callId));
		videoWindow.addVideoOnOffBtnListener(new CameraListener(callId));
		videoWindow.addExpandVideoBtnListener(attachDetachWindowAction(callId));
		videoWindow.addMuteBtnListener(muteAction(callId));
		videoWindow.addMouseControlAction(mouseControlAction(callId));

		return videoWindow;
	}

	/**
	 * Add remote video onto the window with the given callId.
	 * 
	 * @param video
	 * @param callId
	 */
	public void addRemoteVideo(final Component video, final String callId) {
		if (!isArgsValid(video, callId))
			return;

		VideoWindow window = getVideoWindowByCallID(callId);
		window.setRemoteVideoComponent(video);
	}

	public void addLocalVideo(Component video, String callId) {
		if (!isArgsValid(video, callId))
			return;
		VideoWindow window = getVideoWindowByCallID(callId);
		window.setLocalVideoComponent(video);
	}

	public void removeLocalVideo(String callId) {
		if (!isArgsValid(callId))
			return;

		VideoWindow window = getVideoWindowByCallID(callId);
		window.setLocalVideoComponent(null);
	}

	public void removeAllLocalVideos() {
		for (VideoWindow window : mVideoWindows.values()) {
			window.setLocalVideoComponent(null);
		}
	}

	public void removeAllRemoteVideos() {
		for (VideoWindow window : mVideoWindows.values()) {
			window.setRemoteVideoComponent(null);
		}
	}

	public void removeRemoteVideo(String callId) {
		if (!isArgsValid(callId))
			return;
		VideoWindow window = getVideoWindowByCallID(callId);
		window.setRemoteVideoComponent(null);

	}

	/**
	 * Check whether all objects are not null and all string are not empty.
	 * */
	private boolean isArgsValid(Object... args) {
		for (Object arg : args) {
			if (arg == null)
				return false;

			if ((arg instanceof String) && "".equals((String) arg))
				return false;

		}
		return true;
	}

	/**
	 * Return videoWindow from cache. Create a new window if needed.
	 * */
	public VideoWindow getVideoWindowByCallID(String callId) {
		if (callId == null || "".equals(callId))
			return null;

		VideoWindow window = mVideoWindows.get(callId);
		if (window == null) {
			window = createVideoWindow(callId);
			mVideoWindows.put(callId, window);
		}
		return window;
	}

	private ActionListener mouseControlAction(final String callId) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: to be implemented
				System.out
						.println("TODO: VideoCallWindowManager#mouseControlAction("
								+ callId + ")#actionPerformed()");
			}
		};
	}

	private ActionListener muteAction(final String callId) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: to be implemented
				System.out.println("TODO: VideoCallWindowManager#muteAction("
						+ callId + ")#actionPerformed()");
			}
		};
	}

	private ActionListener attachDetachWindowAction(final String callId) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (containerToAttach == null)
					return;

				VideoWindow window = getVideoWindowByCallID(callId);

				// detach
				if (window.isAttached() || window.isAttachedToChat()) {

					window.detachVideo();
					UIPhoneManager.getInstance().updateUi(null);
					return;
				}

				// attach
				window.attachVideo();
				UIPhoneManager.getInstance().updateUi(null);
			}
		};
	}

	public boolean hasVideo() {
		VideoWindow window = getVideoWindowByCallID(getConnectedCallId());
		return window != null
				&& (window.hasLocalVideo() || window.hasRemoteVideo());
	}

	public void attachVideoToMainWindow() {
		String callID = getConnectedCallId();
		VideoWindow window = getVideoWindowByCallID(callID);
		if (window == null)
			return;
		window.attachVideo();
	}

	public String getConnectedCallId() {
		if (PhoneManager.getInstance().getConnectedCall() == null)
			return null;

		if (PhoneManager.getInstance().getConnectedCall().getCall() == null)
			return null;

		String callID = PhoneManager.getInstance().getConnectedCall().getCall()
				.getCallID();
		return callID;
	}
}
