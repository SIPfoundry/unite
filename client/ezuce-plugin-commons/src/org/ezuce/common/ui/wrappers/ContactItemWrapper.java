package org.ezuce.common.ui.wrappers;

import javax.swing.ImageIcon;

import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.PresenceManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Wraps the original Spark ContactItem in order to implement the necessary
 * interface allowing it to be displayed in the Call Tab contact list.
 */
public class ContactItemWrapper extends ContactListEntry {
    private ContactItem contactItem=null;
    private ContactGroup originalGroup=null;


    public ContactItemWrapper() {
        this.contactItem=new ContactItem("", "", "");
    }
    
    public ContactItemWrapper(String alias, String nickname, String fullyQualifiedJID){
    	this.contactItem=new ContactItem(alias, nickname, fullyQualifiedJID);
    }

    public ContactItemWrapper(ContactItem ci, ContactGroup originalContactGroup) {
        this.originalGroup=originalContactGroup;//TODO: must remove this coupling
        this.contactItem = ci;
    }

    @Override
    public String getUserDisplayName() {
        return ((isEmpty(contactItem.getDisplayName()) || contactItem.getDisplayName().contains("Unknown"))?
                getNumber() : contactItem.getDisplayName());
    }

    @Override
    public ImageIcon getUserAvatar() {
    	return Utils.getAvatarFromCache(contactItem.getJID());     
    }

    @Override
    public String getDescription() {
        return contactItem.getDescriptionLabel().getText();
    }

    @Override
    public String getNumber() {
        return Utils.getExtension(contactItem.getJID());
    }

    @Override
    public String getStatus() {
        return this.contactItem.getStatus();
    }

    @Override
    public Presence getPresence() {
        return PresenceManager.getPresence(contactItem.getJID());
    }

    public ContactItem getContactItem() {
        return contactItem;
    }

    @Override
    public String getImId() {
    	return Utils.getImId(contactItem.getJID());
    }
    
    public String getFullJID() {
    	return SparkManager.getUserManager().getFullJID(contactItem.getJID());
    }

    public ContactGroup getContactGroup() {
        return this.originalGroup;
    }   
}