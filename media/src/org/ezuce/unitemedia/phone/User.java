package org.ezuce.unitemedia.phone;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.java.sip.communicator.service.protocol.AccountID;

import org.ezuce.unitemedia.context.AbstractUser;
import org.ezuce.unitemedia.event.UniteEvent;

public class User extends AbstractUser {
	private String m_userName;
	private String m_password;
	private String m_server;
	private String m_port;
	private AccountID m_accountId = null;
	private Map<String, UniteCall> m_uniteCallMap = new HashMap<String, UniteCall>();
	
	public User() {
		
	}
	
	public User(String userName, String password, String server, String port) {
		m_userName = userName;
		m_password = password;
		m_server = server;
		m_port = port;
	}
	
	public String getUserName() {
		return m_userName;
	}
	public void setUserName(String userName) {
		m_userName = userName;
	}
	public String getPassword() {
		return m_password;
	}
	public void setPassword(String password) {
		m_password = password;
	}
	public String getServer() {
		return m_server;
	}
	public void setServer(String server) {
		m_server = server;
	}
	public String getPort() {
		return m_port;
	}
	public void setPort(String port) {
		m_port = port;
	}

	public UniteCall getUniteCall(String callId) {
		return m_uniteCallMap.get(callId);
	}
	/**
	 * Maximum one call can be connected at a time
	 * @return
	 */
	public UniteCall getConnectedCall() {
		for (UniteCall uniteCall : getUniteCalls()) {
			if (uniteCall.isConnected()) {
				return uniteCall;
			}
		}
		return null;
	}
	
	public Collection<UniteCall> getUniteCalls() {
		return m_uniteCallMap.values();
	}

	public void addUniteCall(UniteCall uniteCall) {
		m_uniteCallMap.put(uniteCall.getCall().getCallID(), uniteCall);
	}
	
	public UniteCall removeUniteCall(String callId) {
		return m_uniteCallMap.remove(callId);
	}

	public AccountID getAccountId() {
		return m_accountId;
	}

	public void setAccountId(AccountID accountId) {
		m_accountId = accountId;
	}
}
