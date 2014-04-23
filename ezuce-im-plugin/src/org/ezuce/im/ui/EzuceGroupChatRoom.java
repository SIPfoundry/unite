package org.ezuce.im.ui;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.jivesoftware.smack.util.StringUtils.parseName;
import static org.jivesoftware.smack.util.StringUtils.parseResource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.EzuceButtonFactory;
import org.ezuce.common.EzuceChatRoomButton;
import org.ezuce.common.EzuceConferenceUtils;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.UniteRes;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.windows.ContactSearchDialog;
import org.ezuce.common.windows.ContactSearchDialog.OKButtonType;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.im.IMCommons;
import org.ezuce.im.ui.history.GroupChatHistoryManager;
import org.ezuce.media.manager.StreamingManager;
import org.ezuce.media.ui.listeners.MultiCastScreenSharingListener;
import org.ezuce.media.ui.listeners.ScreenSharingInvitees;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SessionManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.UserManager;
import org.jivesoftware.spark.plugin.ContextMenuListener;
import org.jivesoftware.spark.ui.ChatFrame;
import org.jivesoftware.spark.ui.ChatRoomButton;
import org.jivesoftware.spark.ui.TranscriptWindow;
import org.jivesoftware.spark.ui.TranscriptWindowInterceptor;
import org.jivesoftware.spark.ui.conferences.GroupChatParticipantList;
import org.jivesoftware.spark.ui.rooms.GroupChatRoom;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.log.Log;

/**
 * 
 * @author Razvan
 */
