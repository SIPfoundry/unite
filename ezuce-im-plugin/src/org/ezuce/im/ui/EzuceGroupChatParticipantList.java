package org.ezuce.im.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.UniteRes;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.MugshotPanel;
import org.ezuce.common.ui.panels.Images;
import org.ezuce.media.manager.StreamingManager;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.UserManager;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactListListener;
import org.jivesoftware.spark.ui.conferences.ConferenceUtils;
import org.jivesoftware.spark.ui.conferences.GroupChatParticipantList;
import org.jivesoftware.spark.util.log.Log;

/**
 * 
 * @author Razvan
 */
public class EzuceGroupChatParticipantList extends GroupChatParticipantList
		implements ContactListListener, RosterListener {
	private EzuceGroupChatRoomPanel egcrp;
	private HashMap<String, String> participantsMap = new HashMap<String, String>();
	private HashMap<String, String> nicknameMap = new HashMap<String, String>();

	public EzuceGroupChatParticipantList() {
		super();
		getParticipantsList().setCellRenderer(new EzuceParticipantRenderer());
		// Respond to Double-Click in Agent List to start a chat
		for (MouseListener ml : getParticipantsList().getMouseListeners()) {
			getParticipantsList().removeMouseListener(ml);
		}
		MouseListener mouseListener = new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				int index = getParticipantsList().locationToIndex(
						evt.getPoint());
				getParticipantsList().setSelectedIndex(index);
				if (evt.getClickCount() == 2) {
					String selectedUser = getSelectedUser();
					startActiveChat(StringUtils.parseBareAddress(participantsMap.get(selectedUser)),
							selectedUser, selectedUser);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					checkPopup(evt);
				}
			}

			@Override
			public void mousePressed(final MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					checkPopup(evt);
				}
			}
		};
		getParticipantsList().addMouseListener(mouseListener);
		EzucePresenceUtils.registerRosterListener(this);
	}
	
	public EzuceGroupChatRoomPanel getGroupChatPanel() {
		return egcrp;
	}

	public void setGroupChatPanel(EzuceGroupChatRoomPanel panel) {
		this.egcrp = panel;
	}

	@Override
	public void addInvitee(String jid, String message) {
		// verify if the user was already invited
		String invitedJid = null;
		String invitedBareJid = null;
		for (String displayName : getInvitees().keySet()) {
			invitedJid = SparkManager.getUserManager().getJIDFromDisplayName(
					displayName);
			if (invitedJid == null) {
				continue;
			}
			invitedBareJid = StringUtils.parseBareAddress(invitedJid);
			if (jid.equals(invitedBareJid)) {
				return;
			}
		}
		//super.addInvitee(jid, message);
                final UserManager um = SparkManager.getUserManager();

		String displayName = um.getUserNicknameFromJID(jid);

		getGroupChatRoom().getTranscriptWindow().insertNotificationMessage(
				displayName + " " + Res.getString("groupchat.newuser.invited"),
				ChatManager.NOTIFICATION_COLOR);

		if (getRoomInformation() != null
				&& !getRoomInformation().containsFeature("muc_nonanonymous")) {
			return;
		}
                                
		final ImageIcon inviteIcon = Utils.getAvatarFromCache(jid);
                        
		addUser(inviteIcon, displayName);

		getInvitees().put(displayName, message);

	}

	private String getMyDisplayName() {
		VCard vCard = SparkManager.getVCardManager().getVCard();
		if (vCard != null && vCard.getError() == null) {
			final String firstName = vCard.getFirstName();
			final String lastName = vCard.getLastName();
			String nickname = vCard.getNickName();
			if (ModelUtil.hasLength(firstName) && ModelUtil.hasLength(lastName)) {
				return firstName + " " + lastName;
			} else if (ModelUtil.hasLength(firstName)) {
				return firstName;
			} else if (ModelUtil.hasLength(nickname)) {
				return nickname;
			} else {
				nickname = SparkManager.getSessionManager().getUsername();
				return nickname;
			}
		} else {
			final String nickname = SparkManager.getSessionManager()
					.getUsername();
			return nickname;
		}
	}

	public HashMap<String, String> getParticipantsMap() {
		return participantsMap;
	}

	public HashMap<String, String> getNicknameMap() {
		return nicknameMap;
	}

	@Override
	protected void addParticipant(final String participantJID, Presence presence, String jid) {
		if (jid == null) {
			// fallback to original addParticipant method
			super.addParticipant(participantJID, presence, jid);
			return;
		}

		String displayName = null;
		if (org.apache.commons.lang3.StringUtils.equals(jid, SparkManager
				.getSessionManager().getBareAddress())) {
			displayName = getMyDisplayName();
		} else {
			displayName = SparkManager.getUserManager().getUserNicknameFromJID(
					jid);
		}
		super.addParticipant(participantJID, presence, jid);
		
		String fullJID = SparkManager.getUserManager().getFullJID(jid);
		participantsMap.put(displayName, fullJID != null ? fullJID : jid);

		// Customize the display of participants in vertical right panel:
		String nickname = StringUtils.parseResource(participantJID);
		nicknameMap.put(nickname, jid);

		int index = getIndex(nickname);
		if (index != -1) {
			if (getModel().getElementAt(index) instanceof JLabel) {
				final JLabel userLabel = (JLabel) (getModel()
						.getElementAt(index));
				if (userLabel != null) {
					EzuceGroupChatParticipantMiniPanel participantPanel = new EzuceGroupChatParticipantMiniPanel(jid, this);				
					participantPanel.setNickname(userLabel.getText());
					participantPanel.setUserDisplayName(displayName);
					participantPanel.setUserAvatar(Utils.retrieveAvatar(SparkManager.getVCardManager().getVCard(jid)));
					getModel().setElementAt(participantPanel, index);
				}
			}
		}

		getParticipantsList().setModel(getModel());
		getParticipantsList().revalidate();
		getParticipantsList().repaint();
		revalidate();
		repaint();
		String bareJid = StringUtils.parseBareAddress(jid);
		if (egcrp != null) {
			EzuceChatUserMiniPanel ecump = egcrp.getUserMiniPanelByJid(bareJid);
			if (ecump != null) {
				ecump.updateStatus(presence);
			}
		}
	}

	@Override
	protected String getSelectedUser() {
		JComponent label = (JComponent) getParticipantsList()
				.getSelectedValue();
		if (label != null) {
			if (label instanceof JLabel) {
				return ((JLabel) label).getText();
			} else if (label instanceof EzuceGroupChatParticipantMiniPanel) {
				return ((EzuceGroupChatParticipantMiniPanel) label)
						.getUserDisplayName();
			}
		}

		return null;
	}
	
	public void enableStartScreenSharing(boolean enable, boolean transmitting) {
		ListModel model = getParticipantsList().getModel();

		for (int i = 0; i < model.getSize(); i++) {
			Object o = model.getElementAt(i);
			if (o instanceof EzuceGroupChatParticipantMiniPanel) {
				EzuceGroupChatParticipantMiniPanel egcpmp = (EzuceGroupChatParticipantMiniPanel) o;
				boolean condition = transmitting ? 
						StreamingManager.getInstance().isTransmittingTo(egcpmp.getUserJid()) : StreamingManager.getInstance().isReceivingFrom(egcpmp.getUserJid());  
				if (enable && condition) {
					egcpmp.getScreenSharingButton().setVisible(true);
					egcpmp.getScreenSharingButton().setIcon(UniteRes.getImageIcon(UniteRes.SCREEN_SHARING_ON));					
				} else {
					egcpmp.getScreenSharingButton().setVisible(false);
				}
			}
		}		
		repaint();
	}

	@Override
	protected void checkPopup(MouseEvent evt) {
		Point p = evt.getPoint();
		final int index = getParticipantsList().locationToIndex(p);

		final JPopupMenu popup = new JPopupMenu();

		if (index != -1
				&& (getModel().getElementAt(index) instanceof EzuceGroupChatParticipantMiniPanel)) {
			getParticipantsList().setSelectedIndex(index);
			final EzuceGroupChatParticipantMiniPanel userLabel = (EzuceGroupChatParticipantMiniPanel) getModel()
					.getElementAt(index);
			final String selectedUser = userLabel.getNickname();
			final String groupJID = getUserMap().get(selectedUser);
			String groupJIDNickname = StringUtils.parseResource(groupJID);

			final String nickname = getGroupChatRoom().getNickname();
			final Occupant occupant = SparkManager.getUserManager()
					.getOccupant(getGroupChatRoom(), selectedUser);
			if (occupant == null) {
				Log.error("Cannot retrieve occupant - probably a network outage took place during client running");
			}
			final boolean iamAdmin = SparkManager.getUserManager().isAdmin(
					getGroupChatRoom(), getChat().getNickname());
			final boolean iamOwner = SparkManager.getUserManager().isOwner(
					getGroupChatRoom(), getChat().getNickname());

			final boolean iamAdminOrOwner = iamAdmin || iamOwner;

			final boolean iamModerator = SparkManager.getUserManager()
					.isModerator(getGroupChatRoom(), getChat().getNickname());

			final boolean userIsMember = occupant == null ? false
					: SparkManager.getUserManager().isMember(occupant);
			final boolean userIsAdmin = occupant == null ? false : SparkManager
					.getUserManager().isAdmin(getGroupChatRoom(),
							occupant.getNick());
			final boolean userIsOwner = occupant == null ? false : SparkManager
					.getUserManager().isOwner(occupant);
			final boolean userIsModerator = occupant == null ? false
					: SparkManager.getUserManager().isModerator(occupant);
			boolean selectedMyself = nickname.equals(groupJIDNickname);

			// Handle invites
			if (groupJIDNickname == null) {
				Action inviteAgainAction = new AbstractAction() {
					private static final long serialVersionUID = -1875073139356098243L;

					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						String message = getInvitees().get(selectedUser);
						String jid = SparkManager.getUserManager()
								.getJIDFromDisplayName(selectedUser);
						getChat().invite(jid, message);
					}
				};

				inviteAgainAction.putValue(Action.NAME,
						Res.getString("menuitem.inivite.again"));
				popup.add(inviteAgainAction);

				Action removeInvite = new AbstractAction() {
					private static final long serialVersionUID = -3647279452501661970L;

					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						int index = getIndex(selectedUser);

						if (index != -1) {
							getModel().removeElementAt(index);
						}
					}
				};

				removeInvite.putValue(Action.NAME,
						Res.getString("menuitem.remove"));
				popup.add(removeInvite);

				popup.show(getParticipantsList(), evt.getX(), evt.getY());
				return;
			}

			if (selectedMyself) {
				Action changeNicknameAction = new AbstractAction() {
					private static final long serialVersionUID = -7891803180672794112L;

					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						String newNickname = JOptionPane.showInputDialog(
								getGroupChatRoom(),
								Res.getString("label.new.nickname") + ":",
								Res.getString("title.change.nickname"),
								JOptionPane.QUESTION_MESSAGE);
						if (ModelUtil.hasLength(newNickname)) {
							while (true) {
								newNickname = newNickname.trim();
								String nick = getChat().getNickname();
								if (newNickname.equals(nick)) {
									// return;
								}
								try {
									getChat().changeNickname(newNickname);
									break;
								} catch (XMPPException e1) {
									if (e1.getXMPPError().getCode() == 406) { // handle
										// deny
										// changing
										// nick
										JOptionPane
												.showMessageDialog(
														getGroupChatRoom(),
														Res.getString("message.nickname.not.acceptable"),
														Res.getString("title.change.nickname"),
														JOptionPane.ERROR_MESSAGE);
										break;
									}
									newNickname = JOptionPane
											.showInputDialog(
													getGroupChatRoom(),
													Res.getString("message.nickname.in.use")
															+ ":",
													Res.getString("title.change.nickname"),
													JOptionPane.QUESTION_MESSAGE);
									if (!ModelUtil.hasLength(newNickname)) {
										break;
									}
								}
							}
						}
					}
				};

				changeNicknameAction.putValue(Action.NAME,
						Res.getString("menuitem.change.nickname"));
				changeNicknameAction.putValue(Action.SMALL_ICON,
						SparkRes.getImageIcon(SparkRes.DESKTOP_IMAGE));

				if (isAllowNicknameChange()) {
					popup.add(changeNicknameAction);
				}
			}

			Action chatAction = new AbstractAction() {
				private static final long serialVersionUID = -2739549054781928195L;

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					String selectedUser = getSelectedUser();
					startActiveChat(participantsMap.get(selectedUser),
							selectedUser, selectedUser);
				}
			};

			chatAction.putValue(Action.NAME,
					Res.getString("menuitem.start.a.chat"));
			chatAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.SMALL_MESSAGE_IMAGE));
			if (!selectedMyself) {
				popup.add(chatAction);
			}

			Action blockAction = new AbstractAction() {
				private static final long serialVersionUID = 8771362206105723776L;

				@Override
				public void actionPerformed(ActionEvent e) {
					ImageIcon icon;
                                        
                                        MugshotPanel.State state;
					if (getGroupChatRoom().isBlocked(groupJID)) {
						getGroupChatRoom().removeBlockedUser(groupJID);
                                                state = MugshotPanel.State.NONE;
					} else {
						getGroupChatRoom().addBlockedUser(groupJID);
                                                state = MugshotPanel.State.BLOCKED;
					}

					((EzuceGroupChatParticipantMiniPanel) (getModel()
							.get(index))).setUserState(state);
					getParticipantsList().revalidate();
					getParticipantsList().repaint();

				}
			};

			blockAction.putValue(Action.NAME,
					Res.getString("menuitem.block.user"));
			blockAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.BRICKWALL_IMAGE));
			if (!selectedMyself) {
				if (getGroupChatRoom().isBlocked(groupJID)) {
					blockAction.putValue(Action.NAME,
							Res.getString("menuitem.unblock.user"));
				}
				popup.add(blockAction);
			}

			Action kickAction = new AbstractAction() {
				private static final long serialVersionUID = 5769982955040961189L;

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					kickUser(selectedUser);
				}
			};

			kickAction.putValue(Action.NAME,
					Res.getString("menuitem.kick.user"));
			kickAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.SMALL_DELETE));
			if (iamModerator && !userIsAdmin && !selectedMyself) {
				popup.add(kickAction);
			}

			// Handle Voice Operations
			Action voiceAction = new AbstractAction() {
				private static final long serialVersionUID = 7628207942009369329L;

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (SparkManager.getUserManager().hasVoice(
							getGroupChatRoom(), selectedUser)) {
						revokeVoice(selectedUser);
					} else {
						grantVoice(selectedUser);
					}
					Collections.sort(getUsers(), getLabelComp());

				}
			};

			voiceAction.putValue(Action.NAME, Res.getString("menuitem.voice"));
			voiceAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.MEGAPHONE_16x16));
			if (iamModerator && !userIsModerator && !selectedMyself) {
				if (SparkManager.getUserManager().hasVoice(getGroupChatRoom(),
						selectedUser)) {
					voiceAction.putValue(Action.NAME,
							Res.getString("menuitem.revoke.voice"));
				} else {
					voiceAction.putValue(Action.NAME,
							Res.getString("menuitem.grant.voice"));
				}
				popup.add(voiceAction);
			}

			Action banAction = new AbstractAction() {
				private static final long serialVersionUID = 4290194898356641253L;

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					banUser(selectedUser);
				}
			};
			banAction.putValue(Action.NAME, Res.getString("menuitem.ban.user"));
			banAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.RED_FLAG_16x16));
			if (iamAdminOrOwner && !userIsModerator && !selectedMyself) {
				popup.add(banAction);
			}

			JMenu affiliationMenu = new JMenu(
					Res.getString("menuitem.affiliation"));
			affiliationMenu.setIcon(SparkRes
					.getImageIcon(SparkRes.MODERATOR_IMAGE));

			Action memberAction = new AbstractAction() {
				private static final long serialVersionUID = -2528887841227305432L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!userIsMember) {
						grantMember(selectedUser);
					} else {
						revokeMember(selectedUser);
					}
					Collections.sort(getUsers(), getLabelComp());
				}
			};
			memberAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.STAR_YELLOW_IMAGE));
			if (iamAdminOrOwner && !userIsMember) {
				memberAction.putValue(Action.NAME,
						Res.getString("menuitem.grant.member"));
				affiliationMenu.add(memberAction);
			} else if (iamAdminOrOwner && userIsMember && !selectedMyself) {
				memberAction.putValue(Action.NAME,
						Res.getString("menuitem.revoke.member"));
				affiliationMenu.add(memberAction);
			}

			Action moderatorAction = new AbstractAction() {
				private static final long serialVersionUID = 8162535640460764896L;

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (!userIsModerator) {
						grantModerator(selectedUser);
					} else {
						revokeModerator(selectedUser);
					}
					Collections.sort(getUsers(), getLabelComp());

				}
			};

			moderatorAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.STAR_MODERATOR));

			if (iamAdminOrOwner && !userIsModerator && !userIsAdmin
					&& !userIsOwner) {
				moderatorAction.putValue(Action.NAME,
						Res.getString("menuitem.grant.moderator"));
				affiliationMenu.add(moderatorAction);
			} else if (iamAdminOrOwner && userIsModerator && !selectedMyself) {
				moderatorAction.putValue(Action.NAME,
						Res.getString("menuitem.revoke.moderator"));
				affiliationMenu.add(moderatorAction);
			}

			Action adminAction = new AbstractAction() {
				private static final long serialVersionUID = 3672121864443182872L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!userIsAdmin) {
						grantAdmin(selectedUser);
					} else {
						revokeAdmin(selectedUser);
					}
					Collections.sort(getUsers(), getLabelComp());

				}
			};
			adminAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.STAR_ADMIN));
			if (iamAdminOrOwner && !userIsAdmin && !userIsOwner) {
				adminAction.putValue(Action.NAME,
						Res.getString("menuitem.grant.admin"));
				affiliationMenu.add(adminAction);
			} else if (iamAdminOrOwner && !selectedMyself) {
				adminAction.putValue(Action.NAME,
						Res.getString("menuitem.revoke.admin"));
				affiliationMenu.add(adminAction);
			}

			Action ownerAction = new AbstractAction() {
				private static final long serialVersionUID = 3672121864443182872L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!userIsOwner) {
						grantOwner(selectedUser);
					} else {
						revokeOwner(selectedUser);
					}
					Collections.sort(getUsers(), getLabelComp());

				}
			};
			ownerAction.putValue(Action.SMALL_ICON,
					SparkRes.getImageIcon(SparkRes.STAR_OWNER));

			if (iamOwner && !userIsOwner) {
				ownerAction.putValue(Action.NAME,
						Res.getString("menuitem.grant.owner"));
				affiliationMenu.add(ownerAction);
			} else if (iamOwner && !selectedMyself) {
				ownerAction.putValue(Action.NAME,
						Res.getString("menuitem.revoke.owner"));
				affiliationMenu.add(ownerAction);
			}

			if (affiliationMenu.getItemCount() > 0)
				popup.add(affiliationMenu);

			// Handle Unbanning of users.
			Action unbanAction = new AbstractAction() {
				private static final long serialVersionUID = 3672121864443182872L;

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					String jid = ((JMenuItem) actionEvent.getSource())
							.getText();
					unbanUser(jid);
				}
			};

			if (iamAdmin || iamOwner) {
				JMenu unbanMenu = new JMenu(Res.getString("menuitem.unban"));
				Iterator<Affiliate> bannedUsers = null;
				try {
					bannedUsers = getChat().getOutcasts().iterator();
				} catch (XMPPException e) {
					Log.error("Error loading all banned users", e);
				}

				while (bannedUsers != null && bannedUsers.hasNext()) {
					Affiliate bannedUser = (Affiliate) bannedUsers.next();
					ImageIcon icon = SparkRes.getImageIcon(SparkRes.RED_BALL);
					JMenuItem bannedItem = new JMenuItem(bannedUser.getJid(),
							icon);
					unbanMenu.add(bannedItem);
					bannedItem.addActionListener(unbanAction);
				}

				if (unbanMenu.getMenuComponentCount() > 0) {
					popup.add(unbanMenu);
				}
			}
		}

		Action inviteAction = new AbstractAction() {
			private static final long serialVersionUID = 2240864466141501086L;

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ConferenceUtils.inviteUsersToRoom(getGroupChatRoom()
						.getConferenceService(), getGroupChatRoom()
						.getRoomname(), null, false);
			}
		};

		inviteAction.putValue(Action.NAME,
				Res.getString("menuitem.invite.users"));
		inviteAction.putValue(Action.SMALL_ICON,
				SparkRes.getImageIcon(SparkRes.CONFERENCE_IMAGE_16x16));

		if (index != -1) {
			popup.addSeparator();
		}
		popup.add(inviteAction);

		popup.show(getParticipantsList(), evt.getX(), evt.getY());
	}

	private void startActiveChat(String userJID, String nickname, String title) {
		// you cannot chat with yourself
		String currentJid = SparkManager.getSessionManager().getBareAddress();
		if (userJID.equals(currentJid)) {
			return;
		}
		ChatManager chatManager = SparkManager.getChatManager();
		ChatRoom room = chatManager.createChatRoom(userJID, nickname, title);
		chatManager.getChatContainer().activateChatRoom(room);
	}

	@Override
	protected ImageIcon getImageIcon(String participantJID) {
		String displayName = StringUtils.parseResource(participantJID);
		ImageIcon icon = SparkRes.getImageIcon(SparkRes.GREEN_BALL);
		icon.setDescription(displayName);
		return icon;
	}

	@Override
	public int getIndex(String name) {
		for (int i = 0; i < getModel().getSize(); i++) {
			JComponent comp = (JComponent) getModel().getElementAt(i);
			if (comp instanceof JLabel) {
				if (((JLabel) comp).getText().equals(name)) {
					return i;
				}
			} else if (comp instanceof EzuceGroupChatParticipantMiniPanel) {
				if (((EzuceGroupChatParticipantMiniPanel) comp).getNickname()
						.equals(name)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Removes a user from the participant list based on their displayed name.
	 * 
	 * @param displayName
	 *            the users displayed name to remove.
	 */
	@Override
	public synchronized void removeUser(String displayName) {
		try {
			JLabel theLabel = null;
			for (int i = 0; i < getUsers().size(); i++) {
				theLabel = getUsers().get(i);
				if (theLabel.getText().equals(displayName)) {
					getUsers().remove(theLabel);
					getModel().removeElement(theLabel);
				}
			}

			for (int i = 0; i < getModel().size(); i++) {
				if (getModel().getElementAt(i) instanceof EzuceGroupChatParticipantMiniPanel) {
					EzuceGroupChatParticipantMiniPanel panel = (EzuceGroupChatParticipantMiniPanel) getModel()
							.getElementAt(i);
					if (panel.getNickname().equals(displayName)) {
						getUsers().remove(theLabel);
						getModel().removeElement(panel);
					}
				} else if (getModel().getElementAt(i) instanceof JLabel) {
					JLabel label = (JLabel) getModel().getElementAt(i);
					if (label.getText().equals(displayName)) {
						getUsers().remove(label);
						getModel().removeElement(label);
					}
				}

			}
			getInvitees().remove(displayName);
			nicknameMap.remove(displayName);
		} catch (Exception e) {
			Log.error(e);
		}
	}

	/**
	 * Adds a new user to the participant list.
	 * 
	 * @param userIcon
	 *            the icon to use initially.
	 * @param nickname
	 *            the users nickname.
	 */
	@Override
	public synchronized void addUser(Icon userIcon, String nickname) {
		try {
                        final JLabel user = new JLabel(nickname, Images.ensureSizeScaling((ImageIcon)userIcon, 40, 40, null), JLabel.LEFT);                        
                        user.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 0));

                        getUsers().add(user);                       

			// Sort users alpha.
			Collections.sort(getUsers(), getLabelComp());

			// Add to the correct position in the model.
			final int index = getUsers().indexOf(user);
			getModel().insertElementAt(user, index);
		} catch (Exception e) {
			Log.error(e);
		}
	}

	@Override
	protected boolean exists(String nickname) {
		for (int i = 0; i < getModel().getSize(); i++) {
			final JComponent panel = (JComponent) getModel().getElementAt(i);
			String displayName = null;
			if (panel instanceof EzuceGroupChatParticipantMiniPanel) {
				displayName = ((EzuceGroupChatParticipantMiniPanel) panel)
						.getNickname();
			} else if (panel instanceof JLabel) {
				displayName = ((JLabel) panel).getText();
			}
			if (displayName != null && displayName.equals(nickname)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void contactItemAdded(ContactItem item) {

	}

	@Override
	public void contactItemRemoved(ContactItem item) {

	}

	@Override
	public void contactGroupAdded(ContactGroup group) {

	}

	@Override
	public void contactGroupRemoved(ContactGroup group) {

	}

	@Override
	public void contactItemClicked(ContactItem item) {

	}

	@Override
	public void contactItemDoubleClicked(ContactItem item) {

	}

	public class EzuceParticipantRenderer extends JPanel implements
			ListCellRenderer {
		private static final long serialVersionUID = -7509947975798079141L;

		/**
		 * Construct Default JLabelIconRenderer.
		 */
		public EzuceParticipantRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JComponent panel = (JComponent) value;
			if (isSelected || cellHasFocus || index == list.getSelectedIndex()) {
				panel.setBackground(list.getSelectionBackground());
				panel.revalidate();
				panel.repaint();
			} else {
				panel.setBackground(list.getBackground());
				panel.revalidate();
				panel.repaint();
			}

			return panel;
		}
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void presenceChanged(Presence prsnc) {
		String bareJid = StringUtils.parseBareAddress(prsnc.getFrom());
		for (int i = 0; i < getModel().getSize(); i++) {
			EzuceGroupChatParticipantMiniPanel participantPanel = (EzuceGroupChatParticipantMiniPanel) getModel().get(i);
			String jid = participantPanel.getUserJid();
			if (jid != null && StringUtils.parseBareAddress(jid).equals(bareJid)) {
				if (Utils.isVCardUpdated(prsnc)) {
					AsyncLoader.getInstance().execute(prsnc.getFrom(), participantPanel);
				}
				repaint();
				revalidate();
				break;
			}
		}
	}
}
