package com.ezuce.preferences;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;

import com.ezuce.MainWindow;
import com.ezuce.preferences.category.Preference;
import com.ezuce.util.WidgetBuilder;

/**
 * @author Vyacheslav Durin
 * 
 *         Mar 25, 2013
 * @version 0.1
 */
public class EzucePreferenceDialog extends PreferenceDialog {

	private static final Color BG_PREFERENCE_CONTENT_TITLE = Color
			.decode("#99D4E5");
	private static final Dimension SIZE = new Dimension(800, 600);
	private static final Insets INSETS_RIGHT_PANEL = new Insets(20, 20, 20, 20);
	private static final Font FONT_TITLE = new Font("Droid Sans", Font.PLAIN,
			14);
	private static final Color COLOR_TITLE_FONT = Color.BLACK;
	private static final Font FONT_CONTENT = FONT_TITLE;
	private static final Color COLOR_CONTENT = COLOR_TITLE_FONT;

	// components
	private JFrame preferenceDialog;
	private JButton btnApply;
	private JButton btnSave;
	private JButton btnClose;

	//
	private JLabel titleLabel;
	private JPanel preferenceContentPanel;
	private Preference currentPreference;

	private EzucePreferenceList preferencesList;
	private DefaultListModel prefCategories = new DefaultListModel();

	public EzucePreferenceDialog() {
		preferenceDialog = new JFrame();
		preferenceDialog.setVisible(false);
		preferenceDialog.setSize(SIZE);
		preferenceDialog.setResizable(false);
		// TODO: remove
		preferenceDialog
				.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initComponents();
	}

	@Override
	public void invoke(JFrame parentFrame, Iterator<Preference> preferences,
			Preference selectedPref) {

		PreferenceDataBuilder.buildPreferensCategories(preferences,
				prefCategories);
		preferencesList.setSelectedValue(selectedPref);
		showDialog();
	};

	@Override
	public Container getDialog() {
		return preferenceDialog;
	}

	private void showDialog() {
		preferenceDialog.setLocationRelativeTo(MainWindow.getInstance());
		preferenceDialog.setVisible(true);
		preferenceDialog.toFront();
	}

	private void hideDialog() {
		preferenceDialog.setVisible(false);
		preferenceDialog.dispose();
	}

	private void initComponents() {
		// create
		btnApply = WidgetBuilder.createJButton("Apply");
		btnSave = WidgetBuilder.createJButton("Save");
		btnClose = WidgetBuilder.createJButton("Close");

		// title
		titleLabel = new JLabel();
		titleLabel.setFont(FONT_TITLE);
		titleLabel.setForeground(COLOR_TITLE_FONT);

		// content
		preferenceContentPanel = new JPanel();
		preferenceContentPanel.setLayout(new GridLayout(1, 1));
		preferenceContentPanel.setFont(FONT_CONTENT);
		preferenceContentPanel.setForeground(COLOR_CONTENT);

		// init categories

		preferencesList = new EzucePreferenceList(prefCategories) {
			private static final long serialVersionUID = 2172141093926058510L;

			public void valueChanged(ListSelectionEvent e) {
				updateContent(/* preferencesList */getSelectedValue());
			};
		};

		// apply actions
		btnApply.addActionListener(applyAction());
		btnSave.addActionListener(saveAction());
		btnClose.addActionListener(closeAction());

		// set up layout
		initLayout();
	}

	private void initLayout() {
		GridBagLayout layout = new GridBagLayout();
		preferenceDialog.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		preferenceDialog.add(preferencesList, c);

		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = INSETS_RIGHT_PANEL;
		preferenceDialog.add(getRightPanel(), c);
	}

	private void updateContent(Preference pref) {
		if (pref == null)
			return;

		// keep it for further use
		currentPreference = pref;

		// init title
		titleLabel.setIcon(pref.getIcon());
		titleLabel.setText(pref.getTitle());

		// init content
		preferenceContentPanel.removeAll();
		JComponent content = pref.getGUI();
		if (content == null) {
			content = new JPanel();
			content.add(new JLabel("Not implemented yet"));
		}

		// make sure that fonts are correct
		updateFonts(content);

		// update content
		preferenceContentPanel.add(content);
		preferenceContentPanel.revalidate();
		preferenceContentPanel.repaint();
	}

	private Component getRightPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0.1;
		panel.add(getTitlePanel(), c);

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(45, 0, 0, 0);
		panel.add(preferenceContentPanel, c);

		c.anchor = GridBagConstraints.PAGE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 0, 0, 0);
		panel.add(getButtonsPanel(), c);

		return panel;
	}

	private Component getButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(btnApply);
		panel.add(btnSave);
		panel.add(btnClose);
		return panel;
	}

	private Component getTitlePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(BG_PREFERENCE_CONTENT_TITLE);
		panel.add(titleLabel);
		return panel;
	}

	private void updateFonts(Container content) {
		for (Component c : content.getComponents()) {
			if (c instanceof Container) {
				Container cont = (Container) c;
				updateFonts(cont);
			}
			c.setFont(FONT_CONTENT);
			c.setForeground(COLOR_CONTENT);
		}
	}

	private void save() {
		if (currentPreference == null)
			throw new IllegalStateException("Current preference cannot be null");

		currentPreference.commit();
	}

	private ActionListener closeAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// apply settings
				hideDialog();
			}
		};
	}

	private ActionListener saveAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				hideDialog();
			}
		};
	}

	private ActionListener applyAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		};
	}

}