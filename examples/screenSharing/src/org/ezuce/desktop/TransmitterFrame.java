package org.ezuce.desktop;

import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.ezuce.unitemedia.streaming.DirectionType;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.ezuce.unitemedia.streaming.MultiCastData;
import org.ezuce.unitemedia.streaming.ParticipantMultiCastData;
import org.ezuce.unitemedia.streaming.StreamType;
import org.ezuce.unitemedia.streaming.Streamer;
import org.ezuce.unitemedia.streaming.XMPPUser;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

public class TransmitterFrame extends javax.swing.JFrame implements Observer {
	private static final String VIDEOBRIDGE_ADDRESS = "192.168.7.100";
	
	private final Action sendVideoAction = new SendVideoAction();
	private final Action stopAction = new StopAction();
	private XMPPUser m_xmppUser;
	private Streamer m_streamer = new Streamer();
	private final Action sendDesktopAction = new SendDesktopAction();
	
	public TransmitterFrame() {
		init();
		XMPPConnection conn = createConnection();
		m_xmppUser = new XMPPUser(conn, "192.168.7.100", "jitsi-videobridge.ezuce.ro", DirectionType.sendandreceive, 2000, 2001, 0, 0);
		m_xmppUser.addObserver(this);
	}
	
	private void init() {
		setTitle("Transmitter");
		
		JButton sendVideo = new JButton("Start");
		sendVideo.setAction(sendVideoAction);
		
		JButton stop = new JButton("Stop");
		stop.setAction(stopAction);
		
		JButton sendDesktop = new JButton("Send Desktop");
		sendDesktop.setAction(sendDesktopAction);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(sendDesktop, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sendVideo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(stop, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(119)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(sendVideo, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(stop, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(sendDesktop)
					.addContainerGap(86, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);

	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("MIRCEA OBSERVABLE " + arg);
		UniteXMPPVideoEvent event = (UniteXMPPVideoEvent)arg;
		System.out.println("MIRCEA event " + event.getMultiCastData() + " QQQ " + event.getEventType());
		MultiCastData mcData = event.getMultiCastData();
		switch (event.getEventType()) {
		case TRANSMITTER_STREAM_INITIALIZED:
			try {
				MediaChannel videoChannel = null;
				if (mcData != null) {
					videoChannel = (MediaChannel)mcData.getData();
					System.out.println("Received channel MIRCEA " + videoChannel.getRtpPort() + " XXX " + videoChannel.getRtcpPort());
					System.out.println("UPDATE EVENIMENTUL LUI PESTE XXXCCC: " + event.getEventType());
					System.out.println("TRANSMITTER STREAM INITIALIZED, START SENDING DATA TO ............" + videoChannel.getHost());
					m_xmppUser.getStreamingConnection().connect(videoChannel.getRtpPort(), 
							videoChannel.getRtcpPort(), VIDEOBRIDGE_ADDRESS);					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
			break;
		case TRANSMITTER_STREAM_DENIED:
			ParticipantMultiCastData pmData = (ParticipantMultiCastData)mcData;
			System.out.println("The following bastard denied your stream!" + pmData.getData());
			break;
		case TRANSMITTER_STREAM_STARTED:
			System.out.println("YOUR STREAM STARTED TO BE SENDING TO THE VIDEOBRIDGE");
			break;
		case TRANSMITTER_STREAM_ENDED:
			System.out.println("YOUR STREAM HAS ENDED");
			break;
		}
				
			
	}
	
	public XMPPConnection createConnection() {
		try {
			System.out.println("Create connection");
			ConnectionConfiguration cc = new ConnectionConfiguration(VIDEOBRIDGE_ADDRESS);		
			XMPPConnection conn = new XMPPConnection(cc);
			conn.connect();
			conn.login("200", "11111111");
			System.out.println("CICI " + conn.getUser());
			return conn;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	private void startSendingVideoCamera(XMPPUser xmppUser) {
		try {
			System.out.println("start screen sharing");
		
			m_streamer.startStreaming(xmppUser, 
					StreamType.cameravideo, "201@ezuce.ro/Smack", "202@ezuce.ro/Smack", "203@ezuce.ro/Smack");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void startSendingDesktop(XMPPUser xmppUser) {
		try {
			System.out.println("start screen sharing");
		
			m_streamer.startStreaming(xmppUser, 
					StreamType.desktopVideo, "201@ezuce.ro/Smack", "202@ezuce.ro/Smack", "203@ezuce.ro/Smack");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	private void stop(XMPPUser xmppUser) {
		try {
			m_streamer.stopStreaming(xmppUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SendVideoAction extends AbstractAction {
		public SendVideoAction() {
			putValue(NAME, "Send Video");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			startSendingVideoCamera(m_xmppUser);
		}
	}
	
	private class StopAction extends AbstractAction {
		public StopAction() {
			putValue(NAME, "Stop");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			stop(m_xmppUser);
		}
	}
	private class SendDesktopAction extends AbstractAction {
		public SendDesktopAction() {
			putValue(NAME, "Send Desktop");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			startSendingDesktop(m_xmppUser);
		}
	}
}
