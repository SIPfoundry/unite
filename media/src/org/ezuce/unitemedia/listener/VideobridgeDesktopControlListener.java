package org.ezuce.unitemedia.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import net.java.sip.communicator.service.protocol.CallPeer;
import net.java.sip.communicator.service.protocol.event.RemoteControlGrantedEvent;
import net.java.sip.communicator.service.protocol.event.RemoteControlListener;
import net.java.sip.communicator.service.protocol.event.RemoteControlRevokedEvent;

public class VideobridgeDesktopControlListener implements RemoteControlListener, KeyListener, MouseListener, MouseMotionListener{

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		System.out.println("MOUSE MOVED");
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CallPeer getCallPeer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remoteControlGranted(RemoteControlGrantedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remoteControlRevoked(RemoteControlRevokedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
