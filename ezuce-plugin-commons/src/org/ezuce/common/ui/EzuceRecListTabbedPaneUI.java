package org.ezuce.common.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author Razvan
 */
public class EzuceRecListTabbedPaneUI extends BasicTabbedPaneUI{
    
    private static final int ADDED_TAB_HEIGTH = 2;
    private static final int ADDED_TAB_WIDTH = 3;
    private static final int SPACE_BETWEEN_TAB = 0;   
    private static final Color EDGE_COLOR = new Color(220, 220, 220);
    private static final Color GRADIENT_TOP = new Color(246, 246, 246);
    private static final Color GRADIENT_BOTTOM = new Color(220, 220, 220);
 
 
  // overrided to add more space each side of the tab title and spacing between tabs.
  protected void installDefaults() {
    super.installDefaults();
           lightHighlight = null;
           shadow = new Color(214, 214, 214,0);
           darkShadow = new Color(214, 214, 214,0);
  }
  
 
   @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] r, int tabIndex,
        Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    // don't paint anything
    }

  // overrided to paint the selected tab with a different color.
    @Override
  protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y,
                                    int w, int h, boolean isSelected) {
 
    final GradientPaint gradPaint = new GradientPaint(0, 0, GRADIENT_TOP, 0, h, GRADIENT_BOTTOM);
    final Graphics2D g2d = (Graphics2D) g;
    g2d.setPaint(gradPaint);
    switch (tabPlacement) {
        case LEFT:
        g2d.fillRect(x + 1, y + 1, w - 1, h - 3);
        break;
        case RIGHT:
        g2d.fillRect(x, y + 1, w - 2, h - 3);
        break;
        case BOTTOM:
        g2d.fillRect(x + 1, y, w - 3, h - 1);
        break;
        case TOP:
        default:
        g2d.fillRect(x + 1, y + 1, w - 3, h - 1);
        }
  }
  
    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        final Graphics2D g2d = (Graphics2D) g;
        final int diameter = 9;
        g2d.setColor(EDGE_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawLine(x + 1, y+diameter / 2, x + 1, y+h-2);
        g2d.drawLine(x + w - 1, y+diameter / 2, x + w - 1, y+h-2);
        g2d.drawLine(x + 1 + diameter / 2, y+2, x + w - 1 - diameter / 2, y+2);
      
        g2d.drawArc(x + 1, y+2, diameter, diameter, 90, 90);
        g2d.drawArc(x + w - 1 - diameter, y+2, diameter, diameter, 90, -90);
    }
 
    @Override
    protected Insets getTabAreaInsets(int arg0) {
        return new Insets(0, 0, 0, 0);
    }
 
 
    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        g.setColor(EDGE_COLOR);
        g.drawLine(x, y + 1, x, y + h);
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        g.setColor(EDGE_COLOR);
        g.drawLine(w - 1, y + 1, w - 1, y + h);
    }
 
      @Override
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {                
            g.setColor(EDGE_COLOR);     
            g.drawLine(x, y + h-1, x + w, y + h-1);
    }

    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
    {              
        if (selectedIndex != -1 && tabPlacement == TOP) {
            g.setColor(EDGE_COLOR);
            g.drawLine(x, y+1, x + w, y+1);
        }
    }
  
}
