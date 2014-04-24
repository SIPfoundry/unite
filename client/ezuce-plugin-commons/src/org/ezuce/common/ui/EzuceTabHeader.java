package org.ezuce.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXSearchField;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;

import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.CommandPanel;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.sparkimpl.plugin.gateways.transports.TransportUtils;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceTabHeader extends JPanel {
	private static final long serialVersionUID = 7838385757122114583L;

	public EzuceTabHeader() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		ButtonPanel buttons = new ButtonPanel();
		final CommandPanel cp = Workspace.getInstance().getCommandPanel();

		cp.setVisible(false);
		add(buttons);
		MouseListener listener = new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				boolean visible = ((JToggleButton) e.getSource()).isSelected();
				cp.setVisible(visible);
			}

		};
		buttons.setConnectListener(listener);

		if (TransportUtils.getTransports().isEmpty()) {
			buttons.getConnectButton().setVisible(false);
		}

		add(cp);
		final EzuceSearchField jXSearchField = new EzuceSearchField();
		jXSearchField.setForeground(new Color(153, 153, 153));
		jXSearchField.setInstantSearchDelay(600);
		jXSearchField.setLayoutStyle(JXSearchField.LayoutStyle.MAC);
		jXSearchField.setPrompt(Res.getString("find.contact"));
		jXSearchField.setPromptFontStyle(Font.PLAIN);
		jXSearchField.setPromptForeground(new Color(204, 204, 204));
		jXSearchField.setUseNativeSearchFieldIfPossible(false);
		jXSearchField.setDoubleBuffered(true);
		jXSearchField.setFont(new Font("Arial", 0, 14));
		jXSearchField.setMargin(new Insets(2, 6, 2, 2));
		jXSearchField.setMinimumSize(new Dimension(320, 30));
		jXSearchField.setName("jXSearchField");
		jXSearchField.setOpaque(false);
		jXSearchField.setPreferredSize(new Dimension(215, 30));
		jXSearchField.setBorder(null);
		final ResourceMap rMap = org.jdesktop.application.Application
				.getInstance().getContext()
				.getResourceMap(EzuceSearchField.class);
		final Icon lensIcon = rMap.getImageIcon("searchField.icon");
		final JButton btn = jXSearchField.getFindButton();
		btn.setIcon(lensIcon);
		jXSearchField.addKeyListener(new SearchListener());		
		javax.swing.Action action = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        final String text = jXSearchField.getText();
		        search(text);
		    }
		};
		jXSearchField.setAction(action);
		add(jXSearchField);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Paint p = new GradientPaint(0, 0, new Color(232, 233, 233), 0,
				getHeight(), new Color(215, 226, 228));
		((Graphics2D) g).setPaint(p);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	
	
	public void search(final String text) {

		final List<ContactGroup> groups = SparkManager.getContactList()
				.getContactGroups();
		for (final ContactGroup group : groups) {
			if (group instanceof EzuceContactGroup) {
				final EzuceContactGroup ezuceGroup = (EzuceContactGroup) group;
				for (final ContactItem item : group.getContactItems()) {
					final boolean visible = matches(item, text);
					ezuceGroup.setVisibility(item, visible);
				}

				if (SettingsManager.getLocalPreferences()
						.isOfflineUsersShown())// if offline contacts are
												// visible, perform the
												// search through them, too:
				{
					for (final ContactItem item : group
							.getOfflineContacts()) {
						final boolean visible = matches(item, text);
						ezuceGroup.setVisibility(item, visible);
					}
				}
			}
		}
		SparkManager.getContactList().validate();
	}
	

	private boolean matches(ContactItem ci, String text) {
		boolean match;

		if (text == null || text.isEmpty()) {
			match = true;
		} else {
			final String lowerCaseText = text.toLowerCase();

			match = caseInsensitiveMatch(ci.getJID(), lowerCaseText)
					|| caseInsensitiveMatch(ci.getDisplayName(),
							lowerCaseText);
		}

		return match;
	}

	private boolean caseInsensitiveMatch(String s1, String s2) {
		boolean match = false;

		if (s1 != null) {
			String[] tokens = s1.split(" ");

			for (String token : tokens) {
				if (token.toLowerCase().startsWith(s2)) {
					match = true;
					break;
				}
			}
		}

		return match;
	}
	
	private class SearchListener extends KeyAdapter {
		public SearchListener() {
			// nothing		
		}
		
		@Override
		public void keyReleased(KeyEvent e){
			final JTextField searchField = (JTextField) e.getSource();
			final String text = searchField.getText();
			search(text);
		}
		
	}

}
