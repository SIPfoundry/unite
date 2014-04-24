package org.ezuce.unitemedia.streaming;

import java.awt.Component;

public class VideoMultiCastData implements MultiCastData {
	Component m_videoComponent;
	
	@Override
	public Component getData() {
		return m_videoComponent;
	}

	public void setVideoComponent(Component videoComponent) {
		m_videoComponent = videoComponent;
	}	
}
