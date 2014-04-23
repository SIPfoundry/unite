package org.ezuce.common.resource;

import static org.ezuce.common.IOUtils.closeStreamQuietly;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.jivesoftware.spark.util.log.Log;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class CertificateManager {

    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private static final String X509_TYPE = "X.509";
    private static final String BEGIN_LINE = "-----BEGIN CERTIFICATE-----";
    private static final String END_LINE = "-----END CERTIFICATE-----";
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String UTF8_CHARSET = "UTF-8";

    private static CertificateManager singleton;
    private boolean trusted;
    
    private Config config = Config.getInstance();

    public static synchronized CertificateManager getInstance() {
        if (null == singleton) {
            singleton = new CertificateManager();
            return singleton;
        }
        return singleton;
    }
    
	public X509Certificate downloadCertificate(String fqdn) {
		try {
			SSLContext context = SSLContext.getInstance("SSL");

			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			File sipxStore = new File(Config.getInstance().getKeystorePath());
			if (sipxStore.exists()) {
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(new FileInputStream(sipxStore), Config.getInstance()
						.getCaPassword().toCharArray());
				tmf.init(ks);
			} else {
				tmf.init((KeyStore) null);
			}

			X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
			CertificateTrustManager tm = new CertificateTrustManager(defaultTrustManager);

			context.init(null, new TrustManager[] { tm }, null);

			SSLSocketFactory socketFactory = context.getSocketFactory();
			SSLSocket socket = (SSLSocket) socketFactory.createSocket(fqdn, 443);
			socket.setSoTimeout(10000);
			Log.debug("Starting SSL handshake...");
			socket.startHandshake();
			socket.close();
			if (trusted) {
				Log.debug("No errors, certificate is already trusted");
			} else {
				Log.warning("The certificate is not trusted");
			}

			X509Certificate[] chain = tm.chainCert;
			for (X509Certificate cert : chain) {
				Log.warning("Certificate successfully downloaded from: "+fqdn);
				return cert;
			}
		} catch (Exception ex) {
			Log.warning("Cannot download certificate from host: "+fqdn, ex);
		}
		return null;
	}

    public boolean isTrusted() {
        return trusted;
    }

    public X509Certificate getX509Certificate(String certificatePath) {
        X509Certificate cert = null;
        InputStream is = null;
        BufferedReader in = null;
        try {
            File sslCertFile = new File(certificatePath);
            if (!sslCertFile.exists()) {
                return null;
            }
            in = new BufferedReader(new FileReader(sslCertFile));

            StringBuffer rep = new StringBuffer();
            boolean shouldWrite = false;
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.equals(BEGIN_LINE)) {
                    shouldWrite = true;
                }
                if (shouldWrite) {
                    rep.append(line);
                    rep.append(NEW_LINE);
                }
            }

            is = new ByteArrayInputStream(rep.toString().getBytes());
            CertificateFactory cf = CertificateFactory.getInstance(X509_TYPE);
            cert = (X509Certificate) cf.generateCertificate(is);
        } catch (Exception ex) {
            cert = null;
        } finally {
            try {
            	if (is!=null) {
            		is.close();
            	}
            	if (in != null) {
            		in.close();
            	}
            } catch (IOException ex) {
            	Log.error("Error closing certificate streams", ex);
            }
        }
        return cert;
    }

    public void saveX509Certificate(X509Certificate cert, String certPath, String alias, boolean binary)
            throws Exception {
        // Get the encoded form which is suitable for exporting       
        byte[] buf = cert.getEncoded();

        File certFile = new File(certPath);
        if(!certFile.exists()) {
        	certFile.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(certFile);
        if (binary) {
            // Write in binary form
            os.write(buf);
        } else {
            // Write in text form
            Writer wr = new OutputStreamWriter(os, Charset.forName(UTF8_CHARSET));
            wr.write(BEGIN_LINE+"\n");
            wr.write(new Base64Encoder().encode(buf));
            wr.write("\n"+END_LINE+"\n");
            wr.flush();
        }
        closeStreamQuietly(os);
        //add certificate to keystore
        
        KeyStore ks = getKeyStore();
        ks.setCertificateEntry(alias, cert);
        ks.store(new FileOutputStream(config.getKeystorePath()), config.getCaPassword().toCharArray());        
    }
    
	public KeyStore getKeyStore() throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		if (!new File(config.getKeystorePath()).exists()) {
			ks.load(null, null);
		} else {
			InputStream in = new FileInputStream(config.getKeystorePath());
			ks.load(in, null);
			closeStreamQuietly(in);
		}
		return ks;
	}    

    public String getSubjectCN(X509Certificate cert) throws Exception {
        return getCN(cert.getSubjectDN().getName());
    }

    public String getIssuerCN(X509Certificate cert) throws Exception {
        return getCN(cert.getIssuerDN().getName());
    }

    private String getCN(String dn) {
        int i = 0;
        i = dn.indexOf("CN=");
        if (i == -1) {
            return null;
        }
        //get the remaining DN without CN=
        dn = dn.substring(i + 3);
        char[] dncs = dn.toCharArray();
        for (i = 0; i < dncs.length; i++) {
            if (dncs[i] == ','  && i > 0 && dncs[i - 1] != '\\') {
                break;
            }
        }
        return dn.substring(0, i);
    }

    public String getSHA1Fingerprint(X509Certificate cert) {
        try {
            return getFingerprint(cert, MessageDigest.getInstance("SHA1"));
        } catch (Exception ex) {
            return null;
        }
    }

    public String getMD5Fingerprint(X509Certificate cert) {
        try {
             return getFingerprint(cert, MessageDigest.getInstance("MD5"));
        } catch (Exception ex) {
            return null;
        }
    }

    private String getFingerprint(X509Certificate cert, MessageDigest md) throws Exception {
        try {
            md.update(cert.getEncoded());
            return toHexString(md.digest());
        } catch (CertificateEncodingException ex) {
            return null;
        }
    }

    private String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }
    
    public List<String> getSipHosts(String serverAddress) {
    	List<String> hosts = new ArrayList<String>();
        try {                        
            Lookup lookup = new Lookup("_sip._tcp." + serverAddress, Type.ANY);
            Record[] records = lookup.run();
            SRVRecord srvRecord = null;
            AAAARecord aaaaRecord = null;
            ARecord aRecord = null;
            if (records == null) {                
                return hosts;
            }
            String host = null;
            for (Record record : records) {                
                if (record instanceof SRVRecord) {
                    srvRecord = (SRVRecord) record;
                    host = Utils.removeDotIfLast(srvRecord.getTarget().toString());
                    hosts.add(host);
                    Log.warning("(SRV) SIP hostname: " + host);
                } else if (record instanceof AAAARecord) {
                    aaaaRecord = (AAAARecord) record;
                    host = Utils.removeDotIfLast(aaaaRecord.rdataToString());
                    hosts.add(host);
                    Log.warning("(AAAA) SIP hostname: " + host);
                } else if (record instanceof ARecord) {
                    aRecord = (ARecord) record;
                    host = Utils.removeDotIfLast(aRecord.rdataToString());
                    hosts.add(host);
                    Log.warning("(A) SIP hostname: " + host);
                }
            }            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hosts;
    }     

    private class CertificateTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chainCert;

        CertificateTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          X509Certificate[] issuers = tm.getAcceptedIssuers();
          return issuers;
        }


        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.chainCert = chain;
            try {
                tm.checkServerTrusted(chainCert, authType);
                trusted = true;
            } catch (Exception ex) {
                Log.warning("Cannot check if the server is trusted ");
                trusted = false;
            }
        }
    }

}
