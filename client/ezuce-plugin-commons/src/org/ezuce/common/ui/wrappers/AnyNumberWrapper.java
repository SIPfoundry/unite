package org.ezuce.common.ui.wrappers;

import javax.swing.ImageIcon;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jivesoftware.smack.packet.Presence;

public class AnyNumberWrapper extends ContactListEntry{
	private String number;
	
	@Override
	public String getUserDisplayName() {
		return number;
	}

	@Override
	public ImageIcon getUserAvatar() {
		return Utils.getDefaultAvatar();
	}

	@Override
	public String getDescription() {
	    return EMPTY;
	}

	@Override
	public String getStatus() {
		return EMPTY;
	}

	@Override
	public Presence getPresence() {
		return null;
	}

	@Override
	public String getNumber() {
		return number;
	}	

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String getImId() {
		return null;
	}
}
