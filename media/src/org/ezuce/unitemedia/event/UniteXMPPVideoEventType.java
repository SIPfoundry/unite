package org.ezuce.unitemedia.event;

public enum UniteXMPPVideoEventType {
	//Channel info received from videobridge. You can now connect to received host port and start sending data
	TRANSMITTER_STREAM_INITIALIZED,
	//The channel was rejected by the receiver
	TRANSMITTER_STREAM_DENIED,
	//Videobridge acknowledged moderator that it is receiving stream data (ssrc not null and associated to moderator)
	TRANSMITTER_STREAM_STARTED,
	//Videobridge acknowledged moderator that stream stopped
	TRANSMITTER_STREAM_ENDED,
	//Channel info received from moderator and videobridge is ready to relay stream data to receiver. 
	//You can now connect to host/port received to receive stream data
	RECEIVER_STREAM_INITIALIZED,
	//Receiver is notified with stream Component (java.awt) instance. You can display stream data in a UI Frame
	RECEIVER_STREAM_STARTED,
	//Stream stopped to be sending by moderator. Receiver is notified that can remove video component from UI
	RECEIVER_STREAM_ENDED
}