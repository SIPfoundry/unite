package org.ezuce.common;

import org.ezuce.common.resource.Config;

public class VoiceUtil {
	public final static void configure(ConfigureVoice configureVoice) throws Exception {
		if(Config.getInstance().isRegisterAsPhone()) {
			configureVoice.configureVoiceEnabled();
		} else {
			configureVoice.configureVoiceDisabled();
		}
	}
	
	public final static void execute(ExecuteVoice executeVoice) {
		if(Config.getInstance().isRegisterAsPhone()) {
			executeVoice.executeVoiceEnabled();
		} else {
			executeVoice.executeVoiceDisabled();
		}
	}
}
