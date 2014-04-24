package com.ezuce;

public class Log {

	public static void error(String string, Exception e) {
		System.out.println(" Log#error()" + string + "  e= " + e.getMessage());

	}

}
