package org.ezuce.media.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.ezuce.media.ui.listeners.AbstractStopScreenSharingListener;
import org.ezuce.media.ui.listeners.AcceptScreenSharingListener;
import org.ezuce.unitemedia.streaming.MediaChannel;
import org.jivesoftware.spark.util.log.Log;


public class ScreenSharingPanel extends JPanel {

	private static final String ACTIVE_TEXT = "Screen Sharing is active";
	private static final String CONNECTING_TEXT = "Screen Sharing is connecting...";
	private static final String NOTIFICATION_TEXT = "Accept Screen Sharing?";
	private static final ImageIcon ICO_SS_ACTIVE = GraphicUtils
			.createImageIcon("/resources/images/screen-sharing_active.png");
	private static final ImageIcon ICO_CONNECTION = GraphicUtils
			.createImageIcon("/resources/images/video_is_connecting.png");

	private static final long serialVersionUID = 2717757661194527146L;
	private static final Color G_COLOR_NOTIFICATION = new Color(250, 245, 125);
	private static final Color G_COLOR_ACTIVE = new Color(169, 206, 212);
	private static final Font G_NOTIFICATION_FONT = new Font("Tahoma",
			Font.PLAIN, 14);
	private JLabel mCurrentIco;
	private JLabel mSharingIcoNotification;
	private JLabel mSharingIcoInProgress;
	private JLabel mStatusLabel;
	private JButton mAcceptBtn;
	private JButton mDeclineBtn;
    private JButton btnTglMouse;
    private JTextField txtDuration;
    
	private Color mCurrentColor = G_COLOR_ACTIVE;
	
	private AbstractStopScreenSharingListener stopScreenSharingListener = null;
	private AcceptScreenSharingListener acceptScreenSharingListener = null;	

	public ScreenSharingPanel() {
		mSharingIcoNotification = new JLabel(ICO_CONNECTION);
		mSharingIcoNotification.setFont(G_NOTIFICATION_FONT);
		mSharingIcoInProgress = new JLabel(ICO_SS_ACTIVE);
		
		mAcceptBtn = WidgetBuilder.createButton("/resources/images/start_off.png", "/resources/images/start_on.png");
		mDeclineBtn = WidgetBuilder.createButton("/resources/images/close_off.png", "/resources/images/close_on.png");
		btnTglMouse = WidgetBuilder.createButton("/resources/images/mouse-control_off.png", "/resources/images/mouse-control_on.png");
		txtDuration = WidgetBuilder.createJTextField(20);
		
		mStatusLabel = new JLabel();

		setBackground(G_COLOR_ACTIVE);
		setLayout(new GridBagLayout());
		addAcceptScreenSharingAction(new AcceptScreenSharingListener());
	}

	public void updateAcceptScreenSharing() {
		removeAll();
		stopScreenSharingListener.setCanReject(true);
		mCurrentIco = mSharingIcoNotification;
		mStatusLabel.setText(NOTIFICATION_TEXT);
		mCurrentColor = G_COLOR_NOTIFICATION;
		buildPanel(true);
		revalidate();
		repaint();
	}
	
	public void updateConnectingScreenSharing() {
		removeAll();
		stopScreenSharingListener.setCanReject(false);
		mCurrentIco = mSharingIcoInProgress;
		mStatusLabel.setText(CONNECTING_TEXT);
		mCurrentColor = G_COLOR_ACTIVE;
		buildPanel(false);
		revalidate();
		repaint();
	}	

	public void updateEnabledScreenSharing() {
		removeAll();
		stopScreenSharingListener.setCanReject(false);
		mCurrentIco = mSharingIcoInProgress;
		mStatusLabel.setText(ACTIVE_TEXT);
		mCurrentColor = G_COLOR_ACTIVE;
		buildPanel(false);
		revalidate();
		repaint();
	}

	private void buildPanel(boolean isNotificationMode) {
		GridBagConstraints c = new GridBagConstraints();

		// bottom left
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.WEST;
		add(getLeftPanel(), c);

		// gap

		// bottom right
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		add(getRightPanel(isNotificationMode), c);
	}

	private Component getRightPanel(boolean isNotificationMode) {
		JPanel p = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.RIGHT);
		p.setLayout(flow);
		p.setBackground(mCurrentColor);
		if (isNotificationMode) {
			p.add(mAcceptBtn);
		}

		p.add(mDeclineBtn);
		return p;
	}

	private Component getLeftPanel() {
		JPanel p = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		p.setLayout(flow);
		p.setBackground(mCurrentColor);
		p.add(mCurrentIco);
		p.add(mStatusLabel);
		return p;
	}

	public void addAcceptAction(ActionListener actionListener) {
		mAcceptBtn.addActionListener(actionListener);
	}

	public void hidePanel() {
		setVisible(false);		
	}
	
    public ActionListener getStopScreenSharingListener() {
		return stopScreenSharingListener;
	}

	public AcceptScreenSharingListener getAcceptScreenSharingListener() {
		return acceptScreenSharingListener;
	}

	private void addAcceptScreenSharingAction(AcceptScreenSharingListener listener) {
		if (acceptScreenSharingListener == null) {
			acceptScreenSharingListener = listener;
			mAcceptBtn.addActionListener(acceptScreenSharingListener);
		} else {
			Log.error("Listener already added");
		}
	}
	
    public void addStopScreenSharingAction(AbstractStopScreenSharingListener listener) {
    	if (stopScreenSharingListener == null) {
    		stopScreenSharingListener = listener;
    		mDeclineBtn.addActionListener(stopScreenSharingListener);
    	} else {
    		Log.error("Listener already added");
    	}
    }
    
    public void addRemoteControlListener(ActionListener listener) {
    	btnTglMouse.addActionListener(listener);
    }	
}
