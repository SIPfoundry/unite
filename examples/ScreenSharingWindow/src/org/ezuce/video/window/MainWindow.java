package org.ezuce.video.window;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ezuce.screensharing.ScreenSharingPanel;

public class MainWindow extends JFrame {

	/**  */
	private static final long serialVersionUID = -241823951502193820L;
	private static MainWindow instance;
	private JPanel attachedVideoContainer;

	private ScreenSharingPanel toolPanel;

	public synchronized static MainWindow getInstance() {
		if (instance == null)
			instance = new MainWindow();
		return instance;
	}

	private MainWindow() {
		setLayout(new GridBagLayout());

		toolPanel = new ScreenSharingPanel();
		attachedVideoContainer = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		add(toolPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(attachedVideoContainer, c);

		toolPanel.addAcceptAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toolPanel.initProgressWidgets();

			}
		});
		toolPanel.addDeclineAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		toolPanel.initNotificationWidgets();
		attachedVideoContainer.setVisible(false);

		setPreferredSize(new Dimension(380, 350));
		pack();

	}

	public Container getContainerToAttachVideo() {
		return attachedVideoContainer;
	}
}
