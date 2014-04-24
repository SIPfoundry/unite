package org.ezuce.common.preference.local;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jivesoftware.Spark;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.component.VerticalFlowLayout;
import org.jivesoftware.spark.util.ResourceUtils;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferencePanel;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * UI for editing Local Preferences.
 */
public class EzuceLocalPreferencePanel extends LocalPreferencePanel {

	private static final long serialVersionUID = -1675058807882383560L;
	private JLabel portLabel = new JLabel();
	private JLabel idleLabel = new JLabel();
	private JLabel timeOutLabel = new JLabel();
	private JLabel idleStatusLabel = new JLabel();

	private JTextField portField = new JTextField();
	private JTextField timeOutField = new JTextField();
	private JTextField idleField = new JTextField();
	private JTextField idleStatusText;

	private JCheckBox autoLoginBox = new JCheckBox();
	private JCheckBox savePasswordBox = new JCheckBox();
	private JCheckBox idleBox = new JCheckBox();
	private JCheckBox launchOnStartupBox = new JCheckBox();
	private JCheckBox startMinimizedBox = new JCheckBox();
	private JCheckBox useSingleTrayClick = new JCheckBox();

	private JLabel languagesLabel = new JLabel();
	private LanguageComboBox languagesBox = new LanguageComboBox();

