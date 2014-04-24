package com.ezuce;

import java.awt.Window;

public interface NativeHandler {

	boolean handleNotification();

	void stopFlashing(Window window);

	void flashWindowStopWhenFocused(Window window);

	void flashWindow(Window window);

	

}
