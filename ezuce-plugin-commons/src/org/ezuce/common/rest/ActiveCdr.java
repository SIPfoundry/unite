package org.ezuce.common.rest;

import java.util.HashMap;
import org.ezuce.common.resource.SipUriUtil;
import org.ezuce.common.xml.ActiveCdrXML;

/**
 *
 * @author Razvan
 */
public class ActiveCdr {
	private ActiveCdrXML activeCdrXML;           

    public ActiveCdr(ActiveCdrXML activeCdrXML) {
    	this.activeCdrXML = activeCdrXML;
    }    

    public String getCallee() {       
        return SipUriUtil.extractUser(activeCdrXML.getToAor());
    }

    public String getCalleeAor() {      
        return SipUriUtil.extractUserName(activeCdrXML.getToAor());
    }

    public String getCaller() {        
        return SipUriUtil.extractUser(activeCdrXML.getFromAor());
    }

    public String getCallerAor() {     
        return SipUriUtil.extractUserName(activeCdrXML.getFromAor());
    }

    public String getDirection() {       
        return activeCdrXML.getDirection();
    }

    public long getDuration() {       
        return activeCdrXML.getDuration();
    }

    public boolean getInternal() {        
        return activeCdrXML.isInternal();
    }

    public String getRecipient() {      
        return activeCdrXML.getRecipient();
    }

    public long getStartTime() {        
        return activeCdrXML.getStartTime();
    }
}
