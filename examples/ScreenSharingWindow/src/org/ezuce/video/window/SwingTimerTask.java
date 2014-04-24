package org.ezuce.video.window;

import java.awt.EventQueue;

public abstract class SwingTimerTask extends java.util.TimerTask {
	public abstract void doRun();

	public void run() {
		if (!EventQueue.isDispatchThread()) {
			EventQueue.invokeLater(this);
		} else {
			doRun();
		}
	}
}