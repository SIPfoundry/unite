package com.ezuce;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MugshotPanel extends JPanel {

	private static final Dimension PREFERRED_SIZE = new Dimension(56, 64);

	private static final long serialVersionUID = -9049772138996039114L;

	private ImageIcon img = GraphicUtils
			.createImageIcon("/resources/images/mugshot.png");

	public MugshotPanel() {
		setPreferredSize(PREFERRED_SIZE);
		setMaximumSize(PREFERRED_SIZE);
		setMinimumSize(PREFERRED_SIZE);
		// setBorder(BorderFactory.createLineBorder(Color.black));
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(img.getImage(), 0, 0, null);
	}
}
