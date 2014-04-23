package org.ezuce.common.ui.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = -6560516478124002621L;
	private final Image gBackground;
	private final int bgWidth;
	private final int bgHeight;

	public BackgroundPanel(ImageIcon bg) {
		setDoubleBuffered(true);
		gBackground = bg.getImage();
		bgWidth = gBackground.getWidth(null);
		bgHeight = gBackground.getHeight(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(gBackground, 0, 0, getWidth(), getHeight(), 0, 0, bgWidth,
				bgHeight, null);
	}

}