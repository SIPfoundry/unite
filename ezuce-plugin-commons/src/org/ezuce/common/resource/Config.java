package org.ezuce.common.resource;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;


import net.java.sip.communicator.impl.protocol.jabber.extensions.colibri.ColibriConferenceIQ;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.Spark;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class Config {
    private String serverAddress;
    private String keystorePath;
    private String caPassword;
    private String sipUserId;
    private String imId;
    private String password;
    private String sipPassword;
    private File certificateDir;
    private int unauthorized = 0;
    private Boolean vbridgeAvailable = null;
    private static Config singleton;
    private String videobridgeAddress;

    private Config() {
        certificateDir = new File(Spark.getSparkUserHome(), "/certificates").getAbsoluteFile();
        if (!certificateDir.exists()) {
            certificateDir.mkdirs();
        }
        keystorePath = certificateDir.getAbsolutePath() + File.separatorChar + "sipx.keystore";
        caPassword = "changeit";
    }

    public static synchronized Config getInstance() {
        if (null == singleton) {
            singleton = new Config();
            return singleton;
        }
        return singleton;
    }

    public String getServerAddress() {
        return serverAddress;
    }
    public String getCertificatePath() {
        return certificateDir.getAbsolutePath() + File.separatorChar + "ca." + serverAddress + ".crt";
    }
    public String getKeystorePath() {
        return keystorePath;
    }

    public String getCaPassword() {
        return caPassword;
    }

	public String getSipUserId() {
		return sipUserId;
	}

	public void setSipUserId(String sipUserId) {
		this.sipUserId = sipUserId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getSipPassword() {
		return sipPassword;
	}

	public void setSipPassword(String sipPassword) {
		this.sipPassword = sipPassword;
	}
	
	public boolean isRegisterAsPhone() {
        Properties props = SettingsManager.getLocalPreferences().getProperties();
        return Boolean.parseBoolean(props.getProperty("registerAsPhoneEnabled", "false"));
	}
	
	public boolean findVideobridge() {        
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(SparkManager.getConnection());
        DiscoverItems items = null;
		try {
			items = discoManager.discoverItems(SparkManager.getConnection().getServiceName());
		} catch (XMPPException e) {
			Log.error("Cannot discover feature items");
		}
		if (items == null) {
			return false;
		}
        for (Iterator<Item> it = items.getItems(); it.hasNext();) {
            Item item = it.next();
            String entityID = item.getEntityID();
            DiscoverInfo discoverInfo = null;
            try {
            	discoverInfo = discoManager.discoverInfo(entityID);
            } catch (XMPPException xmppe) {
            	Log.error("Cannot idscover info for entity: " + entityID);
            }
            if ((discoverInfo != null)
                    && discoverInfo.containsFeature(
                            ColibriConferenceIQ.NAMESPACE)) {
                return !StringUtils.isEmpty(entityID);
            }            
            
        }
        return false;	
	}
	
	public boolean isVideobridgeAvailable() {
		if (vbridgeAvailable == null) {
			vbridgeAvailable = findVideobridge();
		}
		return vbridgeAvailable;
	}
	
	public String getVideobridgeJid() {
		return "jitsi-videobridge." + SparkManager.getConnection().getServiceName() ;
	}

	public void incrementUnauthorized() {
		if (SparkManager.getSessionManager().getConnection() != null) {
			unauthorized++;
		} else {
			unauthorized = 0;
		}
	}

	public String getVideobridgeAddress() {
		return videobridgeAddress;
	}
	
	public void setVideobridgeAddress(String videobridgeAddress) {
		this.videobridgeAddress = videobridgeAddress;
	}

    public boolean checkAuthorization() {
    	if (unauthorized >= 3) {
    		Presence byePresence = new Presence(Presence.Type.unavailable, "Not Authorized", -1, null);
    		SparkManager.getSessionManager().getConnection().disconnect(byePresence);
    		SparkManager.getContactList().setReconnectReason(Res.getString("message.disconnected.authentication.error"));
    		unauthorized = 0;
    		return false;
    	}
    	return true;
    }
    
}
