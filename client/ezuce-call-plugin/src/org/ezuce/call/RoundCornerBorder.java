package org.ezuce.call;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class RoundCornerBorder implements Border {

    protected final int w = 6;
    protected final int h = 6;
    protected Color topColor = Color.black;
    protected Color bottomColor = Color.black;

    public RoundCornerBorder(Color topColor, Color bottomColor) {
        if (topColor != null) {
            this.topColor = topColor;
        }
        if (bottomColor != null) {
            this.bottomColor = bottomColor;
        }
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(h, w, h, w);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        w = w - 5;
        h = h - 5;
        x++;
        y++;
        g.setColor(topColor);
        g.drawLine(x, y + 2, x, y + h - 2);
        g.drawLine(x + 2, y, x + w - 2, y);
        g.drawLine(x, y + 2, x + 2, y); // Top left diagonal
        g.drawLine(x, y + h - 2, x + 2, y + h); // Bottom left diagonal
        g.setColor(bottomColor);
        g.drawLine(x + w, y + 2, x + w, y + h - 2);
        g.drawLine(x + 2, y + h, x + w - 2, y + h);
        g.drawLine(x + w - 2, y, x + w, y + 2); // Top right diagonal
        g.drawLine(x + w, y + h - 2, x + w - 2, y + h); // Bottom right diagonal
    }

}
