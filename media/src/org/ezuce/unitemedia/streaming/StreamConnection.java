package org.ezuce.unitemedia.streaming;

import java.awt.Dimension;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.media.CaptureDeviceInfo;

import org.ezuce.unitemedia.context.DeviceContext;
import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.listener.VideobridgeVideoListener;
import org.jitsi.impl.neomedia.MediaUtils;
import org.jitsi.impl.neomedia.codec.video.h264.JNIEncoder;
import org.jitsi.impl.neomedia.device.MediaDeviceImpl;
import org.jitsi.impl.neomedia.format.VideoMediaFormatImpl;
import org.jitsi.service.neomedia.DefaultStreamConnector;
import org.jitsi.service.neomedia.MediaDirection;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.neomedia.MediaStream;
import org.jitsi.service.neomedia.MediaStreamTarget;
import org.jitsi.service.neomedia.MediaType;
import org.jitsi.service.neomedia.MediaUseCase;
import org.jitsi.service.neomedia.QualityPreset;
import org.jitsi.service.neomedia.StreamConnector;
import org.jitsi.service.neomedia.VideoMediaStream;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.service.neomedia.format.MediaFormat;

public class StreamConnection {
	private int m_connectorRTPPort;
	private int m_connectorRTCPPort;
	private int m_targetRTPPort;
	private int m_targetRTCPPort;
	private String m_targetHost;
	private DirectionType m_directionType;
	private StreamType m_streamType;
	private VideobridgeVideoListener m_videoListener;
	private StreamConnector m_connector;
	private MediaStream m_mediaStream;
	private Dimension m_screenSize;
	private Set<String> m_channelIds = new TreeSet<String>();
	
	public StreamConnection( 
			DirectionType directionType, int connectorRTPPort, int connectorRTCPPort, StreamType streamType) throws Exception {
		m_directionType = directionType;
		m_connectorRTPPort = connectorRTPPort;
		m_connectorRTCPPort = connectorRTCPPort;
		m_streamType = streamType;

		System.out.println("MIRCEA HOST " + m_targetHost + " " + m_connectorRTPPort + " " + m_connectorRTCPPort);
		MediaService service = ServiceContext.getMediaService();
		MediaDevice device = DeviceContext.getMediaDevice(service, m_streamType);
		
		m_mediaStream = service.createMediaStream(device);
		MediaFormat format = null;
		if (streamType == StreamType.audio) { 
			format = MediaUtils.getMediaFormat("PCMU", 8000);
		} else {
			format = MediaUtils.getMediaFormat("H264", VideoMediaFormatImpl.DEFAULT_CLOCK_RATE);
		}
		
		m_mediaStream.addDynamicRTPPayloadType(StreamUtils.getDynamicRTPPayloadType(m_streamType), format);			
		m_mediaStream.setFormat(format);
		m_mediaStream.setDirection(getMediaDirection(m_directionType));
	}
	
	/**
	 * This connection method should be used by the sender - it connects to videobridge host and sends stream data
	 * @param targetRTPPort
	 * @param targetRTCPPort
	 * @param targetHost
	 * @throws Exception
	 */
	public void connect(int targetRTPPort, int targetRTCPPort, String targetHost) throws Exception {
		if (m_connector != null) {
			System.out.println("Connector opened. Connection already opened");
			return;
		}
		m_targetRTPPort = targetRTPPort;
		m_targetRTCPPort = targetRTCPPort;
		m_targetHost = targetHost;
		System.out.println("CONNECTOR PORTS " + m_connectorRTPPort + " VVV " + m_connectorRTCPPort);				
		m_connector =  new DefaultStreamConnector(new DatagramSocket(m_connectorRTPPort), 
				new DatagramSocket(m_connectorRTCPPort));
		m_mediaStream.setConnector(m_connector);
		m_mediaStream.setTarget(new MediaStreamTarget(new InetSocketAddress(m_targetHost, m_targetRTPPort), 
				new InetSocketAddress(m_targetHost, m_targetRTCPPort)));

		m_mediaStream.start();
	}
	
