package org.ezuce.common.xml;

import java.util.List;

/**
 *
 * @author Razvan
 */
public class ConferenceXML {
    private List<ConferenceMemberXML> members;
    private String extension;
    private String description;
    private boolean locked;
    
    public ConferenceXML(List<ConferenceMemberXML> members, String extension,
			String description, boolean locked) {
		super();
		this.members = members;
		this.extension = extension;
		this.description = description;
		this.locked = locked;
	}

	public String getExtension() {
		return extension;
	}

	public String getDescription() {
		return description;
	}

	public boolean isLocked() {
		return locked;
	}

	public List<ConferenceMemberXML> getMembers() {
        return members;
    }       
}
