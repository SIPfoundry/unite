package com.ezuce.jitsi;

import java.util.List;

import javax.media.CaptureDeviceInfo;

import org.jitsi.impl.neomedia.MediaServiceImpl;
import org.jitsi.impl.neomedia.device.AudioSystem;
import org.jitsi.impl.neomedia.device.AudioSystem.DataFlow;
import org.jitsi.impl.neomedia.device.CaptureDeviceInfo2;
import org.jitsi.impl.neomedia.device.DeviceConfiguration;
import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.neomedia.MediaUseCase;

public class UniteLibJitsiImpl {

	public enum Type {
		AUDIO, CAPTURE, PLAYBACK, NOTIFY, VIDEO
	}

	public static AudioNotifierService getAudioNotifierService() {
		return LibJitsi.getAudioNotifierService();
	}

	public static MediaServiceImpl getMediaService() {
		return (MediaServiceImpl) LibJitsi.getMediaService();
	}

	public static DeviceConfiguration getDeviceConfiguration() {
		return getMediaService().getDeviceConfiguration();
	}

	public static Object[] getAvailableAudioSystems() {
		return getDeviceConfiguration().getAvailableAudioSystems();
	}

	public static Object[] getAvailableAudioCaptureDevices() {
		return getDevicesByType(Type.CAPTURE);
	}

	public static Object[] getAvailablePlaybackDevices() {
		return getDevicesByType(Type.PLAYBACK);
	}

	public static Object[] getAvailableNotifyDevices() {
		return getDevicesByType(Type.NOTIFY);
	}

	public static Object[] getAvailableVideoDevices() {
		return getDevicesByType(Type.VIDEO);
	}

	private static CaptureDevice[] getDevicesByType(Type type) {
		if (type == Type.AUDIO)
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

}
