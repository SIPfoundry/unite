package org.ezuce.common.ui.actions.task;

import java.util.List;
import org.ezuce.common.SearchUtils;
import org.ezuce.common.ui.wrappers.interfaces.AudioCallPanelCommonInterface;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.jivesoftware.spark.util.log.Log;


public class SearchPhonebookUserTask extends org.jdesktop.application.Task<ContactListEntry, Void>{
    
    private List<AudioCallPanelCommonInterface> userPanels;
    private String extension;
    
    public SearchPhonebookUserTask(org.jdesktop.application.Application app, List<AudioCallPanelCommonInterface> userPanels, String extension)
    {
        super(app);
        this.userPanels = userPanels;
        this.extension = extension;
        //initially, display the extension/number, until the entire user is retrieved.
        if (this.userPanels!=null)
        {
            for (AudioCallPanelCommonInterface acpci : this.userPanels){
                acpci.displayNonPhonebookUser(extension);
            }
        }
    }
    
    @Override
    protected ContactListEntry  doInBackground() {      
        ContactListEntry result = getPhonebookUser();
        return result;
    }
    
    @Override
    protected void succeeded(final ContactListEntry result) {                 
        if(result!=null ) {
            //now, display the user panel
            //Log.error("FOUND CONTACT : "+result.getNumber()+" / "+result.getImId()+" / "+result.getUserDisplayName());
            if (this.userPanels!=null)
            {
                for (AudioCallPanelCommonInterface acpci : this.userPanels){
                    acpci.displayPhonebookUser(result);
                }
            }
        }        
        else
        {
            //keep displaying the extension/number as the user is unknown.
        }
    }
     
    @Override
    protected void finished() {
        System.gc();
    }
    
    private ContactListEntry getPhonebookUser()
    {
        
        SearchUtils su = SearchUtils.getInstance();
       //find Phonebook users matching search criteria:       
       try {    	   
           ContactListEntry cle = su.findExactPhonebookUser(extension);           
           return cle;
       } catch (Exception ex) {
           Log.error("Cannot retrieve phonebook user", ex);
       }
       return null;
    }
    
}
