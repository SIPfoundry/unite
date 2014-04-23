package org.ezuce.im.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.jivesoftware.spark.ui.CommandPanel;

public class EzuceCommandPanel extends CommandPanel {
	private static final long serialVersionUID = -7364692257112518171L;

	private JPanel inner = null;

	public EzuceCommandPanel() {
		super(false);
		setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		setOpaque(false);
	}

	@Override
	public Component add(Component arg0) {
		synchronized (new byte[0]) {
			if (inner == null) {
				inner = new InnerPanel();
				super.add(inner);
			}
		}

		return inner.add(arg0);
	}

	private class InnerPanel extends JPanel {
		private static final long serialVersionUID = -1627261690368579860L;

		public InnerPanel() {
			setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int width = getWidth();
			int height = getHeight();
			int radius = 5;
			Graphics2D graphics = (Graphics2D) g;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(new Color(234, 234, 234));
			graphics.fillRoundRect(0, 0, width, height, radius, radius);
			graphics.setColor(new Color(181, 186, 187));
			graphics.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);
		}
	}
}
