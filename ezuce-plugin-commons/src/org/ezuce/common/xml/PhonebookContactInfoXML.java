package org.ezuce.common.xml;

public class PhonebookContactInfoXML {
    private String imId;
    private String imPassword;
    private String jobTitle;
    private String jobDept;
    private String assistantName;
    private String location;
    
    private String cellPhoneNumber;
    private String homePhoneNumber;
    private String assistantPhoneNumber;
    private String faxNumber;
    private String didNumber;
    private String companyName;
    private String emailAddress;
    private String imDisplayName;
    private String alternateImId;
    private String alternateEmailAddress;
    private String avatar;
    private AddressXML homeAddress;
    private AddressXML officeAddress;
	public PhonebookContactInfoXML(String imId, String imPassword,
			String jobTitle, String jobDept, String assistantName,
			String location, String cellPhoneNumber, String homePhoneNumber,
			String assistantPhoneNumber, String faxNumber, String didNumber,
			String companyName, String emailAddress, String imDisplayName,
			String alternateImId, String alternateEmailAddress, String avatar,
			AddressXML homeAddress, AddressXML officeAddress) {
		super();
		this.imId = imId;
		this.imPassword = imPassword;
		this.jobTitle = jobTitle;
		this.jobDept = jobDept;
		this.assistantName = assistantName;
		this.location = location;
		this.cellPhoneNumber = cellPhoneNumber;
		this.homePhoneNumber = homePhoneNumber;
		this.assistantPhoneNumber = assistantPhoneNumber;
		this.faxNumber = faxNumber;
		this.didNumber = didNumber;
		this.companyName = companyName;
		this.emailAddress = emailAddress;
		this.imDisplayName = imDisplayName;
		this.alternateImId = alternateImId;
		this.alternateEmailAddress = alternateEmailAddress;
		this.avatar = avatar;
		this.homeAddress = homeAddress;
		this.officeAddress = officeAddress;
	}
	public String getImId() {
		return imId;
	}
	public String getImPassword() {
		return imPassword;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public String getJobDept() {
		return jobDept;
	}
	public String getAssistantName() {
		return assistantName;
	}
	public String getLocation() {
		return location;
	}
	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}
	public String getAssistantPhoneNumber() {
		return assistantPhoneNumber;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public String getDidNumber() {
		return didNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public String getImDisplayName() {
		return imDisplayName;
	}
	public String getAlternateImId() {
		return alternateImId;
	}
	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}
	public String getAvatar() {
		return avatar;
	}	
	public AddressXML getHomeAddress() {
		return homeAddress;
	}
	public AddressXML getOfficeAddress() {
		return officeAddress;
	}        
}
