package org.ezuce.common.ui.wrappers.interfaces;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.smack.packet.Presence;

/**
 * Must be implemented by all entities which need to be displayed in the contacts
 * list, inside the Call Tab.
 */
public abstract class ContactListEntry {
    public abstract String getUserDisplayName();
    public abstract ImageIcon getUserAvatar();
    public abstract String getDescription();
    public abstract String getStatus();
    public abstract Presence getPresence();
    public abstract String getNumber();
    public abstract String getImId();
    
    @Override
    public boolean equals(Object entry) {
    	String entryNumber = ((ContactListEntry)entry).getNumber();
    	String number = getNumber();
    	return (entryNumber== null || number == null) ? false : 
    		StringUtils.equals(entryNumber.toLowerCase(), number.toLowerCase());
    }
    
    public boolean isSameContact(ContactListEntry obj) {
        return equals((ContactListEntry)obj);
    }    
    @Override
    public int hashCode() {
    	String number = getNumber();
        return number == null ? 0 : number.toLowerCase().hashCode();
    }
	public void setNumber(String number) {
		// TODO Auto-generated method stub
		
	}
}
