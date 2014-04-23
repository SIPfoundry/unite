package org.ezuce.common.ui.widget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class CustomEditableComboboxUI extends CustomComboboxUI {

	private ImageIcon mBackgroundHover;
	private String mDefaultText;

	public CustomEditableComboboxUI(ImageIcon backgroundEditor,
			ImageIcon backgroundHover, ImageIcon separatorImage,
			ImageIcon arrowImage, Font font, Color foregroundColor,
			String defaultText) {
		super(backgroundEditor, separatorImage, arrowImage, font,
				foregroundColor);
		this.mBackgroundHover = backgroundHover;
		this.mDefaultText = defaultText;
	}

	@Override
	protected ComboBoxEditor createEditor() {
		return new CustomEditableComboxEditor(mBackgroundUIEditor,
				mBackgroundHover, mFont, mForegroundColor, mDefaultText);
	}

	@Override
	protected ComboPopup createPopup() {
		BasicComboPopup popup = (BasicComboPopup) super.createPopup();
		popup.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0,
				gColorBorder));
		popup.setBorderPainted(true);
		return popup;
	}

}
