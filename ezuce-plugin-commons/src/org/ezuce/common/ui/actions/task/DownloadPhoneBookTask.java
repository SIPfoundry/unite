package org.ezuce.common.ui.actions.task;


import java.util.ArrayList;
import java.util.List;
import org.ezuce.common.CachePhoneBook;
import org.ezuce.common.Paginator;
import org.ezuce.common.resource.RestRes;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.PhoneBookEntryWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.xml.PhonebookEntryXML;
import org.ezuce.common.xml.PhonebookXML;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class DownloadPhoneBookTask extends org.jdesktop.application.Task<List<PhonebookEntryXML>, Void> {
    private static org.jdesktop.application.Application app;
    
   public DownloadPhoneBookTask(org.jdesktop.application.Application app) throws Exception {
       super(app);
       DownloadPhoneBookTask.app = app;
       
       List<ContactListEntry> imList = CachePhoneBook.getContactList();       
       
       PhonebookXML phonebook = RestManager.getInstance().getFirstPhoneBookPage();
       List<PhonebookEntryXML> list = phonebook.getEntries();
       
       int phonebookSize = phonebook.getSize();
       
       Paginator.DOWNLOADED_ENTRIES = list == null ? 0 : list.size();
       Paginator.PHONEBOOK_ENTRIES = phonebookSize;
       
       CachePhoneBook.addElements(imList);       
       
       if(list!=null && list.size()>0) {
           Paginator.CURRENT_PAGE = 1;
           Paginator.CURRENT_PAGETAB = 1;
           List<ContactListEntry> entries = new ArrayList<ContactListEntry>();
		   for (PhonebookEntryXML result : list) {
			   entries.add(new PhoneBookEntryWrapper(result));
		   }
			if(Paginator.USERS_PER_PAGE >= Paginator.PHONEBOOK_ENTRIES) {
				CachePhoneBook.downloadFinished = true;
			}
           CachePhoneBook.addElements(entries);
       } else {
    	   //nothing found in phonebook
    	   CachePhoneBook.downloadFinished = true;
       }
   }

	@Override
	protected List<PhonebookEntryXML> doInBackground() throws Exception {
		int start = Paginator.USERS_PER_PAGE;
		while (start < Paginator.PHONEBOOK_ENTRIES) {
			Log.warning("*** Retrieved " + CachePhoneBook.getCacheSize() + " from " + Paginator.PHONEBOOK_ENTRIES);
			List<PhonebookEntryXML> results = RestManager.getInstance().getPhoneBook(
						start, start + Paginator.USERS_PER_PAGE).getEntries();
			List<ContactListEntry> entries = new ArrayList<ContactListEntry>();
			for (PhonebookEntryXML result : results) {
				entries.add(new PhoneBookEntryWrapper(result));
			}			
			start +=Paginator.USERS_PER_PAGE;
			Paginator.DOWNLOADED_ENTRIES = start;
			if(start >= Paginator.PHONEBOOK_ENTRIES) {
				CachePhoneBook.downloadFinished = true;
			}
			CachePhoneBook.addElements(entries);
			Thread.sleep(1000);
		}		
		return null;
	}
   
}