package org.ezuce.common.io;

import java.util.ArrayList;
import java.util.List;

import org.ezuce.common.rest.CallSequence;
import org.ezuce.common.rest.FindMeGroup;
import org.ezuce.common.rest.Ring;
import org.ezuce.common.xml.CallSequenceXML;
import org.ezuce.common.xml.RingXML;


/**
 *
 * @author Razvan
 */
public class FindMeFileIO extends XStreamIO {
    /**
     * Parses find me follow me xml retrieved from the server
     * @throws Exception
     */
    public CallSequence parseFindMeXml(String xml) throws Exception {
        System.out.println("*******Find Me Configuration read from server:\n"+xml);        
    	CallSequenceXML callSeqXML = parse(xml);
    	List<RingXML> listRingXML = callSeqXML.getContent();
    	
    	CallSequence cs = new CallSequence();
    	List<FindMeGroup> groups = new ArrayList<FindMeGroup>();;
    	FindMeGroup group = null;
    	Ring ring = null;
    	    	
    	for (RingXML ringXML : listRingXML) {
    		if (ringXML.getType().equals(Ring.ringTypes.get(Ring.RingType.IF_NO_RESPONSE))) {
    			group = new FindMeGroup();    			
    			groups.add(group);
    			ring = new Ring();
    			ring.setEnabled(ringXML.isEnabled());
    			ring.setExpiration(ringXML.getExpiration());
    			ring.setNumber(ringXML.getNumber());
    			ring.setType(Ring.ringTypes.get(Ring.RingType.IF_NO_RESPONSE));
    			group.add(ring);
    		} else {
    			if (group == null) {
    				group = new FindMeGroup();
    				groups.add(group);
    			}
    			ring = new Ring();
    			ring.setEnabled(ringXML.isEnabled());
    			ring.setExpiration(ringXML.getExpiration());
    			ring.setNumber(ringXML.getNumber());
    			ring.setType(Ring.ringTypes.get(Ring.RingType.AT_THE_SAME_TIME));
    			group.add(ring);
    		}    		
    	}
    	cs.setGroups(groups);
    	cs.setWithVoiceMail(callSeqXML.isWithVoicemail());
	cs.setExpiration(callSeqXML.getExpiration());
        return cs;
    }

    /**
     * Persists the information from the CallSequence instance received as an
     * argument.
     * @param callSequence
     * @return
     * @throws Exception
     */
	public String convertCallSequenceToXML(CallSequence callSequence)
			throws Exception {
		List<FindMeGroup> groups = callSequence.getGroups();
		if (groups == null || groups.isEmpty()) {
			return "";
		}
		CallSequenceXML callSeqXML = new CallSequenceXML();
		RingXML ringXML = null;
		for (FindMeGroup group : groups) {
			List<Ring> listRing = group.getRings();
			for (Ring ring : listRing) {
				ringXML = new RingXML(ring.getExpiration(), ring.getType()
						.intern(), ring.getEnabled(), ring.getNumber());
				callSeqXML.add(ringXML);
			}
		}
		callSeqXML.setWithVoicemail(callSequence.getWithVoiceMail());
		callSeqXML.setExpiration(callSequence.getExpiration());
		return toXml(callSeqXML);
	}
	@Override
	public void setAliases() {
    	setAlias("call-sequence", CallSequenceXML.class);
    	setAlias("ring", RingXML.class);
		
	}
	@Override
	public CallSequenceXML parse(String xml) throws Exception {
		return (CallSequenceXML)parseXml(xml);
	}

}
