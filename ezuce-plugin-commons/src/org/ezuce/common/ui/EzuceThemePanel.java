package org.ezuce.common.ui;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.ui.themes.ThemePanel;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceThemePanel extends ThemePanel {
	private static final long serialVersionUID = 5118873458312901591L;

	private Component chatRoomFontNameBox;
	private Component contactListFontNameBox;

	private ComboBoxModel chatRoomFontModel;
	private ComboBoxModel contactListFontModel;

	private JLabel chatRoomFontFamilyLabel;
	private JLabel contactListFontFamilyLabel;

	private EzuceLocalPreferences pref = (EzuceLocalPreferences) SettingsManager.getLocalPreferences();

	public EzuceThemePanel() {

		chatRoomFontFamilyLabel = new JLabel(
				Res.getString("preferences.theme.chat.room.font.name"));
		contactListFontFamilyLabel = new JLabel(
				Res.getString("preferences.theme.contact.list.font.name"));

		chatRoomFontModel = new DefaultComboBoxModel(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		contactListFontModel = new DefaultComboBoxModel(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

		chatRoomFontNameBox = new JComboBox(chatRoomFontModel);
		contactListFontNameBox = new JComboBox(contactListFontModel);

		remove(getLookandfeel());
		remove(getLookandfeelLabel());
		remove(getLookandfeelpreview());

		remove(getUseTabsForConference());
		remove(getShowAvatarsBox());
		remove(getAvatarSizeLabel());
		remove(getAvatarSizeField());

		// /
		add(chatRoomFontFamilyLabel, new GridBagConstraints(0, 13, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		add(chatRoomFontNameBox, new GridBagConstraints(1, 13, 2, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 25, 0));

		// /
		add(contactListFontFamilyLabel, new GridBagConstraints(0, 14, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		add(contactListFontNameBox, new GridBagConstraints(1, 14, 2, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 25, 0));

		chatRoomFontModel.setSelectedItem(pref.getChatRoomFontName());
		contactListFontModel.setSelectedItem(pref.getContactListFontName());
	}

	public String getContactListFontName() {
		return (String) contactListFontModel.getSelectedItem();
	}

	public String getChatRoomFontName() {
		return (String) chatRoomFontModel.getSelectedItem();
	}
}
