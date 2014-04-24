/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.borders;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 * @author Razvan
 */
public class PanelBorders
{
    public static final Border userMiniPanelGlossBorderHoverOn=
                    BorderFactory.createLineBorder(new Color(102,153,255,230), 1);
    public static final Border userMiniPanelGlossBorderHoverOff=
                                BorderFactory.createLineBorder(Color.WHITE, 1);
}
