package org.ezuce.common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import org.ezuce.common.ui.MugshotPanel;

public final class ComponentFactory {
	private static final Color NAME_COLOR = new Color(46, 85, 102);
	private static final Color POSITION_COLOR = new Color(116, 116, 116);
	private static final Color BOTTOM_COLOR = new Color(143, 143, 143);
	private static String FONT_NAME = "Arial";
	private static int FONT_SIZE = 11;

	/**
	 * Create a label to display the name info
	 *
	 * @return
	 */
	public static JLabel createNameLabel() {
		return createlabel(NAME_COLOR, Font.BOLD);
	}

	/**
	 * Create a label to display the position info
	 *
	 * @return
	 */
	public static JLabel createPositionLabel() {
		return createlabel(POSITION_COLOR, Font.PLAIN);
	}

	/**
	 * Create a label to display the status or availability info
	 *
	 * @return
	 */
	public static JLabel createBottomLabel() {
		return createlabel(BOTTOM_COLOR, Font.PLAIN);
	}

	/**
	 * Create a label to display the status or availability info
	 *
	 * @param foreground
	 *            Text color
	 * @return
	 */
	public static JLabel createBottomLabel(Color foreground) {
		return createlabel(foreground, Font.PLAIN);
	}

	/**
	 * Create a new avatar panel
	 *
	 * @param smallSize
	 *            <code>true</code> if a small avatar is needed,
	 *            <code>false</code> otherwise
	 * @return
	 */
	public static MugshotPanel createAvatar(boolean smallSize) {
		return new MugshotPanel(smallSize);
	}

	private static JLabel createlabel(Color textColor, int fontType) {
		final JLabel label = new JLabel();
		label.setForeground(textColor);
		label.setFont(new Font(FONT_NAME, fontType, FONT_SIZE));

		return label;
	}
}
