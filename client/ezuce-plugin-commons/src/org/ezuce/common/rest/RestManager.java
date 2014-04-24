package org.ezuce.common.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.Paginator;
import org.ezuce.common.resource.Config;
import org.ezuce.common.resource.RestRes;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.io.CallsIO;
import org.ezuce.common.io.ActiveCdrsIO;
import org.ezuce.common.io.ConferenceDetailsIO;
import org.ezuce.common.io.MyConferencesIO;
import org.ezuce.common.io.VoicemailMessagesIO;
import org.ezuce.common.xml.ActiveCdrXML;
import org.ezuce.common.xml.CallXML;
import org.ezuce.common.xml.ConferenceMemberXML;
import org.ezuce.common.xml.ConferenceXML;
import org.ezuce.common.xml.MyConferenceXML;
import org.ezuce.common.xml.VoicemailMessageXML;
import org.ezuce.common.io.LoginDetailsIO;
import org.ezuce.common.xml.LoginDetailsXML;
import org.ezuce.common.xml.PhonebookEntryXML;
import org.ezuce.common.io.PhonebookIO;
import org.ezuce.common.xml.PhonebookXML;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;
import static org.ezuce.common.IOUtils.closeStreamQuietly;

public class RestManager {

    private static RestManager singleton;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static Config config = Config.getInstance();
    private static final String PRODUCT_NAME = "openUC";
    private static final String NO_RECORDING_RESPONSE = "NO_RECORDING";
    private String latestDialedNumber;

    /**
     * Create rest singleton - use https protocol and server certificate
     */
    private RestManager() {
    }

    public void init() {
        Protocol https = null;
        try {
            https = new Protocol("https", new CertificateSocketFactory(), 443);
        } catch (Exception ex) {
            // https = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
            ex.printStackTrace();
        }
        Protocol.registerProtocol("https", https);

    }

    public static synchronized RestManager getInstance() {
        if (null == singleton) {
            singleton = new RestManager();
            return singleton;
        }
        return singleton;
    }

    private String getRestServerUrl(int port, boolean ssl) {
        String fqdn = Config.getInstance().getServerAddress();
        return ssl ? String.format("https://%s:%d", fqdn, port) : String.format("http://%s:%d", fqdn, port);
    }

    /**
     * Retrieve http client with alternative authentication capabilities (Basic
     * or Digest)
     *
     * @param fqdn
     * @param port
     * @return
     */
    private HttpClient getHttpClient(int port) {
        String fqdn = config.getServerAddress();
        HttpClient client = new HttpClient();
        List<String> authPrefs = new ArrayList<String>(2);

        authPrefs.add(AuthPolicy.BASIC);
        authPrefs.add(AuthPolicy.DIGEST);

        client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);

        Credentials credentials = new UsernamePasswordCredentials(config.getSipUserId(), config.getPassword());
        client.getState().setCredentials(new AuthScope(fqdn, port, AuthScope.ANY_REALM), credentials);

