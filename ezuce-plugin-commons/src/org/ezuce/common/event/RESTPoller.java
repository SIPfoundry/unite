package org.ezuce.common.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ezuce.common.rest.RestManager;
import org.jivesoftware.spark.util.log.Log;

public class RESTPoller {
	public static final String MISSED_CALLS = "missedCalls";
	public static final String VOICE_MESSAGES = "voiceMessages";
	private static final RESTPoller INSTANCE = new RESTPoller();
	
	private final TimerTask task;

	private final Map<String, Integer> counters = new HashMap<String, Integer>();

	private RESTPoller() {
		task = new PollTask();
		final Timer timer = new Timer();

		timer.schedule(task, 500, 60 * 1000);
	}

	public static RESTPoller getInstance() {
		return INSTANCE;
	}
	
	public void refreshPoller() {
		if (task != null) {
			task.run();
		}
	}	

	public int getCounter(String counterName) {
		Integer counter = counters.get(counterName);

		if (counter == null) {
			counter = 0;
		}

		return counter;
	}

	public void setCounter(String counterName, int counterValue) {
		counters.put(counterName, counterValue);
	}
	
	public void setVoicemailCounter() throws Exception {
		int count = RestManager.getInstance().geUnheardVoicemailsNo();
		setCounter(VOICE_MESSAGES, count);
	}

	private class PollTask extends TimerTask {
		public PollTask() {
			// nothing special
		}

		@Override
		public void run() {
			try {
				int count = RestManager.getInstance().getLast24HrsMissedCallsNo();
				setCounter(MISSED_CALLS, count);
				count = RestManager.getInstance().geUnheardVoicemailsNo();
				setCounter(VOICE_MESSAGES, count);
			} catch (Exception e) {
				Log.error("Error polling server", e);
			}
		}
	}

}
