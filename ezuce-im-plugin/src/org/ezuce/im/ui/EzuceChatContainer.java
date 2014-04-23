package org.ezuce.im.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.ezuce.common.ui.EzuceTabbedPaneUI;
import org.ezuce.media.manager.UnitemediaRegistry;
import org.jivesoftware.spark.ui.ChatContainer;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.conferences.GroupChatRoomListener;
import org.jivesoftware.spark.util.log.Log;

public class EzuceChatContainer extends ChatContainer {
	private static final long serialVersionUID = -6946544126472905489L;

	public EzuceChatContainer() {
		getTabbedPane().setUI(new EzuceTabbedPaneUI());
                getTabbedPane().setBorder(null);
		setBackground(new Color(0xE6ECED));
		setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        //add Group chat room listener

		EzuceChatRoomListener ezcrListener = new EzuceChatRoomListener();
		addChatRoomListener(ezcrListener);
		UnitemediaRegistry.getInstance().addUnitemediaEventListener(ezcrListener);
        this.getInputMap(JComponent.WHEN_FOCUSED).remove( KeyStroke.getKeyStroke("ESCAPE"));
        this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(KeyStroke.getKeyStroke("ESCAPE"));
	}

	@Override
	public synchronized void addChatRoom(final ChatRoom room) {
		if(room == null) {
			Log.error("Room is null, we cannot add it to chat container");
			return;
		}
		super.addChatRoom(room);
		room.setBorder(BorderFactory.createEmptyBorder());
		Component c = getTabbedPane().getTabComponentAt(
				getTabbedPane().getTabCount() - 1);
		if (c instanceof TabPanel) {
			TabPanel tp = (TabPanel) c;
			tp.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
			tp.revalidate();
		}
	}
}
