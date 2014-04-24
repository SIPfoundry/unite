package org.ezuce.unitemedia.streaming;

public class MediaChannel {
	private String m_host;
	private int m_rtpPort;
	private int m_rtcpPort;
	private int m_expire;
	private int[] m_SSRCs;
	private Integer m_width;
	private Integer m_height;
	
	public MediaChannel(String host, int rtpPort, int rtcpPort, int expire) {
		m_host = host;
		m_rtpPort = rtpPort;
		m_rtcpPort = rtcpPort;
		m_expire = expire;
	}
	
	public MediaChannel(String host, int rtpPort, int rtcpPort, int expire, int width, int height) {
		this(host, rtpPort, rtcpPort, expire);
		m_width = width;
		m_height = height;
	}
	
	public String getHost() {
		return m_host;
	}
	public void setHost(String host) {
		m_host = host;
	}
	public int getRtpPort() {
		return m_rtpPort;
	}
	public void setRtpPort(int rtpPort) {
		m_rtpPort = rtpPort;
	}
	public int getRtcpPort() {
		return m_rtcpPort;
	}
	public void setRtcpPort(int rtcpPort) {
		m_rtcpPort = rtcpPort;
	}
	public int getExpire() {
		return m_expire;
	}
	public void setExpire(int expire) {
		m_expire = expire;
	}
	public int[] getSSRCs() {
		return m_SSRCs;
	}
	public void setSSRCs(int[] ssrcs) {
		m_SSRCs = ssrcs;
	}

	public Integer getWidth() {
		return m_width;
	}
	
	public Integer getHeight() {
		return m_height;
	}	
}
