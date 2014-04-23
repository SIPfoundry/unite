package org.ezuce.common.rest;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.ezuce.common.xml.PhonebookEntryXML;

/**
 *
 * @author Razvan
 */
public class Ring implements Serializable {
    public static enum RingType { IF_NO_RESPONSE, AT_THE_SAME_TIME }
    public static final Map<RingType,String> ringTypes;
    static {
        Map<RingType, String> tmp=new EnumMap<RingType, String>(RingType.class);
        tmp.put(RingType.IF_NO_RESPONSE, "If no response");
        tmp.put(RingType.AT_THE_SAME_TIME, "At the same time");
        ringTypes=Collections.unmodifiableMap(tmp);
    }

    private int expiration;
    private RingType type;
    private Boolean enabled;
    private String number;

    public Ring() {
    }

    public Ring(int expiration, RingType type, Boolean enabled, String number) {
        this.expiration = expiration;
        this.type = type;
        this.enabled = enabled;
        this.number = number;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean isEnabled()
    {
        return getEnabled();
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return ringTypes.get(this.type);
    }
    
    public RingType getRingType() {
        return this.type;
    }

    public void setType(RingType type) {
        this.type=type;
    }
    
    public void setType(String type) {
        boolean found=false;
        for (RingType rt:ringTypes.keySet()) {
            if (ringTypes.get(rt).equals(type)) {
                this.type = rt;
                found=true;
                break;
            }
        }  
        if (!found) { //if could not identify the type, put the default value as IF_NO_RESPONSE
            this.type=Ring.RingType.IF_NO_RESPONSE;
        }
    }


}
