package org.ezuce.im.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.ComponentFactory;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.event.EventForwarder;
import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.CounterLabel;
import org.ezuce.common.ui.EzuceScrollBarUI;
import org.ezuce.common.ui.MugshotPanel;
import org.ezuce.common.ui.panels.EzuceContactProfile;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.commons.ui.custom.message.EzuceCustomMessageWindow;
import org.ezuce.commons.ui.location.Location;
import org.ezuce.commons.ui.location.LocationManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.ui.status.CustomMessages;
import org.jivesoftware.spark.ui.status.CustomStatusItem;
import org.jivesoftware.spark.ui.status.StatusBar;
import org.jivesoftware.spark.ui.status.StatusItem;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.spark.util.SwingTimerTask;
import org.jivesoftware.spark.util.SwingWorker;
import org.jivesoftware.spark.util.TaskEngine;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.plugin.privacy.PrivacyManager;
import org.jivesoftware.sparkimpl.plugin.privacy.list.SparkPrivacyList;
import org.jivesoftware.sparkimpl.profile.VCardManager;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceStatusBar extends StatusBar implements VCardLoaderCallback,
		Observer {

	private static final long serialVersionUID = 7030306300928717046L;

	private CounterLabel voiceMessageLabel;
	private CounterLabel missedCallsLabel;
	protected MugshotPanel mugshot;
	private JLabel jobLabel;
	protected JLabel locationLabel = ComponentFactory.createBottomLabel();
	protected JLabel locationNameLabel = ComponentFactory.createBottomLabel();
	protected ClickableZone clickablePanel = new ClickableZone();
	private final ResourceMap resourceMap = Application.getInstance()
			.getContext().getResourceMap(getClass());
	private Presence previousPresence = new Presence(Type.available);
	private EzuceCustomMessageWindow mCustomMessageWindow;
	private JPanel jPanelContactProfileContainer;
	private JScrollPane jScrollPane;
	private EzuceContactProfile ezuceContactProfile;
	public static final String CONTACT_PROFILE_PANE = "CONTACT_PROFILE_PANE";
	private SparkTabbedPane stp;
	private boolean contactProfileShowing = false;

	public EzuceStatusBar() {
		super(false);

		mCustomMessageWindow = new EzuceCustomMessageWindow();

		getStatusList().clear();
		for (Presence presence : EzucePresenceManager.getPresences()) {
			Icon icon = EzucePresenceManager.getIcon(presence);
			StatusItem item = new StatusItem(presence, icon);
			getStatusList().add(item);
		}

		removeAll();
		initComponents();
		updatePresence();
		LocationManager.addObserver(this);

		jPanelContactProfileContainer = new JPanel();
		jPanelContactProfileContainer.setLayout(new BorderLayout());
		jPanelContactProfileContainer.setBorder(null);

		jScrollPane = new JScrollPane();
		jScrollPane.setBackground(new Color(255, 255, 255));
		jScrollPane.setBorder(null);
		jScrollPane.setName("jScrollPane"); // NOI18N
		jScrollPane.getVerticalScrollBar().setVisible(false);
		// jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.getVerticalScrollBar().setSize(0, 0);
		jScrollPane.getVerticalScrollBar().setPreferredSize(
				new Dimension(10, 5));
		jScrollPane.getVerticalScrollBar().setUI(new EzuceScrollBarUI());
		jScrollPane.setViewportView(jPanelContactProfileContainer);

		setEzuceContactProfile();

		// Show profile on double click of image label
		mugshot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 1) {
					// final VCardManager vcardManager =
					// SparkManager.getVCardManager();
					// final VCardEditor editor = new VCardEditor();
					// editor.editProfile(vcardManager.getVCard(),
					// SparkManager.getWorkspace());

					showContactProfile(true, false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mugshot.setCursor(GraphicUtils.HAND_CURSOR);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mugshot.setCursor(GraphicUtils.DEFAULT_CURSOR);
			}
		});
		clickablePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				clickablePanel.setCursor(GraphicUtils.HAND_CURSOR);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				clickablePanel.setCursor(GraphicUtils.DEFAULT_CURSOR);
			}
		});
		AsyncLoader.getInstance().execute(
				SparkManager.getSessionManager().getJID(), this);
		SparkManager.getConnection().addConnectionListener(
				reconectionListener());

		final JMenu communicatorMenu = SparkManager.getMainWindow()
				.getJMenuBar().getMenu(0);
		final Component[] menus = communicatorMenu.getMenuComponents();
		for (Component m : menus) {
			if (m instanceof JMenuItem) {
				final JMenuItem editProfileMenu = (JMenuItem) m;
				if (editProfileMenu.getText().equals(
						Res.getString("menuitem.edit.my.profile"))) {
					ActionListener[] listeners = editProfileMenu
							.getActionListeners();
					for (ActionListener al : listeners) {
						editProfileMenu.removeActionListener(al);
					}
					editProfileMenu.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							showContactProfile(true, true);
						}
					});
					break;
				}
			}
		}
	}

	public void showContactProfile(final boolean show, final boolean editMode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (show && !contactProfileShowing) {
					final Workspace w = Workspace.getInstance();
					stp = w.getWorkspacePane();
					GridBagLayout gbl = (GridBagLayout) w.getLayout();
					GridBagConstraints gbc = gbl.getConstraints(stp);
					if (gbc != null) {
						w.remove(stp);
						w.add(jScrollPane, gbc);
						w.revalidate();
						w.repaint();
						contactProfileShowing = true;
					}
					if (editMode) {
						ezuceContactProfile.getBtnEnterEditMode().doClick();
					}
				} else {
					final Workspace w = Workspace.getInstance();
					GridBagLayout gbl = (GridBagLayout) w.getLayout();
					GridBagConstraints gbc = gbl.getConstraints(jScrollPane);
					w.remove(jScrollPane);
					w.add(stp, gbc);
					w.revalidate();
					w.repaint();
					contactProfileShowing = false;
				}
			}
		});
	}

	public EzuceContactProfile getEzuceContactProfile() {
		return this.ezuceContactProfile;
	}

	public void setEzuceContactProfile() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jPanelContactProfileContainer.removeAll();
				final EzuceContactProfile ecp= new EzuceContactProfile(true);
		    	ezuceContactProfile=ecp;
		    	
		    	UserMiniPanelGloss umpg=new UserMiniPanelGloss();
		    	final VCard currentVCard=SparkManager.getVCardManager().getVCard();
		    	String host = SparkManager.getSessionManager().getServerAddress();
		    	ContactItemWrapper ciw = new ContactItemWrapper(currentVCard.getJabberId(), currentVCard.getNickName(), currentVCard.getJabberId()+"@"+host);
		    	ciw.setNumber(currentVCard.getJabberId()+"@"+host);		    
		    	umpg.setContact(ciw);		    	
		    	
		    	ezuceContactProfile.setUserAvatarPanel(umpg);
		    	ezuceContactProfile.hideAvatarPanel();
		    	ezuceContactProfile.getBtnCloseProfile().addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						showContactProfile(false, false);
					}
				});
		    	jPanelContactProfileContainer.add(ezuceContactProfile, BorderLayout.CENTER);
		    	jPanelContactProfileContainer.revalidate();
		    	jPanelContactProfileContainer.repaint();
			}
		});

	}

	private void initComponents() {
		setBackground(new Color(250, 250, 251));
		setOpaque(true);
		mugshot = new MugshotPanel(true);

		Dimension mugshotSize = new Dimension(40, 45);
		mugshot.setMinimumSize(mugshotSize);
		mugshot.setPreferredSize(mugshotSize);
		mugshot.setMaximumSize(mugshotSize);

		jobLabel = ComponentFactory.createPositionLabel();
		voiceMessageLabel = CounterLabel.getMissedVoicemailsLabel();
		missedCallsLabel = CounterLabel.getMissedCallsLabel();

		jobLabel.setForeground(new Color(116, 116, 116));
		jobLabel.setText(resourceMap.getString("job.text"));
		locationLabel.setForeground(new Color(116, 116, 116));
		Dimension currentDim = locationLabel.getSize();
		locationNameLabel.setForeground(locationLabel.getForeground());

		final int width = 180;
		final int height = 18;

		locationLabel.setMaximumSize(new Dimension(width + 400,
				currentDim.height));

		clickablePanel.setSize(new Dimension(width, height));
		// clickablePanel.setPreferredSize(new Dimension(width, height));
		// clickablePanel.setMinimumSize(new Dimension(width, height));
		// clickablePanel.setMaximumSize(new Dimension(width + 400, height));

		final VCardManager vcardManager = SparkManager.getVCardManager();
		final VCard vcard = vcardManager.getVCard();
		setJobTitle(vcard.getField("TITLE"));
		final ImageIcon voiceMessageIcon = new ImageIcon(getClass()
				.getClassLoader().getResource(
						"resources/images/voice_message_off.png"));
		voiceMessageLabel.setIcon(voiceMessageIcon);
		voiceMessageLabel.setName("voiceMessages");

		final ImageIcon missedCallsIcon = new ImageIcon(getClass()
				.getClassLoader().getResource(
						"resources/images/missed_calls_off.png"));
		missedCallsLabel.setIcon(missedCallsIcon);
		missedCallsLabel.setName("missedCalls");

		if (RestManager.getInstance().isLoggedIn()) {
			voiceMessageLabel.addMouseListener(EventForwarder.getInstance());
			voiceMessageLabel.setToolTipText(resourceMap
					.getString("voice.messages"));
			missedCallsLabel.addMouseListener(EventForwarder.getInstance());
			missedCallsLabel.setToolTipText(resourceMap
					.getString("missed.calls"));
		}
		initLayout();
	}

	private void initLayout() {
		setLayout(new GridBagLayout());

		// center
		JPanel infoPanel = new JPanel(new GridBagLayout());
		Dimension infoPanelDim = new Dimension(300, 45);
		// infoPanel.setPreferredSize(infoPanelDim);
		infoPanel.setMinimumSize(infoPanelDim);
		infoPanel.setMaximumSize(infoPanelDim);
		infoPanel.setOpaque(false);
		infoPanel.add(clickablePanel, new GridBagConstraints(0, 0, 2, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 3, 0, 3), 0, 0));
		infoPanel.add(locationLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1,
						3, 0, 3), 0, 0));
		infoPanel.add(jobLabel, new GridBagConstraints(0, 2, 2, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1,
						3, 0, 3), 0, 0));
		infoPanel.add(locationNameLabel, new GridBagConstraints(0, 3, 1, 1, 1,
				0.1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(1, 3, 0, 3), 0, 0));

		// right
		JPanel callsPanel = new JPanel(new GridBagLayout());
		callsPanel.setOpaque(false);
		callsPanel.add(voiceMessageLabel, new GridBagConstraints(0, 0, 1, 1, 0,
				0, GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(3, 3, 0, 3), 0, 0));
		callsPanel.add(missedCallsLabel, new GridBagConstraints(0, 1, 1, 1, 0,
				0, GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(3, 3, 3, 3), 0, 0));

		add(mugshot, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(10, 5, 5, 0), 0, 0));
		add(infoPanel, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		add(callsPanel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				new Insets(10, 0, 5, 5), 0, 0));
	}

	@Override
	protected void updateVCardInformation(final VCard vCard) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ImageIcon avatar = null;
				if (vCard.getError() == null) {
					String nickname = vCard.getNickName();
					String alias = Utils.getAlias(vCard);
					if (alias != null) {
						setNickname(alias);
					} else {
						nickname = SparkManager.getSessionManager()
								.getUsername();
						setNickname(nickname);
					}
					avatar = Utils.retrieveAvatar(vCard);
				} else {
					final String nickname = SparkManager.getSessionManager()
							.getUsername();
					setNickname(nickname);
					avatar = Utils.getDefaultAvatar();
				}
				mugshot.setMugshot(avatar);
			}
		});
	}

	@Override
	protected void updatePresence() {
		if (mugshot == null)
			return;

		mugshot.setPresence(getCurrentPresence());
		setStatus(getCurrentPresence().getStatus());
		saveCurrentPresence();
		setLocationName(LocationManager.getCurrentLocationNameOrEmpty());
	}

	@Override
	public void setNickname(String nickname) {
		clickablePanel.setText(nickname);
	}

	public void setLocation(String location) {
		locationLabel.setText(location);
	}

	public void setLocationName(String locationName) {
		locationNameLabel.setText(locationName);
	}

	public void setJobTitle(String jobTitle) {
		jobLabel.setText(jobTitle);
	}

	public void setAvatar(ImageIcon icon) {
		mugshot.setMugshot(icon);
	}

	@Override
	protected JPanel getStatusPanel() {
		return clickablePanel;
	}

	@Override
	public void setStatus(String status) {
		clickablePanel.setLocation(status);
	}

	private class ClickableZone extends JPanel {
		private static final long serialVersionUID = -7039108151422827251L;

		private final JLabel arrowLabel;
		private final JLabel text = ComponentFactory.createNameLabel();

		public ClickableZone() {
			setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
			setOpaque(false);
			arrowLabel = new JLabel() {
				private static final long serialVersionUID = -5543430256354146493L;

				@Override
				public void paintComponent(Graphics g) {
					final Graphics2D g2D = (Graphics2D) g;
					g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY);
					setForeground(Color.BLACK);
					final Polygon arrow = new Polygon();
					final int mid = getHeight() / 2;
					arrow.addPoint(mid - 3, mid - 2);
					arrow.addPoint(mid + 3, mid - 2);
					arrow.addPoint(mid, mid + 2);
					g.fillPolygon(arrow);

				}
			};
			arrowLabel.setMinimumSize(new Dimension(8, 8));
			arrowLabel.setPreferredSize(new Dimension(8, 8));
			arrowLabel.setSize(new Dimension(8, 8));
			add(arrowLabel);
			add(text);
			locationLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					showPopup(e);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					setCursor(GraphicUtils.HAND_CURSOR);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setCursor(GraphicUtils.DEFAULT_CURSOR);
				}
			});
		}

		public void setLocation(String location) {
			int length = location.length();
			String visualStatus = location;
			if (length > 40) {
				visualStatus = location.substring(0, 37) + "...";
			}

			locationLabel.setText(visualStatus);
			locationLabel.setToolTipText(location);
		}

		public void setText(String text) {
			this.text.setText(text);
		}
	}

	@Override
	public void vcardLoaded(VCard vCard) {
		// refresh personal vcard if previous attempt returned error
		if (vCard.getError() != null) {
			updateVCardInformation(vCard);
		}
	}

	/**
	 * This listener restore previous presence after reconnection.
	 * */
	private ConnectionListener reconectionListener() {
		return new ConnectionListener() {

			@Override
			public void reconnectionSuccessful() {
				if (previousPresence == null)
					previousPresence = EzucePresenceManager
							.getAvailablePresence();

				if (!EzucePresenceManager.isInvisible(previousPresence)) {
					SparkManager.getSessionManager().changePresence(
							previousPresence);
				} else {
					// reactive invisible list
					TimerTask task = new SwingTimerTask() {
						public void doRun() {
							SparkManager.getSessionManager()
									.changePresence(
											EzucePresenceManager
													.getAvailablePresence());
							SparkManager.getSessionManager().changePresence(
									EzucePresenceManager
											.getUnavailablePresence());
							PrivacyManager.getInstance().goToInvisible();
						}
					};
					TaskEngine.getInstance().schedule(task, 1000);
				}
			}

			@Override
			public void reconnectionFailed(Exception arg0) {
			}

			@Override
			public void reconnectingIn(int arg0) {
			}

			@Override
			public void connectionClosedOnError(Exception arg0) {
			}

			@Override
			public void connectionClosed() {
			}
		};
	}

	/**
	 * Create a copy of current presence to restore it in case of reconnection.
	 * */
	private void saveCurrentPresence() {
		if (StringUtils.isEmpty(getCurrentPresence().getStatus()))
			return;
		previousPresence.setType(getCurrentPresence().getType());
		previousPresence.setStatus(getCurrentPresence().getStatus());
		try {
			previousPresence.setPriority(getCurrentPresence().getPriority());
		} catch (Exception e) {
			e.printStackTrace();
		}
		previousPresence.setMode(getCurrentPresence().getMode());
	}

	public void showPopup(MouseEvent e) {
		final JPopupMenu popup = new JPopupMenu();

		List<CustomStatusItem> custom = CustomMessages.load();
		if (custom == null) {
			custom = new ArrayList<CustomStatusItem>();
		}

		// Sort Custom Messages
		Collections.sort(custom, new Comparator<CustomStatusItem>() {
			public int compare(final CustomStatusItem a,
					final CustomStatusItem b) {
				return (a.getStatus().compareToIgnoreCase(b.getStatus()));
			}
		});

		// Build menu from StatusList
		for (final StatusItem statusItem : getStatusList()) {
			final Action statusAction = new AbstractAction() {
				private static final long serialVersionUID = -192865863435381702L;

				public void actionPerformed(ActionEvent actionEvent) {
					final String text = statusItem.getText();
					final StatusItem si = getStatusItem(text);
					if (si == null) {
						// Custom status
						Log.error("Unable to find status item for status - "
								+ text);
						return;
					}

					SwingWorker worker = new SwingWorker() {
						public Object construct() {
							return changePresence(si.getPresence());
						}

						public void finished() {
							setStatus((String) getValue());
						}
					};
					worker.start();
				}
			};

			statusAction.putValue(Action.NAME, statusItem.getText());
			statusAction.putValue(Action.SMALL_ICON, statusItem.getIcon());

			// Has Children
			boolean hasChildren = false;
			for (Object aCustom : custom) {
				final CustomStatusItem cItem = (CustomStatusItem) aCustom;
				String type = cItem.getType();
				if (type.equals(statusItem.getText())) {
					hasChildren = true;
				}
			}

			if (!hasChildren) {
				// Add as Menu Item
				popup.add(statusAction);
			} else {

				final JMenu mainStatusItem = new JMenu(statusAction);

				popup.add(mainStatusItem);

				// Add Custom Messages
				for (Object aCustom : custom) {
					final CustomStatusItem customItem = (CustomStatusItem) aCustom;
					String type = customItem.getType();
					final String customStatus = customItem.getType() + " - "
							+ customItem.getStatus();
					if (type.equals(statusItem.getText())) {
						// Add Child Menu
						Action action = new AbstractAction() {
							private static final long serialVersionUID = -1264239704492879742L;

							public void actionPerformed(ActionEvent actionEvent) {
								final String text = mainStatusItem.getText();
								final StatusItem si = getStatusItem(text);
								if (si == null) {
									// Custom status
									Log.error("Unable to find status item for status - "
											+ text);
									return;
								}

								SwingWorker worker = new SwingWorker() {
									public Object construct() {
										Presence presence = EzucePresenceManager
												.copy(si.getPresence());
										presence.setStatus(customStatus);
										presence.setPriority(customItem
												.getPriority());
										return changePresence(presence);
									}

									public void finished() {
										setStatus((String) getValue());
									}
								};
								worker.start();
							}
						};
						action.putValue(Action.NAME, customItem.getStatus());
						action.putValue(Action.SMALL_ICON, statusItem.getIcon());
						mainStatusItem.add(action);
					}
				}

				// If menu has children, allow it to still be clickable.
				mainStatusItem.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent mouseEvent) {
						statusAction.actionPerformed(null);
						popup.setVisible(false);
					}
				});
			}
		}

		// SPARK-1521. Add privacy menu if Privacy Manager is active and have
		// any visible lists
		final PrivacyManager pmanager = PrivacyManager.getInstance();
		if (pmanager.isPrivacyActive() && pmanager.getPrivacyLists().size() > 0) {

			JMenu privMenu = new JMenu(
					Res.getString("privacy.status.menu.entry"));
			privMenu.setIcon(SparkRes.getImageIcon("PRIVACY_ICON_SMALL"));

			for (SparkPrivacyList plist : pmanager.getPrivacyLists()) {
				JMenuItem it = new JMenuItem(plist.getListName());
				privMenu.add(it);
				if (plist.isActive()) {
					it.setIcon(SparkRes.getImageIcon("PRIVACY_LIGHTNING"));
				} else {
					it.setIcon(null);
				}
				final SparkPrivacyList finalList = plist;
				it.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PrivacyManager.getInstance().setListAsActive(
								finalList.getListName());
					}
				});
			}

			if (pmanager.hasActiveList()) {
				JMenuItem remMenu = new JMenuItem(Res.getString(
						"privacy.menuitem.deactivate.current.list", pmanager
								.getActiveList().getListName()),
						SparkRes.getImageIcon("PRIVACY_DEACTIVATE_LIST"));
				remMenu.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						pmanager.declineActiveList();
					}
				});
				privMenu.addSeparator();
				privMenu.add(remMenu);
			}

			popup.add(privMenu);
		}

		// Add change message
		final JMenuItem changeStatusMenu = new JMenuItem(
				Res.getString("menuitem.set.status.message"),
				SparkRes.getImageIcon(SparkRes.BLANK_IMAGE));
		popup.addSeparator();

		popup.add(changeStatusMenu);
		changeStatusMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// CustomMessages.editCustomMessages();
				mCustomMessageWindow.setVisible(true);
			}
		});

		final JPanel panel = getStatusPanel();

		addLocationsToPopup(popup);
		popup.show(panel, 0, panel.getHeight());
	}

	private JPopupMenu addLocationsToPopup(JPopupMenu menu) {
		menu.addSeparator();

		Action locationAction = new AbstractAction() {
			private static final long serialVersionUID = 7148051050075679995L;

			public void actionPerformed(ActionEvent actionEvent) {
				LocationManager.show();
			}
		};
		locationAction.putValue(Action.NAME,
				Res.getString("menuitem.edit.location"));
		menu.add(locationAction);

		List<Location> userLocations = LocationManager.getLocationsFromFile();
		if (!userLocations.isEmpty()) {
			menu.addSeparator();

			JMenu locationsMenu = new JMenu(
					Res.getString("menuitem.list.locations"));
			locationsMenu.setIcon(SparkRes.getImageIcon("LOCATION_EDIT_PIN"));

			final JMenuItem resetItem = new JMenuItem(
					Res.getString("menuitem.list.locations.reset"));
			resetItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LocationManager.setCurrentLocation(null);
					changeLocation(getCurrentPresence());
				}
			});
			locationsMenu.add(resetItem);

			for (final Location location : userLocations) {
				final JMenuItem it = new JMenuItem(location.getName());
				it.setIcon(locationsMenu.getIcon());
				it.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						LocationManager.setCurrentLocation(location);
						changeLocation(getCurrentPresence());
					}
				});
				locationsMenu.add(it);
			}
			menu.add(locationsMenu);
		}

		return menu;
	}

	private void changeLocation(Presence presence) {
		LocationManager.setCurrentLocationToPresence(presence);
		SparkManager.getSessionManager().changePresence(presence);
		setLocationName(LocationManager.getCurrentLocationNameOrEmpty());
	}

	private String changePresence(Presence presence) {
		// UN-9. Other clients can see "Invisible" status while we are disappearing.
		// So we send "Offline" instead of "Invisible" for them.
		String origStatusString = presence.getStatus();
		boolean isNewPresenceInvisible = EzucePresenceManager.isInvisible(presence);
		
		if (isNewPresenceInvisible && !PrivacyManager.getInstance().isPrivacyActive()) {
			JOptionPane.showMessageDialog(null, Res.getString("dialog.invisible.privacy.lists.not.supported"));
		}
		
		if (EzucePresenceManager.areEqual(getCurrentPresence(), presence)) {
			return origStatusString;
		}
		
		// ask user to confirm that all group chat rooms will be closed if
		// he/she goes to invisible.
		if (isNewPresenceInvisible && SparkManager.getChatManager().getChatContainer().hasGroupChatRooms()) {
			int reply = JOptionPane
					.showConfirmDialog(
							null,
							Res.getString("dialog.confirm.close.all.conferences.if.invisible.msg"),
							Res.getString("dialog.confirm.to.reveal.visibility.title"),
							JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.NO_OPTION) {
				return getCurrentPresence().getStatus();
			}
		}

		// If we go visible then we should send "Available" first.
		if (!isNewPresenceInvisible && EzucePresenceManager.isInvisible(getCurrentPresence()))
			PrivacyManager.getInstance().goToVisible();

		if (isNewPresenceInvisible) {
			presence.setStatus(Res.getString("status.offline"));
		}
		
		// Then set the current status.
		LocationManager.setCurrentLocationToPresence(presence);
		SparkManager.getSessionManager().changePresence(presence);

		// If we go invisible we should activate the "globally invisible list"
		// and send "Available" after "Unavailable" presence.
		if (isNewPresenceInvisible) {
			SparkManager.getChatManager().getChatContainer().closeAllGroupChatRooms();
			PrivacyManager.getInstance().goToInvisible();
		}

		presence.setStatus(origStatusString); // return "Invisible" instead of "Offline"
		return presence.getStatus();
	}
	
	protected Presence getPresenceOnStart() {
		if (SettingsManager.getLocalPreferences().isLoginAsInvisible())
			return EzucePresenceManager.getUnavailablePresence();

		Properties props = SettingsManager.getLocalPreferences()
				.getProperties();
		String resourceOnLogin = props.getProperty("resourceOnLoginByDefault");
		Location resource = LocationManager
				.getLocationByNameFromDefaults(resourceOnLogin);
		String defaultPresence = props.getProperty("presenceOnLoginByDefault");
		Presence foundPresence = resource.getPresence(defaultPresence);

		LocationManager.setCurrentLocation(resource);
		LocationManager.setCurrentLocationToPresence(foundPresence);
		return foundPresence;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Location) {
			Location location = (Location) arg;
			LocationManager.setCurrentLocation(location);
			changeLocation(getCurrentPresence());
		}
	}

}
