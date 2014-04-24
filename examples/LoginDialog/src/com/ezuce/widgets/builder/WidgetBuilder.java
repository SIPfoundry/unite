package com.ezuce.widgets.builder;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.ezuce.util.GraphicUtils;

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
		ImageIcon backgroundNormal = GraphicUtils.createImageIcon(normal);
		ImageIcon backgroundHover = hover == null ? backgroundNormal
				: GraphicUtils.createImageIcon(hover);
		ImageIcon backgroundPressed = GraphicUtils.createImageIcon(pressed);
		return createButton(backgroundNormal, backgroundHover,
				backgroundPressed);
	}

	public static JButton createButton(ImageIcon normal, ImageIcon pressed) {
		return createButton(normal, pressed, null);
	}

	private static JButton createButton(ImageIcon normal, ImageIcon pressed,
			ImageIcon hover) {
		if (hover == null)
			hover = normal;

		JButton btn = new JButton();
		btn.setIcon(normal);
		btn.setBorder(null);
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
		btn.setDoubleBuffered(true);
		btn.setFocusPainted(false);
		btn.setHideActionText(true);
		btn.setPressedIcon(pressed);
		btn.setRolloverIcon(hover);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		return btn;
	}

	public static JButton createJButton(String title) {
		JButton btn = new JButton();
		btn.setText(title);
		return btn;
	}

	public static JTextField createTextField() {
		JTextField field = new JTextField();
		return field;
	}

	public static JCheckBox createCheckbox(String title, ImageIcon bg,
			ImageIcon selectedBg, Font gdefaultfont, Color defaultTextColor) {
		JCheckBox b = new JCheckBox(title);
		b.setIcon(bg);
		b.setSelectedIcon(selectedBg);
		b.setOpaque(false);
		b.setFont(gdefaultfont);
		b.setForeground(defaultTextColor);
		b.setFocusPainted(false);
		return b;
	}

	public static JButton createButton(ImageIcon bg, ImageIcon bgPressed,
			ImageIcon hover, String text, Font font, Color foregroundColor) {
		ButtonBuilder builder = new ButtonBuilder();
		builder.setBackgroundNormal(bg);
		builder.setBackgroundHover(hover);
		builder.setBackgroundPressed(bgPressed);
		builder.setText(text);
		builder.setFont(font);
		builder.setForegroundColor(foregroundColor);
		return (JButton) builder.build();
	}

	public static JTextField createTextField(ImageIcon background,
			ImageIcon backgroundFocused, String defaultText, Font font,
			Color foregroundColor, Border border) {
		TextFieldBuilder builder = new TextFieldBuilder();
		builder.setBackgroundNormal(background);
		builder.setBackgroundHover(backgroundFocused);
		builder.setText(defaultText);
		builder.setFont(font);
		builder.setForegroundColor(foregroundColor);
		builder.setBorder(border);
		builder.setHeight(28);
		return (JTextField) builder.build();
	}

	public static JPasswordField createPasswordField(ImageIcon background,
			ImageIcon backgroundFocused, String defaultText, Font font,
			Color foregroundColor, Border border) {
		TextFieldBuilder builder = new TextFieldBuilder();
		builder.setPassword(true);
		builder.setBackgroundNormal(background);
		builder.setBackgroundHover(backgroundFocused);
		builder.setFont(font);
		builder.setForegroundColor(foregroundColor);
		builder.setBorder(border);
		builder.setHeight(28);
		return (JPasswordField) builder.build();
	}

	public static JComboBox createCustomCombobox(ImageIcon backgroundNormal,
			ImageIcon backgroundHover, ImageIcon backgroundUIEditor,
			ComboBoxModel dataModel, Font font, Color color,
			ImageIcon separatorIcon, ImageIcon arrowImage, boolean editable) {
		ComboboxBuilder builder = new ComboboxBuilder();
		builder.setBackgroundNormal(backgroundNormal);
		builder.setBackgroundHover(backgroundHover);
		builder.setBackgroundUIEditor(backgroundUIEditor);
		builder.setDataModel(dataModel);
		builder.setFont(font);
		builder.setForegroundColor(color);
		builder.setSeparatorIcon(separatorIcon);
		builder.setArrowImage(arrowImage);
		builder.setEditable(editable);
		return (JComboBox) builder.build();
	}

}
