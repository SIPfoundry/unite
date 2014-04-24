/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;

import org.ezuce.common.preference.local.EzuceLocalPreferences;
import org.ezuce.media.ui.WidgetBuilder;
import org.jivesoftware.resource.Default;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.spark.preference.Preference;
import org.jivesoftware.spark.util.ResourceUtils;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 * 
 */
public class EzuceNotificationPreference extends Observable implements Preference {

	public static final String NAMESPACE = "http://www.jivesoftware.org/spark/notifications";
	private JPanel gui;
	private JCheckBox enablePopup;
	private JCheckBox toFront;
	private JCheckBox notifyIfGoOffline;
	private JCheckBox notifyIfComeOnline;
	private JCheckBox showNewMessageInTray;
	private JCheckBox showTypingInTray;
	private JCheckBox checkUpdates;
	private JLabel labelDuration;
	private JSpinner fieldDuration;
	private JRadioButton positionCenter;
	private JRadioButton positionNorthEast;
	private JRadioButton positionSouthWest;

	public EzuceNotificationPreference() {
		disableSparkToaster();

		// enable
		enablePopup = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(enablePopup,
				Res.getString("checkbox.show.toaster"));

		// to front
		toFront = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(toFront,
				Res.getString("checkbox.window.to.front"));
		toFront.setEnabled(false);

		// goes offline
		notifyIfGoOffline = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(notifyIfGoOffline,
				Res.getString("checkbox.notify.user.goes.offline"));

		// come online
		notifyIfComeOnline = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(notifyIfComeOnline,
				Res.getString("checkbox.notify.user.comes.online"));

		// new msg
		showNewMessageInTray = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(showNewMessageInTray,
				Res.getString("checkbox.notify.systemtray"));

		// typing
		showTypingInTray = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(showTypingInTray,
				Res.getString("checkbox.notify.typing.systemtray"));

		// updates
		checkUpdates = WidgetBuilder.createJCheckbox();
		ResourceUtils.resButton(checkUpdates,
				Res.getString("menuitem.check.for.updates"));

		// duration
		labelDuration = WidgetBuilder.createJLabel(Res
				.getString("text.label.notify.duration"));
		fieldDuration = WidgetBuilder.createJNumberSpinner(5, 3, 60, 1);

		// positions
		positionCenter = WidgetBuilder.createJRadioButton(Res
				.getString("radiobutton.position.center"));
		positionNorthEast = WidgetBuilder.createJRadioButton(Res
				.getString("radiobutton.position.northwest"));
		positionSouthWest = WidgetBuilder.createJRadioButton(
				Res.getString("radiobutton.position.southwest"), true);

		// create panel
		gui = new JPanel();
		gui.setLayout(new GridLayout(10, 2));
		gui.setBorder(BorderFactory.createTitledBorder(Res
				.getString("group.notification.options")));

		gui.add(enablePopup);
		gui.add(toFront);
		gui.add(notifyIfGoOffline);
		gui.add(notifyIfComeOnline);
		gui.add(showNewMessageInTray);
		gui.add(showTypingInTray);
		if (!Default.getBoolean(Default.DISABLE_UPDATES))
			gui.add(checkUpdates);

		JPanel durationPanel = new JPanel();
		durationPanel.add(labelDuration);
		durationPanel.add(fieldDuration);
		gui.add(durationPanel);

		ButtonGroup positionGroup = new ButtonGroup();
		JPanel positionPanel = new JPanel(new GridLayout(1, 0));
		positionPanel.setBorder(BorderFactory.createTitledBorder(Res
				.getString("radiobutton.notification.position")));
		positionPanel.setMinimumSize(new Dimension(100, 100));
		positionPanel.setPreferredSize(new Dimension(100, 100));
		positionPanel.setMaximumSize(new Dimension(100, 100));
		positionGroup.add(positionCenter);
		positionGroup.add(positionNorthEast);
		positionGroup.add(positionSouthWest);
		positionPanel.add(positionCenter);
		positionPanel.add(positionNorthEast);
		positionPanel.add(positionSouthWest);
		gui.add(positionPanel);
	}

	@Override
	public String getTitle() {
		return Res.getString("title.notifications");
	}

	@Override
	public Icon getIcon() {
		return SparkRes.getImageIcon("NOTIFICATION_ICON");
	}

	@Override
	public String getTooltip() {
		return Res.getString("tooltip.notifications");
	}

