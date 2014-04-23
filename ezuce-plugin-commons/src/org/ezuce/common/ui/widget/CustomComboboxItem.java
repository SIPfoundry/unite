package org.ezuce.common.ui.widget;

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof CustomComboboxItem))
			return false;

		if (this == obj)
			return true;

		return mText.equals(((CustomComboboxItem) obj).getText());
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (mText != null ? mText.hashCode() : 0);
		return hash;
	}

}
