package org.ezuce.common;

import org.ezuce.common.resource.RestRes;

/**
 *
 * @author Razvan
 */
public class Paginator {
    public static int CURRENT_PAGE;
    public static int CURRENT_PAGETAB;
    public static int CURRENT_PAGEDIALOG;
    public static int PHONEBOOK_ENTRIES;
    public static int DOWNLOADED_ENTRIES;
    public static int USERS_PER_PAGE = Integer.parseInt(RestRes.getValue(RestRes.USERS_PER_PAGE));
    
    public static int getNoDownloadedPages() {
        if(CachePhoneBook.getCacheSize() % USERS_PER_PAGE==0) {
            return CachePhoneBook.getCacheSize() / USERS_PER_PAGE;
        }
        return (CachePhoneBook.getCacheSize() / USERS_PER_PAGE)+1;
    }   	          
	
	public static int getNoPages(int size) {
        if(size % USERS_PER_PAGE==0) {
            return size / USERS_PER_PAGE;
        }
        return (size / USERS_PER_PAGE)+1;
	}
}