package org.ezuce.call;

import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {
    public static final String CALL_ICON = "resources/images/CallsTab.png";
    public static final String DIALED_ICON = "resources/images/dialed_48x48.png";
    public static final String RECEIVED_ICON = "resources/images/received_48x48.png";
    public static final String MISSED_ICON = "resources/images/missed_48x48.png";
    public static final String VOICEMAIL_ICON = "resources/images/voicemail_48x48.png";

    public static ImageIcon getImageIcon(String name) {
        ClassLoader cl = Utils.class.getClassLoader();
        final URL imageURL = cl.getResource(name);
        return new ImageIcon(imageURL);
    }

}
