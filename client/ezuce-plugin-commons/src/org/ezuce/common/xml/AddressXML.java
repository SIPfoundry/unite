package org.ezuce.common.xml;

public class AddressXML {
    private String street;
    private String city;
    private String country;
    private String state;
    private String zip;
    private String officeDesignation;

    public AddressXML(){

    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

	public String getOfficeDesignation() {
		return officeDesignation;
	}

	public void setOfficeDesignation(String officeDesignation) {
		this.officeDesignation = officeDesignation;
	}       
}
