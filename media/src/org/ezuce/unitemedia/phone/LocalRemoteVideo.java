package org.ezuce.unitemedia.phone;

import java.awt.Component;

public class LocalRemoteVideo {
	private Component m_local;
	private Component m_remote;
	
	public LocalRemoteVideo(Component local, Component remote) {
		m_local = local;
		m_remote = remote;
	}
	
	public Component getLocal() {
		return m_local;
	}
	public void setLocal(Component local) {
		m_local = local;
	}
	public Component getRemote() {
		return m_remote;
	}
	public void setRemote(Component remote) {
		m_remote = remote;
	}
}
