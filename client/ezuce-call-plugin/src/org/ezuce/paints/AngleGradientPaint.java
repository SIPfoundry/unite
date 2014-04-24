/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.paints;

import java.awt.Color;
import java.awt.LinearGradientPaint;

/**
 *
 * @author Razvan
 */
public class AngleGradientPaint {
    private static final int width = 20;
    private static final int height = 5;
    private static final Color color1 = Colors.angleGradientPaintColor1;
    private static final Color color2 = Colors.angleGradientPaintColor2;
    public static LinearGradientPaint userMiniPanelGradientPaint = new LinearGradientPaint(0.0f, 0.0f, width,
            height, new float[] {
                0.0f, 1.0f
            }, new Color[] {
                color1, color2
            });
}
