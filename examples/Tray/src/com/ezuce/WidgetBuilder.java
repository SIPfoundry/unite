package com.ezuce;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 * @author Vyacheslav Durin
 * 
 *         Feb 20, 2013
 * @version 0.1
 */
public class WidgetBuilder {

	private static final String RESOURCES_FOLDER = "/resources/images/";

	/**
	 * Create JToggleButton
	 * 
	 * @param picture
	 *            which represents off state
	 * @param picture
	 *            which represents on state
	 * @return JToggleButton
	 */
	public static JToggleButton createToggleButton(String off, String on) {
		JToggleButton btn = new JToggleButton();
		btn.setIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + off)));
		btn.setBorder(null);
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
		btn.setDoubleBuffered(true);
		btn.setFocusPainted(false);
		btn.setHideActionText(true);
		btn.setPressedIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + on)));
		btn.setRolloverIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + on)));
		return btn;

	}

	/**
	 * Create JButton
	 * 
	 * @param picture
	 *            which represents off state
	 * @param picture
	 *            which represents on state
	 * @return JButton
	 */
	public static JButton createButton(String off, String on) {
		JButton btn = new JButton();
		btn.setIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + off)));
		btn.setBorder(null);
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
		btn.setDoubleBuffered(true);
		btn.setFocusPainted(false);
		btn.setHideActionText(true);
		btn.setPressedIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + on)));
		btn.setRolloverIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + on)));
		return btn;
	}

}