        return client;
    }

    public boolean isOpenUc() {
        String uri = RestRes.getValue(RestRes.OPENUC_URI);
        int port = Integer.parseInt(RestRes.getValue(RestRes.SIPXCONFIG_PORT));
        byte[] content = invokeRestService(port, getRestServerUrl(port, true) + uri, GET, null, null);
        return content != null && new String(content).equals(PRODUCT_NAME);
    }

    /**
     * THis is a generic method used for invoking a REST service
     *
     * @param port = REST service port
     * @param uri = REST service relative URI
     * @param method = REST method to call
     * @param ssl = use https protocol
     * @param fis = send stream in request (useful for POST/PUT methods)
     * @param contentType = mime type
     * @param basic = true when basic authentication should be used - instead of
     * digest
     * @return - response
     */
    private byte[] invokeRestService(int port, String restUrl, String method, InputStream fis, String contentType) {
    	if (!config.checkAuthorization()) {
    		return null;
    	}
        byte[] response = null;
        HttpMethodBase methodBase = null;
        InputStream br = null;
        ByteArrayOutputStream outputStream = null;
        try {
            HttpClient client = getHttpClient(port);
            if (method.equals(GET)) {
                methodBase = new GetMethod(restUrl);
            } else if (method.equals(PUT)) {
                methodBase = new PutMethod(restUrl);
            } else if (method.equals(POST)) {
                methodBase = new PostMethod(restUrl);
            } else {
                methodBase = new DeleteMethod(restUrl);
            }
            methodBase.setDoAuthentication(true);

            //always use BASIC +SSL
            String authHeader = "Basic " + new String(Base64.encodeBase64((config.getSipUserId() + ":" + config.getPassword()).getBytes()));
            methodBase.setRequestHeader("Authorization", authHeader);

            //Some of the rest services used here require entity not null
            if (methodBase instanceof EntityEnclosingMethod) {
                RequestEntity re = null;
                if (fis != null) {
                    re = new InputStreamRequestEntity(fis, contentType);
                } else {
                    re = new ByteArrayRequestEntity(new byte[]{0});
                }
                ((EntityEnclosingMethod) methodBase).setRequestEntity(re);
            }

            int statusCode = client.executeMethod(methodBase);
            if (statusCode == HttpStatus.SC_OK) {
                br = methodBase.getResponseBodyAsStream();
                outputStream = new ByteArrayOutputStream();
                int n;
                byte[] buffer = new byte[1024];
                while ((n = br.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, n);
                }
                response = outputStream.toByteArray();
            } else {
            	if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            		config.incrementUnauthorized();
            	}
                Log.warning("Rest URL invoked: " + restUrl + " STATUS_CODE: " + statusCode);
            }
        } catch (Exception e) {
            Log.error("Cannot execute rest call:" + restUrl, e);
        } finally {
            if (methodBase != null) {
            	try {
            		methodBase.releaseConnection();
            	} catch (Exception ex) {
            		Log.error("Cannot release HTTP connection");
            	}
            }
            closeStreamQuietly(br);
            closeStreamQuietly(fis);
            closeStreamQuietly(outputStream);
        }
        return response;
    }

    public byte[] getVoicemailContent(Voicemail voicemail) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = voicemail.getUri();
        byte[] response = invokeRestService(Integer.parseInt(port), uri, GET, null, null);

        return response;
    }

    public boolean configureForwarding(String xmlFindMeCfg) throws Exception {
        if (xmlFindMeCfg == null) {
            return false;
        }
        System.out.println("\n**********Saving forwarding XML:\n" + xmlFindMeCfg);
        InputStream is = new ByteArrayInputStream(xmlFindMeCfg.getBytes());
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CALL_FORWARD_FINDME_URI, config.getSipUserId());
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, PUT, is, "text/xml");
        closeStreamQuietly(is);
        return response != null;
    }

    public String retrieveForwarding() throws Exception {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CALL_FORWARD_FINDME_URI, config.getSipUserId());
        byte[] xmlByte = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, "text/xml");
        return xmlByte != null ? new String(xmlByte) : null;
    }

    public boolean makeCall(String calee) throws Exception {
        String port = RestRes.getValue(RestRes.CALL_PORT);
        String uri = RestRes.getValue(RestRes.CALL_URI, config.getSipUserId(), calee, config.getSipUserId());
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, POST, null, null);
        if (!StringUtils.isEmpty(calee)) {
            latestDialedNumber = calee;
        }
        return response != null;
    }

    private String getActiveCallsXML() {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.ACTIVE_CALL_URI);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    /**
     * Get active call: /sipxconfig/rest/my/activecdrs on port 8443
     */
    public List<ActiveCdr> getActiveCalls() throws Exception {
        List<ActiveCdr> activeCalls = new ArrayList<ActiveCdr>();
        String response = getActiveCallsXML();// REST response.
        if (response != null) {
            activeCalls = getActiveCallEntries(response);
        }
        return activeCalls;
    }

    /**
     * When user1 calls user 2, user1 can transfer the call to user3. Before the
     * transfer can take place, user1 must know which call will be transfered,
     * by calling
     * <code>getActiveCall()</code> Transfer the call
     * /callcontroller/user1/user2?target=user3&action=transfer&agent=user1
     *
     * @param fromUser - user1
     * @param initialToUser - user2
     * @param toUser - user3
     */
    public boolean transferCall(String fromUser, String initialToUser, String toUser) {
        String port = RestRes.getValue(RestRes.CALL_PORT);
        String uri = RestRes.getValue(RestRes.TRANSFER_CALL_URI, initialToUser, toUser, fromUser);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, POST, null, null);
        return response != null;
    }

    public boolean inviteToBridge(String toUser, String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.INVITE_TO_BRIDGE_URI, conferenceName, toUser);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null;
    }

    public boolean inviteToBridgeIm(String toUser, String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.INVITE_TO_BRIDGE_URI_IM, conferenceName, toUser);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null;
    }

    public List<MyConferenceXML> getConferenceList() throws Exception {
        String response = getConferencesXML();
        MyConferencesIO confsio = new MyConferencesIO();
        return confsio.parse(response);
    }
    
    public List<MyConferenceXML> getCachedConferenceList(String xmlCache) throws Exception {
        MyConferencesIO confsio = new MyConferencesIO();
        return confsio.parse(xmlCache);
    }
    
	public List<MyConferenceXML> getConferences(boolean useCache) throws Exception {
		File conferencesFile = SparkManager.getUserDirectory();
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		try {
			File cacheFile = new File (conferencesFile, "conferences.xml");
			String cache = null;
			if (!cacheFile.exists() || !useCache) {
				cache = RestManager.getInstance().getConferencesXML();
				outputStream = new FileOutputStream(cacheFile);
				IOUtils.write(cache, outputStream);
			} else {
				inputStream = new FileInputStream(cacheFile);
				cache = IOUtils.toString(inputStream);
			}
			return RestManager.getInstance().getCachedConferenceList(cache);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}    

    public boolean testSipAuthentication() {
        int port = Integer.parseInt(RestRes.getValue(RestRes.SIPXCONFIG_PORT));
        String uri = RestRes.getValue(RestRes.LOGIN_DETAILS_URI);
        try {
            byte[] response = invokeRestService(port, getRestServerUrl(port, true) + uri, GET, null, null);
            if (response == null) {
                config.setSipUserId(null);
                config.setPassword(null);
                config.setSipPassword(null);
                return false;
            } else {
                LoginDetailsIO xmlio = new LoginDetailsIO();
                LoginDetailsXML loginDetails = xmlio.parse(new String(response));
                config.setSipUserId(loginDetails.getUserName());
                config.setImId(loginDetails.getImId());
                config.setSipPassword(loginDetails.getSipPassword());
                config.setVideobridgeAddress(loginDetails.getVideobridgePublicIp());
            }
        } catch (Exception e) {
            Log.error("Cannot test sip authentication", e);
            return false;
        }
        return true;
    }

    public int getLast24HrsMissedCallsNo() throws Exception {
        List<Cdr> calls = getLastMonthCalls();
        int missed = 0;
        for (Cdr call : calls) {
            if (call.isMissed() && call.isLast24Hrs()) {
                missed++;
            }
        }
        return missed;
    }

    public int geUnheardVoicemailsNo() throws Exception {
        List<Voicemail> voicemails = getLastMonthVoicemails();
        int count = 0;
        for (Voicemail voicemail : voicemails) {
            if (!voicemail.isHeard()) {
                count++;
            }
        }
        return count;
    }

    public List<Cdr> getLastMonthCalls() throws Exception {
        String response = getCallsXML(Utils.getLastMonth());
        CallsIO xmlio = new CallsIO();
        List<CallXML> calls = xmlio.parse(response);
        List<Cdr> callsList = new ArrayList<Cdr>();
        if (calls == null) {
            return callsList;
        }
        for (CallXML call : calls) {
            Cdr cdr = new Cdr(config.getSipUserId(), call);
            if (!StringUtils.equals(cdr.getCallee(), cdr.getCaller())) {
                callsList.add(cdr);
            }
        }
        return callsList;
    }

    private List<ActiveCdr> getActiveCallEntries(String response) throws Exception {
        ActiveCdrsIO cdrsio = new ActiveCdrsIO();
        List<ActiveCdrXML> activeCdrsXML = cdrsio.parse(response);
        List<ActiveCdr> activeCdrs = new ArrayList<ActiveCdr>();
        for (ActiveCdrXML activeCdrXml : activeCdrsXML) {
            activeCdrs.add(new ActiveCdr(activeCdrXml));
        }
        return activeCdrs;
    }

    public List<ConferenceMemberXML> getConferenceParticipants(String conferenceName) throws Exception {
        String response = getConferenceParticipantsListXML(conferenceName);
        ConferenceDetailsIO xmlio = new ConferenceDetailsIO();
        ConferenceXML conference = xmlio.parse(response);
        return conference != null ? conference.getMembers() : new ArrayList<ConferenceMemberXML>();
    }

    public List<PhonebookEntryXML> searchPhoneBook(String searchTerm) throws Exception {
        String response = getPhoneBookSearchXML(searchTerm);//REST response
        PhonebookIO phonebookio = new PhonebookIO();
        PhonebookXML phonebook = phonebookio.parse(response);
        return phonebook.getEntries();
    }

    public PhonebookXML getFirstPhoneBookPage() throws Exception {
        PhonebookXML phonebook = getPhoneBook(0, Paginator.USERS_PER_PAGE);
        return phonebook;
    }

    public PhonebookXML getPhoneBook(int start, int stop) throws Exception {
        String response = getPhoneBookXML(start, stop);// REST response.
        PhonebookIO phonebookio = new PhonebookIO();
        PhonebookXML phonebook = phonebookio.parse(response);
        return phonebook;
    }

    public boolean markVoicemailHeard(String voicemailId) throws Exception {
        String port = RestRes.getValue(RestRes.VOICEMAIL_PORT);
        String uri = RestRes.getValue(RestRes.VOICEMAIL_MARK_HEARD, config.getSipUserId(), voicemailId);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, PUT, null, null);
        return response != null;
    }

    public boolean markVoicemailUnheard(String voicemailId) throws Exception {
        String port = RestRes.getValue(RestRes.VOICEMAIL_PORT);
        String uri = RestRes.getValue(RestRes.VOICEMAIL_MARK_HEARD, config.getSipUserId(), voicemailId);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, DELETE, null, null);
        return response != null;
    }

    public List<Voicemail> getLastMonthVoicemails() throws Exception {
        String response = getVoicemailsXML();
        VoicemailMessagesIO xmlio = new VoicemailMessagesIO();
        List<VoicemailMessageXML> messages = xmlio.parse(response);
        List<Voicemail> voicemailList = new ArrayList<Voicemail>();
        if (messages == null) {
            return voicemailList;
        }
        Voicemail vm = null;
        for (VoicemailMessageXML message : messages) {
            String uri = getVoicemailPlayUrl(message.getUsername(), message.getFolder(), message.getId());
            vm = new Voicemail(message, uri);
            if (vm.isLastMonth() && !vm.isDeleted()) {
                voicemailList.add(vm);
            }
        }
        return voicemailList;
    }

    public boolean deleteVoicemail(Voicemail voicemail) throws Exception {
    	if (voicemail.isDeleted()) {
    		return execDeleteVoicemail(voicemail.getId());
    	} else {
    		if (moveVoicemail(voicemail.getId(), Folder.DELETED)) {
    			return execDeleteVoicemail(voicemail.getId());
    		}
    	}
    	return false;
    }
    
    private boolean execDeleteVoicemail(String voicemailId) throws Exception {
        String port = RestRes.getValue(RestRes.VOICEMAIL_PORT);
        String uri = RestRes.getValue(RestRes.VOICEMAIL_DELETE, config.getSipUserId(), voicemailId);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, PUT, null, null);
        return response != null;
    }
    
    public boolean moveVoicemail(String voicemailId, Folder folder) throws Exception {
        String port = RestRes.getValue(RestRes.VOICEMAIL_PORT);
        String uri = RestRes.getValue(RestRes.VOICEMAIL_MOVE, config.getSipUserId(), voicemailId, folder.toString());
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, PUT, null, null);
        return response != null;
    }    

    private String getCallsXML(String fromDate) {
        String port = RestRes.getValue(RestRes.MISSED_CALLS_PORT);
        String uri = RestRes.getValue(RestRes.HISTORY_CALLS_URI, config.getSipUserId(), fromDate);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    /**
     *
     * Retrieves the voicemails of the current user in XML format.
     *
     * @return
     */
    private String getVoicemailsXML() {
        String port = RestRes.getValue(RestRes.VOICEMAIL_PORT);
        String uri = RestRes.getValue(RestRes.VOICEMAIL_MESSAGES_URI, config.getSipUserId());
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    /**
     * Retrieves the conferences of the current user in XML format.
     *
     * @return
     */
    public String getConferencesXML() {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_LIST_URI);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    private String getVoicemailPlayUrl(String username, String folder, String id) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.VOICEMAIL_MAILBOX, username, id);
        return getRestServerUrl(Integer.parseInt(port), true) + uri;
    }

    private String getPhoneBookXML(int start, int stop) {
        String s = String.format("%d", start).replace(",", "");
        String e = String.format("%d", stop).replace(",", "");
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.PHONE_BOOK_URI, s, e);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    private String getPhoneBookSearchXML(String searchTerm) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.PHONE_BOOK_SEARCH_URI, searchTerm);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    private String getConferenceParticipantsListXML(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_LIST_USERS, conferenceName);
        byte[] response = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return response != null ? new String(response) : null;
    }

    public boolean isLoggedIn() {
        return config.getSipUserId() != null && !config.getSipUserId().trim().isEmpty();
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confKickUser(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_KICK_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confMuteUser(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_MUTE_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confUnmuteUser(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_UNMUTE_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confDeafUser(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_DEAF_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confUndeafUser(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_UNDEAF_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confVolumeIn(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_VOLUME_IN_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    /**
     *
     * @param conferenceName - The identification of the conference.
     * @param userId - The id of the user affected by the command. The value can
     * also be "all" or "last".
     */
    public void confVolumeOut(String conferenceName, int userId) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_VOLUME_OUT_URI, conferenceName, userId);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public void confStartRecording(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_START_RECORD_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public void confStopRecording(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_STOP_RECORD_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public boolean isNoRecording(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_STATUS_URI, conferenceName);
        byte[] result = invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
        return StringUtils.contains(new String(result), NO_RECORDING_RESPONSE);
    }

    public void confKickAll(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_KICK_ALL_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public void confDeafAll(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_DEAF_ALL_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public void confUndeafAll(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_UNDEAF_ALL_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public void confMuteAll(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_MUTE_ALL_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public void confUnmuteAll(String conferenceName) {
        String port = RestRes.getValue(RestRes.SIPXCONFIG_PORT);
        String uri = RestRes.getValue(RestRes.CONFERENCE_UNMUTE_ALL_URI, conferenceName);
        invokeRestService(Integer.parseInt(port), getRestServerUrl(Integer.parseInt(port), true) + uri, GET, null, null);
    }

    public String getLatestDialedNumber() {
        return latestDialedNumber;
    }
}
