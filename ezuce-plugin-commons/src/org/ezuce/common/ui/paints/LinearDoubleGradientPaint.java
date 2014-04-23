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
public class LinearDoubleGradientPaint {

    private static final int makeCallTopPanelHeight=307;
    public static LinearGradientPaint makeCallTopPanelPaint =
            new LinearGradientPaint (0.0f, 0.0f, 0, makeCallTopPanelHeight,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.makeCallTopPanelColor1,
                                        Colors.makeCallTopPanelColor2
                                                });

    private static final int width = 0;
    private static final int height = 30;
    public static LinearGradientPaint missedCallLightPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, height,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.missedCallLightColor1,
                                        Colors.missedCallLightColor2
                                    });



    public static LinearGradientPaint missedCallDarkPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, height,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.missedCallDarkColor1,
                                        Colors.missedCallDarkColor2
                                    });

    public static LinearGradientPaint missedCallIconPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, height,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.missedCallIconColor1,
                                        Colors.missedCallIconColor2
                                    });

    private static final int heightVoiceMessage = 59;

    public static LinearGradientPaint voiceMessageLightPaint =
            new LinearGradientPaint(0.0f, 0.0f, width,heightVoiceMessage,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.voiceMessageLightColor1,
                                        Colors.voiceMessageLightColor2
                                    });



    public static LinearGradientPaint voiceMessageDarkPaint =
            new LinearGradientPaint(0.0f, 0.0f, width,heightVoiceMessage,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.voiceMessageDarkColor1,
                                        Colors.voiceMessageDarkColor2
                                    });

    public static LinearGradientPaint voiceMessageHeardLightPaint =
            new LinearGradientPaint(0.0f, 0.0f, width,heightVoiceMessage,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.voiceMessageHeardLightColor1,
                                        Colors.voiceMessageHeardLightColor2
                                    });

    public static LinearGradientPaint voiceMessageHeardDarkPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, heightVoiceMessage,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.voiceMessageHeardDarkColor1,
                                        Colors.voiceMessageHeardDarkColor2
                                    });

    private static final int findMeRosterPanelMinHeight=84;

    public static LinearGradientPaint findMeRosterPanelActBgPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, findMeRosterPanelMinHeight,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.findMeRosterPanelActBgColor1,
                                        Colors.findMeRosterPanelActBgColor2
                                    });

    public static LinearGradientPaint findMeRosterPanelInactBgPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, findMeRosterPanelMinHeight,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.findMeRosterPanelInactBgColor1,
                                        Colors.findMeRosterPanelInactBgColor2
                                    });

    private static final int findMeRosterMiniPanelHeight=51;

    public static LinearGradientPaint findMeRosterMiniPanelBgPaint =
            new LinearGradientPaint(0.0f, 0.0f, width, findMeRosterMiniPanelHeight,
                                    new float[] {0.0f, 1.0f},
                                    new Color[] {
                                        Colors.findMeRosterMiniPanelBgColor1,
                                        Colors.findMeRosterMiniPanelBgColor2
                                    });


}
