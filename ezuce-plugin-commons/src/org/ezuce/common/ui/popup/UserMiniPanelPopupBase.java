package org.ezuce.common.ui.popup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;

import javax.swing.JPopupMenu;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

public abstract class UserMiniPanelPopupBase extends JPopupMenu {

	private final Color popupColor1 = new Color(255, 255, 255, 255);
    private final Color popupColor2=new Color(218,218,218,255);
	protected final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque()){
            super.paintComponent(g);
            return;
        }

        final int w = getWidth( );
        final int h = getHeight( );
        LinearGradientPaint p=new LinearGradientPaint(0f, 0f, 0, h,
                                        new float[] {0.0f, 1.0f},
                                        new Color[] {popupColor1, popupColor2});
        Graphics2D g2d=(Graphics2D)g;
        g2d.setPaint(p);
        g2d.fillRect(0, 0, w, h);
    }    
    
}
