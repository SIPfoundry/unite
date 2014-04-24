/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.common.ui.borders;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

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
    public static final Border userMiniPanelGlossCompoundBorder= new CompoundBorder(
    		new MatteBorder(1,0,2,0, new Color(255,255,255)),
    		new CompoundBorder(new MatteBorder(0, 0, 1, 0, new Color(192, 192, 192)), 
    						   new LineBorder(new Color(255, 255, 255),2)));
}
