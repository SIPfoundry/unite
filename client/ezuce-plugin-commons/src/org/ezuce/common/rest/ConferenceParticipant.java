package org.ezuce.common.rest;

/**
 *
 * @author Razvan
 */
public class ConferenceParticipant 
{
    private String displayName;
    private String participantExtension;
    
    public ConferenceParticipant()
    {
        
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getParticipantExtension() {
        return participantExtension;
    }

    public void setParticipantExtension(String participantExtension) {
        this.participantExtension = participantExtension;
    }
    
    
}
