package org.ezuce.archive;

import java.util.ArrayList;
import java.util.List;

import org.ezuce.archive.impl.server.ServerArchiveController;
import org.ezuce.archive.model.Otr;
import org.ezuce.archive.model.Save;
import org.ezuce.archive.model.xml.Default;
import org.ezuce.archive.model.xml.Item;
import org.ezuce.archive.model.xml.Session;
import org.ezuce.archive.packet.ArchivePacket;
import org.ezuce.archive.packet.PreferencePacket;
import org.ezuce.archive.provider.ArchiveMessageProvider;
import org.ezuce.archive.util.MockResponseFactory;
import org.ezuce.archive.util.ServerActionsFactory;
import org.ezuce.archive.util.TestXmlUtil;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;

public class TestArchivePreferencesController {

	private XMPPConnection mConnection;
	private ServerArchiveController mController;

	// ########## set up ##########

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void setUp() throws Exception {
		mConnection = context.mock(XMPPConnection.class);
		mController = new ServerArchiveController(mConnection);
		mController.enableDebugOutput(true);
	}

	@After
	public void tearDown() throws Exception {
		mConnection.disconnect();
	}

	// ################ client sends ##################

	@Test
	public void testRetrievePreference() throws Exception {

		// 1. preparation
		final PreferencePacket[] respRef = new PreferencePacket[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection)
						.sendPacket(with(any(PreferencePacket.class)));
				will(ServerActionsFactory.retrieveArchivingPreferences(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		mController.retrievePreference();

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
		Assert.assertNotNull(respRef[0].getDefaultItem());
		Assert.assertNotNull(respRef[0].getAuto());
		Assert.assertEquals(3, respRef[0].getMethods().size());
	}

	@Test
	public void testSetDefaultMode() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection)
						.sendPacket(with(any(PreferencePacket.class)));
				will(ServerActionsFactory.setDefaultMode(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Default preference = new Default();
		preference.setOtr(Otr.prefer);
		preference.setSave(Save.False);
		mController.addPreference(ServerActionsFactory.gWithJID, preference);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
		System.out.println("Result XML: " + respRef[0].toXML());

	}

	@Test
	public void testSetModesForContact() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection)
						.sendPacket(with(any(PreferencePacket.class)));
				will(ServerActionsFactory.setModesForContact(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Item preference = new Item();
		preference.setExpire(604800l);
		preference.setJid(ServerActionsFactory.gWithJID);
		preference.setOtr(Otr.concede);
		preference.setSave(Save.body);
		mController.addPreference(ServerActionsFactory.gWithJID, preference);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
		System.out.println("Result XML: " + respRef[0].toXML());

	}

	@Test
	public void testRemoveContactSettings() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(ArchivePacket.class)));
				will(ServerActionsFactory
						.removeParticularContactSettings(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		List<Item> items = new ArrayList<Item>();
		Item item1 = new Item();
		item1.setJid("2069@ezuce.com");
		items.add(item1);
		mController.removeItems(ServerActionsFactory.gWithJID, items);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
		System.out.println("Result XML: " + respRef[0].toXML());

	}

	@Test
	public void testSetModesForChatSession() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection)
						.sendPacket(with(any(PreferencePacket.class)));
				will(ServerActionsFactory.setModesForChatSession(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Session preference = new Session();
		preference.setSave(Save.body);
		preference.setThread("ffd7076498744578d10edabfe7f4a866");
		preference.setTimeout(3600l);
		preference.setOtr(Otr.concede);
		mController.addPreference(ServerActionsFactory.gWithJID, preference);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
		System.out.println("Result XML: " + respRef[0].toXML());

	}

	@Test
	public void testRemoveChatSessionSettings() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(ArchivePacket.class)));
				will(ServerActionsFactory
						.removeParticularContactSettingsSession(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		List<Session> sessions = new ArrayList<Session>();
		Session session = new Session();
		session.setThread("2069@ezuce.com");
		sessions.add(session);
		mController.removeSessions(ServerActionsFactory.gWithJID, sessions);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
		System.out.println("Result XML: " + respRef[0].toXML());
	}

	// #################### Server pushes #######################

	@Test
	public void testServerPushesNewDefaultModes() throws Exception {

		context.checking(new Expectations() {
			{
				oneOf(mConnection).disconnect();
			}
		});

		XmlPullParser parser = TestXmlUtil
				.parseXML(MockResponseFactory.ServerPushesNewDefaultMode);

		ArchiveMessageProvider archiveMessageProvider = new ArchiveMessageProvider();
		ArchivePacket packet = (ArchivePacket) archiveMessageProvider
				.parseIQ(parser);

		Assert.assertNotNull(packet);

	}

	@Test
	public void testServerPushesNewChatSessionPrefs() throws Exception {

		context.checking(new Expectations() {
			{
				oneOf(mConnection).disconnect();
			}
		});

		XmlPullParser parser = TestXmlUtil
				.parseXML(MockResponseFactory.ServerPushesNewChatSessionPrefs);

		ArchiveMessageProvider archiveMessageProvider = new ArchiveMessageProvider();
		ArchivePacket packet = (ArchivePacket) archiveMessageProvider
				.parseIQ(parser);

		Assert.assertNotNull(packet);

	}

	@Test
	public void testClientEnablesAutoArchiving() throws Exception {
		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(ArchivePacket.class)));
				will(ServerActionsFactory.clientEnablesAutoArchiving((respRef)));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		mController.enableAutoArchiving(ServerActionsFactory.gWithJID, false);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		Assert.assertEquals(IQ.Type.ERROR, respRef[0].getType());
		System.out.println("Result XML: " + respRef[0].toXML());
	}
}
