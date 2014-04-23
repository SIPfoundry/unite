package org.ezuce.media.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.ui.listeners.CameraListener;
import org.ezuce.media.ui.listeners.HangupCallListener;
import org.ezuce.media.ui.listeners.OnOffHoldCallListener;

/**
 * 
 * @author Razvan
 * @author Vyacheslav Durin
 */
public class VideoWindowManager extends javax.swing.JPanel {
	/**  */
	private static final long serialVersionUID = -4357923589347611932L;

	protected enum TYPE {SIP, XMPP};
	private static final Dimension SIZE_ATTACHED_VIDEO = new Dimension(360, 240);
	private Container containerSIPToAttach;
	private Container containerXMPPToAttach;
	private static VideoWindowManager instance;
	private final Map<String, VideoWindow> mVideoWindows = new HashMap<String, VideoWindow>();
	private final Map<String, VideoWindow> mXMPPVideoWindows = new HashMap<String, VideoWindow>();

	/**
	 * Private ctor.
	 * */
	private VideoWindowManager(Container containerSIPToAttach, Container containerXMPPToAttach) {
		this.containerSIPToAttach = containerSIPToAttach;
		this.containerXMPPToAttach = containerXMPPToAttach;
	}

	/**
	 * @param containerToAttach
	 *            - the client's main window's panel where video windows will be
	 *            attached to.
	 * @return new instance of this class
	 */
	public synchronized static VideoWindowManager getInstance(
			Container containerSIPToAttach, Container containerXMPPToAttach) {
		if (instance == null)
			instance = new VideoWindowManager(containerSIPToAttach, containerXMPPToAttach);

		return instance;
	}
	
	public synchronized static VideoWindowManager getCreatedInstance() {
		return instance;
	}

	/**
	 * Create a window for a video call.
	 * */
	private VideoWindow createSIPVideoWindow(final String callId) {
		final VideoWindow videoWindow = new VideoWindow(TYPE.SIP, containerSIPToAttach,
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
		videoWindow.addExpandVideoBtnListener(attachDetachSIPWindowAction(callId));
		videoWindow.addMuteBtnListener(muteAction(callId));
		videoWindow.addMouseControlAction(mouseControlAction(callId));

		return videoWindow;
	}
	
	/**
	 * Create a window for a video call.
	 * */
	private VideoWindow createXMPPVideoWindow(final String jid) {
		final VideoWindow videoWindow = new VideoWindow(TYPE.XMPP, containerXMPPToAttach,
				SIZE_ATTACHED_VIDEO);		
		videoWindow.addExpandVideoBtnListener(attachDetachXMPPWindowAction(jid));
		//TODO this is a screen sharing video - we add a mouse control action
		//videoWindow.addMouseControlAction(mouseControlAction(callId));
		return videoWindow;
	}	

	/**
	 * Add remote video onto the window with the given callId.
	 * 
	 * @param video
	 * @param callId
	 */
	public void addRemoteSIPVideo(final Component video, final String callId) {
		addRemoteVideo(TYPE.SIP, mVideoWindows, video, callId);
	}
	
	public void addRemoteXMPPVideo(final Component video, final String jid) {
		addRemoteVideo(TYPE.XMPP, mXMPPVideoWindows, video, jid);
	}
	
	private void addRemoteVideo(TYPE type, Map<String, VideoWindow> map, final Component video, final String key) {
		if (!isArgsValid(video, key))
			return;

		VideoWindow window = getVideoWindow(type, map, key);
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

	public void removeRemoteSIPVideo(String callId) {
		removeRemote(TYPE.SIP, mVideoWindows, callId);
	}
	
	public void removeRemoteXMPPVideo(String jid) {
		removeRemote(TYPE.XMPP, mXMPPVideoWindows, jid);
	}
	
	private void removeRemote(TYPE type, Map<String, VideoWindow> map, String key) {
		if (!isArgsValid(key))
			return;
		VideoWindow window = getVideoWindow(type, map,key);
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
		return getVideoWindow(TYPE.SIP, mVideoWindows, callId);
	}
	
	public Collection<VideoWindow> getExistingSIPVideoWindows() {
		return mVideoWindows.values();
	}
	
	/**
	 * Return videoWindow from cache. Create a new window if needed.
	 * */
	public VideoWindow getVideoWindowByXMPPJid(String jid) {
		return getVideoWindow(TYPE.XMPP, mXMPPVideoWindows, jid);
	}
	
	public Collection<VideoWindow> getExistingXMPPVideoWindows() {
		return mXMPPVideoWindows.values();
	}
	
	private VideoWindow getVideoWindow(TYPE type, Map<String, VideoWindow> map, String key) {
		if (StringUtils.isEmpty(key))
			return null;
		VideoWindow window = map.get(key);
		if (window == null) {
			window = (type == TYPE.XMPP) ? createXMPPVideoWindow(key) : createSIPVideoWindow(key);
			map.put(key, window);
		}
		return window;
		
	}
	
	private VideoWindow getExistingVideoWindow(TYPE type, Map<String, VideoWindow> map, String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return map.get(key);
	}

	public VideoWindow getExistingVideoWindowByCallID(String callId) {
		return getExistingVideoWindow(TYPE.SIP, mVideoWindows, callId);
	}	
	
	public VideoWindow getExistingVideoWindowByXMPPJid(String jid) {
		return getExistingVideoWindow(TYPE.XMPP, mXMPPVideoWindows, jid);
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
	
	private ActionListener attachDetachSIPWindowAction(final String callId) {
		return attachDetachWindowAction(TYPE.SIP, mVideoWindows, callId);
	}
	
	private ActionListener attachDetachXMPPWindowAction(final String jid) {
		return attachDetachWindowAction(TYPE.XMPP, mXMPPVideoWindows, jid);
	}

	private ActionListener attachDetachWindowAction(final TYPE type, final Map<String, VideoWindow> map, final String key) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if ((type == TYPE.SIP && containerSIPToAttach == null) || (type == TYPE.XMPP && containerXMPPToAttach == null)) {
					return;
				}

				VideoWindow window = getVideoWindow(type, map, key);

				// detach
				if (window.isAttached() || window.isAttachedToChat()) {

					window.detachVideo();
					return;
				}

				// attach
				window.attachVideo();
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
		if (window == null || !hasVideo())
			return;

		window.attachVideoToMainWindow();
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
