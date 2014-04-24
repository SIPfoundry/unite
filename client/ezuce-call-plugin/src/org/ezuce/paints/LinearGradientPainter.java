/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.paints;

import org.jdesktop.swingx.painter.MattePainter;

/**
 *
 * @author Razvan
 */
public class LinearGradientPainter {
    public static MattePainter makeCallTopPanelPainter(){return new MattePainter(
            LinearDoubleGradientPaint.makeCallTopPanelPaint);}

    public static MattePainter dialPadPainter(){return new MattePainter(
            LinearTripleGradientPaint.gradientPaint, true);}

    public static MattePainter missedCallLightPainter (){return  new MattePainter(
            LinearDoubleGradientPaint.missedCallLightPaint, true);}

    public static MattePainter missedCallDarkPainter (){return  new MattePainter(
            LinearDoubleGradientPaint.missedCallDarkPaint, true);}

    public static MattePainter missedCallIconPainter (){return  new MattePainter(
            LinearDoubleGradientPaint.missedCallIconPaint, true);}

    public static MattePainter voiceMessageLightPainter (){return new MattePainter(
            LinearDoubleGradientPaint.voiceMessageLightPaint, true);}

    public static MattePainter voiceMessageDarkPainter (){return  new MattePainter(
            LinearDoubleGradientPaint.voiceMessageDarkPaint, true);}

    public static MattePainter voiceMessageHeardLightPainter (){return  new MattePainter(
            LinearDoubleGradientPaint.voiceMessageHeardLightPaint, true);}

    public static MattePainter voiceMessageHeardDarkPainter (){return  new MattePainter(
            LinearDoubleGradientPaint.voiceMessageHeardDarkPaint, true);}

    public static MattePainter jxLabelStatusBackgroundSmall(){return new MattePainter(
            LinearTripleGradientPaint.gradientPaintJxStatusLabel,true);}

    public static MattePainter findMeRosterPanelActBgPainter(){return new MattePainter(
            LinearDoubleGradientPaint.findMeRosterPanelActBgPaint, true);}

    public static MattePainter findMeRosterPanelInactBgPainter(){return new MattePainter(
            LinearDoubleGradientPaint.findMeRosterPanelInactBgPaint, true);}

    public static MattePainter findMeRosterMiniPanelBgPainter(){return new MattePainter(
            LinearDoubleGradientPaint.findMeRosterMiniPanelBgPaint);}


}
