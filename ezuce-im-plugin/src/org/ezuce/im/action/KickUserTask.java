package org.ezuce.im.action;

import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class KickUserTask extends Task<Object, Void> {
	private String conferenceName;
	private int userId;

	public KickUserTask(org.jdesktop.application.Application app,
			String confName, int userId) {
		super(app);
		this.conferenceName = confName;
		this.userId = userId;
	}

	@Override
	protected Object doInBackground() {
		try {
			RestManager.getInstance().confKickUser(conferenceName, userId);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
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
