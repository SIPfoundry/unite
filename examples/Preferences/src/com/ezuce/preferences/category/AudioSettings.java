package com.ezuce.preferences.category;

import static com.ezuce.util.GraphicUtils.createImageIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jitsi.impl.neomedia.device.AudioSystem;
import org.jitsi.impl.neomedia.device.AudioSystem.DataFlow;
import org.jitsi.impl.neomedia.device.CaptureDeviceInfo2;
import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.audionotifier.SCAudioClip;

import com.ezuce.jitsi.CaptureDevice;
import com.ezuce.jitsi.UniteLibJitsiImpl;
import com.ezuce.util.Res;
import com.ezuce.util.WidgetBuilder;

public class AudioSettings extends Preference {

	private static final Dimension SIZE_COMBO_DEVICES = new Dimension(300, 30);
	/**  */
	private static final long serialVersionUID = -5172249977428979687L;
	private static final String iconName = "/resources/images/prefs/audio";
	private static final String TITLE = "Audio Settings";
	private JComboBox comboAudioSystem;
	private JComboBox comboAudioIn;
	private JComboBox comboAudioOut;
	private JComboBox comboNotifications;
	private JCheckBox checkNoiseCancel;
	private JCheckBox checkEchoCancel;
	private JButton btnAudioOutPlay;
	private JButton btnNotificationPlay;

	public AudioSettings() {
		super(TITLE, createImageIcon(iconName + "_on.png"),
				createImageIcon(iconName + "_off.png"),
				createImageIcon(iconName + ".png"));
	}

