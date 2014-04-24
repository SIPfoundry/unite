package org.ezuce.im.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.ArrayUtils;
import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.ExecuteVoice;
import org.ezuce.common.EzuceButtonFactory;
import org.ezuce.common.EzuceChatRoomButton;
import org.ezuce.common.EzuceRolloverButton;
import org.ezuce.common.VoiceUtil;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.im.IMCommons;
import org.ezuce.im.ui.history.ChatHistoryManager;
import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.ui.listeners.MultiCastScreenSharingListener;
import org.ezuce.media.ui.listeners.ScreenSharingInvitees;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatContainer;
import org.jivesoftware.spark.ui.ChatFrame;
import org.jivesoftware.spark.ui.ChatInputEditor;
import org.jivesoftware.spark.ui.ChatRoomButton;
import org.jivesoftware.spark.ui.VCardPanel;
import org.jivesoftware.spark.ui.rooms.ChatRoomImpl;

public class EzuceChatRoom extends ChatRoomImpl implements ConfigureVoice,
		ExecuteVoice, ScreenSharingInvitees {

	private static final long serialVersionUID = -4255123883297351139L;

	private JPanel toolbarPanel;

	protected EzuceChatRoomJPanel ecrjp;
	private final String participantJID;
	private EzuceRolloverButton jButtonCall;
	private EzuceChatRoomButton jScreenSharing;
	private JComponent statePanel;
	private final ResourceMap resourceMap = Application.getInstance()
			.getContext().getResourceMap(this.getClass());
	private String displayName;
	private boolean customized = false;
	//private ChatHistoryManager chatHistoryManager;

	public EzuceChatRoom(final String participantJID,
			String participantNickname, String title) {
		super(participantJID, participantNickname, title, false);

		ChatContainer chatContainer = SparkManager.getChatManager()
				.getChatContainer();
		chatContainer.getInputMap(JComponent.WHEN_FOCUSED).remove(
				KeyStroke.getKeyStroke("ESCAPE"));
		chatContainer
				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.remove(KeyStroke.getKeyStroke("ESCAPE"));
		ChatInputEditor cie = this.getChatInputEditor();
		cie.getInputMap().remove(KeyStroke.getKeyStroke("ESCAPE"));
		cie.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(
				KeyStroke.getKeyStroke("ESCAPE"));

		this.participantJID = participantJID;
		setSize(new Dimension(643, 460));
		setPreferredSize(new Dimension(643, 460));
		setMinimumSize(new Dimension(300, 100));

		init();

		KeyStroke copyKeyStroke = KeyStroke.getKeyStroke("ctrl C");
		javax.swing.Action copyAction = new AbstractAction("copy") {
			public void actionPerformed(ActionEvent evt) {
				if (getChatInputEditor().hasFocus()) {
					SparkManager.setClipboard(getChatInputEditor()
							.getSelectedText());
				} else if (getTranscriptWindow().hasFocus()) {
					SparkManager.setClipboard(getTranscriptWindow()
							.getSelectedText());
				}
			}
		};
		this.getChatInputEditor().getActionMap().put("copy", copyAction);
		this.getChatInputEditor().getKeymap()
				.addActionForKeyStroke(copyKeyStroke, copyAction);

		KeyStroke cutKeyStroke = KeyStroke.getKeyStroke("ctrl X");
		javax.swing.Action cutAction = new AbstractAction("cut") {
			public void actionPerformed(ActionEvent evt) {
				if (getChatInputEditor().hasFocus()) {
					String selectedText = getChatInputEditor()
							.getSelectedText();
					getChatInputEditor().replaceSelection("");
					SparkManager.setClipboard(selectedText);
				} else if (getTranscriptWindow().hasFocus()) {

				}
			}
		};
		this.getChatInputEditor().getActionMap().put("cut", cutAction);
		this.getChatInputEditor().getKeymap()
				.addActionForKeyStroke(cutKeyStroke, cutAction);

		/*chatHistoryManager = ChatHistoryManager.create(this.participantJID,
				(EzuceTranscriptWindow) getTranscriptWindow(),
				getScrollPaneForTranscriptWindow(), this.getChatInputEditor());

		getChatWindowPanel().add(
				chatHistoryManager.getUI(),
				new GridBagConstraints(0, 10, 1, 1, 1.0, 0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		chatHistoryManager.showHistory();*/
                
	}

	private void init() {

		final javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance().getContext().getActionMap(this);
		displayName = SparkManager.getUserManager().getUserNicknameFromJID(
				getParticipantJID());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setBackground(new Color(0xE6ECED));
		getTextScroller().setOpaque(false);
		getTextScroller().setBorder(BorderFactory.createEmptyBorder());

		jButtonCall = new EzuceRolloverButton();
		jButtonCall.setName("jButtonCall");
		jButtonCall.setRolloverIcon(resourceMap
				.getIcon("jButtonCall.rolloverIcon"));
		javax.swing.Action action = actionMap.get("callAction");
		action.putValue(javax.swing.Action.SMALL_ICON,
				resourceMap.getIcon("jButtonCall.icon"));
		jButtonCall.setAction(action);
		jButtonCall
				.setToolTipText(resourceMap.getString("jButtonCall.Tooltip"));

		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0,
				ColorConstants.SEPARATOR_COLOR_2));
		getToolbarPanel().add(s);
		s.setPreferredSize(new Dimension(2, 30));

		getToolbarPanel().add(jButtonCall);

		jScreenSharing = EzuceButtonFactory.getInstance()
				.createScreenSharingButton();
		jScreenSharing.addActionListener(new MultiCastScreenSharingListener(
				this));
		jScreenSharing.setToolTipText(resourceMap
				.getString("jScreenSharing.Tooltip"));
		if (Config.getInstance().isVideobridgeAvailable()) {
			getToolbarPanel().add(jScreenSharing);
		}

		JPanel headerContainer = new JPanel();
		VerticalLayout vl = new VerticalLayout(5);
		headerContainer.setLayout(vl);
		headerContainer.setOpaque(false);

		ecrjp = new EzuceChatRoomJPanel(participantJID);
		headerContainer.add(ecrjp);

		ecrjp.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(
				192, 192, 192)));
		add(headerContainer, gbc);

		for (Component comp : getToolBar().getComponents()) {
			// do not add Vcard panel here (this is added by spark inside
			// loadHistory method)
			if (!(comp instanceof VCardPanel)) {
				getToolbarPanel().add(comp);
			}
		}

		for (Component comp : getEditorBarLeft().getComponents()) {
			getToolbarPanel().add(comp);
		}
		for (Component comp : getEditorBarRight().getComponents()) {
			getToolbarPanel().add(comp);
		}
		getEditorWrapperBar().remove(getEditorBarLeft());
		getEditorWrapperBar().remove(getEditorBarRight());
		getEditorWrapperBar().setBackground(new Color(0xE6ECED));
		getToolbarPanel().setBackground(new Color(0xE6ECED));
		getEditorWrapperBar().add(getToolbarPanel(), BorderLayout.EAST);
		getEditorWrapperBar().setBorder(
				BorderFactory.createEmptyBorder(2, 2, 2, 2));

		getChatWindowPanel().remove(getScrollPaneForTranscriptWindow());
		getChatWindowPanel().add(
				getScrollPaneForTranscriptWindow(),
				new GridBagConstraints(0, 11, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
	}

	@Override
	public void closeChatRoom() {
		super.closeChatRoom();
		getChatInputEditor().getKeymap().removeBindings();
	}

	private JPanel getToolbarPanel() {
		if (toolbarPanel == null) {
			toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 1));
		}
		return toolbarPanel;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!customized) {
			setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));
			setBackground(new Color(199, 199, 199));
			LayoutManager lm = this.getLayout();
			if (lm instanceof GridBagLayout) {
				GridBagLayout gbl = (GridBagLayout) lm;
				GridBagConstraints gbc = gbl
						.getConstraints(getTranscriptWindow());
				gbc.ipadx = 2;
			}
			customized = true;
		}
	}

	@Override
	protected void addToolbar() {
		// do nothing - toolbar is added in init phase in a different location
	}

	@Override
	protected Insets getChatPanelInsets() {
		return new Insets(0, 0, 0, 0);
	}

	@Override
	protected Insets getChatAreaInsets() {
		return new Insets(0, 0, 0, 0);
	}

	@Override
	protected Insets getEditorWrapperInsets() {
		return new Insets(0, 0, 0, 0);
	}

	@Override
	public void addChatRoomComponent(JComponent component) {
		getToolbarPanel().add(component);
	}

	@Override
	public void addChatRoomButton(ChatRoomButton button) {
		addChatRoomButton(button, false);
	}

	@Override
	public void addChatRoomButton(ChatRoomButton button, boolean forceRepaint) {
		if (button != null) {
			getToolbarPanel().add(button);
			if (forceRepaint) {
				getToolbarPanel().invalidate();
				getToolbarPanel().repaint();
			}
		}
	}

	@Action
	public void callAction(ActionEvent ae) {
		VoiceUtil.execute(this);
	}

	@Override
	public void showToolbar() {
		// we don't use the inherited toolbar
	}

	@Override
	public void hideToolbar() {
		// we don't use the inherited toolbar
	}

	@Override
	public void addEditorComponent(JComponent component) {
		if (component != null) {
			getToolbarPanel().add(component,
					getToolbarPanel().getComponentCount() - 1);
		}
	}

	@Override
	public void removeEditorComponent(JComponent component) {
		if (component != null) {
			getToolbarPanel().remove(component);
		}
	}

	@Override
	protected void removeListeners() {
		getAddToRosterButton().removeActionListener(this);
		EzucePresenceUtils.unregisterRosterListener(ecrjp);
	}

	@Override
	public void notifyChatStateChange(ChatState state) {
		if (statePanel != null) {
			getEditorWrapperBar().remove(statePanel);
		}
		statePanel = new ChatStateIconPanel(resourceMap.getIcon(state.name()),
				displayName);
		getEditorWrapperBar().add(statePanel, BorderLayout.WEST);
		getEditorWrapperBar().revalidate();
		getEditorWrapperBar().repaint();
	}

	@Override
	public void registeredToFrame(ChatFrame chatframe) {
		super.registeredToFrame(chatframe);
		IMCommons.setChatFrameMinimumSize();
	}

	private class ChatStateIconPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public ChatStateIconPanel(Icon icon, String displayName) {
			setBackground(new Color(0xE6ECED));
			setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
			JLabel labelIcon = new JLabel(icon);
			JXLabel labelName = new JXLabel(displayName);
			labelName.setLineWrap(true);
			labelName.setFont(new Font("Courier New", Font.BOLD, 12));
			labelName.setForeground(Color.gray);
			labelName.setHorizontalTextPosition(JLabel.RIGHT);
			labelName.setVerticalTextPosition(JLabel.BOTTOM);
			add(labelIcon);
			add(labelName);
		}
	}

	@Override
	public void configureVoiceEnabled() {

	}

	@Override
	public void configureVoiceDisabled() {

	}

	@Override
	public void executeVoiceEnabled() {
		PhoneManager.getInstance().makeCall(
				Utils.getExtension(getParticipantJID()));
	}

	@Override
	public void executeVoiceDisabled() {
		sendMessage("@call");
	}

	@Override
	public String[] getInvitees() {
		return ArrayUtils.toArray(getJID());
	}

	@Override
	protected void loadHistory() {
		// empty
	}

}
