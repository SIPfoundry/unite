package org.ezuce.archive;

import java.util.ArrayList;
import java.util.List;

import org.ezuce.archive.impl.server.ServerArchiveController;
import org.ezuce.archive.model.xml.Body;
import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.model.xml.Note;
import org.ezuce.archive.packet.ListPacket;
import org.ezuce.archive.packet.SaveChatPacket;
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

public class TestCollectionManagment {

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
	public void testUploadCollection() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(SaveChatPacket.class)));
				will(ServerActionsFactory.uploadCollection(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		String currentAccount = ServerActionsFactory.gWithJID;

		Chat chat = new Chat();
		chat.setWith("juliet@capulet.com/chamber");
		chat.setUTCStart("1469-07-21T02:56:15Z");
		chat.setThreadId("damduoeg08");
		chat.setSubject("She speaks!");

		List<Body> conversations = new ArrayList<Body>();
		Body body1 = new Body();
		body1.setFrom(currentAccount);
		body1.setSec(0);
		body1.setBody("Art thou not Romeo, and a Montague?");

		Body body2 = new Body();
		body2.setTo(chat.getWith());
		body2.setSec(11);
		body2.setBody("Neither, fair saint, if either thee dislike.");

		Body body3 = new Body();
		body3.setFrom(currentAccount);
		body3.setSec(7);
		body3.setBody("How cam'st thou hither, tell me, and wherefore?");

		conversations.add(body1);
		conversations.add(body2);
		conversations.add(body3);
		chat.setConversation(conversations);

		chat.setNote(new Note().from("1469-07-21T03:04:35Z",
				"I think she might fancy me."));

		mController.uploadMessages(currentAccount, chat);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
	}

	@Test
	public void testChangeSubjectOfCollection() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(SaveChatPacket.class)));
				will(ServerActionsFactory.changeSubjectOfCollection(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		String currentAccount = ServerActionsFactory.gWithJID;

		Chat chat = new Chat();
		chat.setWith("juliet@capulet.com/chamber");
		chat.setUTCStart("1469-07-21T02:56:15Z");
		chat.setSubject("She speaks!");

		mController.changeSubject(currentAccount, chat);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
	}

	@Test
	public void testListCollectionsWithLimit() throws Exception {

		// 1. preparation
		final IQ[] respRef = new IQ[1];
		context.checking(new Expectations() {
			{
				// retrieve preferences
				oneOf(mConnection).sendPacket(with(any(ListPacket.class)));
				will(ServerActionsFactory.listCollectionsWithLimit(respRef));
				oneOf(mConnection).disconnect();
			}
		});

		// 2. launch
		mController.listCollectionsWithLimit(ServerActionsFactory.gJID,
				ServerActionsFactory.gWithJID, 30);

		// 3. check the result
		Assert.assertNotNull(respRef[0]);
		System.out.println("Result XML: " + respRef[0].toXML());
		Assert.assertEquals(IQ.Type.RESULT, respRef[0].getType());
	}

}
