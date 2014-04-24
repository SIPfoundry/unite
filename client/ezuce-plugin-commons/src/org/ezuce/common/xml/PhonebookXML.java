package org.ezuce.common.xml;

import java.util.List;

public class PhonebookXML {
	private int size;
	private int filteredSize;
	private int startRow;
	private int endRow;
	private String showOnPhone;
	private String googleDomain;
	private List<PhonebookEntryXML> entries;
	public PhonebookXML(int size, int filteredSize, int startRow, int endRow, String showOnPhone, String googleDomain,
			List<PhonebookEntryXML> entries) {
		super();
		this.size = size;
		this.filteredSize = filteredSize;
		this.startRow = startRow;
		this.endRow = endRow;
		this.showOnPhone = showOnPhone;
		this.googleDomain = googleDomain;
		this.entries = entries;
	}
	public int getSize() {
		return size;
	}
	public int getFilteredSize() {
		return filteredSize;
	}
	public int getStartRow() {
		return startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	
	public String getShowOnPhone() {
		return showOnPhone;
	}
	public String getGoogleDomain() {
		return googleDomain;
	}
	public List<PhonebookEntryXML> getEntries() {
		return entries;
	}
	
}
