package com.ezuce.util;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

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
	public static JButton createButton(String normal, String pressed,
			String hover) {
		JButton btn = new JButton();
		btn.setIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + normal)));
		btn.setBorder(null);
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
		btn.setDoubleBuffered(true);
		btn.setFocusPainted(false);
		btn.setHideActionText(true);
		btn.setPressedIcon(new ImageIcon(WidgetBuilder.class
				.getResource(RESOURCES_FOLDER + pressed)));

		if (hover != null)
			btn.setRolloverIcon(new ImageIcon(WidgetBuilder.class
					.getResource(RESOURCES_FOLDER + hover)));
		return btn;
	}

	public static JButton createButton(String normal, String pressed) {
		return createButton(normal, pressed, null);
	}

	public static JButton createJButton(String title) {
		JButton btn = new JButton();
		btn.setText(title);
		return btn;
	}

	public static JLabel createLabel(String string) {
		JLabel lbl = new JLabel(string);
		return lbl;
	}

	public static JComboBox createComboBox(Object[] objects) {
		JComboBox b = new JComboBox(objects);
		return b;
	}

	public static JCheckBox createCheckbox(String string) {
		return new JCheckBox(string);
	}

	public static JSpinner createIntegerSpinner(int minMB, int maxMB,
			int value, int step) {
		SpinnerModel model = new SpinnerNumberModel(value, minMB, maxMB, step);
		JSpinner spinner = new JSpinner(model);
		return spinner;
	}

	public static JTextPane createTextPane(String contentType) {
		JTextPane pane = new JTextPane();
		pane.setContentType(contentType);
		return pane;
	}

}
