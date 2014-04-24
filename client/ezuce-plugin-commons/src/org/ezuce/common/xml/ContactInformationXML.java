package org.ezuce.common.xml;

public class ContactInformationXML {
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
    private boolean useBranchAddress;
    private String avatar;
    private String firstName;
    private String lastName;
    private AddressXML homeAddress;
    private AddressXML officeAddress;
    private AddressXML branchAddress;

    public ContactInformationXML() {

    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getImPassword() {
        return imPassword == null ? imId : imPassword;
    }

    public void setImPassword(String imPassword) {
        this.imPassword = imPassword;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDept() {
        return jobDept;
    }

    public void setJobDept(String jobDept) {
        this.jobDept = jobDept;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isUseBranchAddress() {
        return useBranchAddress;
    }

    public void setUseBranchAddress(boolean useBranchAddress) {
        this.useBranchAddress = useBranchAddress;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AddressXML getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(AddressXML homeAddress) {
        this.homeAddress = homeAddress;
    }

    public AddressXML getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(AddressXML officeAddress) {
        this.officeAddress = officeAddress;
    }

    public AddressXML getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(AddressXML branchAddress) {
        this.branchAddress = branchAddress;
    }

	public String getAssistantName() {
		return assistantName;
	}

	public void setAssistantName(String assistantName) {
		this.assistantName = assistantName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	public String getAssistantPhoneNumber() {
		return assistantPhoneNumber;
	}

	public void setAssistantPhoneNumber(String assistantPhoneNumber) {
		this.assistantPhoneNumber = assistantPhoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getDidNumber() {
		return didNumber;
	}

	public void setDidNumber(String didNumber) {
		this.didNumber = didNumber;
	}

	public String getImDisplayName() {
		return imDisplayName;
	}

	public void setImDisplayName(String imDisplayName) {
		this.imDisplayName = imDisplayName;
	}

	public String getAlternateImId() {
		return alternateImId;
	}

	public void setAlternateImId(String alternateImId) {
		this.alternateImId = alternateImId;
	}

	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}

	public void setAlternateEmailAddress(String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress;
	}
}
