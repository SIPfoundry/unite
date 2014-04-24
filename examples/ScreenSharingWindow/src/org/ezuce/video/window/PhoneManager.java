/**
 * 
 */
package org.ezuce.video.window;

/**
 * @author slava
 * 
 */
public class PhoneManager {

	private static PhoneManager instance = new PhoneManager();
	private Call call = new Call();

	public static PhoneManager getInstance() {
		return instance;
	}

	public Call getConnectedCall() {
		return call;
	}

}
