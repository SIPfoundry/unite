/**
 * 
 */
package org.ezuce.notification.effect;

import org.ezuce.notification.listener.Callback;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public interface Effect {

	void fire(Callback callback);

	void stop();
}
