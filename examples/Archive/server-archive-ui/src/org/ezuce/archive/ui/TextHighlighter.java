package org.ezuce.archive.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;

public class TextHighlighter implements HighlightPainter {

	private Color mSelectionColor;

	@Override
	public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
		Rectangle r0 = null;
		Rectangle r1 = null;
		Rectangle rBounds = bounds.getBounds();
		int xMax = rBounds.x + rBounds.width;

		try {
			r0 = c.modelToView(p0);
			r1 = c.modelToView(p1);
		} catch (BadLocationException ex) {
			return;
		}
		if ((r0 == null) || (r1 == null))
			return;

		g.setColor(mSelectionColor != null ? mSelectionColor : c
				.getSelectionColor());

		if (r0.y == r1.y) {
			highlight(g, r0, r1.x);
			return;
		}

		highlight(g, r0, xMax);

		r0.y += r0.height;
		r0.x = rBounds.x;
		while (r0.y < r1.y) {
			highlight(g, r0, xMax);
			r0.y += r0.height;
		}

		highlight(g, r0, r1.x);
	}

	private void highlight(Graphics g, Rectangle r, int x2) {
		g.fillRect(r.x, r.y, x2 - r.x, r.height);
	}

	public void setSelectionColor(Color c) {
		this.mSelectionColor = c;
	}

	public Color getSelectionColor() {
		return mSelectionColor;
	}
}
