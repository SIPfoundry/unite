package org.ezuce.unitemedia.listener;

import java.awt.Component;

import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEventType;
import org.ezuce.unitemedia.streaming.VideoMultiCastData;
import org.ezuce.unitemedia.streaming.XMPPUser;
import org.jitsi.util.event.VideoEvent;
import org.jitsi.util.event.VideoListener;

public class VideobridgeVideoListener implements VideoListener {
	private XMPPUser m_user;
	
	public VideobridgeVideoListener(XMPPUser user) {
		m_user = user;
	}
	
	@Override
	public void videoAdded(VideoEvent videoEvent) {
		Component comp = videoEvent.getVisualComponent();
		System.out.println("VIDEO ********** ADDED" + comp);
		VideoMultiCastData data = new VideoMultiCastData();
		data.setVideoComponent(comp);
		UniteXMPPVideoEvent uniteEvent = new UniteXMPPVideoEvent(UniteXMPPVideoEventType.RECEIVER_STREAM_STARTED, data);
		System.out.println("MIRRRRRRRR " + m_user.getStreamingConnection().isDesktopSharing());
		if(m_user.getStreamingConnection().isDesktopSharing()) {
			VideobridgeDesktopControlListener listener = new VideobridgeDesktopControlListener();
			comp.addMouseListener(listener);
			comp.addMouseMotionListener(listener);
		}
		m_user.notify(uniteEvent);	
	}

	@Override
	public void videoRemoved(VideoEvent videoEvent) {
		System.out.println("VIDEO IS REMOVED ****************");
		VideoMultiCastData data = new VideoMultiCastData();
		data.setVideoComponent(videoEvent.getVisualComponent());
		UniteXMPPVideoEvent uniteEvent = new UniteXMPPVideoEvent(UniteXMPPVideoEventType.RECEIVER_STREAM_ENDED, data);
		m_user.setMediaChannel(null);
		m_user.notify(uniteEvent);
	}

	@Override
	public void videoUpdate(VideoEvent videoEvent) {
		System.out.println("VIDEO IS UPDATED **" + videoEvent.getVisualComponent());
	}

}
