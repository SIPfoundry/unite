package org.ezuce.desktop;

import java.awt.Dimension;

import org.ezuce.unitemedia.context.ServiceContext;
 
public class Receiver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServiceContext.start();
			
			ReceiverFrame receiverFrame1 = new ReceiverFrame("201", "11111111", 4000, 4001);
			receiverFrame1.setPreferredSize(new Dimension(640, 480));
			receiverFrame1.pack();
			receiverFrame1.setVisible(true);
			
			ReceiverFrame receiverFrame2 = new ReceiverFrame("202", "11111111", 4002, 4003);
			receiverFrame2.setPreferredSize(new Dimension(640, 480));
			receiverFrame2.pack();
			receiverFrame2.setVisible(true);
		
			ReceiverFrame receiverFrame3 = new ReceiverFrame("203", "11111111", 4004, 4005);
			receiverFrame3.setPreferredSize(new Dimension(640, 480));
			receiverFrame3.pack();
			receiverFrame3.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
}
