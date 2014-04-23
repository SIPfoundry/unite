package org.ezuce.common;

import java.awt.Insets;

import javax.swing.Icon;

import org.jivesoftware.spark.ui.ChatRoomButton;

public class EzuceChatRoomButton extends ChatRoomButton {
    public EzuceChatRoomButton() {
        super();
    }
    public EzuceChatRoomButton(Icon icon) {
        super(icon);
    }
    public EzuceChatRoomButton(String text, Icon icon) {
        super(text, icon);
    }
    public EzuceChatRoomButton(String text) {
        super(text);

    }
	@Override
    protected void decorate() {
        setBorderPainted(false);
        setOpaque(true);

        setContentAreaFilled(false);
        setMargin(new Insets(1, 1, 1, 1));
    }
}
