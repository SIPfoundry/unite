package org.ezuce.common.ui.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;

import org.ezuce.common.EzuceConferenceServices;
import org.ezuce.common.EzuceConferenceUtils;
import org.ezuce.common.resource.Config;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.EzuceContactInfoWindow;
import org.ezuce.common.ui.actions.task.AddToRosterTask;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.UserMiniPanelCommonInterface;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.calendar.DateUtils;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.bookmark.BookmarkedConference;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.ChatNotFoundException;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.component.BackgroundPanel;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.ui.VCardPanel;
import org.jivesoftware.spark.ui.conferences.ConferenceServices;
import org.jivesoftware.spark.ui.conferences.ConferenceUtils;
import org.jivesoftware.spark.ui.rooms.GroupChatRoom;
import org.jivesoftware.spark.ui.status.CustomMessages;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.SwingWorker;
import org.jivesoftware.spark.util.TaskEngine;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.plugin.jabber.VersionViewer;
import org.jivesoftware.sparkimpl.plugin.transcripts.ChatTranscript;
import org.jivesoftware.sparkimpl.plugin.transcripts.ChatTranscripts;
import org.jivesoftware.sparkimpl.plugin.transcripts.HistoryMessage;
import org.jivesoftware.sparkimpl.profile.VCardManager;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class ContactActions {
	private final UserMiniPanelCommonInterface targetUmpg;
	private final Component parent;

	private final String timeFormat = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT)).toPattern();
	private final String dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL)).toPattern();
	protected final SimpleDateFormat notificationDateFormatter = new SimpleDateFormat(dateFormat);
	protected final SimpleDateFormat messageDateFormatter = new SimpleDateFormat(timeFormat);

	public ContactActions(UserMiniPanelCommonInterface targetUmpg, Component parent) {
		this.targetUmpg = targetUmpg;
		this.parent = parent;
	}

	@Action(block = Task.BlockingScope.COMPONENT)
	public void viewProfileAction() {
		final VCardManager vcardSupport = SparkManager.getVCardManager();
		final String jid = targetUmpg.getJID();
		vcardSupport.viewProfile(jid, SparkManager.getWorkspace());
	}

	@Action
	public void renameAction() {
		if (targetUmpg != null && targetUmpg.getJID().length() > 0) {
			if (!(targetUmpg.getContact() instanceof ContactItemWrapper)) {
				return;
			}
			final ContactItem activeItem = ((ContactItemWrapper) (targetUmpg.getContact())).getContactItem();
			final String oldAlias = activeItem.getAlias();
			String newAlias = JOptionPane.showInputDialog(SparkManager.getMainWindow(),
					Res.getString("label.rename.to") + ":", oldAlias);

			// if user pressed 'cancel', output will be null.
			// if user removed alias, output will be an empty String.
			if (newAlias != null) {
				if (!ModelUtil.hasLength(newAlias)) {
					newAlias = null; // allows you to remove an alias.
				}

				final String address = activeItem.getJID();
				final ContactGroup contactGroup = ((ContactItemWrapper) (targetUmpg.getContact())).getContactGroup();
				final ContactItem contactItem = contactGroup.getContactItemByDisplayName(activeItem.getDisplayName());
				contactItem.setAlias(newAlias);

				final Roster roster = SparkManager.getConnection().getRoster();
				final RosterEntry entry = roster.getEntry(address);
				entry.setName(newAlias);

				final Iterator<ContactGroup> contactGroups = SparkManager.getContactList().getContactGroups()
						.iterator();
				final String user = StringUtils.parseBareAddress(address);
				while (contactGroups.hasNext()) {
					final ContactGroup cg = contactGroups.next();
					final ContactItem ci = cg.getContactItemByJID(user);
					if (ci != null) {
						ci.setAlias(newAlias);
					}
				}
			}
		}
	}

	@Action(block = Task.BlockingScope.COMPONENT)
	public void removeFromRosterAction() {
		if (!(targetUmpg.getContact() instanceof ContactItemWrapper)) {
			return;
		}

		final ContactItemWrapper ciw = ((ContactItemWrapper) (targetUmpg.getContact()));
		final ContactItem item = ciw.getContactItem();
		final Roster roster = SparkManager.getConnection().getRoster();
		final RosterEntry entry = roster.getEntry(item.getJID());
		if (entry != null) {
			try {
				roster.removeEntry(entry);
			} catch (final XMPPException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	@Action(block = Task.BlockingScope.COMPONENT)
	public void sendFileAction() {
		final ContactItem item = SparkManager.getContactList().getContactItemByJID(targetUmpg.getJID());
		SparkManager.getTransferManager().sendFileTo(item);
	}

	@Action
	public void sendEmailAction(ActionEvent ae) {
		Desktop desktop = null;
		// Before more Desktop API is used, first check
		// whether the API is supported by this particular
		// virtual machine (VM) on this particular host.
		boolean emailingSupported = true;
		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		} else {
			emailingSupported = false;
		}
		if (emailingSupported) {
			emailingSupported = desktop.isSupported(Desktop.Action.MAIL);
		}

		if (!emailingSupported) {
			JOptionPane.showMessageDialog(parent, "Could not open default mail client.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
                
                String mailTo="";
                
                if (ae.getActionCommand()!=null && !ae.getActionCommand().isEmpty())
                {
                    mailTo=ae.getActionCommand();
                } 
                else
                {
                    final VCard userVCard=SparkManager.getVCardManager().getVCard(targetUmpg.getJID());
                    String emailHome = userVCard.getEmailHome();
                    String emailWork = userVCard.getEmailWork();
                    mailTo = emailWork == null ? emailHome :emailWork;
                }
				
		URI uriMailTo = null;
		try {
			if (mailTo !=null && mailTo.length() > 0) {
				uriMailTo = new URI("mailto", mailTo, null);
				desktop.mail(uriMailTo);
			} else {
				desktop.mail();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		} catch (URISyntaxException use) {
			use.printStackTrace(System.err);
		}
	}

	@Action
	public void alertUserAvailableAction() {
		// For now, this option is not available on a per-user basis, but for
		// anyone who comes online.
		SettingsManager.getLocalPreferences().setOnlineNotifications(true);
	}

	@Action
	public void inviteIntoBridgeAction(ActionEvent ae) {
		String toUser = targetUmpg.getContact().getNumber();															
		String conferenceName = ae.getActionCommand();
		try {
			if (toUser != null) {
				RestManager.getInstance().inviteToBridge(toUser, conferenceName);
			} else {
				toUser = targetUmpg.getContact().getImId();
				RestManager.getInstance().inviteToBridgeIm(toUser, conferenceName);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(parent, "Failed sending the invitation ! Please, try again.",
					"A problem occurred !", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Action
	public void startChatAction() {
		if (targetUmpg.getJID() != null && targetUmpg.getJID().length() > 0) {
			SparkManager.getChatManager().activateChat(targetUmpg.getJID(), targetUmpg.getUserDisplayName());
		}
	}
	
	public static void startDefaultBookmarkedConf(ContactItem contactItem) {
        //1. Get list of chat rooms
        //2. Get the JID of the implicit bookmarked room
        //3. If the JID is configured on the client, get a reference of that room by this JID
        //4. If the JID is NOT set, get a reference of the first bookmarked chat room.
        Collection<String> jidsToInvite=new ArrayList<String>();
        if (contactItem != null) {
     	   jidsToInvite.add(contactItem.getJID());
        }
        try {            
            LocalPreferences prefs = SettingsManager.getLocalPreferences();
            BookmarkedConference bookmarkedConference = EzuceConferenceServices.getDefaultBookmarkToUse();
            if (!prefs.isUseAdHocRoom() && bookmarkedConference != null) {
              startConferenceInRoom(bookmarkedConference.getName(), bookmarkedConference, jidsToInvite);
              return;
            }
            //If there was no bookmark configured - an ad-hoc room is created:
            String userName = StringUtils.parseName(SparkManager.getSessionManager().getJID());
            final String roomName = userName + "_" + StringUtils.randomString(3);
            
            String serviceName = ConferenceServices.getDefaultServiceName();
            final String messageText = Res.getString("message.please.join.in.conference");
            if (ModelUtil.hasLength(serviceName)) {
            	ConferenceUtils.createPrivateConference(serviceName, messageText, roomName, jidsToInvite);
            }
        }
        catch(XMPPException xmppe) {
            Log.error(xmppe);
        }
               
	}

    @Action
	public void startGroupChatAction()
        {
            // MultiUserChat. [...]
            if (!(targetUmpg.getContact() instanceof ContactItemWrapper)) {
                    return;
            }
            final ContactItemWrapper ciw = (ContactItemWrapper) (targetUmpg.getContact());
            startDefaultBookmarkedConf(ciw.getContactItem());

	}
    
    private Task<Object, Void> createCallTask(ActionEvent ae, boolean ignoreNumbersPatern) {
		Task<Object, Void> task = null;
		MakeCallTask mct = new MakeCallTask(org.jdesktop.application.Application.getInstance(), ignoreNumbersPatern);
		mct.setCalee(ae.getActionCommand());
		task = mct;
		return task;
    }

	@Action(block = Task.BlockingScope.COMPONENT)
	public Task<Object, Void> call(ActionEvent ae) {
		return createCallTask(ae, false);
	}
	
	@Action(block = Task.BlockingScope.COMPONENT)
	public Task<Object, Void> calloffice(ActionEvent ae) {
		return createCallTask(ae, true);
	}	

	@Action
	public Task<RosterEntry, Void> addToRosterAction(ActionEvent ae) {
		String groupName = ae.getActionCommand();
		final ContactGroup contactGroup = this.getContactGroup(groupName);
		if (contactGroup == null) {
			return null;
		}
		AddToRosterTask atrt = new AddToRosterTask(org.jdesktop.application.Application.getInstance(),
				targetUmpg.getJID(),
				targetUmpg.getUserDisplayName(), groupName, contactGroup);

		return atrt;
	}

	@Action
	public void transferToAction(ActionEvent ae) {
		// ae must have an action_command configured, where the active call to be transferred is set.
		// the current user, who initiate the call;
		String fromUser = Config.getInstance().getSipUserId();
		// the user to which the active call was initially made;
		String initialToUser = ae.getActionCommand();
		// the user to which the active call must transferred;
		String toUser = targetUmpg.getContact().getNumber();
		toUser = (toUser == null) ? targetUmpg.getContact().getImId() : toUser;
		try {
			RestManager.getInstance().transferCall(fromUser, initialToUser, toUser);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(parent, "The call transfer failed ! Please, try again.",
					"A problem occurred !", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Action
	public void locationAction() {
		CustomMessages.editCustomMessages();
	}

	private ContactGroup getContactGroup(String groupName) {
		final ContactList contactList = SparkManager.getWorkspace().getContactList();
		return contactList.getContactGroup(groupName);
	}

	public void showHistory(final String jid) {
		SwingWorker transcriptLoader = new SwingWorker() {
			@Override
			public Object construct() {
				String bareJID = StringUtils.parseBareAddress(jid);
				return ChatTranscripts.getChatTranscript(bareJID);
			}

			@Override
			public void finished() {
				final JPanel mainPanel = new BackgroundPanel();

				mainPanel.setLayout(new BorderLayout());
				// add search text input
				final JPanel topPanel = new BackgroundPanel();
				topPanel.setLayout(new GridBagLayout());

				final VCardPanel vacardPanel = new VCardPanel(jid);
				final JTextField searchField = new JTextField(25);
				searchField.setText(Res.getString("message.search.for.history"));
				searchField.setToolTipText(Res.getString("message.search.for.history"));
				searchField.setForeground((Color) UIManager.get("TextField.lightforeground"));

				topPanel.add(vacardPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(1, 5, 1, 1), 0, 0));
				topPanel.add(searchField, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
						GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(1, 1, 6, 1), 0, 0));

				mainPanel.add(topPanel, BorderLayout.NORTH);

				final JEditorPane window = new JEditorPane();
				window.setEditorKit(new HTMLEditorKit());
				window.setBackground(Color.white);
				final JScrollPane pane = new JScrollPane(window);
				pane.getVerticalScrollBar().setBlockIncrement(50);
				pane.getVerticalScrollBar().setUnitIncrement(20);

				mainPanel.add(pane, BorderLayout.CENTER);

				final JFrame frame = new JFrame(Res.getString("title.history.for", jid));
				frame.setIconImage(SparkRes.getImageIcon(SparkRes.HISTORY_16x16).getImage());
				frame.getContentPane().setLayout(new BorderLayout());

				frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
				frame.pack();
				frame.setSize(600, 400);
				window.setCaretPosition(0);
				window.requestFocus();
				GraphicUtils.centerWindowOnScreen(frame);
				frame.setVisible(true);

				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						window.setText("");
					}
				});

				window.setEditable(false);

				final StringBuilder builder = new StringBuilder();
				builder.append("<html><body><table cellpadding=0 cellspacing=0>");

				final TimerTask transcriptTask = new TimerTask() {
					@Override
					public void run() {
						final ChatTranscript transcript = (ChatTranscript) get();
						final List<HistoryMessage> list = transcript.getMessage(Res.getString(
								"message.search.for.history").equals(searchField.getText()) ? null : searchField
								.getText());
						final String personalNickname = SparkManager.getUserManager().getNickname();
						Date lastPost = null;
						String lastPerson = null;
						boolean initialized = false;
						for (HistoryMessage message : list) {
							String color = "blue";

							String from = message.getFrom();
							String nickname = SparkManager.getUserManager().getUserNicknameFromJID(message.getFrom());
							String body = org.jivesoftware.spark.util.StringUtils.escapeHTMLTags(message.getBody());
							if (nickname.equals(message.getFrom())) {
								String otherJID = StringUtils.parseBareAddress(message.getFrom());
								String myJID = SparkManager.getSessionManager().getBareAddress();

								if (otherJID.equals(myJID)) {
									nickname = personalNickname;
								} else {
									nickname = StringUtils.parseName(nickname);
								}
							}

							if (!StringUtils.parseBareAddress(from).equals(
									SparkManager.getSessionManager().getBareAddress())) {
								color = "red";
							}

							long lastPostTime = lastPost != null ? lastPost.getTime() : 0;
							int diff = DateUtils.getDaysDiff(lastPostTime, message.getDate().getTime());
							if (diff != 0) {
								if (initialized) {
									builder.append("<tr><td><br></td></tr>");
								}

								builder.append("<tr><td colspan=2><font size=4 color=gray><b><u>")
										.append(notificationDateFormatter.format(message.getDate()))
										.append("</u></b></font></td></tr>");
								lastPerson = null;
								initialized = true;
							}

							String value = "[" + messageDateFormatter.format(message.getDate()) + "]&nbsp;&nbsp;  ";

							boolean newInsertions = lastPerson == null || !lastPerson.equals(nickname);
							if (newInsertions) {
								builder.append("<tr valign=top><td colspan=2 nowrap>");
								builder.append("<font size=4 color='").append(color).append("'><b>");
								builder.append(nickname);
								builder.append("</b></font>");
								builder.append("</td></tr>");
							}

							builder.append("<tr valign=top><td align=left nowrap>");
							builder.append(value);
							builder.append("</td><td align=left>");
							builder.append(body);

							builder.append("</td></tr>");

							lastPost = message.getDate();
							lastPerson = nickname;
						}
						builder.append("</table></body></html>");

						// Handle no history
						if (transcript.getMessages().isEmpty()) {
							builder.append("<b>").append(Res.getString("message.no.history.found")).append("</b>");
						}

						window.setText(builder.toString());
						builder.replace(0, builder.length(), "");
					}
				};

				searchField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyChar() == KeyEvent.VK_ENTER) {
							TaskEngine.getInstance().schedule(transcriptTask, 10);
							searchField.requestFocus();
						}
					}
				});
				searchField.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						searchField.setText("");
						searchField.setForeground((Color) UIManager.get("TextField.foreground"));
					}

					@Override
					public void focusLost(FocusEvent e) {
						searchField.setForeground((Color) UIManager.get("TextField.lightforeground"));
						searchField.setText(Res.getString("message.search.for.history"));
					}
				});

				TaskEngine.getInstance().schedule(transcriptTask, 10);
			}
		};
		transcriptLoader.start();
	}

    public static void startConferenceInRoom(String roomName, final BookmarkedConference selectedBookmarkedConf, Collection<String> jids)
    {
        final String roomTitle = roomName;

        // Add all rooms the user is in to list.
        ChatManager chatManager = SparkManager.getChatManager();
        for (ChatRoom chatRoom : chatManager.getChatContainer().getChatRooms()) {
            if (chatRoom instanceof GroupChatRoom) {
                GroupChatRoom groupRoom = (GroupChatRoom) chatRoom;
                if (groupRoom.getRoomname().equals(roomTitle)) {
                    roomName = groupRoom.getMultiUserChat().getRoom();
                    break;
                }
            }
        }

        final String messageText =  Res.getString("message.please.join.in.conference");

        GroupChatRoom chatRoom;
        try {
            chatRoom = SparkManager.getChatManager().getGroupChat(roomName);
        }
        catch (ChatNotFoundException e1)
        {
            final String serviceName = ConferenceServices.getDefaultServiceName();
            final List<String> jidList=new ArrayList<String>();
            jidList.addAll(jids);

            SwingWorker worker = new SwingWorker() {
                @Override
                public Object construct() {
                    try {                    	
                        Thread.sleep(15);                        
                    }
                    catch (InterruptedException e2) {
                        Log.error(e2);
                    }
                    return "ok";
                }

                @Override
                public void finished() {
                    try {                    	
                            if (selectedBookmarkedConf == null) {
                                    ConferenceUtils.createPrivateConference(serviceName, messageText, roomTitle, jidList);
                            } else {
                                    ConferenceUtils.joinConferenceOnSeperateThread(selectedBookmarkedConf.getName(),
                                                    selectedBookmarkedConf.getJid(), selectedBookmarkedConf.getPassword(), messageText, jidList);
                            }
                    }
                    catch (XMPPException e2) {
                        Log.error(e2);
                    }
                }
            };

            worker.start();
            return;
        }

        final int no = jids != null ? jids.size() : 0;
        Object[] values=jids.toArray();
        for (int i = 0; i < no; i++) {
            String jid = (String)values[i];
            chatRoom.getMultiUserChat().invite(jid, Res.getString("message.please.join.in.conference"));
            String nickname = SparkManager.getUserManager().getUserNicknameFromJID(jid);
            chatRoom.getTranscriptWindow().insertNotificationMessage("Invited " + nickname, ChatManager.NOTIFICATION_COLOR);
        }
    }

}
