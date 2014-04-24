package org.ezuce.common.ui.login;

public class LoginPassPair {

	private String mUsername;
	private String mPassword;
	private String mServer = "";
	private String mShortUsername;

	public LoginPassPair(String user, String passwd) {
		this.mUsername = user;
		this.mPassword = passwd;

		int indexOfAt = user.lastIndexOf("@");
		if (indexOfAt > 0) {
			try {
				String[] userAndServer = user.split("@");
				this.mShortUsername = userAndServer[0];
				this.mServer = userAndServer[1];
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public LoginPassPair(String username, String password, String server) {
		this.mUsername = username;
		this.mPassword = password;
		this.mServer = server;
		this.mShortUsername = username;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public String getServer() {
		return mServer;
	}

	public String getShortUsername() {
		return mShortUsername;
	}

	@Override
	public String toString() {
		return mShortUsername;
	}
}
