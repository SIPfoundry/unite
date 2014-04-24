package org.ezuce.desktop;

import java.awt.Dimension;

import org.ezuce.unitemedia.context.ServiceContext;

public class Transmitter {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServiceContext.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TransmitterFrame transmitterFrame = new TransmitterFrame();
		transmitterFrame.setVisible(true);
		transmitterFrame.setPreferredSize(new Dimension(480, 280));
		transmitterFrame.pack();
		transmitterFrame.setVisible(true);
	}
}
