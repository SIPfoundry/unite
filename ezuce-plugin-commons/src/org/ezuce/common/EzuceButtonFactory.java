package org.ezuce.common;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import org.ezuce.common.ui.LabelButton;
import org.ezuce.common.ui.LabelToggleButton;
import org.ezuce.common.ui.actions.ContactActions;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.spark.ButtonFactory;
import org.jivesoftware.spark.component.RolloverButton;
import org.jivesoftware.spark.ui.ChatRoomButton;
import org.jivesoftware.spark.ui.RosterDialog;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.plugin.filetransfer.transfer.Downloads;

public class EzuceButtonFactory extends ButtonFactory {
	private static final EzuceButtonFactory INSTANCE = new EzuceButtonFactory();

	protected EzuceButtonFactory() {
		// prevent instantiation
	}

	public static EzuceButtonFactory getInstance() {
		return INSTANCE;
	}

	public JButton createDownloadsButton() {
		final Icon icon = new ImageIcon(getClass().getClassLoader().getResource(
				"resources/images/downloaded_files_off.png"));
		final Icon pressedIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"resources/images/downloaded_files_on.png"));

		final JButton viewDownloads = new LabelButton(icon, pressedIcon, null);
		viewDownloads.setToolTipText(Res.getString("menuitem.view.downloads"));
		viewDownloads.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(Downloads.getDownloadDirectory().toURI());
				} catch (final IOException e1) {
					Log.error("Could not find file-browser");
				}
			}
		});
		return viewDownloads;
	}

	public JButton createContactButton() {
		final Icon icon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/add_user_off.png"));
		final Icon pressedIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"resources/images/add_user_on.png"));
		final JButton addContactButton = new LabelButton(icon, pressedIcon, null);

		addContactButton.setToolTipText(Res.getString("message.add.a.contact"));
		addContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RosterDialog().showRosterDialog();
			}
		});
		return addContactButton;
	}

	public JButton createGroupChatButton() {
		final Icon icon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/group_chat_off.png"));
		final Icon pressedIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"resources/images/group_chat_on.png"));

		final JButton groupChat = new LabelButton(icon, pressedIcon, null);
		groupChat.setToolTipText(Res.getString("start.chat.conference")); // TODO put this into resources
		groupChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ContactActions.startDefaultBookmarkedConf(null);
			}
		});
		return groupChat;
	}

	public JToggleButton createConnectButton() {
		final Icon icon = new ImageIcon(getClass().getClassLoader().getResource(
				"resources/images/add_external_network_off.png"));
		final Icon pressedIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"resources/images/add_external_network_on.png"));

		final JToggleButton connect = new LabelToggleButton(icon, pressedIcon, null);
		connect.setToolTipText(Res.getString("connect.to")); // TODO put this into resources

		return connect;
	}

	@Override
	public EzuceChatRoomButton createChatTranscriptButton() {
		return buildChatRoomButton("resources/images/tb_history_on.png", "resources/images/tb_history_off.png");
	}

	@Override
	public EzuceChatRoomButton createSendFileButton() {
		return buildChatRoomButton("resources/images/tb_send_file_on.png", "resources/images/tb_send_file_off.png");
	}

	@Override
	public EzuceChatRoomButton createScreenshotButton() {
		return buildChatRoomButton("resources/images/tb_screen_on.png", "resources/images/tb_screen_off.png");
	}
	
	public EzuceChatRoomButton createScreenSharingButton() {
		return buildChatRoomButtonNoHover("resources/images/screen-sharing_on.png", "resources/images/screen-sharing_off.png");
	}	
	
	@Override
	public EzuceChatRoomButton createOtrButton() {
		return new EzuceChatRoomButton();
	}

	@Override
	public EzuceChatRoomButton createInviteConferenceButton() {
		return buildChatRoomButton("resources/images/tb_conference_on.png", "resources/images/tb_conference_off.png");
	}

	@Override
	public EzuceRolloverButton createBuzzButton() {
		return buildRolloverButton("resources/images/tb_buzz_on.png", "resources/images/tb_buzz_off.png");
	}

	@Override
	public EzuceRolloverButton createEmoticonButton() {
		EzuceRolloverButton erb = buildRolloverButton("resources/images/tb_emot_on.png", "resources/images/tb_emot_off.png");
        erb.setToolTipText(Res.getString("label.emoticons").replace("&", ""));
        return erb;
	}
	
	public EzuceRolloverButton createSettingsButton() {
		EzuceRolloverButton erb = buildRolloverButton("resources/images/configuration_on.png", "resources/images/configuration_off.png");
                erb.setToolTipText(Res.getString("title.configure.room"));
                return erb;
	}
	
    public RolloverButton createTemaButton() {
		RolloverButton rb = buildRolloverButton("resources/images/subject-groupchat_on.png", "resources/images/subject-groupchat_off.png");
                rb.setToolTipText(Res.getString("menuitem.change.subject"));
                return rb;
    }	
	
    public EzuceRolloverButton createRegisterButton() {
    	EzuceRolloverButton erb = buildRolloverButton("resources/images/registration_on.png", "resources/images/registration_off.png");
        erb.setToolTipText(Res.getString("button.register").replace("&", ""));
        return erb;
    }	
	
	public EzuceRolloverButton createAlwaysOnTop(boolean isAlwaysOnTopActive) {
		EzuceRolloverButton alwaysOnTopItem = new EzuceRolloverButton();
		alwaysOnTopItem.setRolloverIcon(SparkRes.getImageIcon("FRAME_ALWAYS_ON_TOP_ACTIVE"));
		if (isAlwaysOnTopActive) {
		    alwaysOnTopItem.setIcon(SparkRes.getImageIcon("FRAME_ALWAYS_ON_TOP_ACTIVE"));
		} else {
		    alwaysOnTopItem.setIcon(SparkRes.getImageIcon("FRAME_ALWAYS_ON_TOP_DEACTIVE"));
		}
		alwaysOnTopItem.setToolTipText(Res.getString("menuitem.always.on.top"));
		return alwaysOnTopItem;
	}	

	private EzuceChatRoomButton buildChatRoomButton(String nameOn, String nameOff) {
		return (EzuceChatRoomButton)buildButton(nameOn, nameOff, ButtonType.CHAT_ROOM, true);
	}
	
	private EzuceChatRoomButton buildChatRoomButtonNoHover(String nameOn, String nameOff) {
		return (EzuceChatRoomButton)buildButton(nameOn, nameOff, ButtonType.CHAT_ROOM, false);
	}
		
	private EzuceRolloverButton buildRolloverButton(String nameOn, String nameOff) {
		return (EzuceRolloverButton)buildButton(nameOn, nameOff, ButtonType.ROLLOVER, true);
	}
	
	private JButton buildButton(String nameOn, String nameOff,  ButtonType type, boolean hover) {
		final Icon icon = new ImageIcon(getClass().getClassLoader().getResource(nameOff));
		final Icon pressedIcon = new ImageIcon(getClass().getClassLoader().getResource(nameOn));
		final JButton button;
		if (type.equals(ButtonType.ROLLOVER)) {
			button = new EzuceRolloverButton(icon);
		} else if (type.equals(ButtonType.CHAT_ROOM)) {
			button = new EzuceChatRoomButton(icon);
		} else {
			button = new JButton(icon);
		}
		if (hover) {
			button.addMouseListener(new HoverListener(pressedIcon, icon));
		}
		
		return button;
	}

	@Override
	public JLabel createDivider() {
		return null;
	}

	private class HoverListener extends MouseAdapter {
		private final Icon on;
		private final Icon off;

		public HoverListener(Icon on, Icon off) {
			this.on = on;
			this.off = off;
		}

		@Override
		public void mouseEntered(MouseEvent evt) {
			JButton button = (JButton) evt.getSource();
			button.setIcon(on);
		}

		@Override
		public void mouseExited(MouseEvent evt) {
			JButton button = (JButton) evt.getSource();
			button.setIcon(off);
		}

	}
	
	public enum ButtonType {
	    ROLLOVER, 
	    CHAT_ROOM
	}

}
