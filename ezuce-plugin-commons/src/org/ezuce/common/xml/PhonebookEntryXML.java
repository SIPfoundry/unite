package org.ezuce.common.xml;

public class PhonebookEntryXML {
	private int id;
	private String firstName;
	private String lastName;
	private String number;
	private PhonebookContactInfoXML phonebookContactInfoXML;
	public PhonebookEntryXML(int id, String firstName, String lastName,
			String number, PhonebookContactInfoXML phonebookContactInfoXML) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
		this.phonebookContactInfoXML = phonebookContactInfoXML;
	}
	public int getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getNumber() {
		return number;
	}
	public PhonebookContactInfoXML getPhonebookContactInfoXML() {
		return phonebookContactInfoXML;
	}
	
}
