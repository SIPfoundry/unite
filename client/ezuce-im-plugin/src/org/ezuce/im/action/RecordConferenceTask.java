package org.ezuce.im.action;


import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class RecordConferenceTask extends Task<Object,Void>
{
    private String conferenceName;
    private boolean recording;
    
	public RecordConferenceTask(org.jdesktop.application.Application app,
			String confName, boolean recording) {
		super(app);
		this.conferenceName = confName;
		this.recording = recording;
	}

	@Override
	protected Object doInBackground() {
		try {
			if (!recording) {
				RestManager.getInstance().confStartRecording(conferenceName);
			} else {
				RestManager.getInstance().confStopRecording(conferenceName);
			}
		} catch (Exception ex) {
			Log.error("Exception during rest call ", ex);
		}
		return null;
	}
    
	@Override
	protected void succeeded(Object result) {

	}

	@Override
	protected void finished() {

	}
}
