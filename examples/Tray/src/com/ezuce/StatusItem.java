package com.ezuce;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.jivesoftware.smack.packet.Presence;

public class StatusItem extends JLabel {

	/**  */
	private static final long serialVersionUID = -7108967564315672875L;
	private Presence presence;
	private Icon icon;
	private String text;

	public StatusItem(Presence presence, Icon icon) {
		this.presence = presence;
		this.icon = icon;
		this.text = presence.getStatus();
	}

	public String getText() {
		return text;
	}

	public Icon getIcon() {
		return icon;
	}

	public Presence getPresence() {
		return presence;
	}

	public String getName() {
		// TODO: to be implemented
		System.out.println("TODO: StatusItem#getName()" );
		return null;
	}
}
