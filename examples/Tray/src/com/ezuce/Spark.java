package com.ezuce;

public class Spark {

	public static boolean isWindows() {
		final String osName = System.getProperty("os.name").toLowerCase();
        return osName.startsWith("windows");
	}

}
