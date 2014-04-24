package org.ezuce.unitemedia.context;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.CaptureDeviceInfo;
import javax.media.Format;

import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.streaming.StreamType;
import org.ezuce.unitemedia.streaming.StreamUtils;
import org.jitsi.impl.neomedia.MediaServiceImpl;
import org.jitsi.impl.neomedia.MediaUtils;
import org.jitsi.impl.neomedia.codec.video.h264.JNIEncoder;
import org.jitsi.impl.neomedia.device.AudioSystem;
import org.jitsi.impl.neomedia.device.CaptureDeviceInfo2;
import org.jitsi.impl.neomedia.device.DeviceConfiguration;
import org.jitsi.impl.neomedia.device.AudioSystem.DataFlow;
import org.jitsi.impl.neomedia.format.MediaFormatImpl;
import org.jitsi.impl.neomedia.format.ParameterizedVideoFormat;
import org.jitsi.impl.neomedia.format.VideoMediaFormatImpl;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.neomedia.MediaType;
import org.jitsi.service.neomedia.MediaUseCase;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.service.neomedia.format.MediaFormat;
import org.jitsi.util.StringUtils;

public class DeviceContext {

	public static Object[] getAvailableAudioSystems() {
		return getDeviceConfiguration().getAvailableAudioSystems();
	}

	public static Object[] getAvailableAudioCaptureDevices() {
		return getDevicesByType(DeviceType.CAPTURE);
	}

	public static Object[] getAvailablePlaybackDevices() {
		return getDevicesByType(DeviceType.PLAYBACK);
	}

	public static Object[] getAvailableNotifyDevices() {
		return getDevicesByType(DeviceType.NOTIFY);
	}

	public static Object[] getAvailableVideoDevices() {
		return getDevicesByType(DeviceType.VIDEO);
	}
	
	public static DeviceConfiguration getDeviceConfiguration() {
		return ((MediaServiceImpl)ServiceContext.getMediaService()).getDeviceConfiguration();
	}	

	private static CaptureDevice[] getDevicesByType(DeviceType type) {
		if (type == DeviceType.AUDIO)
			throw new IllegalStateException("type");

		AudioSystem audioSystem = getDeviceConfiguration().getAudioSystem();
		List<? extends CaptureDeviceInfo> infos = null;
		CaptureDevice[] devices = null;
		if (audioSystem != null) {
			switch (type) {
			case CAPTURE:
				infos = audioSystem.getDevices(AudioSystem.DataFlow.CAPTURE);
				break;
			case NOTIFY:
				infos = audioSystem.getDevices(AudioSystem.DataFlow.NOTIFY);
				break;
			case PLAYBACK:
				infos = audioSystem.getDevices(AudioSystem.DataFlow.PLAYBACK);
				break;
			case VIDEO:
				infos = getDeviceConfiguration()
						.getAvailableVideoCaptureDevices(MediaUseCase.CALL);
				break;
			default:
				throw new IllegalStateException("type");
			}
		}
		final int deviceCount = (infos == null) ? 0 : infos.size();
		devices = new CaptureDevice[deviceCount + 1];

		if (deviceCount > 0) {
			for (int i = 0; i < deviceCount; i++)
				devices[i] = new CaptureDevice(infos.get(i));
		}
		devices[deviceCount] = new CaptureDevice(null);
		return devices;
	}
	
	public static List<MediaDevice> getMediaDevices() {
		return ServiceContext.getMediaService().getDevices(MediaType.VIDEO, MediaUseCase.DESKTOP);	
	}
	
	public static MediaDevice getMediaDevice(MediaService service, StreamType streamType) {
		switch (streamType) {
			case audio :
				return service.getDefaultDevice(MediaType.AUDIO, MediaUseCase.CALL);
			case cameravideo :
				return service.getDefaultDevice(MediaType.VIDEO, MediaUseCase.CALL);
			case desktopVideo :
				return service.getDefaultDevice(MediaType.VIDEO, MediaUseCase.DESKTOP);
			default :
				return service.getDefaultDevice(MediaType.AUDIO, MediaUseCase.CALL);
		}
	}

	public static AudioSystem getAudioSystem() {
		return getDeviceConfiguration().getAudioSystem();
	}

	public static void setAudioSystem(AudioSystem currentAudioSystem, boolean b) {
		getDeviceConfiguration().setAudioSystem(currentAudioSystem, b);
	}

	public static void setDevice(DataFlow dataFlow, CaptureDeviceInfo2 device,
			boolean b) {
		getDeviceConfiguration().getAudioSystem()
				.setDevice(dataFlow, device, b);
	}

	public static CaptureDeviceInfo2 getSelectedDevice(DataFlow dataFlow) {
		return getDeviceConfiguration().getAudioSystem().getSelectedDevice(
				dataFlow);
	}
	
	public static void setInputVolume(float volume) {
		ServiceContext.getMediaService().getInputVolumeControl().setVolume(volume);
	}
	
	public static void setOutputVolume(float volume) {
		ServiceContext.getMediaService().getOutputVolumeControl().setVolume(volume);
	}
	
	public static float getMaximumInputVolume() {
		return ServiceContext.getMediaService().getInputVolumeControl().getMaxValue();
	}
	
	public static float getMinimumInputVolume() {
		return ServiceContext.getMediaService().getInputVolumeControl().getMinValue();
	}
	
	public static float getInputVolume() {
		return ServiceContext.getMediaService().getInputVolumeControl().getVolume();
	}
	
	public static float getMaximumOutputVolume() {
		return ServiceContext.getMediaService().getOutputVolumeControl().getMaxValue();
	}
	
	public static float getMinimumOutputVolume() {
		return ServiceContext.getMediaService().getOutputVolumeControl().getMinValue();
	}
	
	public static float getOutputVolume() {
		return ServiceContext.getMediaService().getOutputVolumeControl().getVolume();
	}	

}
