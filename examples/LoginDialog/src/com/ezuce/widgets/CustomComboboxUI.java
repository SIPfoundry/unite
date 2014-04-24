package com.ezuce.widgets;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import com.ezuce.widgets.builder.WidgetBuilder;

public class CustomComboboxUI extends BasicComboBoxUI {

	protected static final Color gBgColorDefault = new Color(248, 248, 248);
	protected static final Color gBgColorHover = new Color(240, 240, 240);
	protected static final Color gColorBorder = new Color(175, 175, 175);
	protected static final Color gColorItemBorder = new Color(210, 210, 210);
	protected ImageIcon mBtnArrowImage;
	protected ImageIcon mBackgroundUIEditor;
	protected Font mFont;
	protected ImageIcon mSeparatorIcon;
	protected Color mForegroundColor;

	public CustomComboboxUI(ImageIcon backgroundEditor,
			ImageIcon separatorImage, ImageIcon arrowImage, Font font,
			Color foregroundColor) {
		this.mBackgroundUIEditor = backgroundEditor;
		this.mFont = font;
		this.mSeparatorIcon = separatorImage;
		this.mBtnArrowImage = arrowImage;
		this.mForegroundColor = foregroundColor;
	}

	@Override
	protected JButton createArrowButton() {
		return WidgetBuilder.createButton(mBtnArrowImage, mBtnArrowImage);
	}

	@Override
	protected ListCellRenderer createRenderer() {
		return new CustomComboBoxRenderer(gBgColorDefault, gBgColorHover,
				gColorItemBorder, mSeparatorIcon, mFont);
	}

	@Override
	protected ComboBoxEditor createEditor() {
		return new CustomComboboxEditor(mBackgroundUIEditor, mSeparatorIcon,
				mFont);
	}

	@Override
	protected ComboPopup createPopup() {
		BasicComboPopup popup = (BasicComboPopup) super.createPopup();
		popup.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1,
				gColorBorder));
		popup.setBorderPainted(true);
		return popup;
	}
}
