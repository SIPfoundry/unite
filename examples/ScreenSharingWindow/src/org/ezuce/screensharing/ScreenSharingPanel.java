package org.ezuce.screensharing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ezuce.GraphicUtils;
import com.ezuce.WidgetBuilder;

/**
 * @author Vyacheslav Durin
 * 
 *         Feb 20, 2013
 * @version 0.1
 */
public class ScreenSharingPanel extends JPanel {

	private static final String ACTIVE_TEXT = "Screen Sharing is active";
	private static final String NOTIFICATION_TEXT = "Accept Screen Sharing?";
	private static final ImageIcon ICO_SS_ACTIVE = GraphicUtils
			.createImageIcon("/resources/images/screen-sharing_active.png");
	private static final ImageIcon ICO_CONNECTION = GraphicUtils
			.createImageIcon("/resources/images/video_is_connecting.png");
	/**  */
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
	private Color mCurrentColor = G_COLOR_ACTIVE;

	public ScreenSharingPanel() {
		setPreferredSize(new Dimension(100, 79));
		mSharingIcoNotification = new JLabel(ICO_CONNECTION);
		mSharingIcoNotification.setFont(G_NOTIFICATION_FONT);
		mSharingIcoInProgress = new JLabel(ICO_SS_ACTIVE);
		mAcceptBtn = WidgetBuilder
				.createButton("start_off.png", "start_on.png");
		mDeclineBtn = WidgetBuilder.createButton("close_off.png",
				"close_on.png");
		mStatusLabel = new JLabel();

		setBackground(G_COLOR_ACTIVE);
		setLayout(new GridBagLayout());
	}

	public void initNotificationWidgets() {
		removeAll();
		mCurrentIco = mSharingIcoNotification;
		mStatusLabel.setText(NOTIFICATION_TEXT);
		mCurrentColor = G_COLOR_NOTIFICATION;
		buildPanel(true);
		revalidate();
		repaint();
	}

	public void initProgressWidgets() {
		removeAll();
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
		if (isNotificationMode)
			p.add(mAcceptBtn);

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

	public void addDeclineAction(ActionListener actionListener) {
		mDeclineBtn.addActionListener(actionListener);
	}

	public void hidePanel() {
		setVisible(false);
		//
	}
}
