package org.ezuce.common.preference.video;

import static org.ezuce.media.ui.GraphicUtils.createImageIcon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.media.CaptureDeviceInfo;
import javax.media.MediaException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.java.sip.communicator.impl.neomedia.DeviceConfigurationComboBoxModel;

import org.ezuce.media.ui.WidgetBuilder;
import org.ezuce.unitemedia.context.CaptureDevice;
import org.ezuce.unitemedia.context.DeviceContext;
import org.ezuce.unitemedia.context.ServiceContext;
import org.jitsi.impl.neomedia.MediaServiceImpl;
import org.jitsi.impl.neomedia.device.MediaDeviceImpl;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.neomedia.MediaType;
import org.jitsi.service.neomedia.MediaUseCase;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.util.swing.VideoContainer;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.preference.Preference;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * The functionality of this class is based on
 * net.java.sip.communicator.impl.neomedia.MediaConfigurationImpl by Lyubomir
 * Marinov,Damian Minkov, Yana Stamcheva, Boris Grozev, Vincent Lucas. And
 * contains some copy-pasted methods from it. All comments are preserved.
 * 
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 */
public class VideoSettings implements Preference {

	/**  */
	public static final String NAMESPACE = "http://www.jivesoftware.org/spark/video";
	private static final String iconName = "/resources/images/prefs/video";
	private String title;
	private JPanel videoPanel;
	private JComboBox comboVideoCamera;
	private ImageIcon activeImage = createImageIcon(iconName + "_on.png");
	private ImageIcon inactiveImage = createImageIcon(iconName + "_off.png");
	private ImageIcon titleImage = createImageIcon(iconName + ".png");
	private JPanel mPanel;

	public VideoSettings() {
		title = Res.getString("preferences.video.title");
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
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

		selectVideoDevice();
		showPreview();

		return mPanel;
	}

	@Override
	public void commit() {

		CaptureDevice selectedDevice = (CaptureDevice) comboVideoCamera
				.getSelectedItem();

		if (selectedDevice == null)
			return;

		CaptureDeviceInfo device = DeviceContext.getDeviceConfiguration()
				.getVideoCaptureDevice(MediaUseCase.ANY);

		if (!selectedDevice.equals(device)) {
			DeviceContext.getDeviceConfiguration().setVideoCaptureDevice(
					selectedDevice.info, true);
			showPreview();
		}
	}

	private JPanel createGUI() {
		// components
		comboVideoCamera = WidgetBuilder.createComboBox(DeviceContext
				.getAvailableVideoDevices());

		// layout
		JPanel panel = new JPanel(new GridBagLayout());
		// panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

		Insets insets = new Insets(5, 5, 5, 5);
		panel.add(comboVideoCamera,
				new GridBagConstraints(0, 0, 1, 1, 0, 0.2,
						GridBagConstraints.NORTH, GridBagConstraints.NONE,
						insets, 0, 0));

		panel.add(getVideoPanel(), new GridBagConstraints(0, 1, 1, 1, 1, 0.8,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0));

		return panel;
	}

	private JComponent getVideoPanel() {
		if (videoPanel != null)
			return videoPanel;

		videoPanel = new JPanel(new GridLayout(1, 1));
		// videoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		return videoPanel;
	}

	private void selectVideoDevice() {
		CaptureDeviceInfo currentVideoDevice = DeviceContext
				.getDeviceConfiguration().getVideoCaptureDevice(
						MediaUseCase.ANY);
		for (int i = 0; i < comboVideoCamera.getItemCount(); i++) {
			CaptureDevice videoDevice = (CaptureDevice) comboVideoCamera
					.getItemAt(i);
			if (videoDevice.equals(currentVideoDevice)) {
				comboVideoCamera.setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Creates the video container.
	 * 
	 * @param noVideoComponent
	 *            the container component.
	 * @return the video container.
	 */
	private static JComponent createVideoContainer(Component noVideoComponent) {
		return new VideoContainer(noVideoComponent, false);
	}

	/**
	 * Initializes a new <tt>Component</tt> which.is to preview and/or allow
	 * detailed configuration of an audio or video <tt>DeviceSystem</tt>.
	 * 
	 * @param type
	 *            either {@link DeviceConfigurationComboBoxModel#AUDIO} or
	 *            {@link DeviceConfigurationComboBoxModel#VIDEO}
	 * @param comboBox
	 *            the <tt>JComboBox</tt> which lists the available alternatives
	 *            and the selection which is to be previewed. May be
	 *            <tt>null</tt> in the case of audio in which case it is assumed
	 *            that the user is not allowed to set the <tt>AudioSystem</tt>
	 *            to be used and the selection is determined by the
	 *            <tt>DeviceConfiguration</tt> of the <tt>MediaService</tt>.
	 * @param prefSize
	 *            the preferred size to be applied to the preview
	 * @return a new <tt>Component</tt> which is to preview and/or allow
	 *         detailed configuration of the <tt>DeviceSystem</tt> identified by
	 *         <tt>type</tt> and <tt>comboBox</tt>
	 */
	private Component createPreview(JComboBox comboBox) {
		JComponent preview = null;
		JLabel noPreview = new JLabel(
				Res.getString("preferences.video.no.preview"));

		noPreview.setHorizontalAlignment(SwingConstants.CENTER);
		noPreview.setVerticalAlignment(SwingConstants.CENTER);

		preview = createVideoContainer(noPreview);

		Object selectedItem = comboBox.getSelectedItem();
		CaptureDeviceInfo device = null;

		if (selectedItem instanceof CaptureDevice)
			device = ((CaptureDevice) selectedItem).info;

		Exception exception;
		try {
			createVideoPreview(device, preview);
			exception = null;
		} catch (IOException ex) {
			exception = ex;
		} catch (MediaException ex) {
			exception = ex;
		}
		if (exception != null) {
			System.out.println("Failed to create preview for device " + device
					+ " " + exception.getMessage());
		}
		return preview;
	}

	/**
	 * Creates preview for the (video) device in the video container.
	 * 
	 * @param device
	 *            the device
	 * @param videoContainer
	 *            the video container
	 * @throws IOException
	 *             a problem accessing the device
	 * @throws MediaException
	 *             a problem getting preview
	 */
	private static void createVideoPreview(CaptureDeviceInfo device,
			JComponent videoContainer) throws IOException, MediaException {
		videoContainer.removeAll();
		videoContainer.revalidate();
		videoContainer.repaint();

		if (device == null)
			return;

		MediaService mediaService = ServiceContext.getMediaService();
		for (MediaDevice mediaDevice : mediaService.getDevices(MediaType.VIDEO,
				MediaUseCase.ANY)) {
			if (((MediaDeviceImpl) mediaDevice).getCaptureDeviceInfo().equals(
					device)) {
				Dimension videoContainerSize = videoContainer
						.getPreferredSize();

				try {
					Component preview = (Component) mediaService
							.getVideoPreviewComponent(mediaDevice,
									videoContainerSize.width,
									videoContainerSize.height);
					if (preview != null)
						videoContainer.add(preview);

				} catch (Exception e) {
					System.out.println("Preview cannot be shown: "
							+ e.getMessage());
				}

				break;
			}
		}
	}

	private void showPreview() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Component preview = createPreview(comboVideoCamera);
				if (preview != null) {
					videoPanel.removeAll();
					videoPanel.add(preview);
					videoPanel.validate();
				}
			}
		});

	}

}
