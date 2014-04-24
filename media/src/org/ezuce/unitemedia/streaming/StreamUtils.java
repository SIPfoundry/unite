package org.ezuce.unitemedia.streaming;

import org.jitsi.service.neomedia.format.MediaFormatFactory;

public class StreamUtils {
	public static final String VIDEO_ENCODING = "H264";
	public static final double VIDEO_CLOCK_RATE = MediaFormatFactory.CLOCK_RATE_NOT_SPECIFIED;
	public static final byte DYNAMIC_VIDEO_RTP_PAYLOAD_TYPE = 99;
	public static final String AUDIO_ENCODING = "PCMU";
	public static final double AUDIO_CLOCK_RATE = 8000;
	public static final byte DYNAMIC_AUDIO_RTP_PAYLOAD_TYPE = -1;

	
	public static boolean isVideo(StreamType streamType) {
		return streamType == StreamType.cameravideo || streamType == StreamType.desktopVideo;
	}
	
	public static boolean isDesktopSharing(StreamType streamType) {
		System.out.println("MIRCEA SSSSSSSSSSSSSS " + streamType);
		return streamType == StreamType.desktopVideo;
	}
	
	public static String getEncoding(StreamType streamType) {
		return isVideo(streamType) ? VIDEO_ENCODING : AUDIO_ENCODING;
	}
	
	public static double getClockRate(StreamType streamType) {
		return isVideo(streamType) ? VIDEO_CLOCK_RATE : AUDIO_CLOCK_RATE;
	}
	
	public static byte getDynamicRTPPayloadType(StreamType streamType) {
		return isVideo(streamType) ? DYNAMIC_VIDEO_RTP_PAYLOAD_TYPE : DYNAMIC_AUDIO_RTP_PAYLOAD_TYPE;
	}
	
	public static String getContentType(StreamType streamType) {
		return isVideo(streamType) ? "video" : "audio";
	}
	
	public static boolean isReceiver(DirectionType directionType) {
		return directionType == DirectionType.receiveonly || directionType == DirectionType.sendandreceive;
	}
	
	public static boolean isTransmitter(DirectionType directionType) {
		return directionType == DirectionType.sendonly || directionType == DirectionType.sendandreceive;
	}	
}
