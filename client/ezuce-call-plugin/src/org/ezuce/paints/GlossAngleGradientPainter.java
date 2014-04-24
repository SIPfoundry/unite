/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ezuce.paints;

import org.jdesktop.swingx.painter.GlossPainter;

/**
 *
 * @author Razvan
 */
public class GlossAngleGradientPainter {
    public static GlossPainter painter (){return  new GlossPainter(AngleGradientPaint.userMiniPanelGradientPaint,
            GlossPainter.GlossPosition.TOP);}
}
