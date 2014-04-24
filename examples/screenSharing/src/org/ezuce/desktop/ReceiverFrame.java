package org.ezuce.desktop;

import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.ezuce.unitemedia.streaming.ChannelMulticastData;
import org.ezuce.unitemedia.streaming.DirectionType;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.ezuce.unitemedia.streaming.MultiCastData;
import org.ezuce.unitemedia.streaming.ParticipantMultiCastData;
import org.ezuce.unitemedia.streaming.StreamConnection;
import org.ezuce.unitemedia.streaming.StreamType;
import org.ezuce.unitemedia.streaming.Streamer;
import org.ezuce.unitemedia.streaming.XMPPUser;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ReceiverFrame extends javax.swing.JFrame implements Observer {
	
	private static final String XMPP_ADDRESS = "192.168.7.100";
	
	private XMPPUser m_xmppUser;
	private String m_userName;
	private String m_password;
	private int m_rtpPort;
	private int m_rtcpPort;
	private JPanel m_panel;
	private Streamer m_streamer = new Streamer();
	UniteXMPPVideoEvent m_currentEvent;
	
	public ReceiverFrame(String username, String password, int rtpPort, int rtcpPort) {
		m_userName = username;
		m_password = password;
		m_rtpPort = rtpPort;
		m_rtcpPort = rtcpPort;
		
		XMPPConnection conn;
		try {
			conn = createConnection();
			m_xmppUser = new XMPPUser(conn, "192.168.7.100", "jitsi-videobridge.ezuce.ro", DirectionType.sendandreceive, 0, 0, m_rtpPort, m_rtcpPort);
			m_xmppUser.addObserver(this);
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			getContentPane().setLayout(gridBagLayout);
			
			m_panel = new JPanel();
			System.out.println("MIRCEA put panel");
			m_panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 10;
			gbc_panel.gridheight = 7;
			gbc_panel.insets = new Insets(0, 0, 5, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 0;
			getContentPane().add(m_panel, gbc_panel);
			
			JButton acceptButton = new JButton("ACCEPT");
			acceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						MultiCastData mcData = m_currentEvent.getMultiCastData();
						MediaChannel videoChannel = (MediaChannel)mcData.getData();		
						//The StreamType will depend on the content to receive. We are ready to receive the stream
						//The client will just need to connect to channel port/host
						//TODO change here if other stream type needed
						StreamConnection streamConn = new StreamConnection(DirectionType.receiveonly, 
								m_xmppUser.getReceiverRTPPort(), m_xmppUser.getReceiverRTCPPort(), StreamType.desktopVideo);						
						streamConn.notifyAndConnect(videoChannel.getRtpPort(), 
								videoChannel.getRtcpPort(), videoChannel.getHost());
						m_xmppUser.setStreamingConnection(streamConn);
					} catch (Exception e) {
						System.out.println("Exception during connect....");
						e.printStackTrace();
					}
				}
			});
			GridBagConstraints gbc_acceptButton = new GridBagConstraints();
			gbc_acceptButton.insets = new Insets(0, 0, 5, 5);
			gbc_acceptButton.gridx = 4;
			gbc_acceptButton.gridy = 7;
			getContentPane().add(acceptButton, gbc_acceptButton);
			
			JButton btnDeny = new JButton("DENY");
			btnDeny.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("Deny from " + m_xmppUser.getFromJID());
					m_streamer.rejectReceiving(m_xmppUser);
				}
			});
			GridBagConstraints gbc_btnDeny = new GridBagConstraints();
			gbc_btnDeny.insets = new Insets(0, 0, 5, 5);
			gbc_btnDeny.gridx = 8;
			gbc_btnDeny.gridy = 7;
			getContentPane().add(btnDeny, gbc_btnDeny);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("MIRCEA event RECEIVING ********* " + arg1);
		UniteXMPPVideoEvent event = (UniteXMPPVideoEvent)arg1;
		m_currentEvent = event;
		MultiCastData mcData;
		switch (event.getEventType()) {
			case RECEIVER_STREAM_INITIALIZED:		
				//A channel is associated to this client and streaming already started
				//Open a connection to receive data
				System.out.println("Accept or deny ?");
				break;
			case RECEIVER_STREAM_STARTED:
				mcData = event.getMultiCastData();
				System.out.println("Stream STARTED MIRCEA ^%%%%%%%%%%% "+mcData.getData());
    			m_panel.add((Component)mcData.getData());
    			m_panel.revalidate();
				pack();
				setVisible(true);
				break;
			case RECEIVER_STREAM_ENDED:
				mcData = event.getMultiCastData();
				System.out.println("StreamEnded");
				m_panel.remove((Component)mcData.getData());
    			m_panel.revalidate();
				pack();
				setVisible(true);
				System.out.println("Close connection FOR : " + m_userName);
				m_xmppUser.getStreamingConnection().close();
				m_xmppUser.setStreamingConnection(null);
				break;
		}
		
	}
	
	public XMPPConnection createConnection() throws XMPPException {
		ConnectionConfiguration cc = new ConnectionConfiguration(XMPP_ADDRESS);		
		XMPPConnection conn = new XMPPConnection(cc);
		conn.connect();
		conn.login(m_userName, m_password);
		System.out.println("Connection created " + conn.getUser());
		return conn;
	}

}
