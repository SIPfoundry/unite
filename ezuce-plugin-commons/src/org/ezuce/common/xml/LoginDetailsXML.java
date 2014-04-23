package org.ezuce.common.xml;

import org.jivesoftware.spark.util.log.Log;

public class LoginDetailsXML {
	private String userName;
	private String imId;
	private boolean ldapImAuth;
	private String sipPassword;
	private String sipPort;
	private String videobridgePublicIp;
	
	public LoginDetailsXML(String userName, String imId, String sipPassword, String sipPort, String videobridgePublicIp) {
		this.userName = userName;
		this.imId = imId;
		this.sipPassword = sipPassword;
		this.sipPort = sipPort;
		this.videobridgePublicIp = videobridgePublicIp;
	}

	public String getUserName() {
		return userName;
	}

	public String getImId() {
		return imId;
	}

	public boolean isLdapImAuth() {
		return ldapImAuth;
	}

	public String getSipPassword() {
		return sipPassword;
	}

	public String getSipPort() {
		return sipPort;
	}

	public String getVideobridgePublicIp() {
		Log.warning("Videobridge public IP is: " + videobridgePublicIp);
		return videobridgePublicIp;
	}	
}
