package org.ezuce.common.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;

public class EzuceSearchPanel extends JPanel {
	private static final long serialVersionUID = -4388482651612022133L;

	private final ImageIcon bgLeft;
	private final ImageIcon bgCenter;
	private final ImageIcon bgRight;
	private final ImageIcon bgButton;
	protected final JTextField field;

	public EzuceSearchPanel() {
		setLayout(new GridBagLayout());
		bgLeft = new ImageIcon(getClass().getClassLoader().getResource("resources/images/search_left.png"));
		bgCenter = new ImageIcon(getClass().getClassLoader().getResource("resources/images/search_center.png"));
		bgRight = new ImageIcon(getClass().getClassLoader().getResource("resources/images/search_right.png"));
		bgButton = new ImageIcon(getClass().getClassLoader().getResource("resources/images/search_button.png"));
		field = new JTextField(Res.getString("find.contact"));

		initComponents();
	}

	private void initComponents() {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.insets = new Insets(10, 10, 9, 2);
		final JLabel searchButton = new JLabel(bgButton);
		searchButton.setPreferredSize(new Dimension(20, 20));
		searchButton.setMinimumSize(new Dimension(20, 20));
		searchButton.setSize(new Dimension(20, 20));
		searchButton.setFocusable(false);
		add(searchButton, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 3, 0, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		field.setFont(new Font("Arial", Font.PLAIN, 14));
		field.setBorder(BorderFactory.createEmptyBorder());
		field.setOpaque(false);
		field.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				field.setText(null);
				field.removeMouseListener(this);
			}
		});
		field.addKeyListener(new SearchListener());
		add(field, gbc);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bgLeft.getImage(), 0, 0, bgLeft.getIconWidth(), getHeight(), 0, 0, bgLeft.getIconWidth(),
				bgLeft.getIconHeight(), null);
		g.drawImage(bgCenter.getImage(), bgLeft.getIconWidth(), 0, getWidth() - bgRight.getIconWidth(), getHeight(), 0,
				0, bgCenter.getIconWidth(), bgCenter.getIconHeight(), null);
		g.drawImage(bgRight.getImage(), getWidth() - bgRight.getIconWidth(), 0, getWidth(), getHeight(), 0, 0,
				bgRight.getIconWidth(), bgRight.getIconHeight(), null);
	}

	private class SearchListener extends KeyAdapter {
		public SearchListener() {
			// nothing
		}

		@Override
		public void keyReleased(KeyEvent e) {
			final JTextField searchField = (JTextField) e.getSource();
			final String text = searchField.getText();

			final List<ContactGroup> groups = SparkManager.getContactList().getContactGroups();
			for (final ContactGroup group : groups) {
				if (group instanceof EzuceContactGroup) {
					final EzuceContactGroup ezuceGroup = (EzuceContactGroup) group;
					for (final ContactItem item : group.getContactItems()) {
						final boolean visible = matches(item, text);
						ezuceGroup.setVisibility(item, visible);
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
						|| caseInsensitiveMatch(ci.getDisplayName(), lowerCaseText);
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
	}
}
