package org.ezuce.unitemedia.streaming;

public class ChannelMulticastData implements MultiCastData {
	MediaChannel m_mediaChannel;

	@Override
	public MediaChannel getData() {
		return m_mediaChannel;
	}

	public void setMediaChannel(MediaChannel mediaChannel) {
		m_mediaChannel = mediaChannel;
	}	
}
