package org.ezuce.common.ui;

import java.io.File;
import java.security.cert.X509Certificate;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.resource.CertificateManager;
import org.ezuce.common.resource.Config;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class CertificateDialog {
    private CertificateManager certManager = CertificateManager.getInstance();
    private Config config = Config.getInstance();
    protected final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
    
    public boolean downloadCertificate() {
    	boolean success = true;
    	X509Certificate newCert = null;
		try {
			newCert = getNewCertificate();
		} catch (CertificateException e) {
			success = false;
			Log.error(e);
		}
        if (newCert != null) {
            try {
                Object[] options = {
                	resourceMap.getString("accept.text"), resourceMap.getString("reject.text")
                };
                int n = JOptionPane.showOptionDialog(null,
                        getDialogText(newCert), resourceMap.getString("ssl.certificate"),
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    certManager.saveX509Certificate(newCert, config.getCertificatePath(), 
                    		config.getServerAddress(), false);
                } else {
                	success = false;
                }
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(null, ex.getMessage(), resourceMap.getString("certificate.error"), JOptionPane.ERROR_MESSAGE);
            	success = false;
                Log.error("No certificate to download ", ex);
            }
        }
        return success;
    }
    
    private X509Certificate getNewCertificate() throws CertificateException {
    	String sipHost = SettingsManager.getLocalPreferences().getSipHost();
    	if (!StringUtils.isEmpty(sipHost)) {
    		config.setServerAddress(sipHost);
    	}
    	String serverAddress = config.getServerAddress();        
        Log.warning("Search the certificate in servername provided by user: " + serverAddress);
        X509Certificate newCert = certManager.downloadCertificate(serverAddress);
        if (newCert == null) {
            for (String host : certManager.getSipHosts(serverAddress)) {
            	Log.warning("Search the certificate in the following DNS Record: "+host);
            	newCert = certManager.downloadCertificate(host);
            	if (newCert != null) {
            		config.setServerAddress(host);
            		break;
            	}            		
            }
            if (newCert == null) {
            	JOptionPane.showMessageDialog(null, Res.getString("no.security.certificate"), Res.getString("verge.error"), JOptionPane.ERROR_MESSAGE);
            	throw new CertificateException();
            }
        }
        if (newCert != null) {
            X509Certificate existingCert = certManager.getX509Certificate(config.getCertificatePath());
            if (existingCert == null || !existingCert.equals(newCert)) {
            	return newCert;
            }
        }
        return null;        
    }
    
    public static class CertificateException extends Exception {
    	public CertificateException() {
    		super(Res.getString("no.security.certificate"));
    	}
    }

    private JLabel getDialogText(X509Certificate cert) throws Exception {
        StringBuffer text = new StringBuffer();
        text.append("<html><b>"+resourceMap.getString("common.name")+"</b>");
        text.append("<br/>");
        text.append(certManager.getSubjectCN(cert));
        text.append("<br/><br/>");
        text.append("<b>"+resourceMap.getString("fingerprint.text")+"</b>");
        text.append("<br/>");
        text.append(certManager.getSHA1Fingerprint(cert));
        if (!certManager.isTrusted()) {
            text.append("<br/><br/>");
            text.append("<b>WARNING:</b>"+resourceMap.getString("not.trusted"));
        }
        text.append("</html>");
        return new JLabel(text.toString());
    }

}
