package org.ezuce.common.preference.local;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.sparkimpl.settings.local.LocalPreference;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceLocalPreference extends LocalPreference {

	private EzuceLocalPreferencePanel panel;
	private EzuceLocalPreferences preferences;
	private String errorMessage = "Error";

	/**
	 * Initalize and load local preference.
	 */
	public EzuceLocalPreference() {
		preferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();
	}

	public String getTitle() {
		return Res.getString("title.login.settings");
	}

	public String getListName() {
		return Res.getString("title.login");
	}

	public String getTooltip() {
		return Res.getString("title.login.settings");
	}

	public Icon getIcon() {
		return SparkRes.getImageIcon(SparkRes.LOGIN_KEY_IMAGE);
	}

	public void load() {
		preferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();
	}

	public void commit() {
		getData();

		SettingsManager.saveSettings();

		// if language has been changed then open a dialog to confirm that spark
		// should be restarted immediately.
		if (!preferences.getLanguage().equals(Locale.getDefault().toString())) {
			LanguageComboBox.warn();
		}
	}

	public Object getData() {
		preferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();
		preferences.setAutoLogin(panel.getAutoLogin());
		preferences.setTimeOut(Integer.parseInt(panel.getTimeout()));
		preferences.setXmppPort(Integer.parseInt(panel.getPort()));
		preferences.setSavePassword(panel.isSavePassword());
		preferences.setIdleOn(panel.isIdleOn());
		preferences.setIdleTime(Integer.parseInt(panel.getIdleTime()));
		preferences.setStartedHidden(panel.startInSystemTray());
		preferences.setStartOnStartup(panel.startOnStartup());
		preferences.setIdleMessage(panel.getIdleMessage());
		preferences.setUsingSingleTrayClick(panel.useSingleClickInTray());
		preferences.setLanguage(panel.getLanguage().toString());
		return preferences;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isDataValid() {
		preferences.setTimeOut(Integer.parseInt(panel.getTimeout()));
		preferences.setXmppPort(Integer.parseInt(panel.getPort()));

		try {
			Integer.parseInt(panel.getTimeout());
			Integer.parseInt(panel.getPort());
			Integer.parseInt(panel.getIdleTime());
		} catch (Exception ex) {
			errorMessage = Res.getString("message.specify.valid.time.error");
			return false;
		}

		int timeOut = Integer.parseInt(panel.getTimeout());
		if (timeOut < 5) {
			errorMessage = Res.getString("message.timeout.error");
			return false;
		}

		return true;
	}

	public JComponent getGUI() {
		if (panel == null)
			panel = new EzuceLocalPreferencePanel();

		return panel;
	}

	public String getNamespace() {
		return "LOGIN";
	}

	public void shutdown() {
		// Commit to file.
		SettingsManager.saveSettings();
	}

	@Override
	public Icon getActiveIcon() {
		return SparkRes.getImageIcon("LOGIN_KEY_IMAGE_ON");
	}

	@Override
	public Icon getTitleIcon() {
		return SparkRes.getImageIcon("LOGIN_KEY_IMAGE_TITLE");
	}

}
