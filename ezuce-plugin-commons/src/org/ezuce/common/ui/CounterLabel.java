package org.ezuce.common.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import org.ezuce.common.event.RESTPoller;
import org.ezuce.common.rest.RestManager;

public class CounterLabel extends JLabel {
	private static final long serialVersionUID = 3267976469802305501L;
	protected int counterValue;
	private char[] counterValuesChars;
	private TimerTask task;
	private static CounterLabel missedCalls = null;
	private static CounterLabel missedVoicemails = null;
	private Timer t = new Timer();

	private CounterLabel(String counterName) {
		if (RestManager.getInstance().isLoggedIn()) {
			task = new Task(counterName);
			t.schedule(task, 0, 20 * 1000);
		}
	}
	
	public static CounterLabel getMissedCallsLabel() {
		if (missedCalls == null) {
			missedCalls = new CounterLabel(RESTPoller.MISSED_CALLS);
		}
		return missedCalls;
	}
	
	public static CounterLabel getMissedVoicemailsLabel() {
		if (missedVoicemails == null) {
			missedVoicemails = new CounterLabel(RESTPoller.VOICE_MESSAGES);
		}
		return missedVoicemails;
	}

	public void update(int value) {
		if (counterValue != value) {
			counterValue = value;
			counterValuesChars = String.valueOf(value).toCharArray();
		}
		revalidate();
		repaint();
	}
	
	public void refresh() {
		if (task != null) {
			RESTPoller.getInstance().refreshPoller();
			task.run();

		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (counterValue > 0) {
			g.setColor(new Color(46, 85, 102));
			g.fillOval(getWidth() - 16, getHeight() - 16, 15, 15);
			g.setColor(Color.white);
			Font f = g.getFont();
			g.setFont(new Font(f.getFontName(), f.getStyle(), 10));
			int hOffset = 8 + 3 * counterValuesChars.length;
			g.drawChars(counterValuesChars, 0, counterValuesChars.length, getWidth() - hOffset, getHeight() - 5);
		}
	}

	private class Task extends TimerTask {
		private final String counterName;

		public Task(String counterName) {
			this.counterName = counterName;
		}

		@Override
		public void run() {
			update(RESTPoller.getInstance().getCounter(counterName));
		}
	}
}
