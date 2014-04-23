/*
 * EzuceChatRoomJPanel.java
 *
 * Created on Nov 2, 2011, 11:42:48 PM
 */
package org.ezuce.im.ui;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import javax.swing.*;

import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.impl.DummyCommonInterface;
import org.ezuce.common.phone.notifications.EzuceNotification;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.MugshotPanel;
import org.ezuce.common.ui.actions.ContactActions;
import org.ezuce.common.ui.wrappers.interfaces.UserMiniPanelCommonInterface;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactInfoWindow;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.UIComponentRegistry;
import org.jivesoftware.spark.util.log.Log;
import org.ezuce.common.ui.EzuceContactInfoWindow;
import org.ezuce.commons.ui.location.LocationManager;

/**
 * 
 * @author Razvan
 */
public class EzuceChatRoomJPanel extends JPanel implements RosterListener, VCardLoaderCallback {
	private Color bkgColor1 = new Color(0x888888);
	private Color bkgColor2 = new Color(0xAAAAAA);
	private javax.swing.JButton jButtonCallMobile;
	private javax.swing.JButton jButtonCallOffice;
	private javax.swing.JButton jButtonFacebook;
	private javax.swing.JButton jButtonLinkedin;
	private javax.swing.JButton jButtonSendEmail;
	private javax.swing.JButton jButtonTwitter;
	private javax.swing.JButton jButtonViewProfile;
	private javax.swing.JButton jButtonXing;
	private javax.swing.JLabel jLabelEmail;
	private javax.swing.JLabel jLabelCompany;
	private javax.swing.JLabel jLabelCompanyText;
	private javax.swing.JLabel jLabelLocalTime;
	private javax.swing.JLabel jLabelLocalTimeText;
	private javax.swing.JLabel jLabelLocation;
	private javax.swing.JLabel jLabelLocationText;
	private javax.swing.JLabel jLabelLocationFromPresence;
	private javax.swing.JLabel jLabelMobileNo;
	private javax.swing.JLabel jLabelOfficeNo;
	private javax.swing.JLabel jLabelUserDetails;
	private javax.swing.JLabel jLabelUserName;
	private javax.swing.JLabel jLabelViewProfile;
	private javax.swing.JPanel jPanelDetails;
	private javax.swing.JPanel jPanelLinks;
	private javax.swing.JPanel jPanelLocation;
	private javax.swing.JPanel jPanelMugshot;
	private javax.swing.JPanel jPanelPhoneNrs;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;
	private final ResourceMap resourceMap = org.jdesktop.application.Application
			.getInstance().getContext()
			.getResourceMap(EzuceChatRoomJPanel.class);
	private final MugshotPanel lblPicture = new MugshotPanel(false);
	private String participantJid;
	private Timer showLocalTimeTimer;
	private ContactItem ci;

	/** Creates new form EzuceChatRoomJPanel */
	public EzuceChatRoomJPanel() {
		initComponents();
	}

