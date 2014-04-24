package org.ezuce.common.event;

import java.util.EventObject;
import org.ezuce.common.ui.wrappers.interfaces.SearchInterface;

/**
 *
 * @author Razvan
 */
public class ContactListEntryEvent extends EventObject {
    public ContactListEntryEvent(Object source) {
        super(source);   
    }          
}