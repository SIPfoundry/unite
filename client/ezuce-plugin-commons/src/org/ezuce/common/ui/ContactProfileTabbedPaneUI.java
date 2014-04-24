package org.ezuce.common.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import static javax.swing.SwingConstants.TOP;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import org.jivesoftware.Spark;

public class ContactProfileTabbedPaneUI extends BasicTabbedPaneUI
{
    private static final int ADDED_TAB_HEIGTH = 2;
    private static final int ADDED_TAB_WIDTH = 3;
    private static final int SPACE_BETWEEN_TAB = 0;   
    private static final Color EDGE_COLOR = new Color(214, 214, 214);
    private static final Color GRADIENT_TOP = Color.WHITE;
    private static final Color GRADIENT_BOTTOM = new Color(240, 240, 240);
 
 
  
    @Override
  protected void installDefaults() {
    super.installDefaults();
           lightHighlight = null;
           shadow = new Color(214, 214, 214,0);
           darkShadow = new Color(214, 214, 214,0);           
  }
  
 
   @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] r, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    // don't paint anything
    }

   @Override
   protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) { 
	   final GradientPaint gradPaint = new GradientPaint(0, h, GRADIENT_BOTTOM, 0, h, GRADIENT_BOTTOM);
	     final Graphics2D g2d = (Graphics2D) g;
	     g2d.setPaint(gradPaint);
	   //g2d.fillRect(x + 2, y, w, h);
   }
   
    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
    	
    }
 
    @Override
    protected Insets getTabAreaInsets(int arg0) {
        return new Insets(0, 0, 0, 0);
    }
 
    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }
    
    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }
    
 	@Override
    protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth){
 		return 200;
 	}

    
    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
    }
 
      @Override
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {     
    }
    
    @Override
    protected Insets getContentBorderInsets(int tabPlacement){
    	Insets oi = super.getContentBorderInsets(tabPlacement);
    	Insets ni = new Insets(oi.top, oi.left, oi.bottom, oi.right);
    	ni.top+=2;
    	return ni;
    }


    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h){         
    	int n = 7;
        int xMin=2;
        int xMax=-4;
        if (Spark.isMac()){
            n=9;
            xMin=xMin-2;
            xMax = 0;
        }
        if (selectedIndex != -1 && tabPlacement == TOP) {
            g.setColor(GRADIENT_BOTTOM);
            for (int i=-n;i<(n+1);i++)
            {
            	g.drawLine(x+xMin, y+i, x + w+xMax, y+i);
            }            
    	}
    }
    
        
    
}
