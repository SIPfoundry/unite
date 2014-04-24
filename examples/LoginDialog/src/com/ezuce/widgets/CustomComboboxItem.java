package com.ezuce.widgets;

import javax.swing.Icon;

public class CustomComboboxItem {

	private Icon mIcon;
	private String mText;

	public CustomComboboxItem(Icon icon, String text) {
		this.mIcon = icon;
		this.mText = text;
	}

	public Icon getIcon() {
		return mIcon;
	}

	public String getText() {
		return mText;
	}
}
