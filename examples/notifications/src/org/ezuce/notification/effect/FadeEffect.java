/**
 * 
 */
package org.ezuce.notification.effect;

import java.util.Timer;
import java.util.TimerTask;

import org.ezuce.notification.listener.Callback;
import org.ezuce.notification.window.PopupWindow;

import com.sun.awt.AWTUtilities;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class FadeEffect extends TimerTask implements Effect {

	private PopupWindow component;
	private Callback callback;

	public FadeEffect(PopupWindow component) {
		if (component == null)
			throw new IllegalArgumentException("Component");
		this.component = component;
	}

	public void fire(Callback callback) {
		Timer timer = new Timer();
		timer.schedule(this, 500, 5);
		this.callback = callback;
	}

	public void stop() {
	}

	@Override
	public void run() {
		if (AWTUtilities.getWindowOpacity(component) > 0.1f) {
			AWTUtilities.setWindowOpacity(component,
					AWTUtilities.getWindowOpacity(component) - 0.1f);
		} else {
			// component.closeWindow();
			callback.doCall();
			cancel();

		}
	}
}
