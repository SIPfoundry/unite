package org.ezuce.common.impl;

import java.awt.event.FocusEvent;

import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.UserMiniPanelCommonInterface;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;

public class DummyCommonInterface implements UserMiniPanelCommonInterface {
	private final ContactItem item;

	public DummyCommonInterface(ContactItem item) {
		this.item = item;
	}

	@Override
	public ContactListEntry getContact() {
		ContactList list = Workspace.getInstance().getContactList();
		return new ContactItemWrapper(item, list.getContactGroup(item.getGroupName()));
	}

	@Override
	public String getJID() {
		return item.getJID();
	}

	@Override
	public String getUserDisplayName() {
		return item.getDisplayName();
	}

    @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        
    }

	@Override
    public String getExtension() {
        String jid = item.getJID();
        return Utils.getExtension(jid);
    }
}
