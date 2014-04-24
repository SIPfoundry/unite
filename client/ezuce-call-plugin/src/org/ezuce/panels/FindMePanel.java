package org.ezuce.panels;

import org.ezuce.common.ui.panels.LoadingPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.io.FindMeFileIO;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.CallSequence;
import org.ezuce.common.rest.FindMeGroup;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.rest.Ring;
import org.ezuce.panels.interfaces.FindMePanelInterface;
import org.ezuce.tasks.LoadFindMeGroupsTask;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.PresenceListener;
import org.jivesoftware.spark.util.log.Log;


public class FindMePanel extends javax.swing.JPanel implements RosterListener,
		FindMePanelInterface {
	private FindMeRosterPanel findMeRosterPanelInitial;
	private JButton jButtonSave;
	private JLabel jLabelBottomBackground;
	private JLabel jLabelHorizontalLink;
	private JLabel jLabelStart;
	private JLabel jLabelVGap;
	private JLayeredPane jLayeredPane;
	private JPanel jPanelBottom;
	private JPanel jPanelContents;
	private JPanel jPanelRoster;
	private JPanel jPanelVerticalIcons;
	private JPanel jPanelVerticalLinks;
	private JScrollPane jScrollPaneContents;
	private JXPanel jXPanel;
	private FindMeMiniPanel currentUserPanel;
	private final LoadingPanel loading = new LoadingPanel();
	public final String INITIAL_ROSTER_PANEL_NAME = "findMeRosterPanelInitial";
	public final String VOICEMAIL_ROSTER_PANEL_NAME = "voiceMailRosterPanel";
	/** Creates new form FindMePanel */
	public FindMePanel() {
		initComponents();

		jPanelBottom.setOpaque(false);
		jPanelVerticalIcons.setOpaque(false);
		jPanelVerticalLinks.setOpaque(false);
		jScrollPaneContents.setOpaque(false);
		jPanelContents.setOpaque(false);
		final javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance().getContext().getActionMap(this);
		javax.swing.Action action = actionMap.get("saveCallSequencesAction");
		action.putValue(action.SMALL_ICON, jButtonSave.getIcon());
		jButtonSave.setAction(action);

		loadFindMeGroups();
		ResourceMap resourceMap = Application.getInstance().getContext()
				.getResourceMap(FindMePanel.class);
		jButtonSave.setPressedIcon(resourceMap.getImageIcon("jButtonSave.icon.rollover"));
		SparkManager.getSessionManager().addPresenceListener(
				new PresenceListener() {
					@Override
					public void presenceChanged(Presence presence) {
						if (currentUserPanel == null) {
							return;
						}
						currentUserPanel.updateStatus(presence);
					}
				});	
	}

	/**
	 * Loads IN BACKGROUND the information about Find Me/Follow Me groups stored
	 * in config. files, located in the user's directory. REMARK: The first
	 * group should ALWAYS contain the authenticated user on top. If the first
	 * group does not contain the current user, it will be automatically added.
	 */
	private void loadFindMeGroups() {
		LoadFindMeGroupsTask lfmgTask = new LoadFindMeGroupsTask(
				org.jdesktop.application.Application.getInstance(), this);
		lfmgTask.execute();
	}

	/**
	 * Returns TRUE if the user has configured a voice mail box, and FALSE
	 * otherwise.
	 * 
	 * @return
	 */
	public boolean isVoiceMailServiceAvailable() {
		return true; // TODO: call necessary REST service.
	}

	/**
	 * Tests if the CallSequence instance reference received as an argument
	 * contains the currently authenticated user.
	 * 
	 * @param fmg
	 * @return TRUE - if the CallSequence instance reference received as an
	 *         argument contains the currently authenticated user ON THE FIRST
	 *         POSITION, FALSE otherwise.
	 */
	public boolean containsCurrentUser(FindMeGroup fmg) {
		// System.out.format("\n Check if FindMeGroup starts with current user.");
		String currentJID = SparkManager.getSessionManager().getJID();
		if (currentJID.contains("@"))
			currentJID = currentJID.substring(0, currentJID.indexOf("@"));
		if (fmg != null && !fmg.getRings().isEmpty()) {
			final List<Ring> rings = fmg.getRings();
			if (!rings.isEmpty()) {
				Ring r = rings.get(0);
				if (r.getNumber().equalsIgnoreCase(currentJID))
					return true;
			}
		}
		// System.out.format("\n CallSequence does NOT with current user.");
		return false;
	}

	/**
	 * Adds the specified FindMeMiniPanel to the specified FindMeRosterPanel.
	 * 
	 * @param fmrp
	 * @param fmmp
	 */
	public void addContactToPanel(FindMeRosterPanel fmrp, FindMeMiniPanel fmmp) {
		// System.out.format("\nAdding contact to Find Me group.");
		fmrp.addUserToRosterPanel(fmmp);
		// System.out.format("\nUpdating vertical link UI.");
		updateVerticalLinkGap(fmrp, fmmp, true);
	}

	@Override
	public void removeContactFromPanel(FindMeRosterPanel fmrp,
			FindMeMiniPanel fmmp) {
		// System.out.format("\nRemoving contact from Find Me group.");
		fmrp.removeUserFromRosterPanel(fmmp);
		// System.out.format("\nUpdating vertical link UI.");
		updateVerticalLinkGap(fmrp, fmmp, false);
	}

	@Override
	public boolean isFirstInRoster(FindMeRosterPanel fmrp) {
		return (fmrp.getName().equals(INITIAL_ROSTER_PANEL_NAME));
	}

	public int getGroupPanelIndex(String rosterPanelName) {
		int n = this.jPanelRoster.getComponentCount();
		for (int i = 0; i < n; i++) {
			if (this.jPanelRoster.getComponent(i).getName()
					.equals(rosterPanelName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ALWAYS add a FindMeRosterPanel to its container BEFORE adding
	 * FindMeMiniPanel instances to it.
	 * 
	 * @param fmrp
	 */
	public void addRosterPanel(FindMeRosterPanel fmrp,
			String precedingRosterPanelName) {
		// System.out.format("\nAdding roster to Find Me panel, after "+precedingRosterPanelName);
		// First, get the position at which to add the FindMeRosterPanel, then
		// add it:
		int position = -1; // by default, add it at the bottom;
		if (precedingRosterPanelName != null) {
			position = this.getGroupPanelIndex(precedingRosterPanelName) + 1;
			// System.out.println("New roster's position:"+position);
			if (position == 0)
				position = 1;// this means AFTER the initial group, which should
								// only contain the current user.
		}
		this.jPanelRoster.add(fmrp, position);

		JLabel verticalLinkLabel = new JLabel();
		verticalLinkLabel.setName(fmrp.getName() + fmrp.LINK_NAME_SUFFIX);
		ResourceMap resourceMap = Application.getInstance().getContext()
				.getResourceMap(FindMePanel.class);
		verticalLinkLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		verticalLinkLabel.setIcon(resourceMap
				.getIcon("jLabelVerticalLink.icon"));
		if (position != -1)// compute the index at which the link label and then
							// the gap label must be added:
		{
			position = 2 * position;
		}
		this.jPanelVerticalLinks.add(verticalLinkLabel, position);

		JLabel verticalGapLabel = new JLabel(" ");
		verticalGapLabel.setName(fmrp.getName() + fmrp.GAP_NAME_SUFFIX);
		int initialGapHeight = 0;
		verticalGapLabel.setSize(this.jPanelVerticalLinks.getWidth(),
				initialGapHeight);
		verticalGapLabel.setPreferredSize(verticalGapLabel.getSize());
		// verticalGapLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,
		// 1));
		if (position != -1) {
			position++;
		}
		this.jPanelVerticalLinks.add(verticalGapLabel, position);

		this.jPanelVerticalLinks.revalidate();

		this.jPanelRoster.revalidate();
	}

	/**
	 * Resizes the label used to fill the gap between two consecutive links. The
	 * gap label corresponds, by means of its name, to the FindMeRosterPanel
	 * instance to which a FindMeMiniPanel instance was added. The logic for
	 * getting the name of the necessary label is:
	 * label_name=fmrp.getName()+"VGap"; The label's height will be increased by
	 * the height of the FindMeMiniPanel instance.
	 * 
	 * @param fmrp
	 * @param fmmp
	 */
	private void updateVerticalLinkGap(FindMeRosterPanel fmrp,
			FindMeMiniPanel fmmp, boolean increase) {
		JLabel verticalGapLabel = null;
		for (Component c : this.jPanelVerticalLinks.getComponents()) {
			if (c.getName().equals(fmrp.getName() + fmrp.GAP_NAME_SUFFIX)) {
				verticalGapLabel = (JLabel) c;
				break;
			}
		}
		if (verticalGapLabel == null)
			return;

		Dimension gapV = verticalGapLabel.getSize();
		int fmrpVerticalLayoutHeight = 2;
		int gapHeightDelta = fmmp.getPreferredSize().height
				* (increase ? 1 : -1) + fmrpVerticalLayoutHeight
				* (increase ? 1 : -1);
		int gapHeight = verticalGapLabel.getHeight() + gapHeightDelta;

		// if we are adding the first FindMeMiniPanel to a FindMeRosterPanel
		// other
		// than the first one, then we should modify the size of the gap by less
		// than the usual amount.
		// if (!isFirstInRoster(fmrp) &&
		// fmrp.getNumberOfMiniPanels()==1) //compare with 1 because the
		// addition of the FMMP is performed before updating the vertical gap.
		if (fmrp.getNumberOfMiniPanels() == 1 && increase) {
			if (isFirstInRoster(fmrp)) {
				gapHeight -= 8 * (increase ? 1 : -1); // substract the height of
														// the link picture (its
														// horizontal part)
			} else {
				gapHeight -= (18) * (increase ? 1 : -1);
			}
		}

		verticalGapLabel.setSize(gapV.width, gapHeight);
		verticalGapLabel.setPreferredSize(verticalGapLabel.getSize());

		this.jPanelVerticalLinks.revalidate();
		// System.out.format("\nVertical link updated.Increase ? - "+increase);
	}

	/**
	 * Removes the specified FindMeRosterPanel instance from the content panel.
	 * 
	 * @param fmrp
	 */
	@Override
	public void removeRosterPanel(FindMeRosterPanel fmrp) {
		// System.out.format("\n Removing roster to Find Me panel.");

		JLabel verticalGapLabel = null;
		for (Component c : this.jPanelVerticalLinks.getComponents()) {
			if (c.getName().equals(fmrp.getName() + fmrp.GAP_NAME_SUFFIX)) {
				verticalGapLabel = (JLabel) c;
				break;
			}
		}
		if (verticalGapLabel == null) {
			Log.warning(String
					.format("Could not find corresponding verticalGapLabel for FindMeRosterPanel [ %s ]",
							fmrp.getName()));
			return;
		}

		JLabel verticalLinkLabel = null;
		for (Component c : this.jPanelVerticalLinks.getComponents()) {
			if (c.getName().equals(fmrp.getName() + fmrp.LINK_NAME_SUFFIX)) {
				verticalLinkLabel = (JLabel) c;
				break;
			}
		}
		if (verticalLinkLabel == null) {
			Log.warning(String
					.format("Could not find corresponding verticalLinkLabel for FindMeRosterPanel [ %s ]",
							fmrp.getName()));
			return;
		}

		this.jPanelVerticalLinks.remove(verticalGapLabel);
		this.jPanelVerticalLinks.remove(verticalLinkLabel);
		this.jPanelRoster.remove(fmrp);

		this.jPanelVerticalLinks.revalidate();
		this.jPanelRoster.revalidate();
	}

	/**
	 * Counts the number of groups in the Find Me panel roster. The number will
	 * always be at least 1.
	 * 
	 * @return
	 */
	public int getNrRosterGroups() {
		int n = 0;
		for (Component c : this.jPanelRoster.getComponents()) {
			if (c instanceof FindMeRosterPanel) {
				n++;
			}
		}
		return n;
	}

	public List<FindMeRosterPanel> getRosterGroupsList() {
		List<FindMeRosterPanel> groups = new ArrayList<FindMeRosterPanel>();
		for (Component c : this.jPanelRoster.getComponents()) {
			if (c instanceof FindMeRosterPanel) {
				groups.add((FindMeRosterPanel) c);
			}
		}
		return groups;
	}

	@Action
	public void saveCallSequencesAction(ActionEvent ae) {
		saveCallSequence();
	}

	private void saveCallSequence() {
		final CallSequence callSequence = new CallSequence();
		// Get all FindmeRosterPanel references, which contain CallSequence
		// refs.
		final List<FindMeGroup> findMeGroups = new ArrayList<FindMeGroup>();
		final List<FindMeRosterPanel> rosterPanels = this.getRosterGroupsList();

		for (FindMeRosterPanel fmrp : rosterPanels) {
			if (fmrp.getName().equalsIgnoreCase(INITIAL_ROSTER_PANEL_NAME)) {
				// if this is the initial group=>get the expiration time
				// configured
				// for the local user, and set it as expiration time for the
				// callSequence.
				final Object initial = fmrp.getUserFromRoster(0);
				if (initial instanceof FindMeMiniPanel) {
					// if this is a FindMeMiniPanel component - it's the first
					// one,
					// representing the local user, so we can read the
					// expiration from it:
					final int[] exp = ((FindMeMiniPanel) initial)
							.getExpiration();
					if (exp.length > 1)
						callSequence.setExpiration(exp[0] * 60 + exp[1]);
				}
			}
			findMeGroups.add(fmrp.getFindMeGroup());
		}
		callSequence.setGroups(findMeGroups);

		String findMeCfgXML = null;
		try {
			findMeCfgXML = new FindMeFileIO()
					.convertCallSequenceToXML(callSequence);
		} catch (Exception ex) {
			Log.error("Could not retrieve Find Me panel configuration!", ex);
		}

		try {
			if (findMeCfgXML != null) {
				boolean res = RestManager.getInstance().configureForwarding(
						findMeCfgXML);
				if (!res) {
					throw new Exception(
							"Could not send Find Me/Follow Me config to server.");
				}
			}
		} catch (Exception e) {
			Log.error("Could not configure Find Me/Follow Me on the server !");
		}
	}

	@Override
	public void entriesAdded(Collection<String> clctn) {
		System.out.println("Entries added");
	}

	@Override
	public void entriesUpdated(Collection<String> clctn) {

	}

	@Override
	public void entriesDeleted(Collection<String> clctn) {
		System.out.println("Entries deleted");
	}

	@Override
	public void presenceChanged(Presence prsnc) {
		String jid = prsnc.getFrom();
		String bareJid = StringUtils.parseName(jid);
		for (FindMeRosterPanel fmrp : getRosterGroupsList()) {
			final FindMeMiniPanel umpg = fmrp.getMiniPanelByJID(jid);
			if (umpg != null) {
				umpg.customizePresence();
				if (Utils.isVCardUpdated(prsnc)) {
					AsyncLoader.getInstance().execute(prsnc.getFrom(), umpg);
				}
				break;
			}
		}
	}

	/**
	 * Clears all groups shown, and then displays the "Loading" message.
	 */
	public void showLoading() {
		this.jPanelContents.setVisible(false);
		this.jScrollPaneContents.setViewportView(loading);
		loading.setVisible(true);
		this.jScrollPaneContents.revalidate();
	}

	/**
	 * Hides the "Loading" message.
	 */
	public void hideLoading() {
		this.jScrollPaneContents.setViewportView(this.jPanelContents);
		this.jPanelContents.setVisible(true);
		this.jScrollPaneContents.revalidate();
		if (loading != null) {
			loading.setVisible(false);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {// GEN-BEGIN:initComponents

		jXPanel = new JXPanel();
		jScrollPaneContents = new JScrollPane();
		jPanelContents = new JPanel();
		jPanelVerticalIcons = new JPanel();
		jLabelStart = new JLabel();
		jPanelVerticalLinks = new JPanel();
		jLabelHorizontalLink = new JLabel();
		jLabelVGap = new JLabel();
		jPanelRoster = new JPanel();
		findMeRosterPanelInitial = new FindMeRosterPanel(this);
		
		jPanelBottom = new JPanel();
		jLayeredPane = new JLayeredPane();
		jButtonSave = new JButton();
		jLabelBottomBackground = new JLabel();

		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));

		jXPanel.setBackground(Color.white);
		jXPanel.setName("jXPanel"); // NOI18N

		jScrollPaneContents.setBackground(new Color(255, 255, 255));
		jScrollPaneContents.setBorder(null);
		jScrollPaneContents
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPaneContents.setOpaque(false);
		jScrollPaneContents.setDoubleBuffered(true);
		jScrollPaneContents.setName("jScrollPaneContents"); // NOI18N

		jPanelContents.setBackground(new Color(255, 255, 255));
		jPanelContents.setOpaque(false);
		jPanelContents.setName("jPanelContents"); // NOI18N

		jPanelVerticalIcons.setName("jPanelVerticalIcons"); // NOI18N
		jPanelVerticalIcons.setLayout(new VerticalLayout());

		ResourceMap resourceMap = Application.getInstance().getContext()
				.getResourceMap(FindMePanel.class);
		jLabelStart.setIcon(resourceMap.getIcon("jLabelStart.icon")); // NOI18N
		jLabelStart.setName("jLabelStart"); // NOI18N
		jPanelVerticalIcons.add(jLabelStart);

		jPanelVerticalLinks.setName("jPanelVerticalLinks"); // NOI18N
		jPanelVerticalLinks.setLayout(new VerticalLayout());

		jLabelHorizontalLink.setIcon(resourceMap
				.getIcon("jLabelHorizontalLink.icon")); // NOI18N
		jLabelHorizontalLink.setVerticalAlignment(SwingConstants.BOTTOM);
		jLabelHorizontalLink.setDoubleBuffered(true);
		jLabelHorizontalLink.setName("jLabelHorizontalLink"); // NOI18N
		jLabelHorizontalLink.setPreferredSize(new Dimension(25, 20));
		jPanelVerticalLinks.add(jLabelHorizontalLink);

		jLabelVGap.setName("findMeRosterPanelInitialVGap"); // NOI18N
		jPanelVerticalLinks.add(jLabelVGap);

		jPanelRoster.setName("jPanelRoster"); // NOI18N
		VerticalLayout verticalLayout1 = new VerticalLayout();
		verticalLayout1.setGap(8);
		jPanelRoster.setLayout(verticalLayout1);

		findMeRosterPanelInitial.setName(INITIAL_ROSTER_PANEL_NAME);
		jPanelRoster.add(findMeRosterPanelInitial);

		GroupLayout jPanelContentsLayout = new GroupLayout(jPanelContents);
		jPanelContents.setLayout(jPanelContentsLayout);
		jPanelContentsLayout.setHorizontalGroup(jPanelContentsLayout
				.createParallelGroup(Alignment.LEADING).addGroup(
						jPanelContentsLayout
								.createSequentialGroup()
								.addComponent(jPanelVerticalIcons,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0)
								.addComponent(jPanelVerticalLinks,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0)
								.addComponent(jPanelRoster,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(68, Short.MAX_VALUE)));
		jPanelContentsLayout
				.setVerticalGroup(jPanelContentsLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								jPanelContentsLayout
										.createSequentialGroup()
										.addGap(2, 2, 2)
										.addGroup(
												jPanelContentsLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																jPanelVerticalLinks,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jPanelRoster,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jPanelVerticalIcons,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		jScrollPaneContents.setViewportView(jPanelContents);

		jPanelBottom.setName("jPanelBottom"); // NOI18N
		jPanelBottom.setPreferredSize(new Dimension(100, 23));
		jPanelBottom
				.setLayout(new BoxLayout(jPanelBottom, BoxLayout.LINE_AXIS));

		jLayeredPane.setMinimumSize(new Dimension(355, 22));
		jLayeredPane.setName("jLayeredPane"); // NOI18N

		jButtonSave.setIcon(resourceMap.getIcon("jButtonSave.icon")); // NOI18N
		jButtonSave.setBorder(null);
		jButtonSave.setBorderPainted(false);
		jButtonSave.setContentAreaFilled(false);
		jButtonSave.setDoubleBuffered(true);
		jButtonSave.setFocusPainted(false);
		jButtonSave.setHideActionText(true);
		jButtonSave.setName("jButtonSave"); // NOI18N
		jButtonSave.setBounds(5, 3, 38, 18);
		jLayeredPane.add(jButtonSave, JLayeredPane.DEFAULT_LAYER);

		jLabelBottomBackground.setIcon(resourceMap
				.getIcon("jLabelBottomBackground.icon")); // NOI18N
		jLabelBottomBackground.setDoubleBuffered(true);
		jLabelBottomBackground.setName("jLabelBottomBackground"); // NOI18N
		jLabelBottomBackground.setBounds(0, 0, 355, 22);
		jLayeredPane.add(jLabelBottomBackground, JLayeredPane.DEFAULT_LAYER);

		jPanelBottom.add(jLayeredPane);

		GroupLayout jXPanelLayout = new GroupLayout(jXPanel);
		jXPanel.setLayout(jXPanelLayout);
		jXPanelLayout.setHorizontalGroup(jXPanelLayout
				.createParallelGroup(Alignment.LEADING)
				.addComponent(jPanelBottom, GroupLayout.DEFAULT_SIZE, 356,
						Short.MAX_VALUE)
				.addComponent(jScrollPaneContents, GroupLayout.DEFAULT_SIZE,
						356, Short.MAX_VALUE));
		jXPanelLayout.setVerticalGroup(jXPanelLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				jXPanelLayout
						.createSequentialGroup()
						.addComponent(jScrollPaneContents,
								GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
						.addGap(1, 1, 1)
						.addComponent(jPanelBottom, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(jXPanel, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(jXPanel, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
	}

	public FindMeRosterPanel getFindMeRosterPanelInitial() {
		return findMeRosterPanelInitial;
	}

	public void setCurrentUserPanel(FindMeMiniPanel currentUserPanel) {
		this.currentUserPanel = currentUserPanel;
	}
}
