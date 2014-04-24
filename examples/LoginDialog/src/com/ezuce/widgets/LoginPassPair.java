package com.ezuce.widgets;

public class LoginPassPair {

	private String mUsername;
	private String mPassword;

	public LoginPassPair(String user, String passwd) {
		this.mUsername = user;
		this.mPassword = passwd;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	@Override
	public String toString() {
		return mUsername;
	}
}
