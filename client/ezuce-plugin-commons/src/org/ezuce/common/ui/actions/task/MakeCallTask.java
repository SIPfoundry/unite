package org.ezuce.common.ui.actions.task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ezuce.common.ExecuteVoice;
import org.ezuce.common.VoiceUtil;
import org.ezuce.common.resource.Config;
import org.ezuce.common.rest.RestManager;
import org.ezuce.media.manager.PhoneManager;
import org.jivesoftware.spark.util.log.Log;

public class MakeCallTask extends org.jdesktop.application.Task<Object, Void> implements ExecuteVoice {

	private String calee = null;
    private final Pattern numbersPattern = Pattern.compile("[0-9]+");
    private boolean ignoreNumbersPattern = false;
    private boolean videoCall = false;

	public MakeCallTask(org.jdesktop.application.Application app, boolean ignoreNumbersPattern) {
		super(app);
		this.ignoreNumbersPattern = ignoreNumbersPattern;
	}

	public MakeCallTask(org.jdesktop.application.Application app, String calee) {
		super(app);
		if (calee != null) {
			this.calee = calee;
		}
	}
	
	public MakeCallTask(org.jdesktop.application.Application app, String calee, boolean video) {
		this(app, calee);
		videoCall = video;
	}

	public void setCalee(String c) {
		calee = c;
	}

	private String keepNumbers(String phoneNumber) {
		if (phoneNumber.startsWith("+")) {
			phoneNumber = phoneNumber.replaceFirst("\\+", "00");
		}	
		final StringBuilder sb = new StringBuilder();
		final Matcher m = numbersPattern.matcher(phoneNumber);
		while (m.find()) {
			sb.append(m.group());
		}
		return sb.toString();
	}
        
        
	@Override
	protected Object doInBackground() {
		VoiceUtil.execute(this);
		return null;
	}

    @Override
	protected void succeeded(Object result) {
		// EDT:
	}

	public void setIgnoreNumbersPattern(boolean ignoreNumbersPattern) {
		this.ignoreNumbersPattern = ignoreNumbersPattern;
	}

	@Override
	public void executeVoiceEnabled() {
		String number = ignoreNumbersPattern ? calee : keepNumbers(calee);
		if (!videoCall) {
			PhoneManager.getInstance().makeCall(number);				
		} else {
			PhoneManager.getInstance().makeVideoCall(number);
		}
	}

	@Override
	public void executeVoiceDisabled() {
		String number = ignoreNumbersPattern ? calee : keepNumbers(calee);
		try {	
			RestManager.getInstance().makeCall(number);
		} catch (Exception ex) {
			Log.error("Cannot make call", ex);
		}
		
	}    
}
