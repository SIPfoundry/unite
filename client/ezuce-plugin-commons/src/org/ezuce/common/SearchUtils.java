package org.ezuce.common;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import java.util.ArrayList;
import java.util.List;

import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;

/**
* Implements the search functionality for searching users in the ContactList
* and in the Phonebook.
*/
public class SearchUtils
{
    public final String IMUSERS="imUsers";
    public final String PHONEBOOKUSERS="phoneBookUsers";
    private static SearchUtils singleton;

    private SearchUtils() {
    }

    public static synchronized SearchUtils getInstance() {
        if (null == singleton) {
            singleton = new SearchUtils();
            return singleton;
        }
        return singleton;
    }
    
    public List<ContactListEntry> findAvailableIMUsers(String criteria) {
        List<ContactListEntry> imUsers=new ArrayList<ContactListEntry>();
            
        // Populate Invite Panel with Available users.
        final Roster roster = SparkManager.getConnection().getRoster();
        ContactList contactList = SparkManager.getWorkspace().getContactList();
        for (RosterEntry entry : roster.getEntries()) {
            Presence presence = EzucePresenceManager.getPresence(entry.getUser());
            if (presence.isAvailable()) {
                ContactItem contactItem = contactList.getContactItemByJID(entry.getUser());              
                if (contains(lowerCase(contactItem.getDisplayName()), criteria)) {
                    final ContactItemWrapper ciw = new ContactItemWrapper(contactItem, contactList.getContactGroup(contactItem.getGroupName()));
                    imUsers.add(ciw);                    
                }
            }
        }
        return imUsers;
    }

	/**
	 * Implements the search functionality for searching users in the
	 * ContactList and in the Phonebook.
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	public synchronized List<ContactListEntry> findPhonebookUsers(final String criteria) throws Exception {				
		List<ContactListEntry> phonebookEntries = CachePhoneBook.getPhoneBookEntries();				
		//search is not alowed while downloading
		if (isEmpty(criteria) || !CachePhoneBook.downloadFinished) {
			return phonebookEntries;
		} else {
			List<ContactListEntry> phonebookUsers = new ArrayList<ContactListEntry>();
			for (ContactListEntry pbe : phonebookEntries) {
				if ( contains(lowerCase(pbe.getUserDisplayName()), criteria) || contains(pbe.getNumber(), criteria)) {
					phonebookUsers.add(pbe);
				}
			}
			return phonebookUsers;
		}		
	}
        
        /**
         * Implements the search functionality for retrieving a unique user from
         * the phone book, based on their extension.
         * @param criteria
         * @return
         * @throws Exception 
         */
	public synchronized ContactListEntry findExactPhonebookUser(
			final String criteria) throws Exception {
		// search is not alowed while downloading
		if (isEmpty(criteria) || !CachePhoneBook.downloadFinished) {
			return null;
		} else {
			for (ContactListEntry pbe : CachePhoneBook.getPhoneBookEntries()) {

				if (pbe != null && pbe.getNumber() != null && pbe.getNumber().equalsIgnoreCase(criteria)) {
					return pbe;
				}
			}
			return null;
		}
	}
}
