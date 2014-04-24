package org.ezuce.archive.util;

import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.packet.ArchivePacket;
import org.ezuce.archive.packet.ItemRemovePacket;
import org.ezuce.archive.packet.ListPacket;
import org.ezuce.archive.packet.PreferencePacket;
import org.ezuce.archive.packet.RemovePacket;
import org.ezuce.archive.packet.RetrievePacket;
import org.ezuce.archive.packet.SaveChatPacket;
import org.ezuce.archive.packet.SessionRemovePacket;
import org.ezuce.archive.provider.ArchiveMessageProvider;
import org.jivesoftware.smack.packet.IQ;
import org.jmock.api.Action;

public class ServerActionsFactory {

	// ##### set up #####
	private static ArchiveMessageProvider provider = new ArchiveMessageProvider();
	public static final String gJID = "2066@ezuce.com";
	public static final String gWithJID = "2066@ezuce.com";

	// ##### methodss ####
	public static Action retrieveArchivingPreferences(final IQ[] respRef) {
		return new MockAction<ArchivePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory.CompletePreference;
			}

			@Override
			protected boolean validatePacket(ArchivePacket packet) {
				return true;
			}
		};
	}

	public static Action setDefaultMode(final IQ[] respRef) {
		return new MockAction<PreferencePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(PreferencePacket packet) {
				if (packet.getDefaultItem() == null)
					throw new IllegalStateException(
							"Default element cannot be null");

				if (packet.getDefaultItem().getOtr() == null)
					throw new IllegalStateException(
							"Default OTR cannot be null");

				if (packet.getDefaultItem().getSave() == null)
					throw new IllegalStateException(
							"Default SAVE cannot be null");
				return true;
			}
		};
	}

	public static Action setModesForContact(final IQ[] respRef) {
		return new MockAction<PreferencePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(PreferencePacket packet) {
				if (packet.getItems().isEmpty())
					throw new IllegalStateException("Items cannot be empty");
				return true;
			}
		};
	}

	public static Action removeParticularContactSettings(final IQ[] respRef) {
		return new MockAction<ItemRemovePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(ItemRemovePacket packet) {
				if (packet.getItems().isEmpty())
					throw new IllegalStateException("Items cannot be empty");
				return true;
			}
		};
	}

	public static Action setModesForChatSession(final IQ[] respRef) {
		return new MockAction<PreferencePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(PreferencePacket packet) {
				if (packet.getSessions().isEmpty())
					throw new IllegalStateException("Sessions cannot be empty");
				return true;
			}
		};
	}

	public static Action removeParticularContactSettingsSession(
			final IQ[] respRef) {
		return new MockAction<SessionRemovePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(SessionRemovePacket packet) {
				if (packet.getSessions().isEmpty())
					throw new IllegalStateException("Sessions cannot be empty");
				return true;
			}

		};
	}

	public static Action uploadCollection(final IQ[] respRef) {
		return new MockAction<SaveChatPacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				Chat c = packet.getChat();
				String xmlResponse = MockResponseFactory
						.collectionCreatedResponseString(packet.getPacketID(),
								packet.getFrom(), c.getWith(), c.getStartUTC(),
								c.getThreadId(), c.getSubject(),
								String.valueOf(c.getVersion()));
				return xmlResponse;
			}

			@Override
			protected boolean validatePacket(SaveChatPacket packet) {
				return true;
			}
		};
	}

	public static Action changeSubjectOfCollection(final IQ[] respRef) {
		return new MockAction<SaveChatPacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				Chat c = packet.getChat();
				return MockResponseFactory.collectionUpdated(
						packet.getPacketID(), packet.getFrom(), c.getWith(),
						c.getStartUTC(), c.getThreadId(), c.getSubject(),
						String.valueOf(c.getVersion()));
			}

			@Override
			protected boolean validatePacket(SaveChatPacket packet) {
				return true;
			}
		};
	}

	public static Action clientEnablesAutoArchiving(final IQ[] respRef) {
		return new MockAction<ArchivePacket>(provider, respRef) {

			@Override
			protected boolean validatePacket(ArchivePacket packet) {
				return true;
			}

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory.AutomaticArchivingCompulsory;
			}
		};
	}

	public static Action listCollectionsWithLimit(final IQ[] respRef) {

		return new MockAction<ListPacket>(provider, respRef) {

			@Override
			protected boolean validatePacket(ListPacket packet) {
				return true;
			}

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory.EmptyListOfCollections;
			}
		};
	}

	public static Action retrieveMessagesFromCollectionWithLimit(IQ[] respRef) {
		return new MockAction<RetrievePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(RetrievePacket packet) {
				return true;
			}
		};
	}

	public static Action removeCollection(IQ[] respRef) {
		return new MockAction<RemovePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(RemovePacket packet) {
				return true;
			}
		};
	}

	public static Action removeAllCollection(IQ[] respRef) {
		return new MockAction<RemovePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(RemovePacket packet) {
				return true;
			}
		};
	}

	public static Action removeCollectionBeingRecordedByServer(IQ[] respRef) {
		return new MockAction<RemovePacket>(provider, respRef) {

			@Override
			protected String getXmlResponse() {
				return MockResponseFactory
						.createServerAcknowledgesChangeResponse(
								packet.getPacketID(), packet.getFrom()).toXML();
			}

			@Override
			protected boolean validatePacket(RemovePacket packet) {
				return true;
			}
		};
	}
}
