package com.ezuce;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ezuce.panel.MockPanelUI;

public class MainWindow extends JFrame {

	/**  */
	private static final long serialVersionUID = -241823951502193820L;
	private static MainWindow instance;
	private static MockPanelUI mockPanel;
	private JPanel attachedVideoContainer;

	private JPanel toolPanel;

	public synchronized static MainWindow getInstance() {
		if (instance == null)
			instance = new MainWindow();
		return instance;
	}

	private MainWindow() {
		setLayout(new GridBagLayout());

		toolPanel = new JPanel();
		toolPanel.add(new JLabel("MAIN WINDOW"));

		attachedVideoContainer = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(toolPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(attachedVideoContainer, c);

		attachedVideoContainer.setVisible(false);

		pack();

	}

	public Container getContainerToAttachVideo() {
		return attachedVideoContainer;
	}

	public void bringFrameIntoFocus() {
		// TODO: to be implemented
		System.out.println("TODO: MainWindow#bringFrameIntoFocus()");

	}

	public void logout(boolean b) {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		super.setVisible(b);
		mockPanel.setVisible(b);
	}

	public static void setPanel(MockPanelUI panel) {
		mockPanel = panel;
	}

	@Override
	public void setIconImage(Image image) {
		// TODO Auto-generated method stub
		super.setIconImage(image);
		mockPanel.setIconImage(image);
	}
}
