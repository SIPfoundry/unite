/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.common.ui.paints;

import java.awt.Color;
import java.awt.LinearGradientPaint;

/**
 *
 * @author Razvan
 */
public class LinearTripleGradientPaint {
    private static final int width = 344;
    private static final int height = 0;
    public static LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f, width, height,
            new float[] {
                0.0f, 0.5f, 1.0f
            }, new Color[] {
                Colors.tripleGradientColor1, Colors.tripleGradientColor2, Colors.tripleGradientColor1
            });

    private static final int widthJxStatusLabel = 40;
    private static final int heightJxStatusLabel = 0;
    public static LinearGradientPaint gradientPaintJxStatusLabel =
            new LinearGradientPaint(0.0f, 0.0f, widthJxStatusLabel, heightJxStatusLabel,
                                    new float[] {0.0f, 0.5f, 1.0f},
                                    new Color[] {Colors.jxStatusLabelColor1,
                                                 Colors.jxStatusLabelColor2,
                                                 Colors.jxStatusLabelColor1});
}
