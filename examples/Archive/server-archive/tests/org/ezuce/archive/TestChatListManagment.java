package org.ezuce.archive;

import java.util.Calendar;

import org.ezuce.archive.impl.server.ServerArchiveController;
import org.ezuce.archive.packet.RemovePacket;
import org.ezuce.archive.packet.RetrievePacket;
import org.ezuce.archive.util.ServerActionsFactory;
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

public class TestChatListManagment {

	private XMPPConnection mConnection;
	private ServerArchiveController mController;
	private String jid = ServerActionsFactory.gWithJID;

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
	public void testRetrieveMessagesFromCollectionWithLimit() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(RetrievePacket.class)));
				will(ServerActionsFactory
						.retrieveMessagesFromCollectionWithLimit(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -5);
		long start = cal.getTimeInMillis();
		mController.retrieveMessages(jid, ServerActionsFactory.gWithJID, start,
				100);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());

	}

	@Test
	public void testRemoveCollection() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(RemovePacket.class)));
				will(ServerActionsFactory.removeCollection(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Calendar cal = Calendar.getInstance();
		long end = cal.getTimeInMillis();
		cal.add(Calendar.DATE, -5);
		long start = cal.getTimeInMillis();
		mController.remove(jid, ServerActionsFactory.gWithJID, start, end);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());

	}

	@Test
	public void testRemoveAllCollection() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(RemovePacket.class)));
				will(ServerActionsFactory.removeAllCollection(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -5);
		mController.removeAll(null, cal.getTimeInMillis());

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());

	}

	@Test
	public void testRemoveCollectionBeingRecordedByServer() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(RemovePacket.class)));
				will(ServerActionsFactory
						.removeCollectionBeingRecordedByServer(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		Calendar cal = Calendar.getInstance();
		long end = cal.getTimeInMillis();
		cal.add(Calendar.DATE, -5);
		long start = cal.getTimeInMillis();
		mController
				.remove(jid, ServerActionsFactory.gWithJID, start, end, true);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());

	}

}
