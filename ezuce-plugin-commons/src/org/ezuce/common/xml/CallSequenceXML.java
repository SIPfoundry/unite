package org.ezuce.common.xml;

import java.util.ArrayList;
import java.util.List;

public class CallSequenceXML {
	private List<RingXML> rings = new ArrayList<RingXML>();
	private boolean withVoicemail;
        private int expiration;

    public CallSequenceXML()
        {

        }

    public CallSequenceXML(boolean withVoicemail, int expiration, List<RingXML> rings) {
        this.withVoicemail = withVoicemail;
        this.expiration = expiration;
        this.rings=rings;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public List<RingXML> getRings() {
        return rings;
    }

    public void setRings(List<RingXML> rings) {
        this.rings = rings;
    }



	public boolean isWithVoicemail() {
		return withVoicemail;
	}
	
	public void setWithVoicemail(boolean withVoicemail) {
		this.withVoicemail = withVoicemail;
	}
	
    public void add(RingXML ring) {
    	rings.add(ring);
    }

    public List<RingXML> getContent() {
        return rings;
    }	
}