	@Override
	public JComponent getGUI() {

		// components
		// systems
		comboAudioSystem = WidgetBuilder.createComboBox(UniteLibJitsiImpl
				.getAvailableAudioSystems());
		comboAudioSystem.setPreferredSize(SIZE_COMBO_DEVICES);
		JLabel lblAudioSystem = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.system"));

		// in
		comboAudioIn = WidgetBuilder.createComboBox(UniteLibJitsiImpl
				.getAvailableAudioCaptureDevices());
		comboAudioIn.setPreferredSize(SIZE_COMBO_DEVICES);
		JLabel lblAudioIn = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.in"));

		// out
		comboAudioOut = WidgetBuilder.createComboBox(UniteLibJitsiImpl
				.getAvailablePlaybackDevices());
		comboAudioOut.setPreferredSize(SIZE_COMBO_DEVICES);
		JLabel lblAudioOut = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.out"));

		// notifications
		comboNotifications = WidgetBuilder.createComboBox(UniteLibJitsiImpl
				.getAvailableNotifyDevices());
		comboNotifications.setPreferredSize(SIZE_COMBO_DEVICES);
		JLabel lblNotifications = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.notifications"));

		// echo
		checkEchoCancel = WidgetBuilder.createCheckbox(Res
				.getString("preferences.audio.echo.cancellation"));
		checkEchoCancel.setSelected(UniteLibJitsiImpl.getDeviceConfiguration()
				.isEchoCancel());

		// denoise
		checkNoiseCancel = WidgetBuilder.createCheckbox(Res
				.getString("preferences.audio.noise.suppression"));
		checkNoiseCancel.setSelected(UniteLibJitsiImpl.getDeviceConfiguration()
				.isDenoise());

		// audio out play button
		btnAudioOutPlay = WidgetBuilder.createButton("/prefs/play_off.png",
				"/prefs/play_on.png");
		btnAudioOutPlay.addActionListener(audioOutPlayListener());

		// notifications play button
		btnNotificationPlay = WidgetBuilder.createButton("/prefs/play_off.png",
				"/prefs/play_on.png");
		btnNotificationPlay.addActionListener(audioNotificationPlayListener());

		// / layout
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		// 1
		Insets insets = new Insets(5, 5, 5, 5);
		panel.add(lblAudioSystem, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		panel.add(comboAudioSystem, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));

		// 2
		panel.add(lblAudioIn, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		panel.add(comboAudioIn, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));

		// 3
		panel.add(lblAudioOut, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		panel.add(comboAudioOut, new GridBagConstraints(1, 2, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		panel.add(btnAudioOutPlay, new GridBagConstraints(2, 2, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));

		// 4
		panel.add(lblNotifications, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		panel.add(comboNotifications, new GridBagConstraints(1, 3, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		panel.add(btnNotificationPlay, new GridBagConstraints(2, 3, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));

		// 5
		panel.add(checkEchoCancel, new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		// 6
		panel.add(checkNoiseCancel, new GridBagConstraints(0, 5, 1, 1, 1, 0.9,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));

		// select the current audio system and other devices
		selectCurrentAudioSystem();
		selectCurrentDevices();

		return panel;
	}

	@Override
	public void commit() {

		super.commit();

		setAudioSystemIfNew((AudioSystem) comboAudioSystem.getSelectedItem());

		setDeviceIfNew((CaptureDevice) comboAudioIn.getSelectedItem(),
				DataFlow.CAPTURE);

		setDeviceIfNew((CaptureDevice) comboAudioOut.getSelectedItem(),
				DataFlow.PLAYBACK);

		setDeviceIfNew((CaptureDevice) comboNotifications.getSelectedItem(),
				DataFlow.NOTIFY);

		// denoise
		UniteLibJitsiImpl.getDeviceConfiguration().setDenoise(
				checkNoiseCancel.isSelected());
		// echo
		UniteLibJitsiImpl.getDeviceConfiguration().setEchoCancel(
				checkEchoCancel.isSelected());
	}

	private void setAudioSystemIfNew(AudioSystem selectedAudioSystem) {
		if (selectedAudioSystem == null)
			return;

		AudioSystem currentAudioSystem = UniteLibJitsiImpl.getAudioSystem();
		if (!selectedAudioSystem.equals(currentAudioSystem))
			UniteLibJitsiImpl.setAudioSystem(currentAudioSystem, true);
	}

	private void setDeviceIfNew(CaptureDevice selectedDevice, DataFlow dataFlow) {
		if (selectedDevice == null)
			return;

		CaptureDeviceInfo2 currentDevice = UniteLibJitsiImpl
				.getSelectedDevice(dataFlow);

		if (!selectedDevice.equals(currentDevice))
			UniteLibJitsiImpl.setDevice(dataFlow,
					(CaptureDeviceInfo2) selectedDevice.info, true);
	}

	private void selectCurrentAudioSystem() {
		AudioSystem current = UniteLibJitsiImpl.getAudioSystem();
		for (int i = 0; i < comboAudioSystem.getItemCount(); i++) {
			AudioSystem system = (AudioSystem) comboAudioSystem.getItemAt(i);
			if (system == current) {
				comboAudioSystem.setSelectedIndex(i);
				break;
			}
		}
	}

	private void selectCurrentDevices() {
		selectCurrentDevice(comboAudioIn,
				UniteLibJitsiImpl.getSelectedDevice(DataFlow.CAPTURE));
		selectCurrentDevice(comboAudioOut,
				UniteLibJitsiImpl.getSelectedDevice(DataFlow.PLAYBACK));
		selectCurrentDevice(comboNotifications,
				UniteLibJitsiImpl.getSelectedDevice(DataFlow.NOTIFY));
	}

	private void selectCurrentDevice(JComboBox box, CaptureDeviceInfo2 current) {
		for (int i = 0; i < box.getItemCount(); i++) {
			CaptureDevice device = (CaptureDevice) box.getItemAt(i);
			if (device.equals(current)) {
				box.setSelectedIndex(i);
				break;
			}
		}
	}

	private ActionListener audioNotificationPlayListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playSound(false);
			}

		};
	}

	private ActionListener audioOutPlayListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playSound(true);
			}
		};
	}

	private void playSound(boolean isPlaybackEvent) {
		AudioNotifierService audioNotifServ = UniteLibJitsiImpl
				.getAudioNotifierService();

		String testSoundFilename = "/resources/sounds/notify.mp3";
		SCAudioClip sound = audioNotifServ.createAudio(testSoundFilename,
				isPlaybackEvent);
		sound.play();
	}
}
