package com.ezuce.util;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

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

	public static JButton createJButton(String title) {
		JButton btn = new JButton();
		btn.setText(title);
		return btn;
	}

	public static JLabel createJLabel(String text) {
		JLabel label = new JLabel(text);
		return label;
	}

	public static JTextField createJTextField() {
		JTextField field = new JTextField();
		return field;
	}

	public static JTextArea createJTextArea() {
		JTextArea area = new JTextArea();
		return area;
	}

	public static JTree createJTree(DefaultMutableTreeNode top) {
		JTree tree = new JTree(top);
		return tree;
	}

	public static JTextPane createJTextPane() {
		JTextPane pane = new JTextPane();
		return pane;
	}

}
