package org.ezuce.common.ui.actions.task;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ezuce.common.CachePhoneBook;
import org.ezuce.common.SearchUtils;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.SearchInterface;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.log.Log;

public class SearchTask extends org.jdesktop.application.Task<List<ContactListEntry>, Void> {
    private String search="";
    private final boolean canShowContactInfoWindow;
    private final SearchInterface callPanel2;
    private final boolean onlyRoster;
    private final boolean showPopup;
    private final boolean tabSearch;

    /***
     * Builds a new instance of SearchTask.
     * @param app - not really used.
     * @param callPanel2 - interface implemented by all classes that need the 
     *                      services of SearchTask, describing call-back methods.
     * @param ml - mouse listener defined in the calling class, with behavior 
     *              implemented in the calling class.
     * @param rm - resource map used by the calling class to retrieve various 
     *              resources that are also needed by this SearchTask instance.
     * @param extended - indicates whether the search action should be performed 
     *                  in extended mode. Extended = look into all details of a 
     *                  contact available in their VCard.
     * @param canShowContactInfoWindow - indicates if the resulting contacts of 
     *                                  the search process should be able to show their VCard.
     * @param onlyRoster - indicates if the search process should take into account 
     *                      only users with an IM id (inside the roster),who are online.
     * @param showPopup - indicates if the resulting contact groups should feature 
     *                    their specific popup menu.
     */
    public SearchTask(org.jdesktop.application.Application app,
               SearchInterface callPanel2,
               final boolean canShowContactInfoWindow, final boolean onlyRoster, final boolean showPopup, boolean tabSearch) {
        //EDT
        super(app);
                
        this.onlyRoster = onlyRoster;
        this.tabSearch = tabSearch;
        this.showPopup = showPopup;
        
        this.canShowContactInfoWindow=canShowContactInfoWindow;

        this.callPanel2=callPanel2;

        this.search=callPanel2.getjXSearchField().getText().toLowerCase();

        callPanel2.setCursor(hourglassCursor);
        callPanel2.getjXSearchField().setCursor(hourglassCursor);

        //show loading bar:
        callPanel2.getMakeCallPanel().getGroupsPanel().showLoading();
    }        

   @Override
    protected List<ContactListEntry>  doInBackground() {      
        if (onlyRoster) {
            return getAvailableUsersInRoster(false);
        } else {
            return getUsersInPhonebook();
        }
    }

    @Override
    protected void succeeded(final List<ContactListEntry> results) {  
        callPanel2.getMakeCallPanel().getGroupsPanel().hideLoading();
        callPanel2.getjXSearchField().setCursor(defaultCursor);
        callPanel2.setCursor(defaultCursor);

        if(results!=null && results.size()>0) {
        	if(tabSearch) {
        		CachePhoneBook.notifyTabListener(results, 
                                callPanel2.getMakeCallPanel().getGroupsPanel());        		       
        	} else {
        		CachePhoneBook.notifyDialogListener(results);
        	}
        }        
    }

    /**
     * Returns IM users given the search criteria. Only online users are added
     */
    private List<ContactListEntry> getAvailableUsersInRoster(boolean showPopup) {
        SearchUtils su = SearchUtils.getInstance();    
        List<ContactListEntry> imUsers = su.findAvailableIMUsers(search);       
        return imUsers;       
    }

    /**
     * Returns Phonebook users given the search criteria. The users are split into two groups:
     * 1. Users in Roster
     * 2. Users outside Roster
     * @return
     */
    private List<ContactListEntry> getUsersInPhonebook() {       
       SearchUtils su = SearchUtils.getInstance();
       //Step 2: find Phonebook users matching search criteria:
       List<ContactListEntry> phonebookUsers = new ArrayList<ContactListEntry> ();       
       try {    	   
           phonebookUsers = su.findPhonebookUsers(search);           
           return phonebookUsers;
       } catch (Exception ex) {
           Log.error("Cannot retrieve phonebook users", ex);
       }
      return new ArrayList<ContactListEntry>();
   }

    @Override
    protected void finished() {
        System.gc();
    }

    @Override
    protected void cancelled() {
        callPanel2.getjXSearchField().setCursor(defaultCursor);
        callPanel2.setCursor(defaultCursor);
        callPanel2.getMakeCallPanel().getGroupsPanel().hideLoading();
        System.gc();
    }

    private final Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
}
 