	/**
	 * Construct Local Preference UI.
	 */
	public EzuceLocalPreferencePanel() {

		removeAll();
		setLayout(new VerticalFlowLayout());

		// Load local localPref
		EzuceLocalPreferences preferences = (EzuceLocalPreferences) SettingsManager
				.getLocalPreferences();
		portField.setText(Integer.toString(preferences.getXmppPort()));
		timeOutField.setText(Integer.toString(preferences.getTimeOut()));
		autoLoginBox.setSelected(preferences.isAutoLogin());
		savePasswordBox.setSelected(preferences.isSavePassword());
		startMinimizedBox.setSelected(preferences.isStartedHidden());
		useSingleTrayClick.setSelected(preferences.isUsingSingleTrayClick());

		idleStatusText = new JTextField(preferences.getIdleMessage());

		savePasswordBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autoLoginBox.setEnabled(savePasswordBox.isSelected());
				if (!savePasswordBox.isSelected()) {
					autoLoginBox.setSelected(false);
				}
			}
		});

		autoLoginBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (autoLoginBox.isSelected()) {
					savePasswordBox.setSelected(true);
				}
			}
		});

		idleBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idleField.setEnabled(idleBox.isSelected());
			}
		});

		idleBox.setSelected(preferences.isIdleOn());
		idleField.setText(Integer.toString(preferences.getIdleTime()));

		final JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(BorderFactory.createTitledBorder(Res
				.getString("group.login.information")));

		ResourceUtils.resLabel(languagesLabel, languagesBox,
				Res.getString("menuitem.languages") + ":");

		ResourceUtils.resLabel(portLabel, portField,
				Res.getString("label.xmpp.port") + ":");
		ResourceUtils.resLabel(timeOutLabel, timeOutField,
				Res.getString("label.response.timeout") + ":");

		ResourceUtils.resLabel(idleStatusLabel, idleStatusText,
				Res.getString("label.time.till.idlemessage") + ":");

		ResourceUtils.resButton(autoLoginBox,
				Res.getString("checkbox.auto.login"));
		ResourceUtils.resButton(savePasswordBox,
				Res.getString("checkbox.save.password"));
		ResourceUtils.resLabel(idleLabel, idleField,
				Res.getString("label.time.till.idle") + ":");
		ResourceUtils
				.resButton(idleBox, Res.getString("checkbox.idle.enabled"));

		ResourceUtils.resButton(launchOnStartupBox,
				Res.getString("checkbox.launch.on.startup"));
		ResourceUtils.resButton(startMinimizedBox,
				Res.getString("checkbox.start.in.tray"));
		ResourceUtils.resButton(useSingleTrayClick,
				Res.getString("checkbox.click.single.tray"));

		inputPanel.add(languagesLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		inputPanel.add(languagesBox, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		inputPanel.add(portLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		inputPanel.add(portField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		inputPanel.add(timeOutLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(timeOutField, new GridBagConstraints(1, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(idleLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(idleField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(idleStatusLabel, new GridBagConstraints(0, 4, 1, 1, 1.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(idleStatusText, new GridBagConstraints(1, 4, 1, 1, 1.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(idleBox, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(savePasswordBox, new GridBagConstraints(0, 6, 2, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
		inputPanel.add(autoLoginBox, new GridBagConstraints(0, 7, 2, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));

		if (Spark.isWindows()) {
			inputPanel.add(launchOnStartupBox, new GridBagConstraints(0, 8, 2,
					1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50,
					0));
			launchOnStartupBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setStartOnStartup(launchOnStartupBox.isSelected());
				}
			});

			launchOnStartupBox.setSelected(preferences.getStartOnStartup());
		}

		inputPanel.add(startMinimizedBox, new GridBagConstraints(0, 9, 2, 1,
				0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));

		inputPanel.add(new JLabel(), new GridBagConstraints(0, 10, 2, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 50, 0));

		add(inputPanel);
	}

	/**
	 * Sets the XMPP port to comminucate on.
	 * 
	 * @param port
	 *            the XMPP port to communicate on.
	 */
	public void setPort(String port) {
		portField.setText(port);
	}

	/**
	 * Return the XMPP Port to communicate on.
	 * 
	 * @return the XMPP Port to communicate on.
	 */
	public String getPort() {
		return portField.getText();
	}

	/**
	 * Sets the XMPP Timeout(in seconds).
	 * 
	 * @param timeOut
	 *            the XMPP Timeout(in seconds).
	 */
	public void setTimeOut(String timeOut) {
		timeOutField.setText(timeOut);
	}

	/**
	 * Return the XMPP Timeout variable.
	 * 
	 * @return the XMPP Timeout variable.
	 */
	public String getTimeout() {
		return timeOutField.getText();
	}

	/**
	 * Sets Auto Login on and off.
	 * 
	 * @param auto
	 *            true if Auto Login is on.
	 */
	public void setAutoLogin(boolean auto) {
		autoLoginBox.setSelected(auto);
	}

	/**
	 * Return true if Auto Login is on.
	 * 
	 * @return true if Auto Login is on.
	 */
	public boolean getAutoLogin() {
		return autoLoginBox.isSelected();
	}

	/**
	 * Set true if the password should be encoded and saved.
	 * 
	 * @param save
	 *            true if the password should be encoded and saved.
	 */
	public void setSavePassword(boolean save) {
		savePasswordBox.setSelected(save);
	}

	/**
	 * Return true if the password should be saved.
	 * 
	 * @return true if the password should be saved.
	 */
	public boolean isSavePassword() {
		return savePasswordBox.isSelected();
	}

	/**
	 * Returns true if IDLE is on.
	 * 
	 * @return true if IDLE is on.
	 */
	public boolean isIdleOn() {
		return idleBox.isSelected();
	}

	/**
	 * Sets the IDLE on or off.
	 * 
	 * @param on
	 *            true if IDLE should be on.
	 */
	public void setIdleOn(boolean on) {
		idleBox.setSelected(on);
	}

	/**
	 * Sets the Idle Time in minutes.
	 * 
	 * @param time
	 *            the Idle time in minutes.
	 */
	public void setIdleTime(int time) {
		String idleTime = Integer.toString(time);
		idleField.setText(idleTime);
	}

	/**
	 * Return the time to IDLE.
	 * 
	 * @return the time to IDLE.
	 */
	public String getIdleTime() {
		return idleField.getText();
	}

	public void startInSystemTray(boolean startInTray) {
		startMinimizedBox.setSelected(startInTray);
	}

	public boolean startInSystemTray() {
		return startMinimizedBox.isSelected();
	}

	public void useSingleClickInTray(boolean clickInTray) {
		useSingleTrayClick.setSelected(clickInTray);
	}

	public boolean useSingleClickInTray() {
		return useSingleTrayClick.isSelected();
	}

	public boolean startOnStartup() {
		return launchOnStartupBox.isSelected();
	}

	public void setStartOnStartup(boolean startup) {
		launchOnStartupBox.setSelected(startup);
	}

	public String getIdleMessage() {
		return idleStatusText.getText();
	}

	public void setIdleMessage(String text) {
		idleStatusText.setText(text);
	}

	public void setLanguage(String language) {
		languagesBox.setSelectedLanguage(language);
	}

	public Locale getLanguage() {
		return (Locale) languagesBox.getSelectedItem();
	}

}
