package org.ezuce.common.xml;

/**
 *
 * @author Razvan
 */
public class ConferenceMemberXML {
	private int id;
    private String uuid;
    private String name;
    private String imId;
    private boolean canHear;
    private boolean canSpeak;
    private int energyLevel;
    private int volumeIn;
    private int volumeOut;
    
    public ConferenceMemberXML(int id, String uuid, String name, String imId,
			boolean canHear, boolean canSpeak, int energyLevel, int volumeIn,
			int volumeOut) {	
		this.uuid = uuid;
		this.name = name;
		this.imId = imId;
		this.canHear = canHear;
		this.canSpeak = canSpeak;
		this.energyLevel = energyLevel;
		this.volumeIn = volumeIn;
		this.volumeOut = volumeOut;
	}
    
    public int getId() {
    	return id;
    }
    
	public String getName() {
		return name;
	}

	public String getImId() {
		return imId;
	}

	public boolean isCanHear() {
		return canHear;
	}

	public boolean isCanSpeak() {
		return canSpeak;
	}

	public int getEnergyLevel() {
		return energyLevel;
	}

	public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getVolumeIn() {
        return volumeIn;
    }

    public void setVolumeIn(int volumeIn) {
        this.volumeIn = volumeIn;
    }

    public int getVolumeOut() {
        return volumeOut;
    }

    public void setVolumeOut(int volumeOut) {
        this.volumeOut = volumeOut;
    }                
}
