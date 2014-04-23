package org.ezuce.common.ui.wrappers;

import javax.swing.ImageIcon;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.xml.PhonebookContactInfoXML;
import org.ezuce.common.xml.PhonebookEntryXML;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Razvan
 */
public class PhoneBookEntryWrapper extends ContactListEntry {

    private PhonebookEntryXML phoneBookEntry;

    public PhoneBookEntryWrapper() {

    }

    public PhoneBookEntryWrapper(PhonebookEntryXML pbe) {
        this.phoneBookEntry=pbe;        
    }
    
    public PhonebookEntryXML getPhonebookEntryXml()
    {
        return this.phoneBookEntry;
    }

     @Override
    public String getUserDisplayName() {
         StringBuilder name=new StringBuilder();
         name.append((phoneBookEntry.getFirstName()==null || phoneBookEntry.getFirstName().contains("null"))?
             ((phoneBookEntry.getPhonebookContactInfoXML() != null)?
                 ((phoneBookEntry.getFirstName()==null || phoneBookEntry.getFirstName().contains("null"))?"":
                     phoneBookEntry.getFirstName()):""):
             phoneBookEntry.getFirstName());
         name.append(" ");
         name.append((phoneBookEntry.getLastName()==null || phoneBookEntry.getLastName().contains("null"))?
             ((phoneBookEntry.getPhonebookContactInfoXML()!=null)?
                 ((phoneBookEntry.getLastName()==null || phoneBookEntry.getLastName().contains("null"))?"":
                     phoneBookEntry.getLastName()):""):
             phoneBookEntry.getLastName());
        if (name.toString().trim().length()<1 || name.indexOf("Unknown")>-1) return this.getNumber();
        else return name.toString();
     }

    @Override
    public ImageIcon getUserAvatar() {
        return Utils.getDefaultAvatar();
    }

    @Override
    public String getDescription() {
    	PhonebookContactInfoXML contactInfo = phoneBookEntry.getPhonebookContactInfoXML();
    	if (phoneBookEntry.getPhonebookContactInfoXML()==null) {
    		return EMPTY;
    	}
    	StringBuilder builder = new StringBuilder();
    	builder.append(isEmpty(contactInfo.getJobTitle()) ? EMPTY : contactInfo.getJobTitle())
    		   .append(" ")
    		   .append(isEmpty(contactInfo.getJobDept()) ? EMPTY : contactInfo.getJobDept())
    		   .append(" ")
    		   .append(isEmpty(contactInfo.getCompanyName()) ? EMPTY : contactInfo.getCompanyName());
        return builder.toString();
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
        return phoneBookEntry.getNumber();
    }

    @Override
    public String getImId() {
    	return phoneBookEntry.getPhonebookContactInfoXML().getImId();
    }
}
