package com.ezuce;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	/**  */
	private static final long serialVersionUID = -6228002598590345021L;
	private static MainWindow instance = new MainWindow();

	public static MainWindow getInstance() {
		return instance;
	}
}
