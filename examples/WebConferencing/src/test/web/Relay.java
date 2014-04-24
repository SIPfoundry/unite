package test.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Relay {

	public static void main(String[] args) {
		if (args == null || args.length != 2) {
			System.out.println("Incorect arguments - please send 2 args: RTP port  and RTCP port");
			return;
		}
		Relay relay = new Relay();
		try {
			relay.notifyVideobridgeToRelayStream(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} catch (Exception e) {
			System.out.println("Cannot notify videobridge");
			e.printStackTrace();
		}
	}
	
	public void notifyVideobridgeToRelayStream(int rtpPort, int rtcpPort) throws Exception {
		//just send an empty packet to make sure that videobridge locks the channel you are using
		//and starts relaying the data to invitee
		System.out.println("MIRCEA send empty packets to videobridge to ensure video RELAY XX "+rtpPort);
		
		DatagramSocket ds1 = new DatagramSocket();
		DatagramPacket emptyPacket1 = new DatagramPacket(new byte[0], 0, 
				InetAddress.getByName("192.168.7.100"), rtpPort);
		ds1.send(emptyPacket1);
		
		DatagramSocket ds2 = new DatagramSocket();
		DatagramPacket emptyPacket2 = new DatagramPacket(new byte[0], 0, 
				InetAddress.getByName("192.168.7.100"), rtcpPort);
		ds2.send(emptyPacket2);
		
		while(true) {
			DatagramPacket p = new DatagramPacket(new byte [1000] , 1000);
			ds1.receive(p);
			System.out.println("RECEIVED: " + new String(p.getData()));
		}
		//ds1.close();
		//ds2.close();
		
	}

}
