package org.ezuce.common;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.ezuce.common.event.CachePhoneBookObservable;
import org.ezuce.common.event.ContactListEntryEvent;
import org.ezuce.common.ui.panels.FooterPanel;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;

/**
 *
 * @author Razvan
 */
 public class CachePhoneBook implements CachePhoneBookObservable {
    public static List<FooterPanel> tabListener = new ArrayList<FooterPanel>();
    public static FooterPanel dialogListener = null;
    private static final LinkedHashSet<ContactListEntry> cachePhoneBook=new LinkedHashSet<ContactListEntry>();
    
    public static boolean downloadFinished = false;

    public static synchronized int getCacheSize() {
        return cachePhoneBook.size();
    }       

    public static synchronized List<ContactListEntry> getPhoneBookEntries() {
         return new ArrayList(cachePhoneBook);
    } 
            
    public static void notifyTabListener(List<ContactListEntry> results, Object sourceToNotify) {
        CachePhoneBook cf = new CachePhoneBook();
        ContactListEntryEvent ev = new ContactListEntryEvent(results);
        cf.fireTab(ev, sourceToNotify);
    }
    
    public static void notifyDialogListener(List<ContactListEntry> results) {
        CachePhoneBook cf = new CachePhoneBook();
        ContactListEntryEvent ev = new ContactListEntryEvent(results);
        cf.fireDialog(ev);
    }
    
    public static List<ContactListEntry> getContactList() {
    	ContactList contactList = SparkManager.getWorkspace().getContactList();
    	List<ContactGroup> groups = contactList.getContactGroups();
    	List<ContactItem> contactItems = null;
    	List<ContactListEntry> list = new ArrayList<ContactListEntry>();
    	for (ContactGroup group : groups) {
    		contactItems = group.getContactItems();
    		for (ContactItem contactItem : contactItems) {
    			list.add(new ContactItemWrapper(contactItem, group));    			
    		}
    	}
    	return list;
    }
     
    
    public static synchronized void addElements(List<ContactListEntry> elems) {
        cachePhoneBook.addAll(elems);
        notifyTabDownload(new ContactListEntryEvent(new ArrayList<ContactListEntry>(cachePhoneBook)));
        if (dialogListener!=null && !dialogListener.isOnlyRoster()) {
        	notifyDialogDownload(new ContactListEntryEvent(new ArrayList<ContactListEntry>(cachePhoneBook)));
        }
    }
    
    public static synchronized void addTabListener(FooterPanel fp)
    {
        if (!tabListener.contains(fp))
        {
            tabListener.add(fp);
        }
    }
    
    public void fireDialog(ContactListEntryEvent event) {
        if(dialogListener != null) {
        	dialogListener.finishWorker(event);
        }    	
    }
    
    public void fireTab(ContactListEntryEvent event, Object sourceToNotify) {
    	if(tabListener != null && !tabListener.isEmpty()) {
            for (FooterPanel fp:tabListener)
            {
                //Notify only the interested source:
                if (fp.getGroupsPanel().equals(sourceToNotify))
                {
                    fp.finishWorker(event);
                }
            }
    	}    
    }    	
        
    private static void notifyTabDownload(ContactListEntryEvent event) {
    	if(tabListener != null && !tabListener.isEmpty()) {
            for (FooterPanel fp: tabListener)	
            {
                fp.updateProgress(event);
            }
    	}
    }
    
    private static void notifyDialogDownload(ContactListEntryEvent event) {
        if(dialogListener != null) {
        	dialogListener.updateProgress(event);
        }
    }          
}