	@Override
	public String getListName() {
		return Res.getString("title.notifications");
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public JComponent getGUI() {
		return gui;
	}

	@Override
	public void load() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				EzuceLocalPreferences localPreferences = (EzuceLocalPreferences) SettingsManager
						.getLocalPreferences();
				boolean enabled = localPreferences.isShowNotificationPopup();
				boolean windowFocus = localPreferences.getWindowTakesFocus();
				boolean offlineNotification = localPreferences
						.isOfflineNotificationsOn();
				boolean onlineNotification = localPreferences
						.isOnlineNotificationsOn();
				boolean betaChecking = localPreferences.isBetaCheckingEnabled();
				boolean typingNotification = localPreferences
						.isTypingNotificationShown();
				boolean systemTrayNotification = localPreferences
						.isSystemTrayNotificationEnabled();
				int notificationDuration = localPreferences
						.getNotificationDurationTime();
				NotificationPosition notificationPosition = NotificationPosition
						.fromInt(localPreferences.getNotificationPosition());

				enablePopup.setSelected(enabled);
				toFront.setSelected(windowFocus);
				notifyIfGoOffline.setSelected(offlineNotification);
				notifyIfComeOnline.setSelected(onlineNotification);
				checkUpdates.setSelected(betaChecking);
				showNewMessageInTray.setSelected(systemTrayNotification);
				showTypingInTray.setSelected(typingNotification);
				fieldDuration.setValue(notificationDuration);
				setNotificationPosition(notificationPosition);

			}
		});
	}

	@Override
	public void commit() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				EzuceLocalPreferences localPreferences = (EzuceLocalPreferences) SettingsManager
						.getLocalPreferences();

				boolean enabledPopupSelected = enablePopup.isSelected();
				boolean windowFocusSelected = toFront.isSelected();
				boolean notifyOfflineSelected = notifyIfGoOffline.isSelected();
				boolean notifyOnlineSelected = notifyIfComeOnline.isSelected();
				boolean checkUpdateSelected = checkUpdates.isSelected();
				boolean showNewMessageSelected = showNewMessageInTray
						.isSelected();
				boolean showTypingSelected = showTypingInTray.isSelected();
				Integer durationValue = (Integer) fieldDuration.getValue();

				// when windowFocus is selected the systemtraynotification
				// doesn't work --> disable it
				if (windowFocusSelected) {
					showNewMessageSelected = false;
					enablePopup.setEnabled(false);
				} else
					enablePopup.setEnabled(true);

				if (showNewMessageSelected) {
					windowFocusSelected = false;
					toFront.setEnabled(false);
				} else
					toFront.setEnabled(true);

				localPreferences.setShowNotificationPopup(enabledPopupSelected);

				localPreferences.setWindowTakesFocus(windowFocusSelected);
				localPreferences.setOfflineNotifications(notifyOfflineSelected);
				localPreferences.setOnlineNotifications(notifyOnlineSelected);
				localPreferences.setCheckForBeta(checkUpdateSelected);
				localPreferences
						.setSystemTrayNotificationEnabled(showNewMessageSelected);
				localPreferences.setTypingNotificationOn(showTypingSelected);
				localPreferences.setNotificationDurationTime(durationValue);
				saveNotificationPosition(localPreferences);

				SettingsManager.saveSettings();
			}
		});
	}

	@Override
	public boolean isDataValid() {
		return true;
	}

	@Override
	public String getErrorMessage() {
		return "";
	}

	@Override
	public Object getData() {
		return SettingsManager.getLocalPreferences();
	}

	@Override
	public void shutdown() {
		commit();
	}

	private void setNotificationPosition(
			NotificationPosition notificationPosition) {
		switch (notificationPosition) {
		case CENTER:
			positionCenter.setSelected(true);
			break;

		case NORTHEAST:
			positionNorthEast.setSelected(true);
			break;

		case SOUTHEAST:
			positionSouthWest.setSelected(true);
			break;

		default:
			break;
		}
	}

	private void saveNotificationPosition(EzuceLocalPreferences localPreferences) {
		if (positionCenter.isSelected())
			localPreferences
					.setNotificationPosition(NotificationPosition.CENTER
							.toInt());

		else if (positionNorthEast.isSelected())
			localPreferences
					.setNotificationPosition(NotificationPosition.NORTHEAST
							.toInt());

		else
			localPreferences
					.setNotificationPosition(NotificationPosition.SOUTHEAST
							.toInt());
	}

	@Override
	public Icon getActiveIcon() {
		return SparkRes.getImageIcon("NOTIFICATION_ICON_ON");
	}

	@Override
	public Icon getTitleIcon() {
		return SparkRes.getImageIcon("NOTIFICATION_ICON_TITLE");
	}

	private void disableSparkToaster() {
		LocalPreferences localPreferences = SettingsManager
				.getLocalPreferences();
		if (localPreferences.getShowToasterPopup()) {
			localPreferences.setShowToasterPopup(false);
			SettingsManager.saveSettings();
		}
	}

}
