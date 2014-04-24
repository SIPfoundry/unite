package org.ezuce.common;

import static org.ezuce.common.IOUtils.closeStreamQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.preference.EzucePreferenceDialog;
import org.ezuce.common.preference.local.EzuceLocalPreference;
import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.common.preference.sound.EzuceSoundPreference;
import org.ezuce.common.preference.sound.EzuceSoundPreferences;
import org.ezuce.common.preference.theme.EzuceThemePreference;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.EzuceLoginDialog;
import org.ezuce.common.ui.EzuceTabPanel;
import org.ezuce.common.ui.EzuceThemePanel;
import org.ezuce.media.manager.UIMediaManager;
import org.jivesoftware.Spark;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spark.util.UIComponentRegistry;
import org.jivesoftware.spark.util.WinRegistry;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class Commons implements Plugin, ConfigureVoice {
	private PrintStream consoleLogStream;

	public Commons() {
		// turn on antialiased text throughout ezuce plugins
		System.setProperty("swing.aatext", "true");
		redirectConsoleLoggingToFile();
		UIComponentRegistry.registerLocalPreference(EzuceLocalPreference.class);
		UIComponentRegistry
				.registerLocalPreferences(EzuceLocalPreferences.class);
		// We had to remove old LocalPreferences instance here in order to
		// reload preferences and use our EzuceLocalPreferences
		SettingsManager.clean();
		UIComponentRegistry.registerWorkspaceTabPanel(EzuceTabPanel.class);
		UIComponentRegistry.registerLoginDialog(EzuceLoginDialog.class);
		UIComponentRegistry.registerThemePanel(EzuceThemePanel.class);
		UIComponentRegistry.registerButtonFactory(EzuceButtonFactory.class);
		UIComponentRegistry
				.registerConferenceServices(EzuceConferenceServices.class);
		UIComponentRegistry
				.registerPreferenceDialog(EzucePreferenceDialog.class);
		UIComponentRegistry.registerThemePreference(EzuceThemePreference.class);
		UIComponentRegistry.registerSoundPreference(EzuceSoundPreference.class);
		UIComponentRegistry
				.registerSoundPreferences(EzuceSoundPreferences.class);

		try {
			// Set System L&F
			boolean linux = System.getProperty("os.name").contains("Linux");
			// on Linux, Metal is returned instead of GTK
			String lafName = "";

			if (linux) {
				for (LookAndFeelInfo info : UIManager
						.getInstalledLookAndFeels()) {
					if (info.getName().contains("GTK")) {
						lafName = info.getClassName();
						break;
					}
				}
				// in case GTL LaF is not available
				if (lafName.isEmpty()) {
					lafName = UIManager.getSystemLookAndFeelClassName();
				}
			} else {
				lafName = UIManager.getSystemLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lafName);
		} catch (Exception e) {
			Log.error("Error setting system LnF: " + e.getMessage());
		}			
	}

	@Override
	public void initialize() {
		// store the current user for uninstall purposes
		if (Spark.isWindows()) {
			Log.debug("initialize plugin");
			try {
				String configFolder = Spark.getSparkUserHome();
				String usersConfigFilePath = WinRegistry.readString(
						WinRegistry.HKEY_LOCAL_MACHINE,
						"Software\\eZuce\\Verge", "AllUsersAppDataPath");
				String userName = System.getProperty("user.name");

				try {
					Properties prop = new Properties();
					InputStream is = new FileInputStream(usersConfigFilePath);
					prop.loadFromXML(is);
					closeStreamQuietly(is);

					OutputStream os = new FileOutputStream(usersConfigFilePath);
					prop.setProperty(userName, configFolder);
					prop.storeToXML(os, userName);
					closeStreamQuietly(os);
				} catch (Exception e) {
					Log.error("Error reading from registry AllUsersAppDataPath "
							+ e.getMessage());
				}
				// Initialize vcard loader
				AsyncLoader.getInstance();
				
			} catch (Exception ex) {
				Log.error("Exception during initialization", ex);
			}
		}

		try {
			VoiceUtil.configure(this);
			// reload conferences
			RestManager.getInstance().getConferences(false);
		} catch (Exception e) {
			Log.error("Exception during configuration ", e);
		}
	}
	
	private void redirectConsoleLoggingToFile() {
		PrintStream savedOutStream = System.out;
		PrintStream savedErrStream = System.err;

		try {
			consoleLogStream = new PrintStream(new FileOutputStream(new File(
					Spark.getLogDirectory(), "console.log"), true));
			System.setOut(consoleLogStream);
			System.setErr(consoleLogStream);
		} catch (FileNotFoundException e) {
			System.setOut(savedOutStream);
			System.setErr(savedErrStream);
			Log.error(
					"Error while setting System.out or System.err"
							+ e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void shutdown() {
		closeStreamQuietly(consoleLogStream);
	}

	@Override
	public boolean canShutDown() {
		return false;
	}

	@Override
	public void uninstall() {
		// nothing to do
	}

	@Override
	public void configureVoiceEnabled() throws Exception {
		// Init needed audio panels
		UIMediaManager.getInstance();
	}

	@Override
	public void configureVoiceDisabled() {

	}
}
