package org.ezuce.unitemedia.phone;
/**
 * The highest number corresponds with the first codec offered
 * For audio - 14 is the highest and means that PCMA_8000 is offered first
 * For video - 3 is the highest and means that H264 is offered first
 * value 0 means that the code is not offered at all
 * TODO: There is no api code for the moment to reorganize the order
 */

public class EncodingDefaults {
	public static final String OVERRIDE_ENCODINGS = "OVERRIDE_ENCODINGS";
	public static int PCMA_8000 = 14;
	public static int PCMU_8000 = 13;
	public static int G722_8000 = 12;
	public static int opus_48000 = 11;
	public static int iLBC_8000 = 10;
	public static int GSM_8000 = 9;
	public static int SILK_16000 = 8;
	public static int SILK_24000 = 7;
	public static int speex_32000 = 6;
	public static int speex_16000 = 5;
	public static int speex_8000 = 4;
	public static int telephone_event_8000 = 3;	
	public static int SILK_12000 = 2;
	public static int SILK_8000 = 1;

	public static int H264_90000 = 3;
	public static int VP8_90000 = 2;
	public static int H263_1998_90000 = 1;

	
	public static String KEY_G722_8000 = "Encodings.G722/8000";
	public static String KEY_GSM_8000 = "Encodings.GSM/8000";
	public static String KEY_H263_1998_90000 = "Encodings.H263-1998/90000";
	public static String KEY_H264_90000 = "Encodings.H264/90000";
	public static String KEY_PCMA_8000 = "Encodings.PCMA/8000";
	public static String KEY_PCMU_8000 = "Encodings.PCMU/8000";
	public static String KEY_SILK_12000 = "Encodings.SILK/12000";
	public static String KEY_SILK_16000 = "Encodings.SILK/16000";
	public static String KEY_SILK_24000 = "Encodings.SILK/24000";
	public static String KEY_SILK_8000 = "Encodings.SILK/8000";
	public static String KEY_VP8_90000 = "Encodings.VP8/90000";
	public static String KEY_iLBC_8000 = "Encodings.iLBC/8000";
	public static String KEY_opus_48000 = "Encodings.opus/48000";
	public static String KEY_speex_16000 = "Encodings.speex/16000";
	public static String KEY_speex_32000 = "Encodings.speex/32000";
	public static String KEY_speex_8000 = "Encodings.speex/8000";
	public static String KEY_telephone_event_8000 = "Encodings.telephone-event/8000";
}
