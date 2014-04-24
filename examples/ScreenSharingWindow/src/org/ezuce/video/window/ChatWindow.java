package org.ezuce.video.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatWindow extends JFrame {
	/**  */
	private static final long serialVersionUID = 6181185529088259752L;
	private static final Color COLOR_BG = new Color(215, 232, 236);
	private JPanel videoContainer;

	public ChatWindow() {
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		add(getCommonTextPanel(), c);

		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 1;
		c.gridy = 0;
		add(getVideoPanel(), c);

		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(getLocalTextPanel(), c);

		pack();
		setLocation(700, 100);
	}

	public void openWindow() {
		setVisible(true);
	}

	public void closeWindow() {
		setVisible(false);
		dispose();
	}

	private Component getLocalTextPanel() {
		JPanel p = new JPanel();
		p.add(new JTextArea());
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		p.setPreferredSize(new Dimension(700, 50));
		return p;
	}

	private Component getVideoPanel() {
		if (videoContainer != null)
			return videoContainer;

		videoContainer = new JPanel();
		videoContainer.setBackground(COLOR_BG);
		videoContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		videoContainer.setPreferredSize(new Dimension(350, 250));
		return videoContainer;
	}

	private Component getCommonTextPanel() {
		JPanel p = new JPanel();
		p.add(new JTextArea());
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		p.setPreferredSize(new Dimension(350, 200));
		return p;
	}

	// #############

	public Container getVideoContainer() {
		return videoContainer;
	}

}
