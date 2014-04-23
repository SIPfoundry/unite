package org.ezuce.im.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * Draws lines along the top and bottom of the component. Draws background
 * colored lines on the sides.
 *
 * @author alex
 *
 */
public class TopDownBorder implements Border {
	private static final Color EDGE_COLOR = new Color(214, 214, 214);

	@Override
	public Insets getBorderInsets(Component arg0) {
		return new Insets(0, 0, 0, 0);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component comp, Graphics g, int x, int y, int width, int height) {
		g.setColor(EDGE_COLOR);

		g.drawLine(0, 0, width, 0);
		g.drawLine(0, height - 1, width, height - 1);

		g.setColor(comp.getBackground());

		g.drawLine(0, 1, 0, height - 2);
		g.drawLine(width - 1, 1, width - 1, height - 2);
	}

}
