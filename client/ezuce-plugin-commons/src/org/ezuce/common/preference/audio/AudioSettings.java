package org.ezuce.common.preference.audio;

import static org.ezuce.media.ui.GraphicUtils.createImageIcon;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ezuce.media.ui.WidgetBuilder;
import org.ezuce.unitemedia.context.CaptureDevice;
import org.ezuce.unitemedia.context.DeviceContext;
import org.ezuce.unitemedia.context.ServiceContext;
import org.jitsi.impl.neomedia.device.AudioSystem;
import org.jitsi.impl.neomedia.device.AudioSystem.DataFlow;
import org.jitsi.impl.neomedia.device.CaptureDeviceInfo2;
import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.audionotifier.SCAudioClip;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.preference.Preference;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class AudioSettings implements Preference {

	private static final String BTN_PLAY_ON = "/resources/images/prefs/play_on.png";
	private static final String BTN_PLAY_OFF = "/resources/images/prefs/play_off.png";
	private static final Dimension SIZE_COMBO_DEVICES = new Dimension(300, 30);
	/**  */
	public static final String NAMESPACE = "http://www.jivesoftware.org/spark/audio";
	private static final String iconName = "/resources/images/prefs/audio";
	private String title;
	private JComboBox comboAudioSystem;
	private JComboBox comboAudioIn;
	private JComboBox comboAudioOut;
	private JComboBox comboNotifications;
	private JCheckBox checkNoiseCancel;
	private JCheckBox checkEchoCancel;
	private JButton btnAudioOutPlay;
	private JButton btnNotificationPlay;
	private ImageIcon activeImage = createImageIcon(iconName + "_on.png");
	private ImageIcon inactiveImage = createImageIcon(iconName + "_off.png");
	private ImageIcon titleImage = createImageIcon(iconName + ".png");
	private DefaultComboBoxModel audioCaptureModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel audioPlaybackModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel audioNotificationModel = new DefaultComboBoxModel();
	private JPanel mPanel;

	public AudioSettings() {
		title = Res.getString("preferences.audio.title");
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Icon getIcon() {
		return inactiveImage;
	}

	@Override
	public String getTooltip() {
		return title;
	}

	@Override
	public String getListName() {
		return title;
	}

	@Override
	public void load() {
		mPanel = (JPanel) getGUI();
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
	}

	@Override
	public Icon getActiveIcon() {
		return activeImage;
	}

	@Override
	public Icon getTitleIcon() {
		return titleImage;
	}

	@Override
	public JComponent getGUI() {
		if (mPanel == null)
			mPanel = createGUI();

		return mPanel;
	}

	@Override
	public void commit() {

		setAudioSystemIfNew((AudioSystem) comboAudioSystem.getSelectedItem());

		setDeviceIfNew((CaptureDevice) comboAudioIn.getSelectedItem(),
				DataFlow.CAPTURE);

		setDeviceIfNew((CaptureDevice) comboAudioOut.getSelectedItem(),
				DataFlow.PLAYBACK);

		setDeviceIfNew((CaptureDevice) comboNotifications.getSelectedItem(),
				DataFlow.NOTIFY);

		 //denoise
		DeviceContext.getDeviceConfiguration().getAudioSystem().setDenoise(
				checkNoiseCancel.isSelected());
		// echo
		DeviceContext.getDeviceConfiguration().getAudioSystem().setEchoCancel(
				checkEchoCancel.isSelected());
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	private JPanel createGUI() {
		// components
		// systems
		comboAudioSystem = WidgetBuilder.createComboBox(DeviceContext
				.getAvailableAudioSystems());
		comboAudioSystem.setPreferredSize(SIZE_COMBO_DEVICES);
		JLabel lblAudioSystem = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.system"));
		comboAudioSystem.addItemListener(audioSystemChangedListener());

		// in
		comboAudioIn = WidgetBuilder.createComboBox();
		comboAudioIn.setPreferredSize(SIZE_COMBO_DEVICES);
		comboAudioIn.setModel(audioCaptureModel);
		JLabel lblAudioIn = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.in"));

		// out
		comboAudioOut = WidgetBuilder.createComboBox();
		comboAudioOut.setPreferredSize(SIZE_COMBO_DEVICES);
		comboAudioOut.setModel(audioPlaybackModel);
		JLabel lblAudioOut = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.out"));

		// notifications
		comboNotifications = WidgetBuilder.createComboBox();
		comboNotifications.setPreferredSize(SIZE_COMBO_DEVICES);
		comboNotifications.setModel(audioNotificationModel);
		JLabel lblNotifications = WidgetBuilder.createLabel(Res
				.getString("preferences.audio.notifications"));

		// echo
		checkEchoCancel = WidgetBuilder.createCheckbox(Res
				.getString("preferences.audio.echo.cancellation"));
		checkEchoCancel.setSelected(DeviceContext.getDeviceConfiguration().getAudioSystem()
				.isEchoCancel());

		// denoise
		checkNoiseCancel = WidgetBuilder.createCheckbox(Res
				.getString("preferences.audio.noise.suppression"));
		checkNoiseCancel.setSelected(DeviceContext.getDeviceConfiguration().getAudioSystem()
				.isDenoise());

		// audio out play button
		btnAudioOutPlay = WidgetBuilder.createButton(BTN_PLAY_OFF, BTN_PLAY_ON);
		btnAudioOutPlay.addActionListener(audioOutPlayListener());

		// notifications play button
		btnNotificationPlay = WidgetBuilder.createButton(BTN_PLAY_OFF,
				BTN_PLAY_ON);
		btnNotificationPlay.addActionListener(audioNotificationPlayListener());

		// / layout

		// panel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		// 1
		Insets insets = new Insets(5, 5, 5, 5);
		JPanel panel = new JPanel(new GridBagLayout());
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
		fillDevices();
		selectCurrentAudioSystem();
		selectCurrentDevices();

		return panel;
	}

	private void setAudioSystemIfNew(AudioSystem selectedAudioSystem) {
		if (selectedAudioSystem == null)
			return;
		DeviceContext.setAudioSystem(selectedAudioSystem, true);
	}

	private void setDeviceIfNew(CaptureDevice selectedDevice, DataFlow dataFlow) {
		if (selectedDevice == null)
			return;

		CaptureDeviceInfo2 currentDevice = DeviceContext
				.getSelectedDevice(dataFlow);

		if (!selectedDevice.equals(currentDevice))
			DeviceContext.setDevice(dataFlow,
					(CaptureDeviceInfo2) selectedDevice.info, true);
	}

	private void selectCurrentAudioSystem() {
		AudioSystem current = DeviceContext.getAudioSystem();
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
				DeviceContext.getSelectedDevice(DataFlow.CAPTURE));
		selectCurrentDevice(comboAudioOut,
				DeviceContext.getSelectedDevice(DataFlow.PLAYBACK));
		selectCurrentDevice(comboNotifications,
				DeviceContext.getSelectedDevice(DataFlow.NOTIFY));
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
		AudioNotifierService audioNotifServ = ServiceContext
				.getAudioNotifierService();

		// TODO: Slava:
		// I need some help here. it seems not working
		String testSoundFilename = "/resources/sounds/notify.mp3";
		SCAudioClip sound = audioNotifServ.createAudio(testSoundFilename,
				isPlaybackEvent);
		sound.play();
	}

	private void fillDevices() {
		fillDevice(audioCaptureModel,
				DeviceContext.getAvailableAudioCaptureDevices());
		fillDevice(audioPlaybackModel,
				DeviceContext.getAvailablePlaybackDevices());
		fillDevice(audioNotificationModel,
				DeviceContext.getAvailableNotifyDevices());
	}

	private void fillDevice(DefaultComboBoxModel audioComboBoxModel,
			Object[] objects) {
		audioComboBoxModel.removeAllElements();
		for (Object device : objects)
			audioComboBoxModel.addElement(device);
	}

	private ItemListener audioSystemChangedListener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					AudioSystem item = (AudioSystem) event.getItem();
					DeviceContext.setAudioSystem(item, true);
					fillDevices();
					selectCurrentDevices();
				}
			}
		};
	}

}
