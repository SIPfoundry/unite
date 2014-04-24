package org.ezuce.unitemedia.security;

import net.java.sip.communicator.service.protocol.SecurityAuthority;
import net.java.sip.communicator.service.protocol.UserCredentials;

public class DefaultSecurityAuthority implements SecurityAuthority {
	private String m_userName;
	private String m_password;
	
	public DefaultSecurityAuthority(String userName, String password) {
		m_userName = userName;
		m_password = password;
	}

	/**
	 * Implements the <code>SecurityAuthority.obtainCredentials</code> method.
	 *
	 * @param realm
	 *            The realm that the credentials are needed for.
	 * @param userCredentials
	 *            the values to propose the user by default
	 * @param reasonCode
	 *            indicates the reason for which we're obtaining the
	 *            credentials.
	 * @return The credentials associated with the specified realm or null if
	 *         none could be obtained.
	 */
	@Override
	public UserCredentials obtainCredentials(String realm, UserCredentials userCredentials, int reasonCode) {
		userCredentials.setUserName(m_userName);
		userCredentials.setPassword(m_password.toCharArray());		
		userCredentials.setPasswordPersistent(false);
		return userCredentials;
	}

	/**
	 * Implements the <code>SecurityAuthority.obtainCredentials</code> method.
	 * Creates and show an <tt>AuthenticationWindow</tt>, where user could enter
	 * its password.
	 *
	 * @param realm
	 *            The realm that the credentials are needed for.
	 * @param userCredentials
	 *            the values to propose the user by default
	 * @return The credentials associated with the specified realm or null if
	 *         none could be obtained.
	 */
	@Override
	public UserCredentials obtainCredentials(String realm, UserCredentials userCredentials) {
		return this.obtainCredentials(realm, userCredentials, SecurityAuthority.AUTHENTICATION_REQUIRED);
	}

	/**
	 * Sets the userNameEditable property, which indicates if the user name
	 * could be changed by user or not.
	 *
	 * @param isUserNameEditable
	 *            indicates if the user name could be changed by user
	 */
	@Override
	public void setUserNameEditable(boolean isUserNameEditable) {
	}

	/**
	 * Indicates if the user name is currently editable, i.e. could be changed
	 * by user or not.
	 *
	 * @return <code>true</code> if the user name could be changed,
	 *         <code>false</code> - otherwise.
	 */
	@Override
	public boolean isUserNameEditable() {
		return false;
	}
}