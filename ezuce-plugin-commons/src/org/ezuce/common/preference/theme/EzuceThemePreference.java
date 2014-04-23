package org.ezuce.common.preference.theme;

import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.common.ui.EzuceThemePanel;
import org.jivesoftware.spark.ui.themes.ColorSettingManager;
import org.jivesoftware.spark.ui.themes.MainThemePanel;
import org.jivesoftware.spark.ui.themes.ThemePreference;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceThemePreference extends ThemePreference {

	@Override
	public void commit() {
		super.commit();

		EzuceLocalPreferences pref = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();

		MainThemePanel panel = (MainThemePanel) getGUI();
		EzuceThemePanel themePanel = (EzuceThemePanel) panel.getThemePanel();

		pref.setContactListFontName(themePanel.getContactListFontName());
		pref.setChatRoomFontName(themePanel.getChatRoomFontName());

		ColorSettingManager.saveColorSettings();
		SettingsManager.saveSettings();
	}
}