	/**
	 * This connection method should be used to connect for receiving data. 
	 * It first notifies the videobridge host to relay stream towards the receiver and then connects to receive the stream data
	 */
	public void notifyAndConnect(int targetRTPPort, int targetRTCPPort, String targetHost) throws Exception {
		notifyVideobridgeToRelayStream(targetRTPPort, targetRTCPPort, targetHost);
		connect(targetRTPPort, targetRTCPPort, targetHost);
	}
	
	private void notifyVideobridgeToRelayStream(int rtpPort, int rtcpPort, String host) throws Exception {
		//just send an empty packet to make sure that videobridge locks the channel you are using
		//and starts relaying the data to invitee
		System.out.println("MIRCEA send empty packets to videobridge to ensure video RELAY "+rtpPort);
		DatagramSocket ds1 = new DatagramSocket(m_connectorRTPPort);
		DatagramPacket emptyPacket1 = new DatagramPacket(new byte[0], 0, 
				InetAddress.getByName(host), rtpPort);
		ds1.send(emptyPacket1);
		
		DatagramSocket ds2 = new DatagramSocket(m_connectorRTCPPort);
		DatagramPacket emptyPacket2 = new DatagramPacket(new byte[0], 0, 
				InetAddress.getByName(host), rtcpPort);
		ds2.send(emptyPacket2);
		ds1.close();
		ds2.close();		
		System.out.println("MIRCEA Register video listener");
		((VideoMediaStream)m_mediaStream).addVideoListener(m_videoListener);
				
	}
	
	public void close() {
		System.out.println("STOP MEDIA STREAM");		
		if (m_mediaStream != null) {
			if (m_mediaStream instanceof VideoMediaStream && m_videoListener != null) {
				System.out.println("Remove VIDEO LISTENER *********");
				((VideoMediaStream)m_mediaStream).removeVideoListener(m_videoListener);
			}
			if (m_mediaStream.isStarted()) {
				m_mediaStream.stop();
			}
			if(m_connector != null) {
				m_mediaStream.close();
			}
		}
		if(m_connector != null) {
			m_connector.close();
			m_connector = null;
		}
	}
	
	private MediaDirection getMediaDirection(DirectionType directionType) {
		switch (directionType) {
			case sendonly :
				return MediaDirection.SENDONLY;
			case receiveonly :
				return MediaDirection.RECVONLY;
			case sendandreceive :
				return MediaDirection.SENDRECV;
			default :
				return MediaDirection.INACTIVE;
		}
	}
	
	public boolean isVideo() {
		return StreamUtils.isVideo(m_streamType);
	}
	
	public boolean isDesktopSharing() {
		return StreamUtils.isDesktopSharing(m_streamType);
	}

	public int getConnectorRTPPort() {
		return m_connectorRTPPort;
	}

	public int getConnectorRTCPPort() {
		return m_connectorRTCPPort;
	}

	public int getTargetRTPPort() {
		return m_targetRTPPort;
	}

	public int getTargetRTCPPort() {
		return m_targetRTCPPort;
	}

	public String getTargetHost() {
		return m_targetHost;
	}

	protected DirectionType getDirectionType() {
		return m_directionType;
	}

	protected MediaStream getMediaStream() {
		return m_mediaStream;
	}

	public StreamType getStreamType() {
		return m_streamType;
	}
	
	public void addChannelId(String id) {
		m_channelIds.add(id);
	}
		
	protected Set<String> getChannelIds() {
		return m_channelIds;
	}
	
	protected void clearChanelIds() {
		m_channelIds.clear();
	}

	public void setVideoListener(VideobridgeVideoListener videoListener) {
		m_videoListener = videoListener;
	}

	public boolean isStreaming() {
		return m_mediaStream != null && m_mediaStream.isStarted();
	}

	public Dimension getScreenSize() {
		return m_screenSize;
	}

	public void setScreenSize(Dimension screenSize) {
		m_screenSize = screenSize;
	}	
}