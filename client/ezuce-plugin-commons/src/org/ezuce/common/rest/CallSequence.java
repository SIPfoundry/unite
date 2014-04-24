package org.ezuce.common.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Razvan
 */
public class CallSequence implements Serializable
{
    private List<FindMeGroup> groups=new ArrayList<FindMeGroup>();
    private Boolean withVoiceMail;
    private int expiration=0;

    public CallSequence()
    {
        this.withVoiceMail=Boolean.FALSE;
    }

    public CallSequence(List<FindMeGroup> groups, Boolean withVoiceMail, int expirationForLocalUser) {
        this.groups = groups;
        this.withVoiceMail = withVoiceMail;
        this.expiration=expirationForLocalUser;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }



    public List<FindMeGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<FindMeGroup> groups) {
        this.groups = groups;
    }    

    public Boolean getWithVoiceMail() {
        return withVoiceMail;
    }

    public Boolean isWithVoiceMail()
    {
        return getWithVoiceMail();
    }

    public void setWithVoiceMail(Boolean withVoiceMail) {
        this.withVoiceMail = withVoiceMail;
    }



}
