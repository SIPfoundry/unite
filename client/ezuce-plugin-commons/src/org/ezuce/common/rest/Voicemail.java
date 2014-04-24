package org.ezuce.common.rest;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.xml.VoicemailMessageXML;


public class Voicemail {
    private VoicemailMessageXML message;
    String uri;
    private boolean heard;

    public Voicemail(VoicemailMessageXML message, String uri) {
    	this.message = message;
    	this.uri = uri;
    	this.heard = message.isHeard();
    }

    public String getFormat() {
        return this.message.getFormat();
    }
    
    public String getId() {       
        return message.getId();
    }

    public boolean isHeard() {        
        return heard;
    }
    
    public void setHeard(boolean heard) {
		this.heard = heard;
	}

	public boolean isLast24Hrs() throws ParseException {
        return Utils.isLast24Hours3(getDate());
    }
    
    public boolean isLastMonth() throws ParseException {
        return Utils.isLastMonth3(getDate());
    }    

    public boolean isDeleted() {       
        return StringUtils.equals(Folder.DELETED.toString(), message.getFolder());
    }

    public String getAuthor() {        
        return message.getAuthor();
    }
    
    public String getAuthorExtension() {
    	return message.getAuthorExtension();
    }

    public String getDuration() {
        return message.getDuration();
    }

    public String getDate() {
        return Utils.getDateToDisplay(new Long(message.getReceived()).longValue());
    }

    public String getUri() {       
        return uri;
    }
    
    public String getAuthorId() {
        return message.getAuthor();
    }
    
    public String getFolder() {
    	return message.getFolder();
    }
}