public class EzuceGroupChatRoom extends GroupChatRoom implements
		TranscriptWindowInterceptor, ScreenSharingInvitees, ConfigureVoice {
	private final JPanel toolbarPanel = new JPanel(new FlowLayout(
			FlowLayout.LEFT));
	private EzuceSubjectPanel subjPanel;
	private JPanel toolbarSubjectPanel;
	private EzuceGroupChatRoomPanel egcrPanel;
	private EzuceChatHeaderContainer echContainer;
	private String naturalLanguageName;
	private JButton jButtonEscalateToConf;
	private JButton jButtonInviteUsers;
	private EzuceChatRoomButton jScreenSharing;
	private Boolean iAmOwner = null;
	private final ResourceMap resourceMap = Application.getInstance()
			.getContext().getResourceMap(getClass());
	private ContextMenuListener contextMenuListener;

	//private GroupChatHistoryManager chatHistoryManager;

	public EzuceGroupChatRoom(final MultiUserChat muc) {
		super(muc);
		setSize(new Dimension(643, 460));
		setPreferredSize(new Dimension(643, 460));
		setMinimumSize(new Dimension(300, 100));

		contextMenuListener = new ContextMenuListener() {
			@Override
			public void poppingUp(Object component, JPopupMenu popup) {

				// Remove Spark changeSubjectAction, then add our own:
				Component[] menuItems = popup.getComponents();
				for (Component c : menuItems) {
					if (c instanceof JMenuItem) {
						if (((JMenuItem) c).getText().equalsIgnoreCase(
								Res.getString("menuitem.change.subject"))) {
							popup.remove(c);
							break;
						}
					}
				}

				javax.swing.Action subjectChangeAction = new AbstractAction() {
					private static final long serialVersionUID = 6730534406025965089L;

					public void actionPerformed(ActionEvent actionEvent) {
						String newSubject = JOptionPane.showInputDialog(
								getChatRoom(),
								Res.getString("message.enter.new.subject")
										+ ":",
								Res.getString("title.change.subject"),
								JOptionPane.QUESTION_MESSAGE);
						if (ModelUtil.hasLength(newSubject)) {
							try {
								getMultiUserChat().changeSubject(newSubject);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										refreshSubjectPanel();
									}
								});
							} catch (XMPPException e) {
								Log.error(e);
							}
						}
					}
				};

				subjectChangeAction.putValue(javax.swing.Action.NAME,
						Res.getString("menuitem.change.subject"));
				subjectChangeAction
						.putValue(
								javax.swing.Action.SMALL_ICON,
								SparkRes.getImageIcon(SparkRes.SMALL_MESSAGE_EDIT_IMAGE));
				popup.add(subjectChangeAction);
			}

			@Override
			public void poppingDown(JPopupMenu popup) {

			}

			@Override
			public boolean handleDefaultAction(MouseEvent e) {
				return false;
			}
		};
		// Add ContextMenuListener
		getTranscriptWindow().addContextMenuListener(contextMenuListener);

		SparkManager.getChatManager().getTranscriptWindowInterceptors()
				.add(this);

		init();
		repaint();

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

		// add history panel
		/*chatHistoryManager = GroupChatHistoryManager.create(getChatRoom()
				.getRoomname(), (EzuceTranscriptWindow) getTranscriptWindow(),
				getScrollPaneForTranscriptWindow(), this.getChatInputEditor());

		getChatWindowPanel().add(
				chatHistoryManager.getUI(),
				new GridBagConstraints(0, 10, 1, 1, 1.0, 0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));*/
	}

	private void init() {
		final javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance().getContext().getActionMap(this);

		setBorder(BorderFactory.createEmptyBorder());
		setBackground(new Color(0xE6ECED));
		getTextScroller().setOpaque(false);
		getTextScroller().setBorder(BorderFactory.createEmptyBorder());

		for (Component comp : getEditorBarLeft().getComponents()) {
			toolbarPanel.add(comp);
		}
		for (Component comp : getToolBar().getComponents()) {
			if (!(comp instanceof SubjectPanel)) {
				toolbarPanel.add(comp);
			}
		}

		jButtonEscalateToConf = new JButton();
		jButtonEscalateToConf.setBorder(null);
		jButtonEscalateToConf.setBorderPainted(false);
		jButtonEscalateToConf.setContentAreaFilled(false);
		jButtonEscalateToConf.setDoubleBuffered(true);
		jButtonEscalateToConf.setFocusPainted(false);
		jButtonEscalateToConf.setHideActionText(true);
		jButtonEscalateToConf.setName("jButtonEscalateToConf");
		jButtonEscalateToConf.setRolloverIcon(resourceMap
				.getIcon("jButtonEscalateToConf.rolloverIcon"));
		javax.swing.Action action = actionMap.get("escalateToConfAction");
		action.putValue(action.SMALL_ICON,
				resourceMap.getIcon("jButtonEscalateToConf.icon"));
		jButtonEscalateToConf.setAction(action);
		jButtonEscalateToConf.setToolTipText(resourceMap
				.getString("jButtonEscalateToConf.Tooltip"));

		jButtonInviteUsers = new JButton();
		jButtonInviteUsers.setBorder(null);
		jButtonInviteUsers.setContentAreaFilled(false);
		jButtonInviteUsers.setDoubleBuffered(true);
		jButtonInviteUsers.setFocusPainted(false);
		jButtonInviteUsers.setHideActionText(true);
		jButtonInviteUsers.setName("jButtonInviteUsers");
		jButtonInviteUsers.setRolloverIcon(resourceMap
				.getIcon("jButtonInviteUsers.rolloverIcon"));
		action = actionMap.get("inviteUsersToGroupChatAction");
		action.putValue(action.SMALL_ICON,
				resourceMap.getIcon("jButtonInviteUsers.icon"));
		jButtonInviteUsers.setAction(action);
		jButtonInviteUsers.setToolTipText(resourceMap
				.getString("jButtonInviteUsers.Tooltip"));

		toolbarPanel.add(jButtonInviteUsers);
		jScreenSharing = EzuceButtonFactory.getInstance()
				.createScreenSharingButton();
		jScreenSharing.addActionListener(new MultiCastScreenSharingListener(
				this));
		if (Config.getInstance().isVideobridgeAvailable()) {
			toolbarPanel.add(jScreenSharing);
		}
		for (Component comp : getEditorBarRight().getComponents()) {
			toolbarPanel.add(comp);
		}

		getBottomPanel().remove(getEditorWrapperBar());
		toolbarPanel.setBackground(new Color(0xE6ECED));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		getBottomPanel().add(toolbarPanel, gbc);

		getVerticalSlipPane().setDividerSize(2);
		BasicSplitPaneDivider divider = (BasicSplitPaneDivider) (getVerticalSlipPane()
				.getComponent(0));
		divider.setBorder(null);

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
		SparkManager.getChatManager().getTranscriptWindowInterceptors()
				.remove(this);
		if (egcrPanel != null) {
			EzucePresenceUtils.unregisterRosterListener(egcrPanel);
		}
		if (contextMenuListener != null) {
			getTranscriptWindow()
					.removeContextMenuListener(contextMenuListener);
		}
		if (getConferenceRoomInfo() != null
				&& getConferenceRoomInfo() instanceof EzuceGroupChatParticipantList) {
			EzucePresenceUtils
					.unregisterRosterListener(((EzuceGroupChatParticipantList) getConferenceRoomInfo()));
		}

	}

	private void configureScreenSharing(boolean start, boolean transmitting) {
		GroupChatParticipantList gcpl = getConferenceRoomInfo();
		if (gcpl != null && gcpl instanceof EzuceGroupChatParticipantList) {
			EzuceGroupChatParticipantList egcpl = (EzuceGroupChatParticipantList) gcpl;
			egcpl.enableStartScreenSharing(start, transmitting);
		}
		if (start && transmitting) {
			jScreenSharing.setIcon(UniteRes
					.getImageIcon(UniteRes.SCREEN_SHARING_ON));
			jScreenSharing.setEnabled(true);
		} else if (start && !transmitting) {
			jScreenSharing.setIcon(UniteRes
					.getImageIcon(UniteRes.SCREEN_SHARING_GREY));
			jScreenSharing.setEnabled(false);
		} else {
			jScreenSharing.setIcon(UniteRes
					.getImageIcon(UniteRes.SCREEN_SHARING_OFF));
			jScreenSharing.setEnabled(true);
		}
	}

	public void buildChatHeader() {
		echContainer = new EzuceChatHeaderContainer();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		buildChatRoomNameHeader(getRoomTitle());
		add(echContainer, gbc);
	}

	public void buildAudioHeader() {
		if (iAmOwner() && EzuceConferenceUtils.isGroupChatAudio(getRoomTitle())) {
			naturalLanguageName = retrieveNaturalLanguageName();
			EzuceGroupChatParticipantList ezuceRoomInfo = null;
			if (getConferenceRoomInfo() instanceof EzuceGroupChatParticipantList) {
				ezuceRoomInfo = (EzuceGroupChatParticipantList) getConferenceRoomInfo();
			}
			egcrPanel = new EzuceGroupChatRoomPanel(ezuceRoomInfo, this);
			egcrPanel.setParentHeaderContainer(echContainer);
			// Gets participants in conference and displays them.
			// if no one is in the audio conference this panel will be
			// invisible, or if the current user is not owner
			egcrPanel.refreshParticipantsList();

			echContainer.add(egcrPanel, 0);
			toolbarPanel.add(jButtonEscalateToConf, 0);
		}
	}

	private void buildChatRoomNameHeader(String roomName) {
		subjPanel = new EzuceSubjectPanel(
				resourceMap.getString("type.groupchat"), roomName);
		subjPanel.setBackground(toolbarPanel.getBackground());
		echContainer.add(subjPanel);
	}

	protected void buildToolbarSubjectPanel() {
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		toolbarPanel.add(s);
		s.setPreferredSize(new Dimension(2, 30));
		s.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0,
				ColorConstants.SEPARATOR_COLOR_2));
		toolbarSubjectPanel = createSubjectPanel();
		toolbarPanel.add(toolbarSubjectPanel);
	}

	protected void refreshSubjectPanel() {
		if (toolbarSubjectPanel != null) {
			Component[] components = toolbarSubjectPanel.getComponents();
			for (Component c : components) {
				if (c instanceof JLabel) {
					if (((JLabel) c).getName().equalsIgnoreCase("subjectLabel")) {
						String subjectText = getMultiUserChat().getSubject();
						JLabel label = (JLabel) c;
						label.setText("<html>"
								+ resourceMap.getString("groupchat.subject")
								+ "<b>"
								+ (subjectText == null ? " none" : " "
										+ subjectText) + "</b></html>");
						toolbarSubjectPanel.revalidate();
						toolbarSubjectPanel.repaint();

						toolbarPanel.revalidate();
						toolbarPanel.repaint();

						break;
					}
				}
			}
		}
	}

	@Override
	public void registeredToFrame(ChatFrame chatframe) {
		super.registeredToFrame(chatframe);
		IMCommons.setChatFrameMinimumSize();
	}

	public boolean iAmOwner() {
		if (iAmOwner != null) {
			return iAmOwner;
		}
		try {
			Iterator<String> iterator = getMultiUserChat()
					.getConfigurationForm()
					.getField("muc#roomconfig_roomowners").getValues();
			String owner = null;
			String currentImId = SparkManager.getSessionManager()
					.getBareAddress();
			while (iterator.hasNext()) {
				owner = iterator.next();
				if (owner.equals(currentImId)) {
					iAmOwner = true;
					break;
				}
			}
		} catch (Exception ex) {
			Log.warning("Cannot retrieve XMPP info about ownership");
			iAmOwner = false;
		}

		return iAmOwner;
	}

	private String retrieveNaturalLanguageName() {
		String naturalLangName = null;
		try {
			naturalLangName = getMultiUserChat().getConfigurationForm()
					.getField("muc#roomconfig_roomname").getValues().next();
		} catch (Exception ex) {

		}
		return naturalLangName;
	}

	private JPanel createSubjectPanel() {
		JPanel subject = new JPanel();
		subject.setBackground(new Color(0, 0, 0, 0));
		subject.setOpaque(false);
		String subjectText = getMultiUserChat().getSubject();
		JLabel label = new JLabel();
		label.setName("subjectLabel");
		label.setForeground(Color.GRAY);
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setText("<html>"
				+ resourceMap.getString("groupchat.subject")
				+ "<b>"
				+ (subjectText == null ? " " + Res.getString("none") : " "
						+ subjectText) + "</b></html>");
		subject.add(label);
		return subject;
	}

	public JButton getJButtonEscalateToConf() {
		return this.jButtonEscalateToConf;
	}

	public String getNaturalLanguageName() {
		return naturalLanguageName;
	}

	public EzuceGroupChatRoomPanel getGroupChatRoomPanel() {
		return egcrPanel;
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
		toolbarPanel.add(component, 0);
	}

	@Override
	public void addChatRoomButton(ChatRoomButton button) {
		addChatRoomButton(button, false);
	}

	@Override
	public void addChatRoomButton(ChatRoomButton button, boolean forceRepaint) {
		if (button != null) {
			toolbarPanel.add(button);
			if (forceRepaint) {
				toolbarPanel.invalidate();
				toolbarPanel.repaint();
			}
		}
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
			toolbarPanel.add(component);
		}
	}

	@Override
	public void removeEditorComponent(JComponent component) {
		if (component != null) {
			toolbarPanel.remove(component);
		}
	}

	@Override
	public boolean isMessageIntercepted(TranscriptWindow window, String userid,
			Message message) {
		return false;
	}

	@Action
	public void escalateToConfAction(ActionEvent ae) {
		Collection<String> occupants = getOccupantJIDs();
		for (String occupant : occupants) {
			inviteUserToAudioConf(parseName(occupant));
		}
	}

	/**
	 * Invite user into group chat room only if the user is not already in the
	 * room
	 * 
	 * @param id
	 */
	public void inviteUser(Collection<String> occupantJIDs, String id) {
		String jid = Utils.getJidFromId(id);
		SessionManager sessionManager = SparkManager.getSessionManager();
		UserManager userManager = SparkManager.getUserManager();
		if (!occupantJIDs.contains(jid)
				&& !StringUtils.equals(jid, sessionManager.getBareAddress())) {
			getMultiUserChat().invite(jid,
					Res.getString("message.please.join.in.conference"));
			getTranscriptWindow().insertNotificationMessage(
					resourceMap.getString("invited.message.groupchat",
							userManager.getUserNicknameFromJID(jid)),
					ChatManager.NOTIFICATION_COLOR);
		}
	}

	public void inviteUser(String id) {
		inviteUser(getOccupantJIDs(), id);
	}

	public Collection<String> getOccupantJIDs() {
		return ((EzuceGroupChatParticipantList) getConferenceRoomInfo())
				.getNicknameMap().values();
	}

	public void inviteUserToAudioConf(String toUser) {
		UserManager userManager = SparkManager.getUserManager();
		String conferenceName = getNaturalLanguageName();
		EzuceGroupChatRoomPanel panel = getGroupChatRoomPanel();
		List<ConferenceMemberXML> confMembers = panel == null ? null : panel
				.getConfUsers();
		if (!EzuceConferenceUtils.isImUserInConference(confMembers, toUser)) {
			String jid = Utils.getJidFromId(toUser);
			String myJid = SparkManager.getSessionManager().getBareAddress();
			String myNickname = userManager.getNickname();
			String nickname = StringUtils.equals(jid, myJid) ? myNickname
					: userManager.getUserNicknameFromJID(jid);
			if (RestManager.getInstance().inviteToBridgeIm(toUser,
					conferenceName)) {
				getTranscriptWindow().insertNotificationMessage(
						resourceMap.getString("invited.message.audiobridge",
								nickname), ChatManager.NOTIFICATION_COLOR);
			} else {
				getTranscriptWindow().insertNotificationMessage(
						resourceMap.getString(
								"not.invited.message.audiobridge", nickname),
						ChatManager.ERROR_COLOR);
			}
		}
	}

	@Action
	public void inviteUsersToGroupChatAction(ActionEvent ae) {
		// Open ContactSearchDialog, where the user is allowed to use the
		// context menu and invite
		// any contact to bridge:
		List<ContactListEntry> cles = ContactSearchDialog
				.showMultiContactSearchDialog(
						(Frame) SwingUtilities.windowForComponent(this), true,
						true, OKButtonType.INVITE);

		if (cles == null || cles.isEmpty()) {
			return;
		}

		Collection<String> occupantJIDs = getOccupantJIDs();
		String imId = null;
		for (ContactListEntry cle : cles) {
			imId = cle.getImId();
			// this checking applies when no user is selected in contact-search
			// dialog.
			// We assume that the entered value, in the text field is an IM id
			String toUser = isEmpty(imId) ? cle.getNumber() : imId;
			try {
				inviteUser(occupantJIDs, toUser);
			} catch (Exception ex) {
				Log.error("Cannot send invitation ", ex);
				JOptionPane.showMessageDialog(
						(Frame) SwingUtilities.windowForComponent(this),
						resourceMap.getString("error.invite.message"),
						resourceMap.getString("error.invite.title"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public String[] getInvitees() {
		EzuceGroupChatParticipantList ezuceRoomInfo = null;
		if (getConferenceRoomInfo() instanceof EzuceGroupChatParticipantList) {
			ezuceRoomInfo = (EzuceGroupChatParticipantList) getConferenceRoomInfo();
		}
		return (String[]) (ezuceRoomInfo.getParticipantsMap().values()
				.toArray(new String[ezuceRoomInfo.getParticipantsMap().size()]));
	}

	public void configureXMPPScreenSharing() {
		if (!Config.getInstance().isVideobridgeAvailable()) {
			return;
		}
		if (StreamingManager.getInstance().isTransmitting()) {
			configureScreenSharing(true, true);
		} else if (StreamingManager.getInstance().isReceiving()) {
			configureScreenSharing(true, false);
		} else {
			configureScreenSharing(false, false);
		}
	}

	@Override
	public void configureVoiceEnabled() throws Exception {
		configureXMPPScreenSharing();
	}

	@Override
	public void configureVoiceDisabled() {
		configureXMPPScreenSharing();
	}

	@Override
	protected void handleMessagePacket(Packet packet) {
		// super.handleMessagePacket(packet);
		// Do something with the incoming packet here.
		final Message message = (Message) packet;
		setLastMessage(message);

		// Do not accept Administrative messages.
		String host = SparkManager.getSessionManager().getServerAddress();
		if (host.equals(message.getFrom()))
			return;

		if (isBlocked(message.getFrom())) {
			return;
		}

		if (!SparkManager.getUserManager().hasVoice(this,
				parseResource(message.getFrom()))) {
			return;
		}

		if (!ModelUtil.hasLength(message.getBody()))
			return;

		if (message.getType() == Message.Type.groupchat) {

			Date messageSentDate = null;
			DelayInformation inf = (DelayInformation) message.getExtension("x",
					"jabber:x:delay");

			messageSentDate = inf != null ? inf.getStamp() : new Date();
			message.setProperty("date", messageSentDate);

			//if (chatHistoryManager.isHistoryMessage(message))
				//return;

			String messageNickname = parseResource(message.getFrom());
			boolean isFromMe = messageNickname.equals(getNickname())
					&& inf == null;
			if (isFromMe)
				return;

			// show the room's message but don't keep it in the transcript.
			String from = parseResource(message.getFrom());
			boolean isFromRoom = message.getFrom().indexOf("/") == -1;
			if (isFromRoom) {
				((EzuceTranscriptWindow) getTranscriptWindow())
						.insertHistoryMessage(from, from, message.getBody(),
								(Date) message.getProperty("date"), true);
				return;
			}

			// Update transcript
			super.insertMessage(message);

			// update html document
			((EzuceTranscriptWindow) getTranscriptWindow()).insertMessage(from,
					message, getColor(from),
					getMessageBackground(from, message.getBody()));

		} else if (message.getError() != null) {
			String errorMessage = "";

			if (message.getError().getCode() == 403
					&& message.getSubject() != null) {
				errorMessage = Res.getString("message.subject.change.error");
			}

			else if (message.getError().getCode() == 403) {
				errorMessage = Res.getString("message.forbidden.error");
			}

			if (ModelUtil.hasLength(errorMessage)) {
				getTranscriptWindow().insertNotificationMessage(errorMessage,
						ChatManager.ERROR_COLOR);
			}
		}
	}

	@Override
	public void addToTranscript(Message message, boolean updateDate) {
		Date sentDate = (Date) message.getProperty("date");
		boolean needToReplace = message.getType() == Message.Type.groupchat
				&& sentDate == null && message.getFrom() == null;
		if (needToReplace) {
			message.setFrom(message.getTo() + "/" + getNickname()); // not sure
			message.setTo(SparkManager.getSessionManager().getJID());
		}
		super.addToTranscript(message, updateDate);

	}

}