	public EzuceChatRoomJPanel(String jid) {
		initComponents();
		participantJid = jid;
		final String bareAddress = StringUtils.parseBareAddress(participantJid);
		final ContactList cList = Workspace.getInstance().getContactList();
		ci = cList.getContactItemByJID(participantJid);
		ContactGroup cg = null;
		for (ContactGroup g : cList.getContactGroups()) {
			ContactItem item = g.getContactItemByJID(participantJid, true);
			if (item != null) {
				cg = g;
				break;
			}
		}

		final ContactGroup group = cg;		
		
		jButtonViewProfile.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				ContactInfoWindow info = UIComponentRegistry
						.getContactInfoWindow();
				if (info instanceof EzuceContactInfoWindow) {
					((EzuceContactInfoWindow) info).display(group, e);
				} else {
					info.display(group, e);
				}
				info.setContactItem(ci);
				info.setWindowLocation(e.getXOnScreen(), e.getYOnScreen());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jButtonViewProfile.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				jButtonViewProfile.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

		});
		if (ci != null) {
			lblPicture.setPresence(ci.getPresence());
			lblPicture.updateStatus(ci.getPresence());
			setLocationName();
		}
		AsyncLoader.getInstance().executeJob(bareAddress, this);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}

	@Override
	public void vcardLoaded(VCard vCard) {
		if (ci != null) {
			ImageIcon avatar = Utils.retrieveAvatar(vCard);
			lblPicture.setMugshot(avatar);

			jLabelUserName.setText(ci.getDisplayName());
			UserMiniPanelCommonInterface contact = new DummyCommonInterface(ci);
			ContactActions actions = new ContactActions(contact, null);
			ApplicationActionMap actionMap = Application.getInstance()
					.getContext().getActionMap(ContactActions.class, actions);

			if (vCard != null) {
				Action actionOffice = actionMap.get("calloffice");
				jButtonCallOffice.setAction(actionOffice);
				jButtonCallOffice.setActionCommand(contact.getExtension());
				jButtonCallOffice.setIcon(resourceMap
						.getIcon("jButtonCallOffice.icon"));
				jButtonCallOffice.setToolTipText(contact.getExtension());

				if (vCard.getPhoneHome("CELL") != null
						&& !vCard.getPhoneHome("CELL").isEmpty()) {
					Action action = actionMap.get("call");
					jButtonCallMobile.setAction(action);
					jButtonCallMobile.setActionCommand(vCard.getPhoneHome("CELL"));
					jButtonCallMobile.setIcon(resourceMap
							.getIcon("jButtonCallMobile.icon"));
					jButtonCallMobile.setToolTipText(vCard.getPhoneHome("CELL"));
				}

				if (vCard.getEmailWork() != null
						&& !vCard.getEmailWork().isEmpty()) {
					Action action = actionMap.get("sendEmailAction");
					action.putValue(action.SMALL_ICON,
							jButtonSendEmail.getIcon());
					jButtonSendEmail.setActionCommand(vCard.getEmailWork());
					jButtonSendEmail.setAction(action);
					jButtonSendEmail.setToolTipText(vCard.getEmailWork());

				} else if (vCard.getEmailHome() != null
						&& !vCard.getEmailHome().isEmpty()) {
					Action action = actionMap.get("sendEmailAction");
					action.putValue(action.SMALL_ICON,
							jButtonSendEmail.getIcon());
					jButtonSendEmail.setActionCommand(vCard.getEmailHome());
					jButtonSendEmail.setAction(action);
					jButtonSendEmail.setToolTipText(vCard.getEmailHome());
				}
				String job = vCard.getField("TITLE");
				jLabelUserDetails.setText(!isEmpty(job) ? job : resourceMap.getString("no.job.title"));
				String location = vCard.getField("X-LOCATION");
				jLabelLocationText.setText(!isEmpty(location) ? location : EMPTY);
				setLocationName();
				String company = vCard.getOrganization();
				jLabelCompanyText.setText(!isEmpty(company) ? company : EMPTY);
				String twitter = vCard.getField("X-TWITTER");
				jButtonTwitter.setVisible(!isEmpty(twitter));
				jButtonTwitter.setToolTipText(twitter);
				String facebook = vCard.getField("X-FACEBOOK");
				jButtonFacebook.setVisible(!isEmpty(facebook));
				jButtonFacebook.setToolTipText(facebook);
				String linkedin = vCard.getField("X-LINKEDIN");
				jButtonLinkedin.setVisible(!isEmpty(linkedin));
				jButtonLinkedin.setToolTipText(linkedin);
				String xing = vCard.getField("X-XING");
				jButtonXing.setVisible(!isEmpty(xing));
				jButtonXing.setToolTipText(xing);				
			} else {
				Log.warning("VCard for jid=" + participantJid + " is null.");
			}
		} else {
			Log.warning("Contact item for jid=" + participantJid + " is null.");
		}
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		Graphics2D g2d = (Graphics2D) grphcs;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint gp = new GradientPaint(0, 0, bkgColor1,
				2 * getWidth() / 3, getHeight(), bkgColor2, true);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}

	private void configureTimer() {
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		showLocalTimeTimer = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String localTime = sdf.format(Calendar.getInstance().getTime());
				jLabelLocalTimeText.setText(localTime);
			}
		});
		showLocalTimeTimer.setInitialDelay(0);
		showLocalTimeTimer.start();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		jPanelDetails = new javax.swing.JPanel();
		jLabelUserName = new javax.swing.JLabel();
		jLabelUserDetails = new javax.swing.JLabel();
		jLabelViewProfile = new javax.swing.JLabel();
		jButtonViewProfile = new javax.swing.JButton();
		jPanelMugshot = new javax.swing.JPanel();
		jSeparator1 = new javax.swing.JSeparator();
		jPanelLocation = new javax.swing.JPanel();
		jLabelLocation = new javax.swing.JLabel();
		jLabelLocalTime = new javax.swing.JLabel();
		jLabelCompany = new javax.swing.JLabel();
		jLabelLocationText = new javax.swing.JLabel();
		jLabelLocationFromPresence = new javax.swing.JLabel();
		jLabelLocalTimeText = new javax.swing.JLabel();
		jLabelCompanyText = new javax.swing.JLabel();
		jSeparator2 = new javax.swing.JSeparator();
		jPanelPhoneNrs = new javax.swing.JPanel();
		jButtonCallOffice = new javax.swing.JButton();
		jButtonCallMobile = new javax.swing.JButton();
		jButtonSendEmail = new javax.swing.JButton();
		jLabelOfficeNo = new javax.swing.JLabel();
		jLabelMobileNo = new javax.swing.JLabel();
		jLabelEmail = new javax.swing.JLabel();
		jSeparator3 = new javax.swing.JSeparator();
		jPanelLinks = new javax.swing.JPanel();
		jButtonTwitter = new javax.swing.JButton();
		jButtonXing = new javax.swing.JButton();
		jButtonLinkedin = new javax.swing.JButton();
		jButtonFacebook = new javax.swing.JButton();
		jSeparator4 = new javax.swing.JSeparator();

		setMaximumSize(new java.awt.Dimension(32767, 104));
		setMinimumSize(new java.awt.Dimension(300, 104));
		setPreferredSize(new java.awt.Dimension(633, 104));

		jPanelDetails.setName("jPanelDetails"); // NOI18N
		jPanelDetails.setOpaque(false);
		jPanelDetails.setPreferredSize(new java.awt.Dimension(207, 82));

		jLabelUserName.setFont(new java.awt.Font("Arial", 1, 11));
		jLabelUserName.setForeground(new java.awt.Color(0, 102, 153));
		jLabelUserName.setText(resourceMap.getString("user.name"));
		jLabelUserName.setName("jLabelUserName"); // NOI18N

		jLabelUserDetails.setText(resourceMap.getString("position"));
		jLabelUserDetails.setName("jLabelUserDetails"); // NOI18N

		jLabelViewProfile.setForeground(new java.awt.Color(102, 102, 102));
		jLabelViewProfile.setText(resourceMap.getString("view.profile"));
		jLabelViewProfile.setName("jLabelViewProfile"); // NOI18N

		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance().getContext()
				.getResourceMap(EzuceChatRoomJPanel.class);
		jButtonViewProfile.setIcon(resourceMap
				.getIcon("jButtonViewProfile.icon")); // NOI18N
		jButtonViewProfile.setBorder(null);
		jButtonViewProfile.setBorderPainted(false);
		jButtonViewProfile.setContentAreaFilled(false);
		jButtonViewProfile.setDoubleBuffered(true);
		jButtonViewProfile.setFocusPainted(false);
		jButtonViewProfile.setHideActionText(true);
		jButtonViewProfile.setName("jButtonViewProfile"); // NOI18N

		jPanelMugshot.setMaximumSize(new java.awt.Dimension(72, 70));
		jPanelMugshot.setMinimumSize(new java.awt.Dimension(0, 0));
		jPanelMugshot.setName("jPanelMugshot"); // NOI18N
		jPanelMugshot.setPreferredSize(new java.awt.Dimension(72, 80));
		jPanelMugshot.setLayout(new javax.swing.BoxLayout(jPanelMugshot,
				javax.swing.BoxLayout.LINE_AXIS));
		
		jLabelLocationFromPresence.setMinimumSize(new Dimension(100, 16));
		jLabelLocationFromPresence.setPreferredSize(new Dimension(100, 16));
		//jLabelLocationFromPresence.setMaximumSize(new Dimension(100, 20));

		javax.swing.GroupLayout jPanelDetailsLayout = new javax.swing.GroupLayout(jPanelDetails);
		jPanelDetails.setLayout(jPanelDetailsLayout);
		jPanelDetailsLayout.setHorizontalGroup(jPanelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelDetailsLayout.createSequentialGroup()
										.addGap(8, 8, 8)
										.addComponent(jPanelMugshot, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanelDetailsLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(jLabelUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
															.addComponent(jLabelLocationFromPresence)
															.addGroup(
																	jPanelDetailsLayout.createSequentialGroup()
																			.addComponent(jButtonViewProfile)
																			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																			.addComponent(jLabelViewProfile))
															.addComponent(jLabelUserDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
												)
										.addGap(8, 8, 8)));
		
		jPanelDetailsLayout.setVerticalGroup(jPanelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(jPanelDetailsLayout.createSequentialGroup()
											.addGroup(
													jPanelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(jPanelMugshot, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
															.addGroup(
																	jPanelDetailsLayout.createSequentialGroup()
																			.addComponent(jLabelUserName)
																			.addGap(2, 2, 2)
																			.addComponent(jLabelUserDetails)
																			.addGap(2, 2, 2)
																			.addComponent(jLabelLocationFromPresence)
																			.addGap(5, 5, 5)
																			.addGroup(
																					jPanelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
																						.addComponent(jButtonViewProfile)
																						.addComponent(jLabelViewProfile)
																					)
																	)
													)
											.addGap(8, 8, 8)
									));

		jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator1.setName("jSeparator1"); // NOI18N
		jSeparator1.setPreferredSize(new java.awt.Dimension(2, 82));
		jSeparator1.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0,
				ColorConstants.SEPARATOR_COLOR_2));

		jPanelLocation.setName("jPanelLocation"); // NOI18N
		jPanelLocation.setOpaque(false);
		jPanelLocation.setPreferredSize(new java.awt.Dimension(198, 82));

		jLabelLocation.setForeground(new java.awt.Color(102, 102, 102));
		jLabelLocation.setText(resourceMap.getString("location"));
		jLabelLocation.setName("jLabelLocation"); // NOI18N

		jLabelLocalTime.setForeground(new java.awt.Color(102, 102, 102));
		jLabelLocalTime.setText(resourceMap.getString("local.time"));
		jLabelLocalTime.setName("jLabelLocalTime"); // NOI18N

		jLabelCompany.setForeground(new java.awt.Color(102, 102, 102));
		jLabelCompany.setText(resourceMap.getString("company"));
		jLabelCompany.setDoubleBuffered(true);
		jLabelCompany.setName("jLabelCompany"); // NOI18N

		jLabelLocationText.setText(" ");
		jLabelLocationText.setName("jLabelLocationText"); // NOI18N

		jLabelLocalTimeText.setText(" ");
		jLabelLocalTimeText.setName("jLabelLocalTimeText"); // NOI18N

		jLabelCompanyText.setText(" ");
		jLabelCompanyText.setName("jLabelCompanyText"); // NOI18N

		javax.swing.GroupLayout jPanelLocationLayout = new javax.swing.GroupLayout(
				jPanelLocation);
		jPanelLocation.setLayout(jPanelLocationLayout);
		jPanelLocationLayout
				.setHorizontalGroup(jPanelLocationLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelLocationLayout
										.createSequentialGroup()
										.addGap(10, 10, 10)
										.addGroup(
												jPanelLocationLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanelLocationLayout
																		.createSequentialGroup()
																		.addGroup(
																				jPanelLocationLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jLabelLocation)
																						.addComponent(
																								jLabelLocalTime))
																		.addGap(3,
																				3,
																				3)
																		.addGroup(
																				jPanelLocationLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jLabelLocalTimeText,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								106,
																								Short.MAX_VALUE)
																						.addComponent(
																								jLabelLocationText,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								106,
																								Short.MAX_VALUE)))
														.addGroup(
																jPanelLocationLayout
																		.createSequentialGroup()
																		.addComponent(
																				jLabelCompany)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabelCompanyText,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				103,
																				Short.MAX_VALUE)))
										.addGap(8, 8, 8)));
		jPanelLocationLayout
				.setVerticalGroup(jPanelLocationLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelLocationLayout
										.createSequentialGroup()
										.addGap(12, 12, 12)
										.addGroup(
												jPanelLocationLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jLabelLocation)
														.addComponent(
																jLabelLocationText))
										.addGap(12, 12, 12)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanelLocationLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jLabelLocalTime)
														.addComponent(
																jLabelLocalTimeText))
										.addGap(12, 12, 12)
										.addGroup(
												jPanelLocationLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jLabelCompanyText)
														.addComponent(
																jLabelCompany))
										.addGap(8, 8, 8)));

		jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator2.setName("jSeparator2"); // NOI18N
		jSeparator2.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0,
				ColorConstants.SEPARATOR_COLOR_2));
		jSeparator2.setPreferredSize(new java.awt.Dimension(2, 82));
		jPanelPhoneNrs.setName("jPanelPhoneNrs"); // NOI18N
		jPanelPhoneNrs.setOpaque(false);
		jPanelPhoneNrs.setPreferredSize(new java.awt.Dimension(79, 82));

		jButtonCallOffice.setBackground(new java.awt.Color(255, 255, 255));
		jButtonCallOffice
				.setIcon(resourceMap.getIcon("jButtonCallOffice.icon")); // NOI18N
		jButtonCallOffice.setBorder(null);
		jButtonCallOffice.setDoubleBuffered(true);
		jButtonCallOffice.setFocusPainted(false);
		jButtonCallOffice.setHideActionText(true);
		jButtonCallOffice
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonCallOffice.setMaximumSize(new java.awt.Dimension(10, 10));
		jButtonCallOffice.setMinimumSize(new java.awt.Dimension(10, 10));
		jButtonCallOffice.setName("jButtonCallOffice"); // NOI18N
		jButtonCallOffice.setOpaque(false);
		jButtonCallOffice.setPreferredSize(new java.awt.Dimension(10, 10));

		jButtonCallMobile
				.setIcon(resourceMap.getIcon("jButtonCallMobile.icon")); // NOI18N
		jButtonCallMobile.setBorder(null);
		jButtonCallMobile.setBorderPainted(false);
		jButtonCallMobile.setDoubleBuffered(true);
		jButtonCallMobile.setFocusPainted(false);
		jButtonCallMobile.setHideActionText(true);
		jButtonCallMobile.setName("jButtonCallMobile"); // NOI18N
		jButtonCallMobile.setOpaque(false);

		jButtonSendEmail.setIcon(resourceMap.getIcon("jButtonSendEmail.icon")); // NOI18N
		jButtonSendEmail.setBorder(null);
		jButtonSendEmail.setBorderPainted(false);
		jButtonSendEmail.setDoubleBuffered(true);
		jButtonSendEmail.setFocusPainted(false);
		jButtonSendEmail.setHideActionText(true);
		jButtonSendEmail.setName("jButtonSendEmail"); // NOI18N
		jButtonSendEmail.setOpaque(false);

		jLabelOfficeNo.setForeground(new java.awt.Color(102, 102, 102));
		jLabelOfficeNo.setText(resourceMap.getString("office"));
		jLabelOfficeNo.setName("jLabelOfficeNo"); // NOI18N

		jLabelMobileNo.setForeground(new java.awt.Color(102, 102, 102));
		jLabelMobileNo.setText(resourceMap.getString("mobile"));
		jLabelMobileNo.setName("jLabelMobileNo"); // NOI18N

		jLabelEmail.setForeground(new java.awt.Color(102, 102, 102));
		jLabelEmail.setText(resourceMap.getString("email"));
		jLabelEmail.setName("jLabelEmail"); // NOI18N

		javax.swing.GroupLayout jPanelPhoneNrsLayout = new javax.swing.GroupLayout(
				jPanelPhoneNrs);
		jPanelPhoneNrs.setLayout(jPanelPhoneNrsLayout);
		jPanelPhoneNrsLayout
				.setHorizontalGroup(jPanelPhoneNrsLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelPhoneNrsLayout
										.createSequentialGroup()
										.addGap(10, 10, 10)
										.addGroup(
												jPanelPhoneNrsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanelPhoneNrsLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonCallOffice,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				21,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabelOfficeNo))
														.addGroup(
																jPanelPhoneNrsLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonCallMobile,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				21,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabelMobileNo))
														.addGroup(
																jPanelPhoneNrsLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonSendEmail,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				21,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabelEmail)))
										.addGap(8, 8, 8)));
		jPanelPhoneNrsLayout
				.setVerticalGroup(jPanelPhoneNrsLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelPhoneNrsLayout
										.createSequentialGroup()
										.addGap(12, 12, 12)
										.addGroup(
												jPanelPhoneNrsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.CENTER)
														.addComponent(
																jButtonCallOffice,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																21,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabelOfficeNo))
										.addGap(9, 9, 9)
										.addGroup(
												jPanelPhoneNrsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.CENTER)
														.addComponent(
																jButtonCallMobile,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																21,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabelMobileNo))
										.addGap(9, 9, 9)
										.addGroup(
												jPanelPhoneNrsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.CENTER)
														.addComponent(
																jLabelEmail)
														.addComponent(
																jButtonSendEmail,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																21,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(8, 8, 8)));

		jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator3.setName("jSeparator3"); // NOI18N
		jSeparator3.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0,
				ColorConstants.SEPARATOR_COLOR_2));
		jSeparator3.setPreferredSize(new java.awt.Dimension(2, 82));
		jPanelLinks.setName("jPanelLinks"); // NOI18N
		jPanelLinks.setOpaque(false);
		jPanelLinks.setPreferredSize(new java.awt.Dimension(133, 82));

		jButtonTwitter.setIcon(resourceMap.getIcon("jButtonTwitter.icon")); // NOI18N
		jButtonTwitter.setBorder(null);
		jButtonTwitter.setBorderPainted(false);
		jButtonTwitter.setContentAreaFilled(false);
		jButtonTwitter.setFocusPainted(false);
		jButtonTwitter.setHideActionText(true);
		jButtonTwitter.setName("jButtonTwitter"); // NOI18N

		jButtonXing.setIcon(resourceMap.getIcon("jButtonXing.icon")); // NOI18N
		jButtonXing.setBorder(null);
		jButtonXing.setBorderPainted(false);
		jButtonXing.setContentAreaFilled(false);
		jButtonXing.setDoubleBuffered(true);
		jButtonXing.setFocusPainted(false);
		jButtonXing.setHideActionText(true);
		jButtonXing.setName("jButtonXing"); // NOI18N

		jButtonLinkedin.setIcon(resourceMap.getIcon("jButtonLinkedin.icon")); // NOI18N
		jButtonLinkedin.setBorder(null);
		jButtonLinkedin.setBorderPainted(false);
		jButtonLinkedin.setContentAreaFilled(false);
		jButtonLinkedin.setDoubleBuffered(true);
		jButtonLinkedin.setFocusPainted(false);
		jButtonLinkedin.setHideActionText(true);
		jButtonLinkedin.setName("jButtonLinkedin"); // NOI18N

		jButtonFacebook.setIcon(resourceMap.getIcon("jButtonLinkedin1.icon")); // NOI18N
		jButtonFacebook.setBorder(null);
		jButtonFacebook.setBorderPainted(false);
		jButtonFacebook.setContentAreaFilled(false);
		jButtonFacebook.setDoubleBuffered(true);
		jButtonFacebook.setFocusPainted(false);
		jButtonFacebook.setHideActionText(true);
		jButtonFacebook.setName("jButtonFacebook"); // NOI18N

		javax.swing.GroupLayout jPanelLinksLayout = new javax.swing.GroupLayout(
				jPanelLinks);
		jPanelLinks.setLayout(jPanelLinksLayout);
		jPanelLinksLayout
				.setHorizontalGroup(jPanelLinksLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelLinksLayout
										.createSequentialGroup()
										.addGap(10, 10, 10)
										.addGroup(
												jPanelLinksLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanelLinksLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonFacebook)
																		.addGap(4,
																				4,
																				4)
																		.addComponent(
																				jButtonLinkedin))
														.addGroup(
																jPanelLinksLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonTwitter))
														.addGap(4, 4, 4)
														.addGroup(
																jPanelLinksLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonXing)))
										.addGap(8, 8, 8)));
		jPanelLinksLayout
				.setVerticalGroup(jPanelLinksLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelLinksLayout
										.createSequentialGroup()
										.addGap(12, 12, 12)
										.addGroup(
												jPanelLinksLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jButtonFacebook)
														.addGap(4, 4, 4)
														.addComponent(
																jButtonLinkedin))
										.addGap(4, 4, 4)
										.addGroup(
												jPanelLinksLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jButtonTwitter))
										.addGap(4, 4, 4)
										.addGroup(
												jPanelLinksLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jButtonXing))
										.addGap(8, 8, 8)));

		jSeparator4.setName("jSeparator4"); // NOI18N

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(0, 0, 0)
								.addComponent(jPanelDetails,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										260,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jSeparator1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										2,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0)
								.addComponent(jPanelLocation,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										170,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(2, 2, 2)
								.addComponent(jSeparator2,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0)
								.addComponent(jPanelPhoneNrs,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										90,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(2, 2, 2)
								.addComponent(jSeparator3,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(2, 2, 2)
								.addComponent(jPanelLinks,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										140, Short.MAX_VALUE))
				.addComponent(jSeparator4,
						javax.swing.GroupLayout.DEFAULT_SIZE, 670,
						Short.MAX_VALUE));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						jPanelLocation,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						102,
																						Short.MAX_VALUE)
																				.addComponent(
																						jPanelPhoneNrs,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						102,
																						Short.MAX_VALUE)
																				.addGroup(
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addComponent(
																										jSeparator2,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										82,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addGap(10,
																										10,
																										10))
																				.addGroup(
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addComponent(
																										jSeparator1,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										82,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addGap(10,
																										10,
																										10))
																				.addGroup(
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addComponent(
																										jSeparator3,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										82,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addGap(10,
																										10,
																										10))
																				.addComponent(
																						jPanelLinks,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						102,
																						Short.MAX_VALUE))
																.addGap(0, 0, 0))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(10, 10,
																		10)
																.addComponent(
																		jPanelDetails,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		82,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
								.addComponent(jSeparator4,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)));
		jPanelMugshot.add(lblPicture, BorderLayout.CENTER);

		bkgColor1 = new Color(resourceMap.getInteger("bkgColor1"));
		bkgColor2 = new Color(resourceMap.getInteger("bkgColor2"));
		configureTimer();
		EzucePresenceUtils.registerRosterListener(this);
	}	

	@Override
	public void entriesAdded(Collection<String> clctn) {
		// don't handle
	}

	@Override
	public void entriesUpdated(Collection<String> clctn) {
		// don't handle
	}

	@Override
	public void entriesDeleted(Collection<String> clctn) {
		// don't handle
	}

	@Override
	public void presenceChanged(Presence prsnc) {
		String bareJid = StringUtils.parseBareAddress(prsnc.getFrom());
		if (bareJid.equals(participantJid)) {
			lblPicture.updateStatus(prsnc);
			if (Utils.isVCardUpdated(prsnc)) {
				AsyncLoader.getInstance().execute(bareJid, this);
			}
		}

	}
	
	protected void setLocationName() {
		String originalLocation = jLabelLocationText.getText();
		String locationFromPresence = LocationManager.getLocationNameFromPresence(ci.getPresence());
		jLabelLocationText.setText(org.apache.commons.lang3.StringUtils.isEmpty(locationFromPresence) ? originalLocation : locationFromPresence);
	}
	
}