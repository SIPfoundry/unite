/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.common.rest;

import java.util.HashMap;

/**
 *
 * @author Razvan
 */
public class Address
{
    private String street;
    private String city;
    private String country;
    private String state;
    private String zip;

    public static enum AddressKeys {street, city, country, state, zip};
    private HashMap<String, String> address=new HashMap<String, String>();

    public Address()
    {

    }

    public Address(String street, String city, String country, String state, String zip)
    {
        setAttribute(AddressKeys.street.name(), street);
        setAttribute(AddressKeys.city.name(), city);
        setAttribute(AddressKeys.country.name(), country);
        setAttribute(AddressKeys.state.name(), state);
        setAttribute(AddressKeys.zip.name(), zip);
    }

    public final void setAttribute(String key, String value)
    {
        address.put(key, value);
    }

    public String getCity() {
        if (city==null)
        {
            city=address.get(AddressKeys.city.name());
        }
        return city;
    }

    public String getCountry() {
        if (country==null)
        {
            country=address.get(AddressKeys.country.name());
        }
        return country;
    }

    public String getState() {
        if (state==null)
        {
            state=address.get(AddressKeys.state.name());
        }
        return state;
    }

    public String getStreet() {
        if (street==null)
        {
            street=address.get(AddressKeys.street.name());
        }
        return street;
    }

    public String getZip() {
        if (zip==null)
        {
            zip=address.get(AddressKeys.zip.name());
        }
        return zip;
    }

    @Override
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("Address: ").append("Street: ").append(getStreet()).
                append("City: ").append(getCity()).
                append("State: ").append(getState()).
                append("Country: ").append(getCountry()).
                append("Zip: ").append(getZip());
        return sb.toString();
    }

